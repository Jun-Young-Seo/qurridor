import javax.swing.*;

public class ObstacleButton extends JButton {
    private int row;
    private int col;
    private boolean isVertical;

    public ObstacleButton(int row, int col, boolean isVertical){
        this.row=row; this.col=col; this.isVertical=isVertical;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return isVertical;
    }
}
