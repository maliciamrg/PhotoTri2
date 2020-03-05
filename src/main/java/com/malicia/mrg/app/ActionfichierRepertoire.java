package com.malicia.mrg.app;

import com.malicia.mrg.mvc.models.RequeteSql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Actionfichier repertoire.
 */
public class ActionfichierRepertoire {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    private ActionfichierRepertoire() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Delete dir boolean.
     *
     * @param dir the dir
     * @return the boolean
     * @throws IOException the io exception
     */
    public static boolean deleteDir(File dir) throws IOException {
        boolean ret = true;
        Files.delete(dir.toPath());
        LOGGER.log(Level.INFO, "delete_dir: {0} ", new String[]{String.valueOf(dir)});
        return ret;
    }


    /**
     * Move file boolean.
     *
     * @param sourceToPath      the source to path
     * @param destinationToPath the destination to path
     * @return the boolean
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public static void moveFile(Path sourceToPath, Path destinationToPath) throws IOException, SQLException {
        boolean ret = true;
        ret = (RequeteSql.sqlmovefile(
                RequeteSql.retrieverootfolder(sourceToPath.toString()),
                sourceToPath,
                RequeteSql.retrieverootfolder(destinationToPath.toString()),
                destinationToPath) > 0);
        if (ret) {
            Files.move(sourceToPath, destinationToPath);
        }
    }

    /**
     * Normalize path string.
     *
     * @param path the path
     * @return the string
     */
    public static String normalizePath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * Mkdir.
     *
     * @param directoryName the directory name
     * @throws SQLException the sql exception
     */
    public static void mkdir(String directoryName) throws SQLException {
        File fdirectoryName = new File(directoryName);
        if (!fdirectoryName.exists()) {
            fdirectoryName.mkdir();
        }
        ResultSet ret = RequeteSql.sqlGetIdlocalfolderfrompath(directoryName);
        if (ret.isClosed()) {
            RequeteSql.sqlMkdirRepertory(directoryName);
        }
    }

    /**
     * Move file.
     *
     * @param source      the source
     * @param destination the destination
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public static void moveFile(String source, String destination) throws IOException, SQLException {
        File fsource = new File(source);
        File fdest = new File(destination);
        if (fsource.compareTo(fdest) != 0) {
            if (!fsource.exists()) {
                throw new IllegalStateException("non existance : " + fsource.toString());
            }
            if (fdest.exists()) {
                throw new IllegalStateException("existance     : " + fdest.toString());
            }
            LOGGER.info(() -> "move_file p=" + fsource.toString() + " -> " + fdest.toString());
            Files.move(fsource.toPath(), fdest.toPath());
            RequeteSql.sqlmovefile(source, destination);
        }
    }
}
