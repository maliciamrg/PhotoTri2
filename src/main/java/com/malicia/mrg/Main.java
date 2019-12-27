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
    public static final PropertiesParameters p = new PropertiesParameters();

    public static void main(String[] args) {

        MyLogger.setup(Level.INFO);

        LOGGER.info("Start") ;

        SQLiteJDBCDriverConnection.connect(p.CatalogLrcat);


        RequeteSql.SqlCreateAndAlimentionTable(p.PasRepertoirePhoto, p.TempsAdherence,p.RepertoireNew);

        CreateJtable.CreateJTableSelectionRepertoire(BIGTITLE_JTABLE,RequeteSql.SelectionRepertoire());

    }


}
