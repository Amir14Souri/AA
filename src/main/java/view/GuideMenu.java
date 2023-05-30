package view;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GuideMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GridPane guidePane = new GridPane();
        guidePane.setHgap(20);
        guidePane.setVgap(10);
        guidePane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(guidePane, 350, 150);

        Text shoot = new Text("Press " + GameMenu.shootButton + " to shoot a ball");
        if (!SettingsMenu.isEnglish) shoot = new Text("از دکمه " + GameMenu.shootButton + " برای پرتاب استفاده کن");
        Text freeze = new Text("Press " + GameMenu.freezeButton + " to enter freeze state");
        if (!SettingsMenu.isEnglish) freeze = new Text("از دکمه " + GameMenu.freezeButton + " برای ورود به حالت یخ زده استفاده کن");
        shoot.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 16));
        freeze.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 16));
        guidePane.add(shoot, 0, 0);
        guidePane.add(freeze, 0, 1);
        GridPane.setHalignment(shoot, HPos.CENTER);
        GridPane.setHalignment(freeze, HPos.CENTER);

        Button back = new Button("Back");
        if (!SettingsMenu.isEnglish) back.setText("بازگشت");
        back.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        back.setOnAction(actionEvent -> {
            PauseMenu.guideStage.close();
        });
        guidePane.add(back, 0, 2);

        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        guidePane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Control Guide");
        else stage.setTitle("راهنمای کنترل");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
