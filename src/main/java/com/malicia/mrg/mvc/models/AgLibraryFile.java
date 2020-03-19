package com.malicia.mrg.mvc.models;

import static com.malicia.mrg.mvc.models.SystemFiles.normalizePath;

/**
 * The type Ele.
 */
public class AgLibraryFile {
    public static final String REP_NEW = "repNew";
    public boolean setedited;
    public double starValue;
    /**
     * The Lc idx filename.
     */
    private String lcIdxFilename;
    private String fileIdLocal;
    private AgLibraryRootFolder agLibraryRootFolder;
    private String fileformat;
    private String pathFromRoot;
    private String absolutePath;

    /**
     * Instantiates a new Ele.
     *
     * @param absolutePath        the absolute path
     * @param pathFromRoot        the path from root
     * @param lcIdxFilename       the lc idx filename
     * @param fileIdLocal
     * @param agLibraryRootFolder
     */
    public AgLibraryFile(String absolutePath,
                         String pathFromRoot,
                         String lcIdxFilename,
                         String fileIdLocal,
                         AgLibraryRootFolder agLibraryRootFolder,
                         double starValue,
                         String fileformat) {
        this.absolutePath = absolutePath;
        this.pathFromRoot = pathFromRoot;
        this.lcIdxFilename = lcIdxFilename;
        this.fileIdLocal = fileIdLocal;
        this.agLibraryRootFolder = agLibraryRootFolder;
        this.starValue = starValue;
        this.fileformat = fileformat;
    }

    public String getFileIdLocal() {
        return fileIdLocal;
    }

    public AgLibraryRootFolder getAgLibraryRootFolder() {
        return agLibraryRootFolder;
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

    public boolean estRejeter() {
        return starValue < 0;
    }

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

    public String getPath() {
        return normalizePath(absolutePath + pathFromRoot + lcIdxFilename);
    }
}
