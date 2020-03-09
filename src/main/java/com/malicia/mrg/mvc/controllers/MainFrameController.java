package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.Main;
import com.malicia.mrg.app.ActionfichierRepertoire;
import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.photo.Ele;
import com.malicia.mrg.app.photo.ElePhoto;
import com.malicia.mrg.app.photo.GrpPhoto;
import com.malicia.mrg.mvc.models.CatLrcat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.malicia.mrg.app.ActionfichierRepertoire.normalizePath;


/**
 * The type Main frame controller.
 */
public class MainFrameController {

    private static final java.util.logging.Logger LOGGER;

    static {
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
    }

    /**
     * The Absolute path.
     */
    Map<String, String> absolutePath = new HashMap<>();
    @FXML
    private TextArea userlogInfo;
    private int ndDelTotal;

    /**
     * Instantiates a new Main frame controller.
     */
    public MainFrameController() {
        LOGGER.info("mainFrameController");
    }

    /**
     * Boucle supression repertoire physique boolean.
     */
    private int boucleSupressionRepertoire(File dir) throws IOException, SQLException {
        boolean returnVal = false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            int success = 0;
            for (int i = 0; i < children.length; i++) {
                success += boucleSupressionRepertoire(new File(dir, children[i]));
            }
            ndDelTotal += success;
            if (success == children.length) {
                // The directory is now empty directory free so delete it
                returnVal = ActionfichierRepertoire.deleteDir(dir);
                if (returnVal) {
                    return 1;
                }

            }

        }
        return 0;
    }

    /**
     * Gets ele bazar.
     *
     * @param repBazar the rep bazar
     * @return the ele bazar
     * @throws SQLException the sql exception
     */
    public java.util.List<ElePhoto> getEleBazar(String repBazar) throws SQLException {

//            constitution des groupes

        ResultSet rsele = Context.getLrcat_new().sqleleRepBazar(Context.getTempsAdherence(), repBazar);

        java.util.List<ElePhoto> listElePhoto = new ArrayList();


        while (rsele.next()) {


            // Recuperer les info de l'elements
            long captureTime = rsele.getLong(Context.CAPTURE_TIME);
            long mint = rsele.getLong("mint");
            long maxt = rsele.getLong("maxt");
            String src = rsele.getString("src");
            String absPath = rsele.getString(Context.PATH_FROM_ROOT);


            //Constitution des groupes de photo standard
            listElePhoto.add(new ElePhoto(captureTime, mint, maxt, src, absPath, Context.getRepNew() + "/"));


        }


        return listElePhoto;
    }

    /**
     * Regroupe ele rep hors bazarby group java . util . list.
     *
     * @param repBazar the rep bazar
     * @param repKidz  the rep kidz
     * @return the java . util . list
     * @throws SQLException the sql exception
     */
    public java.util.List<GrpPhoto> regroupeEleRepHorsBazarbyGroup(String repBazar, String repKidz) throws SQLException {

//            constitution des groupes
//        grpphotoenc.getAbsolutePath() + grpphotoenc.getPathFromRootComumn() + grpphotoenc.getNomRepetrtoire()
        ResultSet rsgrp = Context.getLrcat_new().sqlGroupByPlageAdheranceHorsRepBazar(Context.getTempsAdherence(), repBazar, repKidz);

        GrpPhoto grpPhotoEnc = new GrpPhoto();

        java.util.List<GrpPhoto> listGrpPhoto = new ArrayList();


        while (rsgrp.next()) {


            // Recuperer les info de l'elements
            long captureTime = rsgrp.getLong(Context.CAPTURE_TIME);
            long mint = rsgrp.getLong("mint");
            long maxt = rsgrp.getLong("maxt");
            String src = rsgrp.getString("src");
            String pathFromRoot = rsgrp.getString(Context.PATH_FROM_ROOT);
            String absPath = rsgrp.getString(Context.PATH_FROM_ROOT);


            //Constitution des groupes de photo standard
            if (!grpPhotoEnc.add("", captureTime, mint, maxt, src, absPath, pathFromRoot + "/")) {


                listGrpPhoto.add(grpPhotoEnc);


                grpPhotoEnc = new GrpPhoto();
                if (!grpPhotoEnc.add("", captureTime, mint, maxt, src, absPath, pathFromRoot + "/")) {
                    throw new IllegalStateException("Erreur l'ors de l'ajout de l'element au group de photo ");
                }
            }


        }
        listGrpPhoto.add(grpPhotoEnc);


        return listGrpPhoto;
    }

    /**
     * Initialize.
     */
    private void initialize() {
        LOGGER.info("initialize");

        if (Context.getPrimaryStage() != null) {
            Context.getPrimaryStage().sizeToScene();
        }

    }


    /**
     * Select le repertoire rootdu fichier ligthroom.
     * <p>
     * selecttioner le repertoire root sur lequelle les actions seront baser
     * modifier et sauvegarde dans le properties
     *
     * @param rootName the root name
     */
    private void selectLeRepertoireRootduFichierLigthroom(String rootName) throws IOException {
        Context.savePropertiesParameters(Context.getCurrentContext());
    }

    /**
     * Action makeadulpicatelrcatwithdate.
     */
    public void actionMakeadulpicatelrcatwithdate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String formattedDate = sdf.format(date);

        Context.getLrcatSource().forEach((k, v) -> {
                    String ori = v.toString();
                    File fori = new File(ori);
                    String dupdest = Context.getRepCatlogSauve() + "\\save_lrcat_" + formattedDate + "\\" + fori.getName();
                    File dest = new File(dupdest);
                    try {
                        ActionfichierRepertoire.mkdir(new File(dupdest).getParent());

                        if (fori.exists()) {
                            Files.copy(fori.toPath(), dest.toPath());
                            logecrireuserlogInfo("sauvegarde lrcat en :" + dupdest);
                        }
                    } catch (IOException e) {
                        logecrireuserlogInfo("sauvegarde erreur :" + fori.toPath());
                        excptlog(e);
                    }
                }
        );
    }

    private void excptlog(Exception theException) {
        StringWriter stringWritter = new StringWriter();
        PrintWriter printWritter = new PrintWriter(stringWritter, true);
        theException.printStackTrace(printWritter);
        printWritter.flush();
        stringWritter.flush();
        LOGGER.severe(() -> "theException = " + "\n" + stringWritter.toString());
    }


    /**
     * Boucle delete repertoire logique.
     */
    public void actionDeleteRepertoireLogique() {
        Context.getLrcat().forEach((k, v) -> {
                    try {
                        int nbdel = 0;
                        int nbdeltotal = 0;
                        do {
                            nbdel = ((CatLrcat) v).sqlDeleteRepertory();
                            nbdeltotal += nbdel;
                        }
                        while (nbdel > 0);

                        logecrireuserlogInfo("logical delete:" + String.format("%04d", nbdeltotal));
                    } catch (SQLException e) {
                        logecrireuserlogInfo(e.toString());
                        excptlog(e);
                    }
                }

        );
    }

    private void logecrireuserlogInfo(String msg) {
        userlogInfo.appendText(msg + "\n");
        LOGGER.info(msg);
    }

    /**
     * Move new to grp photos.
     */
    public void actionRangerRejet() {
        Context.getLrcat().forEach((k, v) -> {

                    try {
                        CatLrcat dblr = (CatLrcat) v;
                        ResultSet rsele = dblr.sqlgetListelementrejetaranger();

                        while (rsele.next()) {

                            // Recuperer les info de l'elements
                            String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);
                            String lcIdxFilename = rsele.getString(Context.LC_IDX_FILENAME);
                            String rootFolder = rsele.getString("rootFolder");
                            String absolutePath = dblr.getabsolutePath(rsele.getString("rootFolder"));

                            String source = normalizePath(absolutePath + pathFromRoot + lcIdxFilename);
                            String dest = source + ".rejet";


                            dblr.sqlmovefile(source, dest);

                        }
                    } catch (SQLException | IOException e) {
                        logecrireuserlogInfo(e.toString());
                        excptlog(e);
                    }
                }
        );
    }

    /**
     * import new photos.
     */
    public void actionImportNew() {

        try {

            // open ligthroom catalog New
            Context.getLrcat_new().disconnect();
            Process process = Runtime.getRuntime().exec("cmd /c  " +"\"" + Context.getLrcatSource().get("New").toString() + "\"");

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            Context.getLrcat_new().connect(Context.getLrcatSource().get("New").toString());
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                logecrireuserlogInfo("Success! = open : " + Context.getLrcatSource().get("New").toString());
                logecrireuserlogInfo("Output : " + output);
            } else {
                logecrireuserlogInfo("Erreur = " + exitVal  + " | " + Context.getLrcatSource().get("New").toString());
            }

        } catch (Exception e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    /**
     * Move new to grp photos.
     */

    public void actionRangerNew() {

        try {

            moveAllNewEleToRacineNew();

            //Regroupement
            ResultSet rsele = Context.getLrcat_new().sqlgetListelementnewaclasser(Context.getTempsAdherence(),Context.getLrcat_new().retrieverootfolderfromname(Context.getRepNew()));
            List<Ele> listEleBazar = new ArrayList();
            List<Ele> listEletmp = new ArrayList();
            List<Ele> listElekidz = new ArrayList();
            List<String> listkidsModel = Context.getKidsModelList();
            long maxprev = 0;
            while (rsele.next()) {

                // Recuperer les info de l'elements
                String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);
                String lcIdxFilename = rsele.getString(Context.LC_IDX_FILENAME);
                String absolutePath = Context.getLrcat_new().getabsolutePath(rsele.getString("rootFolder"));
                String cameraModel = rsele.getString("CameraModel");
                long mint = rsele.getLong("mint");
                long maxt = rsele.getLong("maxt");

                if (listkidsModel.contains(cameraModel)) {
                    listElekidz.add(new Ele(lcIdxFilename, pathFromRoot, absolutePath));
                } else {
                    if (mint > maxprev) {

                        if (listEletmp.size() > Context.getThresholdBazar()) {

                            regrouper(Context.getLrcat_new(), listEletmp);

                        } else {
                            listEleBazar.addAll(listEletmp);
                        }

                        listEletmp = new ArrayList();

                    }
                    maxprev = maxt;
                    listEletmp.add(new Ele(lcIdxFilename, pathFromRoot, absolutePath));
                }

            }

            regrouper(Context.getLrcat_new(), listEletmp);

            regrouper(Context.getLrcat_kidz(), listElekidz, Context.getKidz());
            regrouper(Context.getLrcat_new(), listEleBazar, Context.getRepBazar());

            actionDeleteEmptyDirectoryRepertoireNew();

        } catch (SQLException | IOException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    private void moveAllNewEleToRacineNew() throws SQLException, IOException {
        //mise a plat du repertoire @new
        ResultSet rseleAplat = Context.getLrcat_new().sqlgetListelementnewaclasser(Context.getTempsAdherence(), Context.getLrcat_new().retrieverootfolderfromname(Context.getRepNew()));
        while (rseleAplat.next()) {
            if (rseleAplat.getString(Context.PATH_FROM_ROOT).compareTo("") != 0) {
                String absolutePath = Context.getLrcat_new().getabsolutePath(rseleAplat.getString("rootFolder"));

                String source = normalizePath(absolutePath + rseleAplat.getString(Context.PATH_FROM_ROOT) + rseleAplat.getString(Context.LC_IDX_FILENAME));
                String uniqueID = UUID.randomUUID().toString();
                String rename = ("$" + uniqueID + "$" + supprimerbalisedollar(rseleAplat.getString(Context.LC_IDX_FILENAME))).toLowerCase();
                String destination = normalizePath(absolutePath + File.separator + rename);
                Context.getLrcat_new().sqlmovefile(source, destination);
            }
        }
    }

    private void regrouper(CatLrcat dblrdest, List<Ele> listEle, String repertoiredest) throws IOException, SQLException {
        String destdirectoryName = normalizePath(dblrdest.getabsolutePathfromname(Context.getRepNew()) + "$" + repertoiredest + "$");

        dblrdest.sqlMkdirRepertory(destdirectoryName);

        for (Ele ele : listEle) {
            String source = normalizePath(ele.getAbsolutePath() + ele.getPathFromRoot() + ele.getLcIdxFilename());
            String uniqueID = UUID.randomUUID().toString();
            String rename = ("$" + uniqueID + "$" + supprimerbalisedollar(ele.getLcIdxFilename())).toLowerCase();
            String destination = normalizePath(destdirectoryName + File.separator + rename);
            Context.getLrcat_new().sqlmovefile(source, destination);
        }
    }

    private void regrouper(CatLrcat dblrdest, List<Ele> listEle) throws IOException, SQLException {
        String uniqueID = UUID.randomUUID().toString();
        regrouper(dblrdest, listEle, uniqueID);
    }

    /**
     * Move chaque photo du bazar dans un groupe.
     * #interactif
     */
    public void actionRangerlebazar() {
        try {

            LOGGER.info(() -> "actionRangerlebazar : dryRun = " + Context.getDryRun());

            java.util.List<GrpPhoto> groupDePhoto = regroupeEleRepHorsBazarbyGroup(Context.getRepBazar(), Context.getKidz());
            java.util.List<ElePhoto> elementsPhoto = getEleBazar(Context.getRepBazar());
            for (int iele = 0; iele < elementsPhoto.size(); iele++) {
                ElePhoto elePhotocurrent = elementsPhoto.get(iele);
                elePhotocurrent.getMint();
                elePhotocurrent.getMaxt();
                for (int igrp = 0; igrp < groupDePhoto.size(); igrp++) {
                    if (groupDePhoto.get(igrp).testInterval(elePhotocurrent.getMint(), elePhotocurrent.getMaxt())) {
                        elePhotocurrent.addgroupDePhotoCandidat(groupDePhoto.get(igrp));
                    }
                }
                if (Context.getDryRun()) {
                    LOGGER.log(Level.INFO, elementsPhoto.get(iele).toString());
                } else {
                    Map<String, Object> ret = showPopupWindow(elePhotocurrent);
                    if (ret != null && ret.get(PopUpController.RETOUR_CODE).toString().compareTo(PopUpController.VALSTOPRUN) == 0) {
                        throw new IllegalStateException("actionRangerlebazar->ctrlpopup:" + PopUpController.VALSTOPRUN);
                    }
                }
            }
        } catch (Exception e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    private Map<String, Object> showPopupWindow(ElePhoto elePhotocurrent) throws IOException, SQLException {

        //Preparation technique de la popup
        FXMLLoader loaderpopup = new FXMLLoader();
        loaderpopup.setLocation(Main.class.getClassLoader().getResource("popUp.fxml"));
        Parent rootpopup = loaderpopup.load();
        PopUpController controllerpopup = loaderpopup.getController();
        Scene scene = new Scene(rootpopup);
        Stage popupStage = new Stage();
        PopUpController.setPopupStage(popupStage);
        popupStage.initOwner(Context.getPrimaryStage());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setScene(scene);

        //Preparation fonctionelle de la popup
        controllerpopup.setImage(PopUpController.IMAGE_ONE, elePhotocurrent.getSrc());

        for (int i = 0; i < elePhotocurrent.getGrpPhotoCandidat().size(); i++) {
            LOGGER.info(elePhotocurrent.toString());
            GrpPhoto grpphotoenc = elePhotocurrent.getGrpPhotoCandidat().get(i);
            List<String> grpphotoele = grpphotoenc.getElesrc();
            controllerpopup.setLblinfo("" + (i + 1) + "/" + elePhotocurrent.getGrpPhotoCandidat().size() + " : " + grpphotoenc.getPathFromRootComumn());
            int nb = grpphotoele.size();
            int id1 = 0;
            int id2 = 0;
            int id3 = 0;
            int id4 = 0;

            switch (nb) {
                case 1:
                    id1 = id2 = id3 = id4 = 0;
                    break;
                case 2:
                    id1 = id2 = 0;
                    id3 = id4 = 1;
                    break;
                case 3:
                    id1 = id2 = 0;
                    id3 = 1;
                    id4 = 2;
                    break;
                case 4:
                    id1 = 0;
                    id2 = 1;
                    id3 = 2;
                    id4 = 3;
                    break;
                default:
                    int delta = (nb - 1) / 3;
                    id1 = 0;
                    id2 = id1 + delta;
                    id3 = id2 + delta;
                    id4 = id3 + delta;
                    break;
            }
            controllerpopup.setImage(PopUpController.IMAGE_2_LL, grpphotoele.get(id1));
            controllerpopup.setImage(PopUpController.IMAGE_2_LR, grpphotoele.get(id2));
            controllerpopup.setImage(PopUpController.IMAGE_2_UL, grpphotoele.get(id3));
            controllerpopup.setImage(PopUpController.IMAGE_2_UR, grpphotoele.get(id4));
            //execution popup
            popupStage.showAndWait();
            Map<String, Object> ret = controllerpopup.getResult();
            switch (ret.get(PopUpController.RETOUR_CODE).toString()) {
                case PopUpController.VALSTOPRUN:
                    return ret;
                case PopUpController.VALSELECT:
                    File source = new File(elePhotocurrent.getSrc());
                    String directoryName = grpphotoenc.getAbsolutePath() + grpphotoenc.getPathFromRootComumn();
                    String destination = directoryName + File.separator + source.toPath().getFileName();
                    Context.getLrcat_new().sqlmovefile(elePhotocurrent.getSrc(), destination);
                    return ret;
                case PopUpController.VALNEXT:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + ret.get(PopUpController.RETOUR_CODE).toString());
            }
        }


        return controllerpopup.getResult();
    }

    /**
     * Abouturl.
     */
    public void actionAbouturl() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(Context.getUrlgitwiki()));
            }
        } catch (IOException | URISyntaxException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
        logecrireuserlogInfo(Context.getUrlgitwiki());
    }

    /**
     * Delete empty directory.
     * <p>
     * suprimmer tout les repertoires vide (physique)
     *
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public void actionDeleteEmptyDirectoryRepertoireNew() throws IOException, SQLException {

        File directory = new File(Context.getLrcat_new().getabsolutePathfromname(Context.getRepNew()) + File.separator );
        ndDelTotal = 0;
        boucleSupressionRepertoire(directory);
        logecrireuserlogInfo("delete all from " + directory + " : " + String.format("%05d", ndDelTotal));

    }

    /**
     * First.
     *
     * @throws SQLException the sql exception
     */
    public void first() throws SQLException {

        initialize();
    }

    private String supprimerbalisedollar(String lcIdxFilename) {
        Pattern pattern = Pattern.compile("(\\$.*\\$)*(.*)");
        Matcher matcher = pattern.matcher(lcIdxFilename);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";

    }

}
