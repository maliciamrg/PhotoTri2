package com.malicia.mrg.model;

import java.sql.ResultSet;

import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;

public class RequeteSql {

    private RequeteSql() {
        throw new IllegalStateException("Utility class");
    }

    public static void sqlCreateAndAlimentionTable(String pasRepertoirePhoto, String tempsAdherence, String repertoireNew) {
        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS Repertory;  ");
//
        SQLiteJDBCDriverConnection.execute("CREATE TEMPORARY TABLE Repertory AS  " +
                "select e.captureTime as ortime ,  strftime('%s', DATETIME( e.captureTime,\"-"+ tempsAdherence +"\")) as mint , strftime('%s', DATETIME(e.captureTime,\"+"+ tempsAdherence +"\")) as maxt , c.absolutePath , b.pathFromRoot   " +
                " from AgLibraryFile a  " +
                "inner join AgLibraryFolder b  " +
                "on a.folder = b.id_local  " +
                "inner join AgLibraryRootFolder c  " +
                "on b.rootFolder = c.id_local  " +
                "inner join Adobe_images e  " +
                "on a.id_local = e.rootFile  " +
                "Where  b.pathFromRoot not like \"%" + pasRepertoirePhoto + "%\" " +
                " ;");

        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS NewPhoto;  " );

        SQLiteJDBCDriverConnection.execute( "CREATE TEMPORARY TABLE NewPhoto AS  " +
                "select  strftime('%s', e.captureTime) as captureTime , c.absolutePath , b.pathFromRoot ,a.originalFilename ,e.captureTime as captureTimeOrig , aiecm.value as CameraModel " +
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

        SQLiteJDBCDriverConnection.execute( "CREATE TEMPORARY TABLE SelectionRepertoire AS  " +
                "SELECT distinct  " +
                " b.pathFromRoot || b.originalFilename as src , " +
                " a.absolutePath || a.pathFromRoot as dest " +
                "FROM Repertory a  " +
                "inner join NewPhoto b  " +
                "on b.captureTime between a.mint and a.maxt " +
                ";");
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
}
