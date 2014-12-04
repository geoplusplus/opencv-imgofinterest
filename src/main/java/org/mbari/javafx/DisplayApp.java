package org.mbari.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Brian Schlining
 * @since 2014-10-06T16:07:00
 */
public class DisplayApp extends Application {

    private static boolean isStarted = false;

    public static void start() {
        if (!isStarted) {
            Application.launch();
            isStarted = true;
        }
    }

    public static void main(String[] args) {
        start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //primaryStage.show();
    }

    public static void namedWindow(String name, Image image) {

        start();

        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle(name);

            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(false);

            BorderPane pane = new BorderPane(imageView);
            Scene scene = new Scene(pane, 300, 300, Color.ALICEBLUE);
            stage.setScene(scene);
            stage.show();
        });

    }
}
