package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.photo.repCat;
import com.malicia.mrg.mvc.controllers.MainFrameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Ag library sub folder.
 */
public class AgLibrarySubFolder extends AgLibraryRootFolder {
    /**
     * The constant UNEXPECTED_VALUE.
     */
    public static final String UNEXPECTED_VALUE = "Unexpected value: ";
    public static final String OK = "--OK--";
    public static final String KO = "------";
    private Logger logger;
    private Map<Integer, String> repformatZ = new HashMap();
    /**
     * The List file sub folder.
     */
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
    public String pathFromRoot;
    private String folderIdLocal;
    private Map<Integer, Integer> activephotoNum;
    private int activeNum;
    private int zero = 2;
    private long nbjourfolder;
    private repCat categorie;
    private long dtdeb;
    private long dtfin;
    private boolean isDest;

    /**
     * Instantiates a new Ag library sub folder.
     *
     * @param pathFromRoot        the path from root
     * @param folderIdLocal       the folder id local
     * @param agLibraryRootFolder
     * @throws SQLException the sql exception
     */
    public AgLibrarySubFolder(AgLibraryRootFolder agLibraryRootFolder, String pathFromRoot, String folderIdLocal) throws SQLException {
        super(agLibraryRootFolder.parentLrcat, agLibraryRootFolder.name, agLibraryRootFolder.rootfolderidlocal, agLibraryRootFolder.absolutePath, agLibraryRootFolder.typeRoot);
        AglibraySubFolderConstructor(agLibraryRootFolder, pathFromRoot, folderIdLocal);
    }

    public AgLibrarySubFolder(AgLibraryRootFolder agLibraryRootFolder, String pathFromRoot) throws SQLException {
        super(agLibraryRootFolder.parentLrcat, agLibraryRootFolder.name, agLibraryRootFolder.rootfolderidlocal, agLibraryRootFolder.absolutePath, agLibraryRootFolder.typeRoot);
        String folderIdLocalcalc = String.valueOf(agLibraryRootFolder.getIdlocalforpathFromRoot(pathFromRoot));
        if (folderIdLocalcalc.compareTo("") == 0) {
            folderIdLocalcalc = sqlMkdirRepertory(this.Getpath());
        }
        AglibraySubFolderConstructor(agLibraryRootFolder, pathFromRoot, folderIdLocalcalc);
    }

    public AgLibrarySubFolder(AgLibraryRootFolder agLibraryRootFolder, String pathFromRoot, String folderIdLocal, boolean isDest) throws SQLException {
        super(agLibraryRootFolder.parentLrcat, agLibraryRootFolder.name, agLibraryRootFolder.rootfolderidlocal, agLibraryRootFolder.absolutePath, agLibraryRootFolder.typeRoot);
        this.isDest = isDest;
        AglibraySubFolderConstructor(agLibraryRootFolder, pathFromRoot, folderIdLocal);
    }

    public AgLibrarySubFolder(AgLibrarySubFolder activeRep) throws SQLException {
        super(activeRep.parentLrcat, activeRep.name, activeRep.rootfolderidlocal, activeRep.absolutePath, activeRep.typeRoot);
        AglibraySubFolderConstructor(activeRep, activeRep.pathFromRoot, activeRep.folderIdLocal);
    }

    private String Getpath() {
        return normalizePath(this.absolutePath + pathFromRoot);
    }

