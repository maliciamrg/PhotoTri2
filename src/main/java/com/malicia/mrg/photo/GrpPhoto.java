package com.malicia.mrg.photo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrpPhoto {

    public static final String DEST_NULL = "destNull";
    public static final String DEST_NOT_EXIST = "destNotExist";
    public static final String SRC_NOT_EXIST = "srcNotExist";
    public static final String ERR_IN_MOVE = "errInMove";
    public static final String OK_MOVE_SAME = "OKMoveSame";
    public static final String OK_MOVE_DRY_RUN = "OKMoveDryRun";
    public static final String OK_MOVE_DO = "OKMoveDo";
    public static final String LISTE_ERREUR = "ListeErreur";
    //private String typeEvenement = "";
    //private String emplacement = "";
    //private String personnes = "";
    //private String typeSceancesPhoto = "";
    private String cameraModelGrp = "";
    private long mintGrp;
    private long maxtGrp;
    private List<String> ele = new ArrayList<>();
    private List<Double> eledt = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat repDateFormat = new SimpleDateFormat("YYYY-MM-dd");
    private String absolutePath;
    private String pathFromRootComumn;
    private String ForceGroup = "";

    public GrpPhoto() {

    }

    public GrpPhoto(String forceGroup, String AbsolutePath, String pathfromrootcomumn) {
        ForceGroup = forceGroup;
        absolutePath = AbsolutePath;
        pathFromRootComumn = pathfromrootcomumn;
    }

    public String getPathFromRootComumn() {
        return pathFromRootComumn;
    }

    public void setPathFromRootComumn(String pathFromRootComumn) {
        this.pathFromRootComumn = pathFromRootComumn;
    }


    public String getCameraModelGrp() {
        return cameraModelGrp;
    }

    public List<String> getEle() {
        return ele;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public void setForceGroup(String forceGroup) {
        ForceGroup = forceGroup;
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
                '}';
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
            //if (cameraModel != null && cameraModelGrp != null) {
            //  if (cameraModel.compareTo(cameraModelGrp) != 0) {
            //     return false;
            // }
            //}
            if (mint > 0) {
                if (mint > maxtGrp) {
                    return false;
                }
            }
            if (maxt > 0) {
                if (maxt < mintGrp) {
                    return false;
                }
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
        if (ForceGroup.compareTo("") != 0) {
            return (ForceGroup);
        } else {
            Date datemin = new Date(mintGrp * 1000);
            String datemintFormat = repDateFormat.format(datemin);

            Date datemaxt = new Date(maxtGrp * 1000);
            String datemaxtFormat = simpleDateFormat.format(datemaxt);

//       YYYY-MM-DD_EVENTS_LIEUX_PERSONNES
            //    if (typeEvenement.compareTo("") == 0) {
            //     setTypeEvenement();
            // }
            // if (emplacement.compareTo("") == 0) {
            //  setEmplacement();
            //}
            //if (personnes.compareTo("") == 0) {
            //   setPersonnes();
            //}
            //if (typeSceancesPhoto.compareTo("") == 0) {
            //   setTypeSceancesPhoto();
            //}
            return (datemintFormat + "_" + datemaxtFormat).trim();//typeEvenement + "_" + emplacement + "_" + personnes + "_" + typeSceancesPhoto ).trim();
//            return (datemintFormat + "__" + String.format("%04d", getnbele()) + "__" + cameraModelGrp + "__" + datemaxtFormat);
        }
    }

    public void add(List<String> eleIn) {
        ele.addAll(eleIn);
    }

    public boolean dateNull() {
        return (mintGrp == -2082848400 || mintGrp == 0);
    }


}
