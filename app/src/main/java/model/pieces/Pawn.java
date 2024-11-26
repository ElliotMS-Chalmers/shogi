package pieces;

public class Pawn extends Promotable{
    private int[][] moves = {{0,1}, {0,-1}};
    public Pawn(boolean team){
        this.team = team;
    }
    public void Promote(){}
}
