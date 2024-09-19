package com.example;

import com.google.protobuf.util.JsonFormat;  // For converting protobuf to JSON

import java.io.FileInputStream;
import java.io.IOException;

import com.example.Schema.Employee;
import com.example.Schema.Employee.CalendarDate;
import com.google.protobuf.Message;

public class ProtobufToJSONConverter {

    public static void main(String[] args) {
        String binaryFilePath = "./outputData/98765432.bin";

        try {
            // Step 1: Read the binary file and parse it to a User protobuf object
            FileInputStream fis = new FileInputStream(binaryFilePath);
            Employee employee = Employee.parseFrom(fis);

            // Step 2: Convert the protobuf object to JSON
            String json = JsonFormat.printer().print(employee);

            // Step 3: Output the JSON string
            System.out.println("JSON Output:\n" + json);

            fis.close();
        } 
        catch (IOException e) {
                e.printStackTrace();
        }
    }
}
