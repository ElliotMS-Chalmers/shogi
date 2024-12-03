package model;

import util.Pos;

import java.util.function.BiConsumer;

public class Sfen {
    private String boardPosition;
    private char turn;             // 'b' for Black (Sente), 'w' for White (Gote)
    private String capturedPieces;
    private int moveCount = 1;     // Optional, default to 1 if not specified

    public Sfen(String boardPosition, char turn, String capturedPieces, int moveCount) {
        this.boardPosition = boardPosition;
        this.turn = turn;
        this.capturedPieces = capturedPieces;
        this.moveCount = moveCount;
    }

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

    public String getBoardPosition() {
        return boardPosition;
    }

    public void setBoardPosition(String boardPosition) {
        this.boardPosition = boardPosition;
    }

    public char getTurn() {
        return turn;
    }

    public void setTurn(char turn) {
        this.turn = turn;
    }

    public String getCapturedPieces() {
        return capturedPieces;
    }

    public void setCapturedPieces(String capturedPieces) {
        this.capturedPieces = capturedPieces;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public String toString() {
        return boardPosition + " " + turn + " " + capturedPieces + " " + moveCount;
    }

    public String[] getRows() {
        return this.getBoardPosition().split("/");
    }

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

