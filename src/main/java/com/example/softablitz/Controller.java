package com.example.softablitz;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller implements Initializable {
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
    private TextArea FIELD;
    private ImageView activeImageView;
    private ImageView imageView;
    private List<File> list;
    private Stack<ObservableList<Node>> stk;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stk = new Stack<>();

        TEXTPANE.setVisible(false);

    }

    @FXML
    protected void fileButtonClicked() {
        File outputFile = new File("D:/formattedPicture.png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(imageViewPane.snapshot(null, null), null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void openFile() throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files", "*.jpg", "*.png", "*.jpeg", "*.jfif");
        fileChooser.getExtensionFilters().add(filter);
        int padding = 10;
        File x = fileChooser.showOpenMultipleDialog(null).get(0);
        ImageView imageView = new ImageView();
        Image image = new Image((new FileInputStream(x)));
        imageView.setImage(image);
        centerFrameImage(imageView, imageViewPane);
        handleZoom(imageView);
        stk.push(imageViewPane.getChildren());
        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageView.setLayoutX(mouseEvent.getScreenX());
                imageView.setLayoutY(mouseEvent.getSceneY());
            }
        });

        activeImageView = imageView;
        final double[] layoutx = {imageView.getLayoutX()};
        final double[] layouty = {imageView.getLayoutY()};
        imageView.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent mouseDragEvent) {
//                layoutx[0] = mouseDragEvent.getSceneX();
//                layouty[0] = mouseDragEvent.getSceneY();
                imageView.getScene().setCursor(Cursor.CLOSED_HAND);
            }
        });
        imageView.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent mouseDragEvent) {
                imageView.getScene().setCursor(Cursor.OPEN_HAND);
//                imageView.setLayoutX(imageView.getLayoutX() + layoutx[0] - mouseDragEvent.getSceneX());
//                imageView.setLayoutX(imageView.getLayoutX() + layouty[0] - mouseDragEvent.getSceneY());
            }

            ;
        });

    }

    @FXML
    protected void handleZoom(ImageView imageView) {
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
    protected void cropButtonPressed() {

//      2

//        bottomToUpCrop.valueProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                double x = bottomToUpCrop.getValue() * orgHeight / 100;
//                Rectangle2D rectangle2D = new Rectangle2D(0, 0, orgWidth, x);
//                imageView.setViewport(rectangle2D);
//            }
//        });
//        topToBottomCrop.valueProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                Rectangle2D rectangle2D = new Rectangle2D(0, 0, bottomToUpCrop.getValue() * orgWidth / 100, orgWidth);
//                imageView.setViewport(rectangle2D);
//            }
//        });
//        leftToRightCrop.valueProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                Rectangle2D rectangle2D = new Rectangle2D(0, 0, 50, 50);
//                imageView.setViewport(rectangle2D);
//            }
//        });
//        rightToLeftCrop.valueProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//
//            }
//        });
    }


    @FXML
    private ImageView emj1;
    @FXML
    private AnchorPane EMOJIPANE;

    @FXML
    private void emojiButtonPressed() {
//        if (EMOJIPANE.isVisible())
//            EMOJIPANE.setVisible(false);
//        else
//           EMOJIPANE.setVisible(true);

        emj1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageViewPane.getChildren().add(emj1);
            }
        });
    }
    @FXML
    Slider EMOJISIZEADJUST;




    @FXML
    private AnchorPane TEXTPANE;

    @FXML
    private void textButtonPressed() {
        if (TEXTPANE.isVisible())
            TEXTPANE.setVisible(false);
        else
            TEXTPANE.setVisible(true);

    }

    @FXML
    Slider FONTADJUST;
    @FXML
    ColorPicker COLORPICKER;

    @FXML
    private void ADDBUTTONPRESSED() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: white");
        Text text = new Text();
        text.setText(FIELD.getText());
        text.setFill(COLORPICKER.getValue());
        text.setStyle("-fx-font: 50 arial");
        text.setStyle("-fx-background-color: white");
        text.setLayoutX(imageViewPane.getWidth() / 2);
        text.setLayoutY(imageViewPane.getHeight() / 2);
        text.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                text.setLayoutX(mouseEvent.getSceneX());
                text.setLayoutY(mouseEvent.getSceneY());
            }
        });
        imageViewPane.getChildren().add(text);
        COLORPICKER.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                text.setFill(COLORPICKER.getValue());
            }
        });
        FONTADJUST.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                text.setFont(new Font(FONTADJUST.getValue()));
            }
        });
        stk.push(imageViewPane.getChildren());
    }

    public double centerFrameImage(ImageView imageView, AnchorPane anchorPane) {
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


    @FXML
    protected void adjustHandle() {
        ColorAdjust colorAdjust = new ColorAdjust();
        brightness.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setBrightness(brightness.getValue() / 100);
                activeImageView.setEffect(colorAdjust);
            }
        });
        contrast.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                colorAdjust.setContrast(contrast.getValue() / 100);
                activeImageView.setEffect(colorAdjust);
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
        activeImageView.setRotate((90 + activeImageView.getRotate()));
    }

    @FXML
    protected void rotate180() {
        activeImageView.setRotate((180 + activeImageView.getRotate()));
    }

    @FXML
    protected void undoButtonPressed() {
        if (!stk.empty()) {
            imageViewPane.getChildren().setAll(stk.peek());
            stk.pop();
        } else {
            System.out.println("Empty");
        }
    }


    @FXML
    void SETFRAME1(ActionEvent event) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame1.jpg")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(anchorPane, ratio * 1770 + imageView.getLayoutX(), 1355 * ratio + imageView.getLayoutY(), 745 * ratio, 1090 * ratio);
        stk.push(imageViewPane.getChildren());
    }

    @FXML
    void SETFRAME2(ActionEvent event) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame2.png")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(anchorPane, ratio * 315 + imageView.getLayoutX(), 80 * ratio + imageView.getLayoutY(), 425 * ratio, 595 * ratio);
        stk.push(imageViewPane.getChildren());

    }


    @FXML
    void SETFRAME3(ActionEvent event) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame3.png")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(anchorPane, ratio * 125 + imageView.getLayoutX(), 110 * ratio + imageView.getLayoutY(), 545 * ratio, 350 * ratio);
        stk.push(imageViewPane.getChildren());
    }


    @FXML
    void SETFRAME4(ActionEvent event) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame4.png")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(anchorPane, ratio * 300 + imageView.getLayoutX(), 330 * ratio + imageView.getLayoutY(), 1320 * ratio, 800 * ratio);
        stk.push(imageViewPane.getChildren());
    }


    @FXML
    void SETFRAME5(ActionEvent event) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame5.jpg")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1080 + " " + 605 * ratio + " " + 5055 * ratio + " " + 1925 * ratio);
        addCollageFrame(anchorPane, ratio * 1800 + imageView.getLayoutX(), 605 * ratio + imageView.getLayoutY(), 1380 * ratio, 1925 * ratio);
        stk.push(imageViewPane.getChildren());
    }

    // frame1 : 1770 1355 , 2515  2445
