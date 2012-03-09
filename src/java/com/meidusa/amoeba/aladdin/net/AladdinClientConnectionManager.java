package com.meidusa.amoeba.aladdin.net;

import java.io.IOException;

import com.meidusa.amoeba.aladdin.handler.AladdinMessageDispatcher;
import com.meidusa.amoeba.mysql.net.MysqlClientConnectionManager;
import com.meidusa.amoeba.mysql.net.packet.OkPacket;
import com.meidusa.amoeba.net.AuthResponseData;
import com.meidusa.amoeba.net.Connection;
/**
 * 客户端连接到Aladdin Proxy Server的连接管理类
 * Aladdin Proxy中的一些基本信息，包括ip,port name等
 * @author Li Hui
 *
 */
public class AladdinClientConnectionManager extends MysqlClientConnectionManager {

    private static byte[] authenticateOkPacketData;
    /**
     * 构造函数
     * 没有实现
     * @throws IOException
     */
    public AladdinClientConnectionManager() throws IOException{
    }
    /**
     * 构造函数
     * 直接调用父类的构造函数
      * @param name  “Aladdin proxy Server”
     * @param port Aladdin proxy的ip port
     * @throws IOException
     */
    public AladdinClientConnectionManager(String name, int port) throws IOException{
        super(name, port);
    }
    /**
     * 构造函数
     * 直接调用父类的构造函数
     * @param name  “Aladdin proxy Server”
     * @param ipAddress Aladdin proxy的ip地址
     * @param port Aladdin proxy的ip port
     * @throws IOException
     */
    public AladdinClientConnectionManager(String name, String ipAddress, int port) throws IOException{
        super(name, ipAddress, port);
    }
    
    public void connectionAuthenticateSuccess(Connection conn, AuthResponseData data) {
    	//向 project.log中写日志，类似下面
    	//2012-03-04 20:56:28,625 INFO  net.AladdinClientConnection - Connection Authenticate success [ conn=com.meidusa.amoeba.aladdin.net.AladdinClientConnection@175.186.52.154:3268,hashcode=13729475].
        if (logger.isInfoEnabled()) {
            logger.info("Connection Authenticate success [ conn=" + conn + "].");
        }
        if (authenticateOkPacketData == null) {
            OkPacket ok = new OkPacket();
            ok.packetId = 2;
            ok.affectedRows = 0;
            ok.insertId = 0;
            ok.serverStatus = 2;
            ok.warningCount = 0;
            authenticateOkPacketData = ok.toByteBuffer(conn).array();
        }
        conn.setMessageHandler(new AladdinMessageDispatcher());
        conn.postMessage(authenticateOkPacketData);
    }
}
