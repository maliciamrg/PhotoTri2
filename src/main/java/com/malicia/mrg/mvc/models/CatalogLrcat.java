package com.malicia.mrg.mvc.models;

import com.malicia.mrg.app.Context;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.malicia.mrg.app.Context.lrcat;


public class CatalogLrcat extends SQLiteJDBCDriverConnection {

    public long dateFichier;
    public String dateFichierHR;
    public String nomFichier;
    public Map<String, AgLibraryRootFolder> rep = new HashMap();
    public String cheminfichierLrcat = "";
    public Map<Integer, ObservableList<String>> listeZ = new HashMap();

    public CatalogLrcat(String catalogLrcat) throws SQLException {
        super(catalogLrcat);

        refreshdataLrcat(catalogLrcat);

        addrootFolder("repLegacy", Context.appParam.getString("repLegacy"));
        addrootFolder("repEncours", Context.appParam.getString("repEncours"));
        addrootFolder("repKidz", Context.appParam.getString("repKidz"));
        addrootFolder("repNew", Context.appParam.getString("repNew"));
        for (Integer key : Context.categories.keySet()) {
            addrootFolder("repCat" + key, Context.categories.get(key).getRepertoire());
        }

    }

    private void refreshdataLrcat(String catalogLrcat) {
        cheminfichierLrcat = catalogLrcat;
        File cltg = new File(cheminfichierLrcat);
        nomFichier = cltg.getName();
        dateFichier = cltg.lastModified();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        dateFichierHR = dateFormat.format(dateFichier);
    }

    private void addrootFolder(String nomRep, String appParamString) throws SQLException {
        ResultSet rs = this.select("" +
                "select * " +
                "from AgLibraryRootFolder " +
                "where name = '" + appParamString + "' " +
                ";");
        while (rs.next()) {
            String rootfolderidlocal = rs.getString("id_local");
            String absolutePath = rs.getString("absolutePath");
            String name = rs.getString("name");
            AgLibraryRootFolder tmpRootLib = new AgLibraryRootFolder(this, name, rootfolderidlocal, absolutePath);
            rep.put(nomRep, tmpRootLib);
        }
    }

    public void reconnect() {
        this.connect(cheminfichierLrcat);
        refreshdataLrcat(cheminfichierLrcat);
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
    private int sqlDeleteRepertory() throws SQLException {

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

    public String spyfirst() throws SQLException {
        String sql = "select * FROM AgLibraryFolder " +
                "ORDER by id_local desc " +
                "; ";
        select(sql);
        sql = "select * FROM AgLibraryFile " +
                "ORDER by id_local desc " +
                "; ";
        select(sql);
        sql = "select " +
                "c.absolutePath , " +
                "b.pathFromRoot , " +
                "a.lc_idx_filename as lc_idx_filename , " +
                "c.id_local as path_id_local , " +
                "b.id_local as folder_id_local , " +
                "a.id_local as file_id_local  , " +
                "b.rootFolder " +
                "from AgLibraryFile a  " +
                "inner join AgLibraryFolder b   " +
                " on a.folder = b.id_local  " +
                "inner join AgLibraryRootFolder c   " +
                " on b.rootFolder = c.id_local  " +
                ";";
        ResultSet rs = select(sql);
        String txtret = "";
        int nb = 0;
        int ko = 0;
        while (rs.next()) {
            File filepath = new File(rs.getString("absolutePath") + rs.getString("pathFromRoot") + rs.getString("lc_idx_filename"));
            nb += 1;
            if (!filepath.exists()) {
                txtret += "ko = " + filepath.toString() + "\n";
                ko += 1;
            }

        }
        txtret += " nb logique = " + nb + " : absent physique = " + ko + "\n";
        return txtret;
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

    public int openLigthroomLrcatandWait() throws IOException, InterruptedException {
        // open ligthroom catalog
        disconnect();

        Process process = Runtime.getRuntime().exec("cmd /c  " + "\"" + cheminfichierLrcat + "\"");

        StringBuilder output = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }


        int exitVal = process.waitFor();

        reconnect();
        return exitVal;
    }

    public String getname() {
        return nomFichier + " " + dateFichierHR;
    }

    public ObservableList<AgLibrarySubFolder> getlistofrepertorytoprocess() throws SQLException {
        ObservableList<AgLibrarySubFolder> listrep = FXCollections.observableArrayList();
        for (Map.Entry<String, AgLibraryRootFolder> entry : rep.entrySet()) {
            listrep.addAll(entry.getValue().getlistofrepertorytoprocess());
        }
        return listrep;
    }

    public ObservableList<String> getlistofpossiblecat() {
        return getlistofx("repCatx");
    }

    public ObservableList<String> getlistofx(String key) {
        if (Context.appParam.containsKey(key)) {
            return FXCollections.observableArrayList(Context.appParam.getString(key).split(","));
        } else {
            ObservableList<String> ret = FXCollections.observableArrayList();
            String[] decript = key.split("%");
            if (decript.length > 1) {
                if (Context.appParam.containsKey(decript[1])) {
                    return FXCollections.observableArrayList(Context.appParam.getString(decript[1]).split(","));
                } else {
                    ret.add(key);
                }
            } else {
                ret.add(key);
            }
            return ret;
        }
    }

    public void setListeZ(int numListeZ) {
        listeZ.put(numListeZ,lrcat.getlistofx(Context.formatZ.get(numListeZ)));
    }

    public ObservableList<String> getListeZ(int numListeZ) {
        return listeZ.get(numListeZ);
    }
}
