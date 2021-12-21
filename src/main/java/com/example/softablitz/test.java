package com.example.softablitz;


import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.embed.swing.SwingFXUtils;

import java.awt.font.ImageGraphicAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import javafx.geometry.Rectangle2D;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static org.opencv.core.Core.bitwise_not;
import static org.opencv.core.Core.bitwise_or;
import static org.opencv.imgproc.Imgproc.*;


public class test implements Initializable {


    private ImageView imageView;
    @FXML
    private Slider topToBottomCrop, leftToRightCrop, rightToLeftCrop, bottomToUpCrop;
    @FXML
    private AnchorPane menuBarPane, imageViewPane;
    @FXML
    private Slider brightness;
    @FXML
    private Slider contrast;
    @FXML
    private Slider hue;
    @FXML
    private Slider saturation;
    @FXML
    private TextField brushSize;
    @FXML
    private CheckBox eraser;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private int x, y, height, width;
    @FXML
    private Canvas canvas;
    Stack<AnchorPane> stk;
    private File file;
    private Size size;

    @FXML
    protected void fileButtonClicked() throws IOException {
        WritableImage image = imageViewPane.snapshot(new SnapshotParameters(), null);
        File file = new File("D:\\anchor.png");
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }

    @FXML
    protected void onExit() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void openFile() throws FileNotFoundException {
        imageViewPane.getChildren().removeAll();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files", "*.jpg", "*.png", "*.jpeg", "*.jfif");
        fileChooser.getExtensionFilters().add(filter);
        int padding = 10;
        List<File> list = fileChooser.showOpenMultipleDialog(null);
        if (list.size() == 1) {
            File x = list.get(0);
            file = x;
            ImageView imageView1 = new ImageView();
            Image image = new Image((new FileInputStream(x)));
            imageView1.setImage(image);
            double ratio = Math.min((double) imageViewPane.getHeight() / image.getHeight(), (double) imageViewPane.getWidth() / image.getWidth());
            imageView1.setFitWidth(image.getWidth() * ratio);
            imageView1.setFitHeight(image.getHeight() * ratio);
            double wid = imageView1.getFitWidth();
            double hig = imageView1.getFitHeight();
            imageView1.setLayoutX((imageViewPane.getWidth() - wid) / 2 - padding);
            imageView1.setLayoutY((imageViewPane.getHeight() - hig) / 2 - padding);
            imageViewPane.getChildren().add(imageView1);
            imageView = imageView1;
        } else {
            double startx = 0;
            double starty = 0;
            Boolean done = false;
            int dex = 0;
            for (File x : list) {
                Image image = new Image((new FileInputStream(x)));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(imageViewPane.getWidth() / 2 - 10);
                imageView.setFitHeight(imageViewPane.getHeight() / 2 - 10);
                imageView.setLayoutX(startx);
                imageView.setLayoutY(starty);
                dex++;
                if (dex == 1) {
                    startx = imageView.getFitWidth() + 10;
                } else if (dex == 2) {
                    startx = 0;
                    starty = imageView.getFitHeight() + 10;
                } else if (dex == 3) {
                    startx = imageView.getFitWidth() + 10;
                    starty = imageView.getFitHeight() + 10;
                }
                imageViewPane.getChildren().add(imageView);
            }
        }
        handleZoom();
        imageViewPane.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Dragboard db = imageViewPane.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
            }
        });
    }

    protected void openFile(ImageView imageView) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files", "*.jpg", "*.png", "*.jpeg", "*.jfif");
        fileChooser.getExtensionFilters().add(filter);
        List<File> list = fileChooser.showOpenMultipleDialog(null);
        File x = list.get(0);
        Image image = new Image((new FileInputStream(x)));
        imageView.setImage(image);
    }

    @FXML
    protected void handleZoom() {
        imageView.maxWidth(100);
        imageView.maxHeight(100);
        imageView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 0.95;
                }
                double finalZoomFactor = zoomFactor;

                imageView.setScaleX(imageView.getScaleX() * finalZoomFactor);
                imageView.setScaleY(imageView.getScaleY() * finalZoomFactor);
            }
        });
        imageView.setOnMouseDragEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageViewPane.setCursor(Cursor.OPEN_HAND);
                System.out.println(mouseEvent.getY());
                System.out.println(mouseEvent.getX());
            }
        });

    }

    @FXML

    protected void zoomIn() {
        imageView.maxWidth(100);
        imageView.maxHeight(100);
        double finalZoomFactor = 1.05;
        imageView.setScaleX(imageView.getScaleX() * finalZoomFactor);
        imageView.setScaleY(imageView.getScaleY() * finalZoomFactor);
    }

    @FXML
    protected void zoomOut() {
        imageView.maxWidth(100);
        imageView.maxHeight(100);
        double finalZoomFactor = 0.95;
        imageView.setScaleX(imageView.getScaleX() * finalZoomFactor);
        imageView.setScaleY(imageView.getScaleY() * finalZoomFactor);
    }

    public void centerImage(ImageView imageView, AnchorPane anchorPane) {
        double hig = imageView.getFitHeight();
        double wid = imageView.getFitWidth();
        imageView.setLayoutX((anchorPane.getPrefWidth() - wid) / 2);
        imageView.setLayoutY((anchorPane.getPrefHeight() - hig) / 2);
        System.out.println(anchorPane.getPrefWidth() + " " + wid / 2);
    }

    protected void addCollageFrame(AnchorPane anchorPane, int startx, int starty, double width, double height) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/selection.jpg")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width - 20);
        imageView.setFitHeight(height - 15);
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
                    System.out.println("clicked1");
                    openFile(imageView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        addResizeButton(anchorPane, imageView);
    }

    protected void addResizeButton(AnchorPane anchorPane, ImageView imageView) {
        Button button = new Button();
        button.resize(40, 40);
        anchorPane.getChildren().add(button);
        button.setLayoutX(anchorPane.getPrefWidth() - 30);
        button.setStyle("-fx-background-color: red");
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
                button.setLayoutX(mouseEvent.getSceneX() - 20);
                button.setLayoutY(mouseEvent.getSceneY() - 50);
                if (mouseEvent.getSceneX() <= anchorPane.getPrefWidth()) {
                    imageView.setFitWidth(mouseEvent.getSceneX());
                }
                if (mouseEvent.getSceneY() <= anchorPane.getPrefHeight()) {
                    imageView.setFitHeight(mouseEvent.getSceneY());
                }
                centerImage(imageView, anchorPane);
            }
        });
    }

    @FXML
    protected void HORIZONTOLDOUBLE() throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        int startx = 10;
        int starty = 10;
        double height = imageViewPane.getHeight() / 2;
        double width = imageViewPane.getWidth();
        AnchorPane anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
        starty = (int) (anchorPane.getPrefHeight() + 15);
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
    }

    @FXML
    protected void VERTICALDOUBLE() throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        int startx = 10;
        int starty = 10;
        double height = imageViewPane.getHeight();
        double width = imageViewPane.getWidth() / 2;
        AnchorPane anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
        startx = (int) (anchorPane.getPrefWidth() + 30);
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
    }

    @FXML
    protected void HORIZONTOLTRIPLE() throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        int startx = 10;
        int starty = 10;
        double height = imageViewPane.getHeight() / 3;
        double width = imageViewPane.getWidth();
        AnchorPane anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
        starty = (int) (anchorPane.getPrefHeight() + 20);
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
        starty = (int) (2 * anchorPane.getPrefHeight() + 30);
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
    }

    @FXML
    protected void VERTICALTRIPLE() throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        int startx = 10;
        int starty = 10;
        double height = imageViewPane.getHeight();
        double width = imageViewPane.getWidth() / 3;
        AnchorPane anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
        startx = (int) (anchorPane.getPrefWidth() + 30);
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
        startx = (int) (2 * anchorPane.getPrefWidth() + 50);
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
    }

    @FXML
    protected void QUAD() throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        int startx = 10;
        int starty = 10;
        double height = imageViewPane.getHeight() / 2;
        double width = imageViewPane.getWidth() / 2;
        AnchorPane anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);

        starty = (int) (anchorPane.getPrefHeight() + 20);
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);

        startx = (int) (anchorPane.getPrefWidth() + 20);
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
        starty = 10;
        anchorPane = new AnchorPane();
        addCollageFrame(anchorPane, startx, starty, width, height);
    }

    @FXML
    protected void crop() {

        Rectangle2D rectangle2D = new Rectangle2D(x, y, width, height);
        imageView.setViewport(rectangle2D);
    }

    @FXML
    protected void areaSelection() {
        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = (int) event.getX();
                y = (int) event.getY();
//                System.out.println("X: " + event.getX() + " Y: " + event.getY());

            }
        });

        imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                width = (int) (event.getX() - x);
                height = (int) (event.getY() - y);
                crop();
