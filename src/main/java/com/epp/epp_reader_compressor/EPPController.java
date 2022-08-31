package com.epp.epp_reader_compressor;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.nio.charset.StandardCharsets;

public class EPPController {
    @FXML
    private Label welcomeText;


    List<String> list = new ArrayList<String>();

    @FXML
    protected void onHelloButtonClick() throws IOException {

        welcomeText.setText("EPP Converter");

        File file = new File(
                "C:\\Users\\tholv\\Desktop\\epp\\przesyl7.22.epp");



        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-2"));





        ReadCellData(0, 1, "C:\\Users\\tholv\\Desktop\\epp\\import-firgang722.xlsx");


        String st;


        try {
            File myObj = new File("test.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
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
                    temp = (temp.replaceAll("[\"]", " "));
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



System.out.println("finito");

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


