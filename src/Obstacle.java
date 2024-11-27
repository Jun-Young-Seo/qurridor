import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Obstacle extends JButton {
    public Obstacle(ActionListener ObstacleActionListener){
        this.addActionListener(ObstacleActionListener);
        this.setOpaque(true);

    }
    private boolean isObstacle= false;

    public boolean isObstacle(){
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
        this.setBackground(Color.CYAN);
    }

}
