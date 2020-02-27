package com.malicia.mrg.mvc.controllers;

import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;

public class PopUpChxRepertoireController {

    public static final String retCancel = "retCancel";
    public static final String retCR = "retCr";
    public static final String valstoprun = "stopRun";
    public static final String valCANCEL = "Cancel";
    public static final String idimageOne = "imageOne";
    public static final String idimage2UL = "image2UL";
    public static final String idimage2UR = "image2UR";
    public static final String idimage2LL = "image2LL";
    public static final String idimage2LR = "image2LR";
    private static final java.util.logging.Logger LOGGER;
    private static Stage popupStage;

    @FXML
    private Button BtnCancel;
    @FXML
    private ImageView imageOne;
    @FXML
    private ImageView image2UL;
    @FXML
    private ImageView image2UR;
    @FXML
    private ImageView image2LL;
    @FXML
    private ImageView image2LR;

    static {
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
    }

    private HashMap<String, Object> Result = new HashMap<String, Object>();;

    public static Stage getPopupStage() {
        return popupStage;
    }

    public static void setPopupStage(Stage popupStage) {
        PopUpChxRepertoireController.popupStage = popupStage;
    }

    public HashMap<String, Object> getResult() {
        return Result;
    }

    public void ActionBtnCancel(){
        Result.put(retCancel,valstoprun);
        closeStage();
    }
    /**
     * Closes the stage of this view
     */
    private void closeStage() {
        if (popupStage != null) {
            popupStage.close();
        }
    }

    public void setImageOne(Image src) {
        imageOne.setImage(src);
    }

    public void setImage(String nomimage , String src) throws MalformedURLException {
        File file = new File(src);
        String localUrl = file.toURI().toURL().toString();
        javafx.scene.image.Image image = new javafx.scene.image.Image(localUrl);
        switch (nomimage) {
            case idimageOne:
                imageOne.setImage(image);
                break;
            case idimage2UL:
                image2UL.setImage(image);
                break;
            case idimage2UR:
                image2UR.setImage(image);
                break;
            case idimage2LL:
                image2LL.setImage(image);
                break;
            case idimage2LR:
                image2LR.setImage(image);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + nomimage);
        }
    }
}
