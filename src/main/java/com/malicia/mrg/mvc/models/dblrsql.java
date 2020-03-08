package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.ActionfichierRepertoire;
import com.malicia.mrg.app.Context;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * The type Requete sql.
 */
public class dblrsql extends SQLiteJDBCDriverConnection {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    /**
     * Instantiates a new Dblrsql.
     *
     * @param catalogLrcat the catalog lrcat
     */
    public dblrsql(String catalogLrcat) {
        super(catalogLrcat);
    }

    /**
     * Normalize path string.
     *
     * @param path the path
     * @return the string
     */
    private static String normalizePath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * Sql delete repertory int.
     *
     * @return the int
     * @throws SQLException the sql exception
     */
    public int sqlDeleteRepertory() throws SQLException {

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
        return executeUpdate(sql);

    }

    /**
     * Sqlget listelementrejetaranger result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlgetListelementrejetaranger() throws SQLException {
        return select(
                "select a.id_local as file_id_local, " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "a.lc_idx_filename as lc_idx_filename , " +
                        "b.rootFolder " +
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
     * @param idrootFolder
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlgetListelementnewaclasser(String tempsAdherence, String idrootFolder) throws SQLException {
        return select(
                "select a.id_local as file_id_local, " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "a.lc_idx_filename as lc_idx_filename , " +
                        " aiecm.value as CameraModel , " +
                        " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                        " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt  , " +
                        "b.rootFolder " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b   " +
                        " on a.folder = b.id_local  " +
                        "inner join AgLibraryRootFolder c  " +
                        " on b.rootFolder = c.id_local   " +
                        "inner join Adobe_images e  " +
                        " on a.id_local = e.rootFile    " +
                        "LEFT JOIN AgHarvestedExifMetadata ahem " +
                        "ON e.id_local = ahem.image " +
                        "LEFT JOIN AgInternedExifCameraModel aiecm " +
                        "ON ahem.cameraModelRef = aiecm.id_local " +
                        "Where b.rootFolder =  " + idrootFolder + " " +
                        "order by captureTime asc;");


    }

    /**
     * Gets path first.
     *
     * @param rootFolder the root folder
     * @return the path first
     * @throws SQLException the sql exception
     */
    public String getabsolutePath(String rootFolder) throws SQLException {
        ResultSet rs = select("" +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "where id_local = " + rootFolder + " " +
                ";");

        while (rs.next()) {
            return rs.getString("absolutePath");
        }

        return "";
    }

