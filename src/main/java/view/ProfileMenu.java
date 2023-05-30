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
import java.nio.file.Files;

public class ProfileMenu extends Application {
    public static User user;
    public Label username;
    public TextField usernameField;
    public Button usernameButton;
    public Label oldPassword;
    public PasswordField oldPasswordField;
    public Label newPassword;
    public PasswordField newPasswordField;
    public Label confirmPassword;
    public PasswordField confirmPasswordField;
    public Button changePassword;
    public Text usernameError;
    public Text oldPasswordError;
    public Text newPasswordError;
    public Text confirmPasswordError;
    public Button back;
    public Button deleteAccount;
    public Button logout;
    public Button chooseAvatar;
    public Button uploadImage;
    public FileChooser chooseImageFile = new FileChooser();
    public static ImageView avatar;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane profilePane = FXMLLoader.load(ProfileMenu.class.getResource("/FXML/profileMenu.fxml"));
        Scene scene = new Scene(profilePane, 550, 650);
        makeAvatarStaff(profilePane);
        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        profilePane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Profile Menu");
        else stage.setTitle("منوی پروفایل");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        username.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        usernameField.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        usernameField.setText(user.getUsername());
        usernameButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        oldPassword.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        oldPasswordField.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        newPassword.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        newPasswordField.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        confirmPassword.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        confirmPasswordField.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        changePassword.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        usernameError.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.ITALIC, 14));
        oldPasswordError.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.ITALIC, 14));
        newPasswordError.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.ITALIC, 14));
        confirmPasswordError.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.ITALIC, 14));
        back.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        deleteAccount.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        logout.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) {
            username.setText("نام کاربری:");
            oldPassword.setText("گذرواژه قبلی:");
            newPassword.setText("گذرواژه جدید:");
            confirmPassword.setText("تایید گذرواژه:");
            usernameButton.setText("تغییر");
            changePassword.setText("تغییر گذرواژه");
            logout.setText("خروج از حساب کاربری");
            deleteAccount.setText("حذف حساب کاربری");
            back.setText("بازگشت");

            GridPane.setColumnIndex(username, 2);
            GridPane.setHalignment(username, HPos.LEFT);
            GridPane.setColumnIndex(usernameButton, 0);
            GridPane.setHalignment(usernameButton, HPos.RIGHT);
            GridPane.setColumnIndex(oldPassword, 2);
            GridPane.setHalignment(oldPassword, HPos.LEFT);
            GridPane.setColumnIndex(newPassword, 2);
            GridPane.setHalignment(newPassword, HPos.LEFT);
            GridPane.setColumnIndex(confirmPassword, 2);
            GridPane.setHalignment(confirmPassword, HPos.LEFT);
            GridPane.setColumnIndex(changePassword, 0);
            GridPane.setHalignment(changePassword, HPos.RIGHT);
        }
    }

    public void changeUsername(ActionEvent actionEvent) {
        usernameError.setText("");
        UserController.changeUsername(usernameField.getText(), usernameError);
    }

    public void changePassword(ActionEvent actionEvent) {
        oldPasswordError.setText("");
        newPasswordError.setText("");
        confirmPasswordError.setText("");
        boolean success = UserController.changePassword(oldPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText(),
                oldPasswordError, newPasswordError, confirmPasswordError);
        if (success) {
            oldPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        }
    }

    public void mainMenu(ActionEvent actionEvent) throws Exception {
        MainMenu mainMenu = new MainMenu();
        MainMenu.user = user;
        mainMenu.start(PrimaryMenu.stage);
    }

    public void deleteAccount(ActionEvent actionEvent) throws Exception {
        UserController.deleteAccount();
        ProfileMenu.user = null;
        MainMenu.user = null;
        PrimaryMenu primaryMenu = new PrimaryMenu();
        primaryMenu.start(PrimaryMenu.stage);
    }

    public void logout(ActionEvent actionEvent) throws Exception {
        ProfileMenu.user = null;
        MainMenu.user = null;
        PrimaryMenu primaryMenu = new PrimaryMenu();
        primaryMenu.start(PrimaryMenu.stage);
    }

    public void makeAvatarStaff(GridPane profilePane) {
        avatar = new ImageView(user.getAvatarUrl());
        GridPane.setRowIndex(avatar, 0);
        GridPane.setColumnIndex(avatar, 0);
        GridPane.setColumnSpan(avatar, 3);
        GridPane.setHalignment(avatar, HPos.CENTER);
        avatar.setFitWidth(150);
        avatar.setFitHeight(150);

        makeChooseAvatarButton(profilePane);
        makeUploadAvatarButton(profilePane, RegisterMenu.avatarsStage);

        profilePane.getChildren().addAll(avatar);
    }

    public void makeChooseAvatarButton(GridPane profilePane) {
        chooseAvatar = new Button("Choose Avatar");
        GridPane.setRowIndex(chooseAvatar, 1);
        GridPane.setColumnIndex(chooseAvatar, 0);
        GridPane.setHalignment(chooseAvatar, HPos.RIGHT);
        chooseAvatar.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) chooseAvatar.setText("انتخاب آواتار");

        chooseAvatar.setOnAction(actionEvent -> {
            AvatarsMenu.isFromRegister = false;
            AvatarsMenu avatarsMenu = new AvatarsMenu();
            try {
                avatarsMenu.start(RegisterMenu.avatarsStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        profilePane.getChildren().add(chooseAvatar);
    }

    public void makeUploadAvatarButton(GridPane profilePane, Stage stage) {
        uploadImage = new Button("Upload Avatar");
        GridPane.setRowIndex(uploadImage, 1);
        GridPane.setColumnIndex(uploadImage, 2);
        GridPane.setHalignment(uploadImage, HPos.LEFT);
        uploadImage.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        if (!SettingsMenu.isEnglish) uploadImage.setText("بارگذاری عکس");

        uploadImage.setOnAction(actionEvent -> {
            chooseImageFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = chooseImageFile.showOpenDialog(stage);
            if (file != null) {
                int dot = file.getName().lastIndexOf(".");
                String extension = file.getName().substring(dot, file.getName().length());
                File destination = new File("src/main/resources/avatars/uploaded/image" + RegisterMenu.uploadNumber + extension);
                try {
                    Files.copy(file.toPath(), destination.toPath());
                    avatar.setImage(new Image(destination.getAbsolutePath()));
                    user.setAvatarUrl(avatar.getImage().getUrl());
                    RegisterMenu.uploadNumber++;
                    DBController.saveUploadedImageNumber();
                    DBController.saveAllUsers(null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        profilePane.getChildren().add(uploadImage);
    }
}
