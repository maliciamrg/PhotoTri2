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
import jdk.jfr.events.ExceptionThrownEvent;
import org.omg.CORBA.UserException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;


/**
 * The type Main frame controller.
 */
public class mainFrameController {

    private static final java.util.logging.Logger LOGGER;
    private static int ndDelTotal;

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
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-");
        LOGGER.info("-");
        LOGGER.info("---------------------------------------------------------------------------");
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
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-deplace une photo dans le repertoire (physique et logique)");
        LOGGER.info("---------------------------------------------------------------------------");
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
    private static boolean boucleSupressionRepertoire(File dir) {
        boolean returnVal = false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            boolean success = true;
            for (int i = 0; i < children.length; i++) {
                success &= boucleSupressionRepertoire(new File(dir, children[i]));
            }

            if (success) {
                // The directory is now empty directory free so delete it
                LOGGER.info("delete repertory:" + dir.toString());
                returnVal = actionfichierRepertoire.deleterepertoire(dir);
                if (returnVal) {
                    ndDelTotal += 1;
                }

            }

        } else {
            returnVal = false;
        }
        return returnVal;
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
     * Regroupe by new group java . util . list.
     *
     * @param listkidsModel the kids model list
     * @return the java . util . list
     */
    public static java.util.List<GrpPhoto> regroupeEleRepNewbyGroup(List<String> listkidsModel) {

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
                    Kidz.forceadd(CameraModel, mint, maxt, src);
                } else{


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

                        grpPhotoEnc = new GrpPhoto();
                        if(!grpPhotoEnc.add(CameraModel, captureTime, mint, maxt, src, absolutePath, Context.getRepertoireNew() + "/")) {
                            throw new ArithmeticException("Erreur l'ors de l'ajout de l'element au group de photo ");
                        };
                    }

                }

                //}
//                LOGGER.info("\tCameraModel: " + CameraModel +
//                        ", captureTime: " + captureTime +
//                        ", src : " + src);


            }
            listGrpPhoto.add(grpPhotoEnc);
            listGrpPhoto.add(Bazaz);
            listGrpPhoto.add(NoDate);
            listGrpPhoto.add(Kidz);


