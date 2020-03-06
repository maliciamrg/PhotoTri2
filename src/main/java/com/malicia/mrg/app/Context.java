package com.malicia.mrg.app;

import com.malicia.mrg.Main;
import com.malicia.mrg.mvc.controllers.MainFrameController;
import com.malicia.mrg.mvc.models.RequeteSql;
import com.malicia.mrg.mvc.models.SQLiteJDBCDriverConnection;
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
    /**
     * The constant ABSOLUTEPATH.
     */
    public static final String ABSOLUTEPATH = "absolutePath";
    /**
     * The constant CAPTURE_TIME.
     */
    public static final String CAPTURE_TIME = "captureTime";
    /**
     * The constant LC_IDX_FILENAME.
     */
    public static final String LC_IDX_FILENAME = "lc_idx_filename";
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER;
    /**
     * The constant currentContext.
     */
    private static Context currentContext = new Context();
    private static MainFrameController controller;
    private static Stage primaryStage;
    private static String absolutePathFirst;
    private static boolean dryrun = true;
    private static String repertoireNew = "";
    private static String tempsAdherence = "";
    private static String CatalogLrcat____New = "";
    private static String CatalogLrcat__Inbox = "";
    private static String CatalogLrcat___Book = "";
    private static String CatalogLrcat_Legacy = "";
    private static String urlgitwiki = "";
    private static String repBazar = "";
    private static String repRejet = "";
    private static int thresholdBazar = 0;
    private static String kidz = "";
    private static String noDate = "";
    private static List<String> kidsModelList;
    private static SQLiteJDBCDriverConnection lrcat_new;
    private static SQLiteJDBCDriverConnection lrcat_book;
    private static SQLiteJDBCDriverConnection lrcat_legacy;
    private static SQLiteJDBCDriverConnection lrcat_inbox;
    private static String catalogLrcat___kidz;

    /**
     *
     */
    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static SQLiteJDBCDriverConnection getLrcat_new() {
        return lrcat_new;
    }

    public static SQLiteJDBCDriverConnection getLrcat_book() {
        return lrcat_book;
    }

    public static SQLiteJDBCDriverConnection getLrcat_legacy() {
        return lrcat_legacy;
    }

    public static SQLiteJDBCDriverConnection getLrcat_inbox() {
        return lrcat_inbox;
    }

    /**
     * Gets current context.
     *
     * @return the current context
     */
    public static Context getCurrentContext() {
        return currentContext;
    }

    /**
     * Sets current context.
     *
     * @param currentContext the current context
     */
    public static void setCurrentContext(Context currentContext) {
        Context.currentContext = currentContext;
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

        Context.lrcat_new = new SQLiteJDBCDriverConnection(Context.getCatalogLrcat____New());
        Context.lrcat_book = new SQLiteJDBCDriverConnection(Context.getCatalogLrcat___Book());
        Context.lrcat_inbox = new SQLiteJDBCDriverConnection(Context.getCatalogLrcat__Inbox());
        Context.lrcat_legacy = new SQLiteJDBCDriverConnection(Context.getCatalogLrcat_Legacy());
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

        setUrlgitwiki(properties.getProperty("urlgitwiki"));

        setCatalogLrcat____New(properties.getProperty("CatalogLrcat____New"));
        setCatalogLrcat__Inbox(properties.getProperty("CatalogLrcat__Inbox"));
        setCatalogLrcat___Book(properties.getProperty("CatalogLrcat___Book"));
        setCatalogLrcat_Legacy(properties.getProperty("CatalogLrcat_Legacy"));
        setCatalogLrcat___kidz(properties.getProperty("CatalogLrcat___Kidz"));

        setTempsAdherence(properties.getProperty("TempsAdherence"));

        setKidsModelList(Arrays.asList(properties.getProperty("kidzModel").split(",")));

        setRepertoireNew(properties.getProperty("repNew"));
        setRepBazar(properties.getProperty("repBazar"));
        setRepRejet(properties.getProperty("repRejet"));
        setNoDate(properties.getProperty("repNoDate"));
        setKidz(properties.getProperty("repKidz"));

        setThresholdBazar(Integer.parseInt(properties.getProperty("thresholdBazar")));
    }

    /**
     * Gets catalog lrcat.
     *
     * @return the catalog lrcat
     */
    public static String getCatalogLrcat____New() {
        return CatalogLrcat____New;
    }

    /**
     * Sets catalog lrcat.
     *
     * @param catalogLrcat____New the catalog lrcat
     */
    public static void setCatalogLrcat____New(String catalogLrcat____New) {
        Context.CatalogLrcat____New = catalogLrcat____New;
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

    public static String getCatalogLrcat__Inbox() {
        return CatalogLrcat__Inbox;
    }

    public static void setCatalogLrcat__Inbox(String catalogLrcat__Inbox) {
        CatalogLrcat__Inbox = catalogLrcat__Inbox;
    }

    public static String getCatalogLrcat___Book() {
        return CatalogLrcat___Book;
    }

    public static void setCatalogLrcat___Book(String catalogLrcat___Book) {
        CatalogLrcat___Book = catalogLrcat___Book;
    }

    public static String getCatalogLrcat_Legacy() {
        return CatalogLrcat_Legacy;
    }

    public static void setCatalogLrcat_Legacy(String catalogLrcat_Legacy) {
        CatalogLrcat_Legacy = catalogLrcat_Legacy;
    }

    public static String getCatalogLrcat___kidz() {
        return catalogLrcat___kidz;
    }

    public static void setCatalogLrcat___kidz(String catalogLrcat___kidz) {
        Context.catalogLrcat___kidz = catalogLrcat___kidz;
    }
}
