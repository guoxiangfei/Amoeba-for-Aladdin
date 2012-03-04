package com.meidusa.amoeba.aladdin.net;

import java.io.IOException;

import com.meidusa.amoeba.aladdin.handler.AladdinMessageDispatcher;
import com.meidusa.amoeba.mysql.net.MysqlClientConnectionManager;
import com.meidusa.amoeba.mysql.net.packet.OkPacket;
import com.meidusa.amoeba.net.AuthResponseData;
import com.meidusa.amoeba.net.Connection;

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
     * @param name 名称？？？目前不知道这个名称指代什么
     * @param port 端口号
     * @throws IOException
     */
    public AladdinClientConnectionManager(String name, int port) throws IOException{
        super(name, port);
    }
    /**
     * 构造函数
     * 直接调用父类的构造函数
     * @param name  名称？？？目前不知道这个名称指代什么
     * @param ipAddress ip地址
     * @param port 端口号
     * @throws IOException
     */
    public AladdinClientConnectionManager(String name, String ipAddress, int port) throws IOException{
        super(name, ipAddress, port);
    }

    public void connectionAuthenticateSuccess(Connection conn, AuthResponseData data) {
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
