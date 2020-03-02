package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.Main;
import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.models.RequeteSql;
import com.malicia.mrg.photo.Ele;
import com.malicia.mrg.photo.ElePhoto;
import com.malicia.mrg.photo.GrpPhoto;
import com.malicia.mrg.photo.Rep;
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

import javax.swing.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The type Main frame controller.
 */
public class MainFrameController {

    private static final java.util.logging.Logger LOGGER;
    private static final String DEST_NULL = "destNull";
    private static final String DEST_NOT_EXIST = "destNotExist";
    private static final String SRC_NOT_EXIST = "srcNotExist";
    private static final String ERR_IN_MOVE = "errInMove";
    private static final String OK_MOVE_SAME = "OKMoveSame";
    private static final String OK_MOVE_DRY_RUN = "OKMoveDryRun";
    private static final String OK_MOVE_DO = "OKMoveDo";
    private static final String DRYRUN = "dryRun =>";
    private static final Object FILE = 1;

    static {
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
    }

    /**
     * The Absolute path.
     */
    Map<String, String> absolutePath = new HashMap<String, String>();
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

    /**
     * Instantiates a new Main frame controller.
     */
    public MainFrameController() {
        LOGGER.info("mainFrameController");
    }

    /**
     * Select le repertoire physique new.
     * sélectionner le répertoire ou sont les photo new
     * modifier et sauvegarde dans le properties
     */
    private void selectLeRepertoirePhysiqueNew() {
        LOGGER.info("selectLeRepertoirePhysiqueNew");

        LOGGER.info("-");
        LOGGER.info("-");

        //Create a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(Context.getPrimaryStage());
        if (file != null) {
            LOGGER.info("selectedFile:" + file.getAbsolutePath());
            Context.setRepertoireNew(file.getAbsolutePath());
        }
        Context.savePropertiesParameters(Context.currentContext);
    }

