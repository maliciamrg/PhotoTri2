package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.Main;
import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.photo.ElePhoto;
import com.malicia.mrg.app.photo.GrpPhoto;
import com.malicia.mrg.mvc.models.SystemFiles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.tools.ant.DirectoryScanner;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

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
    private java.util.List<ElePhoto> getEleBazar(String repBazar) throws SQLException {

//            constitution des groupes

        ResultSet rsele = lrcat.rep.get("repNew").sqleleRepBazar(Context.appParam.getString("TempsAdherence"), repBazar);

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
            listElePhoto.add(new ElePhoto(captureTime, mint, maxt, src, absPath, lrcat.rep.get("repNew").name + "/", file_id_local));


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
    private java.util.List<GrpPhoto> regroupeEleRepHorsBazarbyGroup(String repBazar, String repKidz) throws SQLException {

//            constitution des groupes
//        grpphotoenc.getAbsolutePath() + grpphotoenc.getPathFromRootComumn() + grpphotoenc.getNomRepetrtoire()
        ResultSet rsgrp = lrcat.rep.get("repNew").sqlGroupByPlageAdheranceHorsRepBazar(Context.appParam.getString("TempsAdherence"), repBazar, repKidz);

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
            String folder_id_local = rsgrp.getString("id_local");


            //Constitution des groupes de photo standard
            if (!grpPhotoEnc.add("", captureTime, mint, maxt, src, absPath, pathFromRoot + "/", folder_id_local)) {


                listGrpPhoto.add(grpPhotoEnc);


                grpPhotoEnc = new GrpPhoto();
                if (!grpPhotoEnc.add("", captureTime, mint, maxt, src, absPath, pathFromRoot + "/", folder_id_local)) {
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
        Context.getPrimaryStage().setTitle(lrcat.getname());
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
            popupalertException(e);
            excptlog(e);
        }

    }

    /**
     * Action makeadulpicatelrcatwithdate.
     */
    public void actionRestaureLastDuplicate() {

        lrcat.disconnect();

        String basedir = Context.appParam.getString("RepCatlogSauve");

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"save_lrcat_" + "*" + "/" + lrcat.nomFichier});
        scanner.setBasedir(basedir);
        scanner.setCaseSensitive(false);
        scanner.scan();
        List<String> files = Arrays.asList(scanner.getIncludedFiles());

        String theone = showChoiceOneWindow("Quel catalog restaurer ?", "catalog", files);
        String selectfile = basedir + File.separator + theone;


        java.io.File fori = new java.io.File(selectfile);
        java.io.File fdest = new java.io.File(lrcat.cheminfichierLrcat);

        try {
            if (fori.isFile() && fori.exists()) {
                if (fdest.isFile() && fdest.exists()) {
                    Files.delete(fdest.toPath());
                    Files.copy(fori.toPath(), fdest.toPath());
                    logecrireuserlogInfo("restaure lrcat de :" + selectfile);
                }
            } else {
                logecrireuserlogInfo("restaure annule pb de fichier :" + selectfile);
            }
        } catch (IOException e) {
            popupalertException(e);
            e.printStackTrace();
        }

        lrcat.reconnect();

        initialize();
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
            popupalertException(e);
            excptlog(e);
        }

    }

    private void logecrireuserlogInfo(String msg) {
        userlogInfo.appendText(msg + "\n");
        LOGGER.info(msg);
    }

    private void popupalertException(Exception ex) {
        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        String contentText = ex.toString();

        popupalert(contentText,exceptionText);

    }

    private void popupalert( String contentText,String exceptionText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception Dialog");
        alert.setContentText(contentText);

        javafx.scene.control.Label label = new javafx.scene.control.Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }


    /**
     * Move new to grp photos.
     */
    public void actionRangerRejet() {

        try {

            lrcat.rangerRejet();

        } catch (SQLException | IOException e) {
            popupalertException(e);
            excptlog(e);
        }

    }

    /**
     * import new photos.
     */
    public void actionImportNew() {

        try {

            int exitVal = lrcat.openLigthroomLrcatandWait();

            if (exitVal == 0) {
                logecrireuserlogInfo("Success! = open : " + lrcat.cheminfichierLrcat);
            } else {
                logecrireuserlogInfo("Erreur = " + exitVal + " | " + lrcat.cheminfichierLrcat);
            }

        } catch (Exception e) {
            popupalertException(e);
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
            popupalertException(e);
            excptlog(e);
        }
    }


    /**
     * Move chaque photo du bazar dans un groupe.
     * #interactif
     */
    public void actionRangerlebazar() {
        try {

            LOGGER.info(() -> "actionRangerlebazar ");

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
            popupalertException(e);
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
                    lrcat.rep.get("repNew").sqlmovefile(elePhotocurrent.getSrc(), destination, grpphotoenc.getFolder_id_local(), elePhotocurrent.getFileidlocal());
                    return ret;
                case PopUpController.VALNEXT:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + ret.get(PopUpController.RETOUR_CODE).toString());
            }
        }


        return controllerpopup.getResult();
    }


    private String showChoiceOneWindow(String quelchoice, String nomelement, List<String> listeChoice) {

        ChoiceDialog<String> dialog = new ChoiceDialog<>(listeChoice.get(listeChoice.size() - 1), listeChoice);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText(quelchoice);
        dialog.setContentText(nomelement);

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }

        return "";
    }

    private String showChoiceOneWindow(List<String> listeChoice, boolean test) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("listeChoice Dialog");
        alert.setHeaderText("listeChoice Dialog");
        alert.setContentText("Please choice one");


        ListView listview = new ListView<String>();
        listview.getItems().addAll(listeChoice);
        listview.setEditable(false);

        listview.setMaxWidth(Double.MAX_VALUE);
        listview.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(listview, Priority.ALWAYS);
        GridPane.setHgrow(listview, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(listview, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();

        return "";
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
            popupalertException(e);
            excptlog(e);
        }
        logecrireuserlogInfo(Context.getUrlgitwiki());
    }


    public void actionDeleteEmptyDirectoryPhysique() {
        try {
            int ndDelTotal = lrcat.deleteEmptyDirectory();
            logecrireuserlogInfo("delete all empty repertory : " + String.format("%05d", ndDelTotal));
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    public void actionopenligthroom() {
        try {
            lrcat.openLigthroomLrcatandWait();
            initialize();
        } catch (IOException | InterruptedException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    public void spyfirst() {
        try {
            String retourtext = lrcat.spyfirst();
            List<String> retlist = Arrays.asList(retourtext.split("\n"));
            popupalert("spyfirst" + retlist.get(retlist.size()-1) , retourtext);
        } catch (SQLException e) {
            popupalertException(e);
            excptlog(e);
        }

    }
}
