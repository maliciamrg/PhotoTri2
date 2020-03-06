package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.Main;
import com.malicia.mrg.app.ActionfichierRepertoire;
import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.photo.Ele;
import com.malicia.mrg.app.photo.ElePhoto;
import com.malicia.mrg.app.photo.GrpPhoto;
import com.malicia.mrg.mvc.models.RequeteSql;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private ChoiceBox rootSelected;
    @FXML
    private Label databaselrcat;
    @FXML
    private Label lbPasRepertoirePhoto;
    @FXML
    private Label lbTempsAdherence;
    @FXML
    private Label lbRepertoireNew;
    @FXML
    private CheckBox chkDryRun;
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

        ResultSet rsele = RequeteSql.sqleleRepBazar(Context.getTempsAdherence(), repBazar);

        java.util.List<ElePhoto> listElePhoto = new ArrayList();


        while (rsele.next()) {


            // Recuperer les info de l'elements
            long captureTime = rsele.getLong(Context.CAPTURE_TIME);
            long mint = rsele.getLong("mint");
            long maxt = rsele.getLong("maxt");
            String src = rsele.getString("src");
            String absPath = rsele.getString(Context.PATH_FROM_ROOT);


            //Constitution des groupes de photo standard
            listElePhoto.add(new ElePhoto(captureTime, mint, maxt, src, absPath, Context.getRepertoireNew() + "/"));


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
        ResultSet rsgrp = RequeteSql.sqlGroupByPlageAdheranceHorsRepBazar(Context.getTempsAdherence(), repBazar, repKidz);

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

        databaselrcat.setText(Context.getCatalogLrcat____New());
        lbTempsAdherence.setText("Temps d'adherence : " + Context.getTempsAdherence());
        lbRepertoireNew.setText("Pattern du Repertoire New : " + Context.getRepertoireNew());
        chkDryRun.setSelected(Context.getDryRun());

        if (Context.getPrimaryStage() != null) {
            Context.getPrimaryStage().sizeToScene();
        }

    }

    private void initalizerootselection() throws SQLException {
        ResultSet rs = RequeteSql.sqlGetAllRoot();
        rootSelected.getSelectionModel().clearSelection();
        rootSelected.getItems().clear();
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                this.absolutePath.put(name, rs.getString(Context.ABSOLUTEPATH));
                rootSelected.getItems().add(name);
            }
            rootSelected.setValue(rootSelected.getItems().get(0));
        } catch (SQLException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
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
        logecrireuserlogInfo("absolutePath.get(rootName) : " + absolutePath.get(rootName));
        Context.savePropertiesParameters(Context.getCurrentContext());
    }

    /**
     * Select fichier ligthroom.
     * <p>
     * selecttioner le fichier lrcat a traiter
     * modifier et sauvegarde dans le properties
     */
    public void actionSelectFichierLigthroom() {
        try {
            //Create a file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.setInitialDirectory((new File(new File(Context.getCatalogLrcat____New()).getParent())));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("LRCAT files (*.lrcat)", "*.lrcat");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(Context.getPrimaryStage());
            if (file != null) {
                logecrireuserlogInfo("selectedFile:" + file.getAbsolutePath());
                Context.setCatalogLrcat____New(file.getAbsolutePath());
                initalizerootselection();
            }
            Context.savePropertiesParameters(Context.getCurrentContext());
            Context.getController().initialize();
        } catch (SQLException | IOException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    /**
     * Action makeadulpicatelrcatwithdate.
     */
    public void actionMakeadulpicatelrcatwithdate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String formattedDate = sdf.format(date);

        String ori = Context.getCatalogLrcat____New();
        File fori = new File(ori);
        String dupdest = fori.getParent() + "\\" + formattedDate + "_" + fori.getName();
        File dest = new File(dupdest);
        try {
            Files.copy(fori.toPath(), dest.toPath());
            logecrireuserlogInfo("sauvegarde lrcat en :" + dupdest);
        } catch (IOException e) {
            logecrireuserlogInfo("sauvegarde erreur");
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
     * Change dry run.
     */
    public void actionChangeDryRun() {
        Context.setDryRun(!Context.getDryRun());
        Context.getController().initialize();
        logecrireuserlogInfo("ChangeDryRun to " + Context.getDryRun());
    }

    /**
     * Boucle delete repertoire logique.
     */
    public void actionDeleteRepertoireLogique() {
        try {
            int nbdel = 0;
            int nbdeltotal = 0;
            do {
                nbdel = RequeteSql.sqlDeleteRepertory();
                nbdeltotal += nbdel;
            }
            while (nbdel > 0);

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
            ResultSet rsele = RequeteSql.sqlgetListelementrejetaranger();

            while (rsele.next()) {

                // Recuperer les info de l'elements
                String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);
                String lcIdxFilename = rsele.getString(Context.LC_IDX_FILENAME);

                String source = normalizePath(Context.getAbsolutePathFirst() + pathFromRoot + lcIdxFilename);
                String dest = source + ".rejet";
                ActionfichierRepertoire.moveFile(new File(source).toPath(), new File(dest).toPath());

            }
        } catch (SQLException | IOException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    /**
     * Move new to grp photos.
     */
    public void actionRangerNew() {

        ifYourNotOnTheNewCtgGetOut();

        try {

            moveAllNewEleToRacineNew();

            //Regroupement
            ResultSet rsele = RequeteSql.sqlgetListelementnewaclasser(Context.getTempsAdherence());
            List<Ele> listEleBazar = new ArrayList();
            List<Ele> listEletmp = new ArrayList();
            List<Ele> listElekidz = new ArrayList();
            List<String> listkidsModel = Context.getKidsModelList();
            long maxprev = 0;
            while (rsele.next()) {

                // Recuperer les info de l'elements
                String pathFromRoot = rsele.getString(Context.PATH_FROM_ROOT);
                String lcIdxFilename = rsele.getString(Context.LC_IDX_FILENAME);
                String cameraModel = rsele.getString("CameraModel");
                long mint = rsele.getLong("mint");
                long maxt = rsele.getLong("maxt");

                if (listkidsModel.contains(cameraModel)) {
                    listElekidz.add(new Ele(lcIdxFilename, pathFromRoot));
                } else {
                    if (mint > maxprev) {

                        if (listEletmp.size() > Context.getThresholdBazar()) {

                            regrouper(listEletmp);

                        } else {
                            listEleBazar.addAll(listEletmp);
                        }

                        listEletmp = new ArrayList();

                    }
                    maxprev = maxt;

                    listEletmp.add(new Ele(lcIdxFilename, pathFromRoot));
                }

            }

            regrouper(listEletmp);

            regrouper(listElekidz, Context.getKidz());
            regrouper(listEleBazar, Context.getRepBazar());

            actionDeleteEmptyDirectoryRepertoireNew();

        } catch (SQLException | IOException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    private void moveAllNewEleToRacineNew() throws SQLException, IOException {
        //mise a plat du repertoire @new
        ResultSet rseleAplat = RequeteSql.sqlgetListelementnewaclasser(Context.getTempsAdherence());
        while (rseleAplat.next()) {
            if (rseleAplat.getString(Context.PATH_FROM_ROOT).compareTo("") != 0) {
                String source = normalizePath(Context.getAbsolutePathFirst() + rseleAplat.getString(Context.PATH_FROM_ROOT) + rseleAplat.getString(Context.LC_IDX_FILENAME));
                String uniqueID = UUID.randomUUID().toString();
                String rename = ("$" + uniqueID + "$" + supprimerbalisedollar(rseleAplat.getString(Context.LC_IDX_FILENAME))).toLowerCase();
                String destination = normalizePath(Context.getAbsolutePathFirst() + File.separator + rename);
                ActionfichierRepertoire.moveFile(source, destination);
            }
        }
    }

    private void ifYourNotOnTheNewCtgGetOut() {
        if (!Context.getAbsolutePathFirst().contains(Context.getRepertoireNew())) {
            String msg = "nothing do , not in " + Context.getRepertoireNew() + " repertory ";
            logecrireuserlogInfo(msg);
            throw new IllegalStateException(msg);
        }
    }

    private void regrouper(List<Ele> listEle, String repertoiredest) throws IOException, SQLException {
        String directoryName = normalizePath(Context.getAbsolutePathFirst() + "$" + repertoiredest + "$");

        ActionfichierRepertoire.mkdir(directoryName);

        for (Ele ele : listEle) {
            String source = normalizePath(Context.getAbsolutePathFirst() + ele.getPathFromRoot() + ele.getLcIdxFilename());
            String uniqueID = UUID.randomUUID().toString();
            String rename = ("$" + uniqueID + "$" + supprimerbalisedollar(ele.getLcIdxFilename())).toLowerCase();
            String destination = normalizePath(directoryName + File.separator + rename);
            ActionfichierRepertoire.moveFile(source, destination);

        }
    }

    private void regrouper(List<Ele> listEle) throws IOException, SQLException {
        String uniqueID = UUID.randomUUID().toString();
        regrouper(listEle, uniqueID);
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
                    File destination = new File(directoryName + File.separator + source.toPath().getFileName());
                    ActionfichierRepertoire.moveFile(source.toPath(), destination.toPath());
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
     * suprimmer tout les repertoires vide (physique et logique)
     *
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public void actionDeleteEmptyDirectoryRepertoireNew() throws IOException, SQLException {

        ifYourNotOnTheNewCtgGetOut();

        File directory = new File(Context.getAbsolutePathFirst() + File.separator);
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
        initalizerootselection();
        rootSelected.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                        if ((Integer) number2 >= 0) {
                            try {
                                selectLeRepertoireRootduFichierLigthroom(rootSelected.getItems().get((Integer) number2).toString());
                            } catch (IOException e) {
                                LOGGER.log(Level.INFO, e.toString());
                            }
                        }
                    }
                }
        );
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
