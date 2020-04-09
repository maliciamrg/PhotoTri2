package com.malicia.mrg.app.photo;

public class RepCat {
    private String repertoire;
    private String nbminiphotobyday;
    private String nbmaxphotobyday;

    public RepCat(String repertoire, String nbminiphotobyday, String nbmaxphotobyday) {
        this.repertoire = repertoire;
        this.nbminiphotobyday = nbminiphotobyday;
        this.nbmaxphotobyday = nbmaxphotobyday;
    }

    public String getRepertoire() {
        return repertoire;
    }

    public String getNbminiphotobyday() {
        return nbminiphotobyday;
    }

    public String getNbmaxphotobyday() {
        return nbmaxphotobyday;
    }
}
