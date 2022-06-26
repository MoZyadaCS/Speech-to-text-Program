package com.example.speechtotextwithgui;

public class Transcript {
    private String audio_url;
    private String id;
    private String text;
    private String language;
    private String status;

    // getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getAudio_URL() {
        return audio_url;
    }
    public void setAudio_URL(String audio_URL) {
        this.audio_url = audio_URL;
    }
}
