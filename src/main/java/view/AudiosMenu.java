package view;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AudiosMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GridPane audioPane = new GridPane();
        audioPane.setAlignment(Pos.CENTER);
        audioPane.setVgap(15);
        Scene scene = new Scene(audioPane, 250, 150);

        Button audio1 = new Button("8BitDreamLand");
        audio1.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        audio1.setOnAction(actionEvent -> chooseAudio(audio1));
        audioPane.add(audio1, 0, 0);
        GridPane.setHalignment(audio1, HPos.CENTER);

        Button audio2 = new Button("NeonGaming");
        audio2.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        audio2.setOnAction(actionEvent -> chooseAudio(audio2));
        audioPane.add(audio2, 0, 1);
        GridPane.setHalignment(audio2, HPos.CENTER);

        Button audio3 = new Button("WondrousWaters");
        audio3.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        audio3.setOnAction(actionEvent -> chooseAudio(audio3));
        audioPane.add(audio3, 0, 2);
        GridPane.setHalignment(audio3, HPos.CENTER);

        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        audioPane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Choose Audio");
        else stage.setTitle("انتخاب موزیک");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void chooseAudio(Button button) {
        Media audio = new Media(AudiosMenu.class.getResource("/audios/" + button.getText() + ".mp3").toExternalForm());
        PrimaryMenu.audioPlayer.stop();
        PrimaryMenu.audioPlayer = new MediaPlayer(audio);
        PrimaryMenu.audioPlayer.play();
        PrimaryMenu.audioIsPlaying = true;
        PauseMenu.audioStage.close();
        PauseMenu pauseMenu = new PauseMenu();
        try {
            pauseMenu.start(GameMenu.pauseStage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
