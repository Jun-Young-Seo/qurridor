import javax.swing.*;
import java.awt.event.ActionListener;

public class Obstacle implements GameObject{
    private int row;
    private int col;
    private boolean isVertical; // true: 수직 장애물, false: 수평 장애물
    private boolean isObstacle;
    private String userId;
    public Obstacle(int row, int col, boolean isVertical) {
        this.row = row;
        this.col = col;
        this.isVertical = isVertical;
        this.isObstacle = false;
        this.userId="";
    }

    // Getter 메서드 추가
    @Override
    public int getRow() { return row; }
    @Override
    public int getCol() { return col; }

    @Override
    public String getType() {
        return "OBSTACLE";
    }

    public boolean getIsVertical() { return isVertical; }
    public boolean getIsObstacle() { return isObstacle; }
    public void setObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
