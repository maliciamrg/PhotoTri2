package com.malicia.mrg;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MyLogger {

    private MyLogger() {
        throw new IllegalStateException("Utility class");
    }

    public static void setup() {

        // get the global logger to configure it
        Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        InputStream stream = MyLogger.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            LOGGER.severe( e.getMessage());
        }

        LOGGER.severe( "---==[ severe  ]==---");
        LOGGER.warning("---==[ warning ]==---");
        LOGGER.info(   "---==[  info   ]==---");
        LOGGER.config( "---==[ config  ]==---");
        LOGGER.fine(   "---==[  fine   ]==---");
        LOGGER.finer(  "---==[  finer  ]==---");
        LOGGER.finest( "---==[ finest  ]==---");

    }
}