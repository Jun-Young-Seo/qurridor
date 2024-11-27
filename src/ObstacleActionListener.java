import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObstacleActionListener implements ActionListener {
    private Object[][] gameArray; // 게임판 배열
    private int rows, cols; // 게임판 크기

//    public ObstacleActionListener(Object[][] gameArray, int rows, int cols) {
//        this.gameArray = gameArray;
//        this.rows = rows;
//        this.cols = cols;
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Obstacle clickedObstacle = (Obstacle) e.getSource();
        if (clickedObstacle.isObstacle()) {
            System.out.println("이미 장애물");
            return;
        }

        clickedObstacle.setObstacle(true); // 장애물 배치
        boolean[][] visited = new boolean[rows][cols];
        boolean playerPathExists = dfs(rows - 1, 0, visited); // 예: 플레이어가 첫 행에 도달 가능 여부
        visited = new boolean[rows][cols];
        boolean opponentPathExists = dfs(0, cols - 1, visited); // 상대가 마지막 행에 도달 가능 여부

        if (playerPathExists && opponentPathExists) {
            System.out.println("장애물 배치 성공!");
        } else {
            clickedObstacle.setObstacle(false); // 경로가 막히면 장애물 제거
            JOptionPane.showMessageDialog(null, "경로가 막혔습니다! 장애물을 놓을 수 없습니다.");
        }
    }

    private boolean dfs(int row, int col, boolean[][] visited) {
        // 1. 경계 조건
        if (row < 0 || row >= gameArray.length || col < 0 || col >= gameArray[0].length) {
            return false;
        }

        // 2. 방문 여부 확인
        if (visited[row][col]) {
            return false;
        }

        // 3. 장애물 여부 확인
        Object component = gameArray[row][col];
        if (component instanceof Obstacle) {
            Obstacle obstacle = (Obstacle) component;
            if (obstacle.isObstacle()) {
                return false; // 장애물이 있으면 탐색 종료
            }
        }

        // 4. 목표 행 도달 확인 (예: 플레이어 기준 마지막 행)
        if (row == 0) {
            return true;
        }

        // 5. 방문 처리
        visited[row][col] = true;

        // 6. 상하좌우 탐색
        return dfs(row - 1, col, visited) || // 위쪽
                dfs(row + 1, col, visited) || // 아래쪽
                dfs(row, col - 1, visited) || // 왼쪽
                dfs(row, col + 1, visited);   // 오른쪽
    }
}
