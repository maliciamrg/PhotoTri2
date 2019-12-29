package com.malicia.mrg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrpPhoto {
    private String cameraModelGrp ="";
    private long mintGrp;
    private long maxtGrp;
    private List<String> ele = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
    private String repDest;

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

    public Double getnbele() {
        return Double.valueOf(ele.size());
    }

    public void groupAndMouveEle() {

        if (repDest == null){return;}
        File directoryrepDest = new File(repDest);
        if (! directoryrepDest.exists()){
            return;
        }

        String directoryName = repDest + getNomRepetrtoire();
        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }

        for (int i = 0; i < ele.size(); i++) {
            File source = new File( ele.get(i));
            File destination = new File(directoryName + "/" + source.toPath().getFileName());
            try {
                Files.move(source.toPath(), destination.toPath() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getNomRepetrtoire() {

        Date datemin = new Date(mintGrp*1000);
        String datemintFormat = simpleDateFormat.format(datemin);

        Date datemaxt = new Date(maxtGrp*1000);
        String datemaxtFormat = simpleDateFormat.format(datemaxt);

        return (datemintFormat+"__"+datemaxtFormat+"__"+String.format("%04d", getnbele().intValue())+"__"+cameraModelGrp);

    }
}
