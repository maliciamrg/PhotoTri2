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

    public String RepertoireNew = "";
    public String PasRepertoirePhoto = "";
    public String TempsAdherence = "";
    public String CatalogLrcat = "";

    public PropertiesParameters() {
        FileReader reader = null;
        try {

            reader = new FileReader("resource/config.properties");

            Properties properties = new Properties();
            properties.load(reader);

            RepertoireNew = properties.getProperty("RepertoireNew");
            PasRepertoirePhoto = properties.getProperty("PasRepertoirePhoto");
            TempsAdherence = properties.getProperty("TempsAdherence");
            CatalogLrcat = properties.getProperty("CatalogLrcat");

        } catch (IOException e) {
            LOGGER.severe( e.getMessage());
        }

    }


}
