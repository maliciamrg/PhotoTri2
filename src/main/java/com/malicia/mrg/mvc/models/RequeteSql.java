package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class RequeteSql {

    private RequeteSql() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static void sqlCombineAllGrouplessInGroupByPlageAdherance(String pasRepertoirePhoto, String tempsAdherence, String repertoireNew) {
        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS Repertory;  ");
//
        //Pour chaque photo qui ne sont pas du repertoire %pasRepertoirePhoto%
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
                " strftime('%s', DATETIME( e.captureTime,\"-"+ tempsAdherence +"\")) as mint , " +
                " strftime('%s', DATETIME(e.captureTime,\"+"+ tempsAdherence +"\")) as maxt , " +
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
                "Where  b.pathFromRoot not like \"%" + pasRepertoirePhoto + "%\" " +
                " ;");

        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS NewPhoto;  " );

        //Extraction des photo groupeless , dans le repertoire %repertoireNew%
        //
        // NewPhoto
        //      captureTime (in seconds)
        //      CameraModel
        //      pathFromRoot
        //      absolutePath
        //      originalFilename
        //      captureTimeOrig
        SQLiteJDBCDriverConnection.execute( "CREATE TEMPORARY TABLE NewPhoto AS  " +
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

        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS SelectionRepertoire;  " );

        //Extraction les combinansons
        // photo groupless dans la plages d'adherence
        // et meme CameraModel
        //
        // SelectionRepertoire
        //      src (groupeless)
        //      dest (groupe possible)
        SQLiteJDBCDriverConnection.execute( "CREATE TEMPORARY TABLE SelectionRepertoire AS  " +
                "SELECT distinct  " +
                " b.pathFromRoot || b.originalFilename as src , " +
                " a.absolutePath || a.pathFromRoot as dest " +
                "FROM Repertory a  " +
                "inner join NewPhoto b  " +
                "on b.captureTime between a.mint and a.maxt " +
                "  and b.CameraModel = a.CameraModel " +
                ";");
    }

    public static int sqlDeleteRepertory() {

        //compte le nombre de photo presente dans la base poour le repertoire

        boolean ret = SQLiteJDBCDriverConnection.execute(
                "  DROP TABLE IF EXISTS Repertory;  " +
                        " DROP TABLE IF EXISTS RepertoryAdelete; " +
                        " CREATE TEMPORARY TABLE Repertory AS " +
                        " select  b.pathFromRoot " +
                        " from AgLibraryFolder b " +
                        " left join AgLibraryFile a  " +
                        " on a.folder = b.id_local " +
                        " where a.folder is  NULL " +
                        " and  b.pathFromRoot <> \"\" " +
                        " group by  b.pathFromRoot ; " +
                        " CREATE TEMPORARY TABLE RepertoryAdelete AS " +
                        " select r.pathFromRoot  " +
                        " from  Repertory r " +
                        " inner join AgLibraryFolder b " +
                        " on b.pathFromRoot like r.pathFromRoot || \"%\" " +
                        " group by r.pathFromRoot " +
                        " having count(b.pathFromRoot)  = 1 ; ");

        if (ret) {
            String sql = " delete from AgLibraryFolder  " +
                    " where pathFromRoot in ( " +
                    " select * from RepertoryAdelete " +
                    " ); ";
            PreparedStatement pstmt = null;
            try {
                pstmt = SQLiteJDBCDriverConnection.conn.prepareStatement(sql);
                return pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            return 0;
        }
        return 0;
    }

    public static ResultSet sqlGroupGrouplessByPlageAdherance(String tempsAdherence) {

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
                " strftime('%s', DATETIME( e.captureTime,\"-"+ tempsAdherence +"\")) as mint , " +
                " strftime('%s', DATETIME(e.captureTime,\"+"+ tempsAdherence +"\")) as maxt , " +
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

    public static ResultSet selectionRepertoire() {
        return SQLiteJDBCDriverConnection.select("SELECT a.dest  " +
                ", count(*) as nb " +
                "FROM SelectionRepertoire a " +
                "group by  a.dest " +
                ";");
    }


    public static String getabsolutePathFirst() {
        ResultSet rs = SQLiteJDBCDriverConnection.select("select absolutePath " +
                "from AgLibraryRootFolder " +
                ";");
        try {
            while (rs.next()){
                    return rs.getString("absolutePath");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void updateRepertoryName(String id_local, String rootFolder, String repertoiredest) {
        LOGGER.info("updateRepertoryName");
    }

    public static ResultSet sqlGetAllRoot() {
        return SQLiteJDBCDriverConnection.select("select name , absolutePath from AgLibraryRootFolder ;");
    }
}
