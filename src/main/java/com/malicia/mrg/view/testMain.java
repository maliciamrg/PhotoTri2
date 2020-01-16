package com.malicia.mrg.view;

import com.malicia.mrg.GrpPhoto;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class testMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(GrpPhoto.class.getClassLoader().getResource("test.fxml"));
        primaryStage.setTitle("testte");
        primaryStage.setScene(new Scene(root,300,275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
