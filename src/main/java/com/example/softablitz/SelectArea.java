package com.example.softablitz;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.stage.FileChooser;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.core.CvType.CV_8UC4;
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
    protected static ArrayList<MatOfPoint> matOfPointList;
    protected static Mat result;
    protected static Mat mask2;


    public SelectArea(File file, ImageView imageView) throws FileNotFoundException {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            InputStream stream = new FileInputStream(file.getAbsolutePath());
            Image image = new Image(stream);
            ratio1 = image.getHeight() / imageView.getFitHeight();
            ratio2 = image.getWidth() / imageView.getFitWidth();
            matrix = Imgcodecs.imread(file.getAbsolutePath(), 4);
            cvtColor(matrix, matrix, COLOR_BGR2BGRA);
            System.out.println("channels = " + matrix.get(0, 0).length);
            showResult(matrix, imageView);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("An error has been encountered");
            alert.setContentText("Please select Some Image in file option"); //from www.java2s.com
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
            mask = new Mat(matrix.rows(), matrix.cols(), CV_8UC4, new Scalar(0, 0, 0, 0));
            double ratio = Math.max(ratio2, ratio1);
            Point point = new Point(ellipse.getCenterX() * ratio, ellipse.getCenterY() * ratio);
            Size size = new Size(ellipse.getRadiusX() * ratio, ellipse.getRadiusY() * ratio);
            ArrayList<Mat> bgra = new ArrayList<>(4);
            split(mask, bgra);
            ellipse(bgra.get(0), point, size, 0, 0, 360, new Scalar(255), -1, LINE_AA);
            ellipse(bgra.get(1), point, size, 0, 0, 360, new Scalar(255), -1, LINE_AA);
            ellipse(bgra.get(2), point, size, 0, 0, 360, new Scalar(255), -1, LINE_AA);
            ellipse(bgra.get(3), point, size, 0, 0, 360, new Scalar(255), -1, LINE_AA);
            merge(bgra, mask);
        });
    }

    protected static void makeMask(ImageView imageView, File file) throws FileNotFoundException {
        ArrayList<Point> corners = new ArrayList<>();
        double ratio = Math.max(ratio2, ratio1);
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            corners.add(new Point(polygon.getPoints().get(i) * ratio, polygon.getPoints().get(i + 1) * ratio));
        }
        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint.fromList(corners);
        matOfPointList = new ArrayList<>();
        matOfPointList.add(matOfPoint);
        mask = new Mat(matrix.rows(), matrix.cols(), CV_8UC4, new Scalar(0, 0, 0, 0));
        ArrayList<Mat> bgra = new ArrayList<>(4);
        split(mask, bgra);
        drawContours(bgra.get(0), matOfPointList, -1, new Scalar(255), -1, LINE_AA);
        drawContours(bgra.get(1), matOfPointList, -1, new Scalar(255), -1, LINE_AA);
        drawContours(bgra.get(2), matOfPointList, -1, new Scalar(255), -1, LINE_AA);
        drawContours(bgra.get(3), matOfPointList, -1, new Scalar(255), -1, LINE_AA);
        merge(bgra, mask);
    }

    protected static void fourChannels(Mat img) {
        cvtColor(img, img, COLOR_BGR2BGRA);
        Mat gray = new Mat();
        cvtColor(img, gray, COLOR_BGR2GRAY);
        Mat threshed = new Mat();
        threshold(gray, threshed, 127, 255, THRESH_BINARY_INV | THRESH_OTSU);
        Mat kernel = getStructuringElement(MORPH_ELLIPSE, new Size(11, 11));
        Mat morphed = new Mat();
        morphologyEx(threshed, morphed, MORPH_CLOSE, kernel);
        Mat roi = new Mat();
        findContours(morphed, matOfPointList, roi, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        Mat mask = new Mat(img.rows(), img.cols(), img.type());
        fillPoly(mask, matOfPointList, new Scalar(255));
        resize(result, result, result.size());
        bitwise_and(img, mask, result);
    }


    protected static void crop(File file, ImageView imageView, AnchorPane imageViewPane) throws FileNotFoundException {
        result = new Mat();
        bitwise_and(matrix, mask, result);
        showResult(result, imageView);
        imageViewPane.getChildren().remove(rect);
        imageViewPane.getChildren().remove(ellipse);
        imageViewPane.getChildren().remove(polygon);
    }

    protected static ImageView imageView;

    protected static void chooseBackground(AnchorPane imageViewPane) throws FileNotFoundException {
        imageViewPane.getChildren().remove(imageView);
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files", "*.jpg", "*.png", "*.jpeg", "*.jfif");
        fileChooser.getExtensionFilters().add(filter);
        List<File> list = fileChooser.showOpenMultipleDialog(null);
        File x = list.get(0);
        Image image = new Image(new FileInputStream(x.getAbsolutePath()));
        imageView = new ImageView(image);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(imageView);
        ImageFrame.centerFrameImage(imageView, imageViewPane);
        imageViewPane.getChildren().add(anchorPane);
    }

    public static void showResult(Mat img, ImageView imageView) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            Image image = SwingFXUtils.toFXImage(bufImage, null);
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
