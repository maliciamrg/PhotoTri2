package com.malicia.mrg;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

    private MyLogger() {
        throw new IllegalStateException("Utility class");
    }

    public static void setup(Level levelinfo) {

        // get the global logger to configure it
        Logger lOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        lOGGER.setLevel(levelinfo);

        InputStream stream = MyLogger.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);

        } catch (IOException e) {
            lOGGER.severe( e.getMessage());
        }

        try {
            FileHandler fileTxt = new FileHandler("Logging.txt");
            FileHandler fileHTML = new FileHandler("Logging.html");

            // create a TXT formatter
            SimpleFormatter formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter(formatterTxt);
            lOGGER.addHandler(fileTxt);

            // create an HTML formatter
            MyHtmlFormatter formatterHTML = new MyHtmlFormatter();
            fileHTML.setFormatter(formatterHTML);
            lOGGER.addHandler(fileHTML);
        } catch (IOException e) {
            lOGGER.severe( e.getMessage());
        }


        lOGGER.severe( "---==[ severe  ]==---");
        lOGGER.warning("---==[ warning ]==---");
        lOGGER.info(   "---==[  info   ]==---");
        lOGGER.config( "---==[ config  ]==---");
        lOGGER.fine(   "---==[  fine   ]==---");
        lOGGER.finer(  "---==[  finer  ]==---");
        lOGGER.finest( "---==[ finest  ]==---");

    }
}