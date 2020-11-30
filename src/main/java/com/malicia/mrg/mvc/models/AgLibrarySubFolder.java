package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.malicia.mrg.app.Context.*;
import static com.malicia.mrg.view.AlertMessageUtil.AlertChoixSubfolder;

/**
 * The type Ag library sub folder.
 */
public class AgLibrarySubFolder {

    public static final String UNEXPECTED_VALUE = "Unexpected value: ";
    public static final String OK = "--OK--";
    public static final String KO = "--KO--";
    private static final Logger LOGGER = LogManager.getLogger(AgLibrarySubFolder.class);
    public List<ZoneZ> subFolderFormatZ;
    public List<AgLibraryFile> listFileSubFolder;
    private AgLibraryRootFolder agLibraryRootFolder;
    private String pathFromRoot;
    private int nbelerep;
    private int nbphotoRep;
    private int[] nbetrationetoile;
    private int nbphotoapurger;
    private String ratiophotoaconserver;
    private String statusRep;
    private String folderIdLocal;
    private long nbjourfolder;
    private long dtdeb;
    private long dtfin;
    private int nbSelectionner;

    /**
     * Instantiates a new Ag library sub folder.
     *
     * @param pathFromRoot        the path from root
     * @param folderIdLocal       the folder id local
     * @param agLibraryRootFolder
     * @throws SQLException the sql exception
     */
    public AgLibrarySubFolder(AgLibraryRootFolder agLibraryRootFolder, String pathFromRoot, String folderIdLocal) throws SQLException {
        this.agLibraryRootFolder = agLibraryRootFolder;
        aglibraySubFolderConstructor(agLibraryRootFolder, pathFromRoot, folderIdLocal);
    }

    public AgLibrarySubFolder(AgLibraryRootFolder agLibraryRootFolder, String pathFromRoot) throws SQLException {
        this.agLibraryRootFolder = agLibraryRootFolder;
        String folderIdLocalcalc = String.valueOf(agLibraryRootFolder.getIdlocalforpathFromRoot(pathFromRoot));
        if (folderIdLocalcalc.compareTo("") == 0) {
            folderIdLocalcalc = agLibraryRootFolder.sqlMkdirRepertory(this.getpath());
        }
        aglibraySubFolderConstructor(agLibraryRootFolder, pathFromRoot, folderIdLocalcalc);
    }

    public AgLibrarySubFolder(AgLibrarySubFolder activeRep) throws SQLException {
        this.agLibraryRootFolder = activeRep.agLibraryRootFolder;
        aglibraySubFolderConstructor(activeRep.agLibraryRootFolder, activeRep.pathFromRoot, activeRep.folderIdLocal);
    }

    public String getNbSelectionner() {
        String color;
        if (nbSelectionner == 0) {
            color = "0";
        } else {
            color = "1";
        }
        return "@" + color + "@ " + String.format("%04d", nbSelectionner);
    }

    public AgLibraryRootFolder getAgLibraryRootFolder() {
        return agLibraryRootFolder;
    }

    public void setAgLibraryRootFolder(AgLibraryRootFolder agLibraryRootFolder) {
        this.agLibraryRootFolder = agLibraryRootFolder;
    }

    public String getPathFromRoot() {
        return pathFromRoot;
    }

//    public void setPathFromRoot(String pathFromRoot) {
//        this.pathFromRoot = pathFromRoot;
//    }

    private String getpath() {
        return agLibraryRootFolder.normalizePath(agLibraryRootFolder.absolutePath + pathFromRoot);
    }

    private String getpath(String temppathFromRoot) {
        return agLibraryRootFolder.normalizePath(agLibraryRootFolder.absolutePath + temppathFromRoot);
    }

    public void aglibraySubFolderConstructor(AgLibraryRootFolder agLibraryRootFolder, String pathFromRoot, String folderIdLocal) throws SQLException {

        this.pathFromRoot = pathFromRoot;
        this.folderIdLocal = folderIdLocal;

        listFileSubFolder = new ArrayList();
        ResultSet rs = sqlgetListelementsubfolder();
        while (rs.next()) {
            String fileIdLocal = rs.getString("file_id_local");
            String fileIdGlobal = rs.getString("id_global");
            String lcIdxFilename = rs.getString("lc_idx_filename");
            Double rating = rs.getDouble("rating");
            Double pick = rs.getDouble("pick");
            String fileformat = rs.getString("fileformat");
            String orientation = rs.getString("orientation");
            long captureTime = rs.getLong(Context.CAPTURE_TIME);

            listFileSubFolder.add(new AgLibraryFile(this, lcIdxFilename, fileIdLocal, rating, pick, fileformat, captureTime, fileIdGlobal, orientation));
        }


        refreshValue();


    }

