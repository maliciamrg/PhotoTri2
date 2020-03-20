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

public class AgLibrarySubFolder extends AgLibraryRootFolder {
    final int folderUncat = 0;
    final int folderEvents = 1;
    final int folderHolidays = 2;
    final int folderShooting = 3;
    private final Logger LOGGER;
    List<AgLibraryFile> listFileSubFolder;
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
    private String folder_id_local;
    private Map<Integer, Integer> activephotoNum;
    private int activeNum;
    private int activephotoValeur;
    private int zero = 2;
    private int catFolder;
    private long nbjourfolder;

    {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public AgLibrarySubFolder(CatalogLrcat parentLrcat, String name, String pathFromRoot, String folder_id_local, String rootfolderidlocal, String absolutePath) throws SQLException {
        super(parentLrcat, name, rootfolderidlocal, absolutePath);
        this.pathFromRoot = pathFromRoot;
        this.folder_id_local = folder_id_local;
        listFileSubFolder = new ArrayList();
        ResultSet rs = sqlgetListelementsubfolder();
        while (rs.next()) {
            String file_id_local = rs.getString("file_id_local");
            String file_id_global = rs.getString("id_global");
            String lcIdxFilename = rs.getString("lc_idx_filename");
            Double rating = rs.getDouble("rating");
            String fileformat = rs.getString("fileformat");
            long captureTime = rs.getLong(Context.CAPTURE_TIME);

            listFileSubFolder.add(new AgLibraryFile(absolutePath, this.pathFromRoot, lcIdxFilename, file_id_local, this, rating, fileformat, captureTime));
        }
        refreshCompteur();
    }

    public String getNbjourfolder() {
        return  " " + String.format("%03d", nbjourfolder) + " j ";
    }

    public String getCatFolder() {
        switch (catFolder) {
            case folderEvents:
                return Context.appParam.getString("repbookEvents");
            case folderHolidays:
                return Context.appParam.getString("repbookEvents");
            case folderShooting:
                return Context.appParam.getString("repbookEvents");
            case folderUncat:
                return "";
            default:
                throw new IllegalStateException("Unexpected value: " + catFolder);
        }
    }

    public void setCatFolder(String catFoldertxt) {
        catFolder = folderUncat;
        if (Context.appParam.getString("repbookEvents").compareTo(catFoldertxt) == 0) {
            catFolder = folderEvents;
        }
        if (Context.appParam.getString("repbookHolidays").compareTo(catFoldertxt) == 0) {
            catFolder = folderHolidays;
        }
        if (Context.appParam.getString("repbookShooting").compareTo(catFoldertxt) == 0) {
            catFolder = folderShooting;
        }
    }


    public Image getimagepreview(int num) throws IOException, InterruptedException {
        int interval = (nbphotoRep / 5);
        return getimagenumero(getnextphotonumfrom(interval * num));
    }

    public Image getimagenumero(int phototoshow) throws IOException, InterruptedException {
        String localUrl;
        if (phototoshow < 0 || phototoshow > listFileSubFolder.size() - 1) {
            localUrl = Context.getLocalVoidPhotoUrl();
        } else {
            File file = new File(listFileSubFolder.get(phototoshow).getPath());
            if (!file.exists()) {
                localUrl = Context.getLocalErr404PhotoUrl();
            } else {
                localUrl = file.toURI().toURL().toExternalForm();
            }
        }
        LOGGER.info(phototoshow + " " + localUrl);
        Image image = new Image(localUrl, false);
//        TimeUnit.SECONDS.sleep(1);
        if (image.isError()) {
            image = new Image(new URL(Context.getLocalErrPhotoUrl()).openStream());
        }
//        MainFrameController.popupalert(phototoshow + " " + localUrl, image);
        return image;
    }

    public String getactivephotovaleurlibelle() {
        switch (activephotoValeur) {
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

        }
        return "";
    }

    public void valeuractivephotoincrease() {
        if (listFileSubFolder.get(activeNum).starValue < 5) {
            listFileSubFolder.get(activeNum).starValue += 1;
            modifierfile();
        }
    }

    public void modifierfile() {
        activephotoValeur = (int) listFileSubFolder.get(activeNum).starValue;
        listFileSubFolder.get(activeNum).setedited = true;

    }

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
                        "Where a.folder =  \"" + folder_id_local + "\" " +
                        " ;");
    }

    public void refreshCompteur() {
        nbelerep = 0;
        nbphotoRep = 0;
        nbetrationzeroetoile = 0;
        nbetrationuneetoile = 0;
        nbetrationdeuxetoile = 0;
        nbetrationtroisetoile = 0;
        nbetrationquatreetoile = 0;
        nbetrationcinqetoile = 0;
//        nbphotoapurger = 0;
        statusRep = "---";
        long dtfin = 0;
        long dtdeb = 2147483647;
        for (int ifile = 0; ifile < listFileSubFolder.size(); ifile++) {
            AgLibraryFile fi = listFileSubFolder.get(ifile);
            if (!fi.estRejeter()) {
                nbelerep += 1;
                if (fi.estPhoto()) {
                    nbphotoRep += 1;
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

                    }
                    long dt = fi.getCaptureTime();
                    if (dt < dtdeb) {
                        dtdeb = dt;
                    }
                    if (dt > dtfin) {
                        dtfin = dt;
                    }
                }
            }
        }

