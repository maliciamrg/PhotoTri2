package com.malicia.mrg;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.controllers.MainFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * The type Main.
 */
public class Main extends Application {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, URISyntaxException {
        Context.setup();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Context.setPrimaryStage(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getClassLoader().getResource("mainFrame.fxml"));
        Parent root = loader.load();
        MainFrameController controller = loader.getController();
        Context.setController(controller);
        primaryStage.setScene(new Scene(root, 1170, 850));
        controller.start();
        primaryStage.show();

    }

}
