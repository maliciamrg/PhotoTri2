package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.util.ComboboxPlus;
import com.malicia.mrg.app.util.ColorEtLibelle;
import com.malicia.mrg.mvc.models.AgLibraryFile;
import com.malicia.mrg.mvc.models.AgLibraryRootFolder;
import com.malicia.mrg.mvc.models.AgLibrarySubFolder;
import com.malicia.mrg.mvc.models.SystemFiles;
import com.malicia.mrg.view.TextAreaAppender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

import static com.malicia.mrg.app.Context.lrcat;
import static com.malicia.mrg.view.AlertMessageUtil.popupalertConfirmeModification;

import org.apache.logging.log4j.LogManager;

/**
 * The type Main frame controller.
 */
public class MainFrameController {

    private static final Logger LOGGER = LogManager.getLogger(MainFrameController.class);


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
    private Label lbnbeleRep1;
    @FXML
    private Label lbnbetrationzeroetoile1;
    @FXML
    private Label nbetrationzeroetoile;
    @FXML
    private Label lbnbphotoRep1;
    @FXML
    private Label lbnbetrationuneetoile1;
    @FXML
    private Label nbetrationuneetoile;
    @FXML
    private Label lbnbetrationdeuxetoile1;
    @FXML
    private Label nbetrationdeuxetoile;
    @FXML
    private Label lbratiophotoaconcerver1;
    @FXML
    private Label lbnbetrationtroisetoile1;
    @FXML
    private Label nbetrationtroisetoile;
    @FXML
    private Label lbnbphotoapurger1;
    @FXML
    private Label lbnbetrationquatreetoile1;
    @FXML
    private Label nbetrationquatreetoile;
    @FXML
    private Label lbstatusRep1;
    @FXML
    private Label lbnbetrationcinqetoile1;
    @FXML
    private Label nbetrationcinqetoile;
    @FXML
    private Label lbnbSelectionner;
    @FXML
    private Label nbSelectionner;
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
    private ImageView imageM4;
    @FXML
    private ImageView imageM3;
    @FXML
    private ImageView imageM2;
    @FXML
    private ImageView imageM1;
    @FXML
    private ImageView imageP1;
    @FXML
    private ImageView imageP2;
    @FXML
    private ImageView imageP3;
    @FXML
    private ImageView imageP4;
    @FXML
    private ImageView imageZ0;
    @FXML
    private Text imageM4star;
    @FXML
    private Text imageM3star;
    @FXML
    private Text imageM2star;
    @FXML
    private Text imageM1star;
    @FXML
    private Text imageZ0star;
    @FXML
    private Text imageP1star;
    @FXML
    private Text imageP2star;
    @FXML
    private Text imageP3star;
    @FXML
    private Text imageP4star;
    @FXML
    private Text imageM4flag;
    @FXML
    private Text imageM3flag;
    @FXML
    private Text imageM2flag;
    @FXML
    private Text imageM1flag;
    @FXML
    private Text imageZ0flag;
    @FXML
    private Text imageP1flag;
    @FXML
    private Text imageP2flag;
    @FXML
    private Text imageP3flag;
    @FXML
    private Text imageP4flag;
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
    @FXML
    private TextArea logtext;

    private AgLibrarySubFolder activeRep;
    private AgLibrarySubFolder activeRepSrc;
    private AgLibrarySubFolder activeRepSplit;
    private List<AgLibraryFile> eleExclu;

    private int activephotoNum;
    private int filtreNbstar;
    private boolean filtreEstPhoto;
    private boolean FiltreEstrejeter;
    private boolean FiltreEstselectionner;


    /**
     * Instantiates a new Main frame controller.
     */
    public MainFrameController() {
        LOGGER.trace("mainFrameController");
        initialize();
    }

    /**
     * Initialize.
     */
    private void initialize() {
        LOGGER.trace("initialize");

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdHHmmss");
        String formattedDate = sdf.format(date);

        java.io.File fori = new java.io.File(lrcat.cheminfichierLrcat);
        String dupdest = Context.appParam.getString("RepCatlogSauve") + "\\save_lrcat_" + formattedDate + "\\" + fori.getName();
        java.io.File dest = new java.io.File(dupdest);
        try {
            SystemFiles.mkdir(new java.io.File(dupdest).getParent());

            if (fori.exists()) {
                Files.copy(fori.toPath(), dest.toPath());
                LOGGER.info("sauvegarde lrcat en :" + dupdest);
            }
        } catch (IOException e) {
            LOGGER.info("sauvegarde erreur :" + fori.toPath());
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }

    }

