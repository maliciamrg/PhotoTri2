package com.malicia.mrg.mvc.models;

/**
 * The type Ele.
 */
public class AgLibraryFile {
    public static final String REP_NEW = "repNew";
    /**
     * The Lc idx filename.
     */
    private String lcIdxFilename;
    private String fileIdLocal;
    private AgLibraryRootFolder agLibraryRootFolder;
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
    public AgLibraryFile(String absolutePath, String pathFromRoot, String lcIdxFilename, String fileIdLocal, AgLibraryRootFolder agLibraryRootFolder) {
        this.absolutePath = absolutePath;
        this.pathFromRoot = pathFromRoot;
        this.lcIdxFilename = lcIdxFilename;
        this.fileIdLocal = fileIdLocal;
        this.agLibraryRootFolder = agLibraryRootFolder;
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

}
