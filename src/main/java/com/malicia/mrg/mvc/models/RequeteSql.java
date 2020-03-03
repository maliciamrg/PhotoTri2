package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * The type Requete sql.
 */
public class RequeteSql {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    private RequeteSql() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Sql delete repertory int.
     *
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int sqlDeleteRepertory() throws SQLException {

        //compte le nombre de photo presente dans la base poour le repertoire

        String sql = " delete from AgLibraryFolder  " +
                " where pathFromRoot in ( " +
                "select b.pathFromRoot " +
                "from AgLibraryFolder b " +
                "left join AgLibraryFile a " +
                "on a.folder = b.id_local " +
                "left join AgLibraryFolder c " +
                "on c.pathFromRoot like b.pathFromRoot || \"_%\" " +
                "where a.folder is  NULL " +
                "and  c.pathFromRoot  is  NULL " +
                "group by  b.pathFromRoot " +
                " ); ";
        PreparedStatement pstmt = null;
        pstmt = SQLiteJDBCDriverConnection.conn.prepareStatement(sql);
        return pstmt.executeUpdate();

    }

    /**
     * Sql group groupless by plage adherance result set.
     *
     * @param tempsAdherence the temps adherence
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet sqlGroupGrouplessByPlageAdheranceRepNew(String tempsAdherence) throws SQLException {

        //Extraction des photo groupeless , dans le repertoire %repertoireNew%
        // et calcul la plage d'aherance (+- tempsAdherence)
        //
        // GroupNewPhoto
        //      captureTime (in seconds)
        //      mint (adherence min) (in seconds)
        //      maxt (adherence max) (in seconds)
        //      CameraModel
        //      src (groupeless)
        //      dest (groupe)
        //      captureTimeOrig
        return SQLiteJDBCDriverConnection.select(
                "select  " +
                        " strftime('%s', e.captureTime) as captureTime , " +
                        " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                        " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt , " +
                        " aiecm.value as CameraModel , " +
                        " c.absolutePath || b.pathFromRoot || a.lc_idx_filename as src , " +
                        " c.absolutePath  as absolutePath , " +
                        " e.captureTime as captureTimeOrig " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b  " +
                        "on a.folder = b.id_local  " +
                        "inner join AgLibraryRootFolder c  " +
                        "on b.rootFolder = c.id_local  " +
                        "inner join Adobe_images e  " +
                        "on a.id_local = e.rootFile  " +
                        "LEFT JOIN AgHarvestedExifMetadata ahem " +
                        "ON e.id_local = ahem.image " +
                        "LEFT JOIN AgInternedExifCameraModel aiecm " +
                        "ON ahem.cameraModelRef = aiecm.id_local " +
                        "Where b.pathFromRoot like \"" + Context.getRepertoireNew() + "%" + "\"" +
                        " Order by captureTime ;  ");

    }

    /**
     * Sqlget listelementrejetaranger result set.
     *
     * @param tempsAdherence the temps adherence
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet sqlgetListelementrejetaranger() throws SQLException {
        return SQLiteJDBCDriverConnection.select(
                "select a.id_local as file_id_local, " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "a.lc_idx_filename as lc_idx_filename " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b   " +
                        " on a.folder = b.id_local  " +
                        "Where b.pathFromRoot not like \"" + Context.getRepertoireNew() + "%" + "\"" +
                        "  and b.pathFromRoot like \"%" + Context.getRepRejet() + "%\" " +
                        " ;");
    }

    /**
     * Sqlget listelementnewaclasser result set.
     *
     * @param tempsAdherence the temps adherence
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet sqlgetListelementnewaclasser(String tempsAdherence) throws SQLException {
        return SQLiteJDBCDriverConnection.select(
                "select a.id_local as file_id_local, " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "a.lc_idx_filename as lc_idx_filename , " +
                        " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                        " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt  " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b   " +
                        " on a.folder = b.id_local  " +
                        "inner join AgLibraryRootFolder c  " +
                        " on b.rootFolder = c.id_local   " +
                        "inner join Adobe_images e  " +
                        " on a.id_local = e.rootFile    " +
                        "Where b.pathFromRoot like \"%" + Context.getRepertoireNew() + "%\" " +
                        "order by captureTime asc;");
    }

    public static ResultSet sqlgetListrepnew() throws SQLException {
        return SQLiteJDBCDriverConnection.select(
                "select " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot  " +
                        "from AgLibraryFolder b   " +
                        "Where b.pathFromRoot like \"%" + Context.getRepertoireNew() + "%\" " +
                        ";");
    }

    /**
     * Gets path first.
     *
     * @return the path first
     * @throws SQLException the sql exception
     */
    public static String getabsolutePathFirst() throws SQLException {
        ResultSet rs = SQLiteJDBCDriverConnection.select("" +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "LIMIT 1 " +
                ";");

        while (rs.next()) {
            return rs.getString("absolutePath");
        }

        return "";
    }

