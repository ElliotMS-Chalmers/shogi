package pieces;

public class King extends Piece{
    private int[][] moves = {{-1,1}, {0,1}, {1,1}, {-1,0},{1,0},{-1,-1},{0,-1},{1,-1}};
    public King(boolean team){
        this.team = team;
    }
}
