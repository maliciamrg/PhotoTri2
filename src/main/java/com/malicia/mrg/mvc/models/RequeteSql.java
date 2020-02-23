package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;

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
                " a.originalFilename ," +
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
    public static int sqlDeleteRepertory() {

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


        //* appel au select uniquement pour tracer dans la log
        ResultSet rs = SQLiteJDBCDriverConnection.select(
                " select b.pathFromRoot " +
                        "from AgLibraryFolder b " +
                        "left join AgLibraryFile a " +
                        "on a.folder = b.id_local " +
                        "left join AgLibraryFolder c " +
                        "on c.pathFromRoot like b.pathFromRoot || \"_%\" " +
                        "where a.folder is  NULL " +
                        "and  c.pathFromRoot  is  NULL " +
                        "group by  b.pathFromRoot ;");

        try {
            if (true) {
                // rs.close();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Sql group groupless by plage adherance result set.
     *
     * @param tempsAdherence the temps adherence
     * @return the result set
     */
    public static ResultSet sqlGroupGrouplessByPlageAdheranceRepNew(String tempsAdherence) {

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
                        " c.absolutePath || b.pathFromRoot || a.originalFilename as src , " +
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


    public static ResultSet sqlGetTableWithIdlocal() {
        return SQLiteJDBCDriverConnection.select(
                "select * from \n" +
                        "(SELECT tbl_name FROM sqlite_master\n" +
                        "WHERE type = 'table'\n" +
                        "and sql like \"%id_global%\")" +
                        ";" );
    }

    public static ResultSet sqlGetMaxIdlocalFromTbl(String tablename ) {
        return SQLiteJDBCDriverConnection.select(
                "select max(id_local) from " +
                        "\""+tablename+"\" "+
                        ";" );
    }


    /**
     * Liste exif new result set.
     *
     * @param repertoirePhoto the repertoire photo
     * @return the result set
     */
    public static ResultSet listeExifNew(String repertoirePhoto) {
        return SQLiteJDBCDriverConnection.select("SELECT AgLibraryFile.id_local, AgHarvestedExifMetadata.image, AgLibraryFile.originalFilename, Adobe_images.aspectRatioCache, " +
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
    public static ResultSet selectionRepertoire() {
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
    public static String getabsolutePathFirst() {
        ResultSet rs = SQLiteJDBCDriverConnection.select("select absolutePath " +
                "from AgLibraryRootFolder " +
                ";");
        try {
            while (rs.next()) {
                return rs.getString("absolutePath");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    public static ResultSet sqlGetAllRoot() {
        return SQLiteJDBCDriverConnection.select("select name , absolutePath from AgLibraryRootFolder ;");
    }

    public static int sqlMkdirRepertory(String rootfolder, String pathasupprimer) {
        try {
            String sql = "" +
                    "stoprun delete from AgLibraryFolder " +
                    "where rootfolder = \"" + rootfolder + "\" " +
                    "and " +
                    "( " +
                    "select absolutePath " +
                    "from AgLibraryRootFolder " +
                    "where id_local = \"" + rootfolder + "\" " +
                    "LIMIT 1 " +
                    ") " +
                    " || pathFromRoot = \"" + normalizePath(pathasupprimer + "/") + "\" " +
                    "";

            return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int sqlDeleteRepertory(String rootfolder, String pathasupprimer) {
        try {
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
                    " || pathFromRoot = \"" + normalizePath(pathasupprimer + "/") + "\" " +
                    "";

            return SQLiteJDBCDriverConnection.conn.prepareStatement(sql).executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static String normalizePath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    public static String retrieverootfolder(String path) {

        ResultSet result = SQLiteJDBCDriverConnection.select("" +
                "select id_local " +
                "from AgLibraryRootFolder " +
                "where \"" + normalizePath(path) + "\" " +
                "like absolutePath || \"_%\"  ; " +
                "");

        try {
            while (result.next()) {
                return result.getString("id_local");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}


