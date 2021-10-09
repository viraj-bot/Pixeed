package com.example.softablitz;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controller
{
    @FXML
    private Label welcomeText;
    @FXML
    private ContextMenu fileContestMenu;
    @FXML
    private Button file;
    @FXML
    private Label contextMenuLabel;
    @FXML
    private AnchorPane fileMenuPane;
    @FXML
    private ImageView imageView;
    @FXML
    protected void onHelloButtonClick()
    {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void fileButtonClicked()
    {
        fileMenuPane.toFront();
    }
    @FXML
    protected void openFile() throws FileNotFoundException
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files","*.jpg","*.png","*.jpeg");
        fileChooser.getExtensionFilters().add(filter);
        for(File x:fileChooser.showOpenMultipleDialog(null)){
            imageView.setImage(new Image((new FileInputStream(x))));
        }
    }
    @FXML
    protected void handleZoom(){
        imageView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                if (deltaY < 0){
                    zoomFactor = 0.95;
                }
                imageView.setScaleX(imageView.getScaleX() * zoomFactor);
                imageView.setScaleY(imageView.getScaleY() * zoomFactor);
                event.consume();
            }
        });
    }

}