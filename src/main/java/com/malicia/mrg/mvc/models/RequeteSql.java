package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.controllers.ActionfichierRepertoire;

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
     * Sql combine all groupless in group by plage adherance.
     *
     * @param tempsAdherence the temps adherence
     * @param repertoireNew  the repertoire new
     */
    public static void sqlCombineAllGrouplessInGroupByPlageAdherance(String tempsAdherence, String repertoireNew) {
        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS Repertory;  ");
//
        //Pour chaque photo qui sont dans le repertoire %repertoireNew%
        // calcul la plage d'aherance (+- tempsAdherence)
        //
        // Repertory
        //      mint (adherence min) (in seconds)
        //      maxt (adherence max) (in seconds)
        //      pathFromRoot
        //      absolutePath
        //      captureTimeOrig
        //      CameraModel
        SQLiteJDBCDriverConnection.execute("CREATE TEMPORARY TABLE Repertory AS  " +
                "select " +
                " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt , " +
                " b.pathFromRoot , " +
                " c.absolutePath , " +
                " e.captureTime as captureTimeOrig , " +
                " aiecm.value as CameraModel   " +
                " from AgLibraryFile a  " +
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
                "Where " +
                " b.pathFromRoot like \"%" + repertoireNew + "%\" " +
                " ;");

        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS NewPhoto;  ");

        //Extraction des photo groupeless , dans le repertoire %repertoireNew%
        //
        // NewPhoto
        //      captureTime (in seconds)
        //      CameraModel
        //      pathFromRoot
        //      absolutePath
        //      originalFilename
        //      captureTimeOrig
        SQLiteJDBCDriverConnection.execute("CREATE TEMPORARY TABLE NewPhoto AS  " +
                "select  " +
                " strftime('%s', e.captureTime) as captureTime , " +
                " aiecm.value as CameraModel ," +
                " b.pathFromRoot ," +
                " c.absolutePath , " +
                " a.lc_idx_filename as originalFilename ," +
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
                "Where b.pathFromRoot like \"%" + repertoireNew + "%" + "\";  ");

        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS SelectionRepertoire;  ");

        //Extraction les combinansons
        // photo groupless dans la plages d'adherence
        // et meme CameraModel
        //
        // SelectionRepertoire
        //      src (groupeless)
        //      dest (groupe possible)
        SQLiteJDBCDriverConnection.execute("CREATE TEMPORARY TABLE SelectionRepertoire AS  " +
                "SELECT distinct  " +
                " b.pathFromRoot || b.originalFilename as src , " +
                " a.absolutePath || a.pathFromRoot as dest " +
                "FROM Repertory a  " +
                "inner join NewPhoto b  " +
                "on b.captureTime between a.mint and a.maxt " +
                "  and b.CameraModel = a.CameraModel " +
                ";");
    }

    /**
     * Sql delete repertory int.
     *
     * @return the int
     */
    public static int sqlDeleteRepertory() throws SQLException {

        //compte le nombre de photo presente dans la base poour le repertoire

//        SQLiteJDBCDriverConnection.execute("  DROP TABLE IF EXISTS Repertory;  ");
//
//
//        SQLiteJDBCDriverConnection.execute(" CREATE TEMPORARY TABLE IF NOT EXISTS  Repertory AS " +
//                " select  b.pathFromRoot " +
//                " from AgLibraryFolder b " +
//                " left join AgLibraryFile a  " +
//                " on a.folder = b.id_local " +
//                " where a.folder is  NULL " +
//                " and  b.pathFromRoot <> \"\" " +
//                " group by  b.pathFromRoot ; ");
////
//        SQLiteJDBCDriverConnection.execute("  DROP TABLE IF EXISTS RepertoryAdelete;  ");
//
//        SQLiteJDBCDriverConnection.execute(" CREATE TEMPORARY TABLE IF NOT EXISTS RepertoryAdelete AS " +
//                " select r.pathFromRoot  " +
//                " from  Repertory r " +
//                " inner join AgLibraryFolder b " +
//                " on b.pathFromRoot like r.pathFromRoot || \"%\" " +
//                " group by r.pathFromRoot " +
//                " having count(b.pathFromRoot)  = 1 ; ");


//        SQLiteJDBCDriverConnection.execute("  DROP TABLE IF EXISTS Repertory;  ");
//
//
//        SQLiteJDBCDriverConnection.execute(" CREATE TEMPORARY TABLE IF NOT EXISTS  Repertory AS " +
//                " select  b.pathFromRoot " +
//                " from AgLibraryFolder b " +
//                " left join AgLibraryFile a  " +
//                " on a.folder = b.id_local " +
//                " where a.folder is  NULL " +
//                " and  b.pathFromRoot <> \"\" " +
//                " group by  b.pathFromRoot ; ");
////
//        SQLiteJDBCDriverConnection.execute("  DROP TABLE IF EXISTS RepertoryAdelete;  ");
//
//        SQLiteJDBCDriverConnection.execute(" CREATE TEMPORARY TABLE IF NOT EXISTS RepertoryAdelete AS " +
//                " select r.pathFromRoot  " +
//                " from  Repertory r " +
//                " inner join AgLibraryFolder b " +
//                " on b.pathFromRoot like r.pathFromRoot || \"%\" " +
//                " group by r.pathFromRoot " +
//                " having count(b.pathFromRoot)  = 1 ; ");


//        //* appel au select uniquement pour tracer dans la log
//        SQLiteJDBCDriverConnection.select(
//                " select b.pathFromRoot " +
//                        "from AgLibraryFolder b " +
//                        "left join AgLibraryFile a " +
//                        "on a.folder = b.id_local " +
//                        "left join AgLibraryFolder c " +
//                        "on c.pathFromRoot like b.pathFromRoot || \"_%\" " +
//                        "where a.folder is  NULL " +
//                        "and  c.pathFromRoot  is  NULL " +
//                        "group by  b.pathFromRoot ;");


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
                int ret = pstmt.executeUpdate();
                pstmt = null;
                return ret;

    }

    /**
     * Sql group groupless by plage adherance result set.
     *
     * @param tempsAdherence the temps adherence
     * @return the result set
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
//                        " Order by CameraModel , captureTime ;  ");
                        " Order by captureTime ;  ");

    }
    public static ResultSet sqlgetListelementrejetaranger(String tempsAdherence) throws SQLException {
        return SQLiteJDBCDriverConnection.select(
                "select a.id_local as file_id_local, " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "a.lc_idx_filename as lc_idx_filename , " +
                        "e.captureTime " +
                        " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                        " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt , " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b   " +
                        " on a.folder = b.id_local  " +
                        "inner join AgLibraryRootFolder c  " +
                        " on b.rootFolder = c.id_local   " +
                        "inner join Adobe_images e  " +
                        " on a.id_local = e.rootFile    " +
                        "Where b.pathFromRoot like \"%@New%\" \n" +
                        "order by captureTime asc;");
    }
    public static ResultSet sqlgetListelementnewaclasser(String tempsAdherence) throws SQLException {
        return SQLiteJDBCDriverConnection.select(
                "select a.id_local as file_id_local, " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "a.lc_idx_filename as lc_idx_filename , " +
                        "e.captureTime ," +
                        " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                        " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt  " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b   " +
                        " on a.folder = b.id_local  " +
                        "inner join AgLibraryRootFolder c  " +
                        " on b.rootFolder = c.id_local   " +
                        "inner join Adobe_images e  " +
                        " on a.id_local = e.rootFile    " +
                        "Where b.pathFromRoot like \"%"+ Context.getRepertoireNew() +"%\" " +
                        "order by captureTime asc;");
    }

    public static ResultSet sqlGetTableWithIdlocal() throws SQLException {
        return SQLiteJDBCDriverConnection.select(
                "select * from \n" +
                        "(SELECT tbl_name FROM sqlite_master\n" +
                        "WHERE type = 'table'\n" +
                        "and sql like \"%id_global%\")" +
                        ";");
    }

    public static ResultSet sqlGetMaxIdlocalFromTbl(String tablename) throws SQLException {
        return SQLiteJDBCDriverConnection.select(
                "select max(id_local) from " +
                        "\"" + tablename + "\" " +
                        ";");
    }


    /**
     * Liste exif new result set.
     *
     * @param repertoirePhoto the repertoire photo
     * @return the result set
     */
    public static ResultSet listeExifNew(String repertoirePhoto) throws SQLException {
        return SQLiteJDBCDriverConnection.select("SELECT AgLibraryFile.id_local, AgHarvestedExifMetadata.image, AgLibraryFile.lc_idx_filename, Adobe_images.aspectRatioCache, " +
                "Adobe_images.bitDepth, Adobe_images.captureTime, Adobe_images.colorLabels, Adobe_images.fileFormat, Adobe_images.fileHeight, " +
                "Adobe_images.fileWidth, Adobe_images.orientation, AgHarvestedExifMetadata.aperture, AgInternedExifCameraModel.value, " +
                "AgHarvestedExifMetadata.dateDay, AgHarvestedExifMetadata.dateMonth, AgHarvestedExifMetadata.dateYear, AgHarvestedExifMetadata.flashFired, " +
                "AgHarvestedExifMetadata.focalLength, AgHarvestedExifMetadata.gpsLatitude, AgHarvestedExifMetadata.gpsLongitude, AgHarvestedExifMetadata.isoSpeedRating, " +
                "AgInternedExifLens.value, AgHarvestedExifMetadata.shutterSpeed, Adobe_AdditionalMetadata.xmp, Adobe_imageProperties.propertiesString, " +
                "AgLibraryRootFolder.absolutePath, AgLibraryFolder.pathFromRoot, [AgLibraryRootFolder].[absolutePath] & [AgLibraryFolder].[pathFromRoot] AS absolutePath_pathFromRoot " +
                "FROM (((((((AgLibraryFile " +
                "LEFT JOIN Adobe_images " +
                "ON AgLibraryFile.id_local = Adobe_images.rootFile) " +
                "LEFT JOIN AgHarvestedExifMetadata " +
                "ON Adobe_images.id_local = AgHarvestedExifMetadata.image) " +
                "LEFT JOIN Adobe_imageProperties " +
                "ON Adobe_images.id_local = Adobe_imageProperties.image) " +
                "LEFT JOIN Adobe_AdditionalMetadata " +
                "ON Adobe_images.id_local = Adobe_AdditionalMetadata.image) " +
                "LEFT JOIN AgInternedExifCameraModel " +
                "ON AgHarvestedExifMetadata.cameraModelRef = AgInternedExifCameraModel.id_local) " +
                "LEFT JOIN AgInternedExifLens " +
                "ON AgHarvestedExifMetadata.lensRef = AgInternedExifLens.id_local) " +
                "LEFT JOIN AgLibraryFolder " +
                "ON AgLibraryFile.folder = AgLibraryFolder.id_local) " +
                "LEFT JOIN AgLibraryRootFolder " +
                "ON AgLibraryFolder.rootFolder = AgLibraryRootFolder.id_local " +
                "Where  AgLibraryFolder.pathFromRoot like \"" + repertoirePhoto + "%\" " +
                "ORDER BY AgLibraryFile.id_local ;");

    }

    /**
     * Selection repertoire result set.
     *
     * @return the result set
     */
    public static ResultSet selectionRepertoire() throws SQLException {
        return SQLiteJDBCDriverConnection.select("SELECT a.dest  " +
                ", count(*) as nb " +
                "FROM SelectionRepertoire a " +
                "group by  a.dest " +
                ";");
    }


    /**
     * Gets path first.
     *
     * @return the path first
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
     * @param id_local        the id local
     * @param newpathfromroot the root folder
     */
    public static void updateRepertoryName(String id_local, String newpathfromroot) throws SQLException {
        String sql = " Update AgLibraryFolder " +
                " set pathFromRoot = ? " +
                " where id_local = ? " +
                " ; ";

        PreparedStatement pstmt = null;

        pstmt = SQLiteJDBCDriverConnection.conn.prepareStatement(sql);
        pstmt.setString(1, newpathfromroot);
        pstmt.setString(2, id_local);
        pstmt.executeUpdate();

    }

    /**
     * Sql get all root result set.
     *
     * @return the result set
     */
    public static ResultSet sqlGetAllRoot() throws SQLException {
        return SQLiteJDBCDriverConnection.select("select name , absolutePath from AgLibraryRootFolder ;");
    }

    public static int sqlMkdirRepertory(long nextIdlocal, String nextIdGlobal, String rootfolder, String pathacree) throws SQLException {
//        try {
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

//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
    }

    public static int sqlmovefile(String idrootfolder_source_toPath, Path source_toPath, String idrootfolder_destination_toPath, Path destination_toPath) throws SQLException {
//        try {
        String sql;
        LOGGER.info(source_toPath.toString() + " => " + destination_toPath.toString());

        String absolutePathsource_toPath = SQLiteJDBCDriverConnection.select("" +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "where id_local = \"" + idrootfolder_source_toPath + "\" " +
                "LIMIT 1 " +
                ";").getString(1);
        String pathabsolutePathsource_toPath = ActionfichierRepertoire.normalizePath((new File(String.valueOf(source_toPath))).getParent() + File.separator).replace(absolutePathsource_toPath, "");
        sql = "" +
                "select id_local " +
                "from AgLibraryFolder " +
                "where pathFromRoot = \"" + pathabsolutePathsource_toPath + "\" " +
                "and rootFolder = " + idrootfolder_source_toPath + " " +
                "LIMIT 1 " +
                ";";
        String idsource_toPath = SQLiteJDBCDriverConnection.select(sql).getString(1);

        String absolutePathdestination_toPath = SQLiteJDBCDriverConnection.select("" +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "where id_local = \"" + idrootfolder_destination_toPath + "\" " +
                "LIMIT 1 " +
                ";").getString(1);
        String pathabsolutePathdestination_toPath = ActionfichierRepertoire.normalizePath((new File(String.valueOf(destination_toPath))).getParent() + File.separator).replace(absolutePathdestination_toPath, "");
        sql = "" +
                "select id_local " +
                "from AgLibraryFolder " +
                "where pathFromRoot = \"" + pathabsolutePathdestination_toPath + "\" " +
                "and rootFolder = " + idrootfolder_destination_toPath + " " +
                "LIMIT 1 " +
                ";";
        String iddestination_toPath = SQLiteJDBCDriverConnection.select(sql).getString(1);

        sql = "" +
                "update AgLibraryFile " +
                "set folder =  " + iddestination_toPath + " " +
                "where lc_idx_filename =  \"" + (new File(String.valueOf(source_toPath))).getName() + "\" " +
                "and folder =  " + idsource_toPath + " " +
                ";";
        LOGGER.info(sql);
        return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
    }


    public static int sqlDeleteRepertory(String rootfolder, String pathasupprimer) throws SQLException {
//        try {
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

//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
    }

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

    public static ResultSet sqlGetAdobeentityIDCounter() throws SQLException {
        return SQLiteJDBCDriverConnection.select("" +
                "select value " +
                "from Adobe_variablesTable " +
                "where name =  \"Adobe_entityIDCounter\" " +
                ";");
    }

    public static void sqlSetAdobeentityIDCounter(long nextidlocal) throws SQLException {
        String sql = "update Adobe_variablesTable   " +
                " set value = \"" + nextidlocal + "\" " +
                " where name =  \"Adobe_entityIDCounter\" "  +
                "; ";
        PreparedStatement pstmt = null;
        pstmt = SQLiteJDBCDriverConnection.conn.prepareStatement(sql);
        int ret = pstmt.executeUpdate();
        pstmt = null;
    }

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
//        return  SQLiteJDBCDriverConnection.select("" +
//                "select value " +
//                "from Adobe_variablesTable " +
//                "where name =  \"Adobe_storeProviderID\" " +
//                ";");
    }

    public static void sqlSetAdobestoreProviderID(String nextidglobal) throws SQLException {
        String sql = "update Adobe_variablesTable   " +
                " set value = \"" + nextidglobal + "\" " +
                " where name =  \"Adobe_storeProviderID\" "  +
                "; ";
        PreparedStatement pstmt = null;
        pstmt = SQLiteJDBCDriverConnection.conn.prepareStatement(sql);
        int ret = pstmt.executeUpdate();
        pstmt = null;
    }

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
//                        " Order by CameraModel , captureTime ;  ");
                        " Order by b.pathFromRoot , captureTime ;  ");

    }

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
//                        " Order by CameraModel , captureTime ;  ");
                        " Order by captureTime ;  ");
    }

    public static int sqlrenamefile(String file_id_local, String rename) throws SQLException {
        String sql;
        sql = "" +
                "update AgLibraryFile " +
                "set lc_idx_filename =  \"" + rename + "\" " +
                "where id_local =  " + file_id_local + " " +
                ";";
        LOGGER.info(sql);
        return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();
    }
}