//        nbphotoapurger;
        nbphotoapurger = 0;
        nbjourfolder = (dtfin - dtdeb) / (60 * 60 * 24) + 1;
        Double nbmin ;
        Double nbmax ;
        switch (catFolder) {
            case folderEvents:
                nbmin = Double.valueOf(Context.appParam.getString("nbminiEvents"));
                nbmax = Double.valueOf(Context.appParam.getString("nbmaxEvents"));
                break;
            case folderHolidays:
                nbmin = Double.valueOf(Context.appParam.getString("nbminiHolidays"));
                nbmax = Double.valueOf(Context.appParam.getString("nbmaxHolidays"));
                break;
            case folderShooting:
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
            nbphotoapurger = (int) (nbphotoRep- limitemaxfolder);
        }

//        ratiophotoaconserver;
        DecimalFormat df = new DecimalFormat("##.##%");
        double percent;
        if (nbphotoRep != 0) {
            percent = 1-(nbphotoapurger / nbphotoRep);
        } else {
            percent = 0;
        }
        ratiophotoaconserver = "" + String.format("%03d", limiteminfolder) + " - " + String.format("%03d", limitemaxfolder) + " ( " + df.format(percent) + " )";

//        statusRep;
        if (nbphotoapurger == 0) {
            statusRep = "--OK--";
        }
    }

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
        }
        DecimalFormat df = new DecimalFormat("##.##%");
        double percent = (nb / nbphotoRep);
        return " " + String.format("%02d", nb) + " = " + df.format(percent);
    }

    public String getNbelerep() {
        return " " + String.format("%04d", nbelerep);
    }


    public String getNbphotoRep() {
        return " " + String.format("%04d", nbphotoRep);
    }

    public String getRatiophotoaconserver() {
//        DecimalFormat df = new DecimalFormat("##.##%");
//        double percent = ((nbphotoRep - nbphotoapurger) / nbphotoRep);
//        return " " + String.format("%04d", (nbphotoRep - nbphotoapurger)) + " = " + df.format(percent);
        return ratiophotoaconserver;
    }

    public String getNbphotoapurger() {
        return " " + String.format("%04d", nbphotoapurger);
    }


    public String getActivephotoValeur() {
        switch (activephotoValeur) {
            case -1:
                return "     3 3 3 ";
            case 0:
                return "           ";
            case 1:
                return " é         ";
            case 2:
                return " é é       ";
            case 3:
                return " é é é     ";
            case 4:
                return " é é é é   ";
            case 5:
                return " é é é é é ";
        }
        return "";
    }

    public String getStatusRep() {
        return statusRep;
    }

    @Override
    public String toString() {
        return getNbelerep() + " " + getNbphotoRep() + " " + pathFromRoot;
    }


    public int getActivephotoNum(int i) {
        return activephotoNum.get(zero + i);
    }

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


}