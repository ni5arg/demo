package com.example;

import com.example.Schema.Employee;
import com.example.Schema.Employee.CalendarDate;
import com.google.protobuf.Message;
//import com.example.DeviceDataProto.DeviceData;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList; // import the ArrayList class

public class CsvToProtobufConverter {

    public static void main(String[] args) throws Exception {
        // System.out.println(dateValidator("2024-09-15"));
        // System.out.println(dateValidator("2024-09-19"));
        // System.out.println(dateValidator("2024-09-23"));
        // System.out.println(dateValidator("2024-10-15"));

        // System.out.println(dateValidator("2023-09-15"));
        // System.out.println(dateValidator("2025-09-19"));
        System.out.println(dateValidator("2024-09-15"));

        String csvFile = "./test-data/Employee Data.txt";


        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            reader.skip(1);//skip the header line
            
            System.out.println("Read the csv file");
            
            String[] line;
            

            while ((line = reader.readNext()) != null) {
                
                String fullEntry = String.join(",", line);
                System.out.println(fullEntry);

                String valueString = fullEntry.substring(fullEntry.indexOf("[", 0)+1, fullEntry.indexOf("]", 0));
                String[] values = valueString.split(",");
                int[] valueArray = new int[values.length];
                try {
                    for(int i=0; i <= values.length; i++) {
                        valueArray[i] = Integer.parseInt(values[i]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int serial = Integer.parseInt(line[0]);
                int[] date = dateValidator(line[1]);
                String type = line[2];
                Integer[] finalValues = valueValidator(type, valueArray);

                // Create User protobuf object
                CalendarDate dateExample =  CalendarDate.newBuilder()
                                        .setYear(date[0])
                                        .setMonth(date[1])
                                        .setDay(date[2])
                                        .build();


                List<Integer> list = Arrays.asList(finalValues);

                Employee employee = Employee.newBuilder()
                                    .setSerial(serial)
                                    .setDate(dateExample)
                                    .setType(type)
                                    .addAllValues(list)
                                    .build();

                users.add(user);

            }

        }
    }

    // private static List<Float> parseValues(String[] record) {
    //     // Parse and return values as Float list
    // }

    private static Integer[] valueValidator(String type, int[] values) {
        // TODO Auto-generated method stub
        if(type == "Summary ") {
            if(values.length != 3){
                throw new java.lang.Error("Summary values are incorrect.");
            }
        }
        else if (type == "Detail"){
            if(values.length != 6){
                throw new java.lang.Error("Detail values are incorrect.");
            }
        }
        // convert to iterables for later
        Integer[] numbers = new Integer[values.length];
        int i = 0;
        for (int value : values) {
            numbers[i++] = Integer.valueOf(value);
        }

        return numbers;
    }

    private static int[] dateValidator(String dateString) {
        // TODO Auto-generated method stub

        int[] date = new int[3];
        try {
            if (new SimpleDateFormat("yyyy-MM-dd").parse(dateString).before(new Date())){

                date[0] = Integer.parseInt(dateString.split("-")[0]);
                date[1] = Integer.parseInt(dateString.split("-")[1]);
                date[2] = Integer.parseInt(dateString.split("-")[2]);
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    private static void sendToCloudService(byte[] protobufData) {
        // Send data to cloud service via gRPC or HTTP
    }
}
