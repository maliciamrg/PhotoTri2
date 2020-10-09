package com.malicia.mrg.view;

import com.malicia.mrg.mvc.models.ZoneZ;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.Optional;

public class AlertMessageUtil {

    public static void AlertMessage(Stage primaryStage) {
                             // show close dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close post Process");
        alert.setHeaderText("Wait until end of post process");
        alert.initOwner(primaryStage);

        Optional<ButtonType> result = alert.showAndWait();

    }

    /**
     * Popupalert.
     *
     * @param contentText the content text
     * @return
     */
    public static Optional<ButtonType> popupalertConfirmeModification(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("do you confirme ?");
        alert.setContentText(contentText);

        return alert.showAndWait();
    }

    public static void Alertinfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Information Dialog");
        alert.setContentText(msg);
        alert.showAndWait();
    }
    public static Optional<ButtonType> AlertChoixSubfolder(ZoneZ cursubFolderFormatZ, ButtonType[] buttonType) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog with Custom Actions");
        alert.setHeaderText("Choice KeywordMaster for #" + cursubFolderFormatZ.getLocalValue() + "#");
        alert.setContentText("Choose Keyword Master in " + cursubFolderFormatZ.titreZone);
        alert.getButtonTypes().setAll(buttonType);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    public static void popupalert(String contentText, String exceptionText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception Dialog");
        alert.setContentText(contentText);

        javafx.scene.control.Label label = new javafx.scene.control.Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }


}
