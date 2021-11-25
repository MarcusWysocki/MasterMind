package com.MasterMind.Models;

public class Guess {

    private int id;
    private int gameId;
    private int[] guess; //The [4] array containing the actual guess
    private String result; // the [2] array containing exact and partial
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int[] getGuess() {
        return guess;
    }

    public void setGuess(int[] guess) {
        this.guess = guess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
