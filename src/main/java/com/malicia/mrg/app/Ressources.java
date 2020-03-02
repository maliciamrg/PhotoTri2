package com.malicia.mrg.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Ressources.
 */
public class Ressources {

    /**
     * Gets resource files.
     *
     * @param path the path
     * @return the resource files
     * @throws IOException the io exception
     */
    public static List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    private static InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? Ressources.class.getClassLoader().getResourceAsStream(resource) : in;
    }

    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Listetostring string.
     *
     * @param ele the ele
     * @return the string
     */
    public static String listetostring(List<?> ele) {
        StringBuilder listeletostring = new StringBuilder();
        for (int i = 0; i < ele.size(); i++) {
            listeletostring.append("-->" + ele.get(i) + "\n");
        }
        return listeletostring.toString();
    }
}
