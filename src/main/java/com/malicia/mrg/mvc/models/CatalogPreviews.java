package com.malicia.mrg.mvc.models;

import java.io.File;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * The type Catalog previews.
 */
public class CatalogPreviews extends SQLiteJDBCDriverConnection {

    /**
     * The Date fichier.
     */
    public long dateFichier;
    /**
     * The Date fichier hr.
     */
    public String dateFichierHR;
    /**
     * The Nom fichier.
     */
    public String nomFichier;
    /**
     * The Cheminfichier previews.
     */
    public String cheminfichierPreviews = "";

    /**
     * Instantiates a new Catalog previews.
     *
     * @param catalogPreviews the catalog previews
     * @throws SQLException the sql exception
     */
    public CatalogPreviews(String catalogPreviews) throws SQLException {
        super(catalogPreviews);
        refreshdataLrcat(catalogPreviews);

    }

    private void refreshdataLrcat(String catalogPreviews) {
        cheminfichierPreviews = catalogPreviews;
        File cltg = new File(cheminfichierPreviews);
        nomFichier = cltg.getName();
        dateFichier = cltg.lastModified();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        dateFichierHR = dateFormat.format(dateFichier);
    }

    /**
     * Gets jpeg from uuid file.
     *
     * @param uuidFile the uuid file
     * @return the jpeg from uuid file
     * @throws SQLException the sql exception
     */
    public Blob getJpegFromUuidFile(String uuidFile) throws SQLException {
        ResultSet rs = this.select("" +
                "select jpegData " +
                "from RootPixels " +
                "where uuid = '" + uuidFile + "' " +
                ";");
        while (rs.next()) {
            return rs.getBlob("jpegData");
        }
        return null;
    }

    /**
     * Gets .
     *
     * @return the
     */
    public String getname() {
        return nomFichier + " " + dateFichierHR;
    }


}
