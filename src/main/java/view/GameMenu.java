package view;

import controller.AA;
import controller.DBController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.ShootingBall;
import view.animations.ShootBallAnimation;

import java.util.Random;

public class GameMenu extends Application {
    public static boolean isResume;
    public static AnchorPane gamePane;
    public static Game game;
    public static ShootingBall currentBall;
    public static String shootButton;
    public static String freezeButton;
    public static Stage endGameStage = new Stage();
    public static Stage pauseStage = new Stage();
    public static double millisPerDegree;
    public static ShootBallAnimation shootBallAnimation;
    public static long startTime;
    public Text point;
    public static Text angle;
    public Label freeze;
    public Label balls;
    public ProgressBar freezeBar;
    public ProgressBar ballBar;
    public Button pause;
    public Text timer;
    public static Timeline rotatingTimeline;
    public static Timeline changeDirectionTimeline;
    public static Timeline changeBallsRadiusTimeline;
    public static Timeline changeVisibilityTimeline;
    public static Timeline changeShootAngleTimeline;
    public static Timeline updateTimerTimeline;

    @Override
    public void start(Stage stage) throws Exception {
        millisPerDegree = game.getDifficulty().getMillisPerDegree();
        gamePane = FXMLLoader.load(GameMenu.class.getResource("/FXML/gameMenu.fxml"));
        Scene scene = new Scene(gamePane, 600, 600);

        freezeBar = new ProgressBar(game.getCurrentFreezeState());
        AnchorPane.setTopAnchor(freezeBar, 13.0);
        AnchorPane.setLeftAnchor(freezeBar, 60.0);

        ballBar = new ProgressBar((float) game.getCurrentBallNumber() / game.getNumberOfBalls());
        AnchorPane.setTopAnchor(ballBar, 13.0);
        AnchorPane.setRightAnchor(ballBar, 10.0);
        switch (game.getCurrentPhase()) {
            case 1:
                ballBar.getStyleClass().add("redBar");
                break;
            case 2:
                ballBar.getStyleClass().add("orangeBar");
                break;
            case 3:
                ballBar.getStyleClass().add("yellowBar");
                break;
            case 4:
                ballBar.getStyleClass().add("greenBar");
                break;
        }

        String pointWord = (SettingsMenu.isEnglish) ? "Point: " : "امتیاز: ";
        point = new Text(pointWord + game.getPoint());
        AnchorPane.setLeftAnchor(point, 10.0);
        AnchorPane.setTopAnchor(point, 35.0);
        point.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        String angleWord = (SettingsMenu.isEnglish) ? "Angle: " : "زاویه: ";
        angle = new Text(angleWord + game.getCurrentShootAngle());
        AnchorPane.setRightAnchor(angle, 25.0);
        AnchorPane.setTopAnchor(angle, 35.0);
        angle.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        createPauseButton();
        createShootingBall();
        createTimer();
        Circle centerBall = new Circle(300, 250, 50, Color.BLACK);
        if (!isResume) {
            switch (game.getMapNumber()) {
                case 1:
                    Maps.createMap1(gamePane);
                    break;
                case 2:
                    Maps.createMap2(gamePane);
                    break;
                case 3:
                    Maps.createMap3(gamePane);
                    break;
            }
        } else loadSavedGame();
        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        gamePane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        GameMenu.rotatingTimeline = rotateCentralObjects();
        changeRotateDirection(new Random().nextLong(3) + 4);
        changeBallsRadius();
        changeVisibility();
        changeShootAngle();

        String guestWord = (SettingsMenu.isEnglish) ? "Guest" : "مهمان";
        String username = (GameMenu.game.getUser() != null) ? GameMenu.game.getUser().getUsername() : guestWord;
        gamePane.getChildren().addAll(freezeBar, centerBall, point, ballBar, angle);
        if (SettingsMenu.isEnglish) stage.setTitle("Game Menu - " + username);
        else stage.setTitle("منوی بازی - " + username);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        freeze.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        balls.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) {
            freeze.setText("یخ زده");
            balls.setText("توپ ها");
        }
    }

    public void createPauseButton() {
        pause = new Button("Pause");
        AnchorPane.setTopAnchor(pause, 70.0);
        AnchorPane.setRightAnchor(pause, 15.0);

        if (!SettingsMenu.isEnglish) pause.setText("توقف");

        pause.setOnAction(actionEvent -> {
            long time = System.currentTimeMillis();
            if (currentBall.getNumber() != game.getNumberOfBalls()) {
                game.addTime((time - startTime) / 1000.0);
            }
            PauseMenu pauseMenu = new PauseMenu();
            rotatingTimeline.pause();
            updateTimerTimeline.pause();
            if (changeDirectionTimeline != null) changeDirectionTimeline.pause();
            if (changeBallsRadiusTimeline != null) changeBallsRadiusTimeline.pause();
            if (changeVisibilityTimeline != null) changeVisibilityTimeline.pause();
            if (changeShootAngleTimeline != null) changeShootAngleTimeline.pause();
            try {
                pauseMenu.start(pauseStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        gamePane.getChildren().add(pause);
    }

    public void createTimer() {
        int minute = game.getTimerSeconds() / 60;
        int second = game.getTimerSeconds() % 60;
        String secondString = (second < 10) ? "0" + second : Integer.toString(second);
        timer = new Text("0" + minute + " : " + secondString);
        AnchorPane.setLeftAnchor(timer, 10.0);
        AnchorPane.setTopAnchor(timer, 65.0);
        timer.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 14));
        updateTimer();
        gamePane.getChildren().add(timer);
    }

    public void updateTimer() {
        updateTimerTimeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            game.setTimerSeconds(game.getTimerSeconds() - 1);
            int minute = game.getTimerSeconds() / 60;
            int second = game.getTimerSeconds() % 60;
            String secondString = (second < 10) ? "0" + second : Integer.toString(second);
            timer.setText("0" + minute + " : " + secondString);
            if (game.getTimerSeconds() == 0) {
                try {
                    endGame(false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        updateTimerTimeline.setCycleCount(-1);
    }

    private void createShootingBall() {
        Integer ballNumber = game.getCurrentBallNumber();
        ShootingBall shootingBall = new ShootingBall(ballNumber, GameMenu.game, GameMenu.game.getUser(), gamePane);
        gamePane.getChildren().add(shootingBall);
        shootingBall.requestFocus();
        game.setCurrentBallNumber(ballNumber - 1);
        GameMenu.currentBall = shootingBall;

        pause.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldBoolean, Boolean newBoolean) {
                if (newBoolean) currentBall.requestFocus();
            }
        });

        shootingBall.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().getName();
                if (keyName.equals(shootButton)) {
                    shootBallAnimation = new ShootBallAnimation(GameMenu.currentBall, freezeBar, ballBar, point, game.getCurrentShootAngle());
                    shootBallAnimation.play();
                    ballBar.setProgress((float) game.getCurrentBallNumber() / game.getNumberOfBalls());
                    if (game.getCurrentBallNumber() > 0)
                        createShootingBall();
                } else if (keyName.equals(freezeButton)) {
                    if (freezeBar.getProgress() < 1) return;
                    freezeBar.setProgress(0);
                    game.setCurrentFreezeState(0);
                    Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
                        GameMenu.millisPerDegree = 15;
                        rotatingTimeline.stop();
                        rotatingTimeline = rotateCentralObjects();
                    }),
                            new KeyFrame(Duration.millis(1000 * game.getDifficulty().getFreezeTime()), actionEvent -> {
                                millisPerDegree = game.getDifficulty().getMillisPerDegree();
                                rotatingTimeline.stop();
                                rotatingTimeline = rotateCentralObjects();
                            }));
                    timeline.play();
                } else if (keyName.equals("Right") && game.getCurrentPhase() == 4 && shootingBall.getCenterX() <= 545) {
                    shootingBall.setCenterX(shootingBall.getCenterX() + 5);
                    if (shootingBall.getNumber() < 10)
                        AnchorPane.setLeftAnchor(shootingBall.getNumberText(), shootingBall.getCenterX() - 3);
                    else AnchorPane.setLeftAnchor(shootingBall.getNumberText(), shootingBall.getCenterX() - 7);
                } else if (keyName.equals("Left") && game.getCurrentPhase() == 4 && shootingBall.getCenterX() >= 55) {
                    shootingBall.setCenterX(shootingBall.getCenterX() - 5);
                    if (shootingBall.getNumber() < 10)
                        AnchorPane.setLeftAnchor(shootingBall.getNumberText(), shootingBall.getCenterX() - 3);
                    else AnchorPane.setLeftAnchor(shootingBall.getNumberText(), shootingBall.getCenterX() - 7);
                }
            }
        });
    }

    public static Timeline rotateCentralObjects() {
        Rotate rotate = new Rotate();
        Rotate rotateText = new Rotate();
        rotateText.setAngle(game.getCurrentRotateDirection());
        rotate.setPivotX(300);
        rotate.setPivotY(250);
        rotate.setAngle(game.getCurrentRotateDirection());
        rotatingTimeline = new Timeline(new KeyFrame(Duration.millis(GameMenu.millisPerDegree),
                actionEvent -> {
                    for (int i = 0; i < gamePane.getChildren().size(); i++) {
                        Node node = gamePane.getChildren().get(i);
                        if (node instanceof ShootingBall) {
                            if (((ShootingBall) node).isShot()) {
                                ((ShootingBall) node).addRotatedDegree(game.getCurrentRotateDirection());
                                node.getTransforms().add(rotate);
                                ((ShootingBall) node).getLine().getTransforms().add(rotate);
                                if (((ShootingBall) node).getNumberText() != null) {
                                    AnchorPane.setTopAnchor(((ShootingBall) node).getNumberText(), Maps.getY(((ShootingBall) node).getRotatedDegree()) - 9);
                                    if (((ShootingBall) node).getNumber() < 10)
                                        AnchorPane.setLeftAnchor(((ShootingBall) node).getNumberText(), Maps.getX(((ShootingBall) node).getRotatedDegree()) - 3);
                                    else
                                        AnchorPane.setLeftAnchor(((ShootingBall) node).getNumberText(), Maps.getX(((ShootingBall) node).getRotatedDegree()) - 7);
                                }
                            }
                        }
                    }
                }));
        rotatingTimeline.setCycleCount(-1);
        rotatingTimeline.play();
        return rotatingTimeline;
    }

    public static void changeRotateDirection(long interval) {
        if (game.getCurrentPhase() == 1) return;
        changeDirectionTimeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
        }),
                new KeyFrame(Duration.millis(1000 * interval), actionEvent -> {
                    rotatingTimeline.stop();
                    game.setCurrentRotateDirection(-game.getCurrentRotateDirection());
                    rotatingTimeline = rotateCentralObjects();
                    changeRotateDirection(new Random().nextLong(3) + 4);
                }));
        changeDirectionTimeline.play();
    }

    public static void changeBallsRadius() {
        if (game.getCurrentPhase() == 1) return;
        changeBallsRadiusTimeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            double percent = (new Random().nextDouble(5) + 15) / 100.0;
            for (int i = 0; i < gamePane.getChildren().size(); i++) {
                Node node = gamePane.getChildren().get(i);
                if (node instanceof ShootingBall && ((ShootingBall) node).isShot()) {
                    ((ShootingBall) node).setRadius(10 * (percent + 1));
                }
            }
            for (int i = 0; i < gamePane.getChildren().size(); i++) {
                Node node1 = gamePane.getChildren().get(i);
                if (node1 instanceof ShootingBall && ((ShootingBall) node1).isShot()) {
                    for (int j = 0; j < gamePane.getChildren().size(); j++) {
                        Node node2 = gamePane.getChildren().get(j);
                        if (node2 instanceof ShootingBall && !node1.equals(node2) && ((ShootingBall) node2).isShot()) {
                            if (node1.getBoundsInParent().intersects(node2.getBoundsInParent())) {
                                try {
                                    endGame(false);
                                    return;
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        }), new KeyFrame(Duration.millis(1000), actionEvent -> {
            for (int i = 0; i < gamePane.getChildren().size(); i++) {
                Node node = gamePane.getChildren().get(i);
                if (node instanceof ShootingBall && ((ShootingBall) node).isShot()) {
                    ((ShootingBall) node).setRadius(10);
                }
            }
        }), new KeyFrame(Duration.millis(2000), actionEvent -> {
        }));
        changeBallsRadiusTimeline.setCycleCount(-1);
        changeBallsRadiusTimeline.play();
    }

    public static void changeVisibility() {
        if (game.getCurrentPhase() < 3) return;
        changeVisibilityTimeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            for (int i = 0; i < gamePane.getChildren().size(); i++) {
                Node node = gamePane.getChildren().get(i);
                if (node instanceof ShootingBall && ((ShootingBall) node).isShot()) {
                    node.setVisible(false);
                    ((ShootingBall) node).getLine().setVisible(false);
                    if (((ShootingBall) node).getNumberText() != null)
                        ((ShootingBall) node).getNumberText().setVisible(false);
                }
            }
        }), new KeyFrame(Duration.millis(1000), actionEvent -> {
            for (int i = 0; i < gamePane.getChildren().size(); i++) {
                Node node = gamePane.getChildren().get(i);
                if (node instanceof ShootingBall && ((ShootingBall) node).isShot()) {
                    node.setVisible(true);
                    ((ShootingBall) node).getLine().setVisible(true);
                    if (((ShootingBall) node).getNumberText() != null)
                        ((ShootingBall) node).getNumberText().setVisible(true);
                }
            }
        }), new KeyFrame(Duration.millis(3000), actionEvent -> {
        }));
        changeVisibilityTimeline.setCycleCount(-1);
        changeVisibilityTimeline.play();
    }

    public static void changeShootAngle() {
        if (game.getCurrentPhase() < 4) return;
        changeShootAngleTimeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            game.setCurrentShootAngle((new Random().nextDouble(30) - 15) * Math.PI / 180);
            String angleWord = (SettingsMenu.isEnglish) ? "Angle: " : "زاویه: ";
            angle.setText(angleWord + Math.round(game.getCurrentShootAngle() * 180 / Math.PI * 10) / 10.0);
        }), new KeyFrame(Duration.millis(1000 * game.getDifficulty().getAngleChangeInterval()), actionEvent -> {
        }));
        changeShootAngleTimeline.setCycleCount(-1);
        changeShootAngleTimeline.play();
    }

    public static void endGame(boolean isWin) throws Exception {
        long endTime = System.currentTimeMillis();
        if (currentBall.getNumber() != game.getNumberOfBalls() - 1) {
            game.addTime((endTime - startTime) / 1000.0);
        }

        String color = isWin ? "green" : "red";
        rotatingTimeline.stop();
        shootBallAnimation.stop();
        updateTimerTimeline.stop();
        if (changeDirectionTimeline != null) changeDirectionTimeline.stop();
        if (changeBallsRadiusTimeline != null) changeBallsRadiusTimeline.stop();
        if (changeVisibilityTimeline != null) changeVisibilityTimeline.stop();
        if (changeShootAngleTimeline != null) changeShootAngleTimeline.stop();
        gamePane.setStyle("-fx-background-color: " + color);

        if (game.getUser() != null) game.getUser().refreshGame(game);
        AA.refreshScoreboard();
        DBController.saveAllUsers(null);

        EndGameMenu endGameMenu = new EndGameMenu();
        endGameMenu.isWin = isWin;
        endGameMenu.start(GameMenu.endGameStage);
        makeAllVisible(gamePane);
    }

    public static void makeAllVisible(AnchorPane gamePane) {
        for (int i = 0; i < gamePane.getChildren().size(); i++) {
            Node node = gamePane.getChildren().get(i);
            if (node instanceof ShootingBall && ((ShootingBall) node).isShot()) {
                node.setVisible(true);
                ((ShootingBall) node).getLine().setVisible(true);
                if (((ShootingBall) node).getNumberText() != null)
                    ((ShootingBall) node).getNumberText().setVisible(true);
            }
        }
    }

    public void loadSavedGame() {
        if (!game.getUser().isHasSavedGame()) return;
        startTime = System.currentTimeMillis();
        updateTimerTimeline.play();

        for (int i = 0; i < game.getUser().getSavedGameBalls().size(); i++) {
            ShootingBall ball = game.getUser().getSavedGameBalls().get(i);
            gamePane.getChildren().addAll(ball, ball.getLine());
            if (ball.getNumberText() != null) gamePane.getChildren().add(ball.getNumberText());
        }

        game.getUser().setSavedGame(null);
    }
}
