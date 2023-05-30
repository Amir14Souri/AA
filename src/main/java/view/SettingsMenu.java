package view;

import controller.DBController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import model.enumerations.Difficulty;

public class SettingsMenu extends Application {
    public static Stage buttonStage;
    public static boolean isEnglish = true;
    public static boolean colored = true;
    public static User user;
    public Button mute;
    public Button color;
    public Button language;
    public Button buttons;
    public Text settings;
    public Label difficulty;
    public ChoiceBox difficultyBox;
    public Label ballNumber;
    public TextField ballNumberField;
    public Label map;
    public ChoiceBox mapBox;
    public Button back;
    public Text ballNumberError;
    public GridPane centerPane;
    public Button save;

    @Override
    public void start(Stage stage) throws Exception {
        buttonStage = new Stage();
        BorderPane settingsPane = FXMLLoader.load(SettingsMenu.class.getResource("/FXML/settingsMenu.fxml"));
        Scene scene = new Scene(settingsPane, 500, 350);
        centerPane = (GridPane) scene.lookup("#centerPane");

        String[] difficulties = {"Easy", "Average", "Hard"};
        difficultyBox = new ChoiceBox<>();
        difficultyBox.setItems(FXCollections.observableArrayList(difficulties));
        difficultyBox.setValue(user.getSelectedDifficulty().getName());
        if (!SettingsMenu.isEnglish) {
            difficulties[0] = "آسان";
            difficulties[1] = "متوسط";
            difficulties[2] = "سخت";
            difficultyBox.setItems(FXCollections.observableArrayList(difficulties));
            int difficultyNumber = 0;
            if (user.getSelectedDifficulty().getName().equals("Average")) difficultyNumber = 1;
            if (user.getSelectedDifficulty().getName().equals("Hard")) difficultyNumber = 2;
            difficultyBox.setValue(difficulties[difficultyNumber]);
        }

        String[] maps = {"Map 1", "Map 2", "Map 3"};
        mapBox = new ChoiceBox<>();
        mapBox.setItems(FXCollections.observableArrayList(maps));
        mapBox.setValue("Map " + user.getSelectedMapNumber());
        if (!SettingsMenu.isEnglish) {
            maps[0] = "نقشه 1";
            maps[1] = "نقشه 2";
            maps[2] = "نقشه 3";
            mapBox.setItems(FXCollections.observableArrayList(maps));
            mapBox.setValue("نقشه " + user.getSelectedMapNumber());
        }

        if (SettingsMenu.isEnglish) centerPane.add(difficultyBox, 1, 1);
        else {
            centerPane.add(difficultyBox, 0, 1);
            GridPane.setHalignment(difficultyBox, HPos.RIGHT);
        }
        if (SettingsMenu.isEnglish) centerPane.add(mapBox, 1, 4);
        else {
            centerPane.add(mapBox, 0, 4);
            GridPane.setHalignment(mapBox, HPos.RIGHT);
        }

        makeBallNumberField(centerPane);
        makeSaveButton(centerPane);
        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        settingsPane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Settings Menu");
        else stage.setTitle("منوی تنظیمات");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        mute.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        color.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        language.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        buttons.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        settings.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 24));
        difficulty.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        ballNumber.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        map.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        back.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) {
            settings.setText("تنظیمات");
            mute.setText("بی صدا");
            color.setText("سیاه/سفید");
            language.setText("انگلیسی");
            buttons.setText("کنترل");
            difficulty.setText("سختی:");
            ballNumber.setText("تعداد توپ:");
            map.setText("نقشه:");
            back.setText("بازگشت");

            GridPane.setColumnIndex(difficulty, 1);
            GridPane.setHalignment(difficulty, HPos.LEFT);
            GridPane.setColumnIndex(ballNumber, 1);
            GridPane.setHalignment(ballNumber, HPos.LEFT);
            GridPane.setColumnIndex(map, 1);
            GridPane.setHalignment(map, HPos.LEFT);
        }
        if (!PrimaryMenu.audioIsPlaying) {
            if (SettingsMenu.isEnglish) mute.setText("play");
            else mute.setText("پخش");
        }
        if (!SettingsMenu.colored){
            if (SettingsMenu.isEnglish) color.setText("colored");
            else color.setText("رنگی");
        }
    }

    public void makeBallNumberField(GridPane centerPane) {
        ballNumberField = new TextField(Integer.toString(user.getSelectedBallNumber()));
        if (!SettingsMenu.isEnglish) {
            GridPane.setColumnIndex(ballNumberField, 0);
            GridPane.setHalignment(ballNumberField, HPos.RIGHT);
        }

        ballNumberError = new Text();
        GridPane.setColumnSpan(ballNumberError, 2);
        GridPane.setHalignment(ballNumberError, HPos.CENTER);

        if (SettingsMenu.isEnglish) centerPane.add(ballNumberField, 1, 2);
        else centerPane.add(ballNumberField, 0, 2);
        centerPane.add(ballNumberError, 0, 3);
    }

    public void makeSaveButton(GridPane centerPane) {
        save = new Button("Save");
        GridPane.setHalignment(save, HPos.LEFT);
        save.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        if (!SettingsMenu.isEnglish) save.setText("ذخیره");

        save.setOnAction(actionEvent -> {
            try {
                save();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        centerPane.add(save, 1, 5);
    }

    public void back(ActionEvent actionEvent) throws Exception {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(PrimaryMenu.stage);
    }

    public void save() throws Exception {
        ballNumberError.setText("");
        int numberOfBalls = 0;
        try {
            numberOfBalls = Integer.parseInt(ballNumberField.getText());
        } catch (Exception e) {
            ballNumberError.setText("enter a number!");
            return;
        }

        if (numberOfBalls < 5 || numberOfBalls > 20) {
            ballNumberError.setText("invalid number");
            return;
        }

        user.setSelectedBallNumber(numberOfBalls);
        String selectedDifficulty = (String) difficultyBox.getValue();
        if (!SettingsMenu.isEnglish) {
            if (selectedDifficulty.equals("آسان")) selectedDifficulty = "Easy";
            else if (selectedDifficulty.equals("متوسط")) selectedDifficulty = "Average";
            else selectedDifficulty = "Hard";
        }
        user.setSelectedDifficulty(Difficulty.getDifficultyByName(selectedDifficulty));
        int mapNumber = 0;
        if (!SettingsMenu.isEnglish) {
            mapNumber = 1;
            if (mapBox.getValue().equals("نقشه 2")) mapNumber = 2;
            else if (mapBox.getValue().equals("نقشه 3")) mapNumber = 3;
        } else mapNumber = Integer.parseInt(((String) mapBox.getValue()).substring(4, 5));
        user.setSelectedMapNumber(mapNumber);
        DBController.saveAllUsers(null);
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(PrimaryMenu.stage);
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

    public void changeLanguage(ActionEvent actionEvent) throws Exception {
        isEnglish = !isEnglish;
        SettingsMenu settingsMenu = new SettingsMenu();
        settingsMenu.start(PrimaryMenu.stage);
    }

    public void changeColor(ActionEvent actionEvent) throws Exception {
        colored = !colored;
        SettingsMenu settingsMenu = new SettingsMenu();
        settingsMenu.start(PrimaryMenu.stage);
    }

    public void changeButton(ActionEvent actionEvent) throws Exception {
        ButtonsMenu buttonsMenu = new ButtonsMenu();
        buttonsMenu.start(buttonStage);
    }
}
