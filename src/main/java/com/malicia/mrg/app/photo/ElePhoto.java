package com.malicia.mrg.app.photo;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Ele photo.
 */
public class ElePhoto {

    private long captureTime;
    private long mint;
    private long maxt;
    private String src;
    private String absPath;
    private String rep;
    private String fileidlocal;
    private List<GrpPhoto> grpPhotoCandidat = new ArrayList<>();

    /**
     * Instantiates a new Ele photo.
     *
     * @param captureTime the capture time
     * @param mint        the mint
     * @param maxt        the maxt
     * @param src         the src
     * @param absPath     the abs path
     * @param rep         the rep
     */
    public ElePhoto(long captureTime, long mint, long maxt, String src, String absPath, String rep,String fileidlocal) {
        this.captureTime = captureTime;
        this.mint = mint;
        this.maxt = maxt;
        this.src = src;
        this.absPath = absPath;
        this.rep = rep;
        this.fileidlocal = fileidlocal;
    }

    @Override
    public String toString() {
        return "ElePhoto{" +
                "captureTime=" + captureTime +
                ", mint=" + mint +
                ", maxt=" + maxt +
                ", src='" + src + '\'' +
                ", absPath='" + absPath + '\'' +
                ", rep='" + rep + '\'' +
                "}" +
                "\n" +
                listegrpPhotoCandidattostring();
    }

    /**
     * Gets grp photo candidat.
     *
     * @return the grp photo candidat
     */
    public List<GrpPhoto> getGrpPhotoCandidat() {
        return grpPhotoCandidat;
    }

    /**
     * Gets mint.
     *
     * @return the mint
     */
    public long getMint() {
        return mint;
    }

    /**
     * Gets maxt.
     *
     * @return the maxt
     */
    public long getMaxt() {
        return maxt;
    }

    /**
     * Gets src.
     *
     * @return the src
     */
    public String getSrc() {
        return src;
    }

    /**
     * Addgroup de photo candidat.
     *
     * @param groupDePhoto the group de photo
     */
    public void addgroupDePhotoCandidat(GrpPhoto groupDePhoto) {
        grpPhotoCandidat.add(groupDePhoto);
    }

    private String listegrpPhotoCandidattostring() {
        StringBuilder listeletostring = new StringBuilder();
        for (int i = 0; i < grpPhotoCandidat.size(); i++) {
            listeletostring.append("     " + grpPhotoCandidat.get(i).toString() + "\n");
        }
        return listeletostring.toString();
    }

    public String getFileidlocal() {
        return fileidlocal;
    }

    public void setFileidlocal(String fileidlocal) {
        this.fileidlocal = fileidlocal;
    }
}
