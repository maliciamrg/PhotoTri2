package com.malicia.mrg.app.photo;

import com.malicia.mrg.app.ActionfichierRepertoire;
import com.malicia.mrg.app.Context;

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

    /**
     * Instantiates a new Rep.
     *
     * @param path the path
     */
    public Rep(String path) {
        this.pathFromRoot = ActionfichierRepertoire.normalizePath(path).replace(ActionfichierRepertoire.normalizePath(Context.getAbsolutePathFirst()), "");
    }

}
