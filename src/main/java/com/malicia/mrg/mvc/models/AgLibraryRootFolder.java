package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.malicia.mrg.app.Context.lrcat;

/**
 * The type Requete sql.
 */
public class AgLibraryRootFolder {

    public static final int TYPE_INCONNU = 0;
    public static final int TYPE_NEW = 1;
    public static final int TYPE_ENC = 2;
    public static final int TYPE_LEG = 4;
    public static final int TYPE_CAT = 3;
    public static final int TYPE_KID = 5;
    private static final Logger LOGGER = LogManager.getLogger(SQLiteJDBCDriverConnection.class);
    public String rootfolderidlocal;
    public String absolutePath;
    public int typeRoot;
    public String name;
    public double nbmaxCat;
    public int nbjouCat;
    public boolean[] IsZoneFacultative;
    public String[] IsZoneDefault;
    public boolean[] IsZoneEditable;
    CatalogLrcat parentLrcat;
    private int nbDelTotal;
    private String[] ratioMaxStar;

    public AgLibraryRootFolder(CatalogLrcat catalogLrcat, String NomRootFolder, String rootfolderidlocal, String absolutePath, int typeRoot) {
        parentLrcat = catalogLrcat;
        name = NomRootFolder;
        this.rootfolderidlocal = rootfolderidlocal;
        this.absolutePath = normalizePath(absolutePath);
        this.typeRoot = typeRoot;
        setratioMaxstarCat("0,0,0,0,0");
        nbmaxCat = 999d;
        nbjouCat = 1;
        setIsZoneEditableCat("Open,Open,Open,Open");
        setIsZoneDefaultCat("*,*,*,*");
        IsZoneFacultativeCatVal("Facul,Facul,Facul,Facul");
    }

    public int getRatioMaxStar(int star) {
        return Integer.parseInt(ratioMaxStar[star - 1]);
    }

