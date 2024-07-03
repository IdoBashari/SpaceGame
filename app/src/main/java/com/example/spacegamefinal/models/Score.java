package com.example.spacegamefinal.models;

public class Score {
    private int score;
    private String playerName;
    private double latitude;
    private double longitude;

    public Score(int score, String playerName, double latitude, double longitude) {
        this.score = score;
        this.playerName = playerName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public int getScore() {
        return score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Setters
    public void setScore(int score) {
        this.score = score;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return playerName + ": " + score;
    }
}