    /**
     * Action makeadulpicatelrcatwithdate.
     */
    @FXML
    void actionRestaureLastDuplicate() {

        lrcat.disconnect();

        String basedir = Context.appParam.getString("RepCatlogSauve");
        String patterncherche = "save_lrcat_" + "*" + File.separator + lrcat.nomFichier;

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{patterncherche});
        scanner.setBasedir(basedir);
        scanner.setCaseSensitive(false);
        scanner.scan();
        List<String> files = Arrays.asList(scanner.getIncludedFiles());

        if (!files.isEmpty()) {

            String theone = showChoiceOneWindow(files);
            String selectfile = basedir + File.separator + theone;


            java.io.File fori = new java.io.File(selectfile);
            java.io.File fdest = new java.io.File(lrcat.cheminfichierLrcat);

            try {
                if (fori.isFile() && fori.exists()) {
                    if (fdest.isFile() && fdest.exists()) {
                        Files.delete(fdest.toPath());
                        Files.copy(fori.toPath(), fdest.toPath());
                        LOGGER.info("restaure lrcat de :" + selectfile);
                    }
                } else {
                    LOGGER.info("restaure annule pb de fichier :" + selectfile);
                }
            } catch (IOException e) {
                Context.popupalertException(e);
                Context.excptlog(e, LOGGER);
            }
        } else {
            LOGGER.info("pas de sauvegarde trouvé : " + basedir + File.separator + patterncherche);
        }

        lrcat.reconnect();

