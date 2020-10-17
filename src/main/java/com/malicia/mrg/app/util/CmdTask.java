package com.malicia.mrg.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.models.AgLibrarySubFolder;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Copy all file in C:/Windows
public class CmdTask extends Task<Void> {

    private static final Logger LOGGER = LogManager.getLogger(CmdTask.class);

    private String[] lineArray = new String[10];;

    @Override
    protected Void call() throws Exception {

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", Context.getPostTraitement());
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            this.copy(line);
            LOGGER.debug(line);
        }

        return null;
    }

    private void copy(String line) throws Exception {

        for(int i = 1; i < lineArray.length-1; i++) {
            lineArray[i] = lineArray[i + 1];
        }
        lineArray[lineArray.length-1] = line;

        this.updateMessage(String.join("\n", lineArray));
        Thread.sleep(200);
    }


}