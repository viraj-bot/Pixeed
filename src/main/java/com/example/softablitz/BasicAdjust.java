package com.example.softablitz;

import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

public class BasicAdjust {

    protected JFXSlider brightness,contrast,hue,saturation;
    BasicAdjust(JFXSlider b,JFXSlider c,JFXSlider h,JFXSlider s){
        this.brightness = b;
        this.contrast = c;
        this.hue = h;
        this.saturation = s;
    }
    protected void setSliderListener(ImageView activeImageView){
        ColorAdjust colorAdjust = new ColorAdjust();
        brightness.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setBrightness((brightness.getValue() - 50) / 50);
                activeImageView.setEffect(colorAdjust);
            }
        });
        contrast.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setContrast((contrast.getValue() - 50) /50);
                activeImageView.setEffect(colorAdjust);
            }
        });
        hue.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setHue((hue.getValue() - 50)/ 50);
                activeImageView.setEffect(colorAdjust);
            }
        });
        saturation.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setSaturation((saturation.getValue() - 50) /50);
                activeImageView.setEffect(colorAdjust);
            }
        });
    }

}
