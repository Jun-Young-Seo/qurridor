import javax.swing.*;

public class Block extends JLabel {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public Block(boolean up, boolean down, boolean left, boolean right){
        this.up=up; this.down=down; this.left=left; this.right=right;
    }
}
