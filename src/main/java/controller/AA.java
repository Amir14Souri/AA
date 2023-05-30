package controller;

import model.Game;
import model.User;
import model.enumerations.Difficulty;

import java.util.ArrayList;
import java.util.Collections;

public class AA {
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<User> scoreboard = new ArrayList<>();
    private static User currentUser;

    public static ArrayList<User> getScoreboard() {
        return scoreboard;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        AA.currentUser = currentUser;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static User getUserByUsername(String username) {
        for (int i = 0; i < users.size(); i++) {
            User user = AA.users.get(i);
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public static void addUser(User user) {
        AA.users.add(user);
    }

    public static void deleteUser(User user) {
        users.remove(user);
    }

    public static void refreshScoreboard() {
        scoreboard = new ArrayList<>();
        int count = users.size();
        int trueCount = count;
        for (int i = 0; i < count; i++) {
            if (users.get(i).getBestGame() != null) scoreboard.add(users.get(i));
            else trueCount--;
        }
        for (int i = 0; i < trueCount; i++) {
            Game game1 = scoreboard.get(i).getBestGame();
            for (int j = i + 1; j < trueCount; j++) {
                Game game2 = scoreboard.get(j).getBestGame();
                if (game1.getPoint() < game2.getPoint()) {
                    Collections.swap(scoreboard, i, j);
                } else if (game1.getPoint() == game2.getPoint() && game1.getElapsedTime() > game2.getElapsedTime()) {
                    Collections.swap(scoreboard, i, j);
                } else if (game1.getPoint() == game2.getPoint() && game1.getElapsedTime() == game2.getElapsedTime()
                        && game1.getUser().getUsername().compareToIgnoreCase(game2.getUser().getUsername()) > 0) {
                    Collections.swap(scoreboard, i, j);
                }
            }
        }
    }

    public static int getRank(User user, ArrayList<User> scoreboardByDifficulty) {
        for (int i = 0; i < scoreboardByDifficulty.size(); i++) {
            if (scoreboardByDifficulty.get(i).equals(user))
                return i + 1;
        }
        return -1;
    }

    public static ArrayList<User> getScoreboardByDifficulty(Difficulty difficulty) {
        refreshScoreboard();
        ArrayList<User> resultScoreboard = new ArrayList<>();
        for (int i = 0; i < scoreboard.size(); i++) {
            if (scoreboard.get(i).getBestGame().getDifficulty().equals(difficulty))
                resultScoreboard.add(scoreboard.get(i));
        }

        for (int i = 0; i < resultScoreboard.size(); i++) {
            User user = resultScoreboard.get(i);
            user.setRank(AA.getRank(user, resultScoreboard));
        }

        return resultScoreboard;
    }
}
