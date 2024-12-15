import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObstacleActionListener implements ActionListener {
    private GameObject[][] gameBoard;
    private ServerConnect serverConnect;
    private String userId;
    private boolean myTurn;
    public ObstacleActionListener(GameObject[][] gameBoard, ServerConnect serverConnect, String userId) {
        this.gameBoard = gameBoard;
        this.serverConnect = serverConnect;
        this.userId=userId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!myTurn){
            System.out.println("not turn "+userId);
            return;
        }
        System.out.println("click");
        ObstacleButton clickedObstacleBtn = (ObstacleButton) e.getSource();

        int row = clickedObstacleBtn.getRow();
        int col = clickedObstacleBtn.getCol();
        System.out.println(row+" , "+col);
        if (gameBoard[row][col] instanceof Obstacle) {
            Obstacle obstacle = (Obstacle) gameBoard[row][col];
            System.out.println(obstacle.getIsVertical()?"V":"H");
            // 이미 장애물이 설정된 경우 처리
            if (obstacle.getIsObstacle()) {
                System.out.println("이미 장애물이 배치된 위치입니다.");
                return;
            }
            System.out.println(userId);
            obstacle.setObstacle(true);

        }
        serverConnect.sendObstacle(gameBoard);

    }

    public void setGameBoard(GameObject[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public boolean isMyTurn() {
        return myTurn;
    }
    public void setMyTurn(boolean myTurn){
        this.myTurn=myTurn;
    }
}
