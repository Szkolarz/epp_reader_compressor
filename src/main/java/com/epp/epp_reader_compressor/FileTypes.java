package com.epp.epp_reader_compressor;

import java.util.regex.*;


public class FileTypes {

    public String date (String temp) {
        Pattern p_date = Pattern.compile("(\\t2[0-9]{13}\\t)");
        Matcher m_date = p_date.matcher(temp);
        boolean b_date = m_date.find();

        String date = "";
        StringBuilder tmpDate = new StringBuilder();

        if (b_date == true) {

            date = m_date.group(1);
            date = date.substring(0, date.length() - 7);
            date = date.substring(1);

            Integer counter = 0;

            for (int a = 0; a < date.length(); a++) {
                counter++;
                tmpDate = tmpDate.append(String.valueOf(date.charAt(a)));
                if (counter == 4 || counter == 6)
                    tmpDate = tmpDate.append(String.valueOf("."));
                }
        }
        else
            tmpDate = tmpDate.append(String.valueOf(""));

        return tmpDate.toString();
    }


    public String fileBP (String temp, String firstTwoChars, String listOfErrors) {
        if (firstTwoChars.equals("BP")) {

            Pattern p_vat = Pattern.compile("(\\t(\\/VAT)(.*?)\\t)");
            Matcher m_vat = p_vat.matcher(temp);
            boolean b_vat = m_vat.find();
            String date = date(temp);

            if (b_vat == true) {
                String tmp = "";
                tmp = m_vat.group(1);
                tmp = tmp.substring(0, tmp.length() - 1);
                tmp = (tmp + ",");
                temp = (temp.replaceAll("(\\t(\\/VAT)(.*?)\\t)", tmp));
                System.out.println(temp);
            }

            Pattern p_content = Pattern.compile("((\\t.([a-zA-Z]{0,3})\\s[0-9]{0,6}\\/[a-zA-Z0-9]{0,3}\\/[0-9]{0,5}(.*?)\\t))|((\\t(Liczba).*[.][0-9]{2}\\t))|(\\t(\\/VAT)(.*?)\\t)"); //. represents single character
            Pattern p_amount = Pattern.compile("[0-9]{0,7}\\.[0-9]{4}");

            Matcher m_content = p_content.matcher(temp);
            Matcher m_amount = p_amount.matcher(temp);
            boolean b_content = m_content.find();
            boolean b_amount = m_amount.find();


            String document = ("BP " + listOfErrors);
            String contractor = "";
            String content = "";
            String amount = "";


            Integer counterForTabs = 0;

            StringBuilder ooo = new StringBuilder();

            for (int a = 0; a < temp.length(); a++) {
                char ch = temp.charAt(a);
                if (ch == '\t')
                    counterForTabs++;
                if (counterForTabs == 7)
                    ooo = ooo.append( String.valueOf(temp.charAt(a)));
                if (counterForTabs == 8) {
                    contractor = ooo.toString();
                    break;
                }
            }



            if (b_content == true) {
                content = m_content.group(1);
                if (content == null) {
                    content = m_content.group(5);
                    if (content == null)
                        content = m_content.group(8);
                }
            }
            else
                content = "\tbrak danych\t";

            if (b_amount == true) {
                amount = m_amount.group(0);
                amount = amount.substring(0, amount.length() - 2);
            }
            else
                amount = "brak danych";

            String output = new StringBuilder().append(document)
                    .append(contractor)
                    .append(content).append(amount)
                    .append("\t").append(date).toString();
            temp = output;

        }
        return temp;
    }

    public String fileOther (String temp, String firstTwoChars, String listOfErrors) {

            Pattern p_content = Pattern.compile("(\\t[0-9]{3,})");
            Matcher m_content = p_content.matcher(temp);
            boolean b_content = m_content.find();
            String date = date(temp);

            String document = (listOfErrors);
            String contractor = "";
            String content = "";
            String amount = "";


            if (b_content == true)
                content = ("\t" + m_content.group(1));
            else
                content = "";


            String output = new StringBuilder().append(document)
                    .append(contractor)
                    .append(content).append(amount)
                    .append("\t").append(date).toString();
            temp = output;


        return temp;
    }

