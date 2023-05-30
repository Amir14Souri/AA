package controller;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.User;
import view.ProfileMenu;

public class UserController {
    public static User register(String username, String password, Text usernameError, Text passwordError, String avatarUrl) {
        if (AA.getUserByUsername(username) != null) {
            usernameError.setText("this username already exists");
            return null;
        }
        if (password.contains(" ")) {
            passwordError.setText("password should not contain space");
            return null;
        }
        if (password.length() < 6) {
            passwordError.setText("password must be at least 6 characters");
            return null;
        }

        User newUser = new User(username, password, avatarUrl);
        AA.addUser(newUser);
        DBController.saveAllUsers(null);
        AA.refreshScoreboard();
        return newUser;
    }

    public static User login(String username, String password, Text usernameError, Text passwordError) {
        User user = AA.getUserByUsername(username);
        if (user == null) {
            usernameError.setText("user not found");
            return null;
        }
        if (!user.getPassword().equals(password)) {
            passwordError.setText("password is wrong");
            return null;
        }

        return user;
    }

    public static void changeUsername(String newUsername, Text usernameError) {
        User currentUser = ProfileMenu.user;
        if (currentUser.getUsername().equals(newUsername)) {
            usernameError.setText("username hasn't been changed");
            return;
        }
        if (AA.getUserByUsername(newUsername) != null) {
            usernameError.setText("this username has been used before");
            return;
        }

        currentUser.setUsername(newUsername);
        DBController.saveAllUsers(null);
        usernameError.setText("username changed successfully");
    }

    public static boolean changePassword(String oldPassword, String newPassword, String confirmPassword,
                                         Text oldPasswordError, Text newPasswordError, Text confirmPasswordError) {
        User currentUser = ProfileMenu.user;
        if (!currentUser.getPassword().equals(oldPassword)) {
            oldPasswordError.setText("old password is wrong");
            return false;
        }
        if (newPassword.contains(" ")) {
            newPasswordError.setText("new password should not contain space");
            return false;
        }
        if (newPassword.length() < 6) {
            newPasswordError.setText("new password must be at least 6 characters");
            return false;
        }
        if (!confirmPassword.equals(newPassword)) {
            confirmPasswordError.setText("new password doesn't match with confirmation");
            return false;
        }

        currentUser.setPassword(newPassword);
        DBController.saveAllUsers(null);
        return true;
    }

    public static void deleteAccount() {
        AA.deleteUser(ProfileMenu.user);
        DBController.saveAllUsers(null);
        AA.refreshScoreboard();
    }
}
