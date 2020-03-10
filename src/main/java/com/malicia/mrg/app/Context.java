package com.malicia.mrg.app;

import com.malicia.mrg.mvc.controllers.MainFrameController;
import com.malicia.mrg.mvc.models.AgLibraryRootFolder;
import com.malicia.mrg.mvc.models.CatalogLrcat;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
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
    public static ResourceBundle appParam;
    public static CatalogLrcat lrcat;
    public static AgLibraryRootFolder repLegacy;
    public static AgLibraryRootFolder repbookEvents;
    public static AgLibraryRootFolder repbookHolidays;
    public static AgLibraryRootFolder repbookShooting;
    public static AgLibraryRootFolder repEncours;
    public static AgLibraryRootFolder repKidz;
    public static AgLibraryRootFolder repNew;
    private static Logger LOGGER;
    /**
     * The constant currentContext.
     */
    private static MainFrameController controller;
    private static Stage primaryStage;
    private static HashMap<String, String> lrcatSource = new HashMap();

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
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
     * Sets .
     *
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException           the sql exception
     */
    public static void setup() throws IOException,  SQLException {

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

        Context.initPropertiesParameters();

        lrcat = new CatalogLrcat(appParam.getString("CatalogLrcat"));

    }

    /**
     * Init properties parameters.
     */
    public static void initPropertiesParameters() {
        LOGGER.info("initPropertiesParameters");

        appParam = ResourceBundle.getBundle("config");
        Enumeration<String> keys = appParam.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = appParam.getString(key);
            LOGGER.info(key + ": " + value);
        }

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


    /**
     * Gets lrcat source.
     *
     * @return the lrcat source
     */
    public static HashMap getLrcatSource() {
        return lrcatSource;
    }

    /**
     * Sets lrcat source.
     *
     * @param lrcatSource the lrcat source
     */
    public static void setLrcatSource(HashMap lrcatSource) {
        Context.lrcatSource = lrcatSource;
    }

    public static List<String> getKidsModelList() {
        return Arrays.asList(appParam.getString("listeModelKidz").split(","));
    }

    public static int getThresholdBazar() {
        return Integer.parseInt(appParam.getString("thresholdBazar"));
    }

    public static String getUrlgitwiki() {
        return Context.appParam.getString("urlgitwiki");
    }
}