    public String getRepformatZ(int i) {
        if (agLibraryRootFolder.IsZoneDefault[i].compareTo("*")!=0){
            return agLibraryRootFolder.IsZoneDefault[i];
        }
        return subFolderFormatZ.get(i).getLocalValue();
    }

    public int getNbphotoRep() {
        return nbphotoRep;
    }

    public String getDtdebHumain() {
        SimpleDateFormat repDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dtdebH = new Date(dtdeb * 1000);
        return repDateFormat.format(dtdebH);
    }

    /**
     * Gets nbjourfolder.
     *
     * @return the nbjourfolder
     */
    public String getNbjourfolder() {
        return " " + String.format("%03d", nbjourfolder) + " j ";
    }

    /**
     * Gets cat folder.
     *
     * @return the cat folder
     */
    public AgLibraryRootFolder getCatFolder() {
        if (agLibraryRootFolder.isCat()) {
            return agLibraryRootFolder;
        } else {
            return null;
        }
    }


    /**
     * Gets .
     *
     * @param phototoshow the phototoshow
     * @return the
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public Image getimagenumero(int phototoshow) throws IOException, SQLException {
        Image image = null;
        String localUrl;
        if (phototoshow < 0 || phototoshow > listFileSubFolder.size() - 1) {
            localUrl = Context.getLocalVoidPhotoUrl();
            image = new Image(localUrl, false);
        } else {
            File file = new File(listFileSubFolder.get(phototoshow).getPath());
            if (file.exists()) {
                localUrl = file.toURI().toURL().toExternalForm();
                LOGGER.debug(localUrl);
                image = new Image(localUrl, 400, 400, true, false, false);
                //file not supported in jdk
                if (image.isError()) {

                    localUrl = Context.getLocalVoidPhotoUrl();
                    image = new Image(localUrl, false);

                    // get preview file if exist
                    String uuid = listFileSubFolder.get(phototoshow).getFileIdGlobal();
                    ResultSet rs = Previews.getJpegFromUuidFile(uuid);
                    InputStream jpegData;
                    while (rs.next()) {
                        jpegData = rs.getBinaryStream("jpegData");
                        String digest = rs.getString("digest");
                        File filePreview = new File(appParam.getString("RepCatalog") + File.separator + appParam.getString("RepPreviews") + File.separator
                                + uuid.charAt(0) + File.separator + uuid.substring(0, 4) + File.separator + uuid + "-" + digest + ".lrprev");
                        if (filePreview.exists()) {
                            image = new Image(FileLrprev.getLastJpegFromLrprev(filePreview));
                        } else {
                            // get preview bloc (low quality) if exist
                            if (jpegData == null) {
                                localUrl = Context.getLocalVoidPhotoUrl();
                                image = new Image(localUrl, false);
                            } else {
                                image = new Image(jpegData);
                            }
                        }
                    }

                }
            } else {
                localUrl = Context.getLocalErr404PhotoUrl();
                image = new Image(localUrl, false);
            }

        }
        if (image.isError()) {
            LOGGER.log(Level.ERROR, " {} {} ", new Object[]{phototoshow, localUrl});
            image = new Image(new URL(Context.getLocalErrPhotoUrl()).openStream());
        }
        return image;
    }

    /**
     * Valeuractivephotoincrease.
     */
    public void valeuractivephotoincrease(int activeNum) {
        listFileSubFolder.get(activeNum).valeurIncrease();
    }

    /**
     * Valeuractivephotodecrease.
     */
    public void valeuractivephotodecrease(int activeNum) {
        listFileSubFolder.get(activeNum).valeurDecrease();
    }

    public void valeuractivephotounflag(int activeNum) {

        listFileSubFolder.get(activeNum).unflag();
    }

    public void valeuractivephotoflag(int activeNum) {

        listFileSubFolder.get(activeNum).flag();
    }

