package com.example.softablitz;



import java.io.Serializable;

public class Undo implements Serializable {
    private int imageViewPane;

    public Undo(int anchorPane) {
        this.imageViewPane = anchorPane;
    }
}
