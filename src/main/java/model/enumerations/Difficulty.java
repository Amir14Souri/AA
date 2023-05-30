package model.enumerations;

public enum Difficulty {
    EASY("Easy", 10, 8, 7, 0.8, 80),
    AVERAGE("Average", 7, 6, 5, 1, 55),
    HARD("Hard", 5, 4, 3, 1.4, 30);

    private String name;
    private double millisPerDegree;
    private double angleChangeInterval;
    private long freezeTime;
    private double pointCoefficient;
    private int timerSeconds;

    private Difficulty(String name, double millisPerDegree, double windSpeed, long freezeTime, double pointCoefficient, int timerSeconds) {
        this.name = name;
        this.millisPerDegree = millisPerDegree;
        this.angleChangeInterval = windSpeed;
        this.freezeTime = freezeTime;
        this.pointCoefficient = pointCoefficient;
        this.timerSeconds = timerSeconds;
    }

    public String getName() {
        return name;
    }

    public double getMillisPerDegree() {
        return millisPerDegree;
    }

    public double getAngleChangeInterval() {
        return angleChangeInterval;
    }

    public long getFreezeTime() {
        return freezeTime;
    }

    public double getPointCoefficient() {
        return pointCoefficient;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    public static Difficulty getDifficultyByName(String name) {
        if (name.equals("Easy")) return Difficulty.EASY;
        if (name.equals("Average")) return Difficulty.AVERAGE;
        if (name.equals("Hard")) return Difficulty.HARD;
        return null;
    }
}
