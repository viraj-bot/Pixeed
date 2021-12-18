package com.example.softablitz;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Filters {


    protected Filters() {

    }

    protected void setRain1Filter(AnchorPane anchorPane) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/rain1.png")));
        ImageView imageView = new ImageView(image);
        AnchorPane anchorPane1 = new AnchorPane();
        anchorPane1.getChildren().add(imageView);
        anchorPane.getChildren().add(anchorPane1);
    }

    protected void setRain2Filter(AnchorPane anchorPane) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/rain2.png")));
        ImageView imageView = new ImageView(image);
        AnchorPane anchorPane1 = new AnchorPane();
        anchorPane1.getChildren().add(imageView);
        anchorPane.getChildren().add(anchorPane1);
    }

    protected void setRain3Filter(AnchorPane anchorPane) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/rain3.png")));
        ImageView imageView = new ImageView(image);
        AnchorPane anchorPane1 = new AnchorPane();
        anchorPane1.getChildren().add(imageView);
        anchorPane.getChildren().add(anchorPane1);
    }
}