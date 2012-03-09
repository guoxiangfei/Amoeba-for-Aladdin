package com.meidusa.amoeba.aladdin.net;

import java.nio.channels.SocketChannel;

import com.meidusa.amoeba.mysql.net.MysqlClientConnection;

/**
 * 客户端连接到Aladdin Proxy Server类
 * @author Li Hui
 *
 */
public class AladdinClientConnection extends MysqlClientConnection {

	/**
	 * 构造函数，继承其父类
	 * @param channel
	 * @param createStamp
	 */
	public AladdinClientConnection(SocketChannel channel, long createStamp) {
		super(channel, createStamp);
	}
	/**
	 * 得到模式（schema）
	 * 该方法目前没有实现
	 */
	public void setSchema(String schema) {
		//ignore client mysql schema
		//	this.schema = schema;
	}
}
