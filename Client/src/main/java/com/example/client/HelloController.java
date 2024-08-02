package com.example.client;

import javafx.fxml.FXML;
import java.io.*;
import java.net.*;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class HelloController {
    private Socket socket; // socket that will be used to establish a connection to the server.
    private PrintWriter out; // send data messages to the server.
    private BufferedReader in; // read data messages from the server.

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField inputField;

    @FXML
    public void initialize() {
        new Thread(this::startClient).start();
    }

    //create and start a new thread
    private void startClient() {
        try {
            //creates a new socket and connects it to the server running on localhost at port 12345.
            socket = new Socket(InetAddress.getLocalHost(), 12345);
            //send data to the server
            //parameter enables auto-flushing,
            // PrintWriter will flush its output buffer automatically after each println call.
            out = new PrintWriter(socket.getOutputStream(), true);
            //initializes the BufferedReader to read data from the server.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                String finalMessage = message;
                //This ensures that updating the messageArea is done on the JavaFX application thread,
                Platform.runLater(() -> messageArea.appendText(finalMessage + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage() {
        String message = inputField.getText();
        out.println(message);
        inputField.clear();
    }
}