    /**
     * Update repertory name.
     *
     * @param idLocal        the id local
     * @param newpathfromroot the root folder
     * @throws SQLException the sql exception
     * @return
     */
    public static int updateRepertoryName(String idLocal, String newpathfromroot) throws SQLException {
        String sql;
        sql = "" +
                "update AgLibraryFolder " +
                "set pathFromRoot =  \"" + newpathfromroot + "\" " +
                "where id_local =  " + idLocal + " " +
                ";";
        LOGGER.info(sql);
        return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();
    }

    /**
     * Sql get all root result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet sqlGetAllRoot() throws SQLException {
        return SQLiteJDBCDriverConnection.select("select name , absolutePath from AgLibraryRootFolder ;");
    }

    /**
     * Sql mkdir repertory int.
     *
     * @param nextIdlocal  the next idlocal
     * @param nextIdGlobal the next id global
     * @param rootfolder   the rootfolder
     * @param pathacree    the pathacree
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int sqlMkdirRepertory(long nextIdlocal, String nextIdGlobal, String rootfolder, String pathacree) throws SQLException {
        String absolutePath = SQLiteJDBCDriverConnection.select("" +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "where id_local = \"" + rootfolder + "\" " +
                "LIMIT 1 " +
                ";").getString(1);
        String pathacreewoabsolutePath = ActionfichierRepertoire.normalizePath(pathacree).replace(absolutePath, "");
        String sql = "" +
                "INSERT INTO AgLibraryFolder " +
                "(id_local, " +
                "id_global, " +
                "pathFromRoot, " +
                "rootFolder) " +
                "VALUES ( " +
                " \"" + nextIdlocal + "\" , " +
                " \"" + nextIdGlobal + "\" , " +
                " \"" + ActionfichierRepertoire.normalizePath(pathacreewoabsolutePath + "/") + "\" , " +
                " \"" + rootfolder + "\"  " +
                ");";

        return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();

    }

    /**
     * Sqlmovefile int.
     *
     * @param fileIdLocal   the file id local
     * @param folderIdLocal the folder id local
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int sqlmovefile(String fileIdLocal, String folderIdLocal) throws SQLException {
        String sql;
        sql = "" +
                "update AgLibraryFile " +
                "set folder =  " + folderIdLocal + " " +
                "where id_local =  " + fileIdLocal + " " +
                ";";
        LOGGER.info(sql);
        return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();
    }

    /**
     * Sqlmovefile int.
     *
     * @param idrootfolderSourceToPath      the idrootfolder source to path
     * @param sourceToPath                   the source to path
     * @param idrootfolderDestinationToPath the idrootfolder destination to path
     * @param destinationToPath              the destination to path
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int sqlmovefile(String idrootfolderSourceToPath, Path sourceToPath, String idrootfolderDestinationToPath, Path destinationToPath) throws SQLException {
        String sql;
        LOGGER.info(sourceToPath.toString() + " => " + destinationToPath.toString());

        String absolutePathsourceToPath = SQLiteJDBCDriverConnection.select("" +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "where id_local = \"" + idrootfolderSourceToPath + "\" " +
                "LIMIT 1 " +
                ";").getString(1);
        String pathabsolutePathsourceToPath = ActionfichierRepertoire.normalizePath((new File(String.valueOf(sourceToPath))).getParent() + File.separator).replace(absolutePathsourceToPath, "");
        sql = "" +
                "select id_local " +
                "from AgLibraryFolder " +
                "where pathFromRoot = \"" + pathabsolutePathsourceToPath + "\" " +
                "and rootFolder = " + idrootfolderSourceToPath + " " +
                "LIMIT 1 " +
                ";";
        String idsourceToPath = SQLiteJDBCDriverConnection.select(sql).getString(1);

        String absolutePathdestinationToPath = SQLiteJDBCDriverConnection.select("" +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "where id_local = \"" + idrootfolderDestinationToPath + "\" " +
                "LIMIT 1 " +
                ";").getString(1);
        String pathabsolutePathdestinationToPath = ActionfichierRepertoire.normalizePath((new File(String.valueOf(destinationToPath))).getParent() + File.separator).replace(absolutePathdestinationToPath, "");
        sql = "" +
                "select id_local " +
                "from AgLibraryFolder " +
                "where pathFromRoot = \"" + pathabsolutePathdestinationToPath + "\" " +
                "and rootFolder = " + idrootfolderDestinationToPath + " " +
                "LIMIT 1 " +
                ";";
        String iddestinationToPath = SQLiteJDBCDriverConnection.select(sql).getString(1);

        sql = "" +
                "update AgLibraryFile " +
                "set folder =  " + iddestinationToPath + " " +
                "where lc_idx_filename =  \"" + (new File(String.valueOf(sourceToPath))).getName() + "\" " +
                "and folder =  " + idsourceToPath + " " +
                ";";
        LOGGER.info(sql);
        return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();
    }

    /**
     * Sql delete repertory int.
     *
     * @param rootfolder     the rootfolder
     * @param pathasupprimer the pathasupprimer
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int sqlDeleteRepertory(String rootfolder, String pathasupprimer) throws SQLException {
        String sql = "" +
                "delete from AgLibraryFolder " +
                "where rootfolder = \"" + rootfolder + "\" " +
                "and " +
                "( " +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "where id_local = \"" + rootfolder + "\" " +
                "LIMIT 1 " +
                ") " +
                " || pathFromRoot = \"" + ActionfichierRepertoire.normalizePath(pathasupprimer + "/") + "\" " +
                "";

        return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();

    }

    /**
     * Retrieverootfolder string.
     *
     * @param path the path
     * @return the string
     * @throws SQLException the sql exception
     */
    public static String retrieverootfolder(String path) throws SQLException {

        ResultSet result = SQLiteJDBCDriverConnection.select("" +
                "select id_local " +
                "from AgLibraryRootFolder " +
                "where \"" + ActionfichierRepertoire.normalizePath(path) + "\" " +
                "like absolutePath || \"_%\"  ; " +
                "");


        while (result.next()) {
            return result.getString("id_local");
        }

        return "";
    }

