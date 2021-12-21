package com.example.softablitz;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgcodecs.Imgcodecs;

public class Hello {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Running FaceDetector\n");
        CascadeClassifier faceDetector = new CascadeClassifier("E:\\InstalledSoftwares\\openCV\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        CascadeClassifier eyeDetector = new CascadeClassifier("E:\\InstalledSoftwares\\openCV\\opencv\\sources\\data\\haarcascades\\haarcascade_eye.xml");
        Mat image = Imgcodecs.imread("E:\\Downloads\\pic1.jpg");
        String faces;
        String eyes;
        MatOfRect faceDetections = new MatOfRect();
        MatOfRect eyeDetections = new MatOfRect();
        Mat face;
        Mat crop = null;
        Mat circles = new Mat();
        faceDetector.detectMultiScale(image, faceDetections);
        System.out.println("face detectino array size: " + faceDetections.toArray().length);
        for (int i = 0; i < faceDetections.toArray().length; i++) {
            faces = "Face" + i + ".png";
            face = image.submat(faceDetections.toArray()[i]);
            crop = face.submat(4, (2 * face.width()) / 3, 0, face.height());
            Imgcodecs.imwrite(faces, face);
            eyeDetector.detectMultiScale(crop, eyeDetections, 1.1, 2, 0, new Size(30, 30), new Size());
            if (eyeDetections.toArray().length == 0) {
                System.out.println(" Not a face" + i);
            } else {
                System.out.println("Face with " + eyeDetections.toArray().length + "eyes");
                for (int j = 0; j < eyeDetections.toArray().length; j++) {
                    System.out.println("Eye");
                    Mat eye = crop.submat(eyeDetections.toArray()[j]);
                    eyes = "Eye" + j + ".png";
                    Imgcodecs.imwrite(eyes, eye);
                }
            }
            System.out.println("end1");
        }
        System.out.println("end2");
    }
}
