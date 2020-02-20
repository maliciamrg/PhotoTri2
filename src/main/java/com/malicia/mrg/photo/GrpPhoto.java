package com.malicia.mrg.photo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class GrpPhoto {

    public static final String DEST_NULL = "destNull";
    public static final String DEST_NOT_EXIST = "destNotExist";
    public static final String SRC_NOT_EXIST = "srcNotExist";
    public static final String ERR_IN_MOVE = "errInMove";
    public static final String OK_MOVE_SAME = "OKMoveSame";
    public static final String OK_MOVE_DRY_RUN = "OKMoveDryRun";
    public static final String OK_MOVE_DO = "OKMoveDo";
    public static final String LISTE_ERREUR = "ListeErreur" ;


    private void setTypeEvenement() {
//        anniversaire mariage fête vacances ski etc ...
        this.typeEvenement = "typeEvenement";
    }

    private void setEmplacement() {
//        ville de prise de vue
        this.emplacement = "emplacement";
    }

    private void setPersonnes() {
//        liste de personnes separré par des _
        this.personnes = "personnes";
    }

    private void setTypeSceancesPhoto() {
//        Events 10-15 j
//        Holidays 20-30 sem
//        Shooting 03-05 j
        this.typeSceancesPhoto = "typeSéancesPhoto";
    }

    private String typeEvenement ="";
    private String emplacement="";
    private String personnes="";
    private String typeSceancesPhoto="";

    public String getCameraModelGrp() {
        return cameraModelGrp;
    }

    private String cameraModelGrp ="";
    private long mintGrp;
    private long maxtGrp;

    public List<String> getEle() {
        return ele;
    }

    private List<String> ele = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat repDateFormat = new SimpleDateFormat("YYYY-MM-dd");

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    private String absolutePath;
    private String pathFromRootComumn;
    private String pathFromRoot;
    private String ForceGroup ="";

    public void setForceGroup(String forceGroup) {
        ForceGroup = forceGroup;
    }


    public  GrpPhoto() {

    }

    public GrpPhoto(String forceGroup, String AbsolutePath,String pathfromrootcomumn) {
        ForceGroup=forceGroup;
        absolutePath=AbsolutePath;
        pathFromRootComumn=pathfromrootcomumn;
    }

    @Override
    public String toString() {

        Date datemin = new Date(mintGrp*1000);
        String datemintFormat = simpleDateFormat.format(datemin);

        Date datemaxt = new Date(maxtGrp*1000);
        String datemaxtFormat = simpleDateFormat.format(datemaxt);

        return "GrpPhoto{" +
                "cameraModelGrp='" + cameraModelGrp + '\'' +
                ", mintGrp=" + datemintFormat +
                ", maxtGrp=" + datemaxtFormat +
                ", nbele=" + ele.size() +
                ", getNomRepetrtoire=" + getNomRepetrtoire() +
                '}';
    }

    public boolean add(String cameraModel, double captureTime, long mint, long maxt, String elesrc) {
        if (cameraModel!= null && cameraModelGrp != null ) {
            if (cameraModel.compareTo(cameraModelGrp) != 0) {
                return false;
            }
        }
        if (mint>0) {
            if (mint > maxtGrp) {
                return false;
            }
        }
        if (maxt>0) {
            if (maxt < mintGrp) {
                return false;
            }
        }

        //elesrc dans le groupe a conserver
        if (mint < mintGrp ) {mintGrp=mint;}
        if (maxt > maxtGrp ) {maxtGrp=maxt;}
        if (cameraModel!= null) {cameraModelGrp = cameraModel;}
        ele.add(elesrc);

        return true;
    }

    public void addfirst(String cameraModel, double captureTime, long mint, long maxt, String elesrc, String absolutepath, String pathfromrootcomumn) {
        mintGrp=mint;
        maxtGrp=maxt;
        cameraModelGrp = cameraModel;
        absolutePath = absolutepath;
        pathFromRootComumn=pathfromrootcomumn;
        ele.add(elesrc);
    }

    public int getnbele() {
        return ele.size();
    }

    public Hashtable groupAndMouveEle(boolean dryRun) {

        Hashtable displayReturn = new Hashtable();
        displayReturn.put(DEST_NULL,0);
        displayReturn.put(DEST_NOT_EXIST,0);
        displayReturn.put(SRC_NOT_EXIST,0);
        displayReturn.put(ERR_IN_MOVE,0);
        displayReturn.put(OK_MOVE_SAME,0);
        displayReturn.put(OK_MOVE_DRY_RUN,0);
        displayReturn.put(OK_MOVE_DO,0);
        ArrayList<String> listeErreur = new ArrayList<String>();

        if (absolutePath == null){
            displayReturn.put(DEST_NULL,(Integer) displayReturn.get(DEST_NULL)+1);
            listeErreur.add("DEST_NULL:absolutePath is null");
            return displayReturn;
        }
        File directoryrepDest = new File(absolutePath+pathFromRootComumn);
        if (! directoryrepDest.exists()){
            displayReturn.put(DEST_NOT_EXIST,(Integer) displayReturn.get(DEST_NOT_EXIST)+1);
            listeErreur.add("DEST_NOT_EXIST:"+directoryrepDest.toString());
            return displayReturn;
        }

        pathFromRoot = pathFromRootComumn + getNomRepetrtoire();

        String directoryName = absolutePath +pathFromRoot;
        File directory = new File(directoryName);
        if (! directory.exists()){
            if (!dryRun) {
                directory.mkdir();
            }
        }

        for (int i = 0; i < ele.size(); i++) {
            File source = new File( ele.get(i));
            File destination = new File(directoryName + "/" + source.toPath().getFileName());
            if (source.toString().compareTo(destination.toString())==0) {
                displayReturn.put(OK_MOVE_SAME,(Integer) displayReturn.get(OK_MOVE_SAME)+1);
            }else{
                if (! source.exists()){
                    displayReturn.put(SRC_NOT_EXIST,(Integer) displayReturn.get(SRC_NOT_EXIST)+1);
                    listeErreur.add("SRC_NOT_EXIST:"+source.toString());
                }else{
                    if (dryRun) {
                        displayReturn.put(OK_MOVE_DRY_RUN,(Integer) displayReturn.get(OK_MOVE_DRY_RUN)+1);
                    } else {
                        try {
                            Files.move(source.toPath(), destination.toPath());
                            displayReturn.put(OK_MOVE_DO,(Integer) displayReturn.get(OK_MOVE_DO)+1);
                        } catch (IOException e) {
                            displayReturn.put(ERR_IN_MOVE,(Integer) displayReturn.get(ERR_IN_MOVE)+1);
                            listeErreur.add("ERR_IN_MOVE:"+source.toString()+"=>"+destination.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        displayReturn.put(LISTE_ERREUR,listeErreur);
        return displayReturn;
    }

    public String getNomRepetrtoire() {
        if (ForceGroup.compareTo("")!=0) {
            return (ForceGroup + "__" + cameraModelGrp );
        } else {
            Date datemin = new Date(mintGrp * 1000);
            String datemintFormat = repDateFormat.format(datemin);

            Date datemaxt = new Date(maxtGrp * 1000);
            String datemaxtFormat = simpleDateFormat.format(datemaxt);

//       YYYY-MM-DD_EVENTS_LIEUX_PERSONNES
            if (typeEvenement.compareTo("")==0) {setTypeEvenement();}
            if (emplacement.compareTo("")==0) {setEmplacement();}
            if (personnes.compareTo("")==0) {setPersonnes();}
            if (typeSceancesPhoto.compareTo("")==0) {setTypeSceancesPhoto();}
            return (datemintFormat + "_" + typeEvenement + "_" + emplacement + "_" + personnes + "_" +  typeSceancesPhoto + "   " +  cameraModelGrp).trim();
//            return (datemintFormat + "__" + String.format("%04d", getnbele()) + "__" + cameraModelGrp + "__" + datemaxtFormat);
        }
    }

    public void add(List<String> eleIn) {
        ele.addAll(eleIn);
    }

    public boolean dateNull() {
        return (mintGrp==-2082848400 || mintGrp==0);
    }


}
