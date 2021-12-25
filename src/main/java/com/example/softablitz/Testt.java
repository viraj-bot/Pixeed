package com.example.softablitz;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static org.opencv.imgproc.Imgproc.*;

public class Testt implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane imageViewPane;

    private double mouseDownX;
    private double mouseDownY;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageView.setImage(new Image(String.valueOf(new File("C:\\Users\\hp\\Desktop\\pic2.jpg"))));
    }
    private Polygon polygon;
    @FXML
    private void crop() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ArrayList<Point> corners = new ArrayList<>();
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            corners.add(new Point(polygon.getPoints().get(i) * Math.max(ratio2, ratio1), polygon.getPoints().get(i + 1) * Math.max(ratio2, ratio1)));
        }
        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint.fromList(corners);
        ArrayList<MatOfPoint> matOfPointList = new ArrayList<>();
        matOfPointList.add(matOfPoint);
        Mat matrix = Imgcodecs.imread("C:\\Users\\hp\\Desktop\\pic2.jpg", Imgcodecs.IMREAD_COLOR);
        Mat mat = Converters.vector_Point_to_Mat(corners);
        Rect rect = boundingRect(mat);
        Mat cropped = matrix.clone();
        Mat mask = new Mat(matrix.rows(), matrix.cols(), cropped.type(), new Scalar(0));
        drawContours(mask, matOfPointList, -1, new Scalar(255, 255, 255), -1, LINE_AA);
        Mat result = cropped.clone();
        Core.bitwise_and(cropped, mask, result);
        showResult(result);
    }

    public void showResult(Mat img) {
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
    double ratio1;
    double ratio2;
    @FXML
    private void select() throws IOException {
        imageViewPane.getChildren().remove(polygon);
        polygon = new Polygon();
        polygon.setStroke(Color.BLACK);
        polygon.setFill(Color.TRANSPARENT);
        polygon.getStrokeDashArray().addAll(5.0, 5.0);
        imageViewPane.getChildren().add(polygon);
        InputStream stream = new FileInputStream("C:\\Users\\hp\\Desktop\\pic2.jpg");
        Image image = new Image(stream);
        ratio1 = image.getHeight() / imageView.getFitHeight();
        ratio2 = image.getWidth() / imageView.getFitWidth();
        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                mouseDownX = e.getX() - imageView.getLayoutX();
                mouseDownY = e.getY() - imageView.getLayoutY();
                polygon.getPoints().add(mouseDownX);
                polygon.getPoints().add(mouseDownY);
            }
        });

    }

}
