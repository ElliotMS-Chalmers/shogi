package model;

import com.fasterxml.jackson.annotation.JsonValue;
import util.Pos;

import java.util.function.BiConsumer;

/**
 * The `Sfen` class represents a Shogi board state in the SFEN (Shogi Forsyth-Edwards Notation) format.
 *
 * SFEN is a text representation of a Shogi game state, including the board position, whose turn it is, captured pieces, and the move count.
 */
public class Sfen {

    /**
     * A string representing the current position of pieces on the Shogi board, using SFEN notation.
     *
     * SFEN notation represents each row of the board with a combination of piece characters and digits.
     * A digit indicates the number of empty squares in that part of the row. For example, "1" means one empty square.
     * A piece character is represented by its respective Shogi piece abbreviation (e.g., 'P' for Pawn, 'L' for Lance).
     */
    private String boardPosition;

    /**
     * A character representing whose turn it is in the game.
     * 'b' for Black (Sente) and 'w' for White (Gote).
     */
    private char turn;

    /**
     * A string representing the captured pieces in the game, using SFEN notation.
     * Captured pieces are represented by their characters, with digits indicating the quantity of each type.
     * For example, "P3" means 3 captured pawns, and "R" means one captured rook.
     */
    private String capturedPieces;

    /**
     * An integer representing the move count in the game.
     * It is incremented each time a move is made. The move count starts from 1.
     */
    private int moveCount = 1;

    /**
     * Constructs a new SFEN object with the specified board position, turn, captured pieces, and move count.
     *
     * @param boardPosition the current position of the pieces on the board, represented as a SFEN string.
     * @param turn the current turn, where 'b' represents Black's turn (Sente) and 'w' represents White's turn (Gote).
     * @param capturedPieces the captured pieces, represented as a SFEN string.
     * @param moveCount the current move count in the game.
     */
    public Sfen(String boardPosition, char turn, String capturedPieces, int moveCount) {
        this.boardPosition = boardPosition;
        this.turn = turn;
        this.capturedPieces = capturedPieces;
        this.moveCount = moveCount;
    }

    /**
     * Constructs a new SFEN object from a SFEN string.
     *
     * @param sfenString a string representing the SFEN notation, consisting of the board position, turn, captured pieces,
     *                   and optionally the move count.
     * @throws IllegalArgumentException if the provided SFEN string is not in the correct format.
     */
    public Sfen(String sfenString) {
        String[] parts = sfenString.split(" ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid SFEN string format.");
        }
        this.boardPosition = parts[0];
        this.turn = parts[1].charAt(0);
        this.capturedPieces = parts[2];
        this.moveCount = parts.length == 4 ? Integer.parseInt(parts[3]) : 1;
    }

    /**
     * Returns the board position in SFEN format.
     *
     * @return the board position as a string in SFEN format.
     */
    public String getBoardPosition() {
        return boardPosition;
    }

    /**
     * Sets the board position in SFEN format.
     *
     * @param boardPosition the new board position.
     */
    public void setBoardPosition(String boardPosition) {
        this.boardPosition = boardPosition;
    }

    /**
     * Returns the current turn ('b' for Black, 'w' for White).
     *
     * @return the current turn.
     */
    public char getTurn() {
        return turn;
    }

    /**
     * Sets the current turn ('b' for Black, 'w' for White).
     *
     * @param turn the new turn character.
     */
    public void setTurn(char turn) {
        this.turn = turn;
    }

    /**
     * Returns the captured pieces in SFEN format.
     *
     * @return the captured pieces as a string.
     */
    public String getCapturedPieces() {
        return capturedPieces;
    }

    /**
     * Sets the captured pieces in SFEN format.
     *
     * @param capturedPieces the new captured pieces string.
     */
    public void setCapturedPieces(String capturedPieces) {
        this.capturedPieces = capturedPieces;
    }

    /**
     * Returns the move count.
     *
     * @return the move count.
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Sets the move count.
     *
     * @param moveCount the new move count.
     */
    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    /**
     * Returns the SFEN representation of the board position, turn, captured pieces, and move count.
     *
     * @return the SFEN string representation.
     */
    @JsonValue
    public String toString() {
        return boardPosition + " " + turn + " " + capturedPieces + " " + moveCount;
    }

    /**
     * Returns the rows of the board position as an array of strings.
     *
     * @return an array representing the rows of the board in SFEN format.
     */
    public String[] getRows() {
        return this.getBoardPosition().split("/");
    }

    /**
     * Iterates through each piece on the board and applies the provided handler to each piece's type and position.
     *
     * This method allows you to process each piece on the board, including its type and position.
     * The handler receives the piece's type (as a string) and its position as a `Pos` object.
     *
     * @param handler a `BiConsumer` that processes the piece type (as a string) and its position (`Pos`).
     */
    public void forEachPiece(BiConsumer<String, Pos> handler) {
        String[] rows = getRows();
        for (int row = 0; row < rows.length; row++) {
            int col = 0;
            for (int i = 0; i < rows[row].length(); i++) {
                char ch = rows[row].charAt(i);
                if (Character.isDigit(ch)) {
                    col += Character.getNumericValue(ch);
                } else if (ch == '+') {
                    char nextCh = rows[row].charAt(++i);
                    handler.accept("+" + nextCh, new Pos(row, col));
                    col += 1;
                } else {
                    handler.accept(Character.toString(ch), new Pos(row, col));
                    col += 1;
                }
            }
        }
    }

    /**
     * Iterates through the captured pieces and applies the provided handler to each captured piece's type and count.
     *
     * This method allows you to process each captured piece, including its type (as a character) and the count of how many
     * pieces of that type have been captured.
     *
     * @param handler a `BiConsumer` that processes the captured piece type (as a character) and its count (as an integer).
     */
    public void forEachCapturedPiece(BiConsumer<Character, Integer> handler) {
        if (capturedPieces.equals("-")) return;

        for (int i = 0; i < capturedPieces.length(); i++) {
            char ch = capturedPieces.charAt(i);
            int count = 1;
            if (Character.isDigit(ch)) {
                count = Character.getNumericValue(ch);
                char nextCh = capturedPieces.charAt(++i);
                ch = nextCh;
            }
            handler.accept(ch, count);
        }
    }
}
