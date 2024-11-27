import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObstacleActionListener implements ActionListener {
    private boolean[][] verticalObstacleMatrix;
    private boolean[][] horizontalObstacleMatrix;
    private ServerConnect serverConnect;

    public ObstacleActionListener(boolean[][] verticalObstacleMatrix, boolean[][] horizontalObstacleMatrix,
                                  ServerConnect serverConnect) {
        this.verticalObstacleMatrix = verticalObstacleMatrix;
        this.horizontalObstacleMatrix = horizontalObstacleMatrix;
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

        if (clickedObstacle.isVertical()) {
            verticalObstacleMatrix[row][col + 1] = true; // 열 인덱스에 +1
        } else {
            horizontalObstacleMatrix[row + 1][col] = true; // 행 인덱스에 +1
        }

        clickedObstacle.setBackground(Color.CYAN);
        clickedObstacle.setObstacle(true);
        serverConnect.sendObstacle(verticalObstacleMatrix,horizontalObstacleMatrix);
    }
}
