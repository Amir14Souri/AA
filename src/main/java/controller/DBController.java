package controller;

import model.Game;
import model.ShootingBall;
import model.User;
import model.enumerations.Difficulty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import view.RegisterMenu;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class DBController {
    public static void saveAllUsers(User saveGameUser) {
        JSONArray users = new JSONArray();
        for (int i = 0; i < AA.getUsers().size(); i++) {
            User user = AA.getUsers().get(i);
            JSONObject userObject = new JSONObject();
            userObject.put("username", user.getUsername());
            userObject.put("password", user.getPassword());
            userObject.put("avatarUrl", user.getAvatarUrl());
            userObject.put("selectedDifficultyName", user.getSelectedDifficulty().getName());
            userObject.put("selectedBallNumber", user.getSelectedBallNumber());
            userObject.put("selectedMapNumber", user.getSelectedMapNumber());
            userObject.put("selectedShootButton", user.getSelectedShootButton());
            userObject.put("selectedFreezeButton", user.getSelectedFreezeButton());
            userObject.put("hasSavedGame", user.isHasSavedGame());
            if (user.getBestGame() != null) {
                userObject.put("bestGameDifficultyName", user.getBestGame().getDifficulty().getName());
                userObject.put("bestGamePoint", user.getBestGame().getPoint());
                userObject.put("bestGameElapsedTime", user.getBestGame().getElapsedTime());
            }

            if (saveGameUser != null && user.equals(saveGameUser)) {
                userObject.put("savedGameBallsNumber", user.getSavedGameBalls().size());
                for (int j = 0; j < user.getSavedGameBalls().size(); j++) {
                    ShootingBall ball = user.getSavedGameBalls().get(j);
                    userObject.put("ball" + j + "Angle", ball.getRotatedDegree());
                    userObject.put("ball" + j + "Number", ball.getNumber());
                }
                userObject.put("savedGameRotateDirection", user.getSavedGame().getCurrentRotateDirection());
                userObject.put("savedGameDifficulty", user.getSavedGame().getDifficulty().getName());
                userObject.put("savedGamePoint", user.getSavedGame().getPoint());
                userObject.put("savedGameElapsedTime", user.getSavedGame().getElapsedTime());
                userObject.put("savedGameCurrentBallNumber", user.getSavedGame().getCurrentBallNumber());
                userObject.put("savedGameNumberOfBalls", user.getSavedGame().getNumberOfBalls());
                userObject.put("savedGameCurrentPhase", user.getSavedGame().getCurrentPhase());
                userObject.put("savedGameShootAngle", user.getSavedGame().getCurrentShootAngle());
                userObject.put("savedGameFreezeState", user.getSavedGame().getCurrentFreezeState());
                userObject.put("savedGameTimerSeconds", user.getSavedGame().getTimerSeconds());
            }
            users.add(userObject);
        }
        try {
            FileWriter usersWriter = new FileWriter("src/main/resources/AA/users.json", false);
            usersWriter.write(users.toJSONString());
            usersWriter.close();
        } catch (Exception e) {
            return;
        }
    }

    public static void loadAllUsers() {
        Scanner scanner = null;
        try {
            File usersFile = new File("src/main/resources/AA/users.json");
            scanner = new Scanner(usersFile);
        } catch (Exception e) {
            return;
        }
        String fileContent = "";
        while (scanner.hasNext()) {
            fileContent += scanner.nextLine();
        }
        Object object = JSONValue.parse(fileContent);
        JSONArray users = (JSONArray) object;
        if (users == null) return;
        AA.getUsers().clear();
        for (int i = 0; i < users.size(); i++) {
            JSONObject userObject = (JSONObject) users.get(i);
            User user = new User(userObject.get("username").toString(), userObject.get("password").toString(),
                    userObject.get("avatarUrl").toString());
            user.setSelectedBallNumber(Integer.parseInt(userObject.get("selectedBallNumber").toString()));
            user.setSelectedDifficulty(Difficulty.getDifficultyByName(userObject.get("selectedDifficultyName").toString()));
            user.setSelectedMapNumber(Integer.parseInt(userObject.get("selectedMapNumber").toString()));
            user.setSelectedShootButton(userObject.get("selectedShootButton").toString());
            user.setSelectedFreezeButton(userObject.get("selectedFreezeButton").toString());
            if (userObject.get("bestGamePoint") != null) {
                user.setBestGamePoint(Integer.parseInt(userObject.get("bestGamePoint").toString()));
                user.setBestGameElapsedTime(Double.parseDouble(userObject.get("bestGameElapsedTime").toString()));
                Game game = new Game();
                game.setUser(user);
                game.setDifficulty(Difficulty.getDifficultyByName(userObject.get("bestGameDifficultyName").toString()));
                game.setPoint(Integer.parseInt(userObject.get("bestGamePoint").toString()));
                game.setElapsedTime(Double.parseDouble(userObject.get("bestGameElapsedTime").toString()));
                user.setBestGame(game);
            }

            if ((Boolean) userObject.get("hasSavedGame") == true) {
                for (int j = 0; j < Integer.parseInt(userObject.get("savedGameBallsNumber").toString()); j++) {
                    ShootingBall shootingBall = new ShootingBall(Double.parseDouble(userObject.get("ball" + j + "Angle").toString()),
                            Integer.parseInt(userObject.get("ball" + j + "Number").toString()), user);
                    user.addSavedGameBall(shootingBall);
                }
                Game game = new Game(user, Difficulty.getDifficultyByName(userObject.get("savedGameDifficulty").toString()),
                        Integer.parseInt(userObject.get("savedGamePoint").toString()), Double.parseDouble(userObject.get("savedGameElapsedTime").toString()),
                        Integer.parseInt(userObject.get("savedGameCurrentBallNumber").toString()), Integer.parseInt(userObject.get("savedGameNumberOfBalls").toString()),
                        Integer.parseInt(userObject.get("savedGameRotateDirection").toString()), Integer.parseInt(userObject.get("savedGameCurrentPhase").toString()),
                        Double.parseDouble(userObject.get("savedGameShootAngle").toString()), Double.parseDouble(userObject.get("savedGameFreezeState").toString()),
                        Integer.parseInt(userObject.get("savedGameTimerSeconds").toString()));
                user.setSavedGame(game);
            }
            AA.addUser(user);
        }
        AA.refreshScoreboard();
    }

    public static void saveUploadedImageNumber() {
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/AA/uploadedImageNumber.txt", false);
            fileWriter.write(Integer.toString(RegisterMenu.uploadNumber));
            fileWriter.close();
        } catch (Exception e) {
            return;
        }
    }

    public static void loadUploadedImageNumber() {
        Scanner scanner = null;
        try {
            File file = new File("src/main/resources/AA/uploadedImageNumber.txt");
            scanner = new Scanner(file);
        } catch (Exception e) {
            return;
        }
        RegisterMenu.uploadNumber = Integer.parseInt(scanner.nextLine());
    }
}
