package com.malicia.mrg.mvc.models;

/**
 * The type Ele.
 */
public class AgLibraryFile {
    /**
     * The Lc idx filename.
     */
    private String lcIdxFilename;
    private String file_id_local;
    private AgLibraryRootFolder agLibraryRootFolder;
    private String pathFromRoot;
    private String absolutePath;
    /**
     * Instantiates a new Ele.
     *
     * @param absolutePath        the absolute path
     * @param pathFromRoot        the path from root
     * @param lcIdxFilename       the lc idx filename
     * @param file_id_local
     * @param agLibraryRootFolder
     */
    public AgLibraryFile(String absolutePath, String pathFromRoot, String lcIdxFilename, String file_id_local, AgLibraryRootFolder agLibraryRootFolder) {
        this.absolutePath = absolutePath;
        this.pathFromRoot = pathFromRoot;
        this.lcIdxFilename = lcIdxFilename;
        this.file_id_local = file_id_local;
        this.agLibraryRootFolder = agLibraryRootFolder;
    }

    public String getFile_id_local() {
        return file_id_local;
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