    public void AglibraySubFolderConstructor(AgLibraryRootFolder agLibraryRootFolder, String pathFromRoot, String folderIdLocal) throws SQLException {
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        this.pathFromRoot = pathFromRoot;
        this.folderIdLocal = folderIdLocal;

        if (agLibraryRootFolder.typeRoot == AgLibraryRootFolder.TYPE_CAT) {
            final Pattern pattern = Pattern.compile("##[A-Za-z0-9 -]*", Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(this.absolutePath);
            if (matcher.find()) {
                setCatFolder(matcher.group(0));
            }
        }
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

        for (Integer key : Context.formatZ.keySet()) {
            if (!repformatZ.containsKey(key)) {
                repformatZ.put(key, "");
            }
        }
        refreshCompteur();

        String[] part = pathFromRoot.replace("/", "").split(Context.appParam.getString("ssrepformatSep"));
        int i;
        for (i = 0; i < part.length && i < parentLrcat.listeZ.size(); i++) {
            if (personalizelist(parentLrcat.listeZ.get(i + 1)).contains(part[i])) {
                setrepformatZ(i + 1, part[i]);
            }
        }
    }

    public String getRepformatZ(int i) {
        return repformatZ.get(i);
    }

    public int getNbphotoRep() {
        return nbphotoRep;
    }

    public String getDtdebHumain() {
        SimpleDateFormat repDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dtdebH = new Date(dtdeb * 1000);
        return repDateFormat.format(dtdebH);
    }

    public int getActiveNum() {
        return activeNum;
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
    public String getCatFolder() {
        if (categorie != null) {
            return categorie.getRepertoire();
        } else {
            return "";
        }
    }

    /**
     * Sets cat folder.
     *
     * @param catFoldertxt the cat foldertxt
     */
    public void setCatFolder(String catFoldertxt) throws SQLException {
        categorie = null;
        for (int key : Context.categories.keySet()) {
            if (Context.categories.get(key).getRepertoire().compareTo(catFoldertxt) == 0) {
                categorie = Context.categories.get(key);
            }
        }
    }


    /**
     * Gets .
     *
     * @param num the num
     * @return the
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public Image getimagepreview(int num) throws IOException, SQLException {
        int interval = (nbphotoRep / 5);
        return getimagenumero(getnextphotonumfrom(interval * num));
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
            logger.info("" + phototoshow + " " + localUrl);
            image = new Image(new URL(Context.getLocalErrPhotoUrl()).openStream());
        }
        return image;
    }

    /**
     * Gets .
     *
     * @return the
     */
    public String getactivephotovaleurlibelle() {
        switch ((int) listFileSubFolder.get(activeNum).getStarValue()) {
            case -1:
                return Context.appParam.getString("valeurCorbeille");
            case 0:
                return Context.appParam.getString("valeurZero__");
            case 1:
                return Context.appParam.getString("valeur1star_");
            case 2:
                return Context.appParam.getString("valeur2stars");
            case 3:
                return Context.appParam.getString("valeur3stars");
            case 4:
                return Context.appParam.getString("valeur4stars");
            case 5:
                return Context.appParam.getString("valeur5stars");
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + (int) listFileSubFolder.get(activeNum).getStarValue());
        }
    }

    /**
     * Valeuractivephotoincrease.
     */
    public void valeuractivephotoincrease() {
        listFileSubFolder.get(activeNum).valeurIncrease();
    }

    /**
     * Valeuractivephotodecrease.
     */
    public void valeuractivephotodecrease() {
        listFileSubFolder.get(activeNum).valeurDecrease();
    }


    /**
     * Sqlget listelementrejetaranger result set.
     *
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet sqlgetListelementsubfolder() throws SQLException {
        return parentLrcat.select(
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
    public void refreshCompteur() throws SQLException {
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
        Double nbmin = 0d;
        Double nbmax = 999d;
        if (categorie != null) {
            nbmin = Double.valueOf(categorie.getNbminiphotobyday());
            nbmax = Double.valueOf(categorie.getNbmaxphotobyday());
        }

        int limiteminfolder = (int) (nbmin * nbjourfolder);
        int limitemaxfolder = (int) (nbmax * nbjourfolder);
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
        ratiophotoaconserver = "" + String.format("%03d", limiteminfolder) + " - " + String.format("%03d", limitemaxfolder) + " ( " + df.format(percent) + " )";
    }

    private void calculStatusRep() throws SQLException {
        //        statusRep
        statusRep = OK;
        String newPathFromRoot = "";
        for (Integer key : Context.formatZ.keySet()) {
            if (!repformatZ.containsKey(key)) {
                statusRep = KO;
            } else {
                if (repformatZ.get(key).compareTo("") == 0) {
                    statusRep = KO;
                } else {
                    newPathFromRoot += repformatZ.get(key).replace(Context.appParam.getString("caractsup"), "") + Context.appParam.getString("ssrepformatSep");
                }
            }
            if (statusRep.compareTo(KO) == 0) {
                break;
            }
        }
        if (newPathFromRoot.endsWith("_")) {
            newPathFromRoot = newPathFromRoot.substring(0, newPathFromRoot.length() - 1);
        }
        if (nbphotoapurger != 0) {
            statusRep = KO;
        }

    }

    /**
     * Nbetratiovaleur string.
     *
     * @param valeur the valeur
     * @return the string
     */
    public String nbetratiovaleur(int valeur) {
        int nb = 0;
        switch (valeur) {
            case 0:
                nb = nbetrationzeroetoile;
                break;
            case 1:
                nb = nbetrationuneetoile;
                break;
            case 2:
                nb = nbetrationdeuxetoile;
                break;
            case 3:
                nb = nbetrationtroisetoile;
                break;
            case 4:
                nb = nbetrationquatreetoile;
                break;
            case 5:
                nb = nbetrationcinqetoile;
                break;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + valeur);
        }
        DecimalFormat df = new DecimalFormat("##.##%");
        double percent;
        if (nbphotoRep == 0) {
            percent = 1;
        } else {
            percent = ((double) nb / nbphotoRep);
        }
        return " " + String.format("%02d", nb) + " = " + df.format(percent);
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
        return " " + String.format("%04d", nbphotoapurger);
    }


