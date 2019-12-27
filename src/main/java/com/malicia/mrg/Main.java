package com.malicia.mrg;

import com.malicia.mrg.model.PropertiesParameters;
import com.malicia.mrg.model.RequeteSql;
import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;
import com.malicia.mrg.view.CreateJtable;

public class Main {

    public static final String BIGTITLE_JTABLE = "auto match (New 2 repertoire photo) from lrcat";
    public static final PropertiesParameters p = new PropertiesParameters();

    public static void main(String[] args) {

        SQLiteJDBCDriverConnection.connect(p.CatalogLrcat);


        RequeteSql.SqlCreateAndAlimentionTable(p.PasRepertoirePhoto, p.TempsAdherence,p.RepertoireNew);

        CreateJtable.CreateJTableSelectionRepertoire(BIGTITLE_JTABLE,RequeteSql.SelectionRepertoire());

    }


}
