package view;

import controller.DBController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AvatarsMenu extends Application {
    public static boolean isFromRegister;
    @Override
    public void start(Stage stage) throws Exception {
        GridPane avatarsPane = new GridPane();
        Scene scene = new Scene(avatarsPane, 400, 500);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                int avatarNumber = i * 4 + j + 1;
                ImageView avatar = new ImageView(getClass().getResource("/avatars/avatar" + avatarNumber + ".png").toExternalForm());
                avatar.setFitWidth(100);
                avatar.setFitHeight(100);
                GridPane.setRowIndex(avatar, i);
                GridPane.setColumnIndex(avatar, j);
                chooseAvatar(avatar, stage);
                avatarsPane.getChildren().add(avatar);
            }
        }

        String cssFile = (SettingsMenu.colored) ? "/styles.css" : "/blackAndWhite.css";
        avatarsPane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (SettingsMenu.isEnglish) stage.setTitle("Choose Avatar");
        else stage.setTitle("انتخاب آواتار");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void chooseAvatar(ImageView avatar, Stage stage) {
        avatar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String imageUrl = ((ImageView) mouseEvent.getSource()).getImage().getUrl();
                if (isFromRegister) RegisterMenu.avatar.setImage(new Image(imageUrl));
                else {
                    ProfileMenu.avatar.setImage(new Image(imageUrl));
                    ProfileMenu.user.setAvatarUrl(imageUrl);
                    DBController.saveAllUsers(null);
                }
                stage.close();
            }
        });
    }
}
