package com.malicia.mrg.app;

import com.malicia.mrg.mvc.models.CatalogLrcat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The type Context.
 */
public class Context implements Serializable {

    /**
     * The constant PATH_FROM_ROOT.
     */
    public static final String PATH_FROM_ROOT = "pathFromRoot";
    /**
     * The constant CAPTURE_TIME.
     */
    public static final String CAPTURE_TIME = "captureTime";
    /**
     * The constant LC_IDX_FILENAME.
     */
    public static final String LC_IDX_FILENAME = "lc_idx_filename";
    public static final String FILE_ID_LOCAL = "file_id_local";
    private static final long serialVersionUID = 1L;
    public static ResourceBundle appParam;
    public static CatalogLrcat lrcat;
    public static HashMap<Integer, String> formatZ = new HashMap();
    private static Logger LOGGER;
    /**
     * The constant currentContext.
     */
    private static Stage primaryStage;
    private static String localVoidPhotoUrl;
    private static String localErr404PhotoUrl;
    private static String localErrPhotoUrl;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static String getLocalErrPhotoUrl() {
        return localErrPhotoUrl;
    }

    public static String getLocalErr404PhotoUrl() {
        return localErr404PhotoUrl;
    }

    public static String getLocalVoidPhotoUrl() {
        return localVoidPhotoUrl;
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
     * Sets .
     *
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public static void setup() throws IOException, SQLException, URISyntaxException {

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

        //array des format de zones
        int numformatZ = 1;
        for (String ssrepformatZ : Context.appParam.getString("ssrepformatZx").split(",")) {
            formatZ.put(numformatZ, ssrepformatZ);
            numformatZ += 1;
        }

        lrcat = new CatalogLrcat(appParam.getString("CatalogLrcat"));

    }

    /**
     * Init properties parameters.
     */
    public static void initPropertiesParameters() throws URISyntaxException, MalformedURLException {
        LOGGER.info("initPropertiesParameters");

        appParam = ResourceBundle.getBundle("config");
        Enumeration<String> keys = appParam.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = appParam.getString(key);
            LOGGER.info(key + ": " + value);
        }

        localVoidPhotoUrl = Objects.requireNonNull(Context.class.getClassLoader().getResource("images.png")).toURI().toURL().toExternalForm();
        localErr404PhotoUrl = Objects.requireNonNull(Context.class.getClassLoader().getResource("err404.jpg")).toURI().toURL().toExternalForm();
        localErrPhotoUrl = Objects.requireNonNull(Context.class.getClassLoader().getResource("error.jpg")).toURI().toURL().toExternalForm();
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

    public static ObservableList<String> getlistofx(String key) {
        if (appParam.containsKey(key)) {
            return FXCollections.observableArrayList(appParam.getString(key).split(","));
        } else {
            ObservableList<String> ret = FXCollections.observableArrayList();
            String[] decript = key.split("%");
            if (decript.length > 1) {
                if (appParam.containsKey(decript[1])) {
                    return FXCollections.observableArrayList(appParam.getString(decript[1]).split(","));
                } else {
                    ret.add(key);
                }
            } else {
                ret.add(key);
            }
            return ret;
        }
    }
}
