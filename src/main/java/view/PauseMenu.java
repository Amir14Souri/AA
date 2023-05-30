package view;

import controller.DBController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Game;
import model.ShootingBall;

public class PauseMenu extends Application {
    public static Stage audioStage;
    public static Stage guideStage;
    public Text title;
    public Button save;
    public Button restart;
    public Button changeAudio;
    public Button exit;
    public Button guide;
    public Button mute;
    public Button resume;

    @Override
    public void start(Stage stage) throws Exception {
        audioStage = new Stage();
        guideStage = new Stage();
        BorderPane pausePane = FXMLLoader.load(getClass().getResource("/FXML/pauseMenu.fxml"));
        Scene scene = new Scene(pausePane, 400, 400);
        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        pausePane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Pause Menu");
        else stage.setTitle("منوی توقف");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        title.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 24));
        resume.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        save.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        restart.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        changeAudio.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        guide.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        mute.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        exit.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) {
            title.setText("توقف");
            resume.setText("ادامه");
            save.setText("ذخیره و خروج");
            restart.setText("شروع دوباره");
            changeAudio.setText("انتخاب موزیک");
            guide.setText("راهنما");
            mute.setText("بی صدا");
            exit.setText("خروج");
        }
        if (GameMenu.game.getUser() == null) save.setVisible(false);
        if (!PrimaryMenu.audioIsPlaying) {
            if (SettingsMenu.isEnglish) mute.setText("play");
            else mute.setText("پخش");
        }
    }

    public void resume(ActionEvent actionEvent) {
        long time = System.currentTimeMillis();
        if (GameMenu.currentBall.getNumber() != GameMenu.game.getNumberOfBalls()) {
            GameMenu.startTime = time;
        }
        GameMenu.pauseStage.close();
        GameMenu.rotatingTimeline.play();
        GameMenu.updateTimerTimeline.play();
        if (GameMenu.changeDirectionTimeline != null) GameMenu.changeDirectionTimeline.play();
        if (GameMenu.changeBallsRadiusTimeline != null) GameMenu.changeBallsRadiusTimeline.play();
        if (GameMenu.changeVisibilityTimeline != null) GameMenu.changeVisibilityTimeline.play();
        if (GameMenu.changeShootAngleTimeline != null) GameMenu.changeShootAngleTimeline.play();
        GameMenu.currentBall.requestFocus();
    }

    public void save(ActionEvent actionEvent) throws Exception {
        GameMenu.game.getUser().getSavedGameBalls().clear();
        GameMenu.game.getUser().setSavedGame(null);
        for (int i = 0; i < GameMenu.gamePane.getChildren().size(); i++) {
            Node node = GameMenu.gamePane.getChildren().get(i);
            if (node instanceof ShootingBall && ((ShootingBall) node).isShot()) {
                GameMenu.game.getUser().addSavedGameBall((ShootingBall) node);
            }
        }
        GameMenu.game.setCurrentBallNumber(GameMenu.game.getCurrentBallNumber() + 1);
        GameMenu.game.getUser().setSavedGame(GameMenu.game);
        DBController.saveAllUsers(GameMenu.game.getUser());
        GameMenu.pauseStage.close();
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(PrimaryMenu.stage);
    }


    public void restart(ActionEvent actionEvent) throws Exception {
        GameMenu.pauseStage.close();
        Game newGame = (GameMenu.game.getUser() != null) ? new Game(GameMenu.game.getUser()) : new Game();
        GameMenu gameMenu = new GameMenu();
        GameMenu.game = newGame;
        GameMenu.isResume = false;
        gameMenu.start(PrimaryMenu.stage);
    }

    public void exit(ActionEvent actionEvent) throws Exception {
        GameMenu.pauseStage.close();
        if (GameMenu.game.getUser() != null) {
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(PrimaryMenu.stage);
        } else {
            PrimaryMenu primaryMenu = new PrimaryMenu();
            primaryMenu.start(PrimaryMenu.stage);
        }
    }

    public void mute(ActionEvent actionEvent) {
        if (PrimaryMenu.audioIsPlaying) {
            PrimaryMenu.audioPlayer.stop();
            PrimaryMenu.audioIsPlaying = false;
            if (SettingsMenu.isEnglish) mute.setText("play");
            else mute.setText("پخش");
        } else {
            PrimaryMenu.audioPlayer.play();
            PrimaryMenu.audioIsPlaying = true;
            if (SettingsMenu.isEnglish) mute.setText("mute");
            else mute.setText("بی صدا");
        }
    }

    public void changeAudio(ActionEvent actionEvent) throws Exception {
        AudiosMenu audiosMenu = new AudiosMenu();
        audiosMenu.start(audioStage);
    }

    public void guide(ActionEvent actionEvent) throws Exception {
        GuideMenu guideMenu = new GuideMenu();
        guideMenu.start(guideStage);
    }
}
