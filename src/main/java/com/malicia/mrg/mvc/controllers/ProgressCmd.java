package com.malicia.mrg.mvc.controllers;

import java.io.File;
import java.util.List;

import com.malicia.mrg.app.util.CmdTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.soap.Text;

public class ProgressCmd extends Application {

    private CmdTask cmdTask;

    @Override
    public void start(Stage primaryStage) {

        final Label label = new Label("CmdTask:");

        final Button startButton = new Button("Start");
        final Button cancelButton = new Button("Cancel");

        final TextArea statusText = new TextArea();
        final CheckBox doPostTraitement = new CheckBox("doPostTraitement");
//        final Label statusLabelm1 = new Label();
        statusText.setMinWidth(400);
//        statusText.setTextFill(Color.BLUE);
//        statusLabelm1.setMinWidth(250);
//        statusLabelm1.setTextFill(Color.BLUE);

        // Start Button.
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startButton.setDisable(true);
                cancelButton.setDisable(false);

                // Create a Task.
                cmdTask = new CmdTask();

                // Unbind text property for Label.
                statusText.textProperty().unbind();
                // Unbind text property for Label.
//                statusLabelm1.textProperty().unbind();

                // Bind the text property of Label
                // with message property of Task
                statusText.textProperty().bind(cmdTask.messageProperty());
                // Bind the text property of Label
                // with message property of Task
//                statusLabelm1.textProperty().bind(cmdTask.messagePropertyM1());

                // When completed tasks
                cmdTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                        new EventHandler<WorkerStateEvent>() {

                            @Override
                            public void handle(WorkerStateEvent t) {
                                closeAll(statusText);
                            }
                        });

                // Start the Task.
                new Thread(cmdTask).start();

            }
        });

        // Cancel
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startButton.setDisable(false);
                cancelButton.setDisable(true);
                if (cmdTask!=null) {
                    cmdTask.cancel(true);
                }
                closeAll(statusText);
            }
        });

        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);

        root.getChildren().addAll(label, //
 //               statusLabelm1,
                statusText, startButton, cancelButton, doPostTraitement);

        Scene scene = new Scene(root, 800, 300, Color.WHITE);
        primaryStage.setTitle("Progress CmdTask");
        primaryStage.setScene(scene);
        primaryStage.show();

        startButton.setDisable(true);
        doPostTraitement.setSelected(true);

        final Timeline animation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {
                            private int i = 10 ;
                            @Override public void handle(ActionEvent actionEvent) {
                                startButton.setText("Start in "+i--);
                                if (i==0){
                                    if (doPostTraitement.isSelected()) {
                                        startButton.setDisable(false);
                                        startButton.fire();
                                    } else {
                                        cancelButton.fire();
                                    }
                                }
                            }
                        }));
        animation.setCycleCount(10);
        animation.play();
    }

    public void closeAll(TextArea statusText) {
        statusText.textProperty().unbind();
        Platform.exit();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}