package com.example.spacejam;

import androidx.annotation.NonNull;
import java.io.Serializable;

class Score implements Serializable {
    private int playerScore;
    private String playerName = "";
    private double latitude;
    private double longitude;

    public Score(String playerName, int score, double lat, double lon) {
        setPlayerScore(score);
        setPlayerName(playerName);
        setLatitude(lat);
        setLongitude(lon);
    }

    public int compareTo(Score player){
        if(this.playerScore > player.playerScore)
            return 1;
        else
            return 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Score{" +
                "playerScore=" + playerScore +
                ", playerName='" + playerName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
