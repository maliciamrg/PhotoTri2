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

    /**
     * Instantiates a new Ele.
     *
     * @param lcIdxFilename the lc idx filename
     * @param pathFromRoot  the path from root
     */
    public Ele( String lcIdxFilename, String pathFromRoot) {
        this.lcIdxFilename = lcIdxFilename;
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
     * Gets lc idx filename.
     *
     * @return the lc idx filename
     */
    public String getLcIdxFilename() {
        return lcIdxFilename;
    }

}
