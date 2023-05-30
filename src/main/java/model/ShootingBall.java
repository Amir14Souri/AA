package model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import view.Maps;

public class ShootingBall extends Circle {
    public ShootingBall(int number, Game game, User user, AnchorPane gamePane) {
        super(300, 580, 10, Color.BLACK);
        this.number = number;
        this.game = game;
        this.user = user;
        this.isShot = false;

        int indent = 0;
        if (number >= 10) indent = 4;
        this.numberText = new Text(Integer.toString(number));
        this.numberText.setFill(Color.WHITE);
        AnchorPane.setLeftAnchor(this.numberText, 297.0 - indent);
        AnchorPane.setTopAnchor(this.numberText, 571.0);
        this.numberText.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 13));
        this.numberText.setViewOrder(-1);
        gamePane.getChildren().add(this.numberText);
    }

    public ShootingBall(AnchorPane gamePane, double theta) {
        super(Maps.getX(theta), Maps.getY(theta), 10, Color.BLACK);
        this.isShot = true;
        this.number = -1;
        this.rotatedDegree = theta;
        this.line = new Line(Maps.getX(theta), Maps.getY(theta), 300, 250);
        gamePane.getChildren().add(line);
        gamePane.getChildren().add(this);
    }

    public ShootingBall(double theta, int number, User user) {
        super(Maps.getX(theta), Maps.getY(theta), 10, Color.BLACK);
        this.number = number;
        this.user = user;
        this.line = new Line(Maps.getX(theta), Maps.getY(theta), 300, 250);
        this.isShot = true;
        this.rotatedDegree = theta;
        if (number != -1) {
            int indent = 0;
            if (number >= 10) indent = 4;
            this.numberText = new Text(Integer.toString(number));
            this.numberText.setFill(Color.WHITE);
            AnchorPane.setTopAnchor(this.numberText, this.getCenterY() - 9);
            AnchorPane.setLeftAnchor(this.numberText, this.getCenterX() - 3);
            this.numberText.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 13));
            this.numberText.setViewOrder(-1);
        }
    }

    private int number;
    private Game game;
    private User user;
    private Text numberText;
    private Line line;
    private boolean isShot;
    private double rotatedDegree;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Text getNumberText() {
        return numberText;
    }

    public void setNumberText(Text numberText) {
        this.numberText = numberText;
    }

    public boolean isShot() {
        return isShot;
    }

    public void setShot(boolean shot) {
        isShot = shot;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public double getRotatedDegree() {
        return rotatedDegree;
    }

    public void addRotatedDegree(int direction) {
        rotatedDegree += direction * Math.PI / 180;
    }

    public void setRotatedDegree(double rotatedDegree) {
        this.rotatedDegree = rotatedDegree;
    }
}

