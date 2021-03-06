/*
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details. 
 * 	You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.meidusa.amoeba.mysql.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.meidusa.amoeba.mysql.net.CommandInfo;
import com.meidusa.amoeba.mysql.net.CommandListener;
import com.meidusa.amoeba.mysql.net.MysqlClientConnection;
import com.meidusa.amoeba.mysql.net.MysqlConnection;
import com.meidusa.amoeba.mysql.net.MysqlServerConnection;
import com.meidusa.amoeba.mysql.net.packet.EOFPacket;
import com.meidusa.amoeba.mysql.net.packet.ErrorPacket;
import com.meidusa.amoeba.mysql.net.packet.MysqlPacketBuffer;
import com.meidusa.amoeba.mysql.net.packet.OkPacket;
import com.meidusa.amoeba.mysql.net.packet.QueryCommandPacket;
import com.meidusa.amoeba.net.Connection;
import com.meidusa.amoeba.net.MessageHandler;
import com.meidusa.amoeba.net.Sessionable;
import com.meidusa.amoeba.net.packet.AbstractPacketBuffer;
import com.meidusa.amoeba.net.packet.Packet;
import com.meidusa.amoeba.net.packet.PacketBuffer;
import com.meidusa.amoeba.net.poolable.ObjectPool;
import com.meidusa.amoeba.net.poolable.PoolableObject;
import com.meidusa.amoeba.util.Reporter;
import com.meidusa.amoeba.util.StringUtil;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public abstract class CommandMessageHandler implements MessageHandler,Sessionable,Reporter.SubReporter {
	private static Logger logger = Logger.getLogger(CommandMessageHandler.class); 
	
	/**
	 * 表示服务器返回的数据包所表示当前会话状态
	 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
	 *
	 */
	static class SessionStatus{
		public static final int QUERY = 1;
		public static final int RESULT_HEAD  = 2;
		public static final int EOF_FIELDS  = 4;
		public static final int EOF_ROWS  = 8;
		public static final int OK  = 16;
		public static final int ERROR  = 32;
		public static final int COMPLETED  = 64;
	}
	
	static enum CommandStatus{
		ConnectionNotComplete,ConnectionCompleted,AllCompleted
	}
	
	/**
	 * 描述服务端连接的状态。包括当前命令的状态,当前连接的数据包
	 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
	 *
	 */
	static abstract class ConnectionStatuts{
		protected Connection conn;
		public ConnectionStatuts(Connection conn){
			this.conn = conn;
		}
		int statusCode;
		int packetIndex;
		List<byte[]> buffers;
		protected  byte commandType;
		
		public void clearBuffer(){
			if(buffers != null){
				buffers.clear();
			}
		}
		
		public void setCommandType(byte commandType){
			this.commandType = commandType;
			statusCode = 0;
			packetIndex = 0; 
		}
		/**
		 * 判断从服务器端返回得数据包是否表示当前请求的结束。
		 * @param buffer
		 * @return
		 */
		public boolean isCompleted(byte[] buffer) {
			if(this.commandType == QueryCommandPacket.COM_INIT_DB){
				boolean isCompleted = false; 
				if(MysqlPacketBuffer.isErrorPacket(buffer)){
					statusCode |= SessionStatus.ERROR;
					statusCode |= SessionStatus.COMPLETED;
					isCompleted = true;
				}else if(MysqlPacketBuffer.isOkPacket(buffer)){
					statusCode |= SessionStatus.OK;
					statusCode |= SessionStatus.COMPLETED;
					isCompleted = true;
				}
				return isCompleted;
			}else{
				return false;
			}
		}
	}
	
	protected static class CommandQueue{
		protected List<CommandInfo> sessionInitQueryQueue; //所有的从客户端发送过来的 command 队列
		protected CommandInfo currentCommand;//当前的query
		private final Lock lock = new ReentrantLock(false);
		protected Map<MysqlServerConnection,ConnectionStatuts> connStatusMap = new HashMap<MysqlServerConnection,ConnectionStatuts>();
		private boolean mainCommandExecuted;
		private MysqlClientConnection source;
		public CommandQueue(MysqlClientConnection source){
			this.source = source;
		}
		public boolean isMultiple(){
			return connStatusMap.size()>1;
		}
		
		public void clearAllBuffer(){
			Collection<ConnectionStatuts> collection = connStatusMap.values();
			for(ConnectionStatuts status : collection){
				status.clearBuffer();
			}
		}
		
		/**
		 * 尝试下一个命令，如果返回false，表示队列中没有命令了。
		 * 
		 * @return
		 */
		private boolean tryNextCommandTuple(){
			if(sessionInitQueryQueue == null){
				return false;
			}else{
				if(sessionInitQueryQueue.size()>0){
					currentCommand = sessionInitQueryQueue.get(0);
					if(logger.isDebugEnabled()){
						QueryCommandPacket command = new QueryCommandPacket();
						command.init(currentCommand.getBuffer(),source);
						logger.debug(command);
					}
					return true;
				}
				return false;
			}
		}
		
		/**
		 * 判断返回的数据是否是当前命令的结束包。
		 * 当前全部连接都全部返回以后则表示当前命令完全结束。
		 * @param conn
		 * @param buffer
		 * @return
		 */
		protected  CommandStatus checkResponseCompleted(Connection conn,byte[] buffer){
			boolean isCompleted = false;
			ConnectionStatuts connStatus = connStatusMap.get(conn);
			if(connStatus == null){
				logger.error("connection Status not Found, byffer="+StringUtil.dumpAsHex(buffer, buffer.length));
			}
			isCompleted = connStatus.isCompleted(buffer);
			connStatus.packetIndex ++;
			/**
			 * 如果是多个连接的，需要将数据缓存起来，等待命令全部完成以后，将数据进行组装，然后发送到客户端
			 * {@link #CommandMessageHandler.mergeMessageToClient}
			 */
			if(connStatus.buffers == null){
				connStatus.buffers = new ArrayList<byte[]>();
			}
			connStatus.buffers.add(buffer);
			if(isCompleted){
				lock.lock();
				try{
					if(currentCommand.getCompletedCount().incrementAndGet() == connStatusMap.size()){
						if(logger.isDebugEnabled()){
							Packet packet = null;
							if(MysqlPacketBuffer.isErrorPacket(buffer)){
								packet = new ErrorPacket();
							}else if(MysqlPacketBuffer.isEofPacket(buffer)){
								packet = new EOFPacket();
							}else if(MysqlPacketBuffer.isOkPacket(buffer)){
								packet = new OkPacket();
							}
							packet.init(buffer,conn);
							logger.debug("returned Packet:"+packet);
						}
						return CommandStatus.AllCompleted;
						
					}else{
						return CommandStatus.ConnectionCompleted;
					}
				}finally{
					lock.unlock();
				}
			}else{
				return CommandStatus.ConnectionNotComplete;
			}
		}
		
		/**
		 * 是否append 成功，如果成功则表示以前曾经堆积过，需要队列来保证发送命令的循序。
		 * 如果队列中没有堆积的命令，则返回false.
		 * 否则返回true， 则表示可直接发送命令
		 * @param commandInfo
		 * @param force 强制append 命令，返回为true
		 * @return
		 */
		public synchronized  boolean appendCommand(CommandInfo commandInfo,boolean force){
			if(force){
				if(sessionInitQueryQueue == null){
					sessionInitQueryQueue = Collections.synchronizedList(new ArrayList<CommandInfo>());
				}
				if(!sessionInitQueryQueue.contains(commandInfo)){
					sessionInitQueryQueue.add(commandInfo);
				}
				return true;
			}else{
				if(sessionInitQueryQueue == null){
					return false;
				}else{
					if(sessionInitQueryQueue.size() ==0){
						return false;
					}
					if(!sessionInitQueryQueue.contains(commandInfo)){
						sessionInitQueryQueue.add(commandInfo);
					}
					return true;
				}
			}
		}
	}
	
	protected MysqlClientConnection source;
	private boolean completed;
	private long createTime;
	private long timeout;
	private long endTime;
	private boolean ended = false;
	protected CommandQueue commandQueue;
	private ObjectPool[] pools;
	private CommandInfo info = new CommandInfo();
	protected byte commandType;
	protected Map<Connection,MessageHandler> handlerMap = new HashMap<Connection,MessageHandler>();
	private final Lock lock = new ReentrantLock(false);
	private PacketBuffer buffer = new AbstractPacketBuffer(1400);
	public CommandMessageHandler(final MysqlClientConnection source,byte[] query,ObjectPool[] pools,long timeout){
		handlerMap.put(source, source.getMessageHandler());
		source.setMessageHandler(this);
		commandQueue = new CommandQueue(source);
		QueryCommandPacket command = new QueryCommandPacket();
		command.init(query,source);
		this.pools = pools;
		info.setBuffer(query);
		info.setMain(true);
		
		this.source = source;
		this.createTime = System.currentTimeMillis();
		this.timeout = timeout;
	}
	
	/**
	 * 判断被handled的Connection 消息传送是否都完成
	 * @return
	 */
	public boolean isCompleted(){
		return completed;
	}
	
	/**
	 * 主要是为了服务端连接 与 客户端连接的环境一致（比如，当前的schema 、charset等）
	 * 
	 * 在发送主命令之前，预先需要发送一些额外的命令，比如sourceConnection、destConnection 当前的database不一致，需要发送init_db Command
	 * 为了减少复杂度，只要一个Connection需要发送命令，那么所有连接都必须发送一次相同的命令。
	 * 
	 * @param sourceMysql
	 * @param destMysqlConn
	 */
	//TODO 需要进行优化
	protected void appendPreMainCommand(){
		Set<MysqlServerConnection> connSet = commandQueue.connStatusMap.keySet();
		final MysqlConnection sourceMysql =(MysqlConnection) source;
		for(Connection destConn : connSet){
			MysqlConnection destMysqlConn = (MysqlConnection)destConn;
			if(!StringUtil.equalsIgnoreCase(sourceMysql.getSchema(), destMysqlConn.getSchema())){
				if(sourceMysql.getSchema() != null){
					QueryCommandPacket selectDBCommand = new QueryCommandPacket();
					selectDBCommand.query = sourceMysql.getSchema();
					selectDBCommand.command = QueryCommandPacket.COM_INIT_DB;
					
					byte[] buffer = selectDBCommand.toByteBuffer(destMysqlConn).array();
					CommandInfo info = new CommandInfo();
					info.setBuffer(buffer);
					info.setMain(false);
					info.setRunnable(new Runnable(){
						public void run() {
							Set<MysqlServerConnection> connSet = commandQueue.connStatusMap.keySet();
							for(Connection conn : connSet){
								((MysqlConnection)conn).setSchema(sourceMysql.getSchema());
							}
						}
					});
					commandQueue.appendCommand(info,true);
				}
			}
			
			if(sourceMysql.getCharset()!= null &&
					!StringUtil.equalsIgnoreCase(sourceMysql.getCharset(),destMysqlConn.getCharset())){
				QueryCommandPacket charsetCommand = new QueryCommandPacket();
				charsetCommand.query = "set names " + sourceMysql.getCharset();
				charsetCommand.command = QueryCommandPacket.COM_QUERY;
				
				byte[] buffer = charsetCommand.toByteBuffer(sourceMysql).array();
				CommandInfo info = new CommandInfo();
				info.setBuffer(buffer);
				info.setMain(false);
				info.setRunnable(new Runnable(){
					public void run() {
						Set<MysqlServerConnection> connSet = commandQueue.connStatusMap.keySet();
						for(Connection conn : connSet){
							((MysqlConnection)conn).setCharset(sourceMysql.getCharset());
						}
					}
				});
				commandQueue.appendCommand(info,true);
			}
			
			if(sourceMysql.isAutoCommit() != destMysqlConn.isAutoCommit()){
				QueryCommandPacket charsetCommand = new QueryCommandPacket();
				charsetCommand.query = "set autocommit = " + (sourceMysql.isAutoCommit()?1:0);
				charsetCommand.command = QueryCommandPacket.COM_QUERY;
				
				byte[] buffer = charsetCommand.toByteBuffer(sourceMysql).array();
				CommandInfo info = new CommandInfo();
				info.setBuffer(buffer);
				info.setMain(false);
				info.setRunnable(new Runnable(){
					public void run() {
						Set<MysqlServerConnection> connSet = commandQueue.connStatusMap.keySet();
						for(Connection conn : connSet){
							((MysqlConnection)conn).setAutoCommit(sourceMysql.isAutoCommit());
						}
					}
				});
				commandQueue.appendCommand(info,true);
			}
			
		}
	}
	protected void appendAfterMainCommand(){
		
	}

	/**
	 * 当连接开始一个命令的时候
	 * @param conn
	 */
	protected void startConnectionCommand(Connection conn,CommandInfo currentCommand){
		if(conn instanceof CommandListener){
			CommandListener listener = (CommandListener)conn;
			listener.startCommand(commandQueue.currentCommand);
		}
	}
	
	/**
	 * 当连接完成一个命令的时候执行，只是针对连接自己，而不是所有连接。
	 * @param conn
	 */
	protected void finishedConnectionCommand(Connection conn,CommandInfo currentCommand){
		if(conn instanceof CommandListener){
			CommandListener listener = (CommandListener) conn;
			listener.finishedCommand(this.commandQueue.currentCommand);
		}
	}
	
	public void handleMessage(Connection fromConn, byte[] message) {
		/*if(ended){
			logger.error("ended session handler handle message:\n"+StringUtil.dumpAsHex(message, message.length));
			return;
		}*/

		if(fromConn == source){
			CommandInfo info = new CommandInfo();
			info.setBuffer(message);
			info.setMain(true);
			
			if(!commandQueue.appendCommand(info,false)){
				dispatchMessageFrom(source,message);
			}
		}else{
			
			if(logger.isDebugEnabled()){
				if(MysqlPacketBuffer.isErrorPacket(message)){
					logger.error("connection="+fromConn.hashCode()+",error packet:\n"+StringUtil.dumpAsHex(message, message.length));
				}
			}
			//判断命令是否完成了
			CommandStatus commStatus = commandQueue.checkResponseCompleted(fromConn, message);
			
			if(CommandStatus.AllCompleted == commStatus || CommandStatus.ConnectionCompleted == commStatus){
				finishedConnectionCommand(fromConn,commandQueue.currentCommand);
				lock.lock();
				try{
					if(this.ended){
						releaseConnection(fromConn);
					}
				}finally{
					lock.unlock();
				}
			}
			
			if(CommandStatus.AllCompleted == commStatus){
				try{
					if(commandQueue.currentCommand.isMain()){
						commandQueue.mainCommandExecuted = true;
						releaseConnection(source);
					}

					/**
					 * 如果是客户端请求的命令则:
					 * 1、请求是多台server的，需要进行合并数据
					 * 2、单台server直接写出到客户端
					 */
					
					if(commandQueue.currentCommand.isMain()){
						if(commandQueue.isMultiple()){
							List<byte[]> list = this.mergeMessages();
							if(list != null){
								for(byte[] buffer : list){
									dispatchMessageFrom(fromConn,buffer);
								}
							}
						}else{
							dispatchMessageFrom(fromConn,message);
						}
					}else{
						//非主命令发送以后返回出错信息，则结束当前的session
						Collection<ConnectionStatuts> connectionStatutsSet = commandQueue.connStatusMap.values();
						for(ConnectionStatuts connStatus : connectionStatutsSet){
							//看是否每个服务器返回的数据包都没有异常信息。
							if((connStatus.statusCode & SessionStatus.ERROR) >0){
								this.commandQueue.currentCommand.setStatusCode(connStatus.statusCode);
								byte[] errorBuffer = connStatus.buffers.get(connStatus.buffers.size()-1);
								if(!commandQueue.mainCommandExecuted){
									dispatchMessageFrom(connStatus.conn,errorBuffer);
									if(source.isAutoCommit()){
										this.endSession();
									}
								}else{
									if(logger.isDebugEnabled()){
										byte[] commandBuffer = commandQueue.currentCommand.getBuffer();
										StringBuffer buffer = new StringBuffer();
										buffer.append("Current Command Execute Error:\n");
										buffer.append(StringUtil.dumpAsHex(commandBuffer,commandBuffer.length));
										buffer.append("\n error Packet:\n");
										buffer.append(StringUtil.dumpAsHex(errorBuffer,errorBuffer.length));
										logger.debug(buffer.toString());
									}
								}
								return;
							}
						}
					}
				}finally{
					afterCommandCompleted(commandQueue.currentCommand);
				}
			}else{
				if(commandQueue.currentCommand.isMain()){
					if(!commandQueue.isMultiple()){
						dispatchMessageFrom(fromConn,message);
					}
				}
			}
		}
	}
	
	/**
	 * 当一个命令结束的时候，清理缓存的数据包。并且尝试发送下一个command
	 * 如果队列中没有命令，则结束当前回话
	 * @param oldCommand 当前的command
	 */
	protected void afterCommandCompleted(CommandInfo oldCommand){
		if(oldCommand.getRunnable()!= null){
			oldCommand.getRunnable().run();
		}
		commandQueue.clearAllBuffer();

		//当一个命令的最后一个数据包到达，则将当前的命令从队列中删除。
		commandQueue.sessionInitQueryQueue.remove(0);
		if(!ended){
			startNextCommand();
		}
	}
	
	//判断是否需要继续发送下一条客户端命令
	//发送下一条命令
	protected synchronized void startNextCommand(){
		if(commandQueue.currentCommand != null && (commandQueue.currentCommand.getStatusCode() & SessionStatus.ERROR) >0){
			if(source.isAutoCommit()){
				this.endSession();
			}
			return;
		}
		
		if(!this.ended && commandQueue.tryNextCommandTuple()){
			commandType = commandQueue.currentCommand.getBuffer()[4];
			Collection<ConnectionStatuts> connSet = commandQueue.connStatusMap.values();
			
			boolean commandCompleted = commandQueue.currentCommand.getCompletedCount().get() == commandQueue.connStatusMap.size();
			
			if(!commandCompleted){
				for(ConnectionStatuts status : connSet){
					status.setCommandType(commandType);
					startConnectionCommand(status.conn,commandQueue.currentCommand);
				}
			}
			
			dispatchMessageFrom(source,commandQueue.currentCommand.getBuffer());
			
			if(commandCompleted){
				afterCommandCompleted(commandQueue.currentCommand);
			}
		}else{
			if(source.isAutoCommit()){
				this.endSession();
			}
		}
	}
	
	/**
	 * 任何在handler里面需要发送到目标连接的数据包，都调用该方法发送出去。
	 * 从服务器端发送过来的消息到客户端，或者从客户端发送命令到各个mysql server。
	 * 
	 * 这儿主要发送的消息有2种：
	 * 1、从客户端发送过来的消息
	 * 2、reponse当前的主要命令（是客户端发出来的命令而不是该proxy内部产生的命令）的数据包
	 * 以上2种数据包通过dispatchMessage 方法发送出去的。
	 * 由内部产生的命令数据包可以在 afterCommandCompleted()之后 根据ConnectionStatus.buffers中保存。
	 * commandQueue.clearAllBuffer() 以后buffers 将被清空
	 * 
	 * @param fromServer 是否是从mysql server 端发送过来的
	 * @param message 消息内容
	 */
	protected void dispatchMessageFrom(Connection fromConn,byte[] message){
		if(fromConn != source){
			dispatchMessageTo(source,message);
		}else{
			Collection<MysqlServerConnection> connSet =  commandQueue.connStatusMap.keySet();
			for(Connection conn : connSet){
				dispatchMessageTo(conn,message);
			}
		}
	}
	
	/**
	 * 这儿将启动一些缓存机制，避免小数据包频繁调用 系统write, CommandMessageHandler类或者其子类必须通过该方法发送数据包
	 * @param toConn
	 * @param message
	 */
	protected void dispatchMessageTo(Connection toConn,byte[] message){
		
		if(toConn == source){
			if(message != null){
				appendBufferToWrite(message,buffer,toConn,false);
			}else{
				appendBufferToWrite(message,buffer,toConn,true);
			}
		}else{
			toConn.postMessage(message);
		}
		
	}
	
	private  boolean appendBufferToWrite(byte[] byts,PacketBuffer buffer,Connection conn,boolean writeNow){
		if(byts == null){
			if(buffer.getPosition()>0){
				conn.postMessage(buffer.toByteBuffer());
				buffer.reset();
			}
			return true;
		}else{
			if(writeNow || buffer.remaining() < byts.length){
				if(buffer.getPosition()>0){
					buffer.writeBytes(byts);
					conn.postMessage(buffer.toByteBuffer());
					buffer.reset();
				}else{
					conn.postMessage(byts);
				}
				return true;
			}else{
				buffer.writeBytes(byts);
				return true;
			}
		}
	}
	
	protected void releaseConnection(Connection conn){
		lock.lock();
		try{
			MessageHandler handler = handlerMap.remove(conn);
			if(handler != null){
				conn.setMessageHandler(handler);
			}
			
			if(conn instanceof MysqlServerConnection){
				PoolableObject pooledObject = (PoolableObject)conn;
				if(pooledObject.getObjectPool() != null){
					try {
						pooledObject.getObjectPool().returnObject(conn);
						if(logger.isDebugEnabled()){
							logger.debug("connection:"+conn+" return to pool");
						}
					} catch (Exception e) {
					}
				}
			}
		}finally{
			lock.unlock();	
		}
	}
	
	/**
	 * 关闭该messageHandler 并且恢复所有这个messageHandler所handle的Connection
	 */
	protected void releaseAllCompletedConnection(){
		lock.lock();
		try{
			Set<Map.Entry<Connection,MessageHandler>> handlerSet = handlerMap.entrySet();
			for(Map.Entry<Connection,MessageHandler> entry:handlerSet){
				MessageHandler handler = entry.getValue();
				Connection connection = entry.getKey();
				ConnectionStatuts status = this.commandQueue.connStatusMap.get(connection);
				if(this.commandQueue.currentCommand == null || status != null && (status.statusCode & SessionStatus.COMPLETED)>0){
					connection.setMessageHandler(handler);
					if(!connection.isClosed()){
						if(connection instanceof MysqlServerConnection){
							PoolableObject pooledObject = (PoolableObject)connection;
							if(pooledObject.getObjectPool() != null){
								try {
									pooledObject.getObjectPool().returnObject(connection);
									if(logger.isDebugEnabled()){
										logger.debug("connection:"+connection+" return to pool");
									}
								} catch (Exception e) {
								}
							}
						}
					}
				}
			}
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * 合并多服务端的消息，发送到客户端
	 * 只有在多连接的情况下需要进行数据包聚合，聚合以后逐一将数据包通过 {@link #dispatchMessageFrom(Connection, byte[])}方法发送出去,
	 * 一对一的连接直接通过{@link #dispatchMessageFrom(Connection, byte[])} 方法 直接发送出去,而不需要merge。
	 * @return
	 */
	protected List<byte[]> mergeMessages(){
		Collection<ConnectionStatuts> connectionStatutsSet = commandQueue.connStatusMap.values();
		boolean isSelectQuery = true;
		List<byte[]> buffers = null;
		List<byte[]> returnList = new ArrayList<byte[]>();
		for(ConnectionStatuts connStatus : connectionStatutsSet){
			//看是否每个服务器返回的数据包都没有异常信息。
			byte[] buffer = connStatus.buffers.get(connStatus.buffers.size()-1);
			buffers = connStatus.buffers;
			if((connStatus.statusCode & SessionStatus.ERROR) >0){
				return buffers;
			}
			if(isSelectQuery){
				isSelectQuery =isSelectQuery && MysqlPacketBuffer.isEofPacket(buffer);
			}
		}
		
		if(isSelectQuery){
			//当前的packetId
			byte paketId = 0;
			
			//发送field信息
			for(byte[] buffer : buffers){
				if(MysqlPacketBuffer.isEofPacket(buffer)){
					returnList.add(buffer);
					paketId = buffer[3];
					break;
				}else{
					returnList.add(buffer);
					paketId = buffer[3];
				}
			}
			paketId += 1;
			//发送rows数据包
			for(ConnectionStatuts connStatus : connectionStatutsSet){
				boolean rowStart = false;;
				for(byte[] buffer : connStatus.buffers){
					if(!rowStart){
						if(MysqlPacketBuffer.isEofPacket(buffer)){
							rowStart = true;
						}else{
							continue;
						}
					}else{
						if(!MysqlPacketBuffer.isEofPacket(buffer)){
							buffer[3] = paketId;
							paketId += 1;
							returnList.add(buffer);
						}
					}
				}
			}
			
			byte[] eofBuffer = buffers.get(buffers.size()-1);
			eofBuffer[3] = paketId;
			returnList.add(eofBuffer);
		}else{
			OkPacket ok = new OkPacket();
			StringBuffer strbuffer = new StringBuffer();
			for(ConnectionStatuts connStatus : connectionStatutsSet){
				byte[] buffer = connStatus.buffers.get(connStatus.buffers.size()-1);
				OkPacket connOK = new OkPacket();
				connOK.init(buffer,connStatus.conn);
				ok.affectedRows +=connOK.affectedRows;
				ok.insertId =connOK.insertId;
				ok.packetId = 1;
				strbuffer.append(connOK.message);
				ok.warningCount +=connOK.warningCount;
			}
			ok.message = strbuffer.toString();
			returnList.add(ok.toByteBuffer(source).array());
		}
		return returnList;
	}

	protected abstract ConnectionStatuts newConnectionStatuts(Connection conn);

	public synchronized void startSession() throws Exception {
		if(logger.isInfoEnabled()){
			logger.info(this+" session start");
		}
		for(ObjectPool pool:pools){
			MysqlServerConnection conn;
			conn = (MysqlServerConnection)pool.borrowObject();
			handlerMap.put(conn, conn.getMessageHandler());
			conn.setMessageHandler(this);
			commandQueue.connStatusMap.put(conn, newConnectionStatuts(conn));
		}
		appendPreMainCommand();
		this.commandQueue.appendCommand(info, true);
		appendAfterMainCommand();
		startNextCommand();
	}
	
	public boolean checkIdle(long now) {
		if(timeout >0){
			return (now - createTime)>timeout;
		}else{
			if(ended){
				/**
				 * 如果该session已经结束，此时如果serverConnection端还在等待所有数据访问。并且超过15s,则需要当空闲的会话
				 * 避免由于各种原因造成服务器端没有发送数据或者已经结束的会话而ServerConnection无法返回Pool中。
				 */
				return (now - endTime)>15000;
			}
			return false;
		}
	}

	public void endSession() {
		lock.lock();
		try{
			if(!ended){
				endTime = System.currentTimeMillis();
				ended = true;
				this.releaseAllCompletedConnection();
				if(!this.commandQueue.mainCommandExecuted){
					ErrorPacket error = new ErrorPacket();
					error.errno = 10000;
					error.packetId = 2;
					error.serverErrorMessage = "session was killed!!";
					this.dispatchMessageTo(source, error.toByteBuffer(source).array());
					logger.warn("session was killed!!",new Exception());
					source.postClose(null);
				}else{
					if(logger.isInfoEnabled()){
						logger.info(this+" session ended.");
					}
				}
				this.dispatchMessageTo(this.source, null);
			}
		}finally{
			lock.unlock();
		}
	}
	

	public boolean isEnded() {
		lock.lock();
		try{
			return this.ended;
		}finally{
			lock.unlock();
		}
	}
	
	public void appendReport(StringBuilder buffer, long now, long sinceLast,boolean reset,Level level) {
		buffer.append("    -- MessageHandler:").append("multiple Size:").append(commandQueue.connStatusMap.size());
		if(commandQueue.currentCommand != null){
			buffer.append(",currentCommand completedCount:");
			buffer.append(commandQueue.currentCommand.getCompletedCount()).append("\n");
		}else{
			buffer.append("\n");
		}
	}

}
