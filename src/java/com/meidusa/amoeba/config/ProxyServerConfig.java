/**
 * <pre>
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details. 
 * 	You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * </pre>
 */
package com.meidusa.amoeba.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ���ڱ���amoeba.xml�е�ȫ����Ϣ
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 * @author hexianmao
 */
public class ProxyServerConfig {

    private String                              ipAddress;
    private int                                 port                     = 8066;
    private String                              user;
    private String                              password;
    private int                                 readThreadPoolSize       = 16;
    private int                                 clientSideThreadPoolSize = 16;
    private int                                 serverSideThreadPoolSize = 16;
    private boolean                             tcpNoDelay               = false;
    private int                                 netBufferSize            = 16;
    
    /**
     * Ĭ����û��ֵ��
     */
    private String                              serverCharset            = null;

    private Map<String, BeanObjectEntityConfig> managers                 = new HashMap<String, BeanObjectEntityConfig>();
    private Map<String, BeanObjectEntityConfig> unmodifiableManagers     = Collections.unmodifiableMap(managers);
    /**
     * dbServers��Ӧamoeba.xml�е�dbServerList��㣬���ڱ������е�dbServer
     */
    private Map<String, DBServerConfig>         dbServers                = new HashMap<String, DBServerConfig>();
    private Map<String, DBServerConfig>         unmodifiableDbServers    = Collections.unmodifiableMap(dbServers);

    private BeanObjectEntityConfig              queryRouterConfig;

    public void addManager(String name, BeanObjectEntityConfig managerConfig) {
        managers.put(name, managerConfig);
    }

    public Map<String, BeanObjectEntityConfig> getManagers() {
        return unmodifiableManagers;
    }

    public Map<String, DBServerConfig> getDbServers() {
        return unmodifiableDbServers;
    }
    /**
     * �ѵ�ǰ�����dbServer put��dbServers��
     * @param name dbServer�������ƣ�Ʃ��dbServer1
     * @param serverConfig dbServer����ȫ����Ϣ��Ʃ��dbServer1��ȫ����Ϣ
     */
    public void addServer(String name, DBServerConfig serverConfig) {
        dbServers.put(name, serverConfig);
    }
    /**
     * �õ�amoeba��ip����amoeba.xml�д洢
     * @return ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }
    /**
     * ����Ա����ipAddress��ֵ
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    /**
     * �õ�amoeba�Ķ˿ں�port,��amoeba.xml�д洢
     * @return port
     */
    public int getPort() {
        return port;
    }
    /**
     * ����Ա����port��ֵ
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }
    /**
     * ����amoeba��readThreadPoolSizeֵ����amoeba.xml������
     * @return
     */
    public int getReadThreadPoolSize() {
        return readThreadPoolSize;
    }
    /**
     * ����Ա����readThreadPoolSize��ֵ
     * @param readThreadPoolSize
     */
    public void setReadThreadPoolSize(int readThreadPoolSize) {
        this.readThreadPoolSize = readThreadPoolSize;
    }
    /**
     * ����amoeba��serverSideThreadPoolSizeֵ����amoeba.xml������
     * @return serverSideThreadPoolSize
     */
    public int getServerSideThreadPoolSize() {
        return serverSideThreadPoolSize;
    }
    /**
     * ����Ա����serverSideThreadPoolSize��ֵ
     * @param serverSideThreadPoolSize
     */
    public void setServerSideThreadPoolSize(int serverSideThreadPoolSize) {
        this.serverSideThreadPoolSize = serverSideThreadPoolSize;
    }
    /**
     * ����amoeba��clientSideThreadPoolSizeֵ����amoeba.xml������
     * @return clientSideThreadPoolSize
     */
    public int getClientSideThreadPoolSize() {
        return clientSideThreadPoolSize;
    }
    /**
     * ����Ա����clientSideThreadPoolSize��ֵ
     * @param clientSideThreadPoolSize
     */
    public void setClientSideThreadPoolSize(int clientSideThreadPoolSize) {
        this.clientSideThreadPoolSize = clientSideThreadPoolSize;
    }
    /**
     * ����Amoeba Proxy Server��user����amoeba.xml������
     * @return
     */
    public String getUser() {
        return user;
    }
    /**
     * ����Ա����user��ֵ
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * ����amoeba��password,��amoeba.xml������
     * @return
     */
    public String getPassword() {
        return password;
    }
    /**
     * ����Ա����password��ֵ
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * ���س�Ա����queryRouterConfig
     * queryRouterConfig ���ڼ�¼amoeba.xml��queryRouter������Ϣ
     * @return
     */
    public BeanObjectEntityConfig getQueryRouterConfig() {
        return queryRouterConfig;
    }
    /**
     * queryRouterConfig ���ڼ�¼amoeba.xml��queryRouter������Ϣ
     * @param queryRouterConfig
     */
    public void setQueryRouterConfig(BeanObjectEntityConfig queryRouterConfig) {
        this.queryRouterConfig = queryRouterConfig;
    }
    /**
     * ����amoeba��netBufferSize����amoeba.xml������
     * @return
     */
    public int getNetBufferSize() {
        return netBufferSize;
    }
    /**
     * ����Ա����netBufferSize��ֵ
     * @param netBufferSize
     */
    public void setNetBufferSize(int netBufferSize) {
        this.netBufferSize = netBufferSize;
    }
    /**
     * tcpNoDelay�ж�tcp�Ƿ���Ҫ�ȴ�������amoeba��tcpNoDelay����amoeba.xml������
     * @return
     */
    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }
    /**
     * ����Ա����tcpNoDelay��ֵ
     * @param tcpNoDelay
     */
    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }
    /**
     * ����amoeba��serverCharset����amoeba.xml������
     * @return
     */
    public String getServerCharset() {
        return serverCharset;
    }
    /**
     * ����Ա����serverCharset��ֵ
     * @param serverCharset
     */
    public void setServerCharset(String serverCharset) {
        this.serverCharset = serverCharset;
    }

}
