package com.malicia.mrg.app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malicia.mrg.app.repertoirePhoto;

import java.io.File;
import java.io.IOException;

public class Serialize {

    public static void writeJSON(Object o, String FileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FileName), o);
    }

    public static Object readJSON(Class aClass, String FileName) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSubtypes(repertoirePhoto.paramZone.class);
        return mapper.readValue(new File(FileName), aClass);

    }
}
