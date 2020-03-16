package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import javafx.scene.image.Image;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AgLibrarySubFolder extends AgLibraryRootFolder {
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
    private int activephotoNum;
    private int activephotoValeur;

    public AgLibrarySubFolder(CatalogLrcat parentLrcat, String name, String pathFromRoot, String folder_id_local,String rootfolderidlocal, String absolutePath) throws SQLException {
        super(parentLrcat, name, rootfolderidlocal, absolutePath);
        this.pathFromRoot = pathFromRoot;
        this.folder_id_local = folder_id_local;
        listFileSubFolder = new ArrayList();
        ResultSet rs = sqlgetListelementsubfolder();
        while (rs.next()) {
            String file_id_local = rs.getString("file_id_local");
            String lcIdxFilename = rs.getString("lc_idx_filename");
            Double rating = rs.getDouble("rating");
            String fileformat = rs.getString("fileformat");
            listFileSubFolder.add(new AgLibraryFile(absolutePath, this.pathFromRoot, lcIdxFilename, file_id_local, this, rating, fileformat));
        }
        refreshCompteur();
    }

    public Image getimagepreview(int num) {
        int interval = (nbphotoRep / 5);
        return getimagenumero(interval/num);
    }

//    public String getcurrentcat() {
//        return "";
//    }
//
//    public String getcurrentevents() {
//        return "";
//    }
//
//    public String getcurrentlieux() {
//        return "";
//    }
//
//    public String getcurrentperson() {
//        return "";
//    }


    public Image getimagenumero(int getactivephoto) {
        if (getactivephoto <0 ){getactivephoto=0;}
        if (getactivephoto > listFileSubFolder.size()-1 ){getactivephoto=listFileSubFolder.size()-1 ;}
        File file = new File(listFileSubFolder.get(getactivephoto).getPath());
        String localUrl = file.toURI().toString();
        return new javafx.scene.image.Image(localUrl);
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
        if (listFileSubFolder.get(activephotoNum).starValue<5) {
            listFileSubFolder.get(activephotoNum).starValue += 1;
            modifierfile();
        }
    }

    public void modifierfile() {
        activephotoValeur = (int) listFileSubFolder.get(activephotoNum).starValue;
        listFileSubFolder.get(activephotoNum).setedited = true;

    }

    public void valeuractivephotodecrease() {
        if (listFileSubFolder.get(activephotoNum).starValue>-1) {
            listFileSubFolder.get(activephotoNum).starValue -= 1;
            modifierfile();
        }
    }

    public int getActivephotoNum() {
        return activephotoNum;
    }

    public void setActivephotoNum(int activephotoNum) {
        this.activephotoNum = activephotoNum;
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
                        "a.lc_idx_filename as lc_idx_filename , " +
                        "e.rating , " +
                        "e.fileformat " +
                        "from AgLibraryFile a  " +
                        "inner join Adobe_images e  " +
                        " on a.id_local = e.rootFile    " +
                        "Where a.folder =  \"" + folder_id_local + "\" " +
                        " ;");
    }

    public void refreshCompteur() {
        nbelerep = 0;
        nbphotoRep = 0;
        nbetrationzeroetoile=0;
        nbetrationuneetoile=0;
        nbetrationdeuxetoile=0;
        nbetrationtroisetoile=0;
        nbetrationquatreetoile=0;
        nbetrationcinqetoile=0;
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
                }
            }
        }
//
//        nbphotoapurger;
//        ratiophotoaconserver;
//        statusRep;
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
        DecimalFormat df = new DecimalFormat("##.##%");
        double percent = ((nbphotoRep - nbphotoapurger) / nbphotoRep);
        return " " + String.format("%04d", (nbphotoRep - nbphotoapurger)) + " = " + df.format(percent);
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
        return getNbphotoRep() + " " + pathFromRoot ;
    }
}
