package controller;

import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Game;
import model.History;
import model.Move;
import model.pieces.Piece;
import util.Side;
import view.HistoryView;
import view.MoveListItem;

import java.util.Iterator;

public class HistoryController {
    private final HistoryView historyView;
    private final History history;
    private final GameController gameController;
    private final Game game;
    private boolean undo = false; //Effects onBoardChanged based on the change is from a new move or from undo

    public HistoryController(GameController gameController, Game game, HistoryView view){
        this.game = game;
        this.history = game.getHistory();
        this.historyView = view;
        this.gameController = gameController;
        this.game.boardChangedProperty().addListener(this::onBoardChanged);
        this.historyView.setMoveClickHandler(this::processMoveClick);
        this.historyView.setButtonClickHandler(this::forward,this::backward,this::undo);

        Iterator<Move> iterator = history.getMoves();
        while (iterator.hasNext()) {
            Move move = iterator.next();
            historyView.addMove(move.toString());
        }
    }

    public void forward(Button button, ActionEvent event){
        int index = historyView.getHighlightIndex();
        if(index == history.getNumberOfMoves()-1){return;}
        highlightMove(index+1);
    }

    public void backward(Button button, ActionEvent event){
        int index = historyView.getHighlightIndex();
        if(index < 1){return;}
        highlightMove(index-1);
    }

    public void undo(Button button, ActionEvent event){
        switch (history.getNumberOfMoves()){
            case 0:
                return;
            case 1:
                break; //If there is only one move, no move should be highlighted
            default:
                highlightMove(history.getNumberOfMoves()-2);
        }
        undo = true;
        game.undo();
        historyView.removeLastMoves(1);
    }

    public void onBoardChanged(Observable observable){
        if(undo){
            undo = false;
            return;
        }
        Move move = history.getLast();
        String moveString = move.toString();
        historyView.addMove(moveString);
    }

    public void processMoveClick(MoveListItem moveListItem, MouseEvent event){
        boolean isRightClick = event.getButton() == MouseButton.SECONDARY;
        if(isRightClick){return;}

        highlightMove(moveListItem.getIndex());
    }

    public void highlightLastMove(){
        highlightMove(history.getNumberOfMoves()-1);
    }

    private void highlightMove(int index){
        int lastHighlightIndex = historyView.getHighlightIndex();
        if(index == lastHighlightIndex){return;}

        historyView.highlight(index);
        gameController.unselectsSquare();

        boolean reverse = index < lastHighlightIndex;

        int iteratorIndex1 = lastHighlightIndex;
        /*The currently highlighted move shouldn't be part of the iterator if running forward
        because the move has already been made in view.*/
        if(!reverse){iteratorIndex1 += 1;}

        //An iterator of all moves between the new and previously highlighted moves
        Iterator<Move> moveIterator = history.getMoves(iteratorIndex1,index);
        Move move = null;
        while(moveIterator.hasNext()){
            move = moveIterator.next();
            if(reverse){reverseMove(move);}
            else{forwardMove(move);}
        }
        if(reverse){forwardMove(move);}
        gameController.clearHighlightedSquares();
        gameController.highlightSquare(move.to());
        if(!move.fromPlayerHand()){
            gameController.highlightSquare(move.from());}
    }

    //Updates Boardview based on a move
    private void forwardMove(Move move){
        gameController.setBoardViewSquare(move.movedPiece(),move.to());
        if(move.capturedPiece() != null){
            Piece piece = move.capturedPiece();
            Side side = (piece.getSide() == Side.SENTE) ? Side.GOTE : Side.SENTE;
            gameController.changeCountAtPieceStandView(piece.getClass(),side,1);
        }
        if(move.fromPlayerHand()){
            Piece piece = move.movedPiece();
            gameController.changeCountAtPieceStandView(piece.getClass(),piece.getSide(),-1);
        }
        else{
            gameController.setBoardViewSquare(null,move.from());}
    }

    private void reverseMove(Move move){
        gameController.setBoardViewSquare(move.capturedPiece(),move.to());
        if(move.capturedPiece() != null){
            Piece piece = move.capturedPiece();
            Side side = (piece.getSide() == Side.SENTE) ? Side.GOTE : Side.SENTE;
            gameController.changeCountAtPieceStandView(piece.getClass(),side,-1);
        }
        if(move.fromPlayerHand()){
            Piece piece = move.movedPiece();
            gameController.changeCountAtPieceStandView(piece.getClass(),piece.getSide(),1);
        }
        else{
            gameController.setBoardViewSquare(move.movedPiece(),move.from());}
    }
}
