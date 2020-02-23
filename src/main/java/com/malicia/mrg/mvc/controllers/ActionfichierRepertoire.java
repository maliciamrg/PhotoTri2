package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.mvc.models.RequeteSql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static boolean delete_dir(File dir) throws IOException {
        boolean ret = true;
        ret &= (RequeteSql.sqlDeleteRepertory(RequeteSql.retrieverootfolder(dir.toString()), dir.toString()) > 0);
        if (ret) {
            Files.delete(dir.toPath());
        }
        LOGGER.log(Level.INFO,"delete_dir: {0} = {1} ", new String[]{String.valueOf(dir), String.valueOf(ret)});
        return ret;
    }

    public static boolean mkdir(File directory) {
        boolean ret = true;
        ret &= (RequeteSql.sqlMkdirRepertory(RequeteSql.retrieverootfolder(directory.toString()), directory.toString()) > 0);
        if (ret) {
            directory.mkdir();
        }
        LOGGER.log(Level.INFO,"mkdir: {0} = {1} ", new String[]{String.valueOf(directory), String.valueOf(ret)});
        return ret;
    }

    public static boolean move_file(Path source_toPath, Path destination_toPath) throws IOException {
        Files.move(source_toPath,destination_toPath);
        return true;
    }
}
