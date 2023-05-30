package view;

import javafx.scene.layout.AnchorPane;
import model.ShootingBall;

public class Maps {
    public static void createMap1(AnchorPane gamePane) {
        new ShootingBall(gamePane, Math.PI / 6);
        new ShootingBall(gamePane, Math.PI / 2);
        new ShootingBall(gamePane, 5 * Math.PI / 6);
        new ShootingBall(gamePane, 7 * Math.PI / 6);
        new ShootingBall(gamePane, 3 * Math.PI / 2);
        new ShootingBall(gamePane, 11 * Math.PI / 6);
    }

    public static void createMap2(AnchorPane gamePane) {
        new ShootingBall(gamePane, 0);
        new ShootingBall(gamePane, 2 * Math.PI / 5);
        new ShootingBall(gamePane, 4 * Math.PI / 5);
        new ShootingBall(gamePane, 6 * Math.PI / 5);
        new ShootingBall(gamePane, 8 * Math.PI / 5);
    }

    public static void createMap3(AnchorPane gamePane) {
        new ShootingBall(gamePane, 0);
        new ShootingBall(gamePane, Math.PI / 2);
        new ShootingBall(gamePane, 3 * Math.PI / 4);
        new ShootingBall(gamePane, Math.PI);
        new ShootingBall(gamePane, 5 * Math.PI / 4);
        new ShootingBall(gamePane, 3 * Math.PI / 2);
    }

    public static double getX(double theta) {
        return 300 - 156 * Math.sin(theta);
    }

    public static double getY(double theta) {
        return 250 + 156 * Math.cos(theta);
    }

    public static double getTheta(double x, double y) {
        return Math.atan(-(x - 300) / (y - 250));
    }
}
