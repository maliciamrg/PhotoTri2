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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.malicia.mrg.app.Context.formatZ;


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

        addrootFolder("repLegacy", Context.appParam.getString("repLegacy"), AgLibraryRootFolder.TYPE_LEG);
        addrootFolder("repEncours", Context.appParam.getString("repEncours"), AgLibraryRootFolder.TYPE_ENC);
        addrootFolder("repKidz", Context.appParam.getString("repKidz"), AgLibraryRootFolder.TYPE_KID);
        addrootFolder("repNew", Context.appParam.getString("repNew"), AgLibraryRootFolder.TYPE_NEW);

        //array de categories
        int numcat = 1;
        for (String repCat : Context.appParam.getString("repCatx").split(",")) {
            addrootFolder("repCat" + numcat, repCat, AgLibraryRootFolder.TYPE_CAT);
            rep.get("repCat" + numcat).nbminiCat = Context.appParam.getString("nbminiCat" + numcat);
            rep.get("repCat" + numcat).nbmaxCat = Context.appParam.getString("nbmaxCat" + numcat);
            numcat += 1;
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

    private void addrootFolder(String nomRep, String appParamString, int typeRoot) throws SQLException {
        ResultSet rs = this.select("" +
                "select * " +
                "from AgLibraryRootFolder " +
                "where name = '" + appParamString + "' " +
                ";");
        while (rs.next()) {
            String rootfolderidlocal = rs.getString("id_local");
            String absolutePath = rs.getString("absolutePath");
            String name = rs.getString("name");
            AgLibraryRootFolder tmpRootLib = new AgLibraryRootFolder(this, name, rootfolderidlocal, absolutePath, typeRoot);
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

    public ObservableList<AgLibrarySubFolder> getlistofrepertorytoprocess(List<Integer> typeToProcess) throws SQLException {
        ObservableList<AgLibrarySubFolder> listrep = FXCollections.observableArrayList();
        for (Map.Entry<String, AgLibraryRootFolder> entry : rep.entrySet()) {
            if (typeToProcess.contains(entry.getValue().typeRoot)) {
                listrep.addAll(entry.getValue().getlistofrepertorytoprocess());
            }
        }
        return listrep;
    }

    public ObservableList<String> getlistofpathFromRoottoprocess(List<Integer> typeToProcess) throws SQLException {
        ObservableList<String> listrep = FXCollections.observableArrayList();
        for (Map.Entry<String, AgLibraryRootFolder> entry : rep.entrySet()) {
            if (typeToProcess.contains(entry.getValue().typeRoot)) {
                listrep.addAll(entry.getValue().getlistofpathFromRoottoprocess());
            }
        }
        return listrep;
    }


    public void setListeZ(int numListeZ) throws SQLException {
        ObservableList<String> listetmp = Context.getlistofx(Context.formatZ.get(numListeZ));

        if (!(listetmp.size() == 1 && listetmp.get(0).startsWith("%") && listetmp.get(0).endsWith("%"))) {
            ObservableList<String> listeleFromCat = FXCollections.observableArrayList();

            ObservableList<String> listSubCat = getlistofpathFromRoottoprocess(Arrays.asList(AgLibraryRootFolder.TYPE_CAT));
            listSubCat.forEach(subFolder -> {

                String[] part = subFolder.split(Context.appParam.getString("ssrepformatSep"));
                if (part.length == formatZ.size()) {
                    String elez = Context.appParam.getString("caractsup") + part[numListeZ - 1].replace("/", "");
                    if (!listeleFromCat.contains(elez)) {
                        listeleFromCat.add(elez);
                    }
                }

            });

            listetmp.addAll(listeleFromCat);
        }

        listeZ.put(numListeZ, listetmp);
    }

    public ObservableList<String> getlistofKeyword(String racinegenealogy) throws SQLException {
        ObservableList<String> list_lc_name = FXCollections.observableArrayList();
        ResultSet rs = this.select("select lc_name " +
                "from AgLibraryKeyword " +
                "where genealogy like ( " +
                "select '/%_' || id_local || '/%' " +
                "from  AgLibraryKeyword " +
                "where lc_name = '" + racinegenealogy.toLowerCase() + "' ) " +
                ";");
        while (rs.next()) {
            list_lc_name.add(rs.getString("lc_name"));
        }
        return list_lc_name;
    }
}
