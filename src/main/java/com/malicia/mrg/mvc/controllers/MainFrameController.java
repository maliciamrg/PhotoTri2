package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.Main;
import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.photo.ElePhoto;
import com.malicia.mrg.app.photo.GrpPhoto;
import com.malicia.mrg.mvc.models.SystemFiles;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.malicia.mrg.app.Context.lrcat;


/**
 * The type Main frame controller.
 */
public class MainFrameController {

    private static final java.util.logging.Logger LOGGER;

    static {
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
    }


    @FXML
    private TextArea userlogInfo;

    /**
     * Instantiates a new Main frame controller.
     */
    public MainFrameController() {
        LOGGER.info("mainFrameController");
        initialize();
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

        ResultSet rsele =  lrcat.rep.get("repNew").sqleleRepBazar(Context.appParam.getString("TempsAdherence"), repBazar);

        java.util.List<ElePhoto> listElePhoto = new ArrayList();


        while (rsele.next()) {


            // Recuperer les info de l'elements
            long captureTime = rsele.getLong(Context.CAPTURE_TIME);
            long mint = rsele.getLong("mint");
            long maxt = rsele.getLong("maxt");
            String src = rsele.getString("src");
            String absPath = rsele.getString(Context.PATH_FROM_ROOT);
            String file_id_local = rsele.getString("file_id_local");


            //Constitution des groupes de photo standard
            listElePhoto.add(new ElePhoto(captureTime, mint, maxt, src, absPath,  lrcat.rep.get("repNew").name + "/",file_id_local));


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
        ResultSet rsgrp =lrcat.rep.get("repNew").sqlGroupByPlageAdheranceHorsRepBazar(Context.appParam.getString("TempsAdherence"), repBazar, repKidz);

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
     * Action makeadulpicatelrcatwithdate.
     */
    public void actionMakeadulpicatelrcatwithdate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String formattedDate = sdf.format(date);

        java.io.File fori = new java.io.File(lrcat.cheminfichierLrcat);
        String dupdest = Context.appParam.getString("RepCatlogSauve") + "\\save_lrcat_" + formattedDate + "\\" + fori.getName();
        java.io.File dest = new java.io.File(dupdest);
        try {
            SystemFiles.mkdir(new java.io.File(dupdest).getParent());

            if (fori.exists()) {
                Files.copy(fori.toPath(), dest.toPath());
                logecrireuserlogInfo("sauvegarde lrcat en :" + dupdest);
            }
        } catch (IOException e) {
            logecrireuserlogInfo("sauvegarde erreur :" + fori.toPath());
            excptlog(e);
        }

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
        try {
            int nbdeltotal = lrcat.deleteAllRepertoireLogiqueVide();
            logecrireuserlogInfo("logical delete:" + String.format("%04d", nbdeltotal));
        } catch (SQLException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }

    }

    private void logecrireuserlogInfo(String msg) {
        userlogInfo.appendText(msg + "\n");
        LOGGER.info(msg);
    }

    /**
     * Move new to grp photos.
     */
    public void actionRangerRejet() {

        try {

            lrcat.rangerRejet();

        } catch (SQLException | IOException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }

    }

    /**
     * import new photos.
     */
    public void actionImportNew() {

        try {

            // open ligthroom catalog New
            lrcat.disconnect();
            Process process = Runtime.getRuntime().exec("cmd /c  " + "\"" + Context.getLrcatSource().get("New").toString() + "\"");

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            lrcat.reconnect();

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                logecrireuserlogInfo("Success! = open : " + Context.getLrcatSource().get("New").toString());
                logecrireuserlogInfo("Output : " + output);
            } else {
                logecrireuserlogInfo("Erreur = " + exitVal + " | " + Context.getLrcatSource().get("New").toString());
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

            lrcat.rep.get("repNew").FlatRootFolder();

            lrcat.rep.get("repNew").RegoupFileByAdherence();

            int ndDelTotal = lrcat.rep.get("repNew").DeleteEmptyDirectory();
            logecrireuserlogInfo("delete all from " + lrcat.rep.get("repNew").name + " : " + String.format("%05d", ndDelTotal));


        } catch (SQLException | IOException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }


    /**
     * Move chaque photo du bazar dans un groupe.
     * #interactif
     */
    public void actionRangerlebazar() {
        try {

            LOGGER.info(() -> "actionRangerlebazar " );

            java.util.List<GrpPhoto> groupDePhoto = regroupeEleRepHorsBazarbyGroup(Context.appParam.getString("ssrepBazar"), Context.appParam.getString("repKidz"));
            java.util.List<ElePhoto> elementsPhoto = getEleBazar(Context.appParam.getString("ssrepBazar"));
            for (int iele = 0; iele < elementsPhoto.size(); iele++) {
                ElePhoto elePhotocurrent = elementsPhoto.get(iele);
                elePhotocurrent.getMint();
                elePhotocurrent.getMaxt();
                for (int igrp = 0; igrp < groupDePhoto.size(); igrp++) {
                    if (groupDePhoto.get(igrp).testInterval(elePhotocurrent.getMint(), elePhotocurrent.getMaxt())) {
                        elePhotocurrent.addgroupDePhotoCandidat(groupDePhoto.get(igrp));
                    }
                }

                    Map<String, Object> ret = showPopupWindow(elePhotocurrent);
                    if (ret != null && ret.get(PopUpController.RETOUR_CODE).toString().compareTo(PopUpController.VALSTOPRUN) == 0) {
                        throw new IllegalStateException("actionRangerlebazar->ctrlpopup:" + PopUpController.VALSTOPRUN);
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
                    java.io.File source = new java.io.File(elePhotocurrent.getSrc());
                    String directoryName = grpphotoenc.getAbsolutePath() + grpphotoenc.getPathFromRootComumn();
                    String destination = directoryName + java.io.File.separator + source.toPath().getFileName();
                    lrcat.rep.get("repNew").sqlmovefile(elePhotocurrent.getSrc(), destination,lrcat.rep.get("repNew").id_local,elePhotocurrent.getFileidlocal());
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


    public void actionDeleteEmptyDirectoryRepertoireNew(ActionEvent actionEvent) throws IOException, SQLException {
        int ndDelTotal = lrcat.deleteEmptyDirectory();
        logecrireuserlogInfo("delete all empty repertory : " + String.format("%05d", ndDelTotal));
    }
}
