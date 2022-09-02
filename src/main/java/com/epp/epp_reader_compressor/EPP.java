package com.epp.epp_reader_compressor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EPP extends Application {


    class WindowButtons extends AnchorPane {

        public WindowButtons() {
            Button closeBtn = new Button("X");

            closeBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    Platform.exit();
                }
            });

            this.getChildren().add(closeBtn);
        }
    }

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("epp-view.fxml"));
        Scene scene = new Scene(root);
        //FXMLLoader fxmlLoader = new FXMLLoader(EPP.class.getResource("epp-view.fxml"));

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX()-xOffset);
            stage.setY(event.getScreenY()-yOffset);
        });

        Image icon = new Image(getClass().getResourceAsStream("/images/epp.png"));
        stage.getIcons().add(icon);


        //remove window decoration
        stage.initStyle(StageStyle.UNDECORATED);

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: green;");
        borderPane.setPrefSize(900, 500);
        borderPane.setMaxSize(900, 500);


        ToolBar toolBar = new ToolBar();

        int height = 25;
        toolBar.setPrefHeight(height);
        toolBar.setMinHeight(height);
        toolBar.setMaxHeight(height);
        toolBar.getItems().add(new EPP.WindowButtons());

        borderPane.setTop(toolBar);



        borderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });

        borderPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });


        stage.setTitle("Konwerter plików EPP");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}