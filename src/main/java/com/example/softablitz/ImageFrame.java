package com.example.softablitz;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class ImageFrame {
    protected static void openFile(ImageView imageView) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files", "*.jpg", "*.png", "*.jpeg", "*.jfif");
        fileChooser.getExtensionFilters().add(filter);
        List<File> list = fileChooser.showOpenMultipleDialog(null);
        File x = list.get(0);
        Image image = new Image((new FileInputStream(x)));
        imageView.setImage(image);
    }

    public static void centerImage(ImageView imageView, AnchorPane anchorPane) {
        double hig = imageView.getFitHeight();
        double wid = imageView.getFitWidth();
        imageView.setLayoutX((anchorPane.getPrefWidth() - wid) / 2);
        imageView.setLayoutY((anchorPane.getPrefHeight() - hig) / 2);
    }

    protected static void addResizeButton(AnchorPane anchorPane, ImageView imageView) throws FileNotFoundException {
        javafx.scene.control.Button button = new Button();
        ImageView buttonImage = new ImageView(new Image(new FileInputStream
                ("src/main/resources/com/example/softablitz/icons/resizeIcon.png")));
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(100);
        buttonImage.setEffect(colorAdjust);
        buttonImage.setFitWidth(15);
        buttonImage.setFitHeight(15);
        button.setGraphic(buttonImage);
        button.setStyle("-fx-background-color: transparent");
        button.setLayoutX(anchorPane.getPrefWidth() - 20);
        anchorPane.getChildren().add(button);
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                anchorPane.setCursor(Cursor.MOVE);
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                anchorPane.setCursor(Cursor.DEFAULT);
            }
        });

        button.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setLayoutX(mouseEvent.getSceneX() - 410);
                button.setLayoutY(mouseEvent.getSceneY() - 150);
                if (mouseEvent.getSceneX() <= anchorPane.getPrefWidth()) {
                    imageView.setFitWidth(mouseEvent.getSceneX());
                }
                if (mouseEvent.getSceneY() <= anchorPane.getPrefHeight()) {
                    imageView.setFitHeight(mouseEvent.getScreenY());
                }
                centerImage(imageView, anchorPane);
            }
        });
    }

    public static double centerFrameImage(ImageView imageView, AnchorPane anchorPane) {
        Image image = imageView.getImage();
        double ratio = Math.min((double) anchorPane.getHeight() / image.getHeight(), (double) anchorPane.getWidth() / image.getWidth());
        imageView.setFitWidth(image.getWidth() * ratio);
        imageView.setFitHeight(image.getHeight() * ratio);
        double wid = imageView.getFitWidth();
        double hig = imageView.getFitHeight();
        int padding = 0;
        imageView.setLayoutX((anchorPane.getWidth() - wid) / 2 - padding);
        imageView.setLayoutY((anchorPane.getHeight() - hig) / 2 - padding);
        anchorPane.getChildren().add(imageView);
        return ratio;
    }

    protected static void addCollageFrame(AnchorPane imageViewPane, AnchorPane anchorPane, double startx, double starty, double width, double height) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/selection.jpg")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        anchorPane.setLayoutX(startx);
        anchorPane.setLayoutY(starty);
        anchorPane.setPrefHeight(imageView.getFitHeight());
        anchorPane.setPrefWidth(imageView.getFitWidth());
        anchorPane.getChildren().add(imageView);
        imageViewPane.getChildren().add(anchorPane);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    openFile(imageView);
                    imageView.toBack();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        addResizeButton(anchorPane, imageView);
    }

    protected static void SETFRAME1(AnchorPane imageViewPane) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame1.jpg")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        addCollageFrame(imageViewPane, anchorPane, ratio * 1770 + imageView.getLayoutX(), 1355 * ratio + imageView.getLayoutY(), 745 * ratio, 1090 * ratio);
    }


    protected static void SETFRAME2(AnchorPane imageViewPane) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame2.png")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(imageViewPane, anchorPane, ratio * 315 + imageView.getLayoutX(), 80 * ratio + imageView.getLayoutY(), 425 * ratio, 595 * ratio);


    }


    protected static void SETFRAME3(AnchorPane imageViewPane) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame3.png")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(imageViewPane, anchorPane, ratio * 125 + imageView.getLayoutX(), 110 * ratio + imageView.getLayoutY(), 545 * ratio, 350 * ratio);

    }


    protected static void SETFRAME4(AnchorPane imageViewPane) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame4.png")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(imageViewPane, anchorPane, ratio * 300 + imageView.getLayoutX(), 330 * ratio + imageView.getLayoutY(), 1320 * ratio, 800 * ratio);

    }


    protected static void SETFRAME5(AnchorPane imageViewPane) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame5.jpg")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1080 + " " + 605 * ratio + " " + 5055 * ratio + " " + 1925 * ratio);
        addCollageFrame(imageViewPane, anchorPane, ratio * 1800 + imageView.getLayoutX(), 605 * ratio + imageView.getLayoutY(), 1380 * ratio, 1925 * ratio);

    }

    protected static void SETFRAME6(AnchorPane imageViewPane) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame6.jpg")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(imageViewPane, anchorPane, ratio * 1650 + imageView.getLayoutX(), 945 * ratio + imageView.getLayoutY(), 1130 * ratio, 1865 * ratio);
        imageViewPane.toFront();

    }
    // frame1 : 1770 1355 , 2515  2445
// frame3 : 125 110  , 670 460
// frame4 : 300 330  , 1620 1130
// frame5 : 1800 605 ,  3180 2530
// frame6 : 1650 945 , 2780 2810
// frame2 : 315  80  , 740  675
}
