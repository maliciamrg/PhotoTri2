package com.malicia.mrg;

import com.malicia.mrg.model.PropertiesParameters;
import com.malicia.mrg.model.RequeteSql;
import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;
import com.malicia.mrg.view.CreateJtable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
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

        LOGGER.info("Start") ;

        PropertiesParameters.initPropertiesParameters();

        SQLiteJDBCDriverConnection.connect(PropertiesParameters.getCatalogLrcat());


        RequeteSql.sqlCombineAllGrouplessInGroupByPlageAdherance(PropertiesParameters.getPasRepertoirePhoto(), PropertiesParameters.getTempsAdherence(),PropertiesParameters.getRepertoireNew());

        CreateJtable.createJTableSelectionRepertoire(BIGTITLE_JTABLE,RequeteSql.selectionRepertoire());

        if (movetoNewGroup(true)){
            movetoNewGroup(false);
        }else {
            LOGGER.info("movetoNewGroup KO, nothig nmove" );
        }
    }

    private static boolean movetoNewGroup(boolean dryRun) {

        Hashtable dReturnEle = new Hashtable();

        ResultSet rs = RequeteSql.sqlGroupGrouplessByPlageAdherance(PropertiesParameters.getTempsAdherence(), PropertiesParameters.getRepertoireNew());

        GrpPhoto gp = new GrpPhoto();

        List<GrpPhoto> ggp = new ArrayList();

        int nbrow = 0;
        int nbele = 0;
        try {
            nbrow = 0;
            boolean first = true;;
            while (rs.next()) {


                // Now we can fetch the data by column name, save and use them!
                String CameraModel = rs.getString("CameraModel");
                long captureTime = rs.getLong("captureTime");
                long mint = rs.getLong("mint");
                long maxt = rs.getLong("maxt");
                String src = rs.getString("src");
                String dest = rs.getString("dest");

                if (first ) {
                    gp.addfirst(CameraModel, captureTime, mint, maxt, src, dest + PropertiesParameters.getRepertoireNew() + "/");
                }else {
                    if (!gp.add(CameraModel, captureTime, mint, maxt, src)) {
                        ggp.add(gp);
                        gp = new GrpPhoto();
                        gp.addfirst(CameraModel, captureTime, mint, maxt, src, dest + PropertiesParameters.getRepertoireNew() + "/");
                    }
                }
                //}
//                LOGGER.info("\tCameraModel: " + CameraModel +
//                        ", captureTime: " + captureTime +
//                        ", src : " + src);


                nbrow = rs.getRow();
                first =false;
            }
            ggp.add(gp);
            LOGGER.info("Nb row => " + nbrow);


            LOGGER.info("Printing result...");

            nbele = 0;

            for (int i = 0; i < ggp.size(); i++) {
                GrpPhoto gptemp = ggp.get(i);
//                LOGGER.info(gptemp.toString());
                mergeHashtable (dReturnEle , gptemp.groupAndMouveEle(dryRun));
            }


//            LOGGER.info("Nb row lues=> " + nbrow);
//            LOGGER.info("Nb row grp => " + nbele);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        LOGGER.info(dReturnEle.toString());
        nbele = (int) dReturnEle.get(GrpPhoto.OK_MOVE_DO) + (int) dReturnEle.get(GrpPhoto.OK_MOVE_SAME) + (int) dReturnEle.get(GrpPhoto.OK_MOVE_DRY_RUN);
        return (nbrow == nbele);
    }

    private static void mergeHashtable(Hashtable dReturnEle, Hashtable groupAndMouveEle) {
        Set<String> keys = groupAndMouveEle.keySet();
        for(String key: keys){
            if (dReturnEle.containsKey(key)) {
                int val = (int)dReturnEle.get(key) + (int)groupAndMouveEle.get(key);
                dReturnEle.put(key,val);
            } else {
                dReturnEle.put(key,groupAndMouveEle.get(key));
            }

//            System.out.println("Value of "+key+" is: "+groupAndMouveEle.get(key));
        }

    }


}
