package com.malicia.mrg.app;

import com.malicia.mrg.app.util.Serialize;
import com.malicia.mrg.mvc.models.CatalogLrcat;
import com.malicia.mrg.mvc.models.CatalogPreviews;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;


//import static com.malicia.mrg.view.AlertMessageUtil.Alertinfo;
import static com.malicia.mrg.view.AlertMessageUtil.popupalert;

/**
 * The type Context.
 */
public class Context implements Serializable {

    public static List<repertoirePhoto> listRepertoirePhoto = FXCollections.observableArrayList();

    public static final String ID_LOCAL = "id_local";
    public static final String REP_CAT = "repCat";
    public static final String ORDER_BY_ID_LOCAL_DESC = "ORDER by id_local desc ";
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
    private static final Logger LOGGER = LogManager.getLogger(Context.class);
    private static final long serialVersionUID = 1L;
    public static ResourceBundle appParam;
    public static CatalogLrcat lrcat;
    public static Integer divMaxToMinstar;
    public static CatalogPreviews Previews;
    /**
     * The constant currentContext.
     */
    private static Stage primaryStage;
    private static String localVoidPhotoUrl;
    private static String localErr404PhotoUrl;
    private static String localErrPhotoUrl;
    private static String PostTraitement;

    public static String getPostTraitement() {
        return PostTraitement;
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
    public static void setup() throws IOException, SQLException, URISyntaxException, TransformerException, ParserConfigurationException, SAXException {

        InputStream stream = Context.class.getClassLoader().getResourceAsStream("log4j2.properties");
//        LogManager.getLogManager().readConfiguration(stream);


        LOGGER.fatal("                                                                                    ");
        LOGGER.fatal("          <==============================================================>          ");
        LOGGER.fatal("    <===                                                                    ===>    ");
        LOGGER.fatal(" <=====        S T A R T   A P P L I C A T I O N   P H O T O T R I 2         =====> ");
        LOGGER.fatal("    <===                                                                    ===>    ");
        LOGGER.fatal("          <==============================================================>          ");
        LOGGER.fatal("                                                                                    ");
//        OFF	0
//        FATAL	100
//        ERROR	200
//        WARN	300
//        INFO	400
//        DEBUG	500
//        TRACE	600
//        ALL	Integer.MAX_VALUE
        LOGGER.trace("---==[ trace  ]==---");
        LOGGER.debug("---==[ debug ]==---");
        LOGGER.info("---==[  info   ]==---");
        LOGGER.warn("---==[  warn   ]==---");
        LOGGER.error("---==[ error  ]==---");
        LOGGER.fatal("---==[  fatal  ]==---");
        LOGGER.info("Start");

        Context.initPropertiesParameters();

        lrcat = new CatalogLrcat(appParam.getString("RepCatalog") + File.separator + appParam.getString("CatalogLrcat"));
        Previews = new CatalogPreviews(appParam.getString("RepCatalog") + File.separator + appParam.getString("RepPreviews") + File.separator + appParam.getString("CatalogPreviews"));
        PostTraitement = new String(appParam.getString("PostTraitement"));

        divMaxToMinstar = Integer.parseInt(Context.appParam.getString("divMaxToMinstar"));


        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String iconConfigPath = rootPath + "config.properties";
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(iconConfigPath));

        File f = new File(rootPath + "objJson\\");
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return (name.startsWith("repertoirePhoto") && name.endsWith(".json"));
            }
        };
        File[] files = f.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            listRepertoirePhoto.add((repertoirePhoto) Serialize.readJSON(repertoirePhoto.class,  files[i].toString()));
        }

    }


    /**
     * Init properties parameters.
     */
    public static void initPropertiesParameters() throws URISyntaxException, MalformedURLException {
        LOGGER.trace("initPropertiesParameters");

        appParam = ResourceBundle.getBundle("config");
        Enumeration<String> keys = appParam.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = appParam.getString(key);
            LOGGER.debug(key + ": " + value);
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

    public static void excptlog(Exception theException, Logger loggerori) {
        StringWriter stringWritter = new StringWriter();
        PrintWriter printWritter = new PrintWriter(stringWritter, true);
        theException.printStackTrace(printWritter);
        printWritter.flush();
        stringWritter.flush();
        loggerori.fatal("theException = " + "\n" + stringWritter.toString());
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

    public static String fixedLengthString(String string, int length) {
        return String.format("%1$-" + length + "s", string).substring(0, length);
    }

//    public static void logecrireuserlogInfo(String msg) {
//        Alertinfo(msg);
//
//        LOGGER.info(msg);
//    }


}
