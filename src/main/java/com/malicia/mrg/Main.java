package com.malicia.mrg;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.controllers.MainFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        Context.setup();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getClassLoader().getResource("mainFrame.fxml"));
        Parent root = loader.load();
        MainFrameController controller = loader.getController();
        controller.first();
        Context.setController(controller);
        Context.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Photo Tri2");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }


}
