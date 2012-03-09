package com.meidusa.amoeba.aladdin.net;

import java.nio.channels.SocketChannel;

import com.meidusa.amoeba.mysql.net.MysqlClientConnection;

/**
 * �ͻ������ӵ�Aladdin Proxy Server��
 * @author Li Hui
 *
 */
public class AladdinClientConnection extends MysqlClientConnection {

	/**
	 * ���캯�����̳��丸��
	 * @param channel
	 * @param createStamp
	 */
	public AladdinClientConnection(SocketChannel channel, long createStamp) {
		super(channel, createStamp);
	}
	/**
	 * �õ�ģʽ��schema��
	 * �÷���Ŀǰû��ʵ��
	 */
	public void setSchema(String schema) {
		//ignore client mysql schema
		//	this.schema = schema;
	}
}
