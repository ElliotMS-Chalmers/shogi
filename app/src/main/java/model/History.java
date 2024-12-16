package model;

import model.pieces.Piece;
import util.Pos;

import java.util.*;

public class History {
    private final Stack<Move> moves = new Stack<>();

    public void addMove(Move move){
        moves.push(move);
    }
    public Move getMove(int index){
        return moves.get(index);
    }
    public int getNumberOfMoves(){return moves.size();}
    public Iterator<Move> getMoves(int index1, int index2){
        if (moves.isEmpty() || index1 < 0 || index2 < 0 || index1 >= moves.size() || index2 >= moves.size()) {
            return Collections.emptyIterator();
        }

        return new Iterator<Move>() {
            boolean reverse = (index1 > index2);
            int index = index1;
            int endIndex = index2;
            int increment = reverse ? -1 : 1;

            @Override
            public boolean hasNext() {
                if(reverse){return index >= endIndex;}
                return index <= endIndex;
            }
            @Override
            public Move next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                Move move = moves.get(index);
                index += increment;
                return move;
            }
        };
    }
    public Iterator<Move> getMoves(){
        return getMoves(0,getNumberOfMoves()-1);
    }
    public Move removeLast(){ //Removes the most recent move, editing the board happens in Game.
        return moves.pop();
    }
    public Move getLast(){
        return moves.getLast();
    }
    public void reset(){
        moves.clear();
    }
}
