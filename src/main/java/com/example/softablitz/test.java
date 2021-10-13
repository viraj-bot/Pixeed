package com.example.softablitz;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class test
{
    @FXML
    AnchorPane imageViewPane;
    @FXML
    protected void openFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select files", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);
//        imageViewPane.setMaxHeight(720);
//        imageViewPane.setMaxWidth(1280);
        Group group = new Group();
        for (File x : fileChooser.showOpenMultipleDialog(null)) {
            ImageView imageView = new ImageView(new Image((new FileInputStream(x))));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            group.getChildren().add(imageView);
        }
        imageViewPane.getChildren().add(group);
        System.out.println(group.getChildren());
        System.out.println(imageViewPane.getChildren());
        System.out.println(imageViewPane.getHeight());
        System.out.println(imageViewPane.getWidth());
    }

}
