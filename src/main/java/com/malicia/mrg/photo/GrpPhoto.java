package com.malicia.mrg.photo;

import com.malicia.mrg.mvc.controllers.ActionfichierRepertoire;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Grp photo.
 */
public class GrpPhoto {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat repDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String cameraModelGrp = "";
    private long mintGrp;
    private long maxtGrp;
    private List<String> elesrc = new ArrayList<>();
    private List<String> eledest = new ArrayList<>();
    private List<String> eledt = new ArrayList<>();
    private String absolutePath;
    private String pathFromRootComumn;
    private String forceGroup = "";

    /**
     * Instantiates a new Grp photo.
     */
    public GrpPhoto() {

    }

    /**
     * Instantiates a new Grp photo.
     *
     * @param forceGroup         the force group
     * @param abspath            the abspath
     * @param pathfromrootcomumn the pathfromrootcomumn
     */
    public GrpPhoto(String forceGroup, String abspath, String pathfromrootcomumn) {
        this.forceGroup = forceGroup;
        absolutePath = abspath;
        pathFromRootComumn = pathfromrootcomumn;
    }

    /**
     * Gets eledest.
     *
     * @return the eledest
     */
    public List<String> getEledest() {
        return eledest;
    }

    /**
     * Sets eledest.
     *
     * @param eledest the eledest
     */
    public void setEledest(List<String> eledest) {
        this.eledest = eledest;
    }

    /**
     * Gets camera model grp.
     *
     * @return the camera model grp
     */
    public String getCameraModelGrp() {
        return cameraModelGrp;
    }

    /**
     * Gets eledt.
     *
     * @return the eledt
     */
    public List<String> getEledt() {
        return eledt;
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
     * Gets path from root comumn.
     *
     * @return the path from root comumn
     */
    public String getPathFromRootComumn() {
        return pathFromRootComumn;
    }

    /**
     * Gets elesrc.
     *
     * @return the elesrc
     */
    public List<String> getElesrc() {
        return elesrc;
    }


    @Override
    public String toString() {

        Date datemin = new Date(mintGrp * 1000);
        String datemintFormat = simpleDateFormat.format(datemin);

        Date datemaxt = new Date(maxtGrp * 1000);
        String datemaxtFormat = simpleDateFormat.format(datemaxt);


        return "GrpPhoto{" +
                "cameraModelGrp='" + cameraModelGrp + '\'' +
                ", mintGrp=" + datemintFormat +
                ", maxtGrp=" + datemaxtFormat +
                ", nbele=" + elesrc.size() +
                ", getNomRepetrtoire=" + getNvxNomRepertoire() +
                "}" +
                "\n" +
                listeeletostring();
    }

    private String listeeletostring() {
        if (elesrc.size() != eledt.size() || elesrc.size() != eledest.size()) {
            throw new IllegalStateException("ele.size=>" + elesrc.size() + " != " + " eledt.size=>" + eledt.size()+ " != " + " eledest.size=>" + eledest.size());
        }
        StringBuilder listeletostring = new StringBuilder();
        for (int i = 0; i < elesrc.size(); i++) {
            listeletostring.append("     " + eledt.get(i) + " -> " + elesrc.get(i)  + " -> " + eledest.get(i)  + "\n");
        }
        return listeletostring.toString();
    }

    /**
     * Add boolean.
     *
     * @param cameraModel        the camera model
     * @param captureTime        the capture time
     * @param mint               the mint
     * @param maxt               the maxt
     * @param elesrc             the elesrc
     * @param absolutepath       the absolutepath
     * @param pathfromrootcomumn the pathfromrootcomumn
     * @return the boolean
     */
    public boolean add(String cameraModel, double captureTime, long mint, long maxt, String elesrc, String absolutepath, String pathfromrootcomumn) {

        boolean first;
        first = absolutePath == null;
        if (pathFromRootComumn == null) {
            first = true;
        }

        if (first) {
            absolutePath = absolutepath;
            pathFromRootComumn = pathfromrootcomumn;
        } else {
            if (!testInterval(mint, maxt)) return false;
        }

        forceadd(cameraModel, captureTime, mint, maxt, ActionfichierRepertoire.normalizePath(elesrc));

        return true;
    }

    /**
     * Test interval boolean.
     *
     * @param mint the mint
     * @param maxt the maxt
     * @return the boolean
     */
    public boolean testInterval(long mint, long maxt) {
        if (mint > 0 && mint > maxtGrp) {
            return false;
        }
        if (maxt > 0 && maxt < mintGrp) {
            return false;
        }
        return true;
    }

    /**
     * Forceadd.
     *
     * @param cameraModel the camera model
     * @param captime     the captime
     * @param mint        the mint
     * @param maxt        the maxt
     * @param elesrc      the elesrc
     */
    public void forceadd(String cameraModel, double captime, long mint, long maxt, String elesrc) {
        //elesrc dans le groupe a conserver
        if (mint < mintGrp || mintGrp == 0) {
            mintGrp = mint;
        }
        if (maxt > maxtGrp) {
            maxtGrp = maxt;
        }
        if (cameraModel != null) {
            cameraModelGrp = cameraModel;
        }
        this.elesrc.add(elesrc);

        Date datecaptime = new Date((long) (captime * 1000));
        String datemaxtFormat = simpleDateFormat.format(datecaptime);

        eledt.add(datemaxtFormat);
    }

    /**
     * Gets .
     *
     * @return the
     */
    public int getnbele() {
        return elesrc.size();
    }

    /**
     * Gets nvx nom repertoire.
     *
     * @return the nvx nom repertoire
     */
    public String getNvxNomRepertoire() {
        if (forceGroup.compareTo("") != 0) {
            return (forceGroup);
        } else {
            Date datemin = new Date(mintGrp * 1000);
            String datemintFormat = repDateFormat.format(datemin);

            Date datemaxt = new Date(maxtGrp * 1000);
            String datemaxtFormat = repDateFormat.format(datemaxt);

            return (datemintFormat + "_" + datemaxtFormat).trim();
        }
    }

    /**
     * Add.
     *
     * @param eleIn     the ele in
     * @param eledtIn   the eledt in
     * @param eledestIn the eledest in
     */
    public void add(List<String> eleIn, List<String> eledtIn, List<String> eledestIn) {
        elesrc.addAll(eleIn);
        eledt.addAll(eledtIn);
        eledest.addAll(eledestIn);
    }

    /**
     * Isdate null boolean.
     *
     * @return the boolean
     */
    public boolean isdateNull() {
        return (mintGrp == -2082848400 || mintGrp == 0);
    }


    /**
     * Add eledest.
     *
     * @param i    the
     * @param dest the dest
     */
    public void addEledest(int i, String dest) {
        if (eledest.size() < i) {
            throw new IllegalStateException("nb ele dest non conforme : " + eledest.size() + " < " + i);
        } else {
            eledest.add(dest);
        }
    }
}
