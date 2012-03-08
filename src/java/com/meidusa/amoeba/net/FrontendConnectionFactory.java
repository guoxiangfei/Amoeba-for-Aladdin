package com.meidusa.amoeba.net;

/**
 * 作为前端数据库连接工厂
 * 应用程序与Amoeba Proxy Server连接的一些基本信息，username,pwd
 * @author struct
 * @author Li Hui
 *
 */
public abstract class FrontendConnectionFactory extends AbstractConnectionFactory {
	/**
	 * Amoeba Proxy Server的user
	 */
	protected String user;
	/**
	 * Amoeba Proxy Server的password
	 */
	protected String password;
	
	/**
	 * 
	 * @return user Amoeba Proxy Server的user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * Amoeba Proxy Server的user
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * 
	 * @return password Amoeba Proxy Server的password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Amoeba Proxy Server的password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	protected void initConnection(Connection connection){
		super.initConnection(connection);
		if(connection instanceof AuthingableConnection){
			AuthingableConnection conn = (AuthingableConnection)connection;
			conn.setUser(user);
			conn.setPassword(password);
		}
	}
	

}
