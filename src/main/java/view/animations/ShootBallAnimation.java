package view.animations;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.ShootingBall;
import view.GameMenu;
import view.Maps;
import view.SettingsMenu;

public class ShootBallAnimation extends Transition {
    private ShootingBall ball;
    private ProgressBar freezeBar;
    private ProgressBar ballBar;
    private Text point;
    private double angle;

    public ShootBallAnimation(ShootingBall ball, ProgressBar freezeBar, ProgressBar ballBar, Text point, double angle) {
        this.ball = ball;
        this.freezeBar = freezeBar;
        this.point = point;
        this.angle = angle;
        this.ballBar = ballBar;
        this.setCycleDuration(Duration.millis(3));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
        if (ball.getLine() != null) return;
        double ballY = ball.getCenterY() - 1;
        double ballX = ball.getCenterX() + Math.tan(angle);

        for (int i = 0; i < GameMenu.gamePane.getChildren().size(); i++) {
            Node node = GameMenu.gamePane.getChildren().get(i);
            if (node instanceof ShootingBall && !node.equals(this.ball) && ((ShootingBall) node).isShot()) {
                if (node.getBoundsInParent().intersects(this.ball.getBoundsInParent())) {
                    try {
                        GameMenu.endGame(false);
                        return;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        if (getDistanceFromCenter(ballX, ballY) <= 155) {
            if (ball.getNumber() == GameMenu.game.getNumberOfBalls()) {
                GameMenu.startTime = System.currentTimeMillis();
                GameMenu.updateTimerTimeline.play();
            }
            GameMenu.game.addPoint((int) (Math.round(GameMenu.game.getCurrentPhase() * 5 * GameMenu.game.getDifficulty().getPointCoefficient())));
            String pointWord = (SettingsMenu.isEnglish) ? "Point: " : "امتیاز: ";
            point.setText(pointWord + GameMenu.game.getPoint());
            int whichBall = GameMenu.game.getNumberOfBalls() - ball.getNumber() + 1;
            if (whichBall >= Math.ceil(0.25 * GameMenu.game.getNumberOfBalls())) {
                GameMenu.game.setCurrentPhase(2);
                ballBar.getStyleClass().add("orangeBar");
            }
            if (whichBall >= Math.ceil(0.5 * GameMenu.game.getNumberOfBalls())) {
                GameMenu.game.setCurrentPhase(3);
                ballBar.getStyleClass().add("yellowBar");
            }
            if (whichBall >= Math.ceil(0.75 * GameMenu.game.getNumberOfBalls())) {
                GameMenu.game.setCurrentPhase(4);
                ballBar.getStyleClass().add("greenBar");
            }
            if (GameMenu.game.getCurrentPhase() > 1) {
                if (GameMenu.changeDirectionTimeline != null) GameMenu.changeDirectionTimeline.stop();
                GameMenu.changeRotateDirection(1);
                if (GameMenu.changeBallsRadiusTimeline != null) GameMenu.changeBallsRadiusTimeline.stop();
                GameMenu.changeBallsRadius();
            }
            if (GameMenu.game.getCurrentPhase() > 2) {
                if (GameMenu.changeVisibilityTimeline != null) GameMenu.changeVisibilityTimeline.stop();
                GameMenu.changeVisibility();
            }
            if (GameMenu.game.getCurrentPhase() > 3) {
                if (GameMenu.changeShootAngleTimeline != null) GameMenu.changeShootAngleTimeline.stop();
                GameMenu.changeShootAngle();
            }

            Media soundEffect = new Media(getClass().getResource("/audios/shootBall.wav").toExternalForm());
            MediaPlayer effectPlayer = new MediaPlayer(soundEffect);
            effectPlayer.setVolume(300);
            effectPlayer.play();

            if (ball.getNumber() == 1) {
                try {
                    GameMenu.endGame(true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if (this.freezeBar.getProgress() < 1) {
                this.freezeBar.setProgress(freezeBar.getProgress() + 0.2);
                GameMenu.game.setCurrentFreezeState(freezeBar.getProgress());
            }
            ball.setShot(true);
            Line line = new Line(ball.getCenterX(), ball.getCenterY(), 300, 250);
            ball.setLine(line);
            GameMenu.gamePane.getChildren().add(line);
            ball.setLine(line);
            ball.setRotatedDegree(Maps.getTheta(ball.getCenterX(), ball.getCenterY()));
            this.stop();
            return;
        }
        ball.setCenterY(ballY);
        ball.setCenterX(ballX);
        AnchorPane.setTopAnchor(ball.getNumberText(), ballY - 9);
        if (ball.getNumber() < 10) AnchorPane.setLeftAnchor(ball.getNumberText(), ballX - 3);
        else AnchorPane.setLeftAnchor(ball.getNumberText(), ballX - 7);

        if (ballX <= 10 || ballX >= 590 || ballY <= 10) {
            try {
                GameMenu.endGame(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static double getDistanceFromCenter(double x, double y) {
        return Math.sqrt(Math.pow(x - 300, 2) + Math.pow(y - 250, 2));
    }
}
