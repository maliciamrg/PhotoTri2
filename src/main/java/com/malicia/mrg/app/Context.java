package com.malicia.mrg.app;

import com.malicia.mrg.Main;
import com.malicia.mrg.mvc.controllers.MainFrameController;
import com.malicia.mrg.mvc.controllers.PopUpController;
import com.malicia.mrg.mvc.models.RequeteSql;
import com.malicia.mrg.mvc.models.SQLiteJDBCDriverConnection;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The type Context.
 */
public class Context implements Serializable {

    /**
     * The constant CONTEXT_OBJECTS_TXT.
     */
    public static final String CONTEXT_OBJECTS_TXT = "myContextObjects.txt";
    /**
     * The constant PATH_FROM_ROOT.
     */
    public static final String PATH_FROM_ROOT = "pathFromRoot";
    public static final String ABSOLUTEPATH = "absolutePath";
    /**
     * The constant CAPTURE_TIME.
     */
    public static final String CAPTURE_TIME = "captureTime";
    private static final long serialVersionUID = 1L;
    private static final Context instance = new Context();
    private static final Logger LOGGER;
    /**
     * The constant currentContext.
     */
    public static Context currentContext = new Context();
    private static MainFrameController controller;
    private static String root;
    private static Stage primaryStage;
    private static String baseDir;
    private static String absolutePathFirst;
    private static boolean dryrun = true;
    private static String repertoireNew = "";
    private static String tempsAdherence = "";
    private static String catalogLrcat = "";
    private static String urlgitwiki = "";
    private static String repBazar = "";
    private static String repRejet = "";
    private static String titreRejet = "";
    private static int thresholdBazar = 0;
    private static String kidz = "";
    private static String noDate = "";
    private static List<String> kidsModelList;
    private static PopUpController controllerpopup;
    private static Popup popup;
    private static String repTech;
    private static String repGrp;

    /**
     *
     */
    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    /**
     * Gets rep rejet.
     *
     * @return the rep rejet
     */
    public static String getRepRejet() {
        return repRejet;
    }

    /**
     * Sets rep rejet.
     *
     * @param repRejet the rep rejet
     */
    public static void setRepRejet(String repRejet) {
        Context.repRejet = repRejet;
    }

    /**
     * Sets titre rejet.
     *
     * @param titreRejet the titre rejet
     */
    public static void setTitreRejet(String titreRejet) {
        Context.titreRejet = titreRejet;
    }

    /**
     * Gets threshold bazar.
     *
     * @return the threshold bazar
     */
    public static int getThresholdBazar() {
        return thresholdBazar;
    }

    /**
     * Sets threshold bazar.
     *
     * @param thresholdBazar the threshold bazar
     */
    public static void setThresholdBazar(int thresholdBazar) {
        Context.thresholdBazar = thresholdBazar;
    }

    /**
     * Sets controllerpopup.
     *
     * @param controllerpopup the controllerpopup
     */
    public static void setControllerpopup(PopUpController controllerpopup) {
        Context.controllerpopup = controllerpopup;
    }

    /**
     * Gets kidz.
     *
     * @return the kidz
     */
    public static String getKidz() {
        return kidz;
    }

    /**
     * Sets kidz.
     *
     * @param kidz the kidz
     */
    public static void setKidz(String kidz) {
        Context.kidz = kidz;
    }

    /**
     * Gets no date.
     *
     * @return the no date
     */
    public static String getNoDate() {
        return noDate;
    }

    /**
     * Sets no date.
     *
     * @param noDate the no date
     */
    public static void setNoDate(String noDate) {
        Context.noDate = noDate;
    }

    /**
     * Gets urlgitwiki.
     *
     * @return the urlgitwiki
     */
    public static String getUrlgitwiki() {
        return urlgitwiki;
    }

    /**
     * Sets urlgitwiki.
     *
     * @param urlgitwiki the urlgitwiki
     */
    public static void setUrlgitwiki(String urlgitwiki) {
        Context.urlgitwiki = urlgitwiki;
    }

    /**
     * Sets root.
     *
     * @param root the root
     */
    public static void setRoot(String root) {
        Context.root = root;
    }

    /**
     * Gets primary stage.
     *
     * @return the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sets primary stage.
     *
     * @param primaryStage the primary stage
     */
    public static void setPrimaryStage(Stage primaryStage) {
        Context.primaryStage = primaryStage;
    }

    /**
     * Gets controller.
     *
     * @return the controller
     */
    public static MainFrameController getController() {
        return controller;
    }

    /**
     * Sets controller.
     *
     * @param controller the controller
     */
    public static void setController(MainFrameController controller) {
        Context.controller = controller;
    }

    /**
     * Sets base dir.
     *
     * @param baseDir the base dir
     */
    public static void setBaseDir(String baseDir) {
        Context.baseDir = baseDir;
    }

    /**
     * Gets absolute path first.
     *
     * @return the absolute path first
     */
    public static String getAbsolutePathFirst() {
        return absolutePathFirst;
    }

    /**
     * Sets absolute path first.
     *
     * @param absolutePathFirst the absolute path first
     */
    public static void setAbsolutePathFirst(String absolutePathFirst) {
        Context.absolutePathFirst = absolutePathFirst;
    }

    /**
     * Gets bazar.
     *
     * @return the bazar
     */
    public static String getRepBazar() {
        return repBazar;
    }

    /**
     * Sets bazar.
     *
     * @param repBazar the bazar
     */
    public static void setRepBazar(String repBazar) {
        Context.repBazar = repBazar;
    }

