package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Requete sql.
 */
public class AgLibraryRootFolder {

    private final Logger LOGGER;
    public String rootfolderidlocal;
    public String absolutePath;
    public String name;
    private CatalogLrcat parentLrcat;
    private int nbDelTotal;

    {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }


    public AgLibraryRootFolder(CatalogLrcat catalogLrcat, String NomRootFolder) {
        parentLrcat = catalogLrcat;
        name = NomRootFolder;
    }


    public void FlatRootFolder() throws SQLException, IOException {

        //mise a plat du repertoire @new
        ResultSet rseleAplat = sqlgetListelementnewaclasser(Context.appParam.getString("TempsAdherence"));
        while (rseleAplat.next()) {

            String pathfromroot = rseleAplat.getString(Context.PATH_FROM_ROOT);
            String filename = rseleAplat.getString(Context.LC_IDX_FILENAME);
            String file_id_local = rseleAplat.getString("file_id_local");

            if (pathfromroot.compareTo("") != 0) {
                String Folder_id_local = String.valueOf(getIdlocalforpathFromRoot(""));

                String source = normalizePath(absolutePath + pathfromroot + filename);

                String rename = ("$" + UUID.randomUUID().toString() + "$" + supprimerbalisedollar(filename)).toLowerCase();
                String destination = normalizePath(absolutePath + rename);
                sqlmovefile(source , destination, Folder_id_local, file_id_local);
            }

        }
    }


    private String supprimerbalisedollar(String lcIdxFilename) {
        Pattern pattern = Pattern.compile("(\\$.*\\$)*(.*)");
        Matcher matcher = pattern.matcher(lcIdxFilename);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";

    }

    /**
     * Normalize path string.
     *
     * @param path the path
     * @return the string
     */
    private String normalizePath(String path) {
        return path.replaceAll("\\\\", "/");
    }


