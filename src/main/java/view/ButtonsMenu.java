package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ButtonsMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GridPane buttonPane = new GridPane();
        buttonPane.setVgap(10);
        buttonPane.setHgap(20);
        buttonPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(buttonPane, 300, 150);

        Text text = new Text();
        text.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.ITALIC, 14));
        buttonPane.add(text, 0, 1);
        GridPane.setColumnSpan(text, 2);

        Button shoot = new Button("Shoot Button");
        if (!SettingsMenu.isEnglish) shoot.setText("کنترل پرتاب");
        shoot.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        shoot.setOnAction(actionEvent -> {
            text.setText("enter a key !!");
            if (!SettingsMenu.isEnglish) text.setText("یک دکمه را انتخاب کن !!");
            shoot.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    SettingsMenu.user.setSelectedShootButton(keyEvent.getCode().getName());
                    text.setText("new key for shooting: " + keyEvent.getCode().getName());
                    if (!SettingsMenu.isEnglish) text.setText("دکمه جدید برای پرتاب: " + keyEvent.getCode().getName());
                }
            });
        });
        buttonPane.add(shoot, 0, 0);

        Button freeze = new Button("Freeze Button");
        if (!SettingsMenu.isEnglish) freeze.setText("کنترل یخ زده");
        freeze.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        freeze.setOnAction(actionEvent -> {
            text.setText("enter a key !!");
            if (!SettingsMenu.isEnglish) text.setText("یک دکمه را انتخاب کن !!");
            freeze.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    SettingsMenu.user.setSelectedFreezeButton(keyEvent.getCode().getName());
                    text.setText("new key for freezing: " + keyEvent.getCode().getName());
                    if (!SettingsMenu.isEnglish) text.setText("دکمه جدید برای یخ زده: " + keyEvent.getCode().getName());
                }
            });
        });
        buttonPane.add(freeze, 1, 0);

        Button back = new Button("Back");
        if (!SettingsMenu.isEnglish) back.setText("بازگشت");
        back.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        back.setOnAction(actionEvent -> {
            SettingsMenu.buttonStage.close();
        });
        buttonPane.add(back, 0, 2);
        GridPane.setColumnSpan(buttonPane, 2);

        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        buttonPane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Choose Button");
        else stage.setTitle("انتخاب کنترل");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
