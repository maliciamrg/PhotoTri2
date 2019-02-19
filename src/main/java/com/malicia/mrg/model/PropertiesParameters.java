package com.malicia.mrg.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesParameters {

    public final String RepertoireNew;
    public final String PasRepertoirePhoto;
    public final String TempsAdherence;
    public final String CatalogLrcat;

    public PropertiesParameters() throws IOException {

        FileReader reader = new FileReader("resource/config.properties");
        Properties properties = new Properties();
        properties.load(reader);

        RepertoireNew = properties.getProperty("RepertoireNew");
        PasRepertoirePhoto = properties.getProperty("PasRepertoirePhoto");
        TempsAdherence = properties.getProperty("TempsAdherence");
        CatalogLrcat = properties.getProperty("CatalogLrcat");


    }
}