    public void FlatRootFolder() throws SQLException, IOException {

        //mise a plat du repertoire @new
        ResultSet rseleAplat = sqlgetListelementnewaclasser(Context.appParam.getString("TempsAdherence"));
        while (rseleAplat.next()) {

            String pathfromroot = rseleAplat.getString(Context.PATH_FROM_ROOT);
            String filename = rseleAplat.getString(Context.LC_IDX_FILENAME);
            String file_id_local = rseleAplat.getString(Context.FILE_ID_LOCAL);

            if (pathfromroot.compareTo("") != 0) {
                String Folder_id_local = String.valueOf(getIdlocalforpathFromRoot(""));

                String source = normalizePath(absolutePath + pathfromroot + filename);

                String rename = ("$" + UUID.randomUUID().toString() + "$" + supprimerbalisedollar(filename)).toLowerCase();
                String destination = normalizePath(absolutePath + rename);
                sqlmovefile(source, destination, Folder_id_local, file_id_local);
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
    public String normalizePath(String path) {
        return path.replaceAll("\\\\", "/").replaceAll("\\/\\/", "/");
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
                        getConditionLike_ssrepRejet("b.pathFromRoot", Context.appParam.getString("ssrepRejet").split(";"), "%", "%") +
                        getConditionLike_ssrepRejet("a.lc_idx_filename", Context.appParam.getString("ssfilerepRejet").split(";"), "%", "") +
                        " ;");
    }

    private String getConditionLike_ssrepRejet(String variable_name, String[] listEle, String likeav, String likeap) {
        String condi = "";
        if (listEle.length > 0) {
            condi += " and ( ";
            condi += "       " + variable_name + " like \""+ likeav + listEle[0] + likeap +"\" ";
            if (listEle.length > 1) {
                int size = listEle.length;
                for (int i = 1; i < size; i++) {
                    condi += "    or ";
                    condi += "       " + variable_name + " like \""+ likeav + listEle[i] + likeap +"\" ";
                }
            }
            condi += "     ) ";
        }
        return condi;
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
                        "a.id_global , " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "a.lc_idx_filename as lc_idx_filename , " +
                        " aiecm.value as CameraModel , " +
                        " strftime('%s', DATETIME( e.captureTime,\"-" + tempsAdherence + "\")) as mint , " +
                        " strftime('%s', DATETIME(e.captureTime,\"+" + tempsAdherence + "\")) as maxt  , " +
                        "b.rootFolder , " +
                        "e.rating , " +
                        "e.pick , " +
                        "e.fileformat , " +
                        "e.orientation , " +
                        "strftime('%s', e.captureTime) as captureTime " +
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
     * Sqlmovefile int.
     *
     * @param source the source
     * @return the int
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    public void sqldeletefile(String source, String fileIdLocal) throws IOException, SQLException {
        SystemFiles.deleteDir(new File(source));

        String sql;
        sql = "" +
                "delete from AgLibraryFile " +
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
        long idlocal = getIdlocalforpathFromRoot(pathFromRoot);

        if (idlocal == 0) {
            idlocal = parentLrcat.sqlGetPrevIdlocalforFolder();
            if (idlocal == 0) {
                throw new IllegalStateException("no more idlocal empty for folder");
            }

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

        String pathFromRootPrev = "first";
        String folder_id_localPrev = "first";
        AgLibrarySubFolder subFolder = null;

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
            String file_id_local = rsele.getString(Context.FILE_ID_LOCAL);
            String cameraModel = rsele.getString("CameraModel");
            long mint = rsele.getLong("mint");
            long maxt = rsele.getLong("maxt");
            Double rating = rsele.getDouble("rating");
            Double pick = rsele.getDouble("pick");
            String fileformat = rsele.getString("fileformat");
            long captureTime = rsele.getLong(Context.CAPTURE_TIME);
            String file_id_global = rsele.getString("id_global");
            String folder_id_local = rsele.getString("folder_id_local");
            String orientation = rsele.getString("orientation");


            if (!(pathFromRootPrev.equals(pathFromRoot)) || !(folder_id_localPrev.equals(folder_id_local))) {
                subFolder = new AgLibrarySubFolder(this, pathFromRoot, folder_id_local, lrcat.listeZ);
            }
            pathFromRootPrev = pathFromRoot;
            folder_id_localPrev = folder_id_local;

            AgLibraryFile eleFile = new AgLibraryFile(subFolder, lcIdxFilename, file_id_local, rating, pick, fileformat, captureTime, file_id_global, orientation);

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
            Date d = new Date(listEle.get(1).getCaptureTime() * 1000);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String date1 = format1.format(d);
            moveListEleD(listEle, date1);
        }
        moveListEle(listFileBazar, Context.appParam.getString("ssrepBazar"));
        parentLrcat.rep.get("repKidz").tranfertdeRep(listElekidz);

    }

    private void tranfertdeRep(List<AgLibraryFile> listEle) throws IOException, SQLException {
        moveListEle(listEle);
    }


    public void moveListEle(List<AgLibraryFile> listFile) throws IOException, SQLException {
        moveListEle(listFile, "$" + UUID.randomUUID().toString() + "$", true);
    }

    public void moveListEleD(List<AgLibraryFile> listFile, String yyyymmdd) throws IOException, SQLException {
        moveListEle(listFile, "$" + "y" + yyyymmdd + "u" + UUID.randomUUID().toString() + "$", true);
    }

    private void moveListEle(List<AgLibraryFile> listFile, String repertoiredest) throws IOException, SQLException {
        moveListEle(listFile, repertoiredest, true);
    }

    private void moveListEle(List<AgLibraryFile> listFile, String repertoiredest, boolean AddprefixFile) throws SQLException, IOException {
        moveListEle(listFile, repertoiredest, AddprefixFile, absolutePath);
    }

    void moveListEle(List<AgLibraryFile> listFile, String repertoiredest, boolean AddprefixFile, String absolutePathdest) throws SQLException, IOException {
        String destdirectoryName = normalizePath(absolutePathdest + repertoiredest);
        LOGGER.debug("moveListEle " + name + " : " + listFile.size() + " -> " + destdirectoryName);

        String Folder_id_local = sqlMkdirRepertory(destdirectoryName);

        for (AgLibraryFile file : listFile) {

            String source = normalizePath(file.getAbsolutePath() + file.getPathFromRoot() + file.getLcIdxFilename());
            String rename = (((AddprefixFile) ? "$" + UUID.randomUUID().toString() + "$" : "") + supprimerbalisedollar(file.getLcIdxFilename())).toLowerCase();
            String destination = normalizePath(destdirectoryName + File.separator + rename);

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
        if (dir.isDirectory()) {
            String[] children = dir.list();
            int success = 0;
            for (int i = 0; i < children.length; i++) {
                success += boucleSupressionRepertoire(new java.io.File(dir, children[i]));
            }
            nbDelTotal += success;
            if (success == children.length) {
                // The directory is now empty directory free so delete it
                if (normalizePath(dir.toString()).compareTo(normalizePath(absolutePath)) != 0  ) {
                    SystemFiles.deleteDir(dir);
                }
                return 1;
            }

        }
        return 0;
    }

    public void rangerRejet() throws SQLException, IOException {

        String ssrepRejetExt = Context.appParam.getString("ssrepRejet").split(";")[0];
        String[] extnfilerepRejet = Context.appParam.getString("ssfilerepRejet").split(";");
        String[] extrepRejet = Context.appParam.getString("ssrepRejet").split(";");

        ResultSet rsele = sqlgetListelementrejetaranger();

        int nbfile = 0;
        int nbrejet = 0;
        while (rsele.next()) {

            // Recuperer les info de l'elements
            String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);
            String lcIdxFilename = rsele.getString(Context.LC_IDX_FILENAME);
            String file_id_local = rsele.getString(Context.FILE_ID_LOCAL);
            String folder_id_local = rsele.getString("folder_id_local");
            nbfile += 1;

            if (isssrepRejetFileAutorized(lcIdxFilename, extnfilerepRejet) && isssrepRejetAutorized(pathFromRoot, extrepRejet)) {

                String source = normalizePath(absolutePath + pathFromRoot + lcIdxFilename);
                String dest = source + "." + ssrepRejetExt;


                sqlmovefile(source, dest, folder_id_local, file_id_local);
                nbrejet += 1;
            }
            if (nbfile % 100 == 0) {
                LOGGER.info("rangerRejet : " + nbrejet + " / " + nbfile + " ");
            }
        }
        LOGGER.info("rangerRejet : " + nbrejet + " / " + nbfile + " ");

    }

    public boolean isssrepRejetFileAutorized(String lcIdxFilename, String[] extn) {
        return Arrays.stream(extn).anyMatch(entry -> lcIdxFilename.toLowerCase().endsWith(entry));
    }

    public boolean isssrepRejetAutorized(String pathFromRoot, String[] extn) {
        return Arrays.stream(extn).anyMatch(entry -> pathFromRoot.toLowerCase().indexOf(entry)>0);
    }

    public ObservableList<String> getlistofpathFromRoottoprocess() throws SQLException {
        ObservableList<String> ret = FXCollections.observableArrayList();

        ResultSet rsele = sqlgetListeRepertoire();

        while (rsele.next()) {
            // Recuperer les info de l'elements
            String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);

            if (isRepertoryToProcess(pathFromRoot)) {
                ret.add(pathFromRoot);
            }

        }
        return ret;
    }

    public ObservableList<AgLibrarySubFolder> getlistofrepertorytoprocess() throws SQLException {
        ObservableList<AgLibrarySubFolder> ret = FXCollections.observableArrayList();

        ResultSet rsele = sqlgetListeRepertoire();

        while (rsele.next()) {
            // Recuperer les info de l'elements
            String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);
            String folder_id_local = rsele.getString("folder_id_local");

            if (isRepertoryToProcess(pathFromRoot)) {
                ret.add(new AgLibrarySubFolder(this, pathFromRoot, folder_id_local, lrcat.listeZ));
            }

        }
        return ret;
    }

    private boolean isRepertoryToProcess(String pathFromRoot) {//todo variabilize
        switch (typeRoot) {
            case TYPE_NEW:
                //repertoire = (@New) $0a19d7c5-e829-4537-9d53-4743397c53d4$
                if (pathFromRoot.matches("\\$[0-9a-zA-Z-]*\\$\\/")) {
                    return true;
                }
                break;
            case TYPE_ENC:
                //repertoire = (#En cours de Traitement) !1er Passe Passe les flags et !2eme Passe les notes
                if (pathFromRoot.matches("![0-9a-zA-Z- _]*\\/[0-9a-zA-Z- _]*\\/")) {
                    return true;
                }
                //repertoire = (#En cours de Traitement) !3eme Passe Affinages\##%cat% et !4eme Passe TAGs\##%cat%
                if (pathFromRoot.matches("![0-9a-zA-Z- _]*\\/##[0-9a-zA-Z- _]*\\/[0-9a-zA-Z- _]*\\/")) {
                    return true;
                }
                break;
            case TYPE_CAT:
                //repertoire = (##%cat%)
                if (pathFromRoot.matches("[0-9a-zA-Z- _]*\\/")) {
                    return true;
                }
                break;
            case TYPE_LEG:
                //repertoire = (!!Legacy) 0000(-0000)/
                if (pathFromRoot.matches("[0-9]{4}-*[0-9]*\\/[0-9a-zA-Z- _]*\\/")) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Sqlget listelementrejetaranger result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlgetListeRepertoire() throws SQLException {
        return parentLrcat.select(
                "select  " +
                        "b.id_local as folder_id_local , " +
                        "b.pathFromRoot , " +
                        "b.rootFolder " +
                        "from AgLibraryFolder b   " +
//                        "Where b.rootFolder =  " + rootfolderidlocal + " " +
                        "where pathFromRoot like  '" + normalizePath(absolutePath) + "/%'" +
                        " ;");
    }


    public void sqlEditPickValue(String fileIdLocal, double pickValue) throws SQLException {

        String sql;

        sql = "update Adobe_images " +
                "set pick = " + pickValue + " " +
                "Where  rootFile =  \"" + fileIdLocal + "\" " +
                " ;";

        parentLrcat.executeUpdate(sql);
    }

    public void sqlEditStarValue(String fileIdLocal, double starValue) throws SQLException {

        String sql;

        sql = "update Adobe_images " +
                "set rating = " + starValue + " " +
                "Where  rootFile =  \"" + fileIdLocal + "\" " +
                " ;";

        parentLrcat.executeUpdate(sql);
    }

    public boolean isCat() {
        return typeRoot == AgLibraryRootFolder.TYPE_CAT;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setratioMaxstarCat(String ratioMaxstarCat) {
        ratioMaxStar = ratioMaxstarCat.split(",");
    }

    public void setIsZoneEditableCat(String IsZoneEditableCat) {
        String[] tmpIsZoneEditable = IsZoneEditableCat.split(",");
        IsZoneEditable = new boolean[tmpIsZoneEditable.length];
        for (int i = 0; i < tmpIsZoneEditable.length; i++) {
            if (tmpIsZoneEditable[i].compareTo("Close") == 0) {
                IsZoneEditable[i] = false;
            }
            if (tmpIsZoneEditable[i].compareTo("Open") == 0) {
                IsZoneEditable[i] = true;
            }
        }
    }
    public void setIsZoneDefaultCat(String IsZoneDefaultCat) {
        String[] tmpIsZoneDefaultCat = IsZoneDefaultCat.split(",");
        IsZoneDefault = new String[tmpIsZoneDefaultCat.length];
        for (int i = 0; i < tmpIsZoneDefaultCat.length; i++) {
            if (tmpIsZoneDefaultCat[i].compareTo("*") == 0) {
                IsZoneDefault[i] = "*";
            } else if (tmpIsZoneDefaultCat[i].compareTo("Space") == 0) {
                IsZoneDefault[i] = "";
            } else {
                IsZoneDefault[i] = tmpIsZoneDefaultCat[i];
            }
        }
    }
    public void IsZoneFacultativeCatVal(String zoneFacultativeCatVal) {
        String[] tmpZoneFacultativeal = zoneFacultativeCatVal.split(",");
        IsZoneFacultative = new boolean[tmpZoneFacultativeal.length];
        for (int i = 0; i < tmpZoneFacultativeal.length; i++) {
            if (tmpZoneFacultativeal[i].compareTo("Facul") == 0) {
                IsZoneFacultative[i] = true;
            }
            if (tmpZoneFacultativeal[i].compareTo("Oblig") == 0) {
                IsZoneFacultative[i] = false;
            }
        }
    }

    public void deleteEle(AgLibraryFile file) throws IOException, SQLException {

        String source = normalizePath(file.getAbsolutePath() + file.getPathFromRoot() + file.getLcIdxFilename());
        LOGGER.debug("deleteEle " + source);

        sqldeletefile(source, file.getFileIdLocal());

    }
}


