package com.malicia.mrg.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesParameters {

    private static final Logger LOGGER;
    private static boolean dryrun = true;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    private static String repertoireNew = "";
    private static String pasRepertoirePhoto = "";
    private static String tempsAdherence = "";
    private static String catalogLrcat = "";

    public static String getBazar() {
        return Bazar;
    }

    public static void setBazar(String bazar) {
        Bazar = bazar;
    }

    private static String Bazar = "";

    public static List<String> getKidsModelList() {
        return kidsModelList;
    }

    public static void setKidsModelList(List<String> kidsModelList) {
        PropertiesParameters.kidsModelList = kidsModelList;
    }

    private static List<String> kidsModelList ;

    public static String getRepertoireNew() {
        return repertoireNew;
    }

    public static void setRepertoireNew(String repertoireNew) {
        PropertiesParameters.repertoireNew = repertoireNew;
    }

    public static String getPasRepertoirePhoto() {
        return pasRepertoirePhoto;
    }

    public static void setPasRepertoirePhoto(String pasRepertoirePhoto) {
        PropertiesParameters.pasRepertoirePhoto = pasRepertoirePhoto;
    }

    public static String getTempsAdherence() {
        return tempsAdherence;
    }

    public static void setTempsAdherence(String tempsAdherence) {
        PropertiesParameters.tempsAdherence = tempsAdherence;
    }

    public static String getCatalogLrcat() {
        return catalogLrcat;
    }

    public static void setCatalogLrcat(String catalogLrcat) {
        PropertiesParameters.catalogLrcat = catalogLrcat;
    }


    private PropertiesParameters() {
        throw new IllegalStateException("Utility class");
    }

    public static void initPropertiesParameters()  {
        FileReader reader = null;

        try {
            reader = new FileReader("resource/config.properties");
        } catch (FileNotFoundException e) {
            LOGGER.severe( e.getMessage());
        }

        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            LOGGER.severe( e.getMessage());
        }

        setRepertoireNew( properties.getProperty("RepertoireNew"));
        setPasRepertoirePhoto(properties.getProperty("PasRepertoirePhoto"));
        setTempsAdherence(properties.getProperty("TempsAdherence"));
        setCatalogLrcat(properties.getProperty("CatalogLrcat"));
        setDryrun(properties.getProperty("dryRun","true").compareTo("true")==0);
        setKidsModelList(Arrays.asList(properties.getProperty("kidzModel").split(",")));
        setBazar(properties.getProperty("repBazar"));
    }


    public static boolean getDryRun() {
        return PropertiesParameters.dryrun ;
    }

    public static void setDryrun(boolean dryrun) {
        PropertiesParameters.dryrun = dryrun;
    }


}
