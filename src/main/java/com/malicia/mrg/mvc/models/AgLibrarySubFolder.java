package com.malicia.mrg.mvc.models;

import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgLibrarySubFolder extends AgLibraryRootFolder {
    public int nbelerep;
    public int nbphotoRep;
    public String statusRep;
    public String ratiophotoaconcerver;
    public String nbphotoapurger;
    public String nbetrationzeroetoile;
    public String nbetrationuneetoile;
    public String nbetrationdeuxetoile;
    public String nbetrationtroisetoile;
    public String nbetrationquatreetoile;
    public String nbetrationcinqetoile;

    private String pathFromRoot;
    private String folder_id_local;
    private int activephotoNum;
    public String activephotoValeur;
    List<AgLibraryFile> listFileSubFolder;

    public AgLibrarySubFolder(CatalogLrcat parentLrcat, String name, String pathFromRoot, String folder_id_local) throws SQLException {
        super(parentLrcat, name);
        this.pathFromRoot = pathFromRoot;
        this.folder_id_local = folder_id_local;
        listFileSubFolder = new ArrayList();
        ResultSet rs = sqlgetListelementsubfolder();
        while (rs.next()) {
            String file_id_local = rs.getString("file_id_local");
            String lcIdxFilename = rs.getString("lc_idx_filename");
            Double rating = rs.getDouble("rating");
            String fileformat = rs.getString("fileformat");
            listFileSubFolder.add(new AgLibraryFile(absolutePath, this.pathFromRoot, lcIdxFilename , file_id_local, this,rating , fileformat));
        }

        refreshCompteur();


    }

    public Image getimagepreview(int num){

    }

    public String getcurrentcat() {


    }

    public String getcurrentevents() {


    }

    public String getcurrentlieux() {


    }

    public String getcurrentperson() {


    }


    public Image getimagenumero(int getactivephoto) {


    }

    public String getactivephotovaleurlibelle(String getactivephotovaleur) {


    }

    public void valeuractivephotoincrease() {

    }

    public void valeuractivephotodecrease() {

    }

    public void setactivephoto(int i) {

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
        nbelerep=0;
        nbphotoRep=0;
        for (int ifile = 0; ifile < listFileSubFolder.size(); ifile++) {
            AgLibraryFile fi = listFileSubFolder.get(ifile);
            if (!fi.estRejeter()){
                nbelerep+=1;
                if (fi.estPhoto()){
                    nbphotoRep+=1;
                    switch(fi.starValue){
                        case
                    }
                }
            }
        }
    }
}
