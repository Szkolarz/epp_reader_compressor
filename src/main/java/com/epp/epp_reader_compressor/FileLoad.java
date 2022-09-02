package com.epp.epp_reader_compressor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileLoad {

    @FXML
    private Label labelEPP;
    @FXML
    private Label labelXLSX;

    private static String nameOfFile;
    private static String pathOfFile;

    public String getNameOfFile() {
        return nameOfFile;
    }
    public String getPathOfFile() {
        return pathOfFile;
    }



    private static void configureFileChooser(final FileChooser fileChooser, String extension) {
        fileChooser.setTitle("Wybierz plik z rozszerzeniem ." + extension);
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(extension, "*."+extension),
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*")
        );
    }


    public void getTheUserFilePath(ActionEvent event, String extension, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, extension);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage.getScene().getWindow());

        if (file != null) {
            //System.out.println((file.getPath()));
            nameOfFile = file.getPath();
            pathOfFile = file.getPath();
            System.out.println(nameOfFile);
            //nameOfFile = nameOfFile.replaceAll(".+(\\.+)$", "a");

            Pattern p = Pattern.compile("(?:[^\\\\/](?!(\\\\|/)))+$");
            Matcher m = p.matcher(nameOfFile);
            while(m.find()) {
                nameOfFile = (m.group(0));
            }
                System.out.println(nameOfFile);

        }
        else {
            System.out.println("Zła ścieżka");
            nameOfFile = "(nie wybrano)";
        }
    }


}
