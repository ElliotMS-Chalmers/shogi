package pieces;

public class SilverGeneral extends Promotable{
    private int[][] moves = {{-1,1}, {0,1}, {1,1},{-1,-1},{1,-1}};
    public SilverGeneral(boolean team){
        this.team = team;
    }
    public void Promote(){}
}
