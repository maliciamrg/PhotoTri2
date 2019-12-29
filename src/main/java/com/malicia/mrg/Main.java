package com.malicia.mrg;

import com.malicia.mrg.model.PropertiesParameters;
import com.malicia.mrg.model.RequeteSql;
import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;
import com.malicia.mrg.view.CreateJtable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

        movetoNewGroup();
    }

    private static void movetoNewGroup() {

        ResultSet rs = RequeteSql.sqlGroupGrouplessByPlageAdherance(PropertiesParameters.getTempsAdherence(), PropertiesParameters.getRepertoireNew());

        GrpPhoto gp  = new GrpPhoto();
        List<GrpPhoto> ggp  = new ArrayList();

        try {
            int nbrow = 0;
            while (rs.next()) {


                // Now we can fetch the data by column name, save and use them!
                String CameraModel = rs.getString("CameraModel");
                long captureTime = rs.getLong("captureTime");
                long mint = rs.getLong("mint");
                long maxt = rs.getLong("maxt");
                String src = rs.getString("src");
                String dest = rs.getString("dest");

                //if (captureTime != null ) {
                    if (!gp.add(CameraModel, captureTime, mint, maxt, src)) {
                        ggp.add(gp);
                        gp = new GrpPhoto();
                        gp.addfirst(CameraModel, captureTime, mint, maxt, src,dest +PropertiesParameters.getRepertoireNew()+"/" );
                    }
                //}
//                LOGGER.info("\tCameraModel: " + CameraModel +
//                        ", captureTime: " + captureTime +
//                        ", src : " + src);


                nbrow = rs.getRow();

            }
            ggp.add(gp);
            LOGGER.info("Nb row => " + nbrow);


            LOGGER.info("Printing result...");
            Double nbele = 0d;
            for (int i = 0; i < ggp.size(); i++) {
                GrpPhoto gptemp = ggp.get(i);
                LOGGER.info(gptemp.toString());
                gptemp.groupAndMouveEle();
                nbele = nbele + gptemp.getnbele();
            }


            LOGGER.info("Nb row lues=> " + nbrow);
            LOGGER.info("Nb row grp => " + nbele);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
