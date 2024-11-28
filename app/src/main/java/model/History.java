package model;

import model.pieces.Piece;
import util.Pos;
import util.Sfen;
import util.Side;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class History {
    //A stack with each element containing a move and the sfen-position that resulted from that move.
    private final Stack<Pair> pastStates = new Stack<>();

    private class Pair{
        private final String move;
        private final Sfen sfen;
        public Pair(String move, Sfen sfen){
            this.move = move;
            this.sfen = sfen;
        }
        public String getMove(){return move;}
        public Sfen getSfen(){return sfen;}
    }

    public History(Sfen startingSfen){
        pastStates.push(new Pair(null,startingSfen));
    }
    public void addMove(Pos from, Pos to, Piece piece, boolean turn,
                        String newPosition, String capturedPieces){
        String move = moveToString(from,to,piece);
        Sfen sfen = new Sfen(newPosition,turn ? 'b':'w',capturedPieces,pastStates.size());
        pastStates.push(new Pair(move,sfen));
    }
    public Sfen getSfen(int index){
        return pastStates.get(index).getSfen();
    }
    public String getMove(int index){
        return pastStates.get(index).getMove();
    }
    public Iterator<String> getMoves(){
        return new Iterator<String>() {
            //Index starts at 1 because the first element in pastStates has no move.
            int index = 1;
            @Override
            public boolean hasNext() {
                return index < pastStates.size();
            }
            @Override
            public String next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                return pastStates.get(index++).getMove();
            }
        };
    }
    public Sfen undo(){ //Removes the most recent move and returns the previous position.
        pastStates.pop();
        return pastStates.getLast().getSfen();
    }
    public Sfen reset(){ //Returns the starting position.
        Pair startingPair = pastStates.getFirst();
        pastStates.clear();
        pastStates.push(startingPair);
        return startingPair.getSfen();
    }
    private String moveToString(Pos from, Pos to, Piece piece){
        //
        return piece.getSfenAbbreviation()+
                String.valueOf(from.col()+1)+String.valueOf(from.row()+1)+'-'+
                String.valueOf(to.col()+1)+String.valueOf(to.row()+1);
    }
}
