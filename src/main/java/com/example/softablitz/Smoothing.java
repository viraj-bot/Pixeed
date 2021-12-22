package com.example.softablitz;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Smoothing {

    Smoothing() {

    }

    protected void smoothImage(File file, ImageView imageView, double alpha, double beta, double gamma, double sigmax) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat source = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.GaussianBlur(source, destination, new Size(0, 0), sigmax);
        Core.addWeighted(source, alpha, destination, beta, gamma, destination);
        RedEyeCorrection.showResult(destination, imageView);
//        Imgcodecs.imwrite("E://smooth.jpg", destination);
    }

    public byte[] imageToByteArray(Image image) throws IOException {

        BufferedImage bufferedImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(),2);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) image, "jpg", bos );
        return bos.toByteArray();
    }
//    public int[][] ConvertArray(int[] Input, int size)
//    {
//        int[][] Output = new int[(int)(Input.length)][size];
//
//        System.IO.StreamWriter sw = new System.IO.StreamWriter(@"C:\OutFile.txt");
//        for (int i = 0; i < Input.Length; i += size)
//        {
//            for (int j = 0; j < size; j++)
//            {
//                Output[(int)(i / size), j] = Input[i + j];
//                sw.Write(Input[i + j]);
//            }
//            sw.WriteLine("");
//        }
//        sw.Close();
//        return Output;
//    }

    class Solution {
        public int[][] imageSmoother(int[][] M) {
            int row = M.length;
            if (row == 0) return M;
            int width = M[0].length;
            if (width == 0) return M;
            int[][] N = new int[row][width];
            for (int i = 0; i < row; ++i) {
                for (int j = 0; j < width; ++j) {
                    int sum = 0, c = 0;
                    for (int k = Math.max(0, i - 1); k <= Math.min(i + 1, row - 1); k++) {
                        for (int u = Math.max(0, j - 1); u <= Math.min(j + 1, width - 1); u++) {
                            sum += M[k][u];
                            c++;
                        }
                    }
                    N[i][j] = sum / c;
                }
            }
            return N;
        }
    }
}
