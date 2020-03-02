package com.malicia.mrg.photo;

import com.malicia.mrg.mvc.models.ActionfichierRepertoire;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The type Ele.
 */
public class Ele {
    /**
     * The Idx.
     */
    private int idx;
    /**
     * The File id local.
     */
    private String fileIdLocal;
    /**
     * The Lc idx filename.
     */
    private String lcIdxFilename;
    /**
     * The Folder id local.
     */
    private String folderIdLocal;
    /**
     * The Capture time.
     */
    private String captureTime;
    /**
     * The Path from root.
     */
    private String pathFromRoot;

    /**
     * Instantiates a new Ele.
     *
     * @param idx           the idx
     * @param fileIdLocal   the file id local
     * @param lcIdxFilename the lc idx filename
     * @param folderIdLocal the folder id local
     * @param captureTime   the capture time
     * @param pathFromRoot  the path from root
     */
    public Ele(int idx, String fileIdLocal, String lcIdxFilename, String folderIdLocal, String captureTime, String pathFromRoot) {
        this.idx = idx;
        this.fileIdLocal = fileIdLocal;
        this.lcIdxFilename = lcIdxFilename;
        this.folderIdLocal = folderIdLocal;
        this.captureTime = captureTime;
        this.pathFromRoot = pathFromRoot;
    }

    /**
     * Gets idx.
     *
     * @return the idx
     */
    public int getIdx() {
        return idx;
    }

    /**
     * Sets idx.
     *
     * @param idx the idx
     */
    public void setIdx(int idx) {
        this.idx = idx;
    }

    /**
     * Gets lc idx filename.
     *
     * @return the lc idx filename
     */
    public String getLcIdxFilename() {
        return lcIdxFilename;
    }

    /**
     * Moveto.
     *
     * @param rep the rep
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public void moveto(Rep rep) throws IOException, SQLException {
        ActionfichierRepertoire.move_file(fileIdLocal, rep.getFolderIdLocal(), lcIdxFilename, pathFromRoot, rep.getPathFromRoot());
    }

    /**
     * Renameto.
     *
     * @param rename the rename
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public void renameto(String rename) throws IOException, SQLException {
        ActionfichierRepertoire.rename_file(fileIdLocal, lcIdxFilename, rename, pathFromRoot);
        lcIdxFilename = rename;
    }
}
