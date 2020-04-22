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

    private static final java.util.logging.Logger LOGGER;

    static {
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws  IOException, URISyntaxException {
        try {
            Context.setup();
            launch(args);
        } catch (SQLException e) {

//            Context.logecrireuserlogInfo("sqlite acces error :" + e.getMessage());
//            Context.popupalertException(e);
            LOGGER.info("sqlite acces error :" + e.getMessage());
            Context.excptlog(e,LOGGER);

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Context.setPrimaryStage(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getClassLoader().getResource("mainFrame.fxml"));
        Parent root = loader.load();
        MainFrameController controller = loader.getController();
//        primaryStage.setScene(new Scene(root, 1000, 950));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getClassLoader().getResource("caspian.css").toExternalForm());
        primaryStage.setScene(scene);
//        primaryStage.sizeToScene();
        controller.start();
        primaryStage.show();

    }

}
