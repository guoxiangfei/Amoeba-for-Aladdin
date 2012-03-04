package com.meidusa.amoeba.aladdin.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	private static String filename="test";
	
//	public static void main(String args[]){
//		System.out.println("Hello Log");
//	}
	
	/**
     * �õ�Ҫ��¼����־��·�����ļ�����
     * @return
     */
    private static String getLogName() {
        StringBuffer logPath = new StringBuffer();
        logPath.append(System.getProperty("user.home"));
        logPath.append("\\"+filename);
        File file = new File(logPath.toString());
        if (!file.exists())
            file.mkdir();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logPath.append("\\"+sdf.format(new Date())+".log");
        System.out.println(logPath.toString());
        return logPath.toString();
    }
    
    /**
     * ����Logger���������־�ļ�·��
     * @param logger 
     * @throws SecurityException
     * @throws IOException
     */
    public static void setLogingProperties(Logger logger) throws SecurityException, IOException {
        setLogingProperties(logger,Level.ALL);
    }
    
    /**
     * ����Logger���������־�ļ�·��
     * @param logger
     * @param level ����־�ļ������level�������ϵ���Ϣ
     * @throws SecurityException
     * @throws IOException
     */
    public static void setLogingProperties(Logger logger,Level level) {
        FileHandler fh;
        try {
            fh = new FileHandler(getLogName(),true);
            logger.addHandler(fh);//��־����ļ�
            //logger.setLevel(level);
            fh.setFormatter(new SimpleFormatter());//�����ʽ
            //logger.addHandler(new ConsoleHandler());//���������̨
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "��ȫ�Դ���", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"��ȡ�ļ���־����", e);
        }
    }
    
    public static void main(String [] args) {
        Logger logger = Logger.getLogger("sgg");
        try {
            Log.setLogingProperties(logger);
            logger.log(Level.INFO, "ddddd");
            logger.log(Level.INFO, "eeeeee");
            logger.log(Level.INFO, "ffffff");
            logger.log(Level.INFO, "gggggg");
            logger.log(Level.INFO, "hhhhhh");
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
