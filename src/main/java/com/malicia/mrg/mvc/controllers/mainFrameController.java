package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.models.RequeteSql;
import com.malicia.mrg.photo.GrpPhoto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


/**
 * The type Main frame controller.
 */
public class mainFrameController {

    public static final String DRY_RUN = "dryRun =>";
    private static final java.util.logging.Logger LOGGER;

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
    public mainFrameController() {
        LOGGER.info("mainFrameController");
    }

    /**
     * Select le repertoire physique new.
     * sélectionner le répertoire ou sont les photo new
     * modifier et sauvegarde dans le properties
     */
    public static void selectLeRepertoirePhysiqueNew() {
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
     * Deplace une photo.
     *
     * @param photo          the photo
     * @param repertoiredest the repertoiredest
     */
    public static void deplaceUnePhoto(String photo, String repertoiredest) {
        LOGGER.info("deplaceUnePhoto");

        LOGGER.info("-deplace une photo dans le repertoire (physique et logique)");

        LOGGER.info("new_repertoire\n" +
                "basename_file\n" +
                "\n" +
                "rqt_rootFolder\n" +
                "max_id_local\n" +
                "max_id_global\n" +
                "\n" +
                "\n" +
                "select id_local , absolutePath\n" +
                "from  \"main\".\"AgLibraryRootFolder\" \n" +
                "\n" +
                "premier ligne => rqt_rootFolder\n" +
                "\n" +
                "\n" +
                "\n" +
                "select * from \n" +
                "(SELECT tbl_name FROM sqlite_master\n" +
                "WHERE type = 'table'\n" +
                "and sql like \"%id_global%\")\n" +
                "\n" +
                "boucle pour trouver le plus grand id_global\n" +
                "select max(id_global) from \n" +
                "tbl_name\n" +
                "\n" +
                "sortie => max_id_global\n" +
                "\n" +
                "\n" +
                "\n" +
                "select * from \n" +
                "(SELECT tbl_name FROM sqlite_master\n" +
                "WHERE type = 'table'\n" +
                "and sql like \"%id_local%\")\n" +
                "\n" +
                "boucle pour trouver le plus grand id_local\n" +
                "select max(id_local) from \n" +
                "tbl_name\n" +
                "\n" +
                "sortie => max_id_local\n" +
                "\n" +
                "\n" +
                "\n" +
                "INSERT INTO \"main\".\"AgLibraryFolder\" \n" +
                "(\"id_local\", \n" +
                "\"id_global\", \n" +
                "\"pathFromRoot\", \n" +
                "\"rootFolder\") \n" +
                "VALUES (\n" +
                "max_id_local+1, \n" +
                "max_id_global+1, \n" +
                "new_repertoire, \n" +
                "rqt_rootFolder);\n" +
                "\n" +
                "update \"main\".\"AgLibraryFile\" \n" +
                "set folder =  max_id_local+1\n" +
                "where \"baseName\" = basename_file;\n" +
                "\n" +
                "\n");

    }

    /**
     * Boucle supression repertoire physique boolean.
     *
     * @param dir the dir
     * @return the boolean
     */
    private static int boucleSupressionRepertoire(File dir) throws IOException, SQLException {
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
    public static ImageIcon getImageiconResized(URL imagesJpg) {
        LOGGER.info(imagesJpg.toString());
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
    public static void mergeHashtable(Hashtable dReturnEle, Hashtable groupAndMouveEle) {
        Set<String> keys = groupAndMouveEle.keySet();
        for (String key : keys) {
            if (key.compareTo(GrpPhoto.LISTE_ERREUR) != 0) {
                if (dReturnEle.containsKey(key)) {
                    int val = (int) dReturnEle.get(key) + (int) groupAndMouveEle.get(key);
                    dReturnEle.put(key, val);
                } else {
                    dReturnEle.put(key, groupAndMouveEle.get(key));
                }
//            System.out.println("Value of "+key+" is: "+groupAndMouveEle.get(key));
            }
        }

    }

    public static Hashtable moveeletonewgroup(GrpPhoto grpPhoto, boolean dryRun) throws IOException {

        Hashtable displayReturn = new Hashtable();
        displayReturn.put(GrpPhoto.DEST_NULL, 0);
        displayReturn.put(GrpPhoto.DEST_NOT_EXIST, 0);
        displayReturn.put(GrpPhoto.SRC_NOT_EXIST, 0);
        displayReturn.put(GrpPhoto.ERR_IN_MOVE, 0);
        displayReturn.put(GrpPhoto.OK_MOVE_SAME, 0);
        displayReturn.put(GrpPhoto.OK_MOVE_DRY_RUN, 0);
        displayReturn.put(GrpPhoto.OK_MOVE_DO, 0);

        ArrayList<String> listeErreur = new ArrayList<String>();

        // Test de fesabilité
        //
        if (grpPhoto.getAbsolutePath() == null) {
            displayReturn.put(GrpPhoto.DEST_NULL, (Integer) displayReturn.get(GrpPhoto.DEST_NULL) + 1);
            listeErreur.add("DEST_NULL:absolutePath is null");
            return displayReturn;
        }

        File directoryrepDest = new File(grpPhoto.getAbsolutePath() + grpPhoto.getPathFromRootComumn());
        if (!directoryrepDest.exists()) {
            displayReturn.put(GrpPhoto.DEST_NOT_EXIST, (Integer) displayReturn.get(GrpPhoto.DEST_NOT_EXIST) + 1);
            listeErreur.add("DEST_NOT_EXIST:" + directoryrepDest.toString());
            return displayReturn;
        }

        //Création du repertoire destination
        String directoryName = grpPhoto.getAbsolutePath() + grpPhoto.getPathFromRootComumn() + grpPhoto.getNomRepetrtoire();
        File directory = new File(directoryName);
        if (!directory.exists()) {
            if (!dryRun) {
                try {
                    ActionfichierRepertoire.mkdir(directory);
                } catch (SQLException e) {
                    LOGGER.info(e.toString());
                    e.printStackTrace();
                    return displayReturn;
                }
            }
        }

        for (int i = 0; i < grpPhoto.getEle().size(); i++) {

            File source = new File(grpPhoto.getEle().get(i));
            File destination = new File(directoryName + "/" + source.toPath().getFileName());

            if (source.toString().compareTo(destination.toString()) == 0) {
                displayReturn.put(GrpPhoto.OK_MOVE_SAME, (Integer) displayReturn.get(GrpPhoto.OK_MOVE_SAME) + 1);
            } else {
                if (!source.exists()) {
                    displayReturn.put(GrpPhoto.SRC_NOT_EXIST, (Integer) displayReturn.get(GrpPhoto.SRC_NOT_EXIST) + 1);
                    listeErreur.add("SRC_NOT_EXIST:" + source.toString());
                } else {
                    if (dryRun) {
                        displayReturn.put(GrpPhoto.OK_MOVE_DRY_RUN, (Integer) displayReturn.get(GrpPhoto.OK_MOVE_DRY_RUN) + 1);
                    } else {
                        try {
                            ActionfichierRepertoire.move_file(source.toPath(), destination.toPath());
                            displayReturn.put(GrpPhoto.OK_MOVE_DO, (Integer) displayReturn.get(GrpPhoto.OK_MOVE_DO) + 1);
                        } catch (SQLException e) {
                            displayReturn.put(GrpPhoto.ERR_IN_MOVE, (Integer) displayReturn.get(GrpPhoto.ERR_IN_MOVE) + 1);
                            listeErreur.add("ERR_IN_MOVE:" + source.toString() + "=>" + destination.toString());
                            LOGGER.info(e.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        displayReturn.put(GrpPhoto.LISTE_ERREUR, listeErreur);
        return displayReturn;
    }

    /**
     * Regroupe by new group java . util . list.
     *
     * @param listkidsModel the kids model list
     * @return the java . util . list
     */
    public java.util.List<GrpPhoto> regroupeEleRepNewbyGroup(List<String> listkidsModel) throws SQLException {

//            constitution des groupes

        GrpPhoto Bazaz = new GrpPhoto(Context.getBazar(), Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");
        GrpPhoto NoDate = new GrpPhoto("@NoDate", Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");
        GrpPhoto Kidz = new GrpPhoto("@Kidz", Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");

        ResultSet rs = RequeteSql.sqlGroupGrouplessByPlageAdheranceRepNew(Context.getTempsAdherence());

        GrpPhoto grpPhotoEnc = new GrpPhoto();

        java.util.List<GrpPhoto> listGrpPhoto = new ArrayList();

        try {

            while (rs.next()) {


                // Recuperer les info de l'elements
                String CameraModel = rs.getString("CameraModel");
                long captureTime = rs.getLong("captureTime");
                long mint = rs.getLong("mint");
                long maxt = rs.getLong("maxt");
                String src = rs.getString("src");
                String absolutePath = rs.getString("absolutePath");

                //constitution des groupes forcé
                if (listkidsModel.contains(CameraModel)) {
                    Kidz.forceadd(null, mint, maxt, src);
                } else {


                    //Constitution des groupes de photo standard
                    if (!grpPhotoEnc.add(CameraModel, captureTime, mint, maxt, src, absolutePath, Context.getRepertoireNew() + "/")) {

                        //regroupement forcé des groupe de photos
                        if (grpPhotoEnc.getnbele() <= 5) {
                            Bazaz.add(grpPhotoEnc.getEle());
                        } else {
                            if (grpPhotoEnc.dateNull()) {
                                NoDate.add(grpPhotoEnc.getEle());
                            } else {
                                listGrpPhoto.add(grpPhotoEnc);
                            }
                        }

                        grpPhotoEnc = null;
                        grpPhotoEnc = new GrpPhoto();
                        if (!grpPhotoEnc.add(CameraModel, captureTime, mint, maxt, src, absolutePath, Context.getRepertoireNew() + "/")) {
                            throw new IllegalStateException("Erreur l'ors de l'ajout de l'element au group de photo ");
                        }
                    }

                }
            }
            listGrpPhoto.add(grpPhotoEnc);
            listGrpPhoto.add(Bazaz);
            listGrpPhoto.add(NoDate);
            listGrpPhoto.add(Kidz);

        } catch (SQLException e) {
            logecrireuserlogInfo(e.toString());
            e.printStackTrace();
        }

        return listGrpPhoto;
    }

    /**
     * Moveto new group boolean.
     *
     * @param dryRun the dry run
     * @param ggp    the ggp
     * @return the boolean
     */
    public boolean movetoNewGroup(boolean dryRun, List<GrpPhoto> ggp) throws IOException, SQLException {
//       Execution du deplacement

        LOGGER.info("Printing result...");

        int nbeleOK = 0;
        int nbeleAfaire = 0;

        Hashtable codeRetourAction = new Hashtable();

        LOGGER.info((dryRun ? DRY_RUN : "") + "Nb Groupe Crée " + ggp.size());

        for (int i = 0; i < ggp.size(); i++) {
            GrpPhoto gptemp = ggp.get(i);

            //Deplacement
            Hashtable hashRet = moveeletonewgroup(gptemp, dryRun);

            //Display
            if ((Integer) hashRet.get(GrpPhoto.OK_MOVE_DRY_RUN) > 0) {
                LOGGER.info("GrpPhoto:" + gptemp.toString());
                LOGGER.info(" hashRet:" + hashRet.toString());
            }
            if (gptemp.getNomRepetrtoire().compareTo("@Bazar__") == 0) {
                LOGGER.info((dryRun ? DRY_RUN : "") + "Bazar Detail:" + hashRet.toString());
            }

            //Cumul des compteurs comparatif
            nbeleAfaire += gptemp.getnbele();
            nbeleOK += (int) hashRet.get(GrpPhoto.OK_MOVE_DO) + (int) hashRet.get(GrpPhoto.OK_MOVE_SAME) + (int) hashRet.get(GrpPhoto.OK_MOVE_DRY_RUN);

            //display liste erreur
            ArrayList<String> listeErreur = (ArrayList<String>) hashRet.get(GrpPhoto.LISTE_ERREUR);
            if (listeErreur.size() > 0) {
                logecrireuserlogInfo((dryRun ? DRY_RUN : "") + "listeErreur non vide : ReSync Ligthroom ");
                Iterator<String> iter = listeErreur.iterator();
                while (iter.hasNext()) {
                    LOGGER.info("listeErreur : " + iter.next() + " ");
                }
            }

            //regrouper les retour de chaque groupe
            mergeHashtable(codeRetourAction, hashRet);

        }


        logecrireuserlogInfo((dryRun ? DRY_RUN : "") + "Nb Groupe " + ggp.size() + " = " + codeRetourAction.toString());

        return (nbeleAfaire == nbeleOK);
    }

    /**
     * Initialize.
     */
    public void initialize() {
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
            e.printStackTrace();
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
    public void selectFichierLigthroom() {
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
            e.printStackTrace();
        }
    }

    public void makeadulpicatelrcatwithdate() {
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
            e.printStackTrace();
        }
    }

    /**
     * Change dry run.
     */
    public void ChangeDryRun() {
        Context.setDryRun(!Context.getDryRun());
        Context.getController().initialize();
        logecrireuserlogInfo("ChangeDryRun to " + Context.getDryRun());
    }

    /**
     * Boucle delete repertoire logique.
     */
    public void deleteRepertoireLogique() {
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
            e.printStackTrace();
        }
    }

    private void logecrireuserlogInfo(String msg) {
        userlogInfo.appendText(msg + "\n");
        LOGGER.info(msg);
    }

    /**
     * Move new to grp photos.
     */
    public void movenewtogrpphotos() {
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
            e.printStackTrace();
        }
    }

    /**
     * Abouturl.
     */
    public void abouturl() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(Context.getUrlgitwiki()));
            }
        } catch (IOException | URISyntaxException e) {
            logecrireuserlogInfo(e.toString());
            e.printStackTrace();
        }
        logecrireuserlogInfo(Context.getUrlgitwiki());
    }

    /**
     * Delete empty directory.
     * <p>
     * suprimmer tout les repertoires vide (physique et logique)
     */
    public void deleteEmptyDirectoryRepertoireNew() {
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
            e.printStackTrace();
        }
    }

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
    }

}
