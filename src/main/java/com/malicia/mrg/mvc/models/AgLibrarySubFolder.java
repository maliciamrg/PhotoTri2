package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The type Ag library sub folder.
 */
public class AgLibrarySubFolder extends AgLibraryRootFolder {
    /**
     * The constant UNEXPECTED_VALUE.
     */
    public static final String UNEXPECTED_VALUE = "Unexpected value: ";
    /**
     * The constant FOLDER_UNCAT.
     */
    public static final int FOLDER_UNCAT = 0;
    /**
     * The constant FOLDER_EVENTS.
     */
    public static final int FOLDER_EVENTS = 1;
    /**
     * The constant FOLDER_HOLIDAYS.
     */
    public static final int FOLDER_HOLIDAYS = 2;
    /**
     * The constant FOLDER_SHOOTING.
     */
    public static final int FOLDER_SHOOTING = 3;
    public static final String OK = "--OK--";
    private final Logger LOGGER;
    /**
     * The List file sub folder.
     */
    public List<AgLibraryFile> listFileSubFolder;
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
    private String pathFromRoot;
    private String folderIdLocal;
    private Map<Integer, Integer> activephotoNum;
    private int activeNum;
    private int zero = 2;
    private int catFolder;
    private long nbjourfolder;
    private long dtdeb;
    private long dtfin;
    /**
     * Instantiates a new Ag library sub folder.
     *
     * @param parentLrcat       the parent lrcat
     * @param name              the name
     * @param pathFromRoot      the path from root
     * @param folderIdLocal     the folder id local
     * @param rootfolderidlocal the rootfolderidlocal
     * @param absolutePath      the absolute path
     * @throws SQLException the sql exception
     */
    public AgLibrarySubFolder(CatalogLrcat parentLrcat, String name, String pathFromRoot, String folderIdLocal, String rootfolderidlocal, String absolutePath) throws SQLException {
        super(parentLrcat, name, rootfolderidlocal, absolutePath);
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
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

            listFileSubFolder.add(new AgLibraryFile(absolutePath, this.pathFromRoot, lcIdxFilename, fileIdLocal, rating, fileformat, captureTime, fileIdGlobal));
        }
        refreshCompteur();
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
        switch (catFolder) {
            case FOLDER_EVENTS:
                return Context.appParam.getString("repbookEvents");
            case FOLDER_HOLIDAYS:
                return Context.appParam.getString("repbookHolidays");
            case FOLDER_SHOOTING:
                return Context.appParam.getString("repbookShooting");
            case FOLDER_UNCAT:
                return "";
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + catFolder);
        }
    }

    /**
     * Sets cat folder.
     *
     * @param catFoldertxt the cat foldertxt
     */
    public void setCatFolder(String catFoldertxt) {
        catFolder = FOLDER_UNCAT;
        if (Context.appParam.getString("repbookEvents").compareTo(catFoldertxt) == 0) {
            catFolder = FOLDER_EVENTS;
        }
        if (Context.appParam.getString("repbookHolidays").compareTo(catFoldertxt) == 0) {
            catFolder = FOLDER_HOLIDAYS;
        }
        if (Context.appParam.getString("repbookShooting").compareTo(catFoldertxt) == 0) {
            catFolder = FOLDER_SHOOTING;
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
    public Image getimagenumero(int phototoshow) throws IOException, SQLException {
        Image image = null;
        String localUrl;
        if (phototoshow < 0 || phototoshow > listFileSubFolder.size() - 1) {
            localUrl = Context.getLocalVoidPhotoUrl();
            image = new Image(localUrl, false);
        } else {
            File file = new File(listFileSubFolder.get(phototoshow).getPath());
//            InputStream in = Context.Previews.getJpegFromUuidFile(listFileSubFolder.get(phototoshow).getFileIdGlobal());
//            if (in == null) {
            if (file.exists()) {
                localUrl = file.toURI().toURL().toExternalForm();
                LOGGER.info(localUrl);
                image = new Image(localUrl, 400, 400, true, false, false);
            } else {
                localUrl = Context.getLocalErr404PhotoUrl();
                image = new Image(localUrl, false);
            }
//            } else {
//                localUrl = file.toURI().toURL().toExternalForm();
//                image = new Image(in);
//            }

        }
        if (image.isError()) {
            LOGGER.info(phototoshow + " " + localUrl);
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
        switch ((int) listFileSubFolder.get(activeNum).starValue) {
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
                throw new IllegalStateException(UNEXPECTED_VALUE + (int) listFileSubFolder.get(activeNum).starValue);
        }
    }

    /**
     * Valeuractivephotoincrease.
     */
    public void valeuractivephotoincrease() {
        if (listFileSubFolder.get(activeNum).starValue < 5) {
            listFileSubFolder.get(activeNum).starValue += 1;
            modifierfile();
        }
    }

    /**
     * Modifierfile.
     */
    public void modifierfile() {
        listFileSubFolder.get(activeNum).setedited = true;
    }

    /**
     * Valeuractivephotodecrease.
     */
    public void valeuractivephotodecrease() {
        if (listFileSubFolder.get(activeNum).starValue > -1) {
            listFileSubFolder.get(activeNum).starValue -= 1;
            modifierfile();
        }
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
    public void refreshCompteur() {
        nbelerep = 0;
        nbphotoRep = 0;
        nbetrationzeroetoile = 0;
        nbetrationuneetoile = 0;
        nbetrationdeuxetoile = 0;
        nbetrationtroisetoile = 0;
        nbetrationquatreetoile = 0;
        nbetrationcinqetoile = 0;
        statusRep = "---";
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
        switch ((int) fi.starValue) {
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
                throw new IllegalStateException(UNEXPECTED_VALUE + (int) fi.starValue);
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
        Double nbmin;
        Double nbmax;
        switch (catFolder) {
            case FOLDER_EVENTS:
                nbmin = Double.valueOf(Context.appParam.getString("nbminiEvents"));
                nbmax = Double.valueOf(Context.appParam.getString("nbmaxEvents"));
                break;
            case FOLDER_HOLIDAYS:
                nbmin = Double.valueOf(Context.appParam.getString("nbminiHolidays"));
                nbmax = Double.valueOf(Context.appParam.getString("nbmaxHolidays"));
                break;
            case FOLDER_SHOOTING:
                nbmin = Double.valueOf(Context.appParam.getString("nbminiShooting"));
                nbmax = Double.valueOf(Context.appParam.getString("nbmaxShooting"));
                break;
            default:
                nbmin = 0d;
                nbmax = 999d;
                break;
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

    private void calculStatusRep() {
        //        statusRep
        if (nbphotoapurger == 0) {
            statusRep = OK;
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
            percent = (nb / nbphotoRep);
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
    public String getNbphotoRep() {
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
        switch ((int) listFileSubFolder.get(activeNum).starValue) {
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
                throw new IllegalStateException(UNEXPECTED_VALUE + (int) listFileSubFolder.get(activeNum).starValue);
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
        return getNbelerep() + " " + getNbphotoRep() + " " + pathFromRoot;
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

}