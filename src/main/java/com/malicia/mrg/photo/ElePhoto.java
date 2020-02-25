package com.malicia.mrg.photo;

import java.util.ArrayList;
import java.util.List;

public class ElePhoto {

    private long captureTime;

    @Override
    public String toString() {
        return "ElePhoto{" +
                "captureTime=" + captureTime +
                ", mint=" + mint +
                ", maxt=" + maxt +
                ", src='" + src + '\'' +
                ", absPath='" + absPath + '\'' +
                ", rep='" + rep + '\'' +
                "}"+
                "\n" +
                listegrpPhotoCandidattostring();
    }

    private long mint;
    private long maxt;
    private String src;
    private String absPath;
    private String rep;
    private List<GrpPhoto> grpPhotoCandidat = new ArrayList<>();

    public ElePhoto(long captureTime, long mint, long maxt, String src, String absPath, String rep) {
        this.captureTime = captureTime;
        this.mint = mint;
        this.maxt = maxt;
        this.src = src;
        this.absPath = absPath;
        this.rep = rep;
    }

    public long getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(long captureTime) {
        this.captureTime = captureTime;
    }

    public long getMint() {
        return mint;
    }

    public void setMint(long mint) {
        this.mint = mint;
    }

    public long getMaxt() {
        return maxt;
    }

    public void setMaxt(long maxt) {
        this.maxt = maxt;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAbsPath() {
        return absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

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
