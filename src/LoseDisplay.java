import javax.swing.*;
import java.awt.*;

public class LoseDisplay extends JLabel {
    public LoseDisplay() {
        ImageIcon loseIcon = new ImageIcon("./images/lose.png");
        setIcon(loseIcon);
        this.setSize(500, 500);
        this.setLocation(380, 280);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        this.setVisible(false);
    }
    public void showLose(){
        this.setVisible(true);
    }

}