    /**
     * Sql get all root result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlGetAllRoot() throws SQLException {
        return select("select name , absolutePath from AgLibraryRootFolder ;");
    }

    /**
     * Sqlmovefile int.
     *
     * @param source      the source
     * @param destination the destination
     * @return the int
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    public void sqlmovefile(String source, String destination) throws IOException, SQLException {
        ActionfichierRepertoire.moveFile(source, destination);

        File fdest = new File(destination);
        String folderIdLocal = null;
        folderIdLocal = sqlGetIdlocalfolderfrompath(fdest.getParent()).getString(1);
        ResultSet rsid = sqlGetIdlocalfilefrompath(source);
        if (!rsid.isClosed()) {
            String fileIdLocal = rsid.getString(1);
            String sql;
            sql = "" +
                    "update AgLibraryFile " +
                    "set folder =  " + folderIdLocal + " ," +
                    " baseName =  '" + FilenameUtils.getBaseName(destination) + "' , " +
                    " idx_filename =  '" + fdest.getName() + "' , " +
                    " lc_idx_filename =  '" + fdest.getName().toLowerCase() + "'  " +
                    "where id_local =  " + fileIdLocal + " " +
                    ";";
            LOGGER.info(sql);
            executeUpdate(sql);
        }

    }

    /**
     * Sqlmovefile int.
     *
     * @param idrootfolderSourceToPath      the idrootfolder source to path
     * @param sourceToPath                  the source to path
     * @param idrootfolderDestinationToPath the idrootfolder destination to path
     * @param destinationToPath             the destination to path
     * @return the int
     * @throws SQLException the sql exception
     */
    public int sqlmovefile(String idrootfolderSourceToPath, Path sourceToPath, String idrootfolderDestinationToPath, Path destinationToPath) throws SQLException {
        String sql;
        LOGGER.info(sourceToPath.toString() + " => " + destinationToPath.toString());

        String absolutePathsourceToPath = select("" +
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
        String idsourceToPath = select(sql).getString(1);

        String absolutePathdestinationToPath = select("" +
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
        String iddestinationToPath = select(sql).getString(1);

        sql = "" +
                "update AgLibraryFile " +
                "set folder =  " + iddestinationToPath + " " +
                "where lc_idx_filename =  \"" + (new File(String.valueOf(sourceToPath))).getName() + "\" " +
                "and folder =  " + idsourceToPath + " " +
                ";";
        LOGGER.info(sql);
        return executeUpdate(sql);
    }

    /**
     * Retrieverootfolder string.
     *
     * @param path the path
     * @return the string
     * @throws SQLException the sql exception
     */
    public String retrieverootfolder(String path) throws SQLException {

        ResultSet result = select("" +
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

    public String getabsolutePathfromname(String name)throws SQLException {
        ResultSet rs = select("" +
                "select absolutePath " +
                "from AgLibraryRootFolder " +
                "where \"" + name + "\" = name   ; " +
                ";");

        while (rs.next()) {
            return rs.getString("absolutePath");
        }

        return "";
    }

    public String retrieverootfolderfromname(String name) throws SQLException {

        ResultSet result = select("" +
                "select id_local " +
                "from AgLibraryRootFolder " +
                "where \"" + name + "\" = name   ; " +
                "");


        while (result.next()) {
            return result.getString("id_local");
        }

        return "";
    }
    /**
     * Sql get idlocalfolderfrompath result set.
     *
     * @param destination the destination
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlGetIdlocalfolderfrompath(String destination) throws SQLException {
        return select("" +
                "select f.id_local from AgLibraryFolder f " +
                "inner join AgLibraryRootFolder r " +
                "on r.id_local = f.rootFolder " +
                "where  r.absolutePath || f.pathFromRoot like '" + normalizePath(destination) + "%'  " +
                "limit 1" +
                ";");
    }

    /**
     * Sql get idlocalfilefrompath result set.
     *
     * @param destination the destination
     * @return the result set
     * @throws SQLException the sql exception
     */
    private ResultSet sqlGetIdlocalfilefrompath(String destination) throws SQLException {
        return select("" +
                "select id_local from AgLibraryFile " +
                "where '" + normalizePath(destination) + "' like '_%' || lc_idx_filename " +
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
    public ResultSet sqlGroupByPlageAdheranceHorsRepBazar(String tempsAdherence, String repBazar, String repKidz) throws SQLException {

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
        return select(
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
    public ResultSet sqleleRepBazar(String tempsAdherence, String repBazar) throws SQLException {
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
        return select(
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
     * Sql mkdir repertory int.
     *
     * @param directoryName the directory name
     * @return the int
     * @throws SQLException the sql exception
     */
    public void sqlMkdirRepertory(String directoryName) throws SQLException {

        ActionfichierRepertoire.mkdir(directoryName);
        ResultSet ret = this.sqlGetIdlocalfolderfrompath(directoryName);
        if (ret.isClosed()) {

            String pathFromRoot = normalizePath(directoryName.replace(this.getabsolutePathfromname(Context.getRepertoireNew()), "") + File.separator);
            long idlocal = sqlGetPrevIdlocalforFolder();
            if (idlocal == 0) {
                throw new IllegalStateException("no more idlocal empty for folder");
            }
//        sqlSetAdobeentityIDCounter(idlocal);
            String rootFolder = retrieverootfolder(directoryName);
            String sql;
            sql = "INSERT INTO AgLibraryFolder" +
                    "(id_local, " +
                    "id_global, " +
                    "pathFromRoot, " +
                    "rootFolder) " +
                    "VALUES " +
                    "('" + idlocal + "', " +
                    "'" + UUID.randomUUID().toString().toUpperCase() + "', " +
                    "'" + pathFromRoot + "', " +
                    "'" + rootFolder + "')" +
                    ";";
            executeUpdate(sql);
        }

    }

    private long sqlGetPrevIdlocalforFolder() throws SQLException {
        String sql = "select * FROM AgLibraryFolder " +
                "ORDER by id_local desc " +
                "; ";
        ResultSet rs = select(sql);
        boolean first = true;
        long idLocalCalcul = 0;
        long id_local = 0;
        while (rs.next()) {
            // Recuperer les info de l'elements
            id_local = rs.getLong("id_local");
            if (first) {
                idLocalCalcul = id_local;
                first = false;
            } else {
                idLocalCalcul -= 1;
                if (idLocalCalcul > id_local) {
                    return idLocalCalcul;
                }
            }
        }
        // 0 ou 1 repertoire dans AgLibraryFolder
        if (idLocalCalcul == id_local) {
            idLocalCalcul = (id_local / 2) + 1;
        }
        return idLocalCalcul;
    }
}


