package model;

import variants.Variant;

public class Game {
    private boolean turn = false;
    private Variant gameMode;
    private Board board;
    //We might not use players
    private Player player1;
    private Player player2;

    public Game(Variant gameMode){
        this.gameMode = gameMode;
        this.board = new Board(gameMode.getWidth(),gameMode.getHeight());
        this.gameMode.initiatePieces(board); /*Det är inte optimalt att skicka board till Variant,
                                             men vet inte hur annars man ska göra det.*/
    }
    public void move(int x1,int y1,int x2,int y2){
        this.board.move(x1,y1,x2,y2);
        if(turn){turn = false;}
        else{turn = true;}
    }
}