// frame3 : 125 110  , 670 460
// frame4 : 300 330  , 1620 1130
// frame5 : 1800 605 ,  3180 2530
// frame6 : 1650 945 , 2780 2810
// frame2 : 315  80  , 740  675

    @FXML
    void SETFRAME6(ActionEvent event) throws FileNotFoundException {
        imageViewPane.getChildren().clear();
        Image image = new Image(new FileInputStream(new File("src/main/resources/com/example/softablitz/icons/frame6.jpg")));
        ImageView imageView = new ImageView(image);
        double ratio = centerFrameImage(imageView, imageViewPane);
        AnchorPane anchorPane = new AnchorPane();
        System.out.println(imageView.getLayoutX() + " " + imageView.getLayoutY() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
        System.out.println(ratio * 1770 + " " + 1355 * ratio + " " + 745 * ratio + " " + 1090 * ratio);
        addCollageFrame(anchorPane, ratio * 1650 + imageView.getLayoutX(), 945 * ratio + imageView.getLayoutY(), 1130 * ratio, 1865 * ratio);
        imageViewPane.toFront();
        stk.push(imageViewPane.getChildren());
    }

    protected void addCollageFrame(AnchorPane anchorPane, double startx, double starty, double width, double height) throws FileNotFoundException {
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
                    System.out.println("clicked1");
                    openFile(imageView);
                    imageView.toBack();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        addResizeButton(anchorPane, imageView);
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

    protected void addResizeButton(AnchorPane anchorPane, ImageView imageView) throws FileNotFoundException {
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
//                System.out.println(mouseEvent.getSceneX() + " " + mouseEvent.getSceneY() + " " + mouseEvent.getScreenX() + " " + mouseEvent.getScreenY());
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

    public void centerImage(ImageView imageView, AnchorPane anchorPane) {
        double hig = imageView.getFitHeight();
        double wid = imageView.getFitWidth();
        imageView.setLayoutX((anchorPane.getPrefWidth() - wid) / 2);
        imageView.setLayoutY((anchorPane.getPrefHeight() - hig) / 2);
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
    protected void SETRAIN1() throws FileNotFoundException {
        Filters filters = new Filters();
        filters.setRain1Filter(imageViewPane);
    }

    @FXML
    protected void SETRAIN2() throws FileNotFoundException {
        Filters filters = new Filters();
        filters.setRain2Filter(imageViewPane);
    }

    @FXML
    protected void SETRAIN3() throws FileNotFoundException {
        Filters filters = new Filters();
        filters.setRain3Filter(imageViewPane);
    }

    @FXML
    protected void SETSUNSHINE1() throws FileNotFoundException {
        Filters filters = new Filters();
        filters.setSunshine1Filter(imageViewPane);
    }

    @FXML
    protected void SETSUNSHINE2() throws FileNotFoundException {
        Filters filters = new Filters();
        filters.setSunshine2Filter(imageViewPane);
    }

    @FXML
    protected void SETSMOOTH() throws FileNotFoundException {
        Filters filters = new Filters();
        filters.setSmoothFilter(imageViewPane);
    }

    @FXML
    protected void COMPRESS() throws IOException {
        Compress compress= new Compress();
        Image image= activeImageView.getImage();
        compress.compressImage(image);

    }

    @FXML
    protected void SCALE() throws IOException {
//        UpScaleImage scale= new UpScaleImage();
//        Image image= activeImageView.getImage();


    }

    @FXML
    protected void SIDEBLUR() throws FileNotFoundException {
        SideBlur sideBlur =new SideBlur();
        ImageView imageView= activeImageView;
       sideBlur.VerticalSideBlur(imageViewPane,imageView);

    }

}

