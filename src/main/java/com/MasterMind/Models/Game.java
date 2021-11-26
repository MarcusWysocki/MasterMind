package com.MasterMind.Models;

public class Game {

    private int id;
    private String timeStarted;
    private boolean completed;
    private int noGuesses;
    private int[] answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(String timeStarted) {
        this.timeStarted = timeStarted;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getNoGuesses() {
        return noGuesses;
    }

    public void setNoGuesses(int guesses) {this.noGuesses = guesses;}

    public void incNoGuesses() {
        this.noGuesses++;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public String getString() {
        int ans = answer[0] * 1000 + answer[1] * 100 + answer[2] * 10 + answer[3];
        String ret = id + "," + timeStarted + "," + completed + "," + noGuesses + "," + ans;

        return ret;
    }
}
