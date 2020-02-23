package com.malicia.mrg.mvc.controllers;

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
    public static boolean delete_dir(File dir) throws IOException {
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
    public static boolean mkdir(File directory) {
        boolean ret = true;
        long v = getMaxidlocal();
        ret &= (RequeteSql.sqlMkdirRepertory(RequeteSql.retrieverootfolder(directory.toString()), directory.toString()) > 0);
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
    public static boolean move_file(Path source_toPath, Path destination_toPath) throws IOException {
        Files.move(source_toPath, destination_toPath);
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
     *
     * @return
     */
    private static long getMaxidlocal() {
        ResultSet rs = RequeteSql.sqlGetTableWithIdlocal();
        long idlocalmax = 0;
        try {
            while (rs.next()) {
                long idlocal = RequeteSql.sqlGetMaxIdlocalFromTbl(rs.getString("tbl_name")).getLong(1);
                if (idlocal > idlocalmax) {
                    idlocalmax = idlocal;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return idlocalmax;
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
