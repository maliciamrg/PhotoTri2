package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.Main;
import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.photo.ElePhoto;
import com.malicia.mrg.app.photo.GrpPhoto;
import com.malicia.mrg.mvc.models.AgLibraryFile;
import com.malicia.mrg.mvc.models.AgLibrarySubFolder;
import com.malicia.mrg.mvc.models.SystemFiles;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
    private MenuItem about;
    @FXML
    private Label lbnbeleRep1;
    @FXML
    private Label lbnbphotoRep1;
    @FXML
    private Label lbstatusRep1;
    @FXML
    private Label lbratiophotoaconcerver1;
    @FXML
    private Label lbnbphotoapurger1;
    @FXML
    private Label lbnbetrationzeroetoile1;
    @FXML
    private Label lbnbetrationuneetoile1;
    @FXML
    private Label lbnbetrationdeuxetoile1;
    @FXML
    private Label lbnbetrationtroisetoile1;
    @FXML
    private Label lbnbetrationquatreetoile1;
    @FXML
    private Label lbnbetrationcinqetoile1;
    @FXML
    private Label nbeleRep;
    @FXML
    private Label nbphotoRep;
    @FXML
    private Label statusRep;
    @FXML
    private Label ratiophotoaconcerver;
    @FXML
    private Label nbphotoapurger;
    @FXML
    private Label nbetrationzeroetoile;
    @FXML
    private Label nbetrationuneetoile;
    @FXML
    private Label nbetrationdeuxetoile;
    @FXML
    private Label nbetrationtroisetoile;
    @FXML
    private Label nbetrationquatreetoile;
    @FXML
    private Label nbetrationcinqetoile;
    @FXML
    private ChoiceBox<AgLibrarySubFolder> repChoose;
    @FXML
    private ImageView imager1;
    @FXML
    private ImageView imager2;
    @FXML
    private ImageView imager3;
    @FXML
    private ImageView imager4;
    @FXML
    private ImageView imageM2;
    @FXML
    private ImageView imageP2;
    @FXML
    private ImageView imageM1;
    @FXML
    private ImageView imageP1;
    @FXML
    private ImageView imageOne;
    @FXML
    private Label imagedestinationcorbeilleorstar;
    @FXML
    private Label imagedestinationinformation;
    @FXML
    private ComboBox<String> selectcat;
    @FXML
    private ComboBox<String> selectevents;
    @FXML
    private ComboBox<String> selectlieux;
    @FXML
    private ComboBox<String> selectperson;

    private AgLibrarySubFolder activeRep;

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

        ResultSet rsele = lrcat.rep.get(AgLibraryFile.REP_NEW).sqleleRepBazar(Context.appParam.getString("TempsAdherence"), repBazar);

        java.util.List<ElePhoto> listElePhoto = new ArrayList();


        while (rsele.next()) {


            // Recuperer les info de l'elements
            long captureTime = rsele.getLong(Context.CAPTURE_TIME);
            long mint = rsele.getLong("mint");
            long maxt = rsele.getLong("maxt");
            String src = rsele.getString("src");
            String absPath = rsele.getString(Context.PATH_FROM_ROOT);
            String fileIdLocal = rsele.getString("fileIdLocal");


            //Constitution des groupes de photo standard
            listElePhoto.add(new ElePhoto(captureTime, mint, maxt, src, absPath, lrcat.rep.get(AgLibraryFile.REP_NEW).name + "/", fileIdLocal));


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
        ResultSet rsgrp = lrcat.rep.get(AgLibraryFile.REP_NEW).sqlGroupByPlageAdheranceHorsRepBazar(Context.appParam.getString("TempsAdherence"), repBazar, repKidz);

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
            String folderIdLocal = rsgrp.getString("id_local");


            //Constitution des groupes de photo standard
            if (!grpPhotoEnc.add("", captureTime, mint, maxt, src, absPath, pathFromRoot + "/", folderIdLocal)) {


                listGrpPhoto.add(grpPhotoEnc);


                grpPhotoEnc = new GrpPhoto();
                if (!grpPhotoEnc.add("", captureTime, mint, maxt, src, absPath, pathFromRoot + "/", folderIdLocal)) {
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
     *
     * @param event the event
     */
    @FXML
    void actionMakeadulpicatelrcatwithdate(ActionEvent event) {
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
     *
     * @param event the event
     */
    @FXML
    void actionRestaureLastDuplicate(ActionEvent event) {

        lrcat.disconnect();

        String basedir = Context.appParam.getString("RepCatlogSauve");
        String patterncherche = "save_lrcat_" + "*" + "/" + lrcat.nomFichier;

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{patterncherche});
        scanner.setBasedir(basedir);
        scanner.setCaseSensitive(false);
        scanner.scan();
        List<String> files = Arrays.asList(scanner.getIncludedFiles());

        if (files.size() != 0) {

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
                excptlog(e);
            }
        } else {
            logecrireuserlogInfo("pas de sauvegarde trouvé : " + basedir + File.separator + patterncherche);
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
     *
     * @param event the event
     */
    @FXML
    void actionDeleteRepertoireLogique(ActionEvent event) {
        try {
            int nbdeltotal = lrcat.deleteAllRepertoireLogiqueVide();
            logecrireuserlogInfo("logical delete:" + String.format("%04d", nbdeltotal));
        } catch (SQLException e) {
            popupalertException(e);
            excptlog(e);
        }

    }

    private void logecrireuserlogInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Information Dialog");
        alert.setContentText(msg);
        alert.showAndWait();

        LOGGER.info(msg);
    }

    private void popupalertException(Exception ex) {
        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        String contentText = ex.toString();

        popupalert(contentText, exceptionText);

    }

    private void popupalert(String contentText, String exceptionText) {
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
     *
     * @param event the event
     */
    @FXML
    void actionRangerRejet(ActionEvent event) {

        try {

            lrcat.rangerRejet();

        } catch (SQLException | IOException e) {
            popupalertException(e);
            excptlog(e);
        }

    }

    /**
     * import new photos.
     *
     * @param event the event
     */
    @FXML
    void actionImportNew(ActionEvent event) {

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
     *
     * @param event the event
     */
    @FXML
    void actionRangerNew(ActionEvent event) {

        try {

            lrcat.rep.get(AgLibraryFile.REP_NEW).FlatRootFolder();

            lrcat.rep.get(AgLibraryFile.REP_NEW).RegoupFileByAdherence();

            int ndDelTotal = lrcat.rep.get(AgLibraryFile.REP_NEW).DeleteEmptyDirectory();
            logecrireuserlogInfo("delete all from " + lrcat.rep.get(AgLibraryFile.REP_NEW).name + " : " + String.format("%05d", ndDelTotal));


        } catch (SQLException | IOException e) {
            popupalertException(e);
            excptlog(e);
        }
    }


    /**
     * Move chaque photo du bazar dans un groupe.
     * #interactif
     *
     * @param event the event
     */
    @FXML
    void actionRangerlebazar(ActionEvent event) {
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
            LOGGER.log(Level.INFO, () -> "" + elePhotocurrent.toString());
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
                    lrcat.rep.get(AgLibraryFile.REP_NEW).sqlmovefile(elePhotocurrent.getSrc(), destination, grpphotoenc.getFolderIdLocal(), elePhotocurrent.getFileidlocal());
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

    /**
     * Abouturl.
     *
     * @param event the event
     */
    @FXML
    void actionAbouturl(ActionEvent event) {
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


    /**
     * Action delete empty directory physique.
     *
     * @param event the event
     */
    @FXML
    void actionDeleteEmptyDirectoryPhysique(ActionEvent event) {
        try {
            int ndDelTotal = lrcat.deleteEmptyDirectory();
            logecrireuserlogInfo("delete all empty repertory : " + String.format("%05d", ndDelTotal));
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    /**
     * Actionopenligthroom.
     *
     * @param event the event
     */
    @FXML
    void actionopenligthroom(ActionEvent event) {
        try {
            lrcat.openLigthroomLrcatandWait();
            initialize();
        } catch (IOException | InterruptedException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    /**
     * Action spyfirst.
     *
     * @param event the event
     */
    @FXML
    void actionSpyfirst(ActionEvent event) {
        try {
            String retourtext = lrcat.spyfirst();
            List<String> retlist = Arrays.asList(retourtext.split("\n"));
            popupalert("spyfirst" + retlist.get(retlist.size() - 1), retourtext);
        } catch (SQLException e) {
            popupalertException(e);
            excptlog(e);
        }

    }

    /**
     * Action cycle traitement photo.
     *
     * @param event the event
     */
    @FXML
    void actionCycleTraitementPhoto(ActionEvent event) {
        repChoose.setItems(lrcat.getlistofrepertorytoprocess());
        repChoose.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                System.out.println(repChoose.getItems().get((Integer) number2));
                activeRep = repChoose.getItems().get((Integer) number2);
                imager1.setImage(activeRep.getimagepreview(1));
                imager2.setImage(activeRep.getimagepreview(2));
                imager3.setImage(activeRep.getimagepreview(3));
                imager4.setImage(activeRep.getimagepreview(4));
                selectcat.getSelectionModel().select(activeRep.getcurrentcat());
                selectevents.getSelectionModel().select(activeRep.getcurrentevents());
                selectlieux.getSelectionModel().select(activeRep.getcurrentlieux());
                selectperson.getSelectionModel().select(activeRep.getcurrentperson());
                activeRep.setactivephoto = 0;
                refreshActivePhoto();
                refreshvaleurphoto();
                refreshcompteurRepertoire();
            }
        });

        selectcat.setItems(lrcat.getlistofpossiblecat());
        selectevents.setItems(lrcat.getlistofpossibleevent());
        selectlieux.setItems(lrcat.getlistofpossiblelieux());
        selectperson.setItems(lrcat.getlistofpossibleperson());
    }

    private void refreshcompteurRepertoire() {
        nbeleRep.setText(activeRep.nbelerep());
        nbphotoRep.setText(activeRep.nbphotoRep());
        statusRep.setText(activeRep.statusRep());
        ratiophotoaconcerver.setText(activeRep.ratiophotoaconcerver());
        nbphotoapurger.setText(activeRep.nbphotoapurger());
        nbetrationzeroetoile.setText(activeRep.nbetrationzeroetoile());
        nbetrationuneetoile.setText(activeRep.nbetrationuneetoile());
        nbetrationdeuxetoile.setText(activeRep.nbetrationdeuxetoile());
        nbetrationtroisetoile.setText(activeRep.nbetrationtroisetoile());
        nbetrationquatreetoile.setText(activeRep.nbetrationquatreetoile());
        nbetrationcinqetoile.setText(activeRep.nbetrationcinqetoile());
    }

    private void refreshActivePhoto() {
        imageOne.setImage(activeRep.getimagenumero(activeRep.getactivephoto));
        imageM1.setImage(activeRep.getimagenumero(activeRep.getactivephoto - 1));
        imageM2.setImage(activeRep.getimagenumero(activeRep.getactivephoto - 2));
        imageP1.setImage(activeRep.getimagenumero(activeRep.getactivephoto + 1));
        imageP2.setImage(activeRep.getimagenumero(activeRep.getactivephoto + 2));
    }

    private void refreshvaleurphoto() {
        imagedestinationcorbeilleorstar.setText(activeRep.getactivephotovaleur());
        imagedestinationinformation.setText(activeRep.getactivephotovaleurlibelle());
    }


    public void actionActivePhoto(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case RIGHT:
                activeRep.setactivephoto(activeRep.getactivephoto + 1);
                refreshActivePhoto();
                refreshvaleurphoto();
                break;
            case LEFT:
                activeRep.setactivephoto(activeRep.getactivephoto - 1);
                refreshActivePhoto();
                refreshvaleurphoto();
                break;
            case UP:
                activeRep.valeuractivephotoincrease();
                refreshvaleurphoto();
                refreshcompteurRepertoire();
                break;
            case DOWN:
                activeRep.valeuractivephotodecrease();
                refreshvaleurphoto();
                refreshcompteurRepertoire();
                break;
            default:
                break;
        }
    }
}
