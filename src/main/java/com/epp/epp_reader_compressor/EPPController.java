package com.epp.epp_reader_compressor;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;

public class EPPController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException {

        welcomeText.setText("EPP Converter");

        File file = new File(
                "C:\\Users\\tholv\\Desktop\\epp\\przesyl7.22.epp");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;

        while ((st = br.readLine()) != null)
            System.out.println(st);
    }

    }
