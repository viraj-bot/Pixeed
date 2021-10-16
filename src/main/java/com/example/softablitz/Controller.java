package com.example.softablitz;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;

import javafx.geometry.Rectangle2D;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Slider topToBottomCrop, leftToRightCrop, rightToLeftCrop, bottomToUpCrop;
    @FXML
    private AnchorPane menuBarPane, imageViewPane;
    @FXML
    private Slider brightness;
    @FXML
    private Slider contrast;
    @FXML
    private Slider hue;
    @FXML
    private Slider saturation;

    private ImageView imageView;
    private List<File> list;

    @FXML
    protected void fileButtonClicked() {
        File outputFile = new File("C:/formattedPicture.png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.snapshot(null, null), null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void openFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files", "*.jpg", "*.png", "*.jpeg", "*.jfif");
        fileChooser.getExtensionFilters().add(filter);
        int padding = 10;
        list = fileChooser.showOpenMultipleDialog(null);
        if (list.size() == 1) {
            File x = list.get(0);
            ImageView imageView = new ImageView();
            Image image = new Image((new FileInputStream(x)));
            imageView.setImage(image);
            double ratio = Math.min((double) imageViewPane.getHeight() / image.getHeight(), (double) imageViewPane.getWidth() / image.getWidth());
            imageView.setFitWidth(image.getWidth() * ratio);
            imageView.setFitHeight(image.getHeight() * ratio);
            double wid = imageView.getFitWidth();
            double hig = imageView.getFitHeight();
            imageView.setLayoutX((imageViewPane.getWidth() - wid) / 2 - padding);
            imageView.setLayoutY((imageViewPane.getHeight() - hig) / 2 - padding);
            imageViewPane.getChildren().add(imageView);
        } else {
            double startx = 0;
            double starty = 0;
            Boolean done = false;
            int dex = 0;
            for (File x : list) {
                Image image = new Image((new FileInputStream(x)));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(imageViewPane.getWidth() / 2 - 10);
                imageView.setFitHeight(imageViewPane.getHeight() / 2 - 10);
                imageView.setLayoutX(startx);
                imageView.setLayoutY(starty);
                dex++;
                if (dex == 1) {
                    startx = imageView.getFitWidth() + 10;
                } else if (dex == 2) {
                    startx = 0;
                    starty = imageView.getFitHeight() + 10;
                } else if (dex == 3) {
                    startx = imageView.getFitWidth() + 10;
                    starty = imageView.getFitHeight() + 10;
                }
                imageViewPane.getChildren().add(imageView);
            }
        }
        handleZoom();
    }
    @FXML
    protected void handleZoom() {
        imageView.maxWidth(100);
        imageView.maxHeight(100);
        imageView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 0.95;
                }
                double finalZoomFactor = zoomFactor;

                imageView.setScaleX(imageView.getScaleX() * finalZoomFactor);
                imageView.setScaleY(imageView.getScaleY() * finalZoomFactor);
            }
        });
        imageView.setOnMouseDragEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageViewPane.setCursor(Cursor.OPEN_HAND);
                System.out.println(mouseEvent.getY());
                System.out.println(mouseEvent.getX());
            }
        });

    }

    @FXML
    protected void cropButtonPressed() {
        System.out.println("Crop Button Pressed");
        Double orgWidth = imageView.getFitWidth();
        Double orgHeight = imageView.getFitHeight();


        bottomToUpCrop.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                double x = bottomToUpCrop.getValue() * orgHeight / 100;
                Rectangle2D rectangle2D = new Rectangle2D(0, 0, orgWidth, x);
                imageView.setViewport(rectangle2D);
            }
        });
        topToBottomCrop.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Rectangle2D rectangle2D = new Rectangle2D(0, 0, bottomToUpCrop.getValue() * orgWidth / 100, orgWidth);
                imageView.setViewport(rectangle2D);
            }
        });
        leftToRightCrop.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Rectangle2D rectangle2D = new Rectangle2D(0, 0, 50, 50);
                imageView.setViewport(rectangle2D);
            }
        });
        rightToLeftCrop.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {

            }
        });

    }

    public void centerImage() {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;
            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();
            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }
            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;
            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void adjustHandle() {
        ColorAdjust colorAdjust = new ColorAdjust();
        brightness.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setBrightness(brightness.getValue() / 100);
                imageView.setEffect(colorAdjust);
            }
        });
        contrast.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setContrast(contrast.getValue() / 100);
                imageView.setEffect(colorAdjust);
            }
        });
        hue.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setHue(hue.getValue() / 100);
                imageView.setEffect(colorAdjust);
            }
        });
        saturation.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setSaturation(saturation.getValue() / 100);
                imageView.setEffect(colorAdjust);
            }
        });
    }

    @FXML
    protected void rotate90() {

        imageView.setRotate((90 + imageView.getRotate()));
    }

    @FXML
    protected void rotate180() {

        imageView.setRotate((180 + imageView.getRotate()));
    }

}