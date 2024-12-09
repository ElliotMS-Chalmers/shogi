package controller;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
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
    private final ShogiController shogiController;

    public HistoryController(ShogiController shogiController, Game game, HistoryView view){
        this.history = game.getHistory();
        this.historyView = view;
        this.shogiController = shogiController;
        game.boardChangedProperty().addListener(this::onBoardChanged);
        this.historyView.setMoveClickHandler(this::processMoveClick);
    }

    public void onBoardChanged(Observable observable){
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
        shogiController.unselectsSquare();

        boolean reverse = index < lastHighlightIndex;

        int iteratorIndex1 = lastHighlightIndex;
        /*Draget som redan är highlightats ska inte vara med i iteratorn om den kör framlänges
        eftersom det draget redan har gjorts i viewn*/
        if(!reverse){iteratorIndex1 += 1;}

        //En iterator av alla drag mellan index och lastHighlightIndex
        Iterator<Move> moveIterator = history.getMoves(iteratorIndex1,index);
        Move move = null;
        while(moveIterator.hasNext()){
            move = moveIterator.next();
            if(reverse){reverseMove(move);}
            else{forwardMove(move);}
        }
        if(reverse){forwardMove(move);}
        shogiController.clearHighlightedSquares();
        shogiController.highlightSquare(move.to());
        if(!move.fromPlayerHand()){shogiController.highlightSquare(move.from());}
    }
    //En funktion för att uppdatera BoardView utifrån ett drag
    private void forwardMove(Move move){
        shogiController.setBoardViewSquare(move.movedPiece(),move.to());
        if(move.capturedPiece() != null){
            Piece piece = move.capturedPiece();
            Side side = (piece.getSide() == Side.SENTE) ? Side.GOTE : Side.SENTE;
            shogiController.changeCountAtPieceStandView(piece.getClass(),side,1);
        }
        if(move.fromPlayerHand()){
            Piece piece = move.movedPiece();
            shogiController.changeCountAtPieceStandView(piece.getClass(),piece.getSide(),-1);
        }
        else{shogiController.setBoardViewSquare(null,move.from());}
    }

    private void reverseMove(Move move){
        shogiController.setBoardViewSquare(move.capturedPiece(),move.to());
        if(move.capturedPiece() != null){
            Piece piece = move.capturedPiece();
            Side side = (piece.getSide() == Side.SENTE) ? Side.GOTE : Side.SENTE;
            shogiController.changeCountAtPieceStandView(piece.getClass(),side,-1);
        }
        if(move.fromPlayerHand()){
            Piece piece = move.movedPiece();
            shogiController.changeCountAtPieceStandView(piece.getClass(),piece.getSide(),1);
        }
        else{shogiController.setBoardViewSquare(move.movedPiece(),move.from());}
    }


}
