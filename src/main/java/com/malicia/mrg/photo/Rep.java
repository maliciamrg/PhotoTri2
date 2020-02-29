package com.malicia.mrg.photo;

import com.malicia.mrg.mvc.controllers.ActionfichierRepertoire;

import java.sql.SQLException;

public class Rep {
    int idxrep;
    String folder_id_local;
    String pathFromRoot;


    public Rep(int idxrep, String folder_id_local, String pathFromRoot) {
        this.idxrep = idxrep;
        this.folder_id_local = folder_id_local;
        this.pathFromRoot = pathFromRoot;
    }

    public int getIdxrep() {
        return idxrep;
    }

    public void setIdxrep(int idxrep) {
        this.idxrep = idxrep;
    }

    public String getFolder_id_local() {
        return folder_id_local;
    }

    public void setFolder_id_local(String folder_id_local) {
        this.folder_id_local = folder_id_local;
    }

    public String getPathFromRoot() {
        return pathFromRoot;
    }

    public void setPathFromRoot(String pathFromRoot) {
        this.pathFromRoot = pathFromRoot;
    }


    public void moveto(String rename) throws SQLException {
        ActionfichierRepertoire.renommerUnRepertoire (folder_id_local, rename , pathFromRoot );
    }

}