    /**
     * Sql get adobeentity id counter result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet sqlGetAdobeentityIDCounter() throws SQLException {
        return SQLiteJDBCDriverConnection.select("" +
                "select value " +
                "from Adobe_variablesTable " +
                "where name =  \"Adobe_entityIDCounter\" " +
                ";");
    }

    /**
     * Sql set adobeentity id counter.
     *
     * @param nextidlocal the nextidlocal
     * @throws SQLException the sql exception
     */
    public static void sqlSetAdobeentityIDCounter(long nextidlocal) throws SQLException {
        String sql = "update Adobe_variablesTable   " +
                " set value = \"" + nextidlocal + "\" " +
                " where name =  \"Adobe_entityIDCounter\" " +
                "; ";
        SQLiteJDBCDriverConnection.conn.prepareStatement(sql);
    }

    /**
     * Sql get id global result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet sqlGetIdGlobal() throws SQLException {
        return SQLiteJDBCDriverConnection.select("" +
                "select id_global ,id_local " +
                "from AgLibraryFile " +
                "UNION " +
                "select id_global ,id_local " +
                "from AgLibraryFolder " +
                "order by id_local desc " +
                "LIMIT 1 " +
                ";");
    }

    /**
     * Sql group by plage adherance hors rep bazar result set.
     *
     * @param tempsAdherence the temps adherence
     * @param repBazar       the rep bazar
     * @param repKidz        the rep kidz
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet sqlGroupByPlageAdheranceHorsRepBazar(String tempsAdherence, String repBazar, String repKidz) throws SQLException {

        //Extraction des photo  , dans toute la bib
        // et calcul la plage d'aherance (+- tempsAdherence)
        //
        // GroupNewPhoto
        //      captureTime (in seconds)
        //      mint (adherence min) (in seconds)
        //      maxt (adherence max) (in seconds)
        //      pathFromRoot (pathFromRoot)
        //      src (element)
        //      absolutePath (absolutePath)
        //      captureTimeOrig
        return SQLiteJDBCDriverConnection.select(
                "select  " +
                        " strftime('%s', e.captureTime) as captureTime , " +
                        " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                        " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt , " +
                        " b.pathFromRoot , " +
                        " c.absolutePath || b.pathFromRoot || a.lc_idx_filename as src , " +
                        " c.absolutePath  as absolutePath , " +
                        " e.captureTime as captureTimeOrig " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b  " +
                        "on a.folder = b.id_local  " +
                        "inner join AgLibraryRootFolder c  " +
                        "on b.rootFolder = c.id_local  " +
                        "inner join Adobe_images e  " +
                        "on a.id_local = e.rootFile  " +
                        "Where b.pathFromRoot not like \"" + "%" + repBazar + "%" + "\"" +
                        " and b.pathFromRoot not like \"" + "%" + repKidz + "%" + "\"" +
                        " Order by b.pathFromRoot , captureTime ;  ");

    }

    /**
     * Sqlele rep bazar result set.
     *
     * @param tempsAdherence the temps adherence
     * @param repBazar       the rep bazar
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet sqleleRepBazar(String tempsAdherence, String repBazar) throws SQLException {
        //Extraction des photo  , du bazar
        // et calcul la plage d'aherance (+- tempsAdherence)
        //
        // GroupNewPhoto
        //      captureTime (in seconds)
        //      mint (adherence min) (in seconds)
        //      maxt (adherence max) (in seconds)
        //      CameraModel
        //      src (element)
        //      absolutePath (absolutePath)
        //      captureTimeOrig
        return SQLiteJDBCDriverConnection.select(
                "select  " +
                        " strftime('%s', e.captureTime) as captureTime , " +
                        " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                        " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt , " +
                        " aiecm.value as CameraModel , " +
                        " c.absolutePath || b.pathFromRoot || a.lc_idx_filename as src , " +
                        " c.absolutePath  as absolutePath , " +
                        " e.captureTime as captureTimeOrig " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b  " +
                        "on a.folder = b.id_local  " +
                        "inner join AgLibraryRootFolder c  " +
                        "on b.rootFolder = c.id_local  " +
                        "inner join Adobe_images e  " +
                        "on a.id_local = e.rootFile  " +
                        "LEFT JOIN AgHarvestedExifMetadata ahem " +
                        "ON e.id_local = ahem.image " +
                        "LEFT JOIN AgInternedExifCameraModel aiecm " +
                        "ON ahem.cameraModelRef = aiecm.id_local " +
                        "Where b.pathFromRoot like \"" + "%" + repBazar + "%" + "\"" +
                        " Order by captureTime ;  ");
    }

    /**
     * Sqlrenamefile int.
     *
     * @param fileIdLocal the file id local
     * @param rename        the rename
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int sqlrenamefile(String fileIdLocal, String rename) throws SQLException {
        String sql;
        sql = "" +
                "update AgLibraryFile " +
                "set lc_idx_filename =  \"" + rename.toLowerCase() + "\" " +
                "  , idx_filename =  \"" + rename + "\" " +
                "  , basename =  \"" + FilenameUtils.getBaseName(rename) + "\" " +
                "where id_local =  " + fileIdLocal + " " +
                ";";
        LOGGER.info(sql);
        return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();
    }
}


