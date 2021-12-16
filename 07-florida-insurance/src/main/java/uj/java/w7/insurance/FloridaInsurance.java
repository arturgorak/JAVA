package uj.java.w7.insurance;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FloridaInsurance {

    static List <InsuranceEntry> insuranceEntryList = new ArrayList<>();

    public static void main(String[] args) {

        fillInsuranceEntryList();
        count();
        sum();
        most_valuable();
    }

    static void fillInsuranceEntryList(){
        try(ZipFile zipFile = new ZipFile("FL_insurance.csv.zip")) {
            ZipEntry zipEntry = zipFile.getEntry("FL_insurance.csv");
            InputStream inputStream = zipFile.getInputStream(zipEntry);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            var line = br.readLine();
            line = br.readLine();

            while(line != null) {
                insuranceEntryList.add(makeInstance(line));
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void count() {
        long count = insuranceEntryList
                .stream()
                .map(InsuranceEntry::county)
                .distinct()
                .count();
        makeFileWithSingleLine("count.txt", count);

    }

    static void sum() {
        BigDecimal sum = insuranceEntryList
                .stream()
                .map(InsuranceEntry::tiv_2012)
                .reduce(new BigDecimal(0), BigDecimal::add);
        makeFileWithSingleLine("tiv2012.txt", sum);

    }

    static void most_valuable(){
        File file = new File("most_valuable.txt");
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            writeLineToFile(bw,"country,value");
            insuranceEntryList
                    .stream()
                    .collect(Collectors.groupingBy(
                            InsuranceEntry::county,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    InsuranceEntry::sub_tiv,
                                    BigDecimal::add)))
                    .entrySet()
                    .stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                    .limit(10)
                    .forEach(entry -> writeLineToFile(bw,entry.getKey() + "," + entry.getValue()));

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static void writeLineToFile(BufferedWriter bw, String line){
        try {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void makeFileWithSingleLine(String filename, Number value) {
        File file = new File(filename);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            writeLineToFile(bw, String.valueOf(value));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static InsuranceEntry makeInstance(String line){
        String[] data = line.split(",");

//        data[0] = policyID
//        data[2] = county
//        data[7] = tiv_2011
//        data[8] = tiv_2012

        return new InsuranceEntry(Integer.parseInt(data[0]), data[2], new BigDecimal(data[7]), new BigDecimal(data[8]));
    }

}
