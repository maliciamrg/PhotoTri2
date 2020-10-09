package com.malicia.mrg.app;

import com.malicia.mrg.mvc.models.CatalogLrcat;
import com.malicia.mrg.mvc.models.CatalogPreviews;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.malicia.mrg.view.AlertMessageUtil.Alertinfo;
import static com.malicia.mrg.view.AlertMessageUtil.popupalert;

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
    public static Integer divMaxToMinstar;
    private static Logger LOGGER;
    /**
     * The constant currentContext.
     */
    private static Stage primaryStage;
    private static String localVoidPhotoUrl;
    private static String localErr404PhotoUrl;
    private static String localErrPhotoUrl;
    public static CatalogPreviews Previews;

    public static String getPostTraitement() {
        return PostTraitement;
    }

    private static String PostTraitement;

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

        lrcat = new CatalogLrcat(appParam.getString("RepCatalog") + File.separator + appParam.getString("CatalogLrcat"));
        Previews = new CatalogPreviews(appParam.getString("RepCatalog") + File.separator + appParam.getString("RepPreviews") + File.separator + appParam.getString("CatalogPreviews"));
        PostTraitement = new String(appParam.getString("PostTraitement") );

        divMaxToMinstar = Integer.parseInt(Context.appParam.getString("divMaxToMinstar"));

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

    public static void excptlog(Exception theException,java.util.logging.Logger loggerori) {
        StringWriter stringWritter = new StringWriter();
        PrintWriter printWritter = new PrintWriter(stringWritter, true);
        theException.printStackTrace(printWritter);
        printWritter.flush();
        stringWritter.flush();
        loggerori.severe(() -> "theException = " + "\n" + stringWritter.toString());
    }

    public static void popupalertException(Exception ex) {
        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        String contentText = ex.toString();

        popupalert(contentText, exceptionText);

    }

    public static void logecrireuserlogInfo(String msg) {
        Alertinfo(msg);

        LOGGER.info(msg);
    }


}
