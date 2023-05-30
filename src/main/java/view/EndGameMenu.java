package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EndGameMenu extends Application {
    public boolean isWin;
    public Text result;
    public Text point;
    public Text time;
    public Button scoreboard;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane endGamePane = FXMLLoader.load(EndGameMenu.class.getResource("/FXML/endGameMenu.fxml"));
        Scene scene = new Scene(endGamePane);

        if (isWin && SettingsMenu.isEnglish) result = new Text("You Won!");
        else if (!isWin && SettingsMenu.isEnglish) result = new Text("You Lost!");
        else if (isWin && !SettingsMenu.isEnglish) result = new Text("تو بردی!");
        else result = new Text("تو باختی!");
        GridPane.setColumnIndex(result, 0);
        GridPane.setRowIndex(result, 0);
        GridPane.setHalignment(result, HPos.CENTER);
        result.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 24));
        Color color = isWin ? Color.GREEN : Color.RED;
        result.setFill(color);
        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        endGamePane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("End Game Menu");
        else stage.setTitle("منوی اتمام بازی");
        endGamePane.getChildren().add(result);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        point.setText("Point: " + GameMenu.game.getPoint());
        time.setText("Elapsed Time: " + GameMenu.game.getElapsedTime());

        point.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        time.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        scoreboard.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) {
            point.setText("امتیاز: " + GameMenu.game.getPoint());
            time.setText("زمان سپری شده: " + GameMenu.game.getElapsedTime());
            scoreboard.setText("جدول امتیازات");

            GridPane.setHalignment(point, HPos.RIGHT);
            GridPane.setHalignment(time, HPos.RIGHT);
        }
        if (GameMenu.game.getUser() == null) {
            if (SettingsMenu.isEnglish) scoreboard.setText("Back");
            else scoreboard.setText("بازگشت");
        }
    }

    public void scoreboard(ActionEvent actionEvent) throws Exception {
        GameMenu.endGameStage.close();
        if (GameMenu.game.getUser() == null) {
            PrimaryMenu primaryMenu = new PrimaryMenu();
            primaryMenu.start(PrimaryMenu.stage);
        } else {
            ScoreboardMenu scoreboardMenu = new ScoreboardMenu();
            ScoreboardMenu.startingDifficultyName = GameMenu.game.getDifficulty().getName();
            scoreboardMenu.start(PrimaryMenu.stage);
        }

//        TODO: double check
        GameMenu.game = null;
        GameMenu.currentBall = null;
        GameMenu.millisPerDegree = 15;
        GameMenu.rotatingTimeline = null;
        GameMenu.changeDirectionTimeline = null;
        GameMenu.changeBallsRadiusTimeline = null;
        GameMenu.changeVisibilityTimeline = null;
        GameMenu.changeShootAngleTimeline = null;
    }
}
