package com.malicia.mrg.photo;

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
    private List<String> ele = new ArrayList<>();
    private List<Double> eledt = new ArrayList<>();
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


    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getPathFromRootComumn() {
        return pathFromRootComumn;
    }

    public List<String> getEle() {
        return ele;
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
                ", nbele=" + ele.size() +
                ", getNomRepetrtoire=" + getNomRepetrtoire() +
                "}" +
                "\n" +
                listeeletostring();
    }

    private String listeeletostring() {
        StringBuilder listeletostring = new StringBuilder();
        for (int i = 0; i <= ele.size(); i++) {
            listeletostring.append("     " + eledt.get(i) + " -> " + ele.get(i) + "\n");
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
            if (mint > 0 && mint > maxtGrp) {
                return false;
            }
            if (maxt > 0 && maxt < mintGrp) {
                return false;
            }
        }

        forceadd(cameraModel, captureTime, mint, maxt, elesrc);

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
        ele.add(elesrc);
        eledt.add(captime);
    }

    public int getnbele() {
        return ele.size();
    }

    public String getNomRepetrtoire() {
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

    public void add(List<String> eleIn) {
        ele.addAll(eleIn);
    }

    public boolean isdateNull() {
        return (mintGrp == -2082848400 || mintGrp == 0);
    }


}