    public String fileBW (String temp1, String firstTwoChars, String listOfErrors) {

        String temp = temp1;

        if (firstTwoChars.equals("BW")) {
            Pattern p_content = Pattern.compile("((\\t.([a-zA-Z]{0,3})\\s[0-9]{0,6}\\/[a-zA-Z0-9]{0,3}\\/[0-9]{0,5}(.*?)\\t))|((\\t(Liczba).*[.][0-9]{2}\\t))"); //. represents single character
            Pattern p_amount = Pattern.compile("[0-9]{0,7}\\.[0-9]{4}");
            Pattern p_number = Pattern.compile("\\t[0-9]+\\t");//. represents single character

            Matcher m_content = p_content.matcher(temp);
            Matcher m_amount = p_amount.matcher(temp);
            boolean b_content = m_content.find();
            boolean b_amount = m_amount.find();
            String date = date(temp);

            // System.out.println("found: " + m.group(0));

            String document = ("BW " + listOfErrors);
            String contractor = "";
            String content = "";
            String amount = "";


            Integer total = 0;
            Integer counterForTabs = 0;
            String firstSeenChars = "";

            StringBuilder builderZipCode = new StringBuilder();
            StringBuilder builderNumber = new StringBuilder();
            String zipCode = "";

            if (b_amount == true) {
                amount = m_amount.group(0);
                amount = amount.substring(0, amount.length() - 2);
            }
            else
                amount = "brak danych";

            for (int a = 0; a < temp.length(); a++) {
                char ch = temp.charAt(a);
                if (ch == '\t') {
                    total++;
                    counterForTabs++;
                }
                if (counterForTabs == 7)
                    builderZipCode = builderZipCode.append(String.valueOf(temp.charAt(a)));
                if (counterForTabs == 8)
                    contractor = builderZipCode.toString();
                if (counterForTabs == 13 || counterForTabs == 14) {
                    builderNumber = builderNumber.append(String.valueOf(temp.charAt(a)));

                   // System.out.println(builderNumber);
                }
                if (counterForTabs == 14) {
                    Matcher m_number = p_number.matcher(builderNumber);
                    boolean b_number = m_number.matches();
                    if (b_number == false) {
                        String output = new StringBuilder().append(temp.substring(0, a))
                                .append(builderNumber).toString();
                        //temp = temp + String.valueOf("---\t" + temp.charAt(a));
                        temp = output;
                        //System.out.println(temp);
                    }
                    break;
                }
            }

            //System.out.println(temp);

            counterForTabs = 0;

            StringBuilder ooo = new StringBuilder();

            for (int a = 0; a < temp.length(); a++) {
                char ch = temp.charAt(a);
                if ( ch == '\t') {
                    total++; counterForTabs++;
                }
                if (counterForTabs == 14) {
                    ooo = ooo.append( String.valueOf(temp.charAt(a)));
                    //System.out.println(temp.charAt(a));
                }
                if (counterForTabs == 15) {
                    content = ooo.toString();
                    break;
                }

            }

            if (content == "")
                content = "\tbrak danych";



            /*if (b_content == true) {
                content = m_content.group(1);
                if (content == null)
                    content = m_content.group(5);
            }
            else
                content = "\tbrak danych\t";*/



            String output = new StringBuilder().append(document)
                    .append(contractor)
                    .append(content)
                    .append("\t").append(amount)
                    .append("\t").append(date).toString();
            temp = output;

        }
        return temp;
    }

    public void fileKP (String temp, Integer a, Integer counterForTabs, Integer total, String firstTwoChars) {

    }

    public String fileKW (String temp, String firstTwoChars, String listOfErrors) {

        if (listOfErrors.substring(0, 2).equals("KW")) {
            Pattern p_content = Pattern.compile("(\\t[a-zA-Z]{0,3}\\s[0-9]{0,6}\\/[a-zA-Z]{0,3}\\/[0-9]{4}+(.*?)\\t)|(\\t(Zwrot ).*)"); //. represents single character
            Pattern p_amount = Pattern.compile("[0-9]{0,7}\\.[0-9]{4}");

           // System.out.println(temp);

            Matcher m_content = p_content.matcher(temp);
            Matcher m_amount = p_amount.matcher(temp);
            boolean b_content = m_content.find();
            boolean b_amount = m_amount.find();
            String date = date(temp);

            // System.out.println("found: " + m.group(0));

            String document = (listOfErrors);

            String contractor = "";
            String content = "";
            String amount = "";

            date(temp);

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
                amount = amount.substring(0, amount.length() - 2);
            } else
                amount = "brak danych";

            if (flag == true) {
                String output = new StringBuilder().append(document)
                        .append(contractor)
                        .append(content).append(amount)
                        .append("\t").append(date).toString();
                temp = output;
            }
        }
            return temp;

    }

    public String fileFS (String temp, String firstTwoChars, String listOfErrors) {
            Pattern p_content = Pattern.compile("(\\t[0-9]{3,})");

            Matcher m_content = p_content.matcher(temp);;
            boolean b_content = m_content.find();
        String date = date(temp);

            String document = (listOfErrors);
            String contractor = "";
            String content = "";
            String amount = "";

            if (b_content == true) {
                content = m_content.group(1);
                if (content == null)
                    content = "\tbrak danych\t";
            }
            else {
                content = "\tbrak danych\t";
            }


            String output = new StringBuilder().append(document)
                    .append(contractor).append("\t")
                    .append(content).append(amount)
                    .append("\t").append(date).toString();
            temp = output;


        return temp;
    }

}
