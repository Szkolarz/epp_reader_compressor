package com.epp.epp_reader_compressor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.concurrent.atomic.AtomicInteger;

public class EPPController extends Thread {

    public EPPController() {}


    @FXML
    private Label welcomeText;

    @FXML
    private Label labelEPP;
    @FXML
    private Label labelXLSX;

    @FXML
    private Button buttonLoad;
    @FXML
    private Button buttonEpp;
    @FXML
    private Button buttonXlsx;

    @FXML
    private ImageView imgTickXlsx;
    @FXML
    private ImageView imgTickEpp;
    @FXML
    private ImageView imageLoading;

    @FXML
    private Label clickMinimized;
    @FXML
    private Label labelLoading;

    private Stage stage;

    private String eppPath;
    private String xlsxPath;
    private String savedFilePath;


    private static List<String> list = new ArrayList<String>();

    public void initialize() {
        buttonLoad.setDisable(true);
        imgTickXlsx.setVisible(false);
        imgTickEpp.setVisible(false);
        imageLoading.setVisible(false);
        labelLoading.setVisible(false);
        setLoadingButtonColor("#171717");
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

    public void mainActivity (final Integer i_, final Integer size_, final BufferedWriter bw) throws InterruptedException {
        Thread thread = new Thread(() -> {

            for (int i = i_; i < size_; i++) {
                atomicInteger.incrementAndGet();
                for (int j = 0; j < listOfErrors.size(); j++) {

                    if ((list.get(i).contains(listOfErrors.get(j))) && ((listOfErrors.get(j)).substring(0, 2).equals(list.get(i).substring(1, 3)))
                            || list.get(i).contains(listOfErrors.get(j)) && ((list.get(i)).startsWith("\"wpłata\""))
                            || list.get(i).contains(listOfErrors.get(j)) && (list.get(i)).startsWith("\"wypłata\"")
                            || list.get(i).contains(listOfErrors.get(j)) && (list.get(i)).startsWith("\"BP\"")
                            || list.get(i).contains(listOfErrors.get(j)) && (list.get(i)).startsWith("\"BW\"")) {


                        String temp = list.get(i);
                        temp = (temp.replaceAll("(,)\\1{1,}", "\t")); //przecinki
                        temp = (temp.replaceAll("[ ]*,[ ]*+", "\t"));
                        temp = (temp.replaceAll("[\"]", "")); //apostrofy
                        temp = (temp.replaceAll("\\s[.]\\s*\\.*", "\t")); //kropki
                        temp = (temp.replaceAll("(\\t)\\1{1,}", "\t"));
                        temp = (temp.replaceAll("(\\t\\s)\\1{1,}", ""));

                        String firstTwoChars = "";   //substring containing first 4 characters

                        firstTwoChars = temp.substring(0, 2);


                        FileTypes fileTypes = new FileTypes();
                        if (firstTwoChars.equals("BP")) {
                            temp = fileTypes.fileBP(temp, firstTwoChars, listOfErrors.get(j));
                        }
                        else if (firstTwoChars.equals("KW")) {
                            temp = fileTypes.fileKW(temp, firstTwoChars, listOfErrors.get(j));
                        }
                        else if (firstTwoChars.equals("BW")) {
                            temp = fileTypes.fileBW(temp, firstTwoChars, listOfErrors.get(j));
                        }
                        else if (firstTwoChars.equals("FS")) {
                            temp = fileTypes.fileFS(temp, firstTwoChars, listOfErrors.get(j));
                        }
                        else {
                            temp = fileTypes.fileOther(temp, firstTwoChars, listOfErrors.get(j));
                        }


                        try {
                            bw.write(temp);
                            bw.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (Integer.valueOf(String.valueOf(atomicInteger)) == list.size()) {
                try {
                    Thread.sleep(3000);
                    bw.close();
                    labelLoading.setVisible(false);
                    imageLoading.setVisible(false);

                    checkRequirements();
                    System.out.println("finito");

                    buttonXlsx.setDisable(false);
                    buttonEpp.setDisable(false);

                    programComplete();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }



    public void programComplete() throws IOException, InterruptedException {

        try {
            Platform.runLater(new Runnable() {
                @Override public void run() {

                    labelXLSX.setText("(nie wybrano)");
                    labelEPP.setText("(nie wybrano)");
                    checkRequirements();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Konwersja danych");
                    alert.setHeaderText(null);
                    alert.setContentText("Pomyślnie wykonano konwersję z .epp do .txt\n\n" +
                            "Teraz można przenieść dane z pliku .txt do Excela");
                    stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("file:src/main/resources/images/epp.png"));
                    alert.showAndWait();

                    list.clear();
                    listOfErrors.clear();
                    interrupt();

                }});
        }  catch (Exception e) {
            System.out.println("Błąd alertu");
        }start();
    }



    private void saveTextFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(EPPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public void onSaveButtonClick() throws IOException, InterruptedException {

        final String columns = "Dokument\tKontrahent\tTreść dokumentu\tKwota";

        atomicInteger.set(0);
        labelLoading.setVisible(true);
        imageLoading.setVisible(true);

        FileChooser fileChooser = new FileChooser();
        String homePath = System.getProperty("user.home");
        String currentPath = Paths.get(homePath).toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki .txt (*.txt)", "*.txt");
        fileChooser.getInitialDirectory();
        fileChooser.getExtensionFilters().add(extFilter);

        File file1 = fileChooser.showSaveDialog(stage);

        if (file1 != null) {
            savedFilePath = file1.getPath();
            System.out.println(savedFilePath);
            saveTextFile(columns, file1);
            setLoadingButtonColor("#171717");
            buttonLoad.setDisable(true);
            buttonXlsx.setDisable(true);
            buttonEpp.setDisable(true);
        }
        else {
            labelLoading.setVisible(false);
            imageLoading.setVisible(false);
        }

        welcomeText.setText("EPP Converter");

        File file = new File(eppPath);

        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-2"));

        ReadCellData(0, 1, xlsxPath);

        String st = "";

        Scanner scanner = new Scanner(newLineRemover);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            list.add(line);
        }
        scanner.close();

        /*while (true) {
            if (!((st = br.readLine()) != null)) break;
            list.add(st);
        }*/


        FileWriter fw = null;
        if (file1 != null) {
            try {
                fw = new FileWriter(savedFilePath, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter bw = new BufferedWriter(fw);


            Integer x = list.size() / 10000;
            Integer y = list.size() % 10000;


            for (int k = 0; k <= x; k++) {
                if (k == (x)) {
                    mainActivity((list.size() - y), list.size(), bw);
                    x++;
                    k++;
                } else {
                    mainActivity((k * 10000), ((k + 1) * 10000), bw);
                    sleep(150); //to be sure that threads did their job
                }

            }
        }
    }

    @FXML
    public void onSaveButtonClick1() throws IOException, InterruptedException {
        sleep(10);
        onSaveButtonClick();
    }



    private void checkRequirements() {
        if (!labelEPP.getText().equals("(nie wybrano)") && !labelXLSX.getText().equals("(nie wybrano)")) {
            buttonLoad.setDisable(false);

            setLoadingButtonColor("#52804d");
        }
        else {
            buttonLoad.setDisable(true);
            setLoadingButtonColor("#171717");
        }

        if (!labelEPP.getText().equals("(nie wybrano)"))
            imgTickEpp.setVisible(true);
        else
            imgTickEpp.setVisible(false);

        if (!labelXLSX.getText().equals("(nie wybrano)"))
            imgTickXlsx.setVisible(true);
        else
            imgTickXlsx.setVisible(false);
    }


    private String newLineRemover;
    @FXML
    protected void onEppButtonClick(ActionEvent event) throws IOException {
        //Runtime.getRuntime().exec("explorer /select, C:\\");
        FileLoad fileload = new FileLoad();
        fileload.getTheUserFilePath(event, "epp", stage);
        labelEPP.setText(fileload.getNameOfFile());
        checkRequirements();
        eppPath = fileload.getPathOfFile();

        Path path = Path.of(eppPath);
        newLineRemover = (Files.readString(path, Charset.forName("ISO-8859-2")));
        newLineRemover = (newLineRemover.toString().replaceAll("([\\r\\n]{3,})", ""));
    }

    public void setLoadingButtonColor(String bgC) {
        buttonLoad.setStyle("-fx-background-color: " + bgC + ";");
    }

    @FXML
    protected void onXlsxButtonClick(ActionEvent event) throws IOException {
        FileLoad fileload = new FileLoad();
        fileload.getTheUserFilePath(event, "*xlsx; *xls", stage);
        labelXLSX.setText(fileload.getNameOfFile());
        checkRequirements();
        xlsxPath = fileload.getPathOfFile();
    }


    private static List<String> listOfErrors = new ArrayList<String>();

    public void ReadCellData(int vColumn, int vColumn2, String file)
    {
        String value = null;          //variable for storing the cell value
        Workbook wb = null;           //initialize Workbook null

        try
        {
            FileInputStream inputStream=new FileInputStream(file);

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