package com.malicia.mrg.photo;

import com.malicia.mrg.mvc.controllers.ActionfichierRepertoire;

import java.io.IOException;
import java.sql.SQLException;

public class Ele {
    int idx;
    String file_id_local;
    String lc_idx_filename;
    String folder_id_local;
    String captureTime;
    String pathFromRoot;

    public Ele(int idx, String file_id_local, String lc_idx_filename, String folder_id_local, String captureTime, String pathFromRoot) {
        this.idx = idx;
        this.file_id_local = file_id_local;
        this.lc_idx_filename = lc_idx_filename;
        this.folder_id_local = folder_id_local;
        this.captureTime = captureTime;
        this.pathFromRoot = pathFromRoot;
    }

    public String getPathFromRoot() {
        return pathFromRoot;
    }

    public void setPathFromRoot(String pathFromRoot) {
        this.pathFromRoot = pathFromRoot;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getFile_id_local() {
        return file_id_local;
    }

    public void setFile_id_local(String file_id_local) {
        this.file_id_local = file_id_local;
    }

    public String getLc_idx_filename() {
        return lc_idx_filename;
    }

    public void setLc_idx_filename(String lc_idx_filename) {
        this.lc_idx_filename = lc_idx_filename;
    }

    public String getFolder_id_local() {
        return folder_id_local;
    }

    public void setFolder_id_local(String folder_id_local) {
        this.folder_id_local = folder_id_local;
    }

    public String getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }

    public void moveto(Rep rep) {
     //   ActionfichierRepertoire.move_file(rep);
    }

    public void renameto(String rename) throws IOException, SQLException {
        ActionfichierRepertoire.rename_file(file_id_local, lc_idx_filename , rename , pathFromRoot);
    }
}
