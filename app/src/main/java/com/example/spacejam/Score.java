package com.example.spacejam;

import androidx.annotation.NonNull;
import java.io.Serializable;

class Score implements Serializable {
    private int playerScore;
    private String playerName = "";

    public Score(String playerName) {
        this.playerScore = 0;
        this.playerName.concat(playerName);
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
        return this.playerName + "-->" + this.playerScore + "\n";
    }

}
