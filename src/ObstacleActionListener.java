import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObstacleActionListener implements ActionListener {
    private GameObject[][] gameBoard;
    private ServerConnect serverConnect;
    public ObstacleActionListener(GameObject[][] gameBoard, ServerConnect serverConnect) {
        this.gameBoard=gameBoard;
        this.serverConnect=serverConnect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Obstacle clickedObstacle = (Obstacle) e.getSource();

        if (clickedObstacle.isObstacle()) {
            System.out.println("이미 장애물");
            return;
        }

        int row = clickedObstacle.getRow();
        int col = clickedObstacle.getCol();

        clickedObstacle.setObstacle(true);
        serverConnect.sendObstacle(gameBoard);
    }
}
