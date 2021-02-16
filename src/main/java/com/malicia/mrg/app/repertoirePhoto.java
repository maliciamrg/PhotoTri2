package com.malicia.mrg.app;


import javafx.collections.FXCollections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class repertoirePhoto {

    public String repertoire;

    public int uniteDeJour;
    public int nbMaxParUniteDeJour;
    public List<Integer> maxStar;

    public List<paramZone> pZone;


    public repertoirePhoto() {
        super();
    }

    public repertoirePhoto(String repertoireIn , int uniteDeJourIn , int nbMaxParUniteDeJourIn) {
        repertoire = repertoireIn;
        uniteDeJour=uniteDeJourIn;
        nbMaxParUniteDeJour=nbMaxParUniteDeJourIn;
        maxStar = FXCollections.observableArrayList();
        pZone = FXCollections.observableArrayList();
    }
    public void addMaxStar (int maxStarIn){
        maxStar.add(maxStarIn);
    }
    public void addMaxStar (String maxStarInVirgule){
        String[] ratioMaxStar = maxStarInVirgule.split(",");
        for (int i = 0; i < ratioMaxStar.length; i++) {
            maxStar.add(Integer.parseInt(ratioMaxStar[i]));
        }
    }
    public void addParamZone (Boolean isEditable, String valeurParDefaut, Boolean isValditationFacultative){
        pZone.add(new paramZone( isEditable,  valeurParDefaut,  isValditationFacultative));
    }
    public void addParamZone (String isEditableVirgule, String valeurParDefautVirgule, String isValditationFacultativeVirgule){
        String[] arrEditableVirgule = isEditableVirgule.split(",");
        String[] arrvaleurParDefautVirgule = valeurParDefautVirgule.split(",");
        String[] arrisValditationFacultativeVirgule = isValditationFacultativeVirgule.split(",");
        for (int i = 0; i < arrEditableVirgule.length; i++) {
            pZone.add(new paramZone(
                    arrEditableVirgule[i]=="Close"?false:true ,
                    arrvaleurParDefautVirgule[i],
                    arrisValditationFacultativeVirgule[i]=="Facul"?false:true));
        }

    }

    public static class paramZone {

        public String valeurDefaut;
        public Boolean validationFacultative;
        public Boolean editable;

        public paramZone() {
            super();
        }

        public paramZone(Boolean isEditable, String valeurParDefaut, Boolean isValidatationFacultative) {
            editable = isEditable;
            valeurDefaut = valeurParDefaut;
            validationFacultative = isValidatationFacultative;
        }
    }
}
