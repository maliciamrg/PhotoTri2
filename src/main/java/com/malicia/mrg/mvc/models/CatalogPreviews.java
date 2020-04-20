package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class CatalogPreviews extends SQLiteJDBCDriverConnection {

    public long dateFichier;
    public String dateFichierHR;
    public String nomFichier;
    public String cheminfichierPreviews = "";

    public CatalogPreviews(String CatalogPreviews) throws SQLException {
        super(CatalogPreviews);
        refreshdataLrcat(CatalogPreviews);

    }

    private void refreshdataLrcat(String CatalogPreviews) {
        cheminfichierPreviews = CatalogPreviews;
        File cltg = new File(cheminfichierPreviews);
        nomFichier = cltg.getName();
        dateFichier = cltg.lastModified();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        dateFichierHR = dateFormat.format(dateFichier);
    }

    public Blob getJpegFromUuidFile(String UuidFile) throws SQLException {
        ResultSet rs = this.select("" +
                "select jpegData " +
                "from RootPixels " +
                "where uuid = '" + UuidFile + "' " +
                ";");
        while (rs.next()) {
            return rs.getBlob("jpegData");
        }
        return null;
    }

    public String getname() {
        return nomFichier + " " + dateFichierHR;
    }


}
