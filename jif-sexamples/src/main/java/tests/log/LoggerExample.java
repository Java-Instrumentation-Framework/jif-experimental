/*
 * Copyright 2011 John Yeary <jyeary@bluelotussoftware.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: LoggerExample.java 357 2011-05-31 22:38:49Z jyeary $
 */
package tests.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 * @version 1.0
 */
public class LoggerExample {

    private static final Logger logger = Logger.getLogger(LoggerExample.class.getName());
    private FileHandler fileHandler;

    public LoggerExample() {
        /*
         * This is one option using the FileHandler in the constructor
         * for the class. This will work if we need to instantiate
         * the class before use. In the case of a class where the main()
         * method is located we should consider a separate case in
         * the main method to initialize the handler. (See below)
         */
        addFileHandler(logger);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.info("main() method called.");

        /*
         * Instantiating the class with actually create two
         * FileHandlers.
         */
        LoggerExample le = new LoggerExample();

        /*
         * Anonymous inner class method for adding a FileHandler
         * in main() method.
         */
        try {
            logger.info("Adding FileHandler");
            logger.addHandler(new FileHandler(LoggerExample.class.getName()
                    + ".main.log"));
            logger.info("Added FileHandler to Logger");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        // Example logging.
        logger.info("Logging for fun and profit");
        logger.fine("Fine...");
        logger.finer("Finer...");
        logger.finest("Finest...");
        logger.warning("Warning...");
        logger.severe("Severe...");
        logger.info("Logging complete.");
    }

    /**
     * Add a <code>FileHandler</code> to the provided <code>Logger</code>.
     * @param logger <code>Logger</code> to add <code>FileHandler</code>.
     */
    private void addFileHandler(Logger logger) {
        try {
            fileHandler = new FileHandler(LoggerExample.class.getName() + ".log");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        logger.addHandler(fileHandler);
    }
}
