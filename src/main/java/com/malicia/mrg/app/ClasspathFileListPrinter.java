package com.malicia.mrg.app;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class ClasspathFileListPrinter {
    private URLClassLoader classLoader;

    ClasspathFileListPrinter(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void print() {
        try {
            URL[] urls = classLoader.getURLs();

            for (URL url : urls) {
                printUrl(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printUrl(URL url) throws URISyntaxException, IOException {
        File file = new File(url.toURI());

        if (file.isDirectory()) {
            printDirContent(file);
        } else {
            printJarContent(new JarFile(file));
        }
    }

    private void printJarContent(JarFile jarFile) {
        Enumeration<JarEntry> enumeration = jarFile.entries();
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement().getName());
        }
    }

    private void printDirContent(File dir) throws IOException {
        String[] children = dir.list();
        for (String aChildren : children) {
            visitAllDirsAndFiles(new File(dir, aChildren));
        }
    }

    private void visitAllDirsAndFiles(File file) throws IOException {
        if (file.isDirectory()) {
            printDirContent(file);
        } else {
            System.out.println(file.getCanonicalPath());
        }
    }

}