package com.meidusa.amoeba.aladdin.net;

import java.nio.channels.SocketChannel;

import com.meidusa.amoeba.net.Connection;
import com.meidusa.amoeba.net.FrontendConnectionFactory;

/**
 * Aladdin Proxy Server�����ӹ���
 * @author Li Hui
 *
 */
public class AladdinClientConnectionFactory extends FrontendConnectionFactory {

	@Override
	protected Connection newConnectionInstance(SocketChannel channel,
			long createStamp) {
		return new AladdinClientConnection(channel,createStamp);
	}
	/**
	 * Դ��������AladdinClientConnectionFactory���Լ��丸�඼û�й��캯��
	 * @author Li Hui
	 */
//	public AladdinClientConnectionFactory()
//	{
//	
//	}
}
