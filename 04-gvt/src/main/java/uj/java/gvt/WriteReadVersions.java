package uj.java.gvt;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uj.java.gvt.Gvt.*;

public class WriteReadVersions {
    static void writeLastVersion(){

        File f = new File(lastVersionPath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))){
            bw.write(Long.toString(lastVersion));
            bw.newLine();
            bw.write(Long.toString(highestVersion));
            bw.newLine();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static void readLastVersion(){
        try(BufferedReader reader = new BufferedReader(new FileReader(lastVersionPath))) {
            lastVersion = Long.parseLong(reader.readLine());
            highestVersion = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeVersions(){
        File f = new File(versionsPath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))){
            for (GvtVersion ver: gvtVersions) {
                bw.write(String.valueOf(ver.number()));
                bw.newLine();
                int rows = countLines(ver.message());
                bw.write(String.valueOf(rows));
                bw.newLine();
                bw.write(ver.message());
                bw.newLine();
                bw.write(String.valueOf(ver.addedFiles().size()));
                bw.newLine();
                for(String fil: ver.addedFiles()){
                    bw.write(fil);
                    bw.newLine();
                }
                bw.write(String.valueOf(ver.committedFiles().size()));
                bw.newLine();
                for(String fil: ver.committedFiles()){
                    bw.write(fil);
                    bw.newLine();
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static void readVersions(){
        try(BufferedReader reader = new BufferedReader(new FileReader(versionsPath))) {
            String number = reader.readLine();
            while (number != null) {
                int rows = Integer.parseInt(reader.readLine());

                StringBuilder message = new StringBuilder(reader.readLine());
                for (int i = 1; i < rows; i++){
                    message.append("\n").append(reader.readLine());
                }

                String listSizeString = reader.readLine();
                int listSize = Integer.parseInt(listSizeString);
                ArrayList<String> tmpAddedList = new ArrayList<>();
                for(int i = 0; i < listSize; i++){
                    tmpAddedList.add(reader.readLine());
                }

                listSizeString = reader.readLine();
                listSize = Integer.parseInt(listSizeString);
                ArrayList<String> tmpCommittedList = new ArrayList<>();
                for(int i = 0; i < listSize; i++){
                    tmpCommittedList.add(reader.readLine());
                }
                GvtVersion tmp = new GvtVersion(Long.parseLong(number), message.toString(), tmpAddedList, tmpCommittedList);
                gvtVersions.add(tmp);
                number = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int countLines(String input){
        Matcher m = Pattern.compile("\r\n|\r|\n").matcher(input);
        int lines = 1;
        while (m.find())
        {
            lines ++;
        }
        return lines;
    }

}
