package com.tictactoe.model;
import lombok.Data;


//game object stores the turns in terms of board[][].
@Data
public class Game {

    private String gameId;
    private Player player1;
    private Player player2;
    private GameStatus status;
    private int[][] board;
    private TicToe winner;

}