package com.malicia.mrg.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesParameters {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    private static String repertoireNew = "";
    private static String pasRepertoirePhoto = "";
    private static String tempsAdherence = "";
    private static String catalogLrcat = "";

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


    }


}
