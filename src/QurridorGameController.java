import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class QurridorGameController extends KeyAdapter {
    private JPanel gamePanel;
    private JPanel gameArea;
    private ServerConnect serverConnect;
    private GameObject[][] gameBoard;
    private boolean isMyTurn;
    private String userId;
    private QurridorUI qurridorUI;

    // 내 현재 위치
    private int nowRow;
    private int nowCol;

    public QurridorGameController(JPanel gamePanel, JPanel gameArea, GameObject[][] gameBoard,
                                  ServerConnect serverConnect, String userId, QurridorUI qurridorUI) {
        this.gamePanel = gamePanel;
        this.gameArea = gameArea;
        this.serverConnect = serverConnect;
        this.gameBoard = gameBoard;
        this.userId = userId;
        this.qurridorUI = qurridorUI;

        // 초기화는 별도 메서드에서 처리
        this.isMyTurn = false; // 기본값
        this.nowRow = -1; // 초기화 전 위치 미정
        this.nowCol = -1; // 초기화 전 위치 미정

        gameArea.setFocusable(true);
        gameArea.addKeyListener(this);
    }

    // 게임 시작 메서드
    public void startGame(boolean isFirst, String firstPlayerId, String secondPlayerId) {
        this.isMyTurn = isFirst;

        // 플레이어의 시작 위치 설정
        if (isFirst) {
            nowRow = gameBoard.length - 1; // 맨 아래
        } else {
            nowRow = 0; // 맨 위
        }
        nowCol = gameBoard[0].length / 2; // 중앙

        // 내 말 초기 상태 설정
        if (gameBoard[nowRow][nowCol] instanceof Block) {
            ((Block) gameBoard[nowRow][nowCol]).setUserId(firstPlayerId); // 내 ID 설정
        }

        // 상대방 말 초기 상태 설정
        int opponentRow = isFirst ? 0 : gameBoard.length - 1;
        int opponentCol = gameBoard[0].length / 2;
        if (gameBoard[opponentRow][opponentCol] instanceof Block) {
            ((Block) gameBoard[opponentRow][opponentCol]).setUserId(secondPlayerId); // 상대방 ID 설정
        }

        // 게임 초기 상태 렌더링
        qurridorUI.renderGameArea(gameBoard);

        // 턴 정보 출력
        if (isMyTurn) {
            System.out.println("내가 첫 번째 플레이어입니다.");
        } else {
            System.out.println("상대가 첫 번째 플레이어입니다.");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isMyTurn) {
            System.out.println("현재 차례가 아닙니다.");
            return;
        }

        String direction = null;
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
            direction = "UP";
        } else if (keyCode == KeyEvent.VK_DOWN) {
            direction = "DOWN";
        } else if (keyCode == KeyEvent.VK_LEFT) {
            direction = "LEFT";
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            direction = "RIGHT";
        }

        if (direction != null && canMove(direction)) {
            movePlayer(direction);
            sendUpdatedGameBoard();
            isMyTurn = false;
        } else if (direction != null) {
            System.out.println("이동할 수 없습니다.");
        }
    }

    private boolean canMove(String direction) {
        int newRow = nowRow;
        int newCol = nowCol;

        if (direction.equals("UP")) {
            newRow -= 2;
        } else if (direction.equals("DOWN")) {
            newRow += 2;
        } else if (direction.equals("LEFT")) {
            newCol -= 2;
        } else if (direction.equals("RIGHT")) {
            newCol += 2;
        }

        if (!isInRange(newRow, newCol)) {
            return false; // 맵 범위를 벗어남
        }

        if (!(gameBoard[newRow][newCol] instanceof Block)) {
            return false; // 유효하지 않은 위치
        }

        // 상대방 말이 있는 위치로 이동할 수 없음
        Block targetBlock = (Block) gameBoard[newRow][newCol];
        if (!targetBlock.getUserId().isEmpty()) {
            System.out.println("상대방의 말이 있는 위치로 이동할 수 없습니다.");
            return false;
        }

        return true;
    }

    private void movePlayer(String direction) {
        int newRow = nowRow;
        int newCol = nowCol;

        if (direction.equals("UP")) {
            newRow -= 2;
        } else if (direction.equals("DOWN")) {
            newRow += 2;
        } else if (direction.equals("LEFT")) {
            newCol -= 2;
        } else if (direction.equals("RIGHT")) {
            newCol += 2;
        }

        // 이전 위치 초기화
        if (gameBoard[nowRow][nowCol] instanceof Block) {
            ((Block) gameBoard[nowRow][nowCol]).setUserId("");
        }

        // 새 위치 설정
        if (gameBoard[newRow][newCol] instanceof Block) {
            ((Block) gameBoard[newRow][newCol]).setUserId(userId);
        }

        nowRow = newRow;
        nowCol = newCol;
    }

    private void sendUpdatedGameBoard() {
        serverConnect.sendMove(gameBoard);
    }

    public void updateGameBoardFromServer(GameObject[][] updatedBoard) {
        this.gameBoard = updatedBoard;
        qurridorUI.renderGameArea(gameBoard);
    }

    private boolean isInRange(int r, int c) {
        return r >= 0 && r < gameBoard.length && c >= 0 && c < gameBoard[0].length;
    }

    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }
}