    /**
     * Sqlget listelementrejetaranger result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlgetListelementsubfolder() throws SQLException {
        return agLibraryRootFolder.parentLrcat.select(
                "select a.id_local as file_id_local , " +
                        "a.id_global , " +
                        "a.lc_idx_filename as lc_idx_filename , " +
                        "e.rating , " +
                        "e.pick , " +
                        "e.fileformat , " +
                        "e.orientation , " +
                        "strftime('%s', e.captureTime) as captureTime " +
                        "from AgLibraryFile a  " +
                        "inner join Adobe_images e  " +
                        " on a.id_local = e.rootFile    " +
                        "Where a.folder =  \"" + folderIdLocal + "\" " +
                        "order by captureTime asc " +
                        " ;");
    }

    /**
     * Refresh compteur.
     */
    public void refreshCompteur() {
        nbelerep = 0;
        nbphotoRep = 0;
        nbetrationetoile = new int[]{0, 0, 0, 0, 0, 0};
        nbSelectionner = 0;
        dtfin = 0;
        dtdeb = 2147483647;
        for (int ifile = 0; ifile < listFileSubFolder.size(); ifile++) {
            AgLibraryFile fi = listFileSubFolder.get(ifile);
            if (!fi.estRejeter() ) {
            nbelerep += 1;
            if (fi.estPhoto()) {
                nbphotoRep += 1;
                calculateStarAndDate(fi);
            }
            }
        }

        calculatenbphotapurger(dtfin, dtdeb);

        calculStatusRep();

    }

