package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.controllers.MainFrameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.malicia.mrg.app.Context.*;

/**
 * The type Ag library sub folder.
 */
public class AgLibrarySubFolder {

    public static final String UNEXPECTED_VALUE = "Unexpected value: ";
    public static final String OK = "--OK--";
    public static final String KO = "--KO--";
    public List<ZoneZ> subFolderFormatZ;
    public AgLibraryRootFolder agLibraryRootFolder;
    private String pathFromRoot;
    private Logger logger;
    private List<AgLibraryFile> listFileSubFolder;
    private int nbelerep;
    private int nbphotoRep;
    private int nbetrationzeroetoile;
    private int nbetrationuneetoile;
    private int nbetrationdeuxetoile;
    private int nbetrationtroisetoile;
    private int nbetrationquatreetoile;
    private int nbetrationcinqetoile;
    private int nbphotoapurger;
    private String ratiophotoaconserver;
    private String statusRep;
    private String folderIdLocal;
    private long nbjourfolder;
    private long dtdeb;
    private long dtfin;

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

    public String getPathFromRoot() {
        return pathFromRoot;
    }

    public void setPathFromRoot(String pathFromRoot) {
        this.pathFromRoot = pathFromRoot;
    }

    private String getpath() {
        return agLibraryRootFolder.normalizePath(agLibraryRootFolder.absolutePath + pathFromRoot);
    }

    public void aglibraySubFolderConstructor(AgLibraryRootFolder agLibraryRootFolder, String pathFromRoot, String folderIdLocal) throws SQLException {
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        this.pathFromRoot = pathFromRoot;
        this.folderIdLocal = folderIdLocal;

        listFileSubFolder = new ArrayList();
        ResultSet rs = sqlgetListelementsubfolder();
        while (rs.next()) {
            String fileIdLocal = rs.getString("file_id_local");
            String fileIdGlobal = rs.getString("id_global");
            String lcIdxFilename = rs.getString("lc_idx_filename");
            Double rating = rs.getDouble("rating");
            String fileformat = rs.getString("fileformat");
            long captureTime = rs.getLong(Context.CAPTURE_TIME);

            listFileSubFolder.add(new AgLibraryFile(this, lcIdxFilename, fileIdLocal, rating, fileformat, captureTime, fileIdGlobal));
        }


        refreshValue();


    }