    /**
     * Gets activephoto valeur.
     *
     * @return the activephoto valeur
     */
    public String getActivephotoValeur() {
        switch ((int) listFileSubFolder.get(activeNum).getStarValue()) {
            case -1:
                return "     \uD83D\uDD71 \uD83D\uDD71 \uD83D\uDD71 ";
            case 0:
                return "           ";
            case 1:
                return " ★         ";
            case 2:
                return " ★ ★       ";
            case 3:
                return " ★ ★ ★     ";
            case 4:
                return " ★ ★ ★ ★   ";
            case 5:
                return " ★ ★ ★ ★ ★ ";
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
        return getNbelerep() + " " + getNbphotoRepHuman() + " #" + typeRoot + " " + pathFromRoot;
    }


    /**
     * Gets activephoto num.
     *
     * @param i the
     * @return the activephoto num
     */
    public int getActivephotoNum(int i) {
        return activephotoNum.get(zero + i);
    }

    /**
     * Move activephoto num to.
     *
     * @param delta the delta
     */
    public void moveActivephotoNumTo(int delta) {

        Integer activephotoNumsetZero = 0;
        if (delta != 0) {
            activephotoNumsetZero = activephotoNum.get(zero);
        }

        activephotoNum = new HashMap<>();
        switch (delta) {
            case 0:
                activephotoNumsetZero = getnextphotonumfrom(-1);
                break;
            case -1:
                activephotoNumsetZero = getprevphotonumfrom(activephotoNumsetZero);
                break;
            case -2:
                activephotoNumsetZero = getprevphotonumfrom(activephotoNumsetZero);
                activephotoNumsetZero = getprevphotonumfrom(activephotoNumsetZero);
                break;
            case +1:
                activephotoNumsetZero = getnextphotonumfrom(activephotoNumsetZero);
                break;
            case +2:
                activephotoNumsetZero = getnextphotonumfrom(activephotoNumsetZero);
                activephotoNumsetZero = getnextphotonumfrom(activephotoNumsetZero);
                break;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + delta);
        }

        //forcage activephotoNum zero sur une photo
        if (activephotoNumsetZero < 0) {
            activephotoNumsetZero = getnextphotonumfrom(activephotoNumsetZero);
        }
        if (activephotoNumsetZero > listFileSubFolder.size() - 1) {
            activephotoNumsetZero = getprevphotonumfrom(activephotoNumsetZero);
        }

        activephotoNum.put(zero, activephotoNumsetZero);
        activephotoNum.put(zero + 1, getnextphotonumfrom(activephotoNum.get(zero)));
        activephotoNum.put(zero + 2, getnextphotonumfrom(activephotoNum.get(zero + 1)));
        activephotoNum.put(zero - 1, getprevphotonumfrom(activephotoNum.get(zero)));
        activephotoNum.put(zero - 2, getprevphotonumfrom(activephotoNum.get(zero - 1)));

        activeNum = activephotoNum.get(zero);

    }

