package model;

import model.pieces.Piece;
import util.Pos;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

public class History {
    private final Stack<Move> moves = new Stack<>();

    public History(){}
    public History(List<Move> moves){
        this.moves.addAll(moves);
    }
    public void addMove(Move move){
        moves.push(move);
    }
    public Move getMove(int index){
        return moves.get(index);
    }
    public Iterator<Move> getMoves(int startIndex, int endIndex){
        return new Iterator<Move>() {
            //Index starts at 1 because the first element in moves has no move.
            int index = startIndex;

            @Override
            public boolean hasNext() {
                return index <= endIndex;
            }
            @Override
            public Move next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                return moves.get(index++);
            }
        };
    }
    public Iterator<Move> getMoves(){
        return getMoves(0,moves.size()-1);
    }
    public Move removeLast(){ //Removes the most recent move, editing the board happens in Game.
        return moves.pop();
    }
    public void reset(){
        moves.clear();
    }
    private String moveToString(Pos from, Pos to, Piece piece){
        return piece.getSfenAbbreviation()+
                String.valueOf(from.col()+1)+String.valueOf(from.row()+1)+'-'+
                String.valueOf(to.col()+1)+String.valueOf(to.row()+1);
    }
}
