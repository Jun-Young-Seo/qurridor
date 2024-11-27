import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class QurridorGameController extends KeyAdapter {
    private JPanel gamePanel; // 게임 보드 패널
    private JPanel gameArea; // 게임 영역 패널
    private JComponent[][] gameMatrix; // 게임판 관리 매트릭스
    private JLabel player; // 플레이어 UI
    private ServerConnect serverConnect; // 서버 통신 객체
    private MessageQueue qurridorMessageQueue; // 메시지 큐
    private boolean isMyTurn; // 현재 턴 여부

    public QurridorGameController(JPanel gamePanel, JPanel gameArea, JComponent[][] gameMatrix,
                                  ServerConnect serverConnect, MessageQueue qurridorMessageQueue, boolean isFirst) {
        this.gamePanel = gamePanel;
        this.gameArea = gameArea;
        this.gameMatrix = gameMatrix;
        this.serverConnect = serverConnect;
        this.qurridorMessageQueue = qurridorMessageQueue;
        this.isMyTurn = isFirst;

        // 플레이어 초기화
        playerInit();

        // 키보드 입력 이벤트 추가
        gameArea.setFocusable(true); // 키 입력 받을 수 있도록 설정
        gameArea.addKeyListener(this);
    }

    // 말 초기화
    public void playerInit() {
        // 말의 초기 위치 (아래 행, 가운데 열)
        int startRow = gameMatrix.length - 1; // 마지막 행
        int startCol = (gameMatrix[0].length - 1) / 2; // 중앙 열

        // 말 UI 생성
        player = new JLabel();
        player.setOpaque(true);
        player.setBackground(Color.GREEN); // 말의 색상
        player.setSize(60, 60); // 말의 크기
        player.setLocation(startCol * 35, startRow * 35); // 위치 설정

        // gameMatrix에 말 위치 등록
        gameMatrix[startRow][startCol] = player;

        gameArea.add(player);
        gameArea.setComponentZOrder(player, 0);
        gameArea.repaint();
    }

    // 말을 이동시키는 메서드
    public boolean movePiece(int fromRow, int toRow, int fromCol, int toCol) {
        // 이동 가능한지 검증
        if (!isValidMove(fromRow, toRow, fromCol, toCol)) {
            System.out.println("Invalid move");
            return false;
        }

        // 기존 위치 초기화
        gameMatrix[fromRow][fromCol] = null;

        // 새로운 위치 설정
        gameMatrix[toRow][toCol] = player;

        // 말 UI 업데이트
        player.setLocation(toCol * 35, toRow * 35);
        gamePanel.repaint();

        return true;
    }

    // 이동 가능한지 검증하는 메서드
    private boolean isValidMove(int fromRow, int toRow, int fromCol, int toCol) {
        // 1. 범위 검사
        if (toRow < 0 || toRow >= gameMatrix.length || toCol < 0 || toCol >= gameMatrix[0].length) {
            System.out.println("Out of bounds");
            return false;
        }

        // 2. 목표 위치가 블록인지 확인
        if (!(gameMatrix[toRow][toCol] instanceof Block)) {
            System.out.println("Target is not a block");
            return false;
        }

        // 3. 이동 방향 확인
        if (fromRow == toRow && Math.abs(fromCol - toCol) == 2) {
            // 수평 이동
            int midCol = (fromCol + toCol) / 2; // 장애물 위치
            if (gameMatrix[fromRow][midCol] instanceof Obstacle) {
                Obstacle obstacle = (Obstacle) gameMatrix[fromRow][midCol];
                if (obstacle.isObstacle()) {
                    System.out.println("Horizontal move blocked by obstacle");
                    return false;
                }
            }
        } else if (fromCol == toCol && Math.abs(fromRow - toRow) == 2) {
            // 수직 이동
            int midRow = (fromRow + toRow) / 2; // 장애물 위치
            if (gameMatrix[midRow][fromCol] instanceof Obstacle) {
                Obstacle obstacle = (Obstacle) gameMatrix[midRow][fromCol];
                if (obstacle.isObstacle()) {
                    System.out.println("Vertical move blocked by obstacle");
                    return false;
                }
            }
        } else {
            // 대각선 또는 두 칸 이상 이동 불가
            System.out.println("Diagonal or too far move not allowed");
            return false;
        }

        return true;
    }

    // KeyListener 메서드 구현
    @Override
    public void keyPressed(KeyEvent e) {
        if (!isMyTurn) {
            System.out.println("Not your turn");
            return;
        }

        // 현재 위치 찾기
        int currentRow = -1, currentCol = -1;
        for (int i = 0; i < gameMatrix.length; i++) {
            for (int j = 0; j < gameMatrix[0].length; j++) {
                if (gameMatrix[i][j] == player) {
                    currentRow = i;
                    currentCol = j;
                    break;
                }
            }
            if (currentRow != -1) break;
        }

        if (currentRow == -1 || currentCol == -1) {
            System.out.println("Player position not found");
            return;
        }

        // 방향키 입력 처리
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                serverConnect.sendMove(currentRow, currentCol, currentRow - 2, currentCol);
                break;
            case KeyEvent.VK_DOWN:
                serverConnect.sendMove(currentRow, currentCol, currentRow + 2, currentCol);
                break;
            case KeyEvent.VK_LEFT:
                serverConnect.sendMove(currentRow, currentCol, currentRow, currentCol - 2);
                break;
            case KeyEvent.VK_RIGHT:
                serverConnect.sendMove(currentRow, currentCol, currentRow, currentCol + 2);
                break;
        }
    }

    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }
}
