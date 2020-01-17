package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.Context;
import com.malicia.mrg.GrpPhoto;
import com.malicia.mrg.mvc.models.RequeteSql;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;

public class mainFrameController {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }
    private static void delEmptyDirectory() {
        if (!Context.getDryRun()) {
            File directory = new File(Context.getAbsolutePathFirst() + Context.getRepertoireNew() + "/");
            deleteEmptyDir(directory);
            int nbdel = 0;
            do {
                nbdel = RequeteSql.sqlDeleteRepertory();
                LOGGER.fine("logical delete:" + String.format("%04d", nbdel));
            }
            while (nbdel > 0);
        }
    }

    public static boolean deleteEmptyDir(File dir) {
        boolean returnVal = false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            boolean success = true;
            for (int i = 0; i < children.length; i++){
                success &= deleteEmptyDir(new File(dir, children[i]));
            }

            if (success) {
                // The directory is now empty directory free so delete it
                LOGGER.fine("delete repertory:" + dir.toString());
                returnVal = dir.delete();

            }

        } else {
            returnVal = false;
        }
        return returnVal;
    }

    private static ImageIcon getImageiconResized(URL imagesJpg) {
        LOGGER.finest(imagesJpg.toString());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //this is your screen size
        ImageIcon imageIcon = new ImageIcon(imagesJpg); //imports the image
        imageIcon.getImage().flush();
        int fact = imageIcon.getIconHeight() / (screenSize.height / 10);
        ImageIcon imageIcon2 = null;
        if (fact > 0) {
            Image image = imageIcon.getImage(); // transform it
            Image newimg = image.getScaledInstance(imageIcon.getIconWidth() / fact, imageIcon.getIconHeight() / fact, Image.SCALE_SMOOTH); // scale it the smooth way
            imageIcon2 = new ImageIcon(newimg);
            imageIcon2.getImage().flush();
        }
        return imageIcon2;
    }

    private static void moveNewToGrpPhotos() {
        RequeteSql.sqlCombineAllGrouplessInGroupByPlageAdherance(Context.getPasRepertoirePhoto(), Context.getTempsAdherence(), Context.getRepertoireNew());

        java.util.List<GrpPhoto> groupDePhoto = regroupeByNewGroup(Context.getKidsModelList());
        java.util.List<GrpPhoto> groupDePhotoExecpt = exceptNewGroup(groupDePhoto, Context.getKidsModelList());
        if (movetoNewGroup(true, groupDePhotoExecpt)) {
            movetoNewGroup(Context.getDryRun(), groupDePhotoExecpt);
//            movetoNewGroup(false,groupDePhoto);
        } else {
            LOGGER.info("movetoNewGroup KO, nothig nmove");
        }
    }

    private static java.util.List<GrpPhoto> exceptNewGroup(java.util.List<GrpPhoto> groupDePhoto, java.util.List<String> KidsModelList) {
        java.util.List<GrpPhoto> excptgdp = new ArrayList();
        GrpPhoto Bazaz = new GrpPhoto( Context.getBazar(), Context.getAbsolutePathFirst(), Context.getRepertoireNew()+ "/");
        GrpPhoto NoDate = new GrpPhoto("@NoDate", Context.getAbsolutePathFirst(), Context.getRepertoireNew()+ "/");
        GrpPhoto Kidz = new GrpPhoto("@Kidz", Context.getAbsolutePathFirst(), Context.getRepertoireNew()+ "/");


        Iterator<GrpPhoto> groupDePhotoIterator = groupDePhoto.iterator();

        while (groupDePhotoIterator.hasNext()) {
            GrpPhoto gdp = groupDePhotoIterator.next();
            if (KidsModelList.contains(gdp.getCameraModelGrp())) {
                Kidz.add(gdp.getEle());
            } else {
                if (gdp.getnbele() <= 5) {
                    Bazaz.add(gdp.getEle());
                } else {
                    if (gdp.dateNull()){
                        NoDate.add(gdp.getEle());
                    } else {
                        excptgdp.add(gdp) ;
                    }
                }
            }
        }
        excptgdp.add(Bazaz);
        excptgdp.add(NoDate);
        excptgdp.add(Kidz);
        return excptgdp;
    }

    private static java.util.List<GrpPhoto> regroupeByNewGroup(java.util.List<String> kidsModelList) {

//            constitution des groupe

        ResultSet rs = RequeteSql.sqlGroupGrouplessByPlageAdherance(Context.getTempsAdherence());

        GrpPhoto gp = new GrpPhoto();

        java.util.List<GrpPhoto> ggp = new ArrayList();

        try {
            boolean first = true;

            while (rs.next()) {


                // Now we can fetch the data by column name, save and use them!
                String CameraModel = rs.getString("CameraModel");
                if (!kidsModelList.contains(CameraModel)){
                    CameraModel = " ";
                };
                long captureTime = rs.getLong("captureTime");
                long mint = rs.getLong("mint");
                long maxt = rs.getLong("maxt");
                String src = rs.getString("src");
                String absolutePath = rs.getString("absolutePath");

                if (first) {
                    gp.addfirst(CameraModel, captureTime, mint, maxt, src, absolutePath, Context.getRepertoireNew() + "/");
                } else {
                    if (!gp.add(CameraModel, captureTime, mint, maxt, src)) {
                        ggp.add(gp);
                        gp = new GrpPhoto();
                        gp.addfirst(CameraModel, captureTime, mint, maxt, src, absolutePath, Context.getRepertoireNew() + "/");
                    }
                }
                //}
//                LOGGER.info("\tCameraModel: " + CameraModel +
//                        ", captureTime: " + captureTime +
//                        ", src : " + src);


                first = false;
            }
            ggp.add(gp);


//            LOGGER.info("Nb row lues=> " + nbrow);
//            LOGGER.info("Nb row grp => " + nbele);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ggp;
    }

    private static boolean movetoNewGroup(boolean dryRun, List<GrpPhoto> ggp) {
//       Execution du deplacement

        LOGGER.fine("Printing result...");
        int nbele = 0;

        Hashtable codeRetourAction = new Hashtable();

        int nbrow = 0;
        for (int i = 0; i < ggp.size(); i++) {
            GrpPhoto gptemp = ggp.get(i);
            nbrow += gptemp.getnbele();

            Hashtable hashRet = gptemp.groupAndMouveEle(dryRun);
            LOGGER.finer("GrpPhoto:"+gptemp.toString());
            LOGGER.finer(" hashRet:"+hashRet.toString());
            mergeHashtable (codeRetourAction ,hashRet);
        }



        LOGGER.info((dryRun?"dryRun =>":"") +  codeRetourAction.toString());
        nbele = (int) codeRetourAction.get(GrpPhoto.OK_MOVE_DO) + (int) codeRetourAction.get(GrpPhoto.OK_MOVE_SAME) + (int) codeRetourAction.get(GrpPhoto.OK_MOVE_DRY_RUN);
        return (nbrow == nbele);
    }

    private static void mergeHashtable(Hashtable dReturnEle, Hashtable groupAndMouveEle) {
        Set<String> keys = groupAndMouveEle.keySet();
        for(String key: keys){
            if (key.compareTo(GrpPhoto.LISTE_ERREUR )!=0) {
                if (dReturnEle.containsKey(key)) {
                    int val = (int) dReturnEle.get(key) + (int) groupAndMouveEle.get(key);
                    dReturnEle.put(key, val);
                } else {
                    dReturnEle.put(key, groupAndMouveEle.get(key));
                }
//            System.out.println("Value of "+key+" is: "+groupAndMouveEle.get(key));
            }
        }

    }

    public void testAction(ActionEvent event){
        System.out.println("coucou");
    }
}
