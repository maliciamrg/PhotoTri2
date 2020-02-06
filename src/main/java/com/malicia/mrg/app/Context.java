package com.malicia.mrg.app;

import com.malicia.mrg.Main;
import com.malicia.mrg.mvc.controllers.mainFrameController;
import com.malicia.mrg.mvc.models.RequeteSql;
import com.malicia.mrg.mvc.models.SQLiteJDBCDriverConnection;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Context {
    private final static Context instance = new Context();
    private static final Logger LOGGER;
    private static mainFrameController controller;
    private static String root;
    private static Stage primaryStage;
    private static String baseDir;
    private static String absolutePathFirst;
    private static boolean dryrun = true;
    private static String repertoireNew = "";
    private static String pasRepertoirePhoto = "";
    private static String tempsAdherence = "";
    private static String catalogLrcat = "";
    private static String Bazar = "";
    private static List<String> kidsModelList;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static String getRoot() {
        return root;
    }

    public static void setRoot(String root) {
        Context.root = root;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Context.primaryStage = primaryStage;
    }

    public static mainFrameController getController() {
        return controller;
    }

    public static void setController(mainFrameController controller) {
        Context.controller = controller;
    }

    public static String getBaseDir() {
        return baseDir;
    }

    public static void setBaseDir(String baseDir) {
        Context.baseDir = baseDir;
    }

    public static String getAbsolutePathFirst() {
        return absolutePathFirst;
    }

    public static void setAbsolutePathFirst(String absolutePathFirst) {
        Context.absolutePathFirst = absolutePathFirst;
    }

    public static Context getInstance() {
        return instance;
    }

    public static String getBazar() {
        return Bazar;
    }

    public static void setBazar(String bazar) {
        Bazar = bazar;
    }

    public static List<String> getKidsModelList() {
        return kidsModelList;
    }

    public static void setKidsModelList(List<String> kidsModelList) {
        Context.kidsModelList = kidsModelList;
    }

    public static String getRepertoireNew() {
        return Context.repertoireNew;
    }

    public static void setRepertoireNew(String repertoireNew) {
        Context.repertoireNew = repertoireNew;
    }

    public static String getPasRepertoirePhoto() {
        return Context.pasRepertoirePhoto;
    }

    public static void setPasRepertoirePhoto(String pasRepertoirePhoto) {
        Context.pasRepertoirePhoto = pasRepertoirePhoto;
    }

    public static String getTempsAdherence() {
        return tempsAdherence;
    }

    public static void setTempsAdherence(String tempsAdherence) {
        Context.tempsAdherence = tempsAdherence;
    }

    public static boolean getDryRun() {
        return Context.dryrun;
    }

    public static void setDryRun(boolean dryRun) {
        Context.dryrun = dryRun;
    }

    public static void setup() {

        // get the global logger to configure it
        Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        InputStream stream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        LOGGER.severe("---==[ severe  ]==---");
        LOGGER.warning("---==[ warning ]==---");
        LOGGER.info("---==[  info   ]==---");
        LOGGER.config("---==[ config  ]==---");
        LOGGER.fine("---==[  fine   ]==---");
        LOGGER.finer("---==[  finer  ]==---");
        LOGGER.finest("---==[ finest  ]==---");

        LOGGER.info("Start");

        Context.initPropertiesParameters();

        SQLiteJDBCDriverConnection.connect(Context.getCatalogLrcat());

        Context.setAbsolutePathFirst(RequeteSql.getabsolutePathFirst());
    }

    public static void initPropertiesParameters() {
        FileReader reader = null;

        try {
            reader = new FileReader("resource/config.properties");
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
        setPasRepertoirePhoto(properties.getProperty("PasRepertoirePhoto"));
        setTempsAdherence(properties.getProperty("TempsAdherence"));
        setCatalogLrcat(properties.getProperty("CatalogLrcat"));
        setDryRun(properties.getProperty("dryRun", "true").compareTo("true") == 0);
        setKidsModelList(Arrays.asList(properties.getProperty("kidzModel").split(",")));
        setBazar(properties.getProperty("repBazar"));
        setBaseDir(properties.getProperty("BaseDir"));
    }

    public static String getCatalogLrcat() {
        return catalogLrcat;
    }

    public static void setCatalogLrcat(String catalogLrcat) {
        Context.catalogLrcat = catalogLrcat;
    }


    public static void savePropertiesParameters() {
        LOGGER.info("savePropertiesParameters");
    }


}
