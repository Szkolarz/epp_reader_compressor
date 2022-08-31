package com.epp.epp_reader_compressor;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EPPController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("EPP Converter");
    }
}