        initialize();
    }

    /**
     * Boucle delete repertoire logique.
     */
    @FXML
    void actionDeleteRepertoireLogique() {
        try {
            int nbdeltotal = lrcat.deleteAllRepertoireLogiqueVide();
            LOGGER.info("logical delete:" + String.format("%04d", nbdeltotal));
        } catch (SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }

    }

    /**
     * Move new to grp photos.
     */
    @FXML
    void actionRangerRejet() {
        LOGGER.info("action Ranger Rejet");
        try {
 //           if (false) {
                lrcat.rangerRejet();
 //           }

        } catch (SQLException | IOException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
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
                LOGGER.info("Success! = open : " + lrcat.cheminfichierLrcat);
            } else {
                LOGGER.info("Erreur = " + exitVal + " | " + lrcat.cheminfichierLrcat);
            }

        } catch (Exception e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
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
            LOGGER.info("delete all from " + lrcat.rep.get(AgLibraryFile.REP_NEW).name + " : " + String.format("%05d", ndDelTotal));


        } catch (SQLException | IOException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
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
            LOGGER.info("Bazar ranger" );
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
                activeRepSrc.getAgLibraryRootFolder().moveListEle(eleExclu);
                activeRepSrc.execmodification(activeRep,activeRepSplit);
//                repChoose.getItems().remove(activeRep);
//                repChoose.getSelectionModel().selectNext();

                int selectindex = repChoose.getSelectionModel().getSelectedIndex();
                populatereChooseChoicebox();
                if(repChoose.getItems().size()>selectindex) {
                    repChoose.getSelectionModel().select(selectindex);
                }

                LOGGER.info("Valider les modification effectuer sur la repertoire " + activeRep.toString() );
            }
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    /**
     * Abouturl.
     */
    @FXML
    void actionAbouturl() {
        LOGGER.info(Context.getUrlgitwiki());
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(Context.getUrlgitwiki()));
            }
        } catch (IOException | URISyntaxException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
//        LOGGER.info(Context.getUrlgitwiki());
    }


    /**
     * Action delete empty directory physique.
     */
    @FXML
    void actionDeleteEmptyDirectoryPhysique() {
        try {
            int ndDelTotal = lrcat.deleteEmptyDirectory();
            LOGGER.info("delete all empty repertory : " + String.format("%05d", ndDelTotal));
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    /**
     * Actionopenligthroom.
     */
    @FXML
    void actionopenligthroom() {
        try {
            LOGGER.info("open Ligthroom Lrcat and Wait");
            lrcat.openLigthroomLrcatandWait();
            initialize();
        } catch (Exception e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
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
//            popupalert("spyfirst" + retlist.get(retlist.size() - 1), retourtext);
            LOGGER.info("spyfirst" + retlist.get(retlist.size() - 1)+ " --- " + retourtext);
        } catch (SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }

    }

    /**
     * Action cycle traitement photo.
     */
    @FXML
    void actionCycleTraitementPhoto() {
        try {
            LOGGER.info("cycle traitement photo");
            populatereChooseChoicebox();
        } catch (SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    private void populatereChooseChoicebox() throws SQLException {
        repChoose.getItems().clear();
        ObservableList<AgLibrarySubFolder> getlistofrepertorytoprocess = FXCollections.observableArrayList();
        ObservableList<AgLibrarySubFolder> getlistofrepertorytoprocessfiltred = FXCollections.observableArrayList();

        getlistofrepertorytoprocess = lrcat.getlistofrepertorytoprocess(Arrays.asList(AgLibraryRootFolder.TYPE_CAT));
        getlistofrepertorytoprocess.forEach(subFolder -> {
            if (subFolder.getNbphotoRep() != 0 && !subFolder.getIsRepValide()) {
                getlistofrepertorytoprocessfiltred.add(subFolder);
            }
        });

        getlistofrepertorytoprocess = lrcat.getlistofrepertorytoprocess(Arrays.asList(AgLibraryRootFolder.TYPE_ENC));
        getlistofrepertorytoprocess.forEach(subFolder -> {
            if (subFolder.getNbphotoRep() != 0 && !subFolder.getIsRepValide()) {
                getlistofrepertorytoprocessfiltred.add(subFolder);
            }
        });

        getlistofrepertorytoprocess = lrcat.getlistofrepertorytoprocess(Arrays.asList(AgLibraryRootFolder.TYPE_NEW));
        getlistofrepertorytoprocess.forEach(subFolder -> {
            if (subFolder.getNbphotoRep() != 0 && !subFolder.getIsRepValide()) {
                getlistofrepertorytoprocessfiltred.add(subFolder);
            }
        });

        getlistofrepertorytoprocess = lrcat.getlistofrepertorytoprocess(Arrays.asList(AgLibraryRootFolder.TYPE_LEG));
        getlistofrepertorytoprocess.forEach(subFolder -> {
            if (subFolder.getNbphotoRep() != 0 && !subFolder.getIsRepValide()) {
                getlistofrepertorytoprocessfiltred.add(subFolder);
            }
        });

        repChoose.setItems(getlistofrepertorytoprocessfiltred);
        repChoose.getSelectionModel().selectFirst();
        try {
            refreshAllPhoto();
        } catch (SQLException | IOException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    private void refreshcomboxRepertoire() {

        selectrepCat.getSelectionModel().select(activeRep.getCatFolder());

        selectssrepformatZ1.setValue(activeRep.getRepformatZ(0));
        selectssrepformatZ2.setValue(activeRep.getRepformatZ(1));
        selectssrepformatZ3.setValue(activeRep.getRepformatZ(2));
        selectssrepformatZ4.setValue(activeRep.getRepformatZ(3));

        selectssrepformatZ1.setItems(activeRep.personalizelist(lrcat.listeZ.get(0)));
        selectssrepformatZ2.setItems(activeRep.personalizelist(lrcat.listeZ.get(1)));
        selectssrepformatZ3.setItems(activeRep.personalizelist(lrcat.listeZ.get(2)));
        selectssrepformatZ4.setItems(activeRep.personalizelist(lrcat.listeZ.get(3)));

    }

    private void refreshcompteurRepertoire() {
        activeRep.refreshCompteur();
        activeRep.calculStatusRep();

        nbeleRep.setText(activeRep.getNbelerep());
        nbphotoRep.setText(activeRep.getNbphotoRepHuman());
        nbjourrep.setText(activeRep.getNbjourfolder());
        ratiophotoaconcerver.setText(activeRep.getRatiophotoaconserver());

        alimetcolornbphotoapurger(nbphotoapurger, lbnbphotoapurger1);
        alimetcolornbSelectionner(nbSelectionner, lbnbSelectionner);

        alimetcolorlabelstatus(statusRep);

        alimetcolorlabeletoile(0, nbetrationzeroetoile, lbnbetrationzeroetoile1);
        alimetcolorlabeletoile(1, nbetrationuneetoile, lbnbetrationuneetoile1);
        alimetcolorlabeletoile(2, nbetrationdeuxetoile, lbnbetrationdeuxetoile1);
        alimetcolorlabeletoile(3, nbetrationtroisetoile, lbnbetrationtroisetoile1);
        alimetcolorlabeletoile(4, nbetrationquatreetoile, lbnbetrationquatreetoile1);
        alimetcolorlabeletoile(5, nbetrationcinqetoile, lbnbetrationcinqetoile1);

        colorlabelzonez(lbselectssrepformatZ1, selectssrepformatZ1);
        colorlabelzonez(lbselectssrepformatZ2, selectssrepformatZ2);
        colorlabelzonez(lbselectssrepformatZ3, selectssrepformatZ3);
        colorlabelzonez(lbselectssrepformatZ4, selectssrepformatZ4);

        valid.setDisable(!activeRep.getIsRepPosiblementValide());
    }

    private void colorlabelzonez(Label champs, ComboBox<String> selectssrepformatZ) {
        champs.setTextFill(Color.BLACK);
        champs.setStyle("-fx-font-weight: normal;");
        String tmpval = selectssrepformatZ.getValue();
        if (tmpval == null || tmpval.isEmpty() || tmpval.equals("")) {
            champs.setTextFill(Color.RED);
            champs.setStyle("-fx-font-weight: bold;");
        }
    }

    private void alimetcolorlabelstatus(Label champs) {
        champs.setTextFill(Color.GREEN);
        champs.setStyle("-fx-font-weight: normal;");
        if (!activeRep.getIsRepPosiblementValide()) {
            champs.setTextFill(Color.RED);
            champs.setStyle("-fx-font-weight: bold;");
        }
        champs.setText(activeRep.getIsRepPosiblementValideTexte());
    }

    private void alimetcolornbSelectionner(Label champs, Label champsConnex) {
        champs.setTextFill(Color.BLACK);
        champs.setStyle("-fx-font-weight: normal;");
        ColorEtLibelle ret = activeRep.getNbSelectionner();
        if (ret.getColor().compareTo("0") != 0) {
            champs.setTextFill(Color.RED);
            champs.setStyle("-fx-font-weight: bold;");
        }
        champs.setText(ret.getLibelle());
        champsConnex.setTextFill(champs.getTextFill());
        champsConnex.setStyle(champs.getStyle());
    }

    private void alimetcolornbphotoapurger(Label champs, Label champsConnex) {
        champs.setTextFill(Color.BLACK);
        champs.setStyle("-fx-font-weight: normal;");
        String[] ret = activeRep.getNbphotoapurger().split("@");
        if (ret[1].compareTo("0") != 0) {
            champs.setTextFill(Color.RED);
            champs.setStyle("-fx-font-weight: bold;");
        }
        champs.setText(ret[2]);
        champsConnex.setTextFill(champs.getTextFill());
        champsConnex.setStyle(champs.getStyle());
    }

    private void alimetcolorlabeletoile(int n, Label champs, Label champsConnex) {
        champs.setTextFill(Color.BLACK);
        champs.setStyle("-fx-font-weight: normal;");
        ColorEtLibelle valStar = activeRep.nbetratiovaleur(n);
        if (valStar.getColor().compareTo("0") != 0) {
            champs.setTextFill(Color.RED);
            champs.setStyle("-fx-font-weight: bold;");
        }
        champs.setText(valStar.getLibelle());
        champsConnex.setTextFill(champs.getTextFill());
        champsConnex.setStyle(champs.getStyle());
    }

    private void refreshAllPhoto() throws IOException, SQLException {
        LOGGER.trace("refresh");

        recalculimagev(getnumphotofromactive(-4), imageM4star, imageM4flag, imageM4);
        recalculimagev(getnumphotofromactive(-3), imageM3star, imageM3flag, imageM3);
        recalculimagev(getnumphotofromactive(-2), imageM2star, imageM2flag, imageM2);
        recalculimagev(getnumphotofromactive(-1), imageM1star, imageM1flag, imageM1);
        recalculimagev(getnumphotofromactive(0), imageZ0star, imageZ0flag, imageZ0);
        recalculimagev(getnumphotofromactive(1), imageP1star, imageP1flag, imageP1);
        recalculimagev(getnumphotofromactive(2), imageP2star, imageP2flag, imageP2);
        recalculimagev(getnumphotofromactive(3), imageP3star, imageP3flag, imageP3);
        recalculimagev(getnumphotofromactive(4), imageP4star, imageP4flag, imageP4);
        refreshcompteurRepertoire();
    }

    private void recalculimagev(int numphotofromactive, Text imagestar, Text imageflag, ImageView imageV) throws IOException, SQLException {
        displayStarValueAndLibelle(imagestar, numphotofromactive);
        displayFlagValue(imageflag, numphotofromactive);
        imageV.setImage(activeRepSrc.getimagenumero(numphotofromactive));
        imageV.setRotate(activeRep.getRotateFromphotonum(numphotofromactive));
    }


    /**
     * Move activephoto num to.
     *
     * @param deltaphoto the delta
     */
    private void moveActivephotoNumTo(int deltaphoto) {

        if (deltaphoto == 0) {
            activephotoNum = -1;
            activephotoNum = getnumphotofromactive(+1);
        } else {
            int sens = Integer.signum(deltaphoto);
            for (int i1 = 1; i1 <= Math.abs(deltaphoto); i1++) {
                int ret = getnumphotofromactive(sens);
                if (ret >= 0 && ret <= activeRep.filsize() - 1) {
                    activephotoNum = ret;
                }
            }
        }
    }

    /**
     * Gets activephoto num.
     *
     * @param deltaphoto the
     * @return the activephoto num
     */
    private int getnumphotofromactive(int deltaphoto) {

        //activephotoNum [-1....max]
        int sens = Integer.signum(deltaphoto);

        int calculnewactivephotoNum = activephotoNum;
        for (int i1 = 1; i1 <= Math.abs(deltaphoto); i1++) {
            for (int num = calculnewactivephotoNum + sens; num > -2 && num < activeRep.filsize() + 1; num += sens) {
                if (num == activeRep.filsize()) {
                    calculnewactivephotoNum = activeRep.filsize();
                    break;
                }
                if (num == -1) {
                    calculnewactivephotoNum = -1;
                    break;
                }
                if (activeRep.fileFiltrer(num, filtreEstPhoto, filtreNbstar, FiltreEstrejeter, FiltreEstselectionner)) {
                    calculnewactivephotoNum = num;
                    break;
                }
            }
        }
        if ((calculnewactivephotoNum != activephotoNum && deltaphoto != 0) || (calculnewactivephotoNum == activephotoNum && deltaphoto == 0)) {
            return calculnewactivephotoNum;
        }
        return -1;
    }


    private void displayFlagValue(Text imageflag, int activeNum) {
        imageflag.setText(activeRep.getActivephotoFlag(activeNum));
        imageflag.setFill(Color.RED);
        imageflag.setFont(new Font("Wingdings", 30));
        imageflag.setSmooth(true);
        imageflag.setFontSmoothingType(FontSmoothingType.LCD);
    }

    private void displayStarValueAndLibelle(Text imagestar, int activeNum) {
        imagestar.setText(activeRep.getActivephotoValeur(activeNum));
        imagestar.setFill(Color.RED);
        imagestar.setFont(new Font("Wingdings", 10));
        imagestar.setSmooth(true);
        imagestar.setFontSmoothingType(FontSmoothingType.LCD);
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
                    moveActivephotoNumTo(+1);
                    refreshAllPhoto();
                    keyEvent.consume();
                    break;
                case LEFT:
                    moveActivephotoNumTo(-1);
                    refreshAllPhoto();
                    keyEvent.consume();
                    break;
                case HOME:
                    moveActivephotoNumTo(-9999);
                    refreshAllPhoto();
                    keyEvent.consume();
                    break;
                case DELETE:
                    if (keyEvent.isShiftDown()) {
                        Optional<ButtonType> result = popupalertConfirmeModification("Supprimer PHYSIQUEMENT la photo ????");
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            activeRepSrc.deletePhoto(activephotoNum);
                            activeRep.listFileSubFolder.remove(activephotoNum);
                            activeRep.refreshValue(lrcat.listeZ);
                            moveActivephotoNumTo(+1);
                            moveActivephotoNumTo(-1);
                            refreshAllPhoto();
                        }
                    } else {
                        activeRep.valeuractivephotodecrease(activephotoNum);
                        activeRep.valeuractivephotodecrease(activephotoNum);
                        activeRep.valeuractivephotodecrease(activephotoNum);
                        activeRep.valeuractivephotodecrease(activephotoNum);
                        activeRep.valeuractivephotodecrease(activephotoNum);
                        refreshcompteurRepertoire();
                        displayStarValueAndLibelle(imageZ0star, activephotoNum);
                        moveActivephotoNumTo(+1);
                        refreshAllPhoto();
                    }
                    keyEvent.consume();
                    break;
                case END:
                    moveActivephotoNumTo(+9999);
                    refreshAllPhoto();
                    keyEvent.consume();
                    break;
                case UP:
                    activeRep.valeuractivephotoincrease(activephotoNum);
                    refreshcompteurRepertoire();
                    displayStarValueAndLibelle(imageZ0star, activephotoNum);
                    keyEvent.consume();
                    break;
                case S:
                    if (keyEvent.isAltDown() && keyEvent.isShiftDown()) {
                        activeRepSplit = activeRepSrc.split(activephotoNum, lrcat.listeZ);
                        activeRep.split(activephotoNum, lrcat.listeZ);
                        activeRep.refreshValue(lrcat.listeZ);
                        moveActivephotoNumTo(-1);
                        refreshAllPhoto();
                    }
                    keyEvent.consume();
                    break;
                case E:
                    if (keyEvent.isShiftDown()) {
//                        Optional<ButtonType> result = popupalertConfirmeModification("Exclure la photo ????");
//                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            eleExclu.add(activeRepSrc.listFileSubFolder.get(activephotoNum));
                            activeRepSrc.listFileSubFolder.remove(activephotoNum);
                            activeRep.listFileSubFolder.remove(activephotoNum);
                            activeRep.refreshValue(lrcat.listeZ);
                            moveActivephotoNumTo(+1);
                            moveActivephotoNumTo(-1);
                            refreshAllPhoto();
//                        }
                    }
                    keyEvent.consume();
                    break;
                case DOWN:
                    activeRep.valeuractivephotodecrease(activephotoNum);
                    refreshcompteurRepertoire();
                    displayStarValueAndLibelle(imageZ0star, activephotoNum);
                    keyEvent.consume();
                    break;
                case D:
                    activeRep.setRotateToFile(activephotoNum, +90);
                    imageZ0.setRotate(activeRep.getRotateFromphotonum(activephotoNum));
                    keyEvent.consume();
                    break;
                case Q:
                    activeRep.setRotateToFile(activephotoNum, -90);
                    imageZ0.setRotate(activeRep.getRotateFromphotonum(activephotoNum));
                    keyEvent.consume();
                    break;
                case U:
                    activeRep.valeuractivephotounflag(activephotoNum);
                    refreshcompteurRepertoire();
                    displayFlagValue(imageZ0flag, activephotoNum);
                    moveActivephotoNumTo(+1);
                    refreshAllPhoto();
                    keyEvent.consume();
                    break;
                case P:
                    activeRep.valeuractivephotoflag(activephotoNum);
                    refreshcompteurRepertoire();
                    displayFlagValue(imageZ0flag, activephotoNum);
                    moveActivephotoNumTo(+1);
                    refreshAllPhoto();
                    keyEvent.consume();
                    break;
                default:
                    break;
            }
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    /**
     * Action active photom 4.
     */
    @FXML
    public void actionActivePhotom4() {
        try {
            moveActivephotoNumTo(-4);
            refreshAllPhoto();
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    /**
     * Action active photom 3.
     */
    @FXML
    public void actionActivePhotom3() {
        try {
            moveActivephotoNumTo(-3);
            refreshAllPhoto();
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    /**
     * Action active photom 2.
     */
    @FXML
    public void actionActivePhotom2() {
        try {
            moveActivephotoNumTo(-2);
            refreshAllPhoto();
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    /**
     * Action active photom 1.
     */
    @FXML
    public void actionActivePhotom1() {
        try {
            moveActivephotoNumTo(-1);
            refreshAllPhoto();
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    /**
     * Action active photop 1.
     */
    @FXML
    public void actionActivePhotop1() {
        try {
            moveActivephotoNumTo(+1);
            refreshAllPhoto();
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }

    }

    /**
     * Action active photop 2.
     */
    @FXML
    public void actionActivePhotop2() {
        try {
            moveActivephotoNumTo(+2);
            refreshAllPhoto();
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    /**
     * Action active photop 3.
     */
    @FXML
    public void actionActivePhotop3() {
        try {
            moveActivephotoNumTo(+3);
            refreshAllPhoto();
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }

    }

    /**
     * Action active photop 4.
     */
    @FXML
    public void actionActivePhotop4() {
        try {
            moveActivephotoNumTo(+4);
            refreshAllPhoto();
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    @FXML
    public void exitApplication(ActionEvent event) throws IOException {
        LOGGER.info("stop Mainframe");
        Platform.runLater(new Runnable() {
            public void run() {
                new ProgressCmd().start(new Stage());
            }
        });
    }

    /**
     * Start.
     *
     * @throws SQLException the sql exception
     */
    public void start() throws SQLException {

        Context.getPrimaryStage().getScene().focusOwnerProperty().addListener(
                (prop, oldNode, newNode) -> placeMarker(newNode));

        TextAreaAppender.setTextArea(logtext);
        LOGGER.info("start Mainframe");

        lbselectrepCat.setText("Categories");
        ObservableList<AgLibraryRootFolder> listRootfolder = FXCollections.observableArrayList();
        for (Map.Entry<String, AgLibraryRootFolder> entry : lrcat.rep.entrySet()) {
            AgLibraryRootFolder rootFolder = entry.getValue();
            if (rootFolder.isCat()) {
                listRootfolder.add(rootFolder);
            }
        }
        selectrepCat.setItems(listRootfolder);

        lrcat.setListeZ();

        lbselectssrepformatZ1.setText(lrcat.listeZ.get(0).titreZone);
        selectssrepformatZ1.setItems(lrcat.listeZ.get(0).listeEleZone);
        lbselectssrepformatZ2.setText(lrcat.listeZ.get(1).titreZone);
        selectssrepformatZ2.setItems(lrcat.listeZ.get(1).listeEleZone);
        lbselectssrepformatZ3.setText(lrcat.listeZ.get(2).titreZone);
        selectssrepformatZ3.setItems(lrcat.listeZ.get(2).listeEleZone);
        lbselectssrepformatZ4.setText(lrcat.listeZ.get(3).titreZone);
        selectssrepformatZ4.setItems(lrcat.listeZ.get(3).listeEleZone);


        ComboboxPlus.autoCompleteComboBoxPlus(selectssrepformatZ1, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText), selectssrepformatZ1.getItems().size() > 1);
        ComboboxPlus.autoCompleteComboBoxPlus(selectssrepformatZ2, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText), selectssrepformatZ2.getItems().size() > 1);
        ComboboxPlus.autoCompleteComboBoxPlus(selectssrepformatZ3, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText), selectssrepformatZ3.getItems().size() > 1);
        ComboboxPlus.autoCompleteComboBoxPlus(selectssrepformatZ4, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText), selectssrepformatZ4.getItems().size() > 1);


        //actionCycleTraitementPhoto();
    }

    /**
     * Place marker.
     *
     * @param newNode the new node
     */
    private void placeMarker(Node newNode) {
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
        activeRep.setAgLibraryRootFolder((AgLibraryRootFolder) (((ChoiceBox) actionEvent.getTarget()).getValue()));
        LOGGER.info("Changement Cat " + (((ChoiceBox) actionEvent.getTarget()).getValue()));
        if (activeRep.getAgLibraryRootFolder() == null) {
            activeRep.setAgLibraryRootFolder(lrcat.rep.get("repNew"));
        }
        activeRep.refreshValue(lrcat.listeZ);
        refreshcomboxRepertoire();
        refreshcompteurRepertoire();
        selectssrepformatZ1.setEditable(activeRep.getAgLibraryRootFolder().IsZoneEditable[0]);
        selectssrepformatZ2.setEditable(activeRep.getAgLibraryRootFolder().IsZoneEditable[1]);
        selectssrepformatZ3.setEditable(activeRep.getAgLibraryRootFolder().IsZoneEditable[2]);
        selectssrepformatZ4.setEditable(activeRep.getAgLibraryRootFolder().IsZoneEditable[3]);
    }

    /**
     * Actionssrepformat z 1 change.
     *
     * @param actionEvent the action event
     */
    public void actionssrepformatZ1Change(ActionEvent actionEvent) {
        actionssrepformatZone(actionEvent, 0);
    }

    /**
     * Actionssrepformat z 2 change.
     *
     * @param actionEvent the action event
     */
    public void actionssrepformatZ2Change(ActionEvent actionEvent) {
        actionssrepformatZone(actionEvent, 1);
    }

    /**
     * Actionssrepformat z 3 change.
     *
     * @param actionEvent the action event
     */
    public void actionssrepformatZ3Change(ActionEvent actionEvent) {
        actionssrepformatZone(actionEvent, 2);
    }

    /**
     * Actionssrepformat z 4 change.
     *
     * @param actionEvent the action event
     */
    public void actionssrepformatZ4Change(ActionEvent actionEvent) {
        actionssrepformatZone(actionEvent, 3);
    }

    public void actionssrepformatZone(ActionEvent actionEvent, int i) {
        String val;
        if (activeRep.getAgLibraryRootFolder().IsZoneDefault[i].startsWith("@")) {
            val = activeRep.getAgLibraryRootFolder().IsZoneDefault[i];
            ((ComboBox) actionEvent.getTarget()).setValue(val);
        } else {
            val =  String.valueOf(((ComboBox) actionEvent.getTarget()).getValue());
        }
        activeRep.setrepformatZ(i,val, lrcat.listeZ);

        refreshcompteurRepertoire();
    }

    public void actionChoose(ActionEvent actionEvent) {
        try {
            activeRep = ((AgLibrarySubFolder) ((ChoiceBox) actionEvent.getTarget()).getValue());
            activeRep.refreshValue(lrcat.listeZ);
            if (activeRep != null) {
                activeRepSrc = new AgLibrarySubFolder(activeRep, lrcat.listeZ);
                activeRepSplit = activeRepSrc.split(activeRepSrc.filsize(), lrcat.listeZ);
                eleExclu= new ArrayList();
                refreshcomboxRepertoire();
                refreshcompteurRepertoire();
                actionFiltreNull();
                datesub.setText(activeRep.getDtdebHumain());
            }
            refreshAllPhoto();
            LOGGER.info( ((ChoiceBox) actionEvent.getTarget()).getValue());
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }

    public void actionFiltreNull() throws IOException, SQLException {
        filtreNbstar = -1;
        filtreEstPhoto = true;
        FiltreEstrejeter = false;
        FiltreEstselectionner = false;
        moveActivephotoNumTo(0);
        refreshAllPhoto();
        LOGGER.info("filtre null");
    }

    public void actionfiltre0() throws IOException, SQLException {
        filtreNbstar = 0;
        filtreEstPhoto = true;
        FiltreEstrejeter = false;
        FiltreEstselectionner = true;
        moveActivephotoNumTo(0);
        refreshAllPhoto();
        LOGGER.info("filtre 0");
    }

    public void actionfiltre1() throws IOException, SQLException {
        filtreNbstar = 1;
        filtreEstPhoto = true;
        FiltreEstrejeter = false;
        FiltreEstselectionner = true;
        moveActivephotoNumTo(0);
        refreshAllPhoto();
        LOGGER.info("filtre 1");
    }

    public void actionfiltre2() throws IOException, SQLException {
        filtreNbstar = 2;
        filtreEstPhoto = true;
        FiltreEstrejeter = false;
        FiltreEstselectionner = true;
        moveActivephotoNumTo(0);
        refreshAllPhoto();
        LOGGER.info("filtre 2");
    }

    public void actionfiltre3() throws IOException, SQLException {
        filtreNbstar = 3;
        filtreEstPhoto = true;
        FiltreEstrejeter = false;
        FiltreEstselectionner = true;
        moveActivephotoNumTo(0);
        refreshAllPhoto();
        LOGGER.info("filtre 3");
    }

    public void actionfiltre4() throws IOException, SQLException {
        filtreNbstar = 4;
        filtreEstPhoto = true;
        FiltreEstrejeter = false;
        FiltreEstselectionner = true;
        moveActivephotoNumTo(0);
        refreshAllPhoto();
        LOGGER.info("filtre 4" );
    }

    public void actionfiltre5() throws IOException, SQLException {
        filtreNbstar = 5;
        filtreEstPhoto = true;
        FiltreEstrejeter = false;
        FiltreEstselectionner = true;
        moveActivephotoNumTo(0);
        refreshAllPhoto();
        LOGGER.info("filtre 5");
    }

    public void actionfiltrep() throws IOException, SQLException {
        filtreNbstar = -1;
        filtreEstPhoto = true;
        FiltreEstrejeter = true;
        FiltreEstselectionner = true;
        moveActivephotoNumTo(0);
        refreshAllPhoto();
        LOGGER.info("filtre repertoire");
    }

    public void actionplayElement(ContextMenuEvent contextMenuEvent) {
        Desktop desktop = Desktop.getDesktop();
        try {
            LOGGER.info("play " + activeRepSrc.listFileSubFolder.get(getnumphotofromactive(0)).getPath());
            desktop.open(new File(activeRepSrc.listFileSubFolder.get(getnumphotofromactive(0)).getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionSuppressionDuplicate(ActionEvent actionEvent) {

        String basedir = Context.appParam.getString("RepCatlogSauve");
        String patterncherche = "save_lrcat_" + "*" + File.separator + lrcat.nomFichier;

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{patterncherche});
        scanner.setBasedir(basedir);
        scanner.setCaseSensitive(false);
        scanner.scan();
        List<String> files = Arrays.asList(scanner.getIncludedFiles());

        if (!files.isEmpty()) {

            String theone = showChoiceOneWindow(files);
            String selectfile = basedir + File.separator + theone;


            java.io.File fori = new java.io.File(selectfile);

            try {
                if (fori.isFile() && fori.exists()) {
                    Files.delete(fori.toPath());
                    LOGGER.info("delete lrcat de :" + selectfile);
                } else {
                    LOGGER.info("delete annule pb de fichier :" + selectfile);
                }
            } catch (IOException e) {
                Context.popupalertException(e);
                Context.excptlog(e, LOGGER);
            }
        } else {
            LOGGER.info("pas de sauvegarde trouvé : " + basedir + File.separator + patterncherche);
        }


    }

    public void actionSelectionner(MouseEvent mouseEvent) {
        LOGGER.info("action selectionner");
        try {
            if (nbSelectionner.toString() == " 0000") {
                Optional<ButtonType> result = popupalertConfirmeModification("Re-selectionner toute les photos (enlever les flags X des toutes les photos) ?");
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    activeRep.flagAllFile();
                    refreshAllPhoto();
                    refreshcompteurRepertoire();
                }
            } else {
                FiltreEstselectionner = !FiltreEstselectionner;
                moveActivephotoNumTo(0);
                refreshAllPhoto();
            }
        } catch (IOException | SQLException e) {
            Context.popupalertException(e);
            Context.excptlog(e, LOGGER);
        }
    }
}