    private void calculateStarAndDate(AgLibraryFile fi) {
//        calcul star
        switch ((int) fi.getStarValue()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                if (fi.estSelectionner()) {
                    nbetrationetoile[(int) fi.getStarValue()] += 1;
                }
                break;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + (int) fi.getStarValue());
        }
//        calcul date
        long dt = fi.getCaptureTime();
        if (dt < dtdeb) {
            dtdeb = dt;
        }
        if (dt > dtfin) {
            dtfin = dt;
        }
//        calcul flag
        if ((int) fi.getPick() == 1) {
            nbSelectionner += 1;
        }
    }

    private void calculatenbphotapurger(long dtfin, long dtdeb) {
        int nbphotoRepSelectionner = nbSelectionner;

        //        nbphotoapurger
        nbphotoapurger = 0;
        nbjourfolder = (dtfin - dtdeb) / (60 * 60 * 24) + 1;

        int limitemaxfolder = (int) (agLibraryRootFolder.nbmaxCat * Math.ceil((double) nbjourfolder / agLibraryRootFolder.nbjouCat));
        if (nbphotoRepSelectionner > limitemaxfolder) {
            nbphotoapurger = (nbphotoRepSelectionner - limitemaxfolder);
        }

        //        ratiophotoaconserver
        DecimalFormat df = new DecimalFormat("##.##%");
        double percent;
        if (nbphotoRepSelectionner != 0) {
            percent = 1 - ((double) nbphotoapurger / nbphotoRepSelectionner);
        } else {
            percent = 0;
        }
        ratiophotoaconserver = " " + String.format("%03d", limitemaxfolder) + " ( " + df.format(percent) + " )";
    }

    private void calculStatusRep() {
        statusRep = OK;
        int i;
        for (i = 0; i < subFolderFormatZ.size(); i++) {
            if (subFolderFormatZ.get(i).getLocalValue().compareTo("") == 0 && !agLibraryRootFolder.IsZoneFacultative[i]) {
                statusRep = KO;
                break;
            }
        }
        if (nbphotoapurger != 0) {
            statusRep = KO;
        }

        if (nbphotoRep > 0) {

            statusRep = statuStar(statusRep, 0);
            statusRep = statuStar(statusRep, 1);
            statusRep = statuStar(statusRep, 2);
            statusRep = statuStar(statusRep, 3);
            statusRep = statuStar(statusRep, 4);
            statusRep = statuStar(statusRep, 5);

        }


    }

    private String statuStar(String starstatusRep, int starnbetrationuneetoile) {
        String[] ret = nbetratiovaleur(starnbetrationuneetoile).split("@");
        if (ret[1].compareTo("0") != 0) {
            return KO;
        }
        return starstatusRep;
    }

    private void calculpathFromRoot() {


        pathFromRoot = "";

        int i;
        for (i = 0; i < subFolderFormatZ.size(); i++) {
            if (subFolderFormatZ.get(i).getLocalValue().compareTo("") != 0) {
                pathFromRoot += subFolderFormatZ.get(i).getLocalValue() + appParam.getString("ssrepformatSep");
            }
        }

        if (pathFromRoot.endsWith("_")) {
            pathFromRoot = pathFromRoot.substring(0, pathFromRoot.length() - 1);
        }
        pathFromRoot += "/";
    }

    /**
     * Nbetratiovaleur string.
     *
     * @param valeur the valeur
     * @return the string
     */
    public String nbetratiovaleur(int valeur) {
        int nb = 0;
        int countmin;
        int countmax;
        switch (valeur) {
            case 0:
                nb = nbetrationetoile[valeur];
                countmin = 0;
                countmax = nbSelectionner - nbphotoapurger;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                nb = nbetrationetoile[valeur];
                countmin = 0;
//                countmin = ((nbphotoRep - nbphotoapurger) * (agLibraryRootFolder.getRatioMaxStar(valeur) / divMaxToMinstar)) / 100;
                countmax = ((nbSelectionner - nbphotoapurger) * agLibraryRootFolder.getRatioMaxStar(valeur)) / 100;
                break;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + valeur);
        }
        if (countmax < 1) {
            countmax = 1;
        }
        DecimalFormat df = new DecimalFormat("##.##%");
        String color;
        if (nb >= countmin && nb <= countmax) {
            color = "0";
        } else {
            color = "1";
        }
        return "@" + color + "@ " + String.format("%02d", nb) + " (" + String.format("%02d", countmin) + "/" + String.format("%02d", countmax) + ")";
    }

    /**
     * Gets nbelerep.
     *
     * @return the nbelerep
     */
    public String getNbelerep() {
        return " " + String.format("%04d", nbelerep);
    }


    /**
     * Gets nbphoto rep.
     *
     * @return the nbphoto rep
     */
    public String getNbphotoRepHuman() {
        return " " + String.format("%04d", nbphotoRep);
    }

    /**
     * Gets ratiophotoaconserver.
     *
     * @return the ratiophotoaconserver
     */
    public String getRatiophotoaconserver() {
        return ratiophotoaconserver;
    }

    /**
     * Gets nbphotoapurger.
     *
     * @return the nbphotoapurger
     */
    public String getNbphotoapurger() {
        String color;
        if (nbphotoapurger == 0) {
            color = "0";
        } else {
            color = "1";
        }
        return "@" + color + "@ " + String.format("%04d", nbphotoapurger);
    }

    public String getActivephotoFlag(int activeNum) {
        String flag = " ";
        if (activeNum == -1 || activeNum > listFileSubFolder.size() - 1) {
            return "";
        }
        switch ((int) listFileSubFolder.get(activeNum).getPick()) {
            case -1:
//                flag = "⚐";
                flag = "X";
                break;
            case 0:
//                flag = "⚐";
                flag = "?";
                break;
            case 1:
//                flag = "\uD83C\uDFC1";
                flag = "";
                break;
        }
        return flag;
    }

    /**
     * Gets activephoto valeur.
     *
     * @param activeNum
     * @return the activephoto valeur
     */
    public String getActivephotoValeur(int activeNum) {

        if (activeNum == -1 || activeNum > listFileSubFolder.size() - 1) {
            return "---";
        }
        SimpleDateFormat repDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dtdebH = new Date(listFileSubFolder.get(activeNum).getCaptureTime() * 1000);

        String infoPrec = String.format("%04d", activeNum + 1) + " - " + repDateFormat.format(dtdebH);


        switch ((int) listFileSubFolder.get(activeNum).getStarValue()) {
            case -1:
                return infoPrec + "\n" + "     \uD83D\uDD71 \uD83D\uDD71 \uD83D\uDD71 " + "\n" + appParam.getString("valeurCorbeille");
            case 0:
                return infoPrec + "\n" + "           " + "\n" + appParam.getString("valeurZero__");
            case 1:
                return infoPrec + "\n" + " ★         " + "\n" + appParam.getString("valeur1star_");
            case 2:
                return infoPrec + "\n" + " ★ ★       " + "\n" + appParam.getString("valeur2stars");
            case 3:
                return infoPrec + "\n" + " ★ ★ ★     " + "\n" + appParam.getString("valeur3stars");
            case 4:
                return infoPrec + "\n" + " ★ ★ ★ ★   " + "\n" + appParam.getString("valeur4stars");
            case 5:
                return infoPrec + "\n" + " ★ ★ ★ ★ ★ " + "\n" + appParam.getString("valeur5stars");
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + (int) listFileSubFolder.get(activeNum).getStarValue());
        }
    }


    /**
     * Gets status rep.
     *
     * @return the status rep
     */
    public String getStatusRep() {
        return statusRep;
    }

    @Override
    public String toString() {
        //return getNbelerep() + " " + getNbphotoRepHuman() + " #" + agLibraryRootFolder.typeRoot + " " + pathFromRoot;
        return getAgLibraryRootFolder().name + " [" + getNbphotoRepHuman() + " ] " + pathFromRoot;
    }


    public void setRotateToFile(int activeNum, int addRotate) {
        listFileSubFolder.get(activeNum).setAddRotate(addRotate);
    }

    public int getRotateFromphotonum(int photonum) {
        if (photonum < 0 || photonum > listFileSubFolder.size() - 1) {
            return 0;
        }
        return listFileSubFolder.get(photonum).getAddRotate();
    }

    public void setrepformatZ(int i, String valeur) {
        if (valeur.compareTo("null") == 0) {
            valeur = "";
        }
        subFolderFormatZ.get(i).setLocalValue(valeur);
    }

    public ObservableList<String> personalizelist(ZoneZ listeZ) {

        ObservableList<String> pListeZ = FXCollections.observableArrayList(listeZ.listeEleZone);

        if (listeZ.typeDeListeDeZone.compareTo("£") == 0) {
            pListeZ.forEach(tab -> {
                if (tab.compareTo("DATE") == 0) {
                    pListeZ.remove(tab);
                    pListeZ.add(this.getDtdebHumain());
                }
            });
        }

        return pListeZ;
    }

    public void execmodification(AgLibrarySubFolder activeRepDest, AgLibrarySubFolder activeRepDestSplit) throws IOException, SQLException {
//        if (activeRepDestSplit.nbelerep>0) {
//            LOGGER.info("Split de " + activeRepDestSplit.nbelerep );
//            activeRepDestSplit.getAgLibraryRootFolder().moveListEle(activeRepDestSplit.listFileSubFolder);
//        }

        activeRepDest.calculpathFromRoot();

        int i;
        for (i = 0; i < activeRepDest.subFolderFormatZ.size(); i++) {
            ZoneZ cursubFolderFormatZ = activeRepDest.subFolderFormatZ.get(i);
            if (cursubFolderFormatZ.typeDeListeDeZone.compareTo("@") == 0) {
                if (!cursubFolderFormatZ.listeEleZone.contains(cursubFolderFormatZ.getLocalValue()) && !activeRepDest.agLibraryRootFolder.IsZoneFacultative[i]) {


                    ButtonType[] buttonType = new ButtonType[cursubFolderFormatZ.keywordMaitrePossible.size() + 1];
                    int y;
                    for (y = 0; y < cursubFolderFormatZ.keywordMaitrePossible.size(); y++) {
                        buttonType[y] = new ButtonType(cursubFolderFormatZ.keywordMaitrePossible.get(y));
                    }
                    buttonType[y] = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);


                    Optional<ButtonType> result = AlertChoixSubfolder(cursubFolderFormatZ, buttonType);

                    int ii;
                    for (ii = 0; ii < cursubFolderFormatZ.keywordMaitrePossible.size(); ii++) {
                        if (result.get() == buttonType[ii]) {
                            lrcat.sqlcreateKeyword(cursubFolderFormatZ.keywordMaitrePossible.get(ii), cursubFolderFormatZ.getLocalValue());
                            lrcat.setListeZ();
                        }
                    }

                }
            }
        }

        activeRepDest.sqlmoveRepertoryWithSubDirectory(this.getpath(),
                activeRepDest.getpath().toLowerCase(),
                this.pathFromRoot,
                activeRepDest.pathFromRoot.toLowerCase(),
                this.agLibraryRootFolder.rootfolderidlocal,
                activeRepDest.agLibraryRootFolder.rootfolderidlocal);

        List<AgLibraryFile> listFileSubFolderRejet = new ArrayList();

        activeRepDest.listFileSubFolder.forEach(ele -> {
                    if (!ele.estSelectionner()) {
                        listFileSubFolderRejet.add(ele);
                    }
                    if (ele.isEdited()) {
                        if (ele.estRejeter()) {
                            listFileSubFolderRejet.add(ele);
                        }
                        try {
                            ele.enregistrerStarValue();
                            ele.enregistrerPickValue();
                        } catch (SQLException e) {
                            popupalertException(e);
                            excptlog(e, LOGGER);
                        }
                    }

                }
        );
        if (!listFileSubFolderRejet.isEmpty()) {
            //move les elements dans le sous repertoire rejet
            activeRepDest.agLibraryRootFolder.moveListEle(listFileSubFolderRejet, activeRepDest.getpathFromRootrejet(), false, activeRepDest.agLibraryRootFolder.absolutePath);
        }


    }

    private String getpathFromRootrejet() {
        return agLibraryRootFolder.normalizePath(pathFromRoot + File.separator + Context.appParam.getString("ssrepRejet").split(";")[0]);
    }


    protected void sqlmoveRepertoryWithSubDirectory(String source,
                                                    String destination,
                                                    String pathFromRootsrc,
                                                    String pathFromRootdest,
                                                    String rootFoldersrc,
                                                    String rootFolderdest) throws IOException, SQLException {
//move repertory and subdirectory
        SystemFiles.moveRepertory(source, destination);


        String sql;
        sql = "" +
                "update AgLibraryFolder " +
                "set pathFromRoot = " +
                "replace( pathFromRoot, '" + pathFromRootsrc + "' , '" + pathFromRootdest + "' ) , " +
                " rootFolder = " + rootFolderdest + " " +
                "where pathFromRoot like '" + pathFromRootsrc + "%' " +
                " and rootFolder = " + rootFoldersrc + "" +
                ";";
        agLibraryRootFolder.parentLrcat.executeUpdate(sql);


    }


    public void refreshValue() {
        subFolderFormatZ = new ArrayList<ZoneZ>();

        int i;
        for (i = 0; i < (agLibraryRootFolder.parentLrcat.ListeZ.size()); i++) {
            subFolderFormatZ.add(new ZoneZ(agLibraryRootFolder.parentLrcat.ListeZ.get(i)));
        }

        refreshCompteur();

        String[] part = pathFromRoot.replace("/", "").split(appParam.getString("ssrepformatSep"));
        for (i = 0; i < part.length && i < subFolderFormatZ.size(); i++) {
            if (personalizelist(subFolderFormatZ.get(i)).contains(part[i]) || agLibraryRootFolder.IsZoneFacultative[i]) {
                setrepformatZ(i, part[i]);
            }
        }

        calculStatusRep();
    }

    public boolean fileFiltrer(int phototoshow, boolean estphoto, int nbstar, boolean estrejeter, boolean estselectionner) {
        boolean ret = true;
        if (estphoto && !listFileSubFolder.get(phototoshow).estPhoto()) {
            ret = false;
        }
        if (nbstar > -1 && listFileSubFolder.get(phototoshow).getStarValue() != nbstar) {
            ret = false;
        }
        if (estrejeter && !listFileSubFolder.get(phototoshow).estRejeter()) {
            ret = false;
        }
        if (estselectionner && !listFileSubFolder.get(phototoshow).estSelectionner()) {
            ret = false;
        }
        return ret;
    }

    public int filsize() {
        return listFileSubFolder.size();
    }

    public void flagAllFile() {
        listFileSubFolder.forEach(ele -> {
                    ele.flag();
                }
        );
    }

    public AgLibrarySubFolder split(int activephotoNum) throws SQLException {
        AgLibrarySubFolder splitedAgLibrarySubFolder = new AgLibrarySubFolder(this);
        List<AgLibraryFile> listFileSubFolderThis = new ArrayList();
        List<AgLibraryFile> listFileSubFolderSplit = new ArrayList();
        for (int ifile = 0; ifile < listFileSubFolder.size(); ifile++) {
            if (ifile < activephotoNum) {
                listFileSubFolderThis.add(listFileSubFolder.get(ifile));
            }
            if (ifile >= activephotoNum) {
                listFileSubFolderSplit.add(listFileSubFolder.get(ifile));
            }
        }
        this.listFileSubFolder = listFileSubFolderThis;
        splitedAgLibrarySubFolder.listFileSubFolder = listFileSubFolderSplit;
        return splitedAgLibrarySubFolder;
    }

    public void deletePhoto(int activephotoNum) throws IOException, SQLException {
        LOGGER.info("deleteEle " + this.listFileSubFolder.get(activephotoNum).getAbsolutePath());
        this.getAgLibraryRootFolder().deleteEle(this.listFileSubFolder.get(activephotoNum));
        this.listFileSubFolder.remove(activephotoNum);

    }
}