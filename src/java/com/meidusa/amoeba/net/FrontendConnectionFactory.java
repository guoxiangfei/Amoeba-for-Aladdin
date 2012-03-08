package com.meidusa.amoeba.net;

/**
 * ��Ϊǰ�����ݿ����ӹ���
 * Ӧ�ó�����Amoeba Proxy Server���ӵ�һЩ������Ϣ��username,pwd
 * @author struct
 * @author Li Hui
 *
 */
public abstract class FrontendConnectionFactory extends AbstractConnectionFactory {
	/**
	 * Amoeba Proxy Server��user
	 */
	protected String user;
	/**
	 * Amoeba Proxy Server��password
	 */
	protected String password;
	
	/**
	 * 
	 * @return user Amoeba Proxy Server��user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * Amoeba Proxy Server��user
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * 
	 * @return password Amoeba Proxy Server��password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Amoeba Proxy Server��password
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
