package pieces;

public class GoldGeneral extends Promotable{
    private int[][] moves = {{-1,1}, {0,1}, {1,1}, {-1,0},{1,0},{0,-1}};
    public GoldGeneral(boolean team){
        this.team = team;
    }
    public void Promote(){}
}
