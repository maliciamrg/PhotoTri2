package com.malicia.mrg.app.util;

public class NbRatioOfValeurStar {
    public String getColor() {
        return color;
    }

    public String getLibelle() {
        return libelle;
    }

    private String color;
    private String libelle;

    public NbRatioOfValeurStar(String color, String libelle) {

        this.color = color;
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "@" + color + "@" + libelle;
    }

}
