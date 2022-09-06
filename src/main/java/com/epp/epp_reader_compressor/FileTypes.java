package com.epp.epp_reader_compressor;

import java.util.List;
import java.util.regex.*;


public class FileTypes {

    public void abc(String temp, String firstTwoChars, String typeOfFile, String listOfErrors) {

        Integer total = 0;
        Integer counterForTabs = 0;

        for (int a = 0; a < temp.length(); a++) {
            char ch = temp.charAt(a);
            if ( ch == '\t') {
                total++; counterForTabs++;

               // if (typeOfFile.equals("BP"))
                    //fileBP(temp, a, counterForTabs, total, firstTwoChars, listOfErrors);

            }
        }
    }


    public String fileBP (String temp, String firstTwoChars, String listOfErrors) {
        if (firstTwoChars.equals("BP")) {
            Pattern p_content = Pattern.compile("((\\t.([a-zA-Z]{0,3})\\s[0-9]{0,6}\\/[a-zA-Z0-9]{0,3}\\/[0-9]{0,5}(.*?)\\t))|((\\t(Liczba).*[.][0-9]{2}\\t))"); //. represents single character
            Pattern p_amount = Pattern.compile("[0-9]{0,7}\\.[0-9]{4}");


            //(\t[a-zA-Z]{0,3}\s[0-9]{0,6}\/[a-zA-Z]{0,3}\/[0-9]{4}+(.*?)\t) //wazne
            //(\t([a-zA-Z]{0,3})\s[0-9]{0,6}\/[a-zA-Z]{0,3}\/[0-9]{4}+(.*?)\t)
            //(\t+([0-9]{0,5})\/[a-zA-Z]{0,4}\/[0-9]{4}+(.*?)\t) //wazne
            //(\t([a-zA-Z]{0,3})\-[0-9]{0,4}\/[a-zA-Z]{0,5}\/[0-9]{0,3}\/[0-9]{0,3}(.*?)\t) //wazne

            //

            Matcher m_content = p_content.matcher(temp);
            Matcher m_amount = p_amount.matcher(temp);
            boolean b_content = m_content.find();
            boolean b_amount = m_amount.find();

           // System.out.println("found: " + m.group(0));

            String document = ("BP " + listOfErrors);
            String contractor = "";
            String content = "";
            String amount = "";


            Integer total = 0;
            Integer counterForTabs = 0;

            StringBuilder ooo = new StringBuilder();

            for (int a = 0; a < temp.length(); a++) {
                char ch = temp.charAt(a);
                if ( ch == '\t') {
                    total++; counterForTabs++;
                }
                if (counterForTabs == 7) {
                    ooo = ooo.append( String.valueOf(temp.charAt(a)));
                }
                if (counterForTabs == 8) {
                    contractor = ooo.toString();
                    //System.out.println(contractor);
                    break;
                }
            }



            if (b_content == true) {
                content = m_content.group(1);
                if (content == null)
                    content = m_content.group(5);
            }
            else
                content = "\tbrak danych\t";

            if (b_amount == true) {
                amount = m_amount.group(0);
            }
            else
                amount = "brak danych";

            String output = new StringBuilder().append(document)
                    .append("\t").append(contractor)
                    .append(content).append(amount).toString();
            temp = output;

        }
        return temp;
    }

   /* public void fileBP (String temp, Integer a, Integer counterForTabs, Integer total, String firstTwoChars) {
        if ((counterForTabs == 9 && firstTwoChars.equals("BP"))) {
            Pattern p = Pattern.compile("[0-9]{2}-[0-9]{3}");//. represents single character

            String firstFiveChars = "";   //substring containing first 4 characters
            firstFiveChars = (String.valueOf(temp.charAt(a+1)) + String.valueOf(temp.charAt(a+2)) +
                    String.valueOf(temp.charAt(a+3)) + String.valueOf(temp.charAt(a+4)) +
                    String.valueOf(temp.charAt(a+5)) + String.valueOf(temp.charAt(a+6)));

            //(\t[a-zA-Z]{0,3}\s[0-9]{0,6}\/[a-zA-Z]{0,3}\/[0-9]{4}+(.*?)\t) //wazne
            //(\t([a-zA-Z]{0,3})\s[0-9]{0,6}\/[a-zA-Z]{0,3}\/[0-9]{4}+(.*?)\t)
            //(\t+([0-9]{0,5})\/[a-zA-Z]{0,4}\/[0-9]{4}+(.*?)\t) //wazne
            //(\t([a-zA-Z]{0,3})\-[0-9]{0,4}\/[a-zA-Z]{0,5}\/[0-9]{0,3}\/[0-9]{0,3}(.*?)\t) //wazne


            Matcher m = p.matcher(firstFiveChars);
            boolean b = m.matches();
            if (b == true) {
                String output = new StringBuilder().append(temp.substring(0, a+1))
                        .append("---\t").append(temp.substring(a+1, temp.length())).toString();
                //temp = temp + String.valueOf("---\t" + temp.charAt(a));
                temp = output;
            }
            System.out.println(b);
        }

    }*/

    public void fileBW (String temp, Integer a, Integer counterForTabs, Integer total, String firstTwoChars) {

    }

    public void fileKP (String temp, Integer a, Integer counterForTabs, Integer total, String firstTwoChars) {

    }

    public String fileKW (String temp, String firstTwoChars, String listOfErrors) {

        if (listOfErrors.substring(0, 2).equals("KW")) {
            Pattern p_content = Pattern.compile("(\\t[a-zA-Z]{0,3}\\s[0-9]{0,6}\\/[a-zA-Z]{0,3}\\/[0-9]{4}+(.*?)\\t)|(\\t(Zwrot ).*)"); //. represents single character
            Pattern p_amount = Pattern.compile("[0-9]{0,7}\\.[0-9]{4}");


            Matcher m_content = p_content.matcher(temp);
            Matcher m_amount = p_amount.matcher(temp);
            boolean b_content = m_content.find();
            boolean b_amount = m_amount.find();

            // System.out.println("found: " + m.group(0));

            String document = (listOfErrors);

            String contractor = "";
            String content = "";
            String amount = "";

            boolean flag = true;
            Integer total = 0;
            Integer counterForTabs = 0;

            StringBuilder ooo = new StringBuilder();

            for (int a = 0; a < temp.length(); a++) {
                char ch = temp.charAt(a);
                if (ch == '\t') {
                    total++;
                    counterForTabs++;
                }

                if (counterForTabs == 7) {
                    ooo = ooo.append(String.valueOf(temp.charAt(a)));
                }
                if (counterForTabs == 8) {
                    contractor = ooo.toString();
                    break;
                }
            }


            if (b_content == true) {
                content = m_content.group(1);
                if (content == null) {
                    content = m_content.group(3);
                    content = content.substring(1);
                    String[] parts = content.split("\t", 2);
                    //String part1 = parts[0];
                    content = ("\t" + parts[0]) + "\t";
                }
            } else
                content = "\tbrak danych\t";

            if (b_amount == true) {
                amount = m_amount.group(0);
            } else
                amount = "brak danych";

            if (flag == true) {
                String output = new StringBuilder().append(document)
                        .append("\t").append(contractor)
                        .append(content).append(amount).toString();
                temp = output;
            }
        }
            return temp;

    }

    public void filePZ (String temp, Integer a, Integer counterForTabs, Integer total, String firstTwoChars) {

    }

}
