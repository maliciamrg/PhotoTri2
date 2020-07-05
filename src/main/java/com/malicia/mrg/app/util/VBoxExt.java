package com.malicia.mrg.app.util;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class VBoxExt extends VBox {

    private ParallelTransition startTransition;
    private ScaleTransition scaleTransition;
    private ScaleTransition startScaleTransition;
    private ScaleTransition initTransition;
    private FadeTransition fadeTransition;
    private SequentialTransition startsSequentialTransition;
    private DoubleProperty expandToMaxProperty;

    public VBoxExt() {
        super();
        init();
    }


    private void init() {
        expandToMaxProperty = new SimpleDoubleProperty(4);

        scaleTransition = new ScaleTransition(Duration.millis(200), this);
        scaleTransition.setCycleCount(1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);

        startScaleTransition = new ScaleTransition(Duration.millis(200), this);
        startScaleTransition.setCycleCount(1);
        startScaleTransition.setInterpolator(Interpolator.EASE_BOTH);

        fadeTransition = new FadeTransition(Duration.millis(200), this);
        fadeTransition.setCycleCount(1);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);

        startTransition = new ParallelTransition();
        startTransition.setCycleCount(2);
        startTransition.setAutoReverse(true);
        startTransition.getChildren().addAll(startScaleTransition, fadeTransition);

        initTransition =
                new ScaleTransition(Duration.millis(200), this);
        initTransition.setToX(1);
        initTransition.setToY(1);
        initTransition.setCycleCount(1);
        initTransition.setInterpolator(Interpolator.EASE_BOTH);

        startsSequentialTransition = new SequentialTransition();
        startsSequentialTransition.getChildren().addAll(startTransition, initTransition);


        setOnMouseEntered((MouseEvent t) -> {
            scaleTransition.setFromX(getScaleX());
            scaleTransition.setFromY(getScaleY());
            scaleTransition.setToX(expandToMaxProperty.get());
            scaleTransition.setToY(expandToMaxProperty.get());
            scaleTransition.playFromStart();
        });

        setOnMouseExited((MouseEvent t) -> {
            scaleTransition.setFromX(getScaleX());
            scaleTransition.setFromY(getScaleY());
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.playFromStart();
        });

        setOnMouseClicked((MouseEvent t) -> {
            startScaleTransition.setFromX(getScaleX());
            startScaleTransition.setFromY(getScaleY());
            startScaleTransition.setToX(2);
            startScaleTransition.setToY(2);
            fadeTransition.setFromValue(1.0f);
            fadeTransition.setToValue(0.5f);
            startsSequentialTransition.playFromStart();
        });
    }

    public DoubleProperty expandToMaxProperty() {
        return expandToMaxProperty;
    }
}