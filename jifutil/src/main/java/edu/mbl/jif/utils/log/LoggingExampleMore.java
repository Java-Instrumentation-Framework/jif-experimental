/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.utils.log;

/**
 *
 * @author GBH
 */

/*
 Java Logging Sample

(Default logging.properties file is in %JAVA_HOME%\\jre\\lib\\logging.properties)
logging.properties file.
---------------------------------------------------------------------

handlers= java.util.logging.FileHandler, java.util.logging.ConsoleHandler

.level= ALL

java.util.logging.FileHandler.pattern= c:\\temp\\test%u.log
java.util.logging.FileHandler.limit= 50000
java.util.logging.FileHandler.count= 1
java.util.logging.FileHandler.append= true
java.util.logging.FileHandler.formatter= my.test.SimpleFormatter

java.util.logging.ConsoleHandler.level= ALL
java.util.logging.ConsoleHandler.formatter= my.test.SimpleFormatter

*/

public class LoggingExampleMore {

}
/*
import java.util.logging.*;
import java.io.*;

public class LogConfig
{
    private static final String FQCN   = LogConfig.class.getName();
    private static final Logger logger = Logger.getLogger(FQCN);

  private String logConfigFileName;
    
    public LogConfig(String logConfigFileName) {
        if(logConfigFileName == null) 
            throw new IllegalArgumentException("logConfigFileName is null");
        
        logConfigFileName = logConfigFileName.trim();
        
        if(logConfigFileName.length() < 1) 
            throw new IllegalArgumentException("logConfigFileName is empty");

        this.logConfigFileName = logConfigFileName;
    }
    
    public void refresh() {
        final String method="refresh";

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(logConfigFileName);
            LogManager lm = LogManager.getLogManager();
            lm.reset();
            lm.readConfiguration(fis);
        } catch(Exception e) {
            logger.logp(Level.WARNING, FQCN, method, "Exception occurred in configuring log. Using default configuration.", e);
        } finally {
            if(fis!=null)try{fis.close();}catch(IOException e){logger.throwing(FQCN, method, e);}
        }
    }
}

package my.test;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.text.SimpleDateFormat;


public class SimpleFormatter extends Formatter
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    private static final String LF = System.getProperty("line.separator");

    public String format(LogRecord record) {
        StringBuffer sb  = new StringBuffer(500);
        String       tmp = null;
        
        Date date  = new Date(record.getMillis());
        sb.append(DATE_FORMAT.format(date));

        sb.append(‘ ’); sb.append(record.getThreadID());
        sb.append(‘ ’); sb.append(record.getSequenceNumber());
        sb.append(‘ ’); sb.append(record.getLevel().toString());

        tmp = record.getLoggerName();
        if(tmp != null) {
            sb.append(‘ ’); 
            sb.append(tmp);
        }

        tmp = record.getSourceMethodName();
        if(tmp != null) { 
            sb.append(‘.’); 
            sb.append(tmp);
        }
        
        if(record.getMessage() != null) { 
            sb.append(" - "); 
            sb.append(formatMessage(record)); 
        }

        Throwable th = record.getThrown();
        if(th!=null) {
            StringWriter sw = new StringWriter();
            PrintWriter  pw = new PrintWriter(sw);
            th.printStackTrace(pw);
            sb.append(LF);sb.append(sw);
            pw.close();
        } else {
            sb.append(LF);
        }
        
        return sb.toString();
    }
}  

package my.test;

import java.util.logging.*;
import java.io.IOException;

public class Test 
{
    private static final String FQCN   = Test.class.getName();
    private static final Logger logger = Logger.getLogger(FQCN);

    private static final LogConfig logConfig = new LogConfig(System.getProperty("my.test.logging")); 
    
    public static void main( String args[] ) {
        final String method = "main";

        logConfig.refresh();
        
        try {
            logger.info("this is test");
            logger.info("this is more test");
            throw new Exception("this is test exception");
        } catch(Exception e) {
            logger.throwing(FQCN, method, e);
            logger.warning("testing warning");
        }

        Thread t = new Thread(new Runnable() {
                public void run() {
                    logger.severe("from another thread");
                }
        });
        t.setDaemon(true);
        t.start();
    }
}
 */