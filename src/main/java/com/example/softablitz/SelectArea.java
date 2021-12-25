package com.example.softablitz;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static org.opencv.imgproc.Imgproc.*;

public class SelectArea {
    protected static double mouseDownX;
    protected static double mouseDownY;
    protected static Polygon polygon;
    protected static double ratio1;
    protected static double ratio2;
    protected static Rectangle rect;
    protected static Ellipse ellipse;
    protected static Mat mask;
    protected static Mat matrix;

    public SelectArea(File file, ImageView imageView) throws FileNotFoundException {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            InputStream stream = new FileInputStream(file.getAbsolutePath());
            Image image = new Image(stream);
            ratio1 = image.getHeight() / imageView.getFitHeight();
            ratio2 = image.getWidth() / imageView.getFitWidth();
            matrix = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("An error has been encountered");
            alert.setContentText("Please select Some Image in file option");//from   www  .  ja va  2 s  .com
            alert.showAndWait();
        }
    }

    protected static void makeRectangle(AnchorPane imageViewPane, ImageView imageView, File file) {
        imageViewPane.getChildren().remove(polygon);
        imageViewPane.getChildren().remove(rect);
        imageViewPane.getChildren().remove(ellipse);
        rect = new Rectangle();
        rect.setStroke(Color.BLACK);
        rect.setFill(Color.TRANSPARENT);
        rect.getStrokeDashArray().addAll(5.0, 5.0);
        imageViewPane.getChildren().add(rect);
        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                mouseDownX = e.getScreenX();
                mouseDownY = e.getScreenY();
                rect.setX(mouseDownX);
                rect.setY(mouseDownY);
                rect.setWidth(0);
                rect.setHeight(0);
            }
        });
        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                rect.setX(Math.min(e.getSceneX(), mouseDownX));
                rect.setY(Math.min(e.getSceneY(), mouseDownY));
                rect.setWidth(Math.abs(e.getSceneX() - mouseDownX));
                rect.setHeight(Math.abs(e.getSceneY() - mouseDownY));
            }
        });
        imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                polygon = new Polygon();
                polygon.getPoints().add(rect.getX());
                polygon.getPoints().add(rect.getY());

                polygon.getPoints().add(rect.getX() + rect.getWidth());
                polygon.getPoints().add(rect.getY());

                polygon.getPoints().add(rect.getX() + rect.getWidth());
                polygon.getPoints().add(rect.getY() + rect.getHeight());

                polygon.getPoints().add(rect.getX());
                polygon.getPoints().add(rect.getY() + rect.getHeight());
                try {
                    makeMask(imageView, file);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    protected static void makeLasso(AnchorPane imageViewPane, ImageView imageView, File file) throws FileNotFoundException {
        imageViewPane.getChildren().remove(polygon);
        imageViewPane.getChildren().remove(rect);
        imageViewPane.getChildren().remove(ellipse);
        polygon = new Polygon();
        polygon.setStroke(Color.BLACK);
        polygon.setFill(Color.TRANSPARENT);
        polygon.getStrokeDashArray().addAll(5.0, 5.0);
        imageViewPane.getChildren().add(polygon);
        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                polygon.getPoints().clear();
                polygon.getPoints().add(e.getSceneX());
                polygon.getPoints().add(e.getSceneY());
            }
        });
        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                mouseDownX = e.getSceneX();
                mouseDownY = e.getSceneY();
                polygon.getPoints().add(mouseDownX);
                polygon.getPoints().add(mouseDownY);
            }
        });
        imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    makeMask(imageView, file);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    protected static void makeEllipse(AnchorPane imageViewPane, ImageView imageView) {
        imageViewPane.getChildren().remove(polygon);
        imageViewPane.getChildren().remove(rect);
        imageViewPane.getChildren().remove(ellipse);
        ellipse = new Ellipse();
        ellipse.setStroke(Color.BLACK);
        ellipse.setFill(Color.TRANSPARENT);
        ellipse.getStrokeDashArray().addAll(5.0, 5.0);
        imageViewPane.getChildren().add(ellipse);
        imageView.setOnMousePressed(e -> {
            mouseDownX = e.getSceneX();
            mouseDownY = e.getSceneY();
            ellipse.setCenterX(mouseDownX);
            ellipse.setCenterY(mouseDownY);
            ellipse.setRadiusX(0.0);
            ellipse.setRadiusY(0.0);
        });
        imageView.setOnMouseDragged(e -> {
            ellipse.setCenterX(Math.min(e.getSceneX(), mouseDownX));
            ellipse.setCenterY(Math.min(e.getSceneY(), mouseDownY));
            ellipse.setRadiusX(Math.abs(e.getSceneX() - mouseDownX));
            ellipse.setRadiusY(Math.abs(e.getSceneY() - mouseDownY));
        });
        imageView.setOnMouseReleased(e -> {
            mask = new Mat(matrix.rows(), matrix.cols(), matrix.type());
            double ratio = Math.max(ratio2, ratio1);
            Point point = new Point(ellipse.getCenterX() * ratio, ellipse.getCenterY() * ratio);
            Size size = new Size(ellipse.getRadiusX() * ratio, ellipse.getRadiusY() * ratio);
            ellipse(mask, point, size, 0, 0, 360, new Scalar(255, 255, 255), -1, LINE_AA);
        });
    }

    protected static void makeMask(ImageView imageView, File file) throws FileNotFoundException {
        ArrayList<Point> corners = new ArrayList<>();
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            corners.add(new Point(polygon.getPoints().get(i) * Math.max(ratio2, ratio1), polygon.getPoints().get(i + 1) * Math.max(ratio2, ratio1)));
        }
        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint.fromList(corners);
        ArrayList<MatOfPoint> matOfPointList = new ArrayList<>();
        matOfPointList.add(matOfPoint);

        Mat cropped = matrix.clone();
        mask = new Mat(matrix.rows(), matrix.cols(), cropped.type(), new Scalar(0));
        drawContours(mask, matOfPointList, -1, new Scalar(255, 255, 255), -1, LINE_AA);
    }


    protected static void crop(File file, ImageView imageView, AnchorPane imageViewPane) throws FileNotFoundException {
        try {
            Mat result = matrix.clone();
            resize(mask, mask, matrix.size());
            Core.bitwise_and(matrix, mask, result);
            showResult(result);
            imageViewPane.getChildren().remove(polygon);
            imageViewPane.getChildren().remove(rect);
            imageViewPane.getChildren().remove(ellipse);
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("An error has been encountered");
            alert.setContentText("Please select area to crop");
            alert.showAndWait();
        }
    }

    public static void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
