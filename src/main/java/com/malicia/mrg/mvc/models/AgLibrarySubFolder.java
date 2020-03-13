package com.malicia.mrg.mvc.models;

import javafx.collections.ObservableList;

public class AgLibrarySubFolder {
    private String choix1;

    @Override
    public String toString() {
        return "AgLibrarySubFolder{" +
                "choix1='" + choix1 + '\'' +
                '}';
    }

    public AgLibrarySubFolder(String choix1) {

        this.choix1 = choix1;
    }
}
