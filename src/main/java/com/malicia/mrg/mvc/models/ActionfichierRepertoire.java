package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
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
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
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
     * @throws SQLException the sql exception
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
     * @param file_id_local    the file id local
     * @param folder_id_local  the folder id local
     * @param lc_idx_filename  the lc idx filename
     * @param pathFromRootsrc  the path from rootsrc
     * @param pathFromRootdest the path from rootdest
     * @return the boolean
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public static boolean move_file(String file_id_local, String folder_id_local, String lc_idx_filename, String pathFromRootsrc, String pathFromRootdest) throws IOException, SQLException {
        String dest = normalizePath(Context.getAbsolutePathFirst() + pathFromRootsrc + lc_idx_filename);
        String source = normalizePath(Context.getAbsolutePathFirst() + pathFromRootdest + lc_idx_filename);
        File fsource = new File(source);
        File fdest = new File(dest);
        if (fsource.compareTo(fdest) != 0) {
            boolean ret = true;
            ret &= (RequeteSql.sqlmovefile(
                    file_id_local,
                    folder_id_local) > 0);
            if (ret) {
                Files.move(fsource.toPath(), fdest.toPath());
            }
        }
        return true;
    }

    /**
     * Move file boolean.
     *
     * @param source_toPath      the source to path
     * @param destination_toPath the destination to path
     * @return the boolean
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public static boolean move_file(Path source_toPath, Path destination_toPath) throws IOException, SQLException {
        boolean ret = true;
        ret &= (RequeteSql.sqlmovefile(
                RequeteSql.retrieverootfolder(source_toPath.toString()),
                source_toPath,
                RequeteSql.retrieverootfolder(destination_toPath.toString()),
                destination_toPath) > 0);
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
        String hexvp1 = StringUtils.leftPad(decimal.toString(16).toUpperCase(), 32, "0");
//        String str = Integer.toHexString(decimal+1);
        return hexvp1.substring(0, 8) + "-" + hexvp1.substring(8, 12) + "-" + hexvp1.substring(12, 16) + "-" + hexvp1.substring(16, 20) + "-" + hexvp1.substring(20, 32);
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
     * Renommer un repertoire.
     * <p>
     * renomme un repertoire (physique et logique)
     *
     * @param idLocal                the id local
     * @param relativerepertoiredest the relativerepertoiredest
     * @param pathtorootorig         the pathtorootorig
     * @throws SQLException the sql exception
     */
    public static void renommerUnRepertoire(String idLocal, String relativerepertoiredest, String pathtorootorig) throws SQLException {
        String repertoiredest = normalizePath(Context.getAbsolutePathFirst() + relativerepertoiredest);
        String repertoiresource = normalizePath(Context.getAbsolutePathFirst() + pathtorootorig);
        File directory = new File(repertoiresource);
        File directorydest = new File(repertoiredest);
        if (repertoiresource.compareTo(repertoiredest) != 0) {
            if (directory.isDirectory()) {
                directory.renameTo(directorydest);
                RequeteSql.updateRepertoryName(idLocal, relativerepertoiredest);
            }
        }

    }

    /**
     * Rename file.
     *
     * @param file_id_local   the file id local
     * @param lc_idx_filename the lc idx filename
     * @param rename          the rename
     * @param pathFromRoot    the path from root
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    public static void rename_file(String file_id_local, String lc_idx_filename, String rename, String pathFromRoot) throws SQLException, IOException {
        String dest = normalizePath(Context.getAbsolutePathFirst() + pathFromRoot + rename);
        String source = normalizePath(Context.getAbsolutePathFirst() + pathFromRoot + lc_idx_filename);
        File fsource = new File(source);
        File fdest = new File(dest);

        if (source.compareTo(dest) != 0) {
            boolean ret = true;
            ret &= (RequeteSql.sqlrenamefile(
                    file_id_local,
                    rename) > 0);
            if (ret) {
                Files.move(fsource.toPath(), fdest.toPath());
            }
        }
    }

}
