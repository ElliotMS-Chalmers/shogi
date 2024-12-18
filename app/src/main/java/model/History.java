package model;

import java.util.*;

/**
 * Represents a history of moves in a game, allowing tracking, retrieval,
 * iteration, and modification of the list of moves.
 */
public class History {

    // Stack to store the sequence of moves.
    private final Stack<Move> moves = new Stack<>();

    /**
     * Adds a move to the history.
     *
     * @param move the Move to be added to the history
     */
    public void addMove(Move move) {
        moves.push(move);
    }

    /**
     * @return the number of moves in the history
     */
    public int getNumberOfMoves() {
        return moves.size();
    }

    /**
     * Returns an iterator over a range of moves in the history, either forward or backwards
     * depending on the order of the parameters.
     *
     * @param index1 the starting index of the range
     * @param index2 the ending index of the range
     * @return an Iterator for the moves in the specified range,
     *         or an empty iterator if the indices are invalid or the history is empty
     */
    public Iterator<Move> getMoves(int index1, int index2) {
        if (moves.isEmpty() || index1 < 0 || index2 < 0 || index1 >= moves.size() || index2 >= moves.size()) {
            return Collections.emptyIterator();
        }

        return new Iterator<Move>() {
            final boolean reverse = (index1 > index2);
            int index = index1;
            final int endIndex = index2;
            final int increment = reverse ? -1 : 1;

            @Override
            public boolean hasNext() {
                if (reverse) {
                    return index >= endIndex;
                }
                return index <= endIndex;
            }

            @Override
            public Move next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Move move = moves.get(index);
                index += increment;
                return move;
            }
        };
    }

    /**
     * Running getMoves without parameters returns an iterator of all moves in the stack.
     * @return an Iterator of all moves
     */
    public Iterator<Move> getMoves(){
        return getMoves(0,getNumberOfMoves()-1);
    }

    /**
     * Removes and returns the last move in the history.
     * @return the most recently added Move
     * @throws EmptyStackException if the history is empty
     */
    public Move removeLast() {
        return moves.pop();
    }

    /**
     * Retrieves the last move in the history without removing it.
     *
     * @return the most recently added Move
     * @throws EmptyStackException if the history is empty
     */
    public Move getLast() {
        return moves.peek();
    }
}