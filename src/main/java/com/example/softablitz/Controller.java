package com.example.softablitz;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
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
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private Slider topToBottomCrop, leftToRightCrop, rightToLeftCrop, bottomToUpCrop;
    @FXML
    private AnchorPane menuBarPane;
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
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);
        for (File x : fileChooser.showOpenMultipleDialog(null)) {
            imageView.setImage(new Image((new FileInputStream(x))));
        }
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
                imageView.setScaleX(imageView.getScaleX() * zoomFactor);
                imageView.setScaleY(imageView.getScaleY() * zoomFactor);
                event.consume();
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
                System.out.println(bottomToUpCrop.getValue());
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
}
