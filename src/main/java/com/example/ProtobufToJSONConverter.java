package com.example;

import com.google.protobuf.util.JsonFormat;  // For converting protobuf to JSON

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import com.example.Schema.Employee;
import java.net.URL;

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

            // POST over HTTP
            postOverHTTP(json);

            fis.close();
        } 
        catch (IOException e) {
                e.printStackTrace();
        }
    }

    public static void postOverHTTP(String jsonInputString) {
        String urlString = "https://example.com/api";  // Replace with the actual URL

        try {
            // Step 1: Create the URL object
            URL url = new URL(urlString);

            // Step 2: Open a connection to the URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Step 3: Set the request method to POST
            con.setRequestMethod("POST");

            // Step 4: Set the required headers for JSON
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            // Step 5: Enable output for the connection (for sending the JSON payload)
            con.setDoOutput(true);

            // Step 6: Write the JSON data to the output stream
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Step 7: Get the response code to confirm the request was successful
            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code: " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
