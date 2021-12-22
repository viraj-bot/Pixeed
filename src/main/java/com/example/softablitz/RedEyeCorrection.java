package com.example.softablitz;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.floodFill;

public class RedEyeCorrection {

    RedEyeCorrection() {

    }

    protected void correctRedEye(File file, Imgcodecs imgCodecs, ImageView imageView) {
        Mat matrix = imgCodecs.imread(file.getAbsolutePath(), imgCodecs.IMREAD_COLOR);
        Mat imgOut = matrix.clone();
        CascadeClassifier cascadeClassifier = new CascadeClassifier("E:\\InstalledSoftwares\\openCV\\opencv\\sources\\data\\haarcascades\\haarcascade_eye.xml");
        MatOfRect eyes = new MatOfRect();
        Size size = new Size(100, 100);
        cascadeClassifier.detectMultiScale(matrix, eyes, 1.3, 4, 0, size);
        Rect[] eyesArray = eyes.toArray();
        if (eyesArray.length == 0) {
            System.out.println("No eye detected");
            return;
        }
        for (int i = 0; i < eyesArray.length; i++) {
            Rect rct = eyesArray[i];
            Mat eye = matrix.submat(rct);
            ArrayList<Mat> bgr = new ArrayList<>(3);
            Core.split(eye, bgr);
            Mat bg = new Mat();
            Core.add(bgr.get(0), bgr.get(1), bg);
            Mat r = bgr.get(2);
            Scalar scalar = new Scalar(150);
            Mat submask = new Mat();
            Core.compare(r, scalar, submask, Core.CMP_GT);
            Mat submask2 = new Mat();
            Core.compare(r, bg, submask2, Core.CMP_GT);
            Mat mask = new Mat();
            Core.bitwise_and(submask, submask2, mask);

//            fillHoles(mask);
            Mat kernel = new Mat();
            dilate(mask, mask, kernel, new Point(-1, -1), 3, 1, new Scalar(1));

            Mat mean = new Mat();
            Core.divide(bg, new Scalar(2), mean);
            mean.copyTo(bgr.get(2), mask);
            mean.copyTo(bgr.get(0), mask);
            mean.copyTo(bgr.get(1), mask);
            Mat eyeout = eye.clone();
            Core.merge(bgr, eyeout);
            eyeout.copyTo(imgOut.submat(eyesArray[i]));
        }
        showResult(imgOut, imageView);
        System.out.println("End");
    }

    public static void showResult(Mat img, ImageView imageView) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            Image image = SwingFXUtils.toFXImage(bufImage, null);
            imageView.setImage(image);
//            JFrame frame = new JFrame();
//            frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
//            frame.pack();
//            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fillHoles(Mat mask) {
        Mat maskFloodfill = mask.clone();
        Size size = new Size(mask.rows() + 2, mask.cols() + 2);
        Mat maskTemp = new Mat(size, maskFloodfill.type());
        floodFill(maskFloodfill, maskTemp, new Point(0, 0), new Scalar(255));
        Mat mask2 = new Mat(maskFloodfill.size(), maskFloodfill.type());
        Core.bitwise_not(maskFloodfill, mask2);
        Core.bitwise_or(mask2, mask, mask);
    }
}
