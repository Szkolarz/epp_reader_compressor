package com.epp.epp_reader_compressor;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EPPController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException {

        welcomeText.setText("EPP Converter");

        File file = new File(
                "C:\\Users\\tholv\\Desktop\\epp\\przesyl7.22.epp");

        BufferedReader br = new BufferedReader(new FileReader(file));



        ReadCellData(0, 1, "C:\\Users\\tholv\\Desktop\\epp\\import-firgang722.xlsx");


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


            for (int i=0; i<listOfErrors.size(); i++) {
                System.out.println(listOfErrors.get(i));
            }

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



   /* public static void readExcel(String filePath) {
        File file = new File(filePath);
        try {
            FileInputStream inputStream = new FileInputStream(file);

            Workbook baeuldungWorkBook = new HSSFWorkbook(inputStream);
            for (Sheet sheet : baeuldungWorkBook) {

                int firstRow = sheet.getFirstRowNum();
                int lastRow = sheet.getLastRowNum();
                for (int index = firstRow + 1; index <= lastRow; index++) {
                    Row row = sheet.getRow(index);

                    for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
                        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        printCellValue(cell);
                    }
                }

            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void printCellValue(Cell cell) {
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            System.out.print(cell.getStringCellValue() + " | ");
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                System.out.print(cell.getDateCellValue() + " | ");
            } else {
                System.out.print(cell.getNumericCellValue() + " | ");
            }
        }
        if (cellType.equals(CellType.BOOLEAN)) {
            System.out.print(cell.getBooleanCellValue() + " | ");
        }
    }*/

    }


