package com.example.spacegamefinal.models;

public class Score implements Comparable<Score> {
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

    @Override
    public String toString() {
        return playerName + ": " + score;
    }

    @Override
    public int compareTo(Score other) {
        return Integer.compare(other.score, this.score);
    }
}