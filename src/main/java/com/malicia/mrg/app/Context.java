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

    public static final String CONTEXT_OBJECTS_TXT = "myContextObjects.txt";
    private static final long serialVersionUID = 1L;
    private static final Context instance = new Context();
    private static final Logger LOGGER;
    public static Context currentContext;
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
    private static String bazar = "";

    public static int getThresholdBazar() {
        return thresholdBazar;
    }

    public static void setThresholdBazar(int thresholdBazar) {
        Context.thresholdBazar = thresholdBazar;
    }

    private static int thresholdBazar = 0;
    private static String Kidz = "";
    private static String NoDate = "";
    private static List<String> kidsModelList;
    private static PopUpController controllerpopup;
    private static Popup popup;

    /**
     *
     */
    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static Popup getPopup() {
        return popup;
    }

    public static void setPopup(Popup popup) {
        Context.popup = popup;
    }

    public static PopUpController getControllerpopup() {
        return controllerpopup;
    }

    public static void setControllerpopup(PopUpController controllerpopup) {
        Context.controllerpopup = controllerpopup;
    }

    public static String getKidz() {
        return Kidz;
    }

    public static void setKidz(String kidz) {
        Kidz = kidz;
    }

    public static String getNoDate() {
        return NoDate;
    }

    public static void setNoDate(String noDate) {
        NoDate = noDate;
    }

    public static String getUrlgitwiki() {
        return urlgitwiki;
    }

    public static void setUrlgitwiki(String urlgitwiki) {
        Context.urlgitwiki = urlgitwiki;
    }

    /**
     * Gets root.
     *
     * @return the root
     */
    public static String getRoot() {
        return root;
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
     * Gets base dir.
     *
     * @return the base dir
     */
    public static String getBaseDir() {
        return baseDir;
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
     * Gets instance.
     *
     * @return the instance
     */
    public static Context getInstance() {
        return instance;
    }

    /**
     * Gets bazar.
     *
     * @return the bazar
     */
    public static String getBazar() {
        return bazar;
    }

    /**
     * Sets bazar.
     *
     * @param bazar the bazar
     */
    public static void setBazar(String bazar) {
        Context.bazar = bazar;
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
     */
    public static void setup() {


        try {

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
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

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

        setRepertoireNew(properties.getProperty("RepertoireNew"));
        setTempsAdherence(properties.getProperty("TempsAdherence"));
        setCatalogLrcat(properties.getProperty("CatalogLrcat"));
        setUrlgitwiki(properties.getProperty("urlgitwiki"));
        setDryRun(properties.getProperty("dryRun", "true").compareTo("true") == 0);
        setKidsModelList(Arrays.asList(properties.getProperty("kidzModel").split(",")));
        setBazar(properties.getProperty("repBazar"));
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
     */
    public static void savePropertiesParameters(Context ctx) {
        LOGGER.info("savePropertiesParameters");
        try {
            FileOutputStream f = new FileOutputStream(new File(CONTEXT_OBJECTS_TXT));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(ctx);

            o.close();
            f.close();

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
     */
    public static Context loadPropertiesParameters() {
        try {
            FileInputStream fi = new FileInputStream(new File(CONTEXT_OBJECTS_TXT));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            Context ctx = (Context) oi.readObject();

            oi.close();
            fi.close();

            return ctx;
        } catch (FileNotFoundException e) {
            LOGGER.info("File not found");
        } catch (IOException e) {
            LOGGER.info("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
