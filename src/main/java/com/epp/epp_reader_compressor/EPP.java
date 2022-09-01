package com.epp.epp_reader_compressor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class EPP extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EPP.class.getResource("epp-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);

        Image icon = new Image(getClass().getResourceAsStream("/images/epp.png"));
        stage.getIcons().add(icon);

        stage.setTitle("Konwerter plików EPP");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}