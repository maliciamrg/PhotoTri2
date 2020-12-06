package com.malicia.mrg;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.mvc.controllers.MainFrameController;
import com.malicia.mrg.mvc.controllers.ProgressCmd;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static com.malicia.mrg.view.AlertMessageUtil.alertMessage;

/**
 * The type Main.
 */
public class Main extends Application {

    private static final Logger LOGGER = LogManager.getLogger(MainFrameController.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        try {
            Context.setup();
            launch(args);
        } catch (SQLException e) {

//            LOGGER.info("sqlite acces error :" + e.getMessage());
//            Context.popupalertException(e);
            LOGGER.trace("sqlite acces error :" + e.getMessage());
            Context.excptlog(e, LOGGER);

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Context.setPrimaryStage(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getClassLoader().getResource("mainFrame.fxml"));
        Parent root = loader.load();
        MainFrameController controller = loader.getController();
//        primaryStage.setScene(new Scene(root, 1000, 950));)
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getClassLoader().getResource("caspian.css").toExternalForm());
        primaryStage.setScene(scene);
//        primaryStage.sizeToScene();
        controller.start();
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                // consume event
                event.consume();

                Platform.runLater(new Runnable() {
                    public void run() {
                        new ProgressCmd().start(new Stage());
                    }
                });

                while (true) {
                    alertMessage(primaryStage);

                }
            }
        });

    }

    @Override
    public void stop() throws IOException {
        LOGGER.trace("Stage is closing");
    }

}
