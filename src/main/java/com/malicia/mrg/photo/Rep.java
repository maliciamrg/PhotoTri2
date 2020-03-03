package com.malicia.mrg.photo;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.models.ActionfichierRepertoire;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedDeque;

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
    String folderIdLocal;
    /**
     * The Path from root.
     */
    String pathFromRoot;
    private String path;

    /**
     * Instantiates a new Rep.
     *
     * @param idxrep        the idxrep
     * @param folderIdLocal the folder id local
     * @param pathFromRoot  the path from root
     */
    public Rep(int idxrep, String folderIdLocal, String pathFromRoot) {
        this.idxrep = idxrep;
        this.folderIdLocal = folderIdLocal;
        this.pathFromRoot = pathFromRoot;
    }

    public Rep(String path) {
        this.path = path;
        this.pathFromRoot = ActionfichierRepertoire.normalizePath(path).replace(ActionfichierRepertoire.normalizePath(Context.getAbsolutePathFirst()),"");
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
     * Gets folder id local.
     *
     * @return the folder id local
     */
    public String getFolderIdLocal() {
        return folderIdLocal;
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
     * Moveto.
     *
     * @param rename the rename
     * @throws SQLException the sql exception
     */
    public void renameTo(String rename) throws SQLException {
        ActionfichierRepertoire.renommerUnRepertoire(folderIdLocal, rename, pathFromRoot);
        pathFromRoot = rename;
    }

}
