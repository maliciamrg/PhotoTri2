package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.util.ComboboxPlus;
import com.malicia.mrg.mvc.models.AgLibraryFile;
import com.malicia.mrg.mvc.models.AgLibraryRootFolder;
import com.malicia.mrg.mvc.models.AgLibrarySubFolder;
import com.malicia.mrg.mvc.models.SystemFiles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.apache.tools.ant.DirectoryScanner;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
    private Label lbselectrepCat;
    @FXML
    private Label lbselectssrepformatZ1;
    @FXML
    private Label lbselectssrepformatZ2;
    @FXML
    private Label lbselectssrepformatZ3;
    @FXML
    private Label lbselectssrepformatZ4;
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
    private ChoiceBox<AgLibraryRootFolder> selectrepCat;
    @FXML
    private ComboBox<String> selectssrepformatZ1;
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
    private AgLibrarySubFolder activeRepSrc;


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
     * @return
     */
    public static Optional<ButtonType> popupalertConfirmeModification(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("do you confirme ?");
        alert.setContentText(contentText);

        return alert.showAndWait();
    }

    /**
     * Initialize.
     */
    private void initialize() {
        LOGGER.info("initialize");

        if (Context.getPrimaryStage() != null) {
            Context.getPrimaryStage().sizeToScene();
        }
        assert Context.getPrimaryStage() != null;
        Context.getPrimaryStage().setTitle(lrcat.getname());

    }

    /**
     * Action makeadulpicatelrcatwithdate.
     */
    @FXML
    void actionMakeadulpicatelrcatwithdate() {
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
    @FXML
    void actionRestaureLastDuplicate() {

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

            String theone = showChoiceOneWindow(files);
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

    public static void excptlog(Exception theException) {
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
    @FXML
    void actionDeleteRepertoireLogique() {
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

    public static void popupalertException(Exception ex) {
        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        String contentText = ex.toString();

        popupalert(contentText, exceptionText);

    }

    private static void popupalert(String contentText, String exceptionText) {
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
    @FXML
    void actionRangerRejet() {

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
    @FXML
    void actionImportNew() {

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
    @FXML
    void actionRangerNew() {

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
     */
    @FXML
    void actionRangerlebazar() {
        Optional<ButtonType> result = popupalertConfirmeModification("actionRangerlebazar " + activeRep.toString() + " ?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            popupalertConfirmeModification("actionRangerlebazar " + activeRep.toString() + " ?");
        }
    }

    private String showChoiceOneWindow(List<String> listeChoice) {

        ChoiceDialog<String> dialog = new ChoiceDialog<>(listeChoice.get(listeChoice.size() - 1), listeChoice);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Quel catalog restaurer ?");
        dialog.setContentText("catalog");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");

    }

    /**
     * ActionExecModification.
     */
    @FXML
    void actionExecModification() {
        try {
            Optional<ButtonType> result = popupalertConfirmeModification("Valider les modification effectuer sur la repertoire " + activeRep.toString() + " ?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                activeRepSrc.execmodification(activeRep);
            }
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    /**
     * Abouturl.
     */
    @FXML
    void actionAbouturl() {
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
     */
    @FXML
    void actionDeleteEmptyDirectoryPhysique() {
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
     */
    @FXML
    void actionopenligthroom() {
        try {
            lrcat.openLigthroomLrcatandWait();
            initialize();
        } catch (Exception e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    /**
     * Action spyfirst.
     */
    @FXML
    void actionSpyfirst() {
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
            ObservableList<AgLibrarySubFolder> getlistofrepertorytoprocess = lrcat.getlistofrepertorytoprocess(Arrays.asList(AgLibraryRootFolder.TYPE_NEW, AgLibraryRootFolder.TYPE_ENC, AgLibraryRootFolder.TYPE_CAT));
            ObservableList<AgLibrarySubFolder> getlistofrepertorytoprocessfiltred = FXCollections.observableArrayList();
            getlistofrepertorytoprocess.forEach(subFolder -> {
                if (subFolder.getNbphotoRep() != 0 && !subFolder.getStatusRep().equals(AgLibrarySubFolder.OK)) {
                    getlistofrepertorytoprocessfiltred.add(subFolder);
                }
            });
            repChoose.setItems(getlistofrepertorytoprocessfiltred);
        } catch (SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }

    private void refreshcomboxRepertoire() {

        selectrepCat.getSelectionModel().select(activeRep.getCatFolder());

        selectssrepformatZ1.setItems(activeRep.personalizelist(lrcat.listeZ.get(1)));
        selectssrepformatZ2.setItems(activeRep.personalizelist(lrcat.listeZ.get(2)));
        selectssrepformatZ3.setItems(activeRep.personalizelist(lrcat.listeZ.get(3)));
        selectssrepformatZ4.setItems(activeRep.personalizelist(lrcat.listeZ.get(4)));

        selectssrepformatZ1.setValue(activeRep.getRepformatZ(1));
        selectssrepformatZ2.setValue(activeRep.getRepformatZ(2));
        selectssrepformatZ3.setValue(activeRep.getRepformatZ(3));
        selectssrepformatZ4.setValue(activeRep.getRepformatZ(4));
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

    private void refreshcompteurRepertoire()  {
        activeRep.refreshCompteur();
        nbeleRep.setText(activeRep.getNbelerep());
        nbphotoRep.setText(activeRep.getNbphotoRepHuman());
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
        valid.setDisable(!activeRep.getStatusRep().equals(AgLibrarySubFolder.OK));
    }

    private void refreshActivePhoto() throws IOException {
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
        } catch (IOException e) {
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
        } catch (IOException e) {
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
        } catch (IOException  e) {
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
        } catch (IOException e) {
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
        } catch (IOException e) {
            popupalertException(e);
            excptlog(e);
        }

    }


    /**
     * Start.
     *
     * @throws SQLException the sql exception
     */
    public void start() throws SQLException {

        Context.getPrimaryStage().getScene().focusOwnerProperty().addListener(
                (prop, oldNode, newNode) -> placeMarker(newNode));

        lbselectrepCat.setText("Categories");
        ObservableList<AgLibraryRootFolder> listRootfolder = FXCollections.observableArrayList();
        for (Map.Entry<String, AgLibraryRootFolder> entry : lrcat.rep.entrySet()) {
            AgLibraryRootFolder rootFolder = entry.getValue();
            if (rootFolder.isCat()){
                listRootfolder.add(rootFolder);
            }
        }
        selectrepCat.setItems(listRootfolder);

        lrcat.setListeZ(1);
        lrcat.setListeZ(2);
        lrcat.setListeZ(3);
        lrcat.setListeZ(4);

        lbselectssrepformatZ1.setText(Context.formatZ.get(1));
        selectssrepformatZ1.setItems(lrcat.listeZ.get(1));
        lbselectssrepformatZ2.setText(Context.formatZ.get(2));
        selectssrepformatZ2.setItems(lrcat.listeZ.get(2));
        lbselectssrepformatZ3.setText(Context.formatZ.get(3));
        selectssrepformatZ3.setItems(lrcat.listeZ.get(3));
        lbselectssrepformatZ4.setText(Context.formatZ.get(4));
        selectssrepformatZ4.setItems(lrcat.listeZ.get(4));


        ComboboxPlus.autoCompleteComboBoxPlus(selectssrepformatZ1, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText), selectssrepformatZ1.getItems().size() > 1);
        ComboboxPlus.autoCompleteComboBoxPlus(selectssrepformatZ2, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText), selectssrepformatZ2.getItems().size() > 1);
        ComboboxPlus.autoCompleteComboBoxPlus(selectssrepformatZ3, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText), selectssrepformatZ3.getItems().size() > 1);
        ComboboxPlus.autoCompleteComboBoxPlus(selectssrepformatZ4, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText), selectssrepformatZ4.getItems().size() > 1);


        actionCycleTraitementPhoto();
    }

    /**
     * Place marker.
     *
     * @param newNode the new node
     */
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

    /**
     * Actionrep cat change.
     *
     * @param actionEvent the action event
     */
    public void actionrepCatChange(ActionEvent actionEvent) {
        activeRep.agLibraryRootFolder = (AgLibraryRootFolder) (((ComboBox) actionEvent.getTarget()).getValue());
        refreshcompteurRepertoire();
    }

    /**
     * Actionssrepformat z 1 change.
     *
     * @param actionEvent the action event
     */
    public void actionssrepformatZ1Change(ActionEvent actionEvent) {
        activeRep.setrepformatZ(1, ((ComboBox) actionEvent.getTarget()).getValue().toString());
        refreshcompteurRepertoire();
    }

    /**
     * Actionssrepformat z 2 change.
     *
     * @param actionEvent the action event
     */
    public void actionssrepformatZ2Change(ActionEvent actionEvent) {
        activeRep.setrepformatZ(2, ((ComboBox) actionEvent.getTarget()).getValue().toString());

        refreshcompteurRepertoire();
    }

    /**
     * Actionssrepformat z 3 change.
     *
     * @param actionEvent the action event
     */
    public void actionssrepformatZ3Change(ActionEvent actionEvent) {
        activeRep.setrepformatZ(3, ((ComboBox) actionEvent.getTarget()).getValue().toString());
        refreshcompteurRepertoire();

    }

    /**
     * Actionssrepformat z 4 change.
     *
     * @param actionEvent the action event
     */
    public void actionssrepformatZ4Change(ActionEvent actionEvent) {
        activeRep.setrepformatZ(4, ((ComboBox) actionEvent.getTarget()).getValue().toString());
        refreshcompteurRepertoire();

    }

    public void actionChoose(ActionEvent actionEvent) {
        try {
            activeRep = ((AgLibrarySubFolder) ((ChoiceBox) actionEvent.getTarget()).getValue());
            activeRepSrc = new AgLibrarySubFolder(activeRep);
            refreshcompteurRepertoire();
            refreshcomboxRepertoire();
            activeRep.moveActivephotoNumTo(0);
            datesub.setText(activeRep.getDtdebHumain());
            refreshActivePhoto();
            refreshvaleurphoto();
            setimagepreview();
        } catch (IOException | SQLException e) {
            popupalertException(e);
            excptlog(e);
        }
    }
}
