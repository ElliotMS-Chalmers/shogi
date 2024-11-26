package util;

public class Sfen {
    private String boardPosition;
    private char turn;             // 'b' for Black, 'w' for White
    private String capturedPieces; // Captured pieces in SFEN format
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
}

