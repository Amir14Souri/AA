package model;

import model.enumerations.Difficulty;

import java.util.Random;

public class Game {
    public Game(User user) {
//        TODO: double check
        this.user = user;
        this.difficulty = user.getSelectedDifficulty();
        this.point = 0;
        this.elapsedTime = 0;
        this.currentBallNumber = user.getSelectedBallNumber();
        this.numberOfBalls = user.getSelectedBallNumber();
        this.mapNumber = user.getSelectedMapNumber();
        this.currentRotateDirection = new Random().nextInt(2) * 2 - 1;
        this.currentPhase = 1;
        this.currentShootAngle = 0;
        this.currentFreezeState = 0;
        this.timerSeconds = user.getSelectedDifficulty().getTimerSeconds();
    }

    public Game() {
        this.difficulty = Difficulty.AVERAGE;
        this.point = 0;
        this.elapsedTime = 0;
        this.currentBallNumber = 5;
        this.numberOfBalls = 5;
        this.mapNumber = 1;
        this.currentRotateDirection = new Random().nextInt(2) * 2 - 1;
        this.currentPhase = 1;
        this.currentShootAngle = 0;
        this.currentFreezeState = 0;
        this.timerSeconds = Difficulty.AVERAGE.getTimerSeconds();
    }

    public Game(User user, Difficulty difficulty, int point, double elapsedTime, int currentBallNumber, int numberOfBalls,
                int currentRotateDirection, int currentPhase, double currentShootAngle, double currentFreezeState, int timerSeconds) {
        this.user = user;
        this.difficulty = difficulty;
        this.point = point;
        this.elapsedTime = elapsedTime;
        this.currentBallNumber = currentBallNumber;
        this.numberOfBalls = numberOfBalls;
        this.currentRotateDirection = currentRotateDirection;
        this.currentPhase = currentPhase;
        this.currentShootAngle = currentShootAngle;
        this.currentFreezeState = currentFreezeState;
        this.timerSeconds = timerSeconds;
    }

    private User user;
    private Difficulty difficulty;
    private int point;
    private double elapsedTime;
    private int currentBallNumber;
    private int numberOfBalls;
    private int mapNumber;
    private int currentRotateDirection;
    private int currentPhase;
    private double currentShootAngle;
    private double currentFreezeState;
    private int timerSeconds;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void addTime(double time) {
        this.elapsedTime += time;
    }

    public int getCurrentBallNumber() {
        return currentBallNumber;
    }

    public void setCurrentBallNumber(int currentBallNumber) {
        this.currentBallNumber = currentBallNumber;
    }

    public int getNumberOfBalls() {
        return numberOfBalls;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setNumberOfBalls(int numberOfBalls) {
        this.numberOfBalls = numberOfBalls;
    }

    public int getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(int mapNumber) {
        this.mapNumber = mapNumber;
    }

    public int getCurrentRotateDirection() {
        return currentRotateDirection;
    }

    public void setCurrentRotateDirection(int currentRotateDirection) {
        this.currentRotateDirection = currentRotateDirection;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
    }

    public double getCurrentShootAngle() {
        return currentShootAngle;
    }

    public void setCurrentShootAngle(double currentShootAngle) {
        this.currentShootAngle = currentShootAngle;
    }

    public double getCurrentFreezeState() {
        return currentFreezeState;
    }

    public void setCurrentFreezeState(double currentFreezeState) {
        this.currentFreezeState = currentFreezeState;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    public void setTimerSeconds(int timerSeconds) {
        this.timerSeconds = timerSeconds;
    }
}
