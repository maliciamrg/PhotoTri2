package com.malicia.mrg.mvc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Pop up controller.
 */
public class PopUpController {

    /**
     * The constant retourCode.
     */
    public static final String RETOUR_CODE = "retourCode";
    /**
     * The constant valstoprun.
     */
    public static final String VALSTOPRUN = "stopRun";
    /**
     * The constant valnext.
     */
    public static final String VALNEXT = "next";
    /**
     * The constant valselect.
     */
    public static final String VALSELECT = "select";
    /**
     * The constant idimageOne.
     */
    public static final String IMAGE_ONE = "imageOne";
    /**
     * The constant idimage2UL.
     */
    public static final String IMAGE_2_UL = "image2UL";
    /**
     * The constant idimage2UR.
     */
    public static final String IMAGE_2_UR = "image2UR";
    /**
     * The constant idimage2LL.
     */
    public static final String IMAGE_2_LL = "image2LL";
    /**
     * The constant idimage2LR.
     */
    public static final String IMAGE_2_LR = "image2LR";
    private static final java.util.logging.Logger LOGGER;
    private static Stage  popupStage;

    static {
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
    }

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
    @FXML
    private Label lblinfo;
    private HashMap<String, Object> result = new HashMap<>();

    /**
     * Sets popup stage.
     *
     * @param popupStage the popup stage
     */
    public static void setPopupStage(Stage popupStage) {
        PopUpController.popupStage = popupStage;
    }


    /**
     * Sets lblinfo.
     *
     * @param lblinfo the lblinfo
     */
    public void setLblinfo(String lblinfo) {
        this.lblinfo.setText(lblinfo);
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public Map<String, Object> getResult() {
        return result;
    }

    /**
     * Handle.
     *
     * @param key the key
     */
    @FXML
    public void handle(KeyEvent key) {
        switch (key.getCode()) {
            case RIGHT:
                result.put(RETOUR_CODE, VALNEXT);
                closeStage();
                break;
            case DOWN:
                result.put(RETOUR_CODE, VALSELECT);
                closeStage();
                break;
            default:
                break;
        }
    }

    /**
     * Action btn cancel.
     */
    public void actionBtnCancel() {
        result.put(RETOUR_CODE, VALSTOPRUN);
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

    /**
     * Sets image.
     *
     * @param nomimage the nomimage
     * @param src      the src
     * @throws IOException the io exception
     */
    public void setImage(String nomimage, String src) throws IOException {
        LOGGER.info(src);
        String ext = FilenameUtils.getExtension(src).toLowerCase();

        javafx.scene.image.Image image;

        switch (ext) {
            case "avi":
                break;
            case "arw":
                break;
            default:
                File file = new File(src);
                String localUrl = file.toURI().toURL().toString();
                image = new javafx.scene.image.Image(localUrl);


                switch (nomimage) {
                    case IMAGE_ONE:
                        imageOne.setImage(image);
                        break;
                    case IMAGE_2_UL:
                        image2UL.setImage(image);
                        break;
                    case IMAGE_2_UR:
                        image2UR.setImage(image);
                        break;
                    case IMAGE_2_LL:
                        image2LL.setImage(image);
                        break;
                    case IMAGE_2_LR:
                        image2LR.setImage(image);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + nomimage);
                }
        }
    }

}
