package com.malicia.mrg.app.photo;

public class repCat {
    private int numcat;
    private String repertoire;
    private String nbminiphotobyday;
    private String nbmaxphotobyday;

    public repCat(int numcat, String repertoire, String nbminiphotobyday, String nbmaxphotobyday) {

        this.numcat = numcat;
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
