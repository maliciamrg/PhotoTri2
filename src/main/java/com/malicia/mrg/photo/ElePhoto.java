package com.malicia.mrg.photo;

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
    public ElePhoto(long captureTime, long mint, long maxt, String src, String absPath, String rep) {
        this.captureTime = captureTime;
        this.mint = mint;
        this.maxt = maxt;
        this.src = src;
        this.absPath = absPath;
        this.rep = rep;
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
     * Gets capture time.
     *
     * @return the capture time
     */
    public long getCaptureTime() {
        return captureTime;
    }

    /**
     * Sets capture time.
     *
     * @param captureTime the capture time
     */
    public void setCaptureTime(long captureTime) {
        this.captureTime = captureTime;
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
     * Sets mint.
     *
     * @param mint the mint
     */
    public void setMint(long mint) {
        this.mint = mint;
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
     * Sets maxt.
     *
     * @param maxt the maxt
     */
    public void setMaxt(long maxt) {
        this.maxt = maxt;
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
     * Sets src.
     *
     * @param src the src
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * Gets abs path.
     *
     * @return the abs path
     */
    public String getAbsPath() {
        return absPath;
    }

    /**
     * Sets abs path.
     *
     * @param absPath the abs path
     */
    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    /**
     * Gets rep.
     *
     * @return the rep
     */
    public String getRep() {
        return rep;
    }

    /**
     * Sets rep.
     *
     * @param rep the rep
     */
    public void setRep(String rep) {
        this.rep = rep;
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
}
