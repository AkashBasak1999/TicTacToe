package com.tictactoe.storage;

import com.tictactoe.model.Game;

import java.util.HashMap;
import java.util.Map;
//so this class is made for basically storing all the games object archives in a map format 
//game object consists of the turns 
public class GameStorage {

    private static Map<String, Game> games;
    private static GameStorage instance;

    private GameStorage() {
        games = new HashMap<>();//so it is written inside the Constructor which means whenever
        //a new object will be created then a map will be created
    }

    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGame(Game game) {
        games.put(game.getGameId(), game);
    }
}