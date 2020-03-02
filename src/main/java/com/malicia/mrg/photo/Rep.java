package com.malicia.mrg.photo;

import com.malicia.mrg.mvc.controllers.ActionfichierRepertoire;

import java.sql.SQLException;

/**
 * The type Rep.
 */
public class Rep {
    /**
     * The Idxrep.
     */
    int idxrep;
    /**
     * The Folder id local.
     */
    String folder_id_local;
    /**
     * The Path from root.
     */
    String pathFromRoot;


    /**
     * Instantiates a new Rep.
     *
     * @param idxrep          the idxrep
     * @param folder_id_local the folder id local
     * @param pathFromRoot    the path from root
     */
    public Rep(int idxrep, String folder_id_local, String pathFromRoot) {
        this.idxrep = idxrep;
        this.folder_id_local = folder_id_local;
        this.pathFromRoot = pathFromRoot;
    }

    /**
     * Gets idxrep.
     *
     * @return the idxrep
     */
    public int getIdxrep() {
        return idxrep;
    }

    /**
     * Sets idxrep.
     *
     * @param idxrep the idxrep
     */
    public void setIdxrep(int idxrep) {
        this.idxrep = idxrep;
    }

    /**
     * Gets folder id local.
     *
     * @return the folder id local
     */
    public String getFolder_id_local() {
        return folder_id_local;
    }

    /**
     * Sets folder id local.
     *
     * @param folder_id_local the folder id local
     */
    public void setFolder_id_local(String folder_id_local) {
        this.folder_id_local = folder_id_local;
    }

    /**
     * Gets path from root.
     *
     * @return the path from root
     */
    public String getPathFromRoot() {
        return pathFromRoot;
    }

    /**
     * Sets path from root.
     *
     * @param pathFromRoot the path from root
     */
    public void setPathFromRoot(String pathFromRoot) {
        this.pathFromRoot = pathFromRoot;
    }


    /**
     * Moveto.
     *
     * @param rename the rename
     * @throws SQLException the sql exception
     */
    public void moveto(String rename) throws SQLException {
        ActionfichierRepertoire.renommerUnRepertoire(folder_id_local, rename, pathFromRoot);
    }

}
