package model.game;

import model.Move;
import model.Sfen;
import model.pieces.Piece;
import model.pieces.PieceFactory;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Represents a game board for managing and interacting with pieces during a game.
 */
public class Board {
    /**
     * A 2D array representing the board grid where each cell may contain a piece or be empty.
     */
    private Piece[][] grid;

    /**
     * Constructs a board with the specified dimensions.
     *
     * @param width  the width of the board
     * @param height the height of the board
     */
    public Board(int width, int height) {
        grid = new Piece[height][width];
    }

    /**
     * Moves a piece from one position to another.
     * Updates the board and returns a Move object representing the move.
     *
     * @param from the starting position of the piece
     * @param to   the target position of the piece
     * @return a Move object representing the performed move
     */
    public Move move(Pos from, Pos to) {
        Move move = new Move(from, to, getPieceAt(from), getPieceAt(to));
        setAtPosition(to, grid[from.row()][from.col()]);
        setAtPosition(from, null);
        return move;
    }

    /**
     * Simulates a move on the board by moving a piece from one position to another.
     *
     * @param from The starting position of the piece.
     * @param to The target position of the piece.
     * @param piece The piece to place back at the starting position after the move.
     * @return The piece that was captured at the target position, or {@code null} if no piece was present.
     */
    public Piece testMove(Pos from, Pos to, Piece piece) {
        Piece capturedPiece = getPieceAt(to);
        setAtPosition(to, grid[from.row()][from.col()]);
        setAtPosition(from, piece);
        return capturedPiece;
    }


    /**
     * Sets a piece at a specific position on the board.
     *
     * @param pos   the position on the board
     * @param piece the piece to place at the specified position
     */
    public void setAtPosition(Pos pos, Piece piece){
        grid[pos.row()][pos.col()] = piece;
    }

    /**
     * Sets the board state based on the provided SFEN string.
     *
     * @param sfen the SFEN string representing the board state
     */
    public void setSfen(Sfen sfen) {
        grid = new Piece[grid.length][grid[0].length];
        initializeBoard(sfen);
    }

    /**
     * Converts the board state to an SFEN string representation.
     *
     * @return a string representing the current board state in SFEN format
     */
    public String getBoardAsSfen() {
        StringBuilder sfen = new StringBuilder();

        for (Piece[] row : grid) {
            int emptyCount = 0;
            for (Piece piece : row) {
                if (piece != null) {
                    if (emptyCount > 0) {
                        sfen.append(emptyCount);
                        emptyCount = 0;
                    }
                    sfen.append(piece.getSfenAbbreviation());
                } else {
                    emptyCount++;
                }
            }

            if (emptyCount > 0) {
                sfen.append(emptyCount);
            }

            sfen.append("/");
        }

        if (!sfen.isEmpty()) {
            sfen.setLength(sfen.length() - 1);
        }
        return sfen.toString();
    }

    /**
     * Initializes the board using the provided SFEN string.
     *
     * @param sfen the SFEN string representing the board state
     */
    public void initializeBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            Piece piece = PieceFactory.fromSfenAbbreviation(abbr);
            setAtPosition(pos, piece);
        });
    }

    /**
     * Retrieves the piece at a specific position on the board.
     *
     * @param pos the position to query
     * @return the piece at the specified position, or null if the position is empty
     */
    public Piece getPieceAt(Pos pos) {
        return grid[pos.row()][pos.col()];
    }

    /**
     * Gets all pieces currently on the board.
     *
     * @return a list of all pieces on the board
     */
    public ArrayList<Piece> getEveryPiece() {
        Piece piece;
        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                piece = getPieceAt(new Pos(i, j));
                if (piece != null) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    /**
     * Gets the positions of all pieces currently on the board.
     *
     * @return a list of positions of all pieces on the board
     */
    public ArrayList<Pos> getEveryPiecePos() {
        Piece piece;
        ArrayList<Pos> positions = new ArrayList<>();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                piece = getPieceAt(new Pos(i, j));
                if (piece != null) {
                    positions.add(new Pos(i, j));
                }
            }
        }
        return positions;
    }

    /**
     * Finds the position of a specific piece on the board.
     *
     * @param side      the side to which the piece belongs
     * @param pieceType the class of the piece to find
     * @return the position of the specified piece, or null if not found
     */
    public Pos getPiecePos(Side side, Class pieceType) {
        Piece piece;
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                piece = getPieceAt(new Pos(i, j));
                if (piece != null) {
                    if (piece.getClass() == pieceType && piece.getSide() == side) {
                        return new Pos(i, j);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the width of the board.
     * The width is determined by the number of columns in the grid.
     *
     * @return the number of columns in the board (width).
     */
    public int getWidth() {
        return grid.length;
    }

    /**
     * Returns the height of the board.
     * The height is determined by the number of rows in the grid.
     *
     * @return the number of rows in the board (height).
     */
    public int getHeight() {
        return grid[0].length;
    }


    /**
     * Checks if a specific type of piece belonging to a specified side exists in a given column on the board.
     *
     * @param side The side (SENTE or GOTE) to which the piece belongs.
     * @param pieceType The class type of the piece to search for.
     * @param col The column index to search in (0-based).
     * @return {@code true} if a piece of the specified type and side exists in the given column, otherwise {@code false}.
     */
    public boolean ifPieceInColum(Side side, Class pieceType, int col) {
        Piece piece;

        for (int i = 0; i < getWidth(); i++) {
            piece = getPieceAt(new Pos(i, col));
            if (piece != null) {
                if (piece.getClass() == pieceType && piece.getSide() == side) {
                    return true;
                }
            }
        }
        return false;
    }
}
