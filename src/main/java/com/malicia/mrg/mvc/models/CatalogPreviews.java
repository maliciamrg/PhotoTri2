package com.malicia.mrg.mvc.models;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


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

    public ResultSet getJpegFromUuidFile(String UuidFile) throws SQLException {
        ResultSet rs = this.select("" +
                "select jpegData , croppedWidth, croppedHeight , digest " +
                "from RootPixels " +
                "where uuid = '" + UuidFile + "' " +
                ";");

        return rs;
    }

    public String getname() {
        return nomFichier + " " + dateFichierHR;
    }


}
