package com.malicia.mrg.mvc.models;

import static com.malicia.mrg.mvc.models.SystemFiles.normalizePath;

/**
 * The type Ele.
 */
public class AgLibraryFile {
    /**
     * The constant REP_NEW.
     */
    public static final String REP_NEW = "repNew";

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
    private String pathFromRoot;
    private String absolutePath;
    private int addRotate;

    /**
     * Instantiates a new Ele.
     *
     * @param absolutePath        the absolute path
     * @param pathFromRoot        the path from root
     * @param lcIdxFilename       the lc idx filename
     * @param fileIdLocal         the file id local
     * @param agLibraryRootFolder the ag library root folder
     * @param starValue           the star value
     * @param fileformat          the fileformat
     * @param captureTime         the capture time
     * @param fileIdGlobal        the file id global
     */
    public AgLibraryFile(String absolutePath,
                         String pathFromRoot,
                         String lcIdxFilename,
                         String fileIdLocal,
                         double starValue,
                         String fileformat,
                         long captureTime,
                         String fileIdGlobal) {
        this.absolutePath = absolutePath;
        this.pathFromRoot = pathFromRoot;
        this.lcIdxFilename = lcIdxFilename;
        this.fileIdLocal = fileIdLocal;
        this.starValue = starValue;
        this.fileformat = fileformat;
        this.captureTime = captureTime;
        this.fileIdGlobal = fileIdGlobal;
        this.addRotate = 0;
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
        return absolutePath;
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
            case "raw":
                return false;
            case "arw":
                return false;
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
        return normalizePath(absolutePath + pathFromRoot + lcIdxFilename);
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
        if (this.addRotate > 180) {
            this.addRotate = this.addRotate - 180;
        } else {
            if (this.addRotate < -180) {
                this.addRotate = this.addRotate + 180;
            }
        }
    }

    public void valeurDecrease() {
        if ( starValue > -1) {
            starValue -= 1;
            edited =true;
        }
    }

    public void valeurIncrease() {
        if (starValue < 5) {
            starValue += 1;
            edited =true;
        }
    }

    public void execmodification(AgLibrarySubFolder subFolderDest) {
        if (isEdited()){
            String source = normalizePath(absolutePath + pathFromRoot + lcIdxFilename);

        }
    }
}
