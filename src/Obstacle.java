import javax.swing.*;
import java.awt.event.ActionListener;

public class Obstacle implements GameObject{
    private int row;
    private int col;
    private boolean isVertical; // true: 수직 장애물, false: 수평 장애물
    private boolean isObstacle;

    public Obstacle(int row, int col, boolean isVertical) {
        this.row = row;
        this.col = col;
        this.isVertical = isVertical;
        this.isObstacle = false; // 초기에는 장애물이 설치되어 있지 않음
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

    public boolean isVertical() { return isVertical; }
    public boolean isObstacle() { return isObstacle; }
    public void setObstacle(boolean isObstacle) { this.isObstacle = isObstacle; }
}