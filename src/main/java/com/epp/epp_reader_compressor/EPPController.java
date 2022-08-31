package com.epp.epp_reader_compressor;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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


        List<String> list = new ArrayList<String>();

        while ((st = br.readLine()) != null) {
            System.out.println(st);
            list.add(st);
        }

        for (int i=0; i<list.size(); i++)
        {
            String input = list.get(i);   //input string
            String firstFourChars = "";   //substring containing first 4 characters

            if (input.length() > 4)
                firstFourChars = input.substring(0, 4);

            if (firstFourChars.contains("KW"))
            {
                String temp = list.get(i);
                System.out.println(temp.replaceAll("(,)\\1{1,}",","));
            }
        }




    }

    }


