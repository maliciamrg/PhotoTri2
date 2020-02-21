package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.mvc.models.RequeteSql;

import java.io.File;
import java.util.logging.Logger;

public class actionfichierRepertoire {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static boolean deleterepertoire(File dir) {
        boolean ret = true;
        ret &= dir.delete();
        if (ret) {
            ret &= (RequeteSql.sqlDeleteRepertory(RequeteSql.retrieverootfolder(dir.toString()),dir.toString()) > 0);
        }
        LOGGER.info("delete:" + dir.toString() + " : " + ret);
        return ret;
    }
}
