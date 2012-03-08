package com.meidusa.amoeba.aladdin.net;

import java.nio.channels.SocketChannel;

import com.meidusa.amoeba.net.Connection;
import com.meidusa.amoeba.net.FrontendConnectionFactory;

/**
 * Aladdin Proxy Server的连接工厂
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
	 * 源程序里面AladdinClientConnectionFactory类以及其父类都没有构造函数
	 * @author Li Hui
	 */
//	public AladdinClientConnectionFactory()
//	{
//	
//	}
}
