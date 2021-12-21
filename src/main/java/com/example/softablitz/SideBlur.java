package com.example.softablitz;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class SideBlur {

    protected void VerticalSideBlur(AnchorPane imageViewPane,ImageView imageView) throws FileNotFoundException {

    Image image = imageView.getImage();
    double x = imageView.getLayoutX();
    double y = imageView.getLayoutY();
    double width = imageView.getFitWidth();
    double height = imageView.getFitHeight();

    imageView.setOnMouseMoved(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            System.out.println("x-"+mouseEvent.getX()+",y-"+mouseEvent.getY());
        }
    });

    GaussianBlur gaussianBlur =new GaussianBlur();
    imageView.setEffect(gaussianBlur);

//        Rectangle2D rectangle2D = new Rectangle2D(x+(width*0.15),y,width*0.7,height);
//        imageView.setViewport(rectangle2D);
//        imageView.setLayoutX(x+width*0.15);
//        imageView.setLayoutY(y);
//        imageView.setFitHeight(height);
//        imageView.setFitWidth(0.7*width);
//
   ImageView imageView1 = new ImageView(image);
   imageView1.setLayoutX(x+0.15*width);
   imageView1.setLayoutY(y+0.15*height);
   imageView1.setFitWidth(0.7*width);
   imageView1.setFitHeight(0.7*height);
   imageViewPane.getChildren().add(imageView1);




    }
}
