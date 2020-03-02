package com.malicia.mrg.photo;

import com.malicia.mrg.mvc.controllers.ActionfichierRepertoire;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The type Ele.
 */
public class Ele {
    /**
     * The Idx.
     */
    int idx;
    /**
     * The File id local.
     */
    String file_id_local;
    /**
     * The Lc idx filename.
     */
    String lc_idx_filename;
    /**
     * The Folder id local.
     */
    String folder_id_local;
    /**
     * The Capture time.
     */
    String captureTime;
    /**
     * The Path from root.
     */
    String pathFromRoot;

    /**
     * Instantiates a new Ele.
     *
     * @param idx             the idx
     * @param file_id_local   the file id local
     * @param lc_idx_filename the lc idx filename
     * @param folder_id_local the folder id local
     * @param captureTime     the capture time
     * @param pathFromRoot    the path from root
     */
    public Ele(int idx, String file_id_local, String lc_idx_filename, String folder_id_local, String captureTime, String pathFromRoot) {
        this.idx = idx;
        this.file_id_local = file_id_local;
        this.lc_idx_filename = lc_idx_filename;
        this.folder_id_local = folder_id_local;
        this.captureTime = captureTime;
        this.pathFromRoot = pathFromRoot;
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
     * Gets file id local.
     *
     * @return the file id local
     */
    public String getFile_id_local() {
        return file_id_local;
    }

    /**
     * Sets file id local.
     *
     * @param file_id_local the file id local
     */
    public void setFile_id_local(String file_id_local) {
        this.file_id_local = file_id_local;
    }

    /**
     * Gets lc idx filename.
     *
     * @return the lc idx filename
     */
    public String getLc_idx_filename() {
        return lc_idx_filename;
    }

    /**
     * Sets lc idx filename.
     *
     * @param lc_idx_filename the lc idx filename
     */
    public void setLc_idx_filename(String lc_idx_filename) {
        this.lc_idx_filename = lc_idx_filename;
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
     * Gets capture time.
     *
     * @return the capture time
     */
    public String getCaptureTime() {
        return captureTime;
    }

    /**
     * Sets capture time.
     *
     * @param captureTime the capture time
     */
    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }

    /**
     * Moveto.
     *
     * @param rep the rep
     */
    public void moveto(Rep rep) throws IOException, SQLException {
        ActionfichierRepertoire.move_file(file_id_local, rep.getFolder_id_local() , lc_idx_filename , pathFromRoot , rep.getPathFromRoot());
     //   ActionfichierRepertoire.move_file(rep);
    }

    /**
     * Renameto.
     *
     * @param rename the rename
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public void renameto(String rename) throws IOException, SQLException {
        ActionfichierRepertoire.rename_file(file_id_local, lc_idx_filename , rename , pathFromRoot);
        lc_idx_filename = rename;
    }
}
