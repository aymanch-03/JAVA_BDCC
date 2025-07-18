package org.ayman.chatapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;

    private ChatClient client;
    private Thread listenerThread;

    @FXML
    public void initialize() {
        try {
            client = new ChatClient("localhost", 12345);
            chatArea.appendText("[System] Connected to chat server.\n");
            listenerThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = client.readMessage()) != null) {
                        String finalMsg = msg;
                        Platform.runLater(() -> chatArea.appendText(finalMsg + "\n"));
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> chatArea.appendText("[System] Disconnected from server.\n"));
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();
        } catch (Exception e) {
            chatArea.appendText("[System] Could not connect to server.\n");
            inputField.setDisable(true);
        }
    }

    @FXML
    protected void onSendButtonClick() {
        String text = inputField.getText().trim();
        if (!text.isEmpty() && client != null) {
            client.sendMessage(text);
            chatArea.appendText("Me: " + text + "\n");
            inputField.clear();
        }
    }
}