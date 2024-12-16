import javax.swing.*;
import java.awt.*;

public class WinDisplay extends JLabel {
    public WinDisplay() {
        ImageIcon errorIcon = new ImageIcon("./images/win.png");
        setIcon(errorIcon);
        this.setSize(500, 500);
        this.setLocation(330, 280);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        this.setVisible(false);
    }
    public void showWin(){
        this.setVisible(true);
    }

}