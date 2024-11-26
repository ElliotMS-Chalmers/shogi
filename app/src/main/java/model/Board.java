package model;

import pieces.Piece;

public class Board {
    private Piece[][] grid;

    public Board(int width, int height){
        grid = new Piece[width][height];
        //Pjäserna antar jag sätts via Variant-klassen, metoden setAtPosition kan användas
    }
    public void move(int x1, int y1, int x2, int y2){ //Is run throuhg Game to alse change the turn
        Piece piece = grid[x1][y1];
        grid[x2][y2] = piece;
        grid[x1][y1] = null;
    }
    public Piece[][] getCurrentBoard(){
        return grid;
    }
    public void setAtPosition(int x, int y, Piece piece){
        grid[x][y] = piece;
    }
}
