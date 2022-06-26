package com.example.speechtotextwithgui;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloController {
    @FXML
    private TextField url;
    @FXML
    private Button submitButton;


    public void setSubmitButtonClicked() {
        String url = this.url.getText();
        String text = "";
        try {
            text = speechToText(url);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        Stage stage = new Stage();
        TextArea textArea = new TextArea();
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        Scene scene = new Scene(textArea, 400, 400);
        stage.setTitle("Audio transcript");
        stage.setScene(scene);
        stage.show();
    }

    public static String speechToText(String audio_url) throws URISyntaxException, IOException, InterruptedException {
        String apiUrl = "https://api.assemblyai.com/v2/transcript";
        String AuthKey = "a19e504a6edf4fee9613b4b0d1a4152e";
        Gson gson = new Gson();
        Transcript transcript = new Transcript();
        transcript.setAudio_URL(audio_url);
        HttpRequest postReq = HttpRequest.newBuilder().uri(new URI(apiUrl))
                .header("authorization", AuthKey)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(transcript))).build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = null;
        try {
            postResponse =  client.send(postReq, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transcript = gson.fromJson(postResponse.body(), Transcript.class);
        while(true){
            String apiGetUrl = "https://api.assemblyai.com/v2/transcript/" + transcript.getId();
            HttpRequest getReq = HttpRequest.newBuilder().uri(new URI(apiGetUrl))
                    .header("authorization", AuthKey)
                    .GET().build();
            HttpResponse<String> getResponse = client.send(getReq, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);
            if(transcript.getStatus().equals("completed")){
                break;
            }
            Thread.sleep(1000);
        }
        return transcript.getText();
    }
}