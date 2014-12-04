package org.mbari.javafx;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author Brian Schlining
 * @since 2014-10-06T16:17:00
 */
public class DisplayAppDemo {

    public static void main(String[] args) throws Exception {
        String imagePath = "/lena1.png";
        DisplayApp.start();
        Thread.sleep(1000L);
        Image image = new Image(imagePath);
        DisplayApp.namedWindow("Lena 1", image);
        DisplayApp.namedWindow("Lena 2", image);
    }
}
