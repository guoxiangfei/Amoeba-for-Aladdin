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
 * 用于保存amoeba.xml中的全部信息
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
     * 默认是没有值的
     */
    private String                              serverCharset            = null;

    private Map<String, BeanObjectEntityConfig> managers                 = new HashMap<String, BeanObjectEntityConfig>();
    private Map<String, BeanObjectEntityConfig> unmodifiableManagers     = Collections.unmodifiableMap(managers);
    /**
     * dbServers对应amoeba.xml中的dbServerList结点，用于保存所有的dbServer
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
     * 把当前考察的dbServer put到dbServers中
     * @param name dbServer结点的名称，譬如dbServer1
     * @param serverConfig dbServer结点的全部信息，譬如dbServer1的全部信息
     */
    public void addServer(String name, DBServerConfig serverConfig) {
        dbServers.put(name, serverConfig);
    }
    /**
     * 得到amoeba的ip，在amoeba.xml中存储
     * @return ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }
    /**
     * 给成员变量ipAddress赋值
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    /**
     * 得到amoeba的端口号port,在amoeba.xml中存储
     * @return port
     */
    public int getPort() {
        return port;
    }
    /**
     * 给成员变量port赋值
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }
    /**
     * 返回amoeba的readThreadPoolSize值，在amoeba.xml中配置
     * @return
     */
    public int getReadThreadPoolSize() {
        return readThreadPoolSize;
    }
    /**
     * 给成员变量readThreadPoolSize赋值
     * @param readThreadPoolSize
     */
    public void setReadThreadPoolSize(int readThreadPoolSize) {
        this.readThreadPoolSize = readThreadPoolSize;
    }
    /**
     * 返回amoeba的serverSideThreadPoolSize值，在amoeba.xml中配置
     * @return serverSideThreadPoolSize
     */
    public int getServerSideThreadPoolSize() {
        return serverSideThreadPoolSize;
    }
    /**
     * 给成员变量serverSideThreadPoolSize赋值
     * @param serverSideThreadPoolSize
     */
    public void setServerSideThreadPoolSize(int serverSideThreadPoolSize) {
        this.serverSideThreadPoolSize = serverSideThreadPoolSize;
    }
    /**
     * 返回amoeba的clientSideThreadPoolSize值，在amoeba.xml中配置
     * @return clientSideThreadPoolSize
     */
    public int getClientSideThreadPoolSize() {
        return clientSideThreadPoolSize;
    }
    /**
     * 给成员变量clientSideThreadPoolSize赋值
     * @param clientSideThreadPoolSize
     */
    public void setClientSideThreadPoolSize(int clientSideThreadPoolSize) {
        this.clientSideThreadPoolSize = clientSideThreadPoolSize;
    }
    /**
     * 返回Amoeba Proxy Server的user，在amoeba.xml中配置
     * @return
     */
    public String getUser() {
        return user;
    }
    /**
     * 给成员变量user赋值
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * 返回amoeba的password,在amoeba.xml中配置
     * @return
     */
    public String getPassword() {
        return password;
    }
    /**
     * 给成员变量password赋值
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * 返回成员变量queryRouterConfig
     * queryRouterConfig 用于记录amoeba.xml中queryRouter结点的信息
     * @return
     */
    public BeanObjectEntityConfig getQueryRouterConfig() {
        return queryRouterConfig;
    }
    /**
     * queryRouterConfig 用于记录amoeba.xml中queryRouter结点的信息
     * @param queryRouterConfig
     */
    public void setQueryRouterConfig(BeanObjectEntityConfig queryRouterConfig) {
        this.queryRouterConfig = queryRouterConfig;
    }
    /**
     * 返回amoeba的netBufferSize，在amoeba.xml中配置
     * @return
     */
    public int getNetBufferSize() {
        return netBufferSize;
    }
    /**
     * 给成员变量netBufferSize赋值
     * @param netBufferSize
     */
    public void setNetBufferSize(int netBufferSize) {
        this.netBufferSize = netBufferSize;
    }
    /**
     * tcpNoDelay判断tcp是否需要等待，设置amoeba的tcpNoDelay，在amoeba.xml中配置
     * @return
     */
    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }
    /**
     * 给成员变量tcpNoDelay赋值
     * @param tcpNoDelay
     */
    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }
    /**
     * 设置amoeba的serverCharset，在amoeba.xml中配置
     * @return
     */
    public String getServerCharset() {
        return serverCharset;
    }
    /**
     * 给成员变量serverCharset赋值
     * @param serverCharset
     */
    public void setServerCharset(String serverCharset) {
        this.serverCharset = serverCharset;
    }

}
