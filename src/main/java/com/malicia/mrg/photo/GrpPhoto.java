package com.malicia.mrg.photo;

import com.malicia.mrg.mvc.controllers.ActionfichierRepertoire;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrpPhoto {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat repDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String cameraModelGrp = "";
    private long mintGrp;
    private long maxtGrp;
    private List<String> elesrc = new ArrayList<>();
    private List<String> eledest = new ArrayList<>();
    private List<String> eledt = new ArrayList<>();
    private String absolutePath;
    private String pathFromRootComumn;
    private String forceGroup = "";
    public GrpPhoto() {

    }
    public GrpPhoto(String forceGroup, String abspath, String pathfromrootcomumn) {
        this.forceGroup = forceGroup;
        absolutePath = abspath;
        pathFromRootComumn = pathfromrootcomumn;
    }

    public List<String> getEledest() {
        return eledest;
    }

    public void setEledest(List<String> eledest) {
        this.eledest = eledest;
    }

    public String getCameraModelGrp() {
        return cameraModelGrp;
    }

    public List<String> getEledt() {
        return eledt;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getPathFromRootComumn() {
        return pathFromRootComumn;
    }

    public List<String> getElesrc() {
        return elesrc;
    }


    @Override
    public String toString() {

        Date datemin = new Date(mintGrp * 1000);
        String datemintFormat = simpleDateFormat.format(datemin);

        Date datemaxt = new Date(maxtGrp * 1000);
        String datemaxtFormat = simpleDateFormat.format(datemaxt);


        return "GrpPhoto{" +
                "cameraModelGrp='" + cameraModelGrp + '\'' +
                ", mintGrp=" + datemintFormat +
                ", maxtGrp=" + datemaxtFormat +
                ", nbele=" + elesrc.size() +
                ", getNomRepetrtoire=" + getNvxNomRepertoire() +
                "}" +
                "\n" +
                listeeletostring();
    }

    private String listeeletostring() {
        if (elesrc.size() != eledt.size() || elesrc.size() != eledest.size()) {
            throw new IllegalStateException("ele.size=>" + elesrc.size() + " != " + " eledt.size=>" + eledt.size()+ " != " + " eledest.size=>" + eledest.size());
        }
        StringBuilder listeletostring = new StringBuilder();
        for (int i = 0; i < elesrc.size(); i++) {
            listeletostring.append("     " + eledt.get(i) + " -> " + elesrc.get(i)  + " -> " + eledest.get(i)  + "\n");
        }
        return listeletostring.toString();
    }

    public boolean add(String cameraModel, double captureTime, long mint, long maxt, String elesrc, String absolutepath, String pathfromrootcomumn) {

        boolean first;
        first = absolutePath == null;
        if (pathFromRootComumn == null) {
            first = true;
        }

        if (first) {
            absolutePath = absolutepath;
            pathFromRootComumn = pathfromrootcomumn;
        } else {
            if (!testInterval(mint, maxt)) return false;
        }

        forceadd(cameraModel, captureTime, mint, maxt, ActionfichierRepertoire.normalizePath(elesrc));

        return true;
    }

    public boolean testInterval(long mint, long maxt) {
        if (mint > 0 && mint > maxtGrp) {
            return false;
        }
        if (maxt > 0 && maxt < mintGrp) {
            return false;
        }
        return true;
    }

    public void forceadd(String cameraModel, double captime, long mint, long maxt, String elesrc) {
        //elesrc dans le groupe a conserver
        if (mint < mintGrp || mintGrp == 0) {
            mintGrp = mint;
        }
        if (maxt > maxtGrp) {
            maxtGrp = maxt;
        }
        if (cameraModel != null) {
            cameraModelGrp = cameraModel;
        }
        this.elesrc.add(elesrc);

        Date datecaptime = new Date((long) (captime * 1000));
        String datemaxtFormat = simpleDateFormat.format(datecaptime);

        eledt.add(datemaxtFormat);
    }

    public int getnbele() {
        return elesrc.size();
    }

    public String getNvxNomRepertoire() {
        if (forceGroup.compareTo("") != 0) {
            return (forceGroup);
        } else {
            Date datemin = new Date(mintGrp * 1000);
            String datemintFormat = repDateFormat.format(datemin);

            Date datemaxt = new Date(maxtGrp * 1000);
            String datemaxtFormat = repDateFormat.format(datemaxt);

            return (datemintFormat + "_" + datemaxtFormat).trim();
        }
    }

    public void add(List<String> eleIn, List<String> eledtIn, List<String> eledestIn) {
        elesrc.addAll(eleIn);
        eledt.addAll(eledtIn);
        eledest.addAll(eledestIn);
    }

    public boolean isdateNull() {
        return (mintGrp == -2082848400 || mintGrp == 0);
    }


    public void addEledest(int i, String dest) {
        if (eledest.size() < i) {
            throw new IllegalStateException("nb ele dest non conforme : " + eledest.size() + " < " + i);
        } else {
            eledest.add(dest);
        }
    }
}
