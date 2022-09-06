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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class EPPController extends Thread {



    public static Integer i;
    public static Integer size;
    public static BufferedWriter bw;

    public EPPController() {

    }

    public void aaa() {

    }

    public static Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public static Integer getSize() {
        return size;
    }

    public void setSIze(Integer size) {
        this.size = size;
    }

    public static BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public EPPController(Integer i, Integer size, BufferedWriter bw) {
        this.i = i;
        this.size = size;  this.bw = bw;
    }

    public EPPController(BufferedWriter bw) {
        this.bw = bw;
    }




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


    private static List<String> list = new ArrayList<String>();

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




    AtomicInteger atomicInteger = new AtomicInteger(0);

    public void doSomething (final Integer i_, final Integer size_, final BufferedWriter bw) throws InterruptedException {
        Thread thread = new Thread(() -> {

            //System.out.println("I: " + i_);


            for (int i = i_; i < size_; i++) { atomicInteger.incrementAndGet();  System.out.println(atomicInteger);
                for (int j = 0; j < listOfErrors.size(); j++) {

                    //System.out.println("I: "+i);
                    //System.out.println("J: "+j +"\n");

                    // System.out.println(list.get(i));

                    //if (list.get(i).contains(listOfErrors.get(j))) {
                    if ((list.get(i).contains(listOfErrors.get(j))) && ((listOfErrors.get(j)).substring(0, 2).equals(list.get(i).substring(1, 3)))
                            || list.get(i).contains(listOfErrors.get(j)) && ((list.get(i)).startsWith("\"wpłata\""))
                            || list.get(i).contains(listOfErrors.get(j)) && (list.get(i)).startsWith("\"wypłata\"")
                            || list.get(i).contains(listOfErrors.get(j)) && (list.get(i)).startsWith("\"BP\"")
                            || list.get(i).contains(listOfErrors.get(j)) && (list.get(i)).startsWith("\"BW\"")) {

                        //System.out.println("d");


                        String temp = list.get(i);
                        temp = (temp.replaceAll("(,)\\1{1,}", "\t"));
                        temp = (temp.replaceAll("[ ]*,[ ]*+", "\t"));
                        temp = (temp.replaceAll("[\"]", ""));               //apostrofy
                        temp = (temp.replaceAll("\\s[.]\\s*\\.*", "\t"));   //kropki
                        temp = (temp.replaceAll("(\\t)\\1{1,}", "\t"));
                        temp = (temp.replaceAll("(\\t\\s)\\1{1,}", ""));
                        //System.out.println(temp.replaceAll("[ ]*,[ ]*|[ ]+", "\t"));
//   (\s[.](.*?)\t)
                        //(\s[.]*)([\s]*\.)

                        String firstTwoChars = "";   //substring containing first 4 characters

                        firstTwoChars = temp.substring(0, 2);


                        FileTypes fileTypes = new FileTypes();
                        if (firstTwoChars.equals("BP")) {

                            temp = fileTypes.fileBP(temp, firstTwoChars, listOfErrors.get(j));
                        }
                        if (firstTwoChars.equals("KW")) {
                            temp = fileTypes.fileKW(temp, firstTwoChars, listOfErrors.get(j));
                        }

                        try {
                            bw.write(temp);
                            bw.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                        //if (tester == list.size())
                           // System.out.println("fdsfdsfdsfdsdsf");

                        //System.out.println(total);


                        break;
                    }
                }

            }
            try {
                Thread.sleep(155);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

      /*  try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/




    }

    Integer flaga = 0;

    @FXML
    public void onSaveButtonClick1() throws IOException, InterruptedException {

        welcomeText.setText("EPP Converter");

        File file = new File(eppPath);


        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-2"));


        ReadCellData(0, 1, xlsxPath);

        String st = "";

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




        while (true) {

            if (!((st = br.readLine()) != null)) break;
            //System.out.println(st);
            list.add(st);
        }
        //System.out.println(list.get(1));


        FileWriter fw = null;
        try {
            fw = new FileWriter("C:\\Users\\tholv\\Desktop\\epp\\test.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);



        EPPController epp = new EPPController();

        Integer x = list.size() / 10000;
        Integer y = list.size() % 10000;

        Integer count = 0;


        for (int k=0; k <= x; k++) {
            if (k == (x)) {
                epp.setI(list.size() - y);
                final Integer q = getI();
                count = k;
               // System.out.println((list.size()) + " l size");



               // System.out.println((list.size() - y) + "df");
                //System.out.println((k) +" k");
System.out.println("kkkkkk");
                doSomething((list.size() - y), list.size(), bw);
                //Runnable r = new EPPController(q, list.size(), bw);
                //new Thread(r).start();
                /*try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/x++; k++;
            }
            else {
                epp.setI(k * 10000);
                epp.setSIze((k+1)*10000);
                final Integer q = getI();
                final Integer w = getSize();
                System.out.println((k) +" k");
               // System.out.println((k * 10000) +" i");
                //System.out.println(((k+1)*10000) +" size");

                System.out.println(k * 10000 + " df");
                System.out.println((k+1)*10000 + " ddf");

                doSomething((k * 10000), ((k+1)*10000), bw);
                sleep(5);
                //Runnable r = new EPPController(q, w, bw);
                //new Thread(r).start();
            }

        }







        labelLoading.setVisible(false);
        System.out.println("finito");






    }


    protected void onSaveButtonClick(Integer i) throws IOException  {


       /* Path source = Paths.get("C:\\Users\\tholv\\Desktop\\epp\\test.txt");
        try{
            // rename a file in the same directory
            Files.move(source, source.resolveSibling("text.xlsx"));

        } catch (IOException e) {
            e.printStackTrace();
        }*/




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



//"(?:[^\\"](?=[\p{P}]{2,})|\\\\|\\")*"  //for later use (probably lmao)



    private static List<String> listOfErrors = new ArrayList<String>();

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


