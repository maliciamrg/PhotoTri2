package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import static com.malicia.mrg.mvc.models.SystemFiles.normalizePath;


public class CatalogLrcat extends SQLiteJDBCDriverConnection {

    public Map<String, AgLibraryRootFolder> rep;
    public String cheminfichierLrcat = "";

    public CatalogLrcat(String catalogLrcat) throws SQLException {
        super(catalogLrcat);
        cheminfichierLrcat = catalogLrcat;
        addrootFolder("repLegacy");
        addrootFolder("repbookEvents");
        addrootFolder("repbookHolidays");
        addrootFolder("repbookShooting");
        addrootFolder("repEncours");
        addrootFolder("repKidz");
        addrootFolder("repNew");

    }

    public void addrootFolder(String nomRep) throws SQLException {
        AgLibraryRootFolder tmpRootLib = new AgLibraryRootFolder(this,Context.appParam.getString(nomRep));
        ResultSet rs = this.select("" +
                "select * " +
                "from AgLibraryRootFolder " +
                "where name = " + Context.appParam.getString(nomRep) + " " +
                ";");
        while (rs.next()) {
            tmpRootLib.id_local = rs.getString("id_local");
            tmpRootLib.absolutePath = rs.getString("absolutePath");
            tmpRootLib.name = rs.getString("name");
            rep.put(nomRep, tmpRootLib);
        }
    }

    public void reconnect() {
        this.connect(cheminfichierLrcat);
    }


    public void rangerRejet() throws IOException, SQLException {


        for (Map.Entry<String, AgLibraryRootFolder> entry : rep.entrySet()) {
            entry.getValue().rangerRejet();
        }

    }

    public int deleteAllRepertoireLogiqueVide() throws SQLException {
        int nbdel = 0;
        int nbdeltotal = 0;
        do {
                nbdel = sqlDeleteRepertory();
                nbdeltotal += nbdel;
        }
        while (nbdel > 0);

        return nbdeltotal;

    }


    /**
     * Sql delete repertory int.
     *
     * @return the int
     * @throws SQLException the sql exception
     */
    public int sqlDeleteRepertory() throws SQLException {

        //compte le nombre de photo presente dans la base poour le repertoire

        String sql = " delete from AgLibraryFolder  " +
                " where pathFromRoot in ( " +
                "select b.pathFromRoot " +
                "from AgLibraryFolder b " +
                "left join AgLibraryFile a " +
                "on a.folder = b.id_local " +
                "left join AgLibraryFolder c " +
                "on c.pathFromRoot like b.pathFromRoot || \"_%\" " +
                "where a.folder is  NULL " +
                "and  c.pathFromRoot  is  NULL " +
                "group by  b.pathFromRoot " +
                " ); ";
        PreparedStatement pstmt = null;
        return executeUpdate(sql);

    }

    public int deleteEmptyDirectory() throws IOException, SQLException {

        int nbdel = 0;
        int nbdeltotal = 0;
        for (Map.Entry<String, AgLibraryRootFolder> entry : rep.entrySet()) {
            nbdel = entry.getValue().DeleteEmptyDirectory();
            nbdeltotal += nbdel;
        }

        return nbdeltotal;

    }


    public long sqlGetPrevIdlocalforFolder() throws SQLException {
        String sql = "select * FROM AgLibraryFolder " +
                "ORDER by id_local desc " +
                "; ";
        ResultSet rs = select(sql);
        boolean first = true;
        long idLocalCalcul = 0;
        long id_local = 0;
        while (rs.next()) {
            // Recuperer les info de l'elements
            id_local = rs.getLong("id_local");
            if (first) {
                idLocalCalcul = id_local;
                first = false;
            } else {
                idLocalCalcul -= 1;
                if (idLocalCalcul > id_local) {
                    return idLocalCalcul;
                }
            }
        }
        // 0 ou 1 repertoire dans AgLibraryFolder
        if (idLocalCalcul == id_local) {
            idLocalCalcul = (id_local / 2) + 1;
        }
        return idLocalCalcul;
    }
}
