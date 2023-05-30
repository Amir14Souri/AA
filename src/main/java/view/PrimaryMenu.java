package view;

import controller.DBController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Game;

public class PrimaryMenu extends Application {
    public static MediaPlayer audioPlayer;
    public static boolean audioIsPlaying = true;
    public Button skip;
    public Text welcome;
    public Button register;
    public Button login;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        PrimaryMenu.stage = stage;
        GridPane root = FXMLLoader.load(PrimaryMenu.class.getResource("/FXML/primaryMenu.fxml"));
        Scene scene = new Scene(root, 300, 300);
        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        root.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        stage.setScene(scene);
        if (SettingsMenu.isEnglish) stage.setTitle("Choose Menu");
        else stage.setTitle("انتخاب منو");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        welcome.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 24));
        register.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        login.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        skip.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        if (!SettingsMenu.isEnglish) {
            welcome.setText("خوش آمدید");
            register.setText("ثبت نام");
            login.setText("ورود");
            skip.setText("ورود به عنوان مهمان");
        }
    }

    public static void main(String[] args) {
        DBController.loadAllUsers();
        DBController.loadUploadedImageNumber();
        Media audio = new Media(PrimaryMenu.class.getResource("/audios/WondrousWaters.mp3").toExternalForm());
        audioPlayer = new MediaPlayer(audio);
        audioPlayer.setAutoPlay(true);
        audioPlayer.setVolume(100);
        launch(args);
    }

    public void registerMenu(ActionEvent actionEvent) throws Exception {
        if (actionEvent.getSource().equals(register)) RegisterMenu.isRegister = true;
        else RegisterMenu.isRegister = false;
        RegisterMenu registerMenu = new RegisterMenu();
        registerMenu.start(PrimaryMenu.stage);
    }

    public void gameMenu(ActionEvent actionEvent) throws Exception {
//        TODO: double check
        Game newGame = new Game();
        GameMenu gameMenu = new GameMenu();
        GameMenu.game = newGame;
        GameMenu.isResume = false;
        GameMenu.shootButton = "Space";
        GameMenu.freezeButton = "Tab";
        gameMenu.start(PrimaryMenu.stage);
    }
}