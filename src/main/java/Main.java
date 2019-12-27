package com.malicia.mrg;

import java.io.IOException;

import com.malicia.mrg.model.PropertiesParameters;
import com.malicia.mrg.model.RequeteSql;
import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;
import com.malicia.mrg.view.CreateJtable;

public class Main {

    public static final String BIGTITLE_JTABLE = "auto match (New 2 repertoire photo) from lrcat";
    public static PropertiesParameters p ;
    private static SQLiteJDBCDriverConnection sql;

    public static void main(String[] args) {

        try {
            p = new PropertiesParameters();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sql = new SQLiteJDBCDriverConnection();
        sql.connect(p.CatalogLrcat);


        RequeteSql.SqlCreateAndAlimentionTable(p.PasRepertoirePhoto, p.TempsAdherence,p.RepertoireNew, sql);
        RequeteSql.SelectionRepertoire(sql);

        CreateJtable.CreateJTableSelectionRepertoire(sql, BIGTITLE_JTABLE);

    }


}
