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
import javafx.stage.FileChooser;

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
import java.util.logging.Logger;

/**
 * The type Main frame controller.
 */
public class mainFrameController {

    private static final Logger LOGGER;
    private static int ndDelPhyTotal;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
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


    /**
     * Instantiates a new Main frame controller.
     */
    public mainFrameController() {
        LOGGER.info("mainFrameController");
    }

    /**
     * Regrouper new par adherence.
     * <p>
     * Regrouper toute les photo/vidéo par date +-adhérence dans des répertoires
     * pour faciliter le classement (physique et logique)
     */
    public static void regrouperNetParAdhérences() {
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
     * Select pattern des repertoire photo trier.
     */
    public static void selectPatternDesRepertoirePhotoTrier() {
        LOGGER.info("selectPatternDesRepertoirePhotoTrier");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-selecttioner le pattern pour selectionner les repertoires eligibles");
        LOGGER.info("-a l'appelation RepertoirePhotoTrier");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Purge repertoire vide.
     */
    public static void purgeRepertoireVide() {
        LOGGER.info("purgeRepertoireVide");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-purge les repertoires vide baser sur le root  (physique et logique)");
        LOGGER.info("---------------------------------------------------------------------------");
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
     * Archive un repertoire rejet.
     *
     * @param repertoireRejet the repertoire rejet
     */
    public static void archiveUnRepertoireRejet(String repertoireRejet) {
        LOGGER.info("archiveUnRepertoireRejet");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-archive les photos du repertoire rejet dans un zip dans le repertoire rejet");
        LOGGER.info("-extrait le catalog LRCAT de ligthroom et l'ajoute au zip");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Desarchive un repertoire rejet.
     *
     * @param repertoireRejet the repertoire rejet
     */
    public static void desarchiveUnRepertoireRejet(String repertoireRejet) {
        LOGGER.info("desarchiveUnRepertoireRejet");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-dezip les photos de l'archive rejet");
        LOGGER.info("-dezip le catalog LRCAT rejet dans le catalog ligthroom");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Select script de sauvegarde.
     */
    public static void selectScriptDeSauvegarde() {
        LOGGER.info("selectScriptDeSauvegarde");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-editer le script de sauvegarde");
        LOGGER.info("- +sauvegarde config ligthroom");
        LOGGER.info("- +sauvegarde bibliotheque");
        LOGGER.info("-modifier et sauvegarde dans le properties");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Application de la comande de sauvegarde.
     */
    public static void applicationDeLaComandeDeSauvegarde() {
        LOGGER.info("applicationDeLaSolutionDeSauvegarde");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-execute sur le system la commande de copy sauvegarde");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Sugerrer des repertoire pour le bazar.
     */
    public static void sugerrerDesRepertoirePourLeBazar() {
        LOGGER.info("sugerrerDesRepertoirePourLeBazar");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-pour chaque elmement du bazar sugerrer 3 repertoires possible");
        LOGGER.info("-Pour chaque photo dans le bazar");
        LOGGER.info("-Retrouver dans TOUTE LA BIBLIOTHÈQUE hors bazar");
        LOGGER.info("-4 photos de 4 répertoire différent dans la plage d'adhérence");
        LOGGER.info("-Puis choisir si la photo doit aller dans un de c'est répertoire ou rester dans le bazar");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Sugerrer nom de repertoire.
     *
     * @param repertoire the repertoire
     */
    public static void sugerrerNomDeRepertoire(String repertoire) {
        LOGGER.info("sugerrerNomDeRepertoire");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-Pour tous le repertoireRejet");
        LOGGER.info("-Faire une pop up avec 4 photo du groupe avec 3 input");
        LOGGER.info("-Lieux");
        LOGGER.info("-Évents");
        LOGGER.info("-Personnes");
        LOGGER.info("-Pour constituer un nom de répertoire conforme");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Select limiteur par type repertoire.
     */
    public static void selectLimiteurParTypeRepertoire() {
        LOGGER.info("selectLimiteurParTypeRepertoire");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-editer les limiteur par type de repertoire");
        LOGGER.info("-[NomRepetoire][Nb][Periode]");
        LOGGER.info("-modifier et sauvegarde dans le properties");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Verifier limiteur.
     */
    public static void verifierLimiteur() {
        LOGGER.info("verifierLimiteur");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-pour chaque RepertoirePhotoTrier verifier si le limiteur est atteint");
        LOGGER.info("-editiion du resultat dans un fichier txt et affichage fichier txt ");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Select mot cle de constitutiion repertoire photo trier.
     */
    public static void selectMotCleDeConstitutiionRepertoirePhotoTrier() {
        LOGGER.info("selectMotCleDeConstitutiionRepertoirePhotoTrier");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-editer les mot cle cnstitutant les nom des RepertoirePhotoTrier");
        LOGGER.info("-[1Events][2Lieux][3Personne]");
        LOGGER.info("-modifier et sauvegarde dans le properties");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Renommer les photos en fonctiion du nom du repertoire.
     *
     * @param RepertoirePhotoTrier the repertoire photo trier
     */
    public static void renommerLesPhotosEnFonctiionDuNomDuRepertoire(String RepertoirePhotoTrier) {
        LOGGER.info("renommerLesPhotosEnFonctiionDuNomDuRepertoire");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-renommer les photos du RepertoirePhotoTrier en fonction du nom de ");
        LOGGER.info("-RepertoirePhotoTrier en ajoutant un numero sequentiel (physique et logique)");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Tagger les photos en fonctiion du nom du repertoire.
     *
     * @param RepertoirePhotoTrier the repertoire photo trier
     */
    public static void taggerLesPhotosEnFonctiionDuNomDuRepertoire(String RepertoirePhotoTrier) {
        LOGGER.info("taggerLesPhotosEnFonctiionDuNomDuRepertoire");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-tagger les photos du RepertoirePhotoTrier en fonction du nom de ");
        LOGGER.info("-RepertoirePhotoTrier en ajoutant un numero sequentiel");
        LOGGER.info("---------------------------------------------------------------------------");
    }

    /**
     * Change dry run.
     */
    public void ChangeDryRun() {
        Context.setDryRun(!Context.getDryRun());
        Context.getController().initialize();
    }
    /**
     * Boucle supression repertoire physique boolean.
     *
     * @param dir the dir
     * @return the boolean
     */
    public static boolean boucleSupressionRepertoirePhysique(File dir) {
        boolean returnVal = false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            boolean success = true;
            for (int i = 0; i < children.length; i++) {
                success &= boucleSupressionRepertoirePhysique(new File(dir, children[i]));
            }

            if (success) {
                // The directory is now empty directory free so delete it
                ndDelPhyTotal +=1;
                LOGGER.fine("delete repertory:" + dir.toString());
                returnVal = dir.delete();

            }

        } else {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * Boucle delete repertoire logique.
     *
     * @return
     */
    public static int boucleDeleteRepertoireLogique() {
        int nbdel = 0;
        int nbdeltotal = 0;
        do {
            nbdel = RequeteSql.sqlDeleteRepertory();
            LOGGER.fine("logical delete:" + String.format("%04d", nbdel));
            nbdeltotal += nbdel;
        }
        while (nbdel > 0);
        return nbdeltotal;
    }

    /**
     * Gets imageicon resized.
     *
     * @param imagesJpg the images jpg
     * @return the imageicon resized
     */
    public static ImageIcon getImageiconResized(URL imagesJpg) {
        LOGGER.finest(imagesJpg.toString());
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
     * Move new to grp photos.
     */
    public void movenewtogrpphotos() {
        LOGGER.info("moveNewToGrpPhotos : dryRun = " + Context.getDryRun());
//        RequeteSql.sqlCombineAllGrouplessInGroupByPlageAdherance(Context.getTempsAdherence(), Context.getRepertoireNew());

        java.util.List<GrpPhoto> groupDePhoto = regroupeByNewGroup(Context.getKidsModelList());
        java.util.List<GrpPhoto> groupDePhotoExecpt = exceptNewGroup(groupDePhoto, Context.getKidsModelList());
        if (movetoNewGroup(true, groupDePhotoExecpt) && !Context.getDryRun()) {
            movetoNewGroup(Context.getDryRun(), groupDePhotoExecpt);
//            movetoNewGroup(false,groupDePhoto);
        } else {
            LOGGER.info("movetoNewGroup KO, nothig nmove");
        }
    }

    /**
     * Regroupe by new group java . util . list.
     *
     * @param kidsModelList the kids model list
     * @return the java . util . list
     */
    public static java.util.List<GrpPhoto> regroupeByNewGroup(List<String> kidsModelList) {

//            constitution des groupe

        ResultSet rs = RequeteSql.sqlGroupGrouplessByPlageAdherance(Context.getTempsAdherence());

        GrpPhoto gp = new GrpPhoto();

        java.util.List<GrpPhoto> ggp = new ArrayList();

        try {
            boolean first = true;

            while (rs.next()) {


                // Now we can fetch the data by column name, save and use them!
                String CameraModel = rs.getString("CameraModel");
                if (!kidsModelList.contains(CameraModel)) {
                    CameraModel = " ";
                }
                long captureTime = rs.getLong("captureTime");
                long mint = rs.getLong("mint");
                long maxt = rs.getLong("maxt");
                String src = rs.getString("src");
                String absolutePath = rs.getString("absolutePath");

                if (first) {
                    gp.addfirst(CameraModel, captureTime, mint, maxt, src, absolutePath, Context.getRepertoireNew() + "/");
                } else {
                    if (!gp.add(CameraModel, captureTime, mint, maxt, src)) {
                        ggp.add(gp);
                        gp = new GrpPhoto();
                        gp.addfirst(CameraModel, captureTime, mint, maxt, src, absolutePath, Context.getRepertoireNew() + "/");
                    }
                }
                //}
//                LOGGER.info("\tCameraModel: " + CameraModel +
//                        ", captureTime: " + captureTime +
//                        ", src : " + src);


                first = false;
            }
            ggp.add(gp);


//            LOGGER.info("Nb row lues=> " + nbrow);
//            LOGGER.info("Nb row grp => " + nbele);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ggp;
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
    public static boolean movetoNewGroup(boolean dryRun, List<GrpPhoto> ggp) {
//       Execution du deplacement

        LOGGER.fine("Printing result...");
        int nbele = 0;

        Hashtable codeRetourAction = new Hashtable();

        LOGGER.info((dryRun ? "dryRun =>" : "") + "Nb Groupe Crée " + ggp.size());
        int nbrow = 0;
        for (int i = 0; i < ggp.size(); i++) {
            GrpPhoto gptemp = ggp.get(i);
            nbrow += gptemp.getnbele();

            Hashtable hashRet = gptemp.groupAndMouveEle(dryRun);
            LOGGER.finer("GrpPhoto:" + gptemp.toString());
            LOGGER.finer(" hashRet:" + hashRet.toString());
            if (gptemp.getNomRepetrtoire().compareTo("@Bazar__")==0) {
                LOGGER.info((dryRun ? "dryRun =>" : "") + "Bazar Detail:" + hashRet.toString());
            }
            mergeHashtable(codeRetourAction, hashRet);

        }


        LOGGER.info((dryRun ? "dryRun =>" : "") + codeRetourAction.toString());
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
    @FXML
    public void initialize() {
        LOGGER.info("initialize");

        databaselrcat.setText(Context.getCatalogLrcat());
        lbTempsAdherence.setText("Temps d'adherence : " + Context.getTempsAdherence());
        lbRepertoireNew.setText("Pattern du Repertoire New : " + Context.getRepertoireNew());
        chkDryRun.setSelected(Context.getDryRun());

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
            rootSelected.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                    if ((Integer)number2 >= 0){
                        selectLeRepertoireRootduFichierLigthroom(rootSelected.getItems().get((Integer) number2).toString());
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Context.getPrimaryStage() != null) {
            Context.getPrimaryStage().sizeToScene();
        }
    }

    /**
     * Select le repertoire rootdu fichier ligthroom.
     *
     * @param rootName the root name
     */
    public void selectLeRepertoireRootduFichierLigthroom(String rootName) {
        LOGGER.info("do:selectLeRepertoireRootduFichierLigthroom");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("-selecttioner le repertoire root sur lequelle les actions seront baser ");
        LOGGER.info("-modifier et sauvegarde dans le properties");
        LOGGER.info("---------------------------------------------------------------------------");
        LOGGER.info("absolutePath.get(rootName)" + absolutePath.get(rootName));
        Context.setRoot(absolutePath.get(rootName));
        Context.savePropertiesParameters(Context.currentContext);
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
            LOGGER.info("selectedFile:" + file.getAbsolutePath());
            Context.setCatalogLrcat(file.getAbsolutePath());
        }
        Context.savePropertiesParameters(Context.currentContext);
        Context.getController().initialize();
    }

    /**
     * Delete empty directory.
     * <p>
     * suprimmer tout les repertoires vide (physique et logique)
     *
     * @return
     */
    public int deleteEmptyDirectory() {
        LOGGER.info("deleteEmptyDirectory : DryRun = " + Context.getDryRun());
        if (!Context.getDryRun()) {
            File directory = new File(Context.getAbsolutePathFirst() + Context.getRepertoireNew() + "/");

            ndDelPhyTotal = 0;
            boucleSupressionRepertoirePhysique(directory);
            LOGGER.info("physical delete all from " + directory + " : "  + String.format("%04d", ndDelPhyTotal));

            int ndDelTotal = boucleDeleteRepertoireLogique();
            LOGGER.info("logical delete all :" + String.format("%04d", ndDelTotal));
            return 1 + ndDelTotal;
        }
        return 0;
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


}
