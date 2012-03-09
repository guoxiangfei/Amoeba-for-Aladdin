package com.meidusa.amoeba.aladdin.net;

import java.io.IOException;

import com.meidusa.amoeba.aladdin.handler.AladdinMessageDispatcher;
import com.meidusa.amoeba.mysql.net.MysqlClientConnectionManager;
import com.meidusa.amoeba.mysql.net.packet.OkPacket;
import com.meidusa.amoeba.net.AuthResponseData;
import com.meidusa.amoeba.net.Connection;
/**
 * �ͻ������ӵ�Aladdin Proxy Server�����ӹ�����
 * Aladdin Proxy�е�һЩ������Ϣ������ip,port name��
 * @author Li Hui
 *
 */
public class AladdinClientConnectionManager extends MysqlClientConnectionManager {

    private static byte[] authenticateOkPacketData;
    /**
     * ���캯��
     * û��ʵ��
     * @throws IOException
     */
    public AladdinClientConnectionManager() throws IOException{
    }
    /**
     * ���캯��
     * ֱ�ӵ��ø���Ĺ��캯��
      * @param name  ��Aladdin proxy Server��
     * @param port Aladdin proxy��ip port
     * @throws IOException
     */
    public AladdinClientConnectionManager(String name, int port) throws IOException{
        super(name, port);
    }
    /**
     * ���캯��
     * ֱ�ӵ��ø���Ĺ��캯��
     * @param name  ��Aladdin proxy Server��
     * @param ipAddress Aladdin proxy��ip��ַ
     * @param port Aladdin proxy��ip port
     * @throws IOException
     */
    public AladdinClientConnectionManager(String name, String ipAddress, int port) throws IOException{
        super(name, ipAddress, port);
    }
    
    public void connectionAuthenticateSuccess(Connection conn, AuthResponseData data) {
    	//�� project.log��д��־����������
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