    private int getprevphotonumfrom(int phototoshow) {
        phototoshow -= 1;

        while (phototoshow >= 0) {
            AgLibraryFile fil = listFileSubFolder.get(phototoshow);
            if (fil.estPhoto()) {
                return phototoshow;
            }
            phototoshow -= 1;
        }
        return -1;

    }

    private int getnextphotonumfrom(int phototoshow) {
        phototoshow += 1;

        while (phototoshow <= listFileSubFolder.size() - 1) {
            AgLibraryFile fil = listFileSubFolder.get(phototoshow);
            if (fil.estPhoto()) {
                return phototoshow;
            }
            phototoshow += 1;
        }
        return listFileSubFolder.size();

    }


    public void setRotateActivephotoNumTo(int addRotate) {
        listFileSubFolder.get(activeNum).setAddRotate(addRotate);
    }

    public int getRotateFromActivephotonum() {
        return listFileSubFolder.get(activeNum).getAddRotate();
    }

    public int getRotateFromphotonum(int photonum) {
        if (photonum < 0 || photonum > listFileSubFolder.size() - 1) {
            return 0;
        }
        return listFileSubFolder.get(photonum).getAddRotate();
    }

    public void setrepformatZ(int i, String valeur) {
        repformatZ.put(i, valeur);
    }

    public ObservableList<String> personalizelist(ObservableList<String> listeZ) {

        ObservableList<String> pListeZ = FXCollections.observableArrayList(listeZ);

        pListeZ.forEach(tab -> {
            String[] part = tab.split("%");
            if (part.length > 1 && part[1].compareTo("DATE") == 0) {
                pListeZ.remove(tab);
                pListeZ.add(this.getDtdebHumain());
            }
        });

        return pListeZ;
    }

    public void execmodification(AgLibrarySubFolder activeRepDest) throws IOException, SQLException {

        List<AgLibraryFile> listFileSubFolderRejet = new ArrayList();

        listFileSubFolder.forEach((ele) -> {
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

        //move les elements dans le sous repertoire rejet
        activeRepDest.moveListEle(listFileSubFolderRejet, activeRepDest.getpathFromRootrejet(), false, activeRepDest.absolutePath);

        //rename du SubFolder ET
        //deplacement du subfolder et des sousrep dans le bon rootfolder
        AgLibrarySubFolder foldersrc = new AgLibrarySubFolder(this,
                getpathFromRootrejet());
        AgLibrarySubFolder folderdest = new AgLibrarySubFolder(activeRepDest,
                activeRepDest.getpathFromRootrejet());
        activeRepDest.sqlmoveRepertoryWithSubDirectory(foldersrc.Getpath(),
                folderdest.Getpath(),
                foldersrc.pathFromRoot,
                folderdest.pathFromRoot,
                foldersrc.rootfolderidlocal,
                folderdest.rootfolderidlocal);


    }

    private String getpathFromRootrejet() {
        return normalizePath(pathFromRoot + File.separator + Context.appParam.getString("ssrepRejet"));
    }

}