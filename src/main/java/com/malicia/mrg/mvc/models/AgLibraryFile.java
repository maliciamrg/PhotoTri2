package com.malicia.mrg.mvc.models;

import java.sql.SQLException;

import static com.malicia.mrg.mvc.models.SystemFiles.normalizePath;

/**
 * The type Ele.
 */
public class AgLibraryFile {
    /**
     * The constant REP_NEW.
     */
    public static final String REP_NEW = "repNew";
    private AgLibrarySubFolder subFolder;
    /**
     * The Setedited.
     */
    private boolean edited;
    /**
     * The Star value.
     */
    private double starValue;
    /**
     * The Lc idx filename.
     */
    private String lcIdxFilename;
    private String fileIdLocal;
    private String fileformat;
    private long captureTime;
    private String fileIdGlobal;
    private int addRotate;

    /**
     * Instantiates a new Ele.
     */
    public AgLibraryFile(AgLibrarySubFolder subFolder,
                         String lcIdxFilename,
                         String fileIdLocal,
                         double starValue,
                         String fileformat,
                         long captureTime,
                         String fileIdGlobal,
                         String orientation) {
        this.subFolder = subFolder;
        this.lcIdxFilename = lcIdxFilename;
        this.fileIdLocal = fileIdLocal;
        this.starValue = starValue;
        this.fileformat = fileformat;
        this.captureTime = captureTime;
        this.fileIdGlobal = fileIdGlobal;
        this.addRotate = translateOrientation(orientation);
    }

    private int translateOrientation(String orientation) {
        if (orientation==null){return 0;}
        switch (orientation) {
            case "BC":
                return 90;
            case "CD":
                return 180;
            case "DA":
                return 270;
            default:
                return 0;
        }
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public double getStarValue() {
        return starValue;
    }

    public void setStarValue(double starValue) {
        this.starValue = starValue;
    }

    /**
     * Gets capture time.
     *
     * @return the capture time
     */
    public long getCaptureTime() {
        return captureTime;
    }

    /**
     * Gets file id local.
     *
     * @return the file id local
     */
    public String getFileIdLocal() {
        return fileIdLocal;
    }

    /**
     * Gets absolute path.
     *
     * @return the absolute path
     */
    public String getAbsolutePath() {
        return subFolder.getAgLibraryRootFolder().absolutePath;
    }

    /**
     * Gets path from root.
     *
     * @return the path from root
     */
    public String getPathFromRoot() {
        return subFolder.getPathFromRoot();
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
     * Est rejeter boolean.
     *
     * @return the boolean
     */
    public boolean estRejeter() {
        return starValue < 0;
    }

    /**
     * Est photo boolean.
     *
     * @return the boolean
     */
    public boolean estPhoto() {
        switch (fileformat.toLowerCase()) {
            case "video":
                return false;
            case "mp4":
                return false;
            case "avi":
                return false;
            case "dng":
                return true;
            case "tiff":
                return true;
            case "raw":
                return true;
            case "arw":
                return true;
            default:
                return true;
        }
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return normalizePath(subFolder.getAgLibraryRootFolder().absolutePath + subFolder.getPathFromRoot() + lcIdxFilename);
    }

    /**
     * Gets file id global.
     *
     * @return the file id global
     */
    public String getFileIdGlobal() {
        return fileIdGlobal;
    }

    public int getAddRotate() {
        return addRotate;
    }

    public void setAddRotate(int addRotate) {
        this.addRotate += addRotate;
        if (this.addRotate > 270) {
            this.addRotate = this.addRotate - 360;
        } else {
            if (this.addRotate < -270) {
                this.addRotate = this.addRotate + 360;
            }
        }
    }

    public void valeurDecrease() {
        if (starValue > -1) {
            starValue -= 1;
            edited = true;
        }
    }

    public void valeurIncrease() {
        if (starValue < 5) {
            starValue += 1;
            edited = true;
        }
    }

    public void enregistrerStarValue() throws SQLException {
        double editstartvalue = starValue;
        if (starValue < 0) {
            editstartvalue = 0;
        }
        subFolder.getAgLibraryRootFolder().sqlEditStarValue(fileIdLocal, editstartvalue);
    }
}
