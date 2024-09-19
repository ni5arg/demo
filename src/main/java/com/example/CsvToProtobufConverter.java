package com.example;

import com.google.protobuf.Message;
//import com.example.DeviceDataProto.DeviceData;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.time.Instant;
import java.util.List;
import java.time.LocalDate;

public class CsvToProtobufConverter {

    public static void main(String[] args) throws Exception {
        String csvFile = "./test-data/Employee Data.txt";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            reader.skip(1);//skip the header line

            //List<String[]> records = reader.readAll();
            System.out.println("Read the csv file");
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                
                int serial = Integer.parseInt(line[0]);

                // LocalDate date = LocalDate.parse(line[1]);
                // final Instant instant = java.sql.Timestamp.valueOf(date.atStartOfDay()).toInstant();        
                // Timestamp t = Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).build();
                dateValidator(line[1]);

                int age = Integer.parseInt(line[1]);
                String email = line[2];

                // Create User protobuf object
                User user = User.newBuilder()
                                .setName(name)
                                .setAge(age)
                                .setEmail(email)
                                .build();

                users.add(user);
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

    private static void dateValidator(String date) {
        // TODO Auto-generated method stub
        String[] args = date.replace("[A-Za-z ]","").split("-");
        if (args[0].length() != 4){
            throw new java.lang.Error("The date format is incorrect.");
        }
        if (args[1].length() == 2 ){
            int month = Integer.parseInt(args[1]);
            if (month < 1 || month > 12){
                throw new java.lang.Error("The date format is incorrect.");
            }
        }
        else{
            throw new java.lang.Error("The date format is incorrect.");
        }
        if (args[2].length() == 2 ){
            int day = Integer.parseInt(args[2]);
            if (day < 1 || day > 31){
                throw new java.lang.Error("The date format is incorrect.");
            }
        }
        else{
            throw new java.lang.Error("The date format is incorrect.");
        }
    }

    private static void sendToCloudService(byte[] protobufData) {
        // Send data to cloud service via gRPC or HTTP
    }
}
