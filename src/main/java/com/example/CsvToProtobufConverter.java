package com.example;

import com.example.Schema.Employee;
import com.google.protobuf.Message;
//import com.example.DeviceDataProto.DeviceData;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;

public class CsvToProtobufConverter {

    public static void main(String[] args) throws Exception {
        System.out.println(dateValidator("2024-09-15"));
        System.out.println(dateValidator("2024-09-19"));
        System.out.println(dateValidator("2024-09-23"));
        System.out.println(dateValidator("2024-10-15"));

        System.out.println(dateValidator("2023-09-15"));
        System.out.println(dateValidator("2025-09-19"));


        String csvFile = "./test-data/Employee Data.txt";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            reader.skip(1);//skip the header line

            System.out.println("Read the csv file");
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                
                int serial = Integer.parseInt(line[0]);
                //String[] date = line[1].split("-");
                // LocalDate date = LocalDate.parse(line[1]);
                // final Instant instant = java.sql.Timestamp.valueOf(date.atStartOfDay()).toInstant();        
                // Timestamp t = Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).build();
                dateValidator(line[1]);

                int age = Integer.parseInt(line[1]);
                String email = line[2];

                // Create User protobuf object
                // Employee employee = Employee.newBuilder()
                //                 .setSerial(serial)
                //                 .setAge(age)
                //                 .setEmail(email)
                //                 .build();

                // users.add(user);
            }
            // for (String[] record : records) {
            //     DeviceData deviceData = DeviceData.newBuilder()
            //             .setSerial(record[0])
            //             .setDate(record[1])
            //             .setType(record[2])
            //             .addAllValues(parseValues(record))
            //             .build();

            //     // Transmit deviceData as Protobuf
            //     byte[] protobufData = deviceData.toByteArray();
            //     sendToCloudService(protobufData);
            // }
        }
    }

    // private static List<Float> parseValues(String[] record) {
    //     // Parse and return values as Float list
    // }

    private static boolean dateValidator(String date) throws ParseException {
        // TODO Auto-generated method stub
        //return new SimpleDateFormat("yyyy-mm-dd").parse(date).compareTo(new Date()); // must be -1 for the date to be valid i.e in the past
        //Date d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return new SimpleDateFormat("yyyy-MM-dd").parse(date).before(new Date());
    }

    private static void sendToCloudService(byte[] protobufData) {
        // Send data to cloud service via gRPC or HTTP
    }
}