//                System.out.println("X: " + (event.getX() - x) + " Y: " + (event.getY() - y));
            }
        });

    }

    @FXML
    protected void filter() {
        imageView.setEffect(new DropShadow());
    }

    @FXML
    protected void cropButtonPressed() {
        System.out.println("Crop Button Pressed");
        Double orgWidth = imageView.getFitWidth();
        Double orgHeight = imageView.getFitHeight();
        bottomToUpCrop.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                double x = bottomToUpCrop.getValue() * orgHeight / 100;
                Rectangle2D rectangle2D = new Rectangle2D(0, 0, orgWidth, x);
                imageView.setViewport(rectangle2D);
            }
        });
        topToBottomCrop.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Rectangle2D rectangle2D = new Rectangle2D(0, 0, bottomToUpCrop.getValue() * orgWidth / 100, orgWidth);
                imageView.setViewport(rectangle2D);
            }
        });
        leftToRightCrop.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Rectangle2D rectangle2D = new Rectangle2D(0, 0, 50, 50);
                imageView.setViewport(rectangle2D);
            }
        });
        rightToLeftCrop.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
            }
        });
    }


    Imgcodecs imgCodecs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stk = new Stack<>();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        imgCodecs = new Imgcodecs();
    }

    @FXML
    protected void adjustHandle() {
        ColorAdjust colorAdjust = new ColorAdjust();
        brightness.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setBrightness(brightness.getValue() / 100);
                imageView.setEffect(colorAdjust);
            }
        });
        contrast.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setContrast(contrast.getValue() / 100);
                imageView.setEffect(colorAdjust);
            }
        });
        hue.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setHue(hue.getValue() / 100);
                imageView.setEffect(colorAdjust);
            }
        });
        saturation.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setSaturation(saturation.getValue() / 100);
                imageView.setEffect(colorAdjust);
            }
        });
    }

    @FXML
    protected void rotate90() {
        AnchorPane anchorPane = new AnchorPane(imageViewPane);
        stk.add(anchorPane);
        imageView.setRotate((90 + imageView.getRotate()));
    }

    @FXML
    protected void rotate180() {
        AnchorPane anchorPane = new AnchorPane(imageViewPane);
        stk.add(anchorPane);
        imageView.setRotate((180 + imageView.getRotate()));
    }

    @FXML
    protected void flipVertical() {
        Translate flipTranslation = new Translate(0, imageViewPane.getHeight());
        Rotate flipRotation = new Rotate(180, Rotate.X_AXIS);
        imageViewPane.getTransforms().addAll(flipTranslation, flipRotation);
    }

    @FXML
    protected void flipHorizontal() {
        Translate flipTranslation = new Translate(imageView.getFitWidth(), 0);
        Rotate flipRotation = new Rotate(180, Rotate.Y_AXIS);
        imageView.getTransforms().addAll(flipTranslation, flipRotation);
    }

    @FXML
    public void colorFiller() {
        GraphicsContext g;
        canvas = new Canvas(imageViewPane.getWidth(), imageViewPane.getHeight());
        g = canvas.getGraphicsContext2D();
        g.drawImage(imageView.getImage(), imageView.getFitWidth(), imageView.getFitHeight());
        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (eraser.isSelected()) {
                g.clearRect(x, y, size, size);
            } else {
                g.setFill(colorPicker.getValue());
                g.fillOval(x, y, size, size);
            }
        });
        imageViewPane.getChildren().add(canvas);
    }

    @FXML
    protected void undoAction() {
//        if (stk.empty()) {
//            System.out.println("Cannot do undo");
//            return;
//        }
//        System.out.println(stk.size());
//        for (Node e : imageViewPane.getChildren()) {
//            System.out.println(e);
//        }
//        for (Node e : stk.peek().getChildren()) {
//            System.out.println(e);
//        }
//        imageViewPane = stk.peek();
//        stk.pop();
        imageView.setSmooth(true);
    }

    @FXML
    protected void redEyeCorrection() throws InterruptedException {
        Mat matrix = imgCodecs.imread(file.getAbsolutePath(), imgCodecs.IMREAD_COLOR);
        Mat imgOut = matrix.clone();
        CascadeClassifier cascadeClassifier = new CascadeClassifier("E:\\InstalledSoftwares\\openCV\\opencv\\sources\\data\\haarcascades\\haarcascade_eye.xml");
        MatOfRect eyes = new MatOfRect();
        Size size = new Size(100, 100);
        cascadeClassifier.detectMultiScale(matrix, eyes, 1.3, 4, 0, size);
        Rect[] eyesArray = eyes.toArray();
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

            fillHoles(mask);
//            Imgproc.dilate(mask, mask, new Mat()  , new Point(-1, -1), 3, 1, new Scalar(1));
//            

            Mat mean = new Mat();
            Core.divide(bg, new Scalar(2), mean);
            mean.copyTo(bgr.get(2), mask);
            mean.copyTo(bgr.get(0), mask);
            mean.copyTo(bgr.get(1), mask);
            Mat eyeout = new Mat();
            eyeout = eye.clone();
            Core.merge(bgr, eyeout);
            eyeout.copyTo(imgOut.submat(eyesArray[i]));
        }
        showResult(imgOut);
        System.out.println("End");
    }

    void fillHoles(Mat mask) {
        Mat maskFloodfill = mask.clone();
        Size size = new Size(mask.rows() + 2, mask.cols() + 2);
        Mat maskTemp = new Mat(size, maskFloodfill.type());
        floodFill(maskFloodfill, maskTemp, new Point(0, 0), new Scalar(255));
        Mat mask2 = new Mat(maskFloodfill.size(), maskFloodfill.type());
        Core.bitwise_not(maskFloodfill, mask2);
        Core.bitwise_or(mask2, mask, mask);
//        Rectangle2D rectangle2D = new Rectangle2D()



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