    /**
     * Gets kids model list.
     *
     * @return the kids model list
     */
    public static List<String> getKidsModelList() {
        return kidsModelList;
    }

    /**
     * Sets kids model list.
     *
     * @param kidsModelList the kids model list
     */
    public static void setKidsModelList(List<String> kidsModelList) {
        Context.kidsModelList = kidsModelList;
    }

    /**
     * Gets repertoire new.
     *
     * @return the repertoire new
     */
    public static String getRepertoireNew() {
        return Context.repertoireNew;
    }

    /**
     * Sets repertoire new.
     *
     * @param repertoireNew the repertoire new
     */
    public static void setRepertoireNew(String repertoireNew) {
        Context.repertoireNew = repertoireNew;
    }

    /**
     * Gets temps adherence.
     *
     * @return the temps adherence
     */
    public static String getTempsAdherence() {
        return tempsAdherence;
    }

    /**
     * Sets temps adherence.
     *
     * @param tempsAdherence the temps adherence
     */
    public static void setTempsAdherence(String tempsAdherence) {
        Context.tempsAdherence = tempsAdherence;
    }

    /**
     * Gets dry run.
     *
     * @return the dry run
     */
    public static boolean getDryRun() {
        return Context.dryrun;
    }

    /**
     * Sets dry run.
     *
     * @param dryRun the dry run
     */
    public static void setDryRun(boolean dryRun) {
        Context.dryrun = dryRun;
    }

    /**
     * Sets .
     *
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException           the sql exception
     */
    public static void setup() throws IOException, ClassNotFoundException, SQLException {


        InputStream stream = Context.class.getClassLoader().getResourceAsStream("logging.properties");
        LogManager.getLogManager().readConfiguration(stream);


        LOGGER.severe("---==[ severe  ]==---");
        LOGGER.warning("---==[ warning ]==---");
        LOGGER.info("---==[  info   ]==---");
        LOGGER.config("---==[ config  ]==---");
        LOGGER.fine("---==[  fine   ]==---");
        LOGGER.finer("---==[  finer  ]==---");
        LOGGER.finest("---==[ finest  ]==---");

        LOGGER.info("Start");

        currentContext = Context.loadPropertiesParameters();
        if (currentContext == null) {
            Context.initPropertiesParameters();
        }

        SQLiteJDBCDriverConnection.connect(Context.getCatalogLrcat());
        Context.setAbsolutePathFirst(RequeteSql.getabsolutePathFirst());

    }

    /**
     * Init properties parameters.
     */
    public static void initPropertiesParameters() {
        LOGGER.info("initPropertiesParameters");
        FileReader reader = null;

        try {
            reader = new FileReader(Main.class.getClassLoader().getResource("config.properties").getFile());
        } catch (FileNotFoundException e) {
            LOGGER.severe(e.getMessage());
        }

        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        setRepertoireNew(properties.getProperty("repNew"));
        setTempsAdherence(properties.getProperty("TempsAdherence"));
        setCatalogLrcat(properties.getProperty("CatalogLrcat"));
        setUrlgitwiki(properties.getProperty("urlgitwiki"));
        setDryRun(properties.getProperty("dryRun", "true").compareTo("true") == 0);
        setKidsModelList(Arrays.asList(properties.getProperty("kidzModel").split(",")));
        setRepBazar(properties.getProperty("repBazar"));
        setRepRejet(properties.getProperty("repRejet"));
        setRepTech(properties.getProperty("repTech"));
        setRepGrp(properties.getProperty("repGrp"));
        setThresholdBazar(Integer.parseInt(properties.getProperty("thresholdBazar")));
        setNoDate(properties.getProperty("repNoDate"));
        setKidz(properties.getProperty("repKidz"));
        setBaseDir(properties.getProperty("BaseDir"));
    }

    /**
     * Gets catalog lrcat.
     *
     * @return the catalog lrcat
     */
    public static String getCatalogLrcat() {
        return catalogLrcat;
    }

    /**
     * Sets catalog lrcat.
     *
     * @param catalogLrcat the catalog lrcat
     */
    public static void setCatalogLrcat(String catalogLrcat) {
        Context.catalogLrcat = catalogLrcat;
    }

    /**
     * Save properties parameters.
     *
     * @param ctx the ctx
     * @throws IOException the io exception
     */
    public static void savePropertiesParameters(Context ctx) throws IOException {
        LOGGER.info("savePropertiesParameters");
        try (FileOutputStream f = new FileOutputStream(new File(CONTEXT_OBJECTS_TXT)); ObjectOutputStream o = new ObjectOutputStream(f)) {

            // Write objects to file
            o.writeObject(ctx);

        } catch (FileNotFoundException e) {
            LOGGER.info("File not found");
        } catch (IOException e) {
            LOGGER.info("Error initializing stream");
        }

    }

    /**
     * Load properties parameters context.
     *
     * @return the context
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static Context loadPropertiesParameters() throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(CONTEXT_OBJECTS_TXT));
        ObjectInputStream oi = new ObjectInputStream(fi);
        // Read objects
        return (Context) oi.readObject();
    }

    public static void setRepTech(String repTech) {
        Context.repTech = repTech;
    }

    public static String getRepTech() {
        return repTech;
    }

    public static void setRepGrp(String repGrp) {
        Context.repGrp = repGrp;
    }

    public static String getRepGrp() {
        return repGrp;
    }
}
