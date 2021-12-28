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

        // read Image from file path in imgCodecs.type and store it in matrix variable
        Mat matrix = imgCodecs.imread(file.getAbsolutePath(), imgCodecs.IMREAD_COLOR);
        // clone the matrix in imgout
        Mat imgOut = matrix.clone();
        // make an object of CascadeClassifier and load haarcascade_eye.xml this native library is used
        // in eye detection in some image
        CascadeClassifier cascadeClassifier = new CascadeClassifier("src\\main\\java\\com\\example\\softablitz\\haarcascade_eye.xml");
        // for storing matrix of rectangle
        MatOfRect eyes = new MatOfRect();
        // define the size of matrix after cropping
        Size size = new Size(100, 100);
        // will detect in matrix the eye and store it into the eyes
        cascadeClassifier.detectMultiScale(matrix, eyes, 1.3, 4, 0, size);
        Rect[] eyesArray = eyes.toArray();
        // now loop each eye
        for (int i = 0; i < eyesArray.length; i++) {
            Rect rct = eyesArray[i];
            // extract eye from matrix
            Mat eye = matrix.submat(rct);
            // split the eye matrix in 3 channel (rgb channels)
            ArrayList<Mat> bgr = new ArrayList<>(3);
            Core.split(eye, bgr);

            Mat bg = new Mat();
            // now add the blue and green channel
            Core.add(bgr.get(0), bgr.get(1), bg);
            // and store red channel in r
            Mat r = bgr.get(2);

            Scalar scalar = new Scalar(150);
            Mat submask = new Mat();
            // now check if r channel value is greater than 150
            // and store (r > 150) result in submask;
            Core.compare(r, scalar, submask, Core.CMP_GT);
            Mat submask2 = new Mat();
            // now check if r channel value is greater than b+g
            // and store (r > bg) result in submask2;
            Core.compare(r, bg, submask2, Core.CMP_GT);
            Mat mask = new Mat();
            // now just store result in (submask & submask2 )in mask
            Core.bitwise_and(submask, submask2, mask);

            Mat kernel = new Mat();
            // now just increase the size of mask created i.e. increase the area assigned to mask
            dilate(mask, mask, kernel, new Point(-1, -1), 3, 1, new Scalar(1));

            Mat mean = new Mat();
            // now assign bg/2 to mask
            Core.divide(bg, new Scalar(2), mean);
            // and copy result to mask
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
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
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

//    void fillHoles(Mat mask) {
//        Mat maskFloodfill = mask.clone();
//        Size size = new Size(mask.rows() + 2, mask.cols() + 2);
//        Mat maskTemp = new Mat(size, maskFloodfill.type());
//        floodFill(maskFloodfill, maskTemp, new Point(0, 0), new Scalar(255));
//        Mat mask2 = new Mat(maskFloodfill.size(), maskFloodfill.type());
//        Core.bitwise_not(maskFloodfill, mask2);
//        Core.bitwise_or(mask2, mask, mask);
//    }
}
