package model;

import model.enumerations.Difficulty;

import java.util.ArrayList;

public class User {
    public User(String username, String password, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.selectedDifficulty = Difficulty.AVERAGE;
        this.selectedBallNumber = 5;
        this.selectedMapNumber = 1;
        this.avatarUrl = avatarUrl;
        this.selectedAudioUrl = "src/main/resources/audios/WondrousWaters.mp3";
        this.savedGame = null;
        this.selectedShootButton = "Space";
        this.selectedFreezeButton = "Tab";
    }

    private String username;
    private String password;
    private String avatarUrl;
    private Game bestGame;
    private int bestGamePoint;
    private double bestGameElapsedTime;
    private int rank;
    private Difficulty selectedDifficulty;
    private int selectedBallNumber;
    private int selectedMapNumber;
    private String selectedAudioUrl;
    private String selectedShootButton;
    private String selectedFreezeButton;
    private Game savedGame;
    private ArrayList<ShootingBall> savedGameBalls = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Game getBestGame() {
        return bestGame;
    }

    public void setBestGame(Game bestGame) {
        this.bestGame = bestGame;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void setSelectedDifficulty(Difficulty selectedDifficulty) {
        this.selectedDifficulty = selectedDifficulty;
    }

    public int getSelectedBallNumber() {
        return selectedBallNumber;
    }

    public void setSelectedBallNumber(int selectedBallNumber) {
        this.selectedBallNumber = selectedBallNumber;
    }

    public int getBestGamePoint() {
        return bestGamePoint;
    }

    public void setBestGamePoint(int bestGamePoint) {
        this.bestGamePoint = bestGamePoint;
    }

    public double getBestGameElapsedTime() {
        return bestGameElapsedTime;
    }

    public void setBestGameElapsedTime(double bestGameElapsedTime) {
        this.bestGameElapsedTime = bestGameElapsedTime;
    }

    public int getSelectedMapNumber() {
        return selectedMapNumber;
    }

    public void setSelectedMapNumber(int selectedMapNumber) {
        this.selectedMapNumber = selectedMapNumber;
    }

    public String getSelectedAudioUrl() {
        return selectedAudioUrl;
    }

    public void setSelectedAudioUrl(String selectedAudioUrl) {
        this.selectedAudioUrl = selectedAudioUrl;
    }

    public boolean isHasSavedGame() {
        return savedGame != null;
    }

    public ArrayList<ShootingBall> getSavedGameBalls() {
        return savedGameBalls;
    }

    public void addSavedGameBall(ShootingBall ball) {
        this.savedGameBalls.add(ball);
    }

    public Game getSavedGame() {
        return savedGame;
    }

    public void setSavedGame(Game savedGame) {
        this.savedGame = savedGame;
    }

    public String getSelectedShootButton() {
        return selectedShootButton;
    }

    public void setSelectedShootButton(String selectedShootButton) {
        this.selectedShootButton = selectedShootButton;
    }

    public String getSelectedFreezeButton() {
        return selectedFreezeButton;
    }

    public void setSelectedFreezeButton(String selectedFreezeButton) {
        this.selectedFreezeButton = selectedFreezeButton;
    }

    public void refreshGame(Game game) {
        if (bestGame == null) {
            bestGame = game;
            bestGamePoint = game.getPoint();
            bestGameElapsedTime = game.getElapsedTime();
        } else {
            if (game.getPoint() > bestGame.getPoint()) {
                bestGame = game;
                bestGamePoint = game.getPoint();
                bestGameElapsedTime = game.getElapsedTime();
            } else if (game.getPoint() == bestGame.getPoint() && game.getElapsedTime() <= bestGame.getElapsedTime()) {
                bestGame = game;
                bestGamePoint = game.getPoint();
                bestGameElapsedTime = game.getElapsedTime();
            }
        }
    }
}