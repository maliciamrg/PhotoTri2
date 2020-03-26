package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.Main;
import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.FxUtilTest;
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
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
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
import static com.malicia.mrg.app.Context.repEncours;


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
    private Label lbLabelratiophotoaconcerver1;
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
    private Label nbjourrep;
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
    private Text imagedestinationcorbeilleorstar;
    @FXML
    private Label imagedestinationinformation;
    @FXML
    private Label datesub;
    @FXML
    private ComboBox<String> selectrepCat;
    @FXML
    private ComboBox<String> selectssrepformatZ2;
    @FXML
    private ComboBox<String> selectssrepformatZ3;
    @FXML
    private ComboBox<String> selectssrepformatZ4;
    @FXML
    private Circle pointeur;
    @FXML
    private Button valid;

    private AgLibrarySubFolder activeRep;


    /**
     * Instantiates a new Main frame controller.
     */
    public MainFrameController() {
        LOGGER.info("mainFrameController");
        initialize();
    }

    /**
     * Popupalert.
     *
     * @param contentText the content text
     */
    public static void popupalertConfirmeModification(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("do you confirme ?");
        alert.setContentText(contentText);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            repEncours.ValidModification();
        }
    }

    /**
     * Popupalert.
     *
     * @param contentText the content text
     * @param imageaaff   the imageaaff
     */
    public static void popupalert(String contentText, Image imageaaff) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Popup Info Image");

        alert.setContentText(contentText);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(imageaaff);


        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(false);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(imageView, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();

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

        if (files.isEmpty()) {

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
     * ActionExecModification.
     *
     * @param event the event
     */
    @FXML
    void ActionExecModification(ActionEvent event) {
        popupalertConfirmeModification("Valider les modification effectuer sur la repertoire " + activeRep.toString() + " ?");
//        try {
////
////        } catch (IOException | URISyntaxException e) {
////            popupalertException(e);
////            excptlog(e);
////        }
////        logecrireuserlogInfo(Context.getUrlgitwiki());
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
     */
    @FXML
    void actionCycleTraitementPhoto() {
        try {
            repChoose.setItems(lrcat.getlistofrepertorytoprocess());
            repChoose.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                    activeRep = repChoose.getItems().get((Integer) number2);
                    refreshcompteurRepertoire();
                    refreshcomboxRepertoire();
                    activeRep.moveActivephotoNumTo(0);
                    datesub.setText(activeRep.getDtdebHumain());
                    try {
                        refreshActivePhoto();
                        refreshvaleurphoto();
                        setimagepreview();
                    } catch (IOException | SQLException e) {
                        popupalertException(e);
                        excptlog(e);
                    }
                }
            });
        } catch (SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    private void refreshcomboxRepertoire() {
        selectrepCat.getSelectionModel().select(activeRep.getCatFolder());
    }

    /**
     * Sets .
     *
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public void setimagepreview() throws IOException, SQLException {
        LOGGER.info("setimagepreview");
        imager1.setImage(activeRep.getimagepreview(1));
        imager2.setImage(activeRep.getimagepreview(2));
        imager3.setImage(activeRep.getimagepreview(3));
        imager4.setImage(activeRep.getimagepreview(4));
    }

    private void refreshcompteurRepertoire() {
        activeRep.refreshCompteur();
        nbeleRep.setText(activeRep.getNbelerep());
        nbphotoRep.setText(activeRep.getNbphotoRep());
        nbjourrep.setText(activeRep.getNbjourfolder());
        ratiophotoaconcerver.setText(activeRep.getRatiophotoaconserver());
        nbphotoapurger.setText(activeRep.getNbphotoapurger());
        statusRep.setText(activeRep.getStatusRep());
        nbetrationzeroetoile.setText(activeRep.nbetratiovaleur(0));
        nbetrationuneetoile.setText(activeRep.nbetratiovaleur(1));
        nbetrationdeuxetoile.setText(activeRep.nbetratiovaleur(2));
        nbetrationtroisetoile.setText(activeRep.nbetratiovaleur(3));
        nbetrationquatreetoile.setText(activeRep.nbetratiovaleur(4));
        nbetrationcinqetoile.setText(activeRep.nbetratiovaleur(5));
        valid.setDisable(activeRep.getStatusRep() != AgLibrarySubFolder.OK);
    }

    private void refreshActivePhoto() throws IOException, SQLException {
        LOGGER.info("refresh");
        imageM2.setImage(activeRep.getimagenumero(activeRep.getActivephotoNum(-2)));
        imageM2.setRotate(activeRep.getRotateFromphotonum(activeRep.getActivephotoNum(-2)));

        imageM1.setImage(activeRep.getimagenumero(activeRep.getActivephotoNum(-1)));
        imageM1.setRotate(activeRep.getRotateFromphotonum(activeRep.getActivephotoNum(-1)));

        imageOne.setImage(activeRep.getimagenumero(activeRep.getActivephotoNum(0)));
        imageOne.setRotate(activeRep.getRotateFromActivephotonum());

        imageP1.setImage(activeRep.getimagenumero(activeRep.getActivephotoNum(1)));
        imageP1.setRotate(activeRep.getRotateFromphotonum(activeRep.getActivephotoNum(1)));

        imageP2.setImage(activeRep.getimagenumero(activeRep.getActivephotoNum(2)));
        imageP2.setRotate(activeRep.getRotateFromphotonum(activeRep.getActivephotoNum(2)));
    }

    private void refreshvaleurphoto() {
        imagedestinationcorbeilleorstar.setText(activeRep.getActivephotoValeur());
        imagedestinationcorbeilleorstar.setFont(new Font("Wingdings", 30));
        imagedestinationcorbeilleorstar.setSmooth(true);
        imagedestinationcorbeilleorstar.setFontSmoothingType(FontSmoothingType.LCD);
        imagedestinationinformation.setText(activeRep.getactivephotovaleurlibelle());
    }

    /**
     * Action active photo.
     *
     * @param keyEvent the key event
     */
    @FXML
    public void actionActivePhoto(KeyEvent keyEvent) {
        try {
            switch (keyEvent.getCode()) {
                case RIGHT:
                    activeRep.moveActivephotoNumTo(+1);
                    refreshActivePhoto();
                    refreshvaleurphoto();
                    keyEvent.consume();
                    break;
                case LEFT:
                    activeRep.moveActivephotoNumTo(-1);
                    refreshActivePhoto();
                    refreshvaleurphoto();
                    keyEvent.consume();
                    break;
                case UP:
                    activeRep.valeuractivephotoincrease();
                    refreshcompteurRepertoire();
                    refreshvaleurphoto();
                    keyEvent.consume();
                    break;
                case DOWN:
                    activeRep.valeuractivephotodecrease();
                    refreshcompteurRepertoire();
                    refreshvaleurphoto();
                    keyEvent.consume();
                    break;
                case Q:
                    activeRep.setRotateActivephotoNumTo(+90);
                    imageOne.setRotate(activeRep.getRotateFromActivephotonum());
                    keyEvent.consume();
                    break;
                case D:
                    activeRep.setRotateActivephotoNumTo(-90);
                    imageOne.setRotate(activeRep.getRotateFromActivephotonum());
                    keyEvent.consume();
                    break;
                default:
                    break;
            }
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    /**
     * Action active photop 2.
     */
    @FXML
    public void actionActivePhotop2() {
        try {
            activeRep.moveActivephotoNumTo(+2);
            refreshActivePhoto();
            refreshvaleurphoto();
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    /**
     * Action active photom 2.
     */
    @FXML
    public void actionActivePhotom2() {
        try {
            activeRep.moveActivephotoNumTo(-2);
            refreshActivePhoto();
            refreshvaleurphoto();
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    /**
     * Action active photom 1.
     */
    @FXML
    public void actionActivePhotom1() {
        try {
            activeRep.moveActivephotoNumTo(-1);
            refreshActivePhoto();
            refreshvaleurphoto();
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    /**
     * Action active photop 1.
     */
    @FXML
    public void actionActivePhotop1() {
        try {
            activeRep.moveActivephotoNumTo(+1);
            refreshActivePhoto();
            refreshvaleurphoto();
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }

    }




    /**
     * Start.
     */
    public void start() {

        Context.getPrimaryStage().getScene().focusOwnerProperty().addListener(
                (prop, oldNode, newNode) -> placeMarker(newNode));

        selectrepCat.setItems(lrcat.getlistofpossiblecat());
        selectssrepformatZ2.setItems(lrcat.getlistofpossibleevent());
        selectssrepformatZ3.setItems(lrcat.getlistofpossiblelieux());
        selectssrepformatZ4.setItems(lrcat.getlistofpossibleperson());

//        FxUtilTest.autoCompleteComboBoxPlus(selectevents, (typedText, itemToCompare) -> itemToCompare.getName().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.getAge().toString().equals(typedText));
        FxUtilTest.autoCompleteComboBoxPlus(selectssrepformatZ2, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));
        FxUtilTest.autoCompleteComboBoxPlus(selectssrepformatZ3, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));
        FxUtilTest.autoCompleteComboBoxPlus(selectssrepformatZ4, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));

        actionCycleTraitementPhoto();
    }

    public void placeMarker(Node newNode) {
        double nodeMinX = newNode.getLayoutBounds().getMinX();
        double nodeMinY = newNode.getLayoutBounds().getMinY();
        Point2D nodeInScene = newNode.localToScene(nodeMinX, nodeMinY);
        Point2D nodeInMarkerLocal = pointeur.sceneToLocal(nodeInScene);
        Point2D nodeInMarkerParent = pointeur.localToParent(nodeInMarkerLocal);

        pointeur.relocate(nodeInMarkerParent.getX()
                + pointeur.getLayoutBounds().getMinX(), nodeInMarkerParent.getY()
                + pointeur.getLayoutBounds().getMinY());
    }

    public void actionrepCatChange(ActionEvent actionEvent) {
        activeRep.setCatFolder(((ComboBox)actionEvent.getTarget()).getValue().toString());
        refreshcompteurRepertoire();
    }

    public void actionssrepformatZ1Change(ActionEvent actionEvent) {
        activeRep.setrepformatZ(1,((ComboBox)actionEvent.getTarget()).getValue().toString());
    }

    public void actionssrepformatZ2Change(ActionEvent actionEvent) {
        activeRep.setrepformatZ(2,((ComboBox)actionEvent.getTarget()).getValue().toString());
    }

    public void actionssrepformatZ3Change(ActionEvent actionEvent) {
        activeRep.setrepformatZ(3,((ComboBox)actionEvent.getTarget()).getValue().toString());
    }

    public void actionssrepformatZ4Change(ActionEvent actionEvent) {
        activeRep.setrepformatZ(4,((ComboBox)actionEvent.getTarget()).getValue().toString());
    }
}