    /**
     * Sqlget listelementrejetaranger result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlgetListelementrejetaranger() throws SQLException {
        return parentLrcat.select(
                "select a.id_local as file_id_local, " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "a.lc_idx_filename as lc_idx_filename , " +
                        "b.rootFolder " +
                        "from AgLibraryFile a  " +
                        "inner join AgLibraryFolder b   " +
                        " on a.folder = b.id_local  " +
                        "Where b.rootFolder =  " + rootfolderidlocal + " " +
                        "  and b.pathFromRoot like \"%" + Context.appParam.getString("ssrepRejet") + "%\" " +
                        " ;");
    }

    /**
     * Sqlget listelementnewaclasser result set.
     *
     * @param tempsAdherence the temps adherence
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlgetListelementnewaclasser(String tempsAdherence) throws SQLException {
        return parentLrcat.select(
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
                        "Where b.rootFolder =  " + rootfolderidlocal + " " +
                        "order by captureTime asc " +
//                        "limit 10 " +
                        ";");


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
    public void sqlmovefile(String source, String destination, String folderIdLocaldestination, String fileIdLocal) throws IOException, SQLException {
        SystemFiles.moveFile(source, destination);

        File fdest = new File(destination);
        String sql;
        sql = "" +
                "update AgLibraryFile " +
                "set folder =  " + folderIdLocaldestination + " ," +
                " baseName =  '" + FilenameUtils.getBaseName(destination) + "' , " +
                " idx_filename =  '" + fdest.getName() + "' , " +
                " lc_idx_filename =  '" + fdest.getName().toLowerCase() + "'  " +
                "where id_local =  " + fileIdLocal + " " +
                ";";
        parentLrcat.executeUpdate(sql);


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
        return parentLrcat.select(
                "select  " +
                        " b.id_local " +
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
        return parentLrcat.select(
                "select  " +
                        " a.id_local , " +
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
    public String sqlMkdirRepertory(String directoryName) throws SQLException {

        SystemFiles.mkdir(directoryName);


        String pathFromRoot = normalizePath(directoryName.replace(absolutePath, "") + File.separator);


//test if folder deja exist
        long idlocal  = getIdlocalforpathFromRoot(pathFromRoot);

        if (idlocal == 0) {
            idlocal = parentLrcat.sqlGetPrevIdlocalforFolder();
            if (idlocal == 0) {
                throw new IllegalStateException("no more idlocal empty for folder");
            }
//        sqlSetAdobeentityIDCounter(idlocal);

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
                    "'" + rootfolderidlocal + "')" +
                    ";";
            parentLrcat.executeUpdate(sql);
        }

        return String.valueOf(idlocal);
    }

    public long getIdlocalforpathFromRoot(String pathFromRoot) throws SQLException {
        ResultSet rsexist = parentLrcat.select(
                "select id_local from AgLibraryFolder where " +
                        " pathFromRoot = " + "'" + pathFromRoot + "' " +
                        " and rootFolder = " + "'" + rootfolderidlocal + "' " +
                        "");
        long idlocal = 0;
        while (rsexist.next()) {
            idlocal = rsexist.getLong("id_local");
        }
        return idlocal;
    }


    public void RegoupFileByAdherence() throws SQLException, IOException {

        //Regroupement
        ResultSet rsele = sqlgetListelementnewaclasser(Context.appParam.getString("TempsAdherence"));

        List<AgLibraryFile> listFileBazar = new ArrayList();
        List<AgLibraryFile> listElekidz = new ArrayList();
        List<List<AgLibraryFile>> listGrpEletmp = new ArrayList();

        List<AgLibraryFile> listEletmp = new ArrayList();

        List<String> listkidsModel = Context.getKidsModelList();

        long maxprev = 0;
        while (rsele.next()) {

            // Recuperer les info de l'elements
            String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);
            String lcIdxFilename = rsele.getString(Context.LC_IDX_FILENAME);
            String file_id_local = rsele.getString("file_id_local");
            String cameraModel = rsele.getString("CameraModel");
            long mint = rsele.getLong("mint");
            long maxt = rsele.getLong("maxt");

            AgLibraryFile eleFile = new AgLibraryFile(absolutePath, pathFromRoot, lcIdxFilename, file_id_local, this);

            if (listkidsModel.contains(cameraModel)) {
                listElekidz.add(eleFile);
            } else {
                if (mint > maxprev) {

                    if (listEletmp.size() > Context.getThresholdBazar()) {

                        listGrpEletmp.add(listEletmp);

                    } else {
                        listFileBazar.addAll(listEletmp);
                    }

                    listEletmp = new ArrayList();

                }
                maxprev = maxt;
                listEletmp.add(eleFile);
            }

        }
        if (listEletmp.size() > Context.getThresholdBazar()) {
            listGrpEletmp.add(listEletmp);
        } else {
            listFileBazar.addAll(listEletmp);
        }


        //deplacement des group d'elements
        for (List<AgLibraryFile> listEle : listGrpEletmp) {
            moveListEle(listEle);
        }
        moveListEle(listFileBazar, Context.appParam.getString("ssrepBazar"));
        parentLrcat.rep.get("repKidz").tranfertdeRep(listElekidz);

    }

    private void tranfertdeRep(List<AgLibraryFile> listEle) throws IOException, SQLException {
        moveListEle(listEle);
    }


    private void moveListEle(List<AgLibraryFile> listFile) throws IOException, SQLException {
        moveListEle(listFile, "$" + UUID.randomUUID().toString() + "$", true);
    }

    private void moveListEle(List<AgLibraryFile> listFile, String repertoiredest) throws IOException, SQLException {
        moveListEle(listFile, repertoiredest, true);
    }

    private void moveListEle(List<AgLibraryFile> listFile, String repertoiredest, boolean AddprefixFile) throws SQLException, IOException {
        String destdirectoryName = normalizePath(absolutePath + repertoiredest);
        LOGGER.info("moveListEle " + name + " : " + listFile.size() + " -> " + destdirectoryName);

        String Folder_id_local = sqlMkdirRepertory(destdirectoryName);

        for (AgLibraryFile file : listFile) {

            String source = normalizePath(file.getAbsolutePath() + file.getPathFromRoot() + file.getLcIdxFilename());
            String rename = (((AddprefixFile) ? "$" + UUID.randomUUID().toString() + "$" : "") + supprimerbalisedollar(file.getLcIdxFilename())).toLowerCase();
            String destination = normalizePath(destdirectoryName + java.io.File.separator + rename);

            sqlmovefile(source, destination, Folder_id_local, file.getFileIdLocal());

        }
    }


    public int DeleteEmptyDirectory() throws IOException, SQLException {

        java.io.File directory = new java.io.File(absolutePath + java.io.File.separator);
        nbDelTotal = 0;
        boucleSupressionRepertoire(directory);
        return nbDelTotal;
    }


    /**
     * Boucle supression repertoire physique boolean.
     */
    private int boucleSupressionRepertoire(java.io.File dir) throws IOException, SQLException {
        boolean returnVal = false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            int success = 0;
            for (int i = 0; i < children.length; i++) {
                success += boucleSupressionRepertoire(new java.io.File(dir, children[i]));
            }
            nbDelTotal += success;
            if (success == children.length) {
                // The directory is now empty directory free so delete it
                returnVal = SystemFiles.deleteDir(dir);
                if (returnVal) {
                    return 1;
                }

            }

        }
        return 0;
    }

    public void rangerRejet() throws SQLException, IOException {

        ResultSet rsele = sqlgetListelementrejetaranger();

        while (rsele.next()) {

            // Recuperer les info de l'elements
            String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);
            String lcIdxFilename = rsele.getString(Context.LC_IDX_FILENAME);
            String file_id_local = rsele.getString("file_id_local");
            String folder_id_local = rsele.getString("folder_id_local");

            String source = normalizePath(absolutePath + pathFromRoot + lcIdxFilename);
            String dest = source + ".rejet";


            sqlmovefile(source, dest, folder_id_local, file_id_local);

        }
    }
}


