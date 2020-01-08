package com.malicia.mrg;

import com.malicia.mrg.model.PropertiesParameters;
import com.malicia.mrg.model.RequeteSql;
import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Main {

    private static final  Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static final String BIGTITLE_JTABLE = "auto match (New 2 repertoire photo) from lrcat";

    public static void main(String[] args) {

        MyLogger.setup(Level.INFO);

        LOGGER.info("Start");

        PropertiesParameters.initPropertiesParameters();

        SQLiteJDBCDriverConnection.connect(PropertiesParameters.getCatalogLrcat());


        RequeteSql.sqlCombineAllGrouplessInGroupByPlageAdherance(PropertiesParameters.getPasRepertoirePhoto(), PropertiesParameters.getTempsAdherence(), PropertiesParameters.getRepertoireNew());

//        CreateJtable.createJTableSelectionRepertoire(BIGTITLE_JTABLE,RequeteSql.selectionRepertoire());

        List<GrpPhoto> groupDePhoto = regroupeByNewGroup(PropertiesParameters.getKidsModelList());
        List<GrpPhoto> groupDePhotoExecpt = exceptNewGroup(groupDePhoto, PropertiesParameters.getKidsModelList(), PropertiesParameters.getRepertoireNew());
        if (movetoNewGroup(true, groupDePhotoExecpt)) {
            movetoNewGroup(PropertiesParameters.getDryRun(), groupDePhotoExecpt);
//            movetoNewGroup(false,groupDePhoto);
        } else {
            LOGGER.info("movetoNewGroup KO, nothig nmove");
        }

        if (!PropertiesParameters.getDryRun()) {
            File directory = new File(groupDePhoto.get(1).getAbsolutePath() + PropertiesParameters.getRepertoireNew() + "/");
            deleteEmptyDir(directory);
            int nbdel = 0;
            do {
                nbdel = RequeteSql.sqlDeleteRepertory();
                LOGGER.info("logical delete:" + String.format("%04d", nbdel));
            }
            while (nbdel > 0);
        }
    }

    private static List<GrpPhoto> exceptNewGroup(List<GrpPhoto> groupDePhoto, List<String> KidsModelList, String repertoireNew) {
        List<GrpPhoto> excptgdp = new ArrayList();
        GrpPhoto Bazaz = new GrpPhoto( "@Bazar",groupDePhoto.get(1).getAbsolutePath(),repertoireNew+ "/");
        GrpPhoto NoDate = new GrpPhoto("@NoDate", groupDePhoto.get(1).getAbsolutePath(),repertoireNew+ "/");
        GrpPhoto Kidz = new GrpPhoto("@Kidz", groupDePhoto.get(1).getAbsolutePath(),repertoireNew+ "/");


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

    private static  List<GrpPhoto> regroupeByNewGroup(List<String> kidsModelList) {

//            constitution des groupe

        ResultSet rs = RequeteSql.sqlGroupGrouplessByPlageAdherance(PropertiesParameters.getTempsAdherence(), PropertiesParameters.getRepertoireNew());

        GrpPhoto gp = new GrpPhoto();

        List<GrpPhoto> ggp = new ArrayList();

        try {
            boolean first = true;

            while (rs.next()) {


                // Now we can fetch the data by column name, save and use them!
                String CameraModel = rs.getString("CameraModel");
                if (!kidsModelList.contains(CameraModel)){
                    CameraModel = "__";
                };
                long captureTime = rs.getLong("captureTime");
                long mint = rs.getLong("mint");
                long maxt = rs.getLong("maxt");
                String src = rs.getString("src");
                String absolutePath = rs.getString("absolutePath");

                if (first) {
                    gp.addfirst(CameraModel, captureTime, mint, maxt, src, absolutePath, PropertiesParameters.getRepertoireNew() + "/");
                } else {
                    if (!gp.add(CameraModel, captureTime, mint, maxt, src)) {
                        ggp.add(gp);
                        gp = new GrpPhoto();
                        gp.addfirst(CameraModel, captureTime, mint, maxt, src, absolutePath, PropertiesParameters.getRepertoireNew() + "/");
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

        LOGGER.info("Printing result...");
        int nbele = 0;

        Hashtable codeRetourAction = new Hashtable();

        int nbrow = 0;
        for (int i = 0; i < ggp.size(); i++) {
            GrpPhoto gptemp = ggp.get(i);
            nbrow += gptemp.getnbele();
            Hashtable hashRet = gptemp.groupAndMouveEle(dryRun);
            LOGGER.info("GrpPhoto:"+gptemp.toString());
            LOGGER.info(" hashRet:"+hashRet.toString());
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
                LOGGER.info("delete repertory:" + dir.toString());
                returnVal = dir.delete();

            }

        } else {
            returnVal = false;
        }
        return returnVal;
    }


}
