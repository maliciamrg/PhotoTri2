package com.malicia.mrg.mvc.models;

import javafx.collections.ObservableList;

import java.util.List;

public class ZoneZ {
    public String typeDeListeDeZone;
    public String titreZone;
    public ObservableList<String> listeEleZone;
    public List<String> keywordMaitrePossible;

    public ZoneZ(ZoneZ zoneZ) {
        ZoneZConstructor(zoneZ.typeDeListeDeZone, zoneZ.titreZone, zoneZ.listeEleZone, zoneZ.keywordMaitrePossible);
    }

    public String getLocalValue() {
        return localValue;
    }

    public void setLocalValue(String localValue) {
        this.localValue = localValue;
    }

    private String localValue;

    public ZoneZ(String typ, String ssrepformatZ, ObservableList<String> listetmp, List<String> keyMaitre) {
        ZoneZConstructor(typ, ssrepformatZ, listetmp, keyMaitre);
    }

    public void ZoneZConstructor(String typ, String ssrepformatZ, ObservableList<String> listetmp, List<String> keyMaitre) {
        this.typeDeListeDeZone = typ;
        this.titreZone = ssrepformatZ;
        this.listeEleZone = listetmp;
        this.keywordMaitrePossible = keyMaitre;
        this.localValue="";
    }

}
