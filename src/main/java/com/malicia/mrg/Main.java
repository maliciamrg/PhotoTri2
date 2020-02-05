package com.malicia.mrg;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.photo.GrpPhoto;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application {

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(GrpPhoto.class.getClassLoader().getResource("com/malicia/mrg/mvc/views/mainFrame.fxml"));
        primaryStage.setTitle("testte");
        primaryStage.setScene(new Scene(root,300,275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Context.setup();
        launch(args);
    }

    private static final Logger LOGGER;


}
