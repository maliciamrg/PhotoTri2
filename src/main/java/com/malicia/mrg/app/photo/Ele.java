package com.malicia.mrg.app.photo;

/**
 * The type Ele.
 */
public class Ele {
    /**
     * The Lc idx filename.
     */
    private String lcIdxFilename;
    /**
     * The Path from root.
     */
    private String pathFromRoot;
    private String absolutePath;

    /**
     * Instantiates a new Ele.
     *
     * @param lcIdxFilename the lc idx filename
     * @param pathFromRoot  the path from root
     * @param absolutePath  the absolute path
     */
    public Ele(String lcIdxFilename, String pathFromRoot, String absolutePath) {
        this.lcIdxFilename = lcIdxFilename;
        this.pathFromRoot = pathFromRoot;
        this.absolutePath = absolutePath;
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
