import javax.swing.*;

//사용자가 움직일 수 있는 칸
public class Block implements GameObject {
    private int row;
    private int col;
    private String userId;
    private int blockStatus; //-1이면 상대 0이면 빈칸 1이면 나
    public Block(int row, int col){
        this.row=row; this.col=col; this.blockStatus=0; userId="";
    }

    public int getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(int blockStatus) {
        this.blockStatus = blockStatus;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public String getType() {
        return "BLOCK";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
