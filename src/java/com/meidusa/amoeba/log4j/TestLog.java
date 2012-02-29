package com.meidusa.amoeba.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

 

public class TestLog {
   static Logger logger = Logger.getLogger(TestLog.class); //First step

   public static void main(String args[]) {
      PropertyConfigurator.configure("log4j.properties"); //Second step
      logger.debug("Here is some DEBUG"); //Third step
      logger.info("Here is some INFO");
      logger.warn("Here is some WARN");
      logger.error("Here is some ERROR");
      logger.fatal("Here is some FATAL");
   }
}
