package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.mvc.models.RequeteSql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActionfichierRepertoire {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    private ActionfichierRepertoire() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean deleterepertoire(File dir) throws IOException {
        boolean ret = true;
        ret &= (RequeteSql.sqlDeleteRepertory(RequeteSql.retrieverootfolder(dir.toString()), dir.toString()) > 0);
        if (ret) {
            Files.delete(dir.toPath());
        }
        LOGGER.log(Level.INFO,"delete: {0} = {1} ", new String[]{String.valueOf(dir), String.valueOf(ret)});
        return ret;
    }
}
