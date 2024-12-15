import javax.swing.*;
import java.awt.*;

public class ErrorDisplay extends JLabel {

    public ErrorDisplay() {
        ImageIcon errorIcon = new ImageIcon("./images/no.png");
        setIcon(errorIcon);
        this.setSize(500, 500);
        this.setLocation(380, 280);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        this.setVisible(false);
    }
    public void showError(){
        this.setVisible(true);
        HideError hideError = new HideError();
        hideError.start();
    }
    private class HideError extends Thread {
        @Override
        public void run() {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setVisible(false);
        }

    }

}