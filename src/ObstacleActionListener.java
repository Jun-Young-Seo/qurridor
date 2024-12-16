import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ObstacleActionListener implements ActionListener {
    private GameObject[][] gameBoard;
    private ServerConnect serverConnect;
    private String userId;
    private boolean myTurn;
    private String firstUserId; // Player1의 ID
    private String secondUserId; // Player2의 ID

    public ObstacleActionListener(GameObject[][] gameBoard, ServerConnect serverConnect, String userId, String firstUserId, String secondUserId) {
        this.gameBoard = gameBoard;
        this.serverConnect = serverConnect;
        this.userId=userId;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
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
            // 이미 장애물이 설정된 경우 처리
            if (obstacle.getIsObstacle()) {
                System.out.println("이미 장애물이 배치된 위치입니다.");
                return;
            }
            obstacle.setObstacle(true); // 장애물 설치
            boolean canPlace = canPlaceObstacle(row, col);
            obstacle.setObstacle(false);

            // 장애물 설치 가능 여부 확인
            if (canPlace) {
                obstacle.setObstacle(true);
                System.out.println("장애물이 성공적으로 배치되었습니다.");
                serverConnect.sendObstacle(gameBoard); // 서버에 장애물 정보 전송
            } else {
                System.out.println("장애물을 배치할 수 없는 위치입니다.");
                return;
            }
            System.out.println(userId);
        }
    }
    public void setGameBoard(GameObject[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
    // 장애물 설치 가능 여부 확인
    private boolean canPlaceObstacle(int row, int col) {
        boolean blocksPath = doesBlockPlayerPath(row, col); // 경로 차단 여부 확인
        return !blocksPath; // 경로가 차단되지 않으면 설치 가능
    }

    // 장애물이 플레이어의 경로를 차단하는지 확인
    private boolean doesBlockPlayerPath(int row, int col) {
        // Player1 경로 확인
        Point player1Start = findPlayerStartPosition(gameBoard, firstUserId);
        if (player1Start == null || !isPathAvailable(player1Start, true)) {
            System.out.println("Player1이 목표에 도달할 수 없습니다.");
            return true;
        }

        // Player2 경로 확인
        Point player2Start = findPlayerStartPosition(gameBoard, secondUserId);
        if (player2Start == null || !isPathAvailable(player2Start, false)) {
            System.out.println("Player2가 목표에 도달할 수 없습니다.");
            return true;
        }

        return false; // 두 플레이어 모두 경로 유지
    }

    // 특정 플레이어가 목표에 도달 가능한지 확인하는 알고리즘
    private boolean isPathAvailable(Point start, boolean isFirstPlayer) {
        java.util.Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            // 목표 위치 확인
            if (isFirstPlayer && current.x == 0) { // 첫 번째 플레이어: 맨 위 행
                System.out.println("Player1이 목표에 도달 가능");
                return true;
            }
            if (!isFirstPlayer && current.x == gameBoard.length - 1) { // 두 번째 플레이어: 맨 아래 행
                System.out.println("Player2가 목표에 도달 가능");
                return true;
            }

            // 다음 탐색 위치 추가
            for (Point neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return false;
    }

    // BFS 탐색에서 사용할 이웃 위치 반환
    private java.util.List<Point> getNeighbors(Point current) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}}; // 상하좌우 2칸씩 이동

        for (int[] dir : directions) {
            int newRow = current.x + dir[0];
            int newCol = current.y + dir[1];

            if (newRow >= 0 && newRow < gameBoard.length && newCol >= 0 && newCol < gameBoard[0].length) {
                if (isValidMove(current, newRow, newCol)) {
                    neighbors.add(new Point(newRow, newCol));
                }
            }
        }
        return neighbors;
    }

    // 이동 가능한 위치인지 확인
    private boolean isValidMove(Point current, int newRow, int newCol) {
        int midRow = (current.x + newRow) / 2;
        int midCol = (current.y + newCol) / 2;

        // 중간에 장애물이 있는지 확인
        if (gameBoard[midRow][midCol] instanceof Obstacle) {
            Obstacle obstacle = (Obstacle) gameBoard[midRow][midCol];
            if (obstacle.getIsObstacle()) {
                return false;
            }
        }

        // 이동하려는 위치가 블록인지 확인
        return gameBoard[newRow][newCol] instanceof Block;
    }

    // 플레이어의 시작 위치를 찾는 메서드
    private Point findPlayerStartPosition(GameObject[][] gameBoard, String userId) {
        //System.out.println("findPlayerStartPosition 호출: userId = " + userId);
        for (int r = 0; r < gameBoard.length; r++) {
            for (int c = 0; c < gameBoard[0].length; c++) {
                if (gameBoard[r][c] instanceof Block) {
                    Block block = (Block) gameBoard[r][c];
                    //System.out.println("검사 중: (" + r + ", " + c + ") userId = " + block.getUserId());
                    if (userId.equals(block.getUserId())) {
                        System.out.println("Player 위치 발견: (" + r + ", " + c + ")");
                        return new Point(r, c);
                    }
                }
            }
        }
        System.out.println("Player 시작 위치를 찾지 못했습니다.");
        return null;
    }
    public boolean isMyTurn() {
        return myTurn;
    }
    public void setMyTurn(boolean myTurn){
        this.myTurn=myTurn;
    }
}
