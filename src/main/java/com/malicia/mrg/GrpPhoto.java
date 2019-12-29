package com.malicia.mrg;

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
    private final Hashtable displayReturn;
    private String cameraModelGrp ="";
    private long mintGrp;
    private long maxtGrp;
    private List<String> ele = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
    private String repDest;

    public GrpPhoto() {
        displayReturn = new Hashtable();
        displayReturn.put(DEST_NULL,0);
        displayReturn.put(DEST_NOT_EXIST,0);
        displayReturn.put(SRC_NOT_EXIST,0);
        displayReturn.put(ERR_IN_MOVE,0);
        displayReturn.put(OK_MOVE_SAME,0);
        displayReturn.put(OK_MOVE_DRY_RUN,0);
        displayReturn.put(OK_MOVE_DO,0);
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

    public void addfirst(String cameraModel, double captureTime, long mint, long maxt, String elesrc, String destination) {
        mintGrp=mint;
        maxtGrp=maxt;
        cameraModelGrp = cameraModel;
        repDest = destination;
        ele.add(elesrc);
    }

    public int getnbele() {
        return ele.size();
    }

    public Hashtable groupAndMouveEle(boolean dryRun) {


        if (repDest == null){
            displayReturn.put(DEST_NULL,(Integer) displayReturn.get(DEST_NULL)+1);
            return displayReturn;
        }
        File directoryrepDest = new File(repDest);
        if (! directoryrepDest.exists()){
            displayReturn.put(DEST_NOT_EXIST,(Integer) displayReturn.get(DEST_NOT_EXIST)+1);
            return displayReturn;
        }

        String directoryName = repDest + getNomRepetrtoire();
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
                }else{
                    if (dryRun) {
                        displayReturn.put(OK_MOVE_DRY_RUN,(Integer) displayReturn.get(OK_MOVE_DRY_RUN)+1);
                    } else {
                        try {
                            Files.move(source.toPath(), destination.toPath());
                            displayReturn.put(OK_MOVE_DO,(Integer) displayReturn.get(OK_MOVE_DO)+1);
                        } catch (IOException e) {
                            displayReturn.put(ERR_IN_MOVE,(Integer) displayReturn.get(ERR_IN_MOVE)+1);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return displayReturn;
    }

    private String getNomRepetrtoire() {

        Date datemin = new Date(mintGrp*1000);
        String datemintFormat = simpleDateFormat.format(datemin);

        Date datemaxt = new Date(maxtGrp*1000);
        String datemaxtFormat = simpleDateFormat.format(datemaxt);

        return (datemintFormat+"__"+datemaxtFormat+"__"+String.format("%04d", getnbele())+"__"+cameraModelGrp);

    }
}
