package com.malicia.mrg.app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malicia.mrg.app.repertoirePhoto;

import java.io.File;
import java.io.IOException;

public class Serialize {

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileNameIn) {
        fileName = fileNameIn;
    }

    public static void writeJSON(Object o, String FileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FileName), o);
    }

    public static void reWriteJSON(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(((Serialize)o).fileName), o);
    }

    public static Object readJSON(Class aClass, String FileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSubtypes(repertoirePhoto.paramZone.class);
        Object objret = mapper.readValue(new File(FileName), aClass);
        ((Serialize)objret).fileName = FileName;
        return objret ;

    }
}
