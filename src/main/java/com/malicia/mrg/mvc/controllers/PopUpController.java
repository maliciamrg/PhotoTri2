package com.malicia.mrg.mvc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * The type Pop up controller.
 */
public class PopUpController {

    /**
     * The constant retourCode.
     */
    public static final String retourCode = "retourCode";
    /**
     * The constant valstoprun.
     */
    public static final String valstoprun = "stopRun";
    /**
     * The constant valnext.
     */
    public static final String valnext = "next";
    /**
     * The constant valselect.
     */
    public static final String valselect = "select";
    /**
     * The constant idimageOne.
     */
    public static final String idimageOne = "imageOne";
    /**
     * The constant idimage2UL.
     */
    public static final String idimage2UL = "image2UL";
    /**
     * The constant idimage2UR.
     */
    public static final String idimage2UR = "image2UR";
    /**
     * The constant idimage2LL.
     */
    public static final String idimage2LL = "image2LL";
    /**
     * The constant idimage2LR.
     */
    public static final String idimage2LR = "image2LR";
    private static final java.util.logging.Logger LOGGER;
    private static Stage popupStage;

    static {
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
    }

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
    @FXML
    private Label lblinfo;
    private HashMap<String, Object> Result = new HashMap<String, Object>();

    /**
     * Gets popup stage.
     *
     * @return the popup stage
     */
    public static Stage getPopupStage() {
        return popupStage;
    }

    /**
     * Sets popup stage.
     *
     * @param popupStage the popup stage
     */
    public static void setPopupStage(Stage popupStage) {
        PopUpController.popupStage = popupStage;
    }
//
//    /**
//     * Converts an image to another format
//     *
//     * @param inputImagePath  Path of the source image
//     * @param outputImagePath Path of the destination image
//     * @param formatName      the format to be converted to, one of: jpeg, png,
//     *                        bmp, wbmp, and gif
//     * @return true if successful, false otherwise
//     * @throws IOException if errors occur during writing
//     */
//    public static boolean convertFormat(String inputImagePath,
//                                        String outputImagePath, String formatName) throws IOException {
//        FileInputStream inputStream = new FileInputStream(inputImagePath);
//        FileOutputStream outputStream = new FileOutputStream(outputImagePath);
//
//        // reads input image from file
//        BufferedImage inputImage = ImageIO.read(inputStream);
//
//        // writes to the output image in specified format
//        boolean result = ImageIO.write(inputImage, formatName, outputStream);
//
//        // needs to close the streams
//        outputStream.close();
//        inputStream.close();
//
//        return result;
//    }

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
    public HashMap<String, Object> getResult() {
        return Result;
    }

    /**
     * Handle.
     *
     * @param key the key
     */
    @FXML
    public void handle(KeyEvent key) {
        System.out.println("Event handled! : " + key.getCharacter());
        switch (key.getCode()) {
            case RIGHT:
                Result.put(retourCode, valnext);
                closeStage();
                break;
            case DOWN:
                Result.put(retourCode, valselect);
                closeStage();
                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + key.getCode());
        }
    }

    /**
     * Action btn cancel.
     */
    public void ActionBtnCancel() {
        Result.put(retourCode, valstoprun);
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
     * Sets image one.
     *
     * @param src the src
     */
    public void setImageOne(Image src) {
        imageOne.setImage(src);
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
//                throw new IllegalStateException("Unexpected extension: " + ext);
                break;
            case "arw":
//                throw new IllegalStateException("Unexpected extension: " + ext);
                break;
            default:
                File file = new File(src);
                String localUrl = file.toURI().toURL().toString();
                image = new javafx.scene.image.Image(localUrl);


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

//    public BufferedImage convertRenderedImage(RenderedImage img) {
//        if (img instanceof BufferedImage) {
//            return (BufferedImage) img;
//        }
//        ColorModel cm = img.getColorModel();
//        int width = img.getWidth();
//        int height = img.getHeight();
//        WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
//        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
//        Hashtable properties = new Hashtable();
//        String[] keys = img.getPropertyNames();
//        if (keys != null) {
//            for (int i = 0; i < keys.length; i++) {
//                properties.put(keys[i], img.getProperty(keys[i]));
//            }
//        }
//        BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
//        img.copyData(raster);
//        return result;
//    }

}