    public String getRepformatZ(int i) {
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
    public Image getimagenumero(int phototoshow) throws IOException {
        Image image = null;
        String localUrl;
        if (phototoshow < 0 || phototoshow > listFileSubFolder.size() - 1) {
            localUrl = Context.getLocalVoidPhotoUrl();
            image = new Image(localUrl, false);
        } else {
            File file = new File(listFileSubFolder.get(phototoshow).getPath());
            if (file.exists()) {
                localUrl = file.toURI().toURL().toExternalForm();
                logger.info(localUrl);
                image = new Image(localUrl, 400, 400, true, false, false);
            } else {
                localUrl = Context.getLocalErr404PhotoUrl();
                image = new Image(localUrl, false);
            }

        }
        if (image.isError()) {
            logger.log(Level.INFO, " {} {} ", new Object[]{phototoshow, localUrl});
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
                        "e.fileformat ," +
                        "strftime('%s', e.captureTime) as captureTime " +
                        "from AgLibraryFile a  " +
                        "inner join Adobe_images e  " +
                        " on a.id_local = e.rootFile    " +
                        "Where a.folder =  \"" + folderIdLocal + "\" " +
                        " ;");
    }

    /**
     * Refresh compteur.
     */
    public void refreshCompteur() {
        nbelerep = 0;
        nbphotoRep = 0;
        nbetrationzeroetoile = 0;
        nbetrationuneetoile = 0;
        nbetrationdeuxetoile = 0;
        nbetrationtroisetoile = 0;
        nbetrationquatreetoile = 0;
        nbetrationcinqetoile = 0;
        dtfin = 0;
        dtdeb = 2147483647;
        for (int ifile = 0; ifile < listFileSubFolder.size(); ifile++) {
            AgLibraryFile fi = listFileSubFolder.get(ifile);
            if (!fi.estRejeter()) {
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
        switch ((int) fi.getStarValue()) {
            case 0:
                nbetrationzeroetoile += 1;
                break;
            case 1:
                nbetrationuneetoile += 1;
                break;
            case 2:
                nbetrationdeuxetoile += 1;
                break;
            case 3:
                nbetrationtroisetoile += 1;
                break;
            case 4:
                nbetrationquatreetoile += 1;
                break;
            case 5:
                nbetrationcinqetoile += 1;
                break;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + (int) fi.getStarValue());
        }
        long dt = fi.getCaptureTime();
        if (dt < dtdeb) {
            dtdeb = dt;
        }
        if (dt > dtfin) {
            dtfin = dt;
        }
    }

    private void calculatenbphotapurger(long dtfin, long dtdeb) {
        //        nbphotoapurger
        nbphotoapurger = 0;
        nbjourfolder = (dtfin - dtdeb) / (60 * 60 * 24) + 1;

        int limitemaxfolder = (int) (agLibraryRootFolder.nbmaxCat * Math.ceil((double) nbjourfolder / agLibraryRootFolder.nbjouCat));
        if (nbphotoRep > limitemaxfolder) {
            nbphotoapurger = (nbphotoRep - limitemaxfolder);
        }

        //        ratiophotoaconserver
        DecimalFormat df = new DecimalFormat("##.##%");
        double percent;
        if (nbphotoRep != 0) {
            percent = 1 - ((double) nbphotoapurger / nbphotoRep);
        } else {
            percent = 0;
        }
        ratiophotoaconserver = " " + String.format("%03d", limitemaxfolder) + " ( " + df.format(percent) + " )";
    }

    private void calculStatusRep() {
        statusRep = OK;
        int i;
        for (i = 0; i < subFolderFormatZ.size(); i++) {
                if (subFolderFormatZ.get(i).getLocalValue().compareTo("") == 0) {
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
                pathFromRoot += subFolderFormatZ.get(i).getLocalValue() + Context.appParam.getString("ssrepformatSep");
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
                nb = nbetrationzeroetoile;
                countmin = 0;
                countmax = nbphotoRep - nbphotoapurger;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                nb = nbetrationuneetoile;
                countmin = ((nbphotoRep - nbphotoapurger) * (agLibraryRootFolder.getRatioMaxStar(valeur) / divMaxToMinstar)) / 100;
                countmax = ((nbphotoRep - nbphotoapurger) * agLibraryRootFolder.getRatioMaxStar(valeur)) / 100;
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
        String dth = repDateFormat.format(dtdebH);

        switch ((int) listFileSubFolder.get(activeNum).getStarValue()) {
            case -1:
                return dth + "\n" + "     \uD83D\uDD71 \uD83D\uDD71 \uD83D\uDD71 " + "\n" + Context.appParam.getString("valeurCorbeille");
            case 0:
                return dth + "\n" + "           " + "\n" + Context.appParam.getString("valeurZero__");
            case 1:
                return dth + "\n" + " ★         " + "\n" + Context.appParam.getString("valeur1star_");
            case 2:
                return dth + "\n" + " ★ ★       " + "\n" + Context.appParam.getString("valeur2stars");
            case 3:
                return dth + "\n" + " ★ ★ ★     " + "\n" + Context.appParam.getString("valeur3stars");
            case 4:
                return dth + "\n" + " ★ ★ ★ ★   " + "\n" + Context.appParam.getString("valeur4stars");
            case 5:
                return dth + "\n" + " ★ ★ ★ ★ ★ " + "\n" + Context.appParam.getString("valeur5stars");
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
        return getNbelerep() + " " + getNbphotoRepHuman() + " #" + agLibraryRootFolder.typeRoot + " " + pathFromRoot;
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

    public void execmodification(AgLibrarySubFolder activeRepDest) throws IOException, SQLException {

        activeRepDest.calculpathFromRoot();

        int i;
        for (i = 0; i < activeRepDest.subFolderFormatZ.size(); i++) {
            ZoneZ cursubFolderFormatZ = activeRepDest.subFolderFormatZ.get(i);
            if (cursubFolderFormatZ.typeDeListeDeZone.compareTo("@") == 0) {
                if (!cursubFolderFormatZ.listeEleZone.contains(cursubFolderFormatZ.getLocalValue()) && activeRepDest.agLibraryRootFolder.sszVal[i]) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog with Custom Actions");
                    alert.setHeaderText("Choice KeywordMaster for #" + cursubFolderFormatZ.getLocalValue() + "#");
                    alert.setContentText("Choose Keyword Master in " + cursubFolderFormatZ.titreZone);

                    ButtonType[] buttonType = new ButtonType[cursubFolderFormatZ.keywordMaitrePossible.size() + 1];
                    int ii;
                    for (ii = 0; ii < cursubFolderFormatZ.keywordMaitrePossible.size(); ii++) {
                        buttonType[ii] = new ButtonType(cursubFolderFormatZ.keywordMaitrePossible.get(ii));
                    }
                    buttonType[ii] = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(buttonType);

                    Optional<ButtonType> result = alert.showAndWait();

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
                activeRepDest.getpath(),
                this.pathFromRoot,
                activeRepDest.pathFromRoot,
                this.agLibraryRootFolder.rootfolderidlocal,
                activeRepDest.agLibraryRootFolder.rootfolderidlocal);


        List<AgLibraryFile> listFileSubFolderRejet = new ArrayList();

        activeRepDest.listFileSubFolder.forEach(ele -> {
                    if (ele.isEdited()) {
                        if (ele.estRejeter()) {
                            listFileSubFolderRejet.add(ele);
                        }
                        try {
                            ele.enregistrerStarValue();
                        } catch (SQLException e) {
                            MainFrameController.popupalertException(e);
                            MainFrameController.excptlog(e);
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
        return agLibraryRootFolder.normalizePath(pathFromRoot + File.separator + Context.appParam.getString("ssrepRejet"));
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

        String[] part = pathFromRoot.replace("/", "").split(Context.appParam.getString("ssrepformatSep"));
        for (i = 0; i < part.length && i < subFolderFormatZ.size(); i++) {
            if (personalizelist(subFolderFormatZ.get(i)).contains(part[i]) || !agLibraryRootFolder.sszVal[i] ) {
                setrepformatZ(i, part[i]);
            }
        }

        calculStatusRep();
    }

    public boolean fileFiltrer(int phototoshow, boolean estphoto, int nbstar, boolean estrejeter) {
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
        return ret;
    }

    public int filsize() {
        return listFileSubFolder.size();
    }
}