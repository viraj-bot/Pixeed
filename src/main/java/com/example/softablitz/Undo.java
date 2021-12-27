package com.example.softablitz;

import javafx.scene.layout.AnchorPane;

import java.io.*;

public class Undo implements Serializable {
    private AnchorPane imageViewPane;

    public Undo(AnchorPane anchorPane) {
        this.imageViewPane = anchorPane;
    }
}
