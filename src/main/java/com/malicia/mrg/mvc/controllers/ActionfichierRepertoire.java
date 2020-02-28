package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.mvc.models.RequeteSql;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public static boolean delete_dir(File dir) throws IOException, SQLException {
        boolean ret = true;
        ret &= (RequeteSql.sqlDeleteRepertory(RequeteSql.retrieverootfolder(dir.toString()), dir.toString()) > 0);
        if (ret) {
            Files.delete(dir.toPath());
        }
        LOGGER.log(Level.INFO, "delete_dir: {0} = {1} ", new String[]{String.valueOf(dir), String.valueOf(ret)});
        return ret;
    }

    /**
     * Mkdir boolean.
     *
     * @param directory the directory
     * @return the boolean
     */
    public static boolean mkdir(File directory) throws SQLException {
        boolean ret = true;
        ret &= (RequeteSql.sqlMkdirRepertory(getNextIdlocal(), getNextIdGlobal(), RequeteSql.retrieverootfolder(directory.toString()), directory.toString()) > 0);
        if (ret) {
            directory.mkdir();
        }
        LOGGER.log(Level.INFO, "mkdir: {0} = {1} ", new String[]{String.valueOf(directory), String.valueOf(ret)});
        return ret;
    }

    /**
     * Move file boolean.
     *
     * @param source_toPath      the source to path
     * @param destination_toPath the destination to path
     * @return the boolean
     * @throws IOException the io exception
     */
    public static boolean move_file(Path source_toPath, Path destination_toPath) throws IOException, SQLException {
        boolean ret = true;
        ret &= (RequeteSql.sqlmovefile(
                RequeteSql.retrieverootfolder(source_toPath.toString()),
                source_toPath,
                RequeteSql.retrieverootfolder(destination_toPath.toString()),
                destination_toPath)> 0);
        if (ret) {
            Files.move(source_toPath, destination_toPath);
        }
        return true;
    }

    /**
     * renvoie le id_local max de la base ligthroom
     * <p>
     * select * from
     * (SELECT tbl_name FROM sqlite_master
     * WHERE type = 'table'
     * and sql like "%id_local%")
     * <
     * boucle pour trouver le plus grand id_local
     * select max(id_local) from
     * tbl_name
     * <p>
     * sortie => max_id_local
     * <p>
     * Adobe_variablesTable.Adobe_entityIDCounter
     *
     * @return
     */
    private static long getNextIdlocal() throws SQLException {
        long nextidlocal = 0;
//        try {
            long idlocal = RequeteSql.sqlGetAdobeentityIDCounter().getLong(1);
            nextidlocal = idlocal + 1;
            RequeteSql.sqlSetAdobeentityIDCounter(nextidlocal);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return nextidlocal;
    }

    private static String getNextIdGlobal() throws SQLException {
        String fmtnextidglobal = "";
//        try {
            String idglobal = RequeteSql.sqlGetIdGlobal().getString(1);
            fmtnextidglobal = calculnextidglobalidglobal(idglobal);

            //RequeteSql.sqlSetAdobestoreProviderID(fmtnextidglobal);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return fmtnextidglobal;
    }

    private static String calculnextidglobalidglobal(String hexv) {
        BigInteger decimal = new BigInteger(hexv.substring(0, 8) + hexv.substring(9, 13)
                + hexv.substring(14, 18) + hexv.substring(19, 23) + hexv.substring(24, 36), 16);
        decimal = decimal.add(BigInteger.ONE);
        String hexvp1 = StringUtils.leftPad(decimal.toString(16).toUpperCase(),32,"0");
//        String str = Integer.toHexString(decimal+1);
        return hexvp1.substring(0, 8) + "-" + hexvp1.substring(8, 12) + "-" + hexvp1.substring(12, 16) + "-" + hexvp1.substring(16, 20) + "-" + hexvp1.substring(20, 32);
    }

    public static String normalizePath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * Renommer un repertoire.
     * <p>
     * renomme un repertoire (physique et logique)
     *
     * @param repertoiresource the repertoiresource
     * @param repertoiredest   the repertoiredest
     * @param idLocal          the id local
     * @param rootFolder       the root folder
     * @throws SQLException the sql exception
     */
    private void renommerUnRepertoire(String repertoiresource, String repertoiredest, String idLocal, String rootFolder) throws SQLException {
        File directory = new File(repertoiresource);
        File directorydest = new File(repertoiredest);
        if (directory.isDirectory()) {
            directory.renameTo(directorydest);
            RequeteSql.updateRepertoryName(idLocal, composeRelativeRep(rootFolder, repertoiredest));
        }

    }

    /**
     * Compose le nom de repertoire relative au rootfolder
     * <p>
     * soustrait le rootfolde rau nom de repertoire pour maj dans la table libraryFolder
     *
     * @param rootFolder
     * @param repertoiredest
     * @return
     */
    private String composeRelativeRep(String rootFolder, String repertoiredest) {
        return repertoiredest.replace(rootFolder, "");
    }
}
