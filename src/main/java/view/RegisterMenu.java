package view;

import controller.DBController;
import controller.UserController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Random;

public class RegisterMenu extends Application {
    public static Stage avatarsStage;
    public static boolean isRegister = true;
    public Text hello;
    public Label username;
    public Label password;
    public TextField usernameField;
    public PasswordField passwordField;
    public Button back;
    public Button register;
    public Text usernameError;
    public Text passwordError;
    public static ImageView avatar;
    public Button chooseAvatar;
    public Button uploadImage;
    public FileChooser chooseImageFile = new FileChooser();
    public static int uploadNumber;

    @Override
    public void start(Stage stage) throws Exception {
        avatarsStage = new Stage();
        URL url = RegisterMenu.class.getResource("/FXML/registerMenu.fxml");
        GridPane registerPane = FXMLLoader.load(url);

        Scene scene = (isRegister) ? new Scene(registerPane, 400, 500) : new Scene(registerPane, 400, 300);

        int avatarNumber = new Random().nextInt(15) + 1;
        avatar = new ImageView(RegisterMenu.class.getResource("/avatars/avatar" + avatarNumber + ".png").toExternalForm());
        GridPane.setColumnIndex(avatar, 0);
        GridPane.setRowIndex(avatar, 1);
        GridPane.setColumnSpan(avatar, 2);
        GridPane.setHalignment(avatar, HPos.CENTER);
        avatar.setFitWidth(150);
        avatar.setFitHeight(150);
        avatar.getStyleClass().add("avatar");

        if (isRegister && SettingsMenu.isEnglish) stage.setTitle("Register Menu");
        else if (!isRegister && SettingsMenu.isEnglish) stage.setTitle("Login Menu");
        else if (isRegister && !SettingsMenu.isEnglish) stage.setTitle("منوی ثبت نام");
        else stage.setTitle("منوی ورود");
        if (isRegister) {
            registerPane.getChildren().add(avatar);
            makeChooseAvatarButton(registerPane);
            makeUploadAvatarButton(registerPane, stage);
        }
        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        registerPane.getStylesheets().add(RegisterMenu.class.getResource(cssFile).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        username.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        password.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        usernameField.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        passwordField.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        back.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        register.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        if (!isRegister) register.setText("Login");
        hello.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 24));
        usernameError.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.ITALIC, 14));
        passwordError.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.ITALIC, 14));
        usernameField.textProperty().addListener((observable, oldText, newText) -> {
            String helloWord = (SettingsMenu.isEnglish) ? "Hello" : "سلام";
            hello.setText(helloWord + " " + newText);
        });

        if (!SettingsMenu.isEnglish) {
            username.setText("نام کاربری:");
            password.setText("گذرواژه:");
            back.setText("بازگشت");
            register.setText("ثبت نام");
            hello.setText("سلام");
            GridPane.setColumnIndex(username, 1);
            GridPane.setColumnIndex(usernameField, 0);
            GridPane.setHalignment(username, HPos.LEFT);

            GridPane.setColumnIndex(password, 1);
            GridPane.setColumnIndex(passwordField, 0);
            GridPane.setHalignment(password, HPos.LEFT);
        }
    }

    public void backToPrimaryMenu(ActionEvent actionEvent) throws Exception {
        PrimaryMenu primaryMenu = new PrimaryMenu();
        primaryMenu.start(PrimaryMenu.stage);
    }

    public void register(ActionEvent actionEvent) throws Exception {
        boolean error = false;
        usernameError.setText("");
        passwordError.setText("");
        if (usernameField.getText().isEmpty()) {
            usernameError.setText("username field is empty");
            error = true;
        }
        if (passwordField.getText().isEmpty()) {
            passwordError.setText("password field is empty");
            error = true;
        }
        if (error) return;

        User user;
        if (isRegister)
            user = UserController.register(usernameField.getText(), passwordField.getText(), usernameError, passwordError, avatar.getImage().getUrl());
        else
            user = UserController.login(usernameField.getText(), passwordField.getText(), usernameError, passwordError);

        if (user != null) {
            MainMenu mainMenu = new MainMenu();
            MainMenu.user = user;
            mainMenu.start(PrimaryMenu.stage);
        }
    }

    public void makeChooseAvatarButton(GridPane registerPane) {
        chooseAvatar = new Button("Choose Avatar");
        GridPane.setRowIndex(chooseAvatar, 2);
        GridPane.setColumnIndex(chooseAvatar, 0);
        GridPane.setHalignment(chooseAvatar, HPos.LEFT);
        chooseAvatar.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) chooseAvatar.setText("انتخاب آواتار");

        chooseAvatar.setOnAction(actionEvent -> {
            AvatarsMenu.isFromRegister = true;
            AvatarsMenu avatarsMenu = new AvatarsMenu();
            try {
                avatarsMenu.start(avatarsStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        registerPane.getChildren().add(chooseAvatar);
    }

    public void makeUploadAvatarButton(GridPane registerPane, Stage stage) {
        uploadImage = new Button("Upload Avatar");
        GridPane.setRowIndex(uploadImage, 2);
        GridPane.setColumnIndex(uploadImage, 1);
        GridPane.setHalignment(uploadImage, HPos.RIGHT);
        uploadImage.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) uploadImage.setText("بارگذاری عکس");

        uploadImage.setOnAction(actionEvent -> {
            chooseImageFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = chooseImageFile.showOpenDialog(stage);
            if (file != null) {
                int dot = file.getName().lastIndexOf(".");
                String extension = file.getName().substring(dot, file.getName().length());
                File destination = new File("src/main/resources/avatars/uploaded/image" + uploadNumber + extension);
                try {
                    Files.copy(file.toPath(), destination.toPath());
                    avatar.setImage(new Image(destination.getAbsolutePath()));
                    uploadNumber++;
                    DBController.saveUploadedImageNumber();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        registerPane.getChildren().add(uploadImage);
    }
}