    /**
     * Boucle supression repertoire physique boolean.
     *
     * @param dir the dir
     * @return the boolean
     */
    private int boucleSupressionRepertoire(File dir) throws IOException, SQLException {
        boolean returnVal = false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            int success = 0;
            for (int i = 0; i < children.length; i++) {
                success += boucleSupressionRepertoire(new File(dir, children[i]));
            }

            if (success == children.length) {
                // The directory is now empty directory free so delete it
                LOGGER.info("delete repertory:" + dir.toString());
                returnVal = ActionfichierRepertoire.delete_dir(dir);
                if (returnVal) {
                    return 1;
                }

            }

        }
        return 0;
    }

    /**
     * Gets imageicon resized.
     *
     * @param imagesJpg the images jpg
     * @return the imageicon resized
     */
    private ImageIcon getImageiconResized(String imagesJpg) {
        LOGGER.info(imagesJpg);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //this is your screen size
        ImageIcon imageIcon = new ImageIcon(imagesJpg); //imports the image
        imageIcon.getImage().flush();
        int fact = imageIcon.getIconHeight() / (screenSize.height / 10);
        ImageIcon imageIcon2 = null;
        if (fact > 0) {
            Image image = imageIcon.getImage(); // transform it
            Image newimg = image.getScaledInstance(imageIcon.getIconWidth() / fact, imageIcon.getIconHeight() / fact, Image.SCALE_SMOOTH); // scale it the smooth way
            imageIcon2 = new ImageIcon(newimg);
            imageIcon2.getImage().flush();
        }
        return imageIcon2;
    }

    /**
     * Merge hashtable.
     *
     * @param dReturnEle       the d return ele
     * @param groupAndMouveEle the group and mouve ele
     */
    private void mergeHashtable(HashMap dReturnEle, HashMap groupAndMouveEle) {
        Set<String> keys = groupAndMouveEle.keySet();
        for (String key : keys) {
            if (dReturnEle.containsKey(key)) {
                int val = (int) dReturnEle.get(key) + (int) groupAndMouveEle.get(key);
                dReturnEle.put(key, val);
            } else {
                dReturnEle.put(key, groupAndMouveEle.get(key));
            }
//            System.out.println("Value of "+key+" is: "+groupAndMouveEle.get(key));

        }

    }

    private HashMap moveeletonewgroup(GrpPhoto grpPhoto, boolean dryRun) throws IOException, SQLException {

        HashMap displayReturn = new HashMap();
        displayReturn.put(DEST_NULL, 0);
        displayReturn.put(DEST_NOT_EXIST, 0);
        displayReturn.put(SRC_NOT_EXIST, 0);
        displayReturn.put(ERR_IN_MOVE, 0);
        displayReturn.put(OK_MOVE_SAME, 0);
        displayReturn.put(OK_MOVE_DRY_RUN, 0);
        displayReturn.put(OK_MOVE_DO, 0);

        // Test de fesabilité
        //
        if (grpPhoto.getAbsolutePath() == null) {
            displayReturn.put(DEST_NULL, (Integer) displayReturn.get(DEST_NULL) + 1);
            throw new IllegalStateException("DEST_NULL:absolutePath is null");
        }

        File directoryrepDest = new File(grpPhoto.getAbsolutePath() + grpPhoto.getPathFromRootComumn());
        if (!directoryrepDest.exists()) {
            displayReturn.put(DEST_NOT_EXIST, (Integer) displayReturn.get(DEST_NOT_EXIST) + 1);
            throw new IllegalStateException("DEST_NOT_EXIST:" + directoryrepDest.toString());
        }

        //Création du repertoire destination
        String directoryName = grpPhoto.getAbsolutePath() + grpPhoto.getPathFromRootComumn() + grpPhoto.getNvxNomRepertoire();
        File directory = new File(directoryName);
        if (!directory.exists() && !dryRun) {
            ActionfichierRepertoire.mkdir(directory);
        }

        for (int i = 0; i < grpPhoto.getElesrc().size(); i++) {

            File source = new File(grpPhoto.getElesrc().get(i));
            File destination = new File(directoryName + "/" + source.toPath().getFileName());
            grpPhoto.addEledest(i, ActionfichierRepertoire.normalizePath(destination.toString()));

            if (true == (source.toString().compareTo(destination.toString()) == 0)) {
                displayReturn.put(OK_MOVE_SAME, (Integer) displayReturn.get(OK_MOVE_SAME) + 1);
            } else if (true == !source.exists()) {
                displayReturn.put(SRC_NOT_EXIST, (Integer) displayReturn.get(SRC_NOT_EXIST) + 1);
                throw new IllegalStateException("SRC_NOT_EXIST:" + source.toString());
            } else if (true == dryRun) {
                displayReturn.put(OK_MOVE_DRY_RUN, (Integer) displayReturn.get(OK_MOVE_DRY_RUN) + 1);
            } else {
                ActionfichierRepertoire.move_file(source.toPath(), destination.toPath());
                displayReturn.put(OK_MOVE_DO, (Integer) displayReturn.get(OK_MOVE_DO) + 1);
            }
        }
        return displayReturn;
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
            long captureTime = rsele.getLong("captureTime");
            long mint = rsele.getLong("mint");
            long maxt = rsele.getLong("maxt");
            String src = rsele.getString("src");
            String absPath = rsele.getString("absolutePath");


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
            long captureTime = rsgrp.getLong("captureTime");
            long captureTimeOrig = rsgrp.getLong("captureTimeOrig");
            long mint = rsgrp.getLong("mint");
            long maxt = rsgrp.getLong("maxt");
            String src = rsgrp.getString("src");
            String pathFromRoot = rsgrp.getString("pathFromRoot");
            String absPath = rsgrp.getString("absolutePath");


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
     * Regroupe by new group java . util . list.
     *
     * @param listkidsModel the kids model list
     * @return the java . util . list
     * @throws SQLException the sql exception
     */
    public java.util.List<GrpPhoto> regroupeEleRepNewbyGroup(List<String> listkidsModel) throws SQLException {

//            constitution des groupes

        GrpPhoto Bazar = new GrpPhoto(Context.getRepBazar(), Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");
        GrpPhoto NoDate = new GrpPhoto(Context.getNoDate(), Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");
        GrpPhoto Kidz = new GrpPhoto(Context.getKidz(), Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");

        ResultSet rs = RequeteSql.sqlGroupGrouplessByPlageAdheranceRepNew(Context.getTempsAdherence());

        GrpPhoto grpPhotoEnc = new GrpPhoto();

        java.util.List<GrpPhoto> listGrpPhoto = new ArrayList();


        while (rs.next()) {


            // Recuperer les info de l'elements
            String CameraModel = rs.getString("CameraModel");
            long captureTime = rs.getLong("captureTime");
            long captureTimeOrig = rs.getLong("captureTimeOrig");
            long mint = rs.getLong("mint");
            long maxt = rs.getLong("maxt");
            String src = rs.getString("src");
            String absPath = rs.getString("absolutePath");

            //constitution des groupes forcé
            if (listkidsModel.contains(CameraModel)) {
                Kidz.forceadd(null, captureTime, mint, maxt, ActionfichierRepertoire.normalizePath(src));
            } else {


                //Constitution des groupes de photo standard
                if (!grpPhotoEnc.add(null, captureTime, mint, maxt, src, absPath, Context.getRepertoireNew() + "/")) {

                    //regroupement forcé des groupe de photos
                    if (grpPhotoEnc.getnbele() <= 5) {
                        Bazar.add(grpPhotoEnc.getElesrc(), grpPhotoEnc.getEledt(), grpPhotoEnc.getEledest());
                    } else {
                        if (grpPhotoEnc.isdateNull()) {
                            NoDate.add(grpPhotoEnc.getElesrc(), grpPhotoEnc.getEledt(), grpPhotoEnc.getEledest());
                        } else {
                            listGrpPhoto.add(grpPhotoEnc);
                        }
                    }

                    grpPhotoEnc = new GrpPhoto();
                    if (!grpPhotoEnc.add(null, captureTime, mint, maxt, src, absPath, Context.getRepertoireNew() + "/")) {
                        throw new IllegalStateException("Erreur l'ors de l'ajout de l'element au group de photo ");
                    }
                }

            }
        }
        listGrpPhoto.add(grpPhotoEnc);
        listGrpPhoto.add(Bazar);
        listGrpPhoto.add(NoDate);
        listGrpPhoto.add(Kidz);


        return listGrpPhoto;
    }

    /**
     * Moveto new group boolean.
     *
     * @param dryRun the dry run
     * @param ggp    the ggp
     * @return the boolean
     */
    private boolean movetoNewGroup(boolean dryRun, List<GrpPhoto> ggp) throws IOException, SQLException {
//       Execution du deplacement

        LOGGER.info("Printing result...");

        int nbeleOK = 0;
        int nbeleAfaire = 0;

        HashMap codeRetourAction = new HashMap();

        LOGGER.info((dryRun ? DRYRUN : "") + "Nb Groupe Crée " + ggp.size());

        for (int i = 0; i < ggp.size(); i++) {
            GrpPhoto gptemp = ggp.get(i);

            //Deplacement
            HashMap hashRet = moveeletonewgroup(gptemp, dryRun);

            //Display
            if ((Integer) hashRet.get(OK_MOVE_DRY_RUN) > 0) {
                LOGGER.info("hashRet:" + hashRet.toString());
                LOGGER.info("GrpPhoto:" + gptemp.toString());
            }
//            if (gptemp.getNvxNomRepertoire().compareTo("@Bazar__") == 0) {
//                LOGGER.info((dryRun ? DRYRUN : "") + "Bazar Detail:" + hashRet.toString());
//            }

            //Cumul des compteurs comparatif
            nbeleAfaire += gptemp.getnbele();
            nbeleOK += (int) hashRet.get(OK_MOVE_DO) + (int) hashRet.get(OK_MOVE_SAME) + (int) hashRet.get(OK_MOVE_DRY_RUN);

            //regrouper les retours de chaque groupe
            mergeHashtable(codeRetourAction, hashRet);

        }


        logecrireuserlogInfo((dryRun ? DRYRUN : "") + "Nb Groupe " + ggp.size() + " = " + codeRetourAction.toString());

        return (nbeleAfaire == nbeleOK);
    }

    /**
     * Initialize.
     */
    private void initialize() {
        LOGGER.info("initialize");

        databaselrcat.setText(Context.getCatalogLrcat());
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
                absolutePath.put(name, rs.getString("absolutePath"));
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
    private void selectLeRepertoireRootduFichierLigthroom(String rootName) {
        logecrireuserlogInfo("absolutePath.get(rootName) : " + absolutePath.get(rootName));
        Context.setRoot(absolutePath.get(rootName));
        Context.savePropertiesParameters(Context.currentContext);
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
            File file = fileChooser.showOpenDialog(Context.getPrimaryStage());
            if (file != null) {
                logecrireuserlogInfo("selectedFile:" + file.getAbsolutePath());
                Context.setCatalogLrcat(file.getAbsolutePath());
                initalizerootselection();
            }
            Context.savePropertiesParameters(Context.currentContext);
            Context.getController().initialize();
        } catch (SQLException e) {
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

        String ori = Context.getCatalogLrcat();
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
        LOGGER.severe("theException = " + "\n" + stringWritter.toString());
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
            ResultSet rsele = RequeteSql.sqlgetListelementrejetaranger(Context.getTempsAdherence());

            int minprev = 0;
            long maxprev = 0;
            int idx = 1; // idx 0 pour le bazar
            int idxrep = 0;

            while (rsele.next()) {

                // Recuperer les info de l'elements
                String file_id_local = rsele.getString("file_id_local");
                String folder_id_local = rsele.getString("folder_id_local");
                String pathFromRoot = rsele.getString("pathFromRoot");
                String lc_idx_filename = rsele.getString("lc_idx_filename");

                String source = ActionfichierRepertoire.normalizePath(Context.getAbsolutePathFirst() + pathFromRoot + lc_idx_filename);
                String dest = source + ".rejet";
                ActionfichierRepertoire.move_file(new File(source).toPath(), new File(dest).toPath());
//                String nomzip = ActionfichierRepertoire.normalizePath(Context.getAbsolutePathFirst() + pathFromRoot + "$" + Context.getTitreRejet()+ "$" + pathFromRoot.replace("/", "_") + ".zip");

//                Packager.packZip(new File(nomzip), new File(source));

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
        try {
            ResultSet rsele = RequeteSql.sqlgetListelementnewaclasser(Context.getTempsAdherence());

            java.util.List<Ele> listEle = new ArrayList();
            java.util.List<Rep> listRep = new ArrayList();

            int minprev = 0;
            long maxprev = 0;
            int idxBazar = 1;// idx 1 pour le bazar
            int idx = 1; // idx 1 pour le bazar
            int idxrep = 0;

            while (rsele.next()) {

                // Recuperer les info de l'elements
                String file_id_local = rsele.getString("file_id_local");
                String folder_id_local = rsele.getString("folder_id_local");
                String pathFromRoot = rsele.getString("pathFromRoot");
                String lc_idx_filename = rsele.getString("lc_idx_filename");
                String captureTime = rsele.getString("captureTime");
                long mint = rsele.getLong("mint");
                long maxt = rsele.getLong("maxt");

                if (mint > maxprev || idx == idxBazar) {// idx 1 pour le bazar
                    idx += 1;
                }
                maxprev = maxt;

                listEle.add(new Ele(idx, file_id_local, lc_idx_filename, folder_id_local, captureTime, pathFromRoot));

                boolean newrep = true;
                for (int i = 0; i < listRep.size(); i++) {
                    if (folder_id_local.compareTo(listRep.get(i).getFolderIdLocal()) == 0) {
                        newrep = false;
                        break;
                    }
                }
                if (newrep) {
                    idxrep += 1;
                    listRep.add(new Rep(idxrep, folder_id_local, pathFromRoot));
                }

            }

            //recalcul des idx pour les ele pour envoyer dans le bazar
            // idx 1 pour le bazar
            int nbidx = 1;
            int idxencart = 0;
            for (int i = 1; i < listEle.size(); i++) {
                int idxprev = listEle.get(i - 1).getIdx();
                int idxenc = listEle.get(i).getIdx();
                if (idxenc == idxprev) {
                    nbidx += 1;
                } else {
                    if (nbidx < Context.getThresholdBazar()) {
                        for (int y = 1; y <= nbidx; y++) {
                            listEle.get(i - y).setIdx(idxBazar);
                        }
                    }
                    nbidx = 1;
                }
            }

            //compactage des idx
            idx = 2;//idx 1 pour bazar
            int idxprev = listEle.get(0).getIdx();
            for (int i = 0; i < listEle.size(); i++) {
                int idxenc = listEle.get(i).getIdx();
                if (idxenc > 1) {
                    if (idxenc != idxprev) {
                        idx += 1;
                    }
                    listEle.get(i).setIdx(idx);
                    idxprev = idxenc;
                }

            }
            //test si assez de repertoire
            if (idx > idxrep) {
                throw new IllegalStateException("Créer " + (idx - idxrep) + " repertoire tech dans lightroom");
            }

            //action sur les elements
            for (int i = 0; i < listEle.size(); i++) {
                int idxenc = listEle.get(i).getIdx();
                String lc_idx_filename = listEle.get(i).getLcIdxFilename();
                String rename = "$grp" + String.format("%05d", idxenc) + "_" + String.format("%05d", i) + "$" + supprimerbalisedollar(lc_idx_filename);
                listEle.get(i).renameto(rename);
                listEle.get(i).moveto(listRep.get(idxenc));
            }

            //preparation action sur les repertoires
            java.util.List<Rep> listRepAsup = new ArrayList();
            java.util.List<String> listRepRename = new ArrayList();
            for (int i = 0; i < listRep.size(); i++) {
                int idxenc = listRep.get(i).getIdxrep();
                String rename;
                if (idxBazar == idx) {
                    rename = Context.getRepertoireNew() + File.separator + Context.getRepBazar();
                } else {
                    if (i <= idx) {
                        rename = Context.getRepertoireNew() + File.separator + "$grp" + String.format("%05d", idxenc) + "$";
                    } else {
                        rename = Context.getRepertoireNew() + File.separator + "$tec" + String.format("%05d", idxenc) + "$";
                    }
                }
                rename = ActionfichierRepertoire.normalizePath(rename + "/");
                boolean repexist = false;
                for (int y = 0; y < listRep.size(); y++) {
                    if (listRep.get(y).getPathFromRoot().compareTo(rename) == 0) {
                        repexist = true;
                        listRepAsup.add(listRep.get(y));
                        break;
                    }
                }
                if (!repexist) {
                    listRepRename.add(rename);
                }
            }
            listRep.removeAll(listRepAsup);

            //action sur les repertoire
            for (int i = 0; i < listRep.size(); i++) {
                listRep.get(i).moveto(listRepRename.get(i));
            }


        } catch (SQLException | IOException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    private String createnewname(String balisedollar, String pathnettoyer) {
        return "";
    }


    private String supprimerbalisedollar(String lc_idx_filename) {
        Pattern pattern = Pattern.compile("(\\$.*\\$)*(.*)");
        Matcher matcher = pattern.matcher(lc_idx_filename);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";

    }

    /**
     * Move new to grp photos.
     */
    public void actionMovenewtogrpphotos() {
        try {
            LOGGER.info("moveNewToGrpPhotos : dryRun = " + Context.getDryRun());

            java.util.List<GrpPhoto> groupDePhoto = regroupeEleRepNewbyGroup(Context.getKidsModelList());

            if (movetoNewGroup(true, groupDePhoto) && !Context.getDryRun()) {
                movetoNewGroup(Context.getDryRun(), groupDePhoto);
            } else {
                LOGGER.info("movetoNewGroup KO, nothig nmove");
            }
        } catch (IOException | SQLException e) {
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
            LOGGER.info("actionRangerlebazar : dryRun = " + Context.getDryRun());

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
                    System.out.println(elementsPhoto.get(iele));
                } else {
                    HashMap<String, Object> ret = showPopupWindow(elePhotocurrent);
                    if (ret != null) {
                        if (ret.get(PopUpController.retourCode).toString().compareTo(PopUpController.valstoprun) == 0) {
                            throw new IllegalStateException("actionRangerlebazar->ctrlpopup:" + PopUpController.valstoprun);
                        }
                    }
                }
            }


//            throw new IllegalStateException("En travaux");
        } catch (Exception e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    private HashMap<String, Object> showPopupWindow(ElePhoto elePhotocurrent) throws IOException, SQLException {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        //Preparation technique de la popup
        FXMLLoader loaderpopup = new FXMLLoader();
        loaderpopup.setLocation(Main.class.getClassLoader().getResource("popUp.fxml"));
        Parent rootpopup = loaderpopup.load();
        PopUpController controllerpopup = loaderpopup.getController();
        Context.setControllerpopup(controllerpopup);
        Scene scene = new Scene(rootpopup);
        Stage popupStage = new Stage();
        PopUpController.setPopupStage(popupStage);
        popupStage.initOwner(Context.getPrimaryStage());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setScene(scene);

        //Preparation fonctionelle de la popup
        controllerpopup.setImage(PopUpController.idimageOne, elePhotocurrent.getSrc());

        for (int i = 0; i < elePhotocurrent.getGrpPhotoCandidat().size(); i++) {
            LOGGER.info(elePhotocurrent.toString());
            GrpPhoto grpphotoenc = elePhotocurrent.getGrpPhotoCandidat().get(i);
            List<String> grpphotoele = grpphotoenc.getElesrc();
            controllerpopup.setLblinfo("" + (i + 1) + "/" + elePhotocurrent.getGrpPhotoCandidat().size() + " : " + grpphotoenc.getPathFromRootComumn());
            int nb = grpphotoele.size();
            int id1 = 0, id2 = 0, id3 = 0, id4 = 0;

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
            controllerpopup.setImage(PopUpController.idimage2LL, grpphotoele.get(id1));
            controllerpopup.setImage(PopUpController.idimage2LR, grpphotoele.get(id2));
            controllerpopup.setImage(PopUpController.idimage2UL, grpphotoele.get(id3));
            controllerpopup.setImage(PopUpController.idimage2UR, grpphotoele.get(id4));
            //execution popup
            popupStage.showAndWait();
            HashMap<String, Object> ret = controllerpopup.getResult();
            switch (ret.get(PopUpController.retourCode).toString()) {
                case PopUpController.valstoprun:
                    return ret;
                case PopUpController.valselect:
                    File source = new File(elePhotocurrent.getSrc());
                    String directoryName = grpphotoenc.getAbsolutePath() + grpphotoenc.getPathFromRootComumn();
                    File destination = new File(directoryName + "/" + source.toPath().getFileName());
                    ActionfichierRepertoire.move_file(source.toPath(), destination.toPath());
                    return ret;
                case PopUpController.valnext:
                    break;
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
     */
    public void actionDeleteEmptyDirectoryRepertoireNew() {
        try {
            if (Context.getDryRun()) {
                logecrireuserlogInfo("deleteEmptyDirectory : DryRun = " + Context.getDryRun());
            } else {
                File directory = new File(Context.getAbsolutePathFirst() + Context.getRepertoireNew() + "/");

                int ndDelTotal = 0;
                ndDelTotal = boucleSupressionRepertoire(directory);
                logecrireuserlogInfo("delete all from " + directory + " : " + String.format("%05d", ndDelTotal));
            }
        } catch (IOException | SQLException e) {
            logecrireuserlogInfo(e.toString());
            excptlog(e);
        }
    }

    /**
     * First.
     *
     * @throws SQLException the sql exception
     */
    public void first() throws SQLException {
        initalizerootselection();
        rootSelected.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if ((Integer) number2 >= 0) {
                    selectLeRepertoireRootduFichierLigthroom(rootSelected.getItems().get((Integer) number2).toString());
                }
            }
        });
        initialize();
    }

}