//            LOGGER.info("Nb row lues=> " + nbrow);
//            LOGGER.info("Nb row grp => " + nbele);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listGrpPhoto;
    }

    /**
     * Except new group java . util . list.
     *
     * @param groupDePhoto  the group de photo
     * @param KidsModelList the kids model list
     * @return the java . util . list
     */
    public static java.util.List<GrpPhoto> exceptNewGroup(List<GrpPhoto> groupDePhoto, List<String> KidsModelList) {
        java.util.List<GrpPhoto> excptgdp = new ArrayList();
        GrpPhoto Bazaz = new GrpPhoto(Context.getBazar(), Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");
        GrpPhoto NoDate = new GrpPhoto("@NoDate", Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");
        GrpPhoto Kidz = new GrpPhoto("@Kidz", Context.getAbsolutePathFirst(), Context.getRepertoireNew() + "/");


        Iterator<GrpPhoto> groupDePhotoIterator = groupDePhoto.iterator();

        while (groupDePhotoIterator.hasNext()) {
            GrpPhoto gdp = groupDePhotoIterator.next();
            if (KidsModelList.contains(gdp.getCameraModelGrp())) {
                Kidz.add(gdp.getEle());
            } else {
                if (gdp.getnbele() <= 5) {
                    Bazaz.add(gdp.getEle());
                } else {
                    if (gdp.dateNull()) {
                        NoDate.add(gdp.getEle());
                    } else {
                        excptgdp.add(gdp);
                    }
                }
            }
        }
        excptgdp.add(Bazaz);
        excptgdp.add(NoDate);
        excptgdp.add(Kidz);
        return excptgdp;
    }

    /**
     * Moveto new group boolean.
     *
     * @param dryRun the dry run
     * @param ggp    the ggp
     * @return the boolean
     */
    public boolean movetoNewGroup(boolean dryRun, List<GrpPhoto> ggp) {
//       Execution du deplacement

        LOGGER.info("Printing result...");
        int nbele = 0;

        Hashtable codeRetourAction = new Hashtable();

        LOGGER.info((dryRun ? "dryRun =>" : "") + "Nb Groupe Crée " + ggp.size());
        int nbrow = 0;
        for (int i = 0; i < ggp.size(); i++) {
            GrpPhoto gptemp = ggp.get(i);
            nbrow += gptemp.getnbele();

            Hashtable hashRet = gptemp.groupAndMouveEle(dryRun);
            LOGGER.info("GrpPhoto:" + gptemp.toString());
            LOGGER.info(" hashRet:" + hashRet.toString());
            if (gptemp.getNomRepetrtoire().compareTo("@Bazar__") == 0) {
                LOGGER.info((dryRun ? "dryRun =>" : "") + "Bazar Detail:" + hashRet.toString());
            }
            mergeHashtable(codeRetourAction, hashRet);

        }


        logecrireuserlogInfo((dryRun ? "dryRun =>" : "") + codeRetourAction.toString());
        nbele = (int) codeRetourAction.get(GrpPhoto.OK_MOVE_DO) + (int) codeRetourAction.get(GrpPhoto.OK_MOVE_SAME) + (int) codeRetourAction.get(GrpPhoto.OK_MOVE_DRY_RUN);
        return (nbrow == nbele);
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

    private void initalizerootselection() {
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
        int nbdel = 0;
        int nbdeltotal = 0;
        do {
            nbdel = RequeteSql.sqlDeleteRepertory();
            nbdeltotal += nbdel;
        }
        while (nbdel > 0);

        logecrireuserlogInfo("logical delete:" + String.format("%04d", nbdeltotal));
    }

    private void logecrireuserlogInfo(String msg) {
        userlogInfo.appendText(msg + "\n");
        LOGGER.info(msg);
    }

    /**
     * Move new to grp photos.
     */
    public void movenewtogrpphotos() {
        LOGGER.info("moveNewToGrpPhotos : dryRun = " + Context.getDryRun());
//        RequeteSql.sqlCombineAllGrouplessInGroupByPlageAdherance(Context.getTempsAdherence(), Context.getRepertoireNew());

        java.util.List<GrpPhoto> groupDePhoto = regroupeEleRepNewbyGroup(Context.getKidsModelList());
        if (movetoNewGroup(true, groupDePhoto) && !Context.getDryRun()) {
            movetoNewGroup(Context.getDryRun(), groupDePhoto);
//            movetoNewGroup(false,groupDePhoto);
        } else {
            LOGGER.info("movetoNewGroup KO, nothig nmove");
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
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
        if (Context.getDryRun()) {
            logecrireuserlogInfo("deleteEmptyDirectory : DryRun = " + Context.getDryRun());
        } else {
            File directory = new File(Context.getAbsolutePathFirst() + Context.getRepertoireNew() + "/");

            ndDelTotal = 0;
            boucleSupressionRepertoire(directory);
            logecrireuserlogInfo("delete all from " + directory + " : " + String.format("%05d", ndDelTotal));
        }
    }

    /**
     * Renommer un repertoire.
     * <p>
     * renomme un repertoire (physique et logique)
     *
     * @param repertoiresource the repertoiresource
     * @param repertoiredest   the repertoiredest
     * @param id_local         the id local
     * @param rootFolder       the root folder
     */
    public void renommerUnRepertoire(String repertoiresource, String repertoiredest, String id_local, String rootFolder) throws SQLException {
        File directory = new File(repertoiresource);
        File directorydest = new File(repertoiredest);
        if (directory.isDirectory()) {
            directory.renameTo(directorydest);
            RequeteSql.updateRepertoryName(id_local, composeRelativeRep(rootFolder, repertoiredest));
        }

    }

    /**
     * Compose le nom de repertoire relative au rootfolder
     * <p>
     * soustrait le rootfolde rau nom de repertoire pour maj dans la table libraryFolder
     *
     * @param rootFolder
     * @param repertoiredest
     * @return
     */
    public String composeRelativeRep(String rootFolder, String repertoiredest) {
        return repertoiredest.replace(rootFolder, "");
    }

    public void first() {
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
