package com.example.softablitz;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;
public class SideBlur {
    protected static void verticalSideBlur(AnchorPane imageViewPane, ImageView imageView) throws FileNotFoundException {
        Image image = imageView.getImage();
        double x = imageView.getLayoutX();
        double y = imageView.getLayoutY();
        double width = image.getWidth();
        double height = image.getHeight();
        GaussianBlur gaussianBlur = new GaussianBlur();
        imageView.setEffect(gaussianBlur);
        Rectangle2D rectangle2D = new Rectangle2D(0, 0.2 * height, width, 0.6*height);
        imageView.setViewport(rectangle2D);
        ImageView imageView1 = new ImageView(image);
        imageViewPane.getChildren().add(imageView1);
        imageView1.setLayoutX(x + (0.1 * imageView.getFitWidth()));
        imageView1.setLayoutY(y);
        imageView1.setFitWidth(0.8 * imageView.getFitWidth());
        imageView1.setFitHeight(imageView.getFitHeight());

    }

    protected static void horizonrolSideBlur(AnchorPane imageViewPane, ImageView imageView) throws FileNotFoundException {




        Image image = imageView.getImage();
        double x = imageView.getLayoutX();
        double y = imageView.getLayoutY();
        double width = image.getWidth();
        double height = image.getHeight();
        GaussianBlur gaussianBlur = new GaussianBlur();
        imageView.setEffect(gaussianBlur);
        Rectangle2D rectangle2D = new Rectangle2D(width * 0.2, 0, width * 0.6, height);
        imageView.setViewport(rectangle2D);
        ImageView imageView1 = new ImageView(image);
        imageViewPane.getChildren().add(imageView1);
        imageView1.setLayoutX(x);
        imageView1.setLayoutY(y + (0.1 * imageView.getFitHeight()));
        imageView1.setFitWidth(imageView.getFitWidth());
        imageView1.setFitHeight(0.8 * imageView.getFitHeight());
    }
}
