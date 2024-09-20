package com.example;

import com.example.Schema.Employee;
import com.example.Schema.Employee.CalendarDate;
import com.opencsv.CSVReader;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.google.protobuf.util.JsonFormat;  // For converting protobuf to JSON

public class CsvToProtobufConverter {

    public static void main(String[] args) throws Exception {

        String csvFile = "./test-data/Employee Data.txt";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            reader.skip(1);//skip the header line
            
            List<Employee> employees = new ArrayList<>();
            String[] line;
            while ((line = reader.readNext()) != null) {
                
                String fullEntry = String.join(",", line);

                String valueString = fullEntry.substring(fullEntry.indexOf("[", 0)+1, fullEntry.indexOf("]", 0));
                String[] values = valueString.split(",");
                int[] valueArray = new int[values.length];
                try {
                    for(int i=0; i < values.length; i++) {
                        valueArray[i] = Integer.parseInt(values[i]);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }

                // Populate the proto fields
                int serial = Integer.parseInt(line[0]);
                //try{
                int[] date;
                try{
                    date = dateValidator(line[1]);
                }
                catch (dateException e){
                    e.printStackTrace();
                    continue;
                }
                String type = line[2];
                Integer[] finalValues = valueValidator(type, valueArray);

                // Create Date protobuf object
                CalendarDate dateExample =  CalendarDate.newBuilder()
                                        .setYear(date[0])
                                        .setMonth(date[1])
                                        .setDay(date[2])
                                        .build();

                // Create Values protobuf object
                List<Integer> list = Arrays.asList(finalValues);

                // Create Employee protobuf object
                Employee employee = Employee.newBuilder()
                                    .setSerial(serial)
                                    .setDate(dateExample)
                                    .setType(type)
                                    .addAllValues(list)
                                    .build();

                employees.add(employee);

                
                

                // Messaging
                byte[] protobufData = employee.toByteArray();
                sendToCloudService(protobufData);

                try (FileOutputStream fos = new FileOutputStream("./outputData/protobuf")) {
                    fos.write(protobufData);
                }

                //Sanity check
                // Convert protobuf message to JSON
                String jsonString = JsonFormat.printer().includingDefaultValueFields().print(employee);

                // Output the JSON string
                System.out.println("Protobuf as JSON:");
                System.out.println(jsonString);
            }

            // Serialize entire batch protobuf messages to a binary file
            for (Employee e : employees) {
                byte[] protobufData = e.toByteArray();
                try (FileOutputStream fos = new FileOutputStream("./outputData/" + e.getSerial() + ".bin")) {
                    fos.write(protobufData);
                }
                //Files.write(Paths.get(e.getSerial() + ".bin"), protobufData);
            }
        }
    }

    private static Integer[] valueValidator(String type, int[] values) {
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

    private static int[] dateValidator(String dateString) throws dateException {

        int[] date = new int[3];
        try {
            if (new SimpleDateFormat("yyyy-MM-dd").parse(dateString).before(new Date())){

                date[0] = Integer.parseInt(dateString.split("-")[0]);
                date[1] = Integer.parseInt(dateString.split("-")[1]);
                date[2] = Integer.parseInt(dateString.split("-")[2]);
            }
            else{
                throw new dateException("Date is not in the past.");
            }
        } 
        catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("format error");
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("parsing error");
        }
        return date;
    }

    private static void sendToCloudService(byte[] protobufData) {
        // Send data to cloud service
    }
}
