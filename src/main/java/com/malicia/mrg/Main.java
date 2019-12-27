package com.malicia.mrg;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.malicia.mrg.model.PropertiesParameters;
import com.malicia.mrg.model.RequeteSql;
import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;
import com.malicia.mrg.view.CreateJtable;



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


        RequeteSql.sqlCreateAndAlimentionTable(PropertiesParameters.getPasRepertoirePhoto(), PropertiesParameters.getTempsAdherence(),PropertiesParameters.getRepertoireNew());

        CreateJtable.createJTableSelectionRepertoire(BIGTITLE_JTABLE,RequeteSql.selectionRepertoire());
        CreateJtable.createJTableSelectionRepertoire(BIGTITLE_JTABLE,RequeteSql.listeExifNew(""));

    }


}
