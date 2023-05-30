package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Game;
import model.User;

import java.util.Random;

public class MainMenu extends Application {
    public static User user;
    public Button profile;
    public Button settings;
    public Text aa;
    public Button start;
    public Button resume;
    public Button scoreboard;
    public Button exit;
    public ImageView avatar;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = FXMLLoader.load(MainMenu.class.getResource("/FXML/mainMenu.fxml"));
        Scene scene = new Scene(mainPane, 350, 300);
        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        mainPane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Main Menu");
        else stage.setTitle("منوی اصلی");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        aa.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 24));
        start.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        resume.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        profile.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        settings.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        scoreboard.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        exit.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));

        if (!SettingsMenu.isEnglish) {
            start.setText("شروع بازی");
            resume.setText("ادامه بازی");
            profile.setText("پروفایل");
            settings.setText("تنظیمات");
            scoreboard.setText("جدول امتیازات");
            exit.setText("خروج");
        }
    }

    public void profileMenu(ActionEvent actionEvent) throws Exception {
        ProfileMenu profileMenu = new ProfileMenu();
        ProfileMenu.user = MainMenu.user;
        profileMenu.start(PrimaryMenu.stage);
    }

    public void settingsMenu(ActionEvent actionEvent) throws Exception {
        SettingsMenu settingsMenu = new SettingsMenu();
        SettingsMenu.user = MainMenu.user;
        settingsMenu.start(PrimaryMenu.stage);
    }

    public void scoreboard(ActionEvent actionEvent) throws Exception {
        ScoreboardMenu scoreboardMenu = new ScoreboardMenu();
        ScoreboardMenu.startingDifficultyName = "Average";
        scoreboardMenu.start(PrimaryMenu.stage);
    }

    public void exit(ActionEvent actionEvent) {
        PrimaryMenu.stage.close();
    }

    public void newGame(ActionEvent actionEvent) throws Exception {
//        TODO: double check
        Game newGame = new Game(user);
        GameMenu gameMenu = new GameMenu();
        GameMenu.game = newGame;
        GameMenu.isResume = false;
        GameMenu.shootButton = user.getSelectedShootButton();
        GameMenu.freezeButton = user.getSelectedFreezeButton();
        gameMenu.start(PrimaryMenu.stage);
    }

    public void resumeGame(ActionEvent actionEvent) throws Exception {
        if (!user.isHasSavedGame()) return;
        GameMenu gameMenu = new GameMenu();
        GameMenu.game = user.getSavedGame();
        GameMenu.isResume = true;
        GameMenu.shootButton = user.getSelectedShootButton();
        GameMenu.freezeButton = user.getSelectedFreezeButton();
        gameMenu.start(PrimaryMenu.stage);
        user.setSavedGame(null);
    }
}
