package com.epp.epp_reader_compressor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.nio.charset.StandardCharsets;

public class EPPController {
    @FXML
    private Label welcomeText;

    @FXML
    private Label labelEPP;
    @FXML
    private Label labelXLSX;

    @FXML
    private Button buttonLoad;

    @FXML
    private ImageView imgTickXlsx;
    @FXML
    private ImageView imgTickEpp;

    @FXML
    private Rectangle clickMinimized1;
    @FXML
    private Label clickMinimized;
    @FXML
    private Label labelLoading;

    private Stage stage;

    private String eppPath;
    private String xlsxPath;


    List<String> list = new ArrayList<String>();

    public void initialize() {

        buttonLoad.setDisable(true);
        imgTickXlsx.setVisible(false);
        imgTickEpp.setVisible(false);
        labelLoading.setVisible(false);

    }

    @FXML
    protected void onMouseExit() throws IOException {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void onMouseMinimized() throws IOException {
        Stage stage = (Stage) clickMinimized.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void onSaveButtonClick() throws IOException {



        welcomeText.setText("EPP Converter");

        File file = new File(eppPath);


        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-2"));


        ReadCellData(0, 1, xlsxPath);


        String st;

        try {
            labelLoading.setVisible(true);
            File myObj = new File("test.txt");
            if (myObj.createNewFile()) {
                System.out.println("Plik utworzony: " + myObj.getName());
            } else {
                System.out.println("Plik już istnieje");
            }
        } catch (IOException e) {
            System.out.println("Błąd tworzenia pliku");
            e.printStackTrace();
        }




        while ((st = br.readLine()) != null) {
            //System.out.println(st);
            list.add(st);
        }
        //System.out.println(list.get(1));


        FileWriter fw = new FileWriter("C:\\Users\\tholv\\Desktop\\epp\\test.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (int i=0; i<list.size(); i++)
        {
            //String input = list.get(i);   //input string
            //String firstFourChars = "";   //substring containing first 4 characters

            /*if (input.length() > 4)
                firstFourChars = input.substring(0, 4);*/

            /*if (firstFourChars.contains("KW"))*/

            for (int j=0; j<listOfErrors.size(); j++) {
                if (list.get(i).contains(listOfErrors.get(j))) {
                    String temp = list.get(i);
                    temp = (temp.replaceAll("(,)\\1{1,}", "\t"));
                    temp = (temp.replaceAll("[ ]*,[ ]*+", "\t"));
                    temp = (temp.replaceAll("[\"]", ""));
                    temp = (temp.replaceAll("\\s[.]\\s*\\.*", ""));
                    temp = (temp.replaceAll("(\\t)\\1{1,}", "\t"));
                    temp = (temp.replaceAll("(\\t\\s)\\1{1,}", ""));
                    //System.out.println(temp.replaceAll("[ ]*,[ ]*|[ ]+", "\t"));
                    bw.write(temp);
                    bw.newLine();


                    break;
                }
            }
        }
        bw.close();

       /* Path source = Paths.get("C:\\Users\\tholv\\Desktop\\epp\\test.txt");
        try{
            // rename a file in the same directory
            Files.move(source, source.resolveSibling("text.xlsx"));

        } catch (IOException e) {
            e.printStackTrace();
        }*/


        labelLoading.setVisible(false);
        System.out.println("finito");

    }


    private void checkRequirements() {
        if (!labelEPP.getText().equals("(nie wybrano)") && !labelXLSX.getText().equals("(nie wybrano)"))
            buttonLoad.setDisable(false);
        else
            buttonLoad.setDisable(true);

        if (!labelEPP.getText().equals("(nie wybrano)"))
            imgTickEpp.setVisible(true);
        else
            imgTickEpp.setVisible(false);

        if (!labelXLSX.getText().equals("(nie wybrano)"))
            imgTickXlsx.setVisible(true);
        else
            imgTickXlsx.setVisible(false);


    }


    @FXML
    protected void onEppButtonClick(ActionEvent event) throws IOException {
        //Runtime.getRuntime().exec("explorer /select, C:\\");
        FileLoad fileload = new FileLoad();
        fileload.getTheUserFilePath(event, "epp", stage);
        labelEPP.setText(fileload.getNameOfFile());
        checkRequirements();
        eppPath = fileload.getPathOfFile();
    }

    @FXML
    protected void onXlsxButtonClick(ActionEvent event) throws IOException {
        FileLoad fileload = new FileLoad();
        fileload.getTheUserFilePath(event, "xlsx", stage);
        labelXLSX.setText(fileload.getNameOfFile());
        checkRequirements();
        xlsxPath = fileload.getPathOfFile();
    }







    List<String> listOfErrors = new ArrayList<String>();

    public void ReadCellData(int vColumn, int vColumn2, String file)
    {
        String value=null;          //variable for storing the cell value
        Workbook wb=null;           //initialize Workbook null


        try
        {
            FileInputStream inputStream=new FileInputStream("C:\\Users\\tholv\\Desktop\\epp\\import-firgang722.xlsx");

            wb = new XSSFWorkbook(inputStream);

            for (Sheet sheet : wb) {

                int firstRow = sheet.getFirstRowNum();
                int lastRow = sheet.getLastRowNum();
                for (int index = firstRow + 1; index <= lastRow; index++) {
                    Row row = sheet.getRow(index);

                    Cell cell = row.getCell(vColumn2);
                    value = cell.getStringCellValue();

                    if (value.equals("Błąd")) {
                        cell = row.getCell(vColumn);
                        value = cell.getStringCellValue();
                        listOfErrors.add(value);
                    }

                }
            }

            inputStream.close();


           /* for (int i=0; i<listOfErrors.size(); i++) {
                System.out.println(listOfErrors.get(i));
            }*/

        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e1)
        {
            e1.printStackTrace();
        }


    }



    }


