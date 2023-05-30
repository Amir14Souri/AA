package view;

import controller.AA;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import model.enumerations.Difficulty;

public class ScoreboardMenu extends Application {
    public static String startingDifficultyName;
    public Text scoreboard;
    public Label difficulty;
    public ChoiceBox difficultyBox;
    public Button back;
    public TableView table;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane scoreboardPane = FXMLLoader.load(ScoreboardMenu.class.getResource("/FXML/scoreboardMenu.fxml"));
        Scene scene = new Scene(scoreboardPane, 450, 450);

        String choices[] = {"Easy", "Average", "Hard"};
        difficultyBox = new ChoiceBox<>();
        difficultyBox.setItems(FXCollections.observableArrayList(choices));
        GridPane.setRowIndex(difficultyBox, 0);
        GridPane.setColumnIndex(difficultyBox, 2);
        GridPane.setHalignment(difficultyBox, HPos.LEFT);
        if (!SettingsMenu.isEnglish) {
            choices[0] = "آسان";
            choices[1] = "متوسط";
            choices[2] = "سخت";
            difficultyBox.setItems(FXCollections.observableArrayList(choices));
            GridPane.setColumnIndex(difficultyBox, 0);
            GridPane.setHalignment(difficultyBox, HPos.RIGHT);
        }
        if (SettingsMenu.isEnglish) difficultyBox.setValue(startingDifficultyName);
        else {
            if (startingDifficultyName.equals("Easy")) difficultyBox.setValue("آسان");
            else if (startingDifficultyName.equals("Average")) difficultyBox.setValue("متوسط");
            else difficultyBox.setValue("سخت");
        }

        makeTable();
        ObservableList<User> users = FXCollections.observableArrayList(AA.getScoreboardByDifficulty(Difficulty.getDifficultyByName(startingDifficultyName)));
        table.setItems(users);

        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        scoreboardPane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Scoreboard");
        else stage.setTitle("جدول امتیازات");
        scoreboardPane.getChildren().addAll(difficultyBox, table);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        scoreboard.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 24));
        difficulty.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        back.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) {
            scoreboard.setText("جدول امتیازات");
            difficulty.setText("سختی:");
            back.setText("بازگشت");

            GridPane.setColumnIndex(scoreboard, 2);
            GridPane.setHalignment(scoreboard, HPos.RIGHT);
            GridPane.setColumnIndex(difficulty, 1);
            GridPane.setHalignment(difficulty, HPos.LEFT);
        }
    }

    private void makeTable() {
        table = new TableView<User>();
        GridPane.setRowIndex(table, 1);
        GridPane.setColumnIndex(table, 0);
        GridPane.setColumnSpan(table, 3);
        table.setPrefHeight(280);

        String rankWord = (SettingsMenu.isEnglish) ? "Rank" : "رتبه";
        TableColumn<User, Integer> rankColumn = new TableColumn<>(rankWord);
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rankColumn.setSortable(false);
        rankColumn.setResizable(false);
        rankColumn.setPrefWidth(50);

        String usernameWord = (SettingsMenu.isEnglish) ? "Username" : "نام کاربری";
        TableColumn<User, String> usernameColumn = new TableColumn<>(usernameWord);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setSortable(false);
        usernameColumn.setResizable(false);
        usernameColumn.setPrefWidth(100);

        String pointWord = (SettingsMenu.isEnglish) ? "Point" : "امتیاز";
        TableColumn<User, Integer> pointColumn = new TableColumn<>(pointWord);
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("bestGamePoint"));
        pointColumn.setSortable(false);
        pointColumn.setResizable(false);
        pointColumn.setPrefWidth(50);

        String timeWord = (SettingsMenu.isEnglish) ? "Elapsed Time (second)" : "زمان سپری شده (ثانیه)";
        TableColumn<User, String> timeColumn = new TableColumn<>(timeWord);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("bestGameElapsedTime"));
        timeColumn.setSortable(false);
        timeColumn.setResizable(false);
        timeColumn.setPrefWidth(140);

        refreshDifficulty();
        table.getColumns().addAll(rankColumn, usernameColumn, pointColumn, timeColumn);
    }

    public void refreshDifficulty() {
        difficultyBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldDifficulty, Object newDifficulty) {
                if (!SettingsMenu.isEnglish) {
                    if (newDifficulty.equals("آسان")) newDifficulty = "Easy";
                    else if (newDifficulty.equals("متوسط")) newDifficulty = "Average";
                    else newDifficulty = "Hard";
                }
                Difficulty chosenDifficulty = Difficulty.getDifficultyByName((String) newDifficulty);
                ObservableList<User> users = FXCollections.observableArrayList(AA.getScoreboardByDifficulty(chosenDifficulty));
                table.setItems(users);
            }
        });
    }

    public void mainMenu(ActionEvent actionEvent) throws Exception {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(PrimaryMenu.stage);
    }
}
