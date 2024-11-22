import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class QurridorGameController extends KeyAdapter {
    private JPanel gamePanel; // 게임 보드 패널
    private JPanel gameArea;
    private int[][] placeMatrix; // 말의 위치를 관리하는 배열
    private JLabel player; // 말 UI
    private ServerConnect serverConnect;
    private MessageQueue qurridorMessageQueue;
    private int rows;
    private int cols;
    private boolean isMyTurn;
    public QurridorGameController(JPanel gamePanel, JPanel gameArea, int[][] placeMatrix,
                                  ServerConnect serverConnect, MessageQueue qurridorMessageQueue, boolean isFirst) {
        this.gamePanel = gamePanel;
        this.placeMatrix = placeMatrix;
        this.gameArea = gameArea;
        this.serverConnect=serverConnect;
        this.qurridorMessageQueue=qurridorMessageQueue;
        this.isMyTurn = isFirst;
        rows=placeMatrix.length;
        cols=placeMatrix[0].length;
        playerInit();

        // 키보드 입력 이벤트 추가
        gameArea.setFocusable(true); // 키 입력 받을 수 있도록 설정
        gameArea.addKeyListener(this);
    }

    // 말 초기화
    public void playerInit() {
        // 말의 초기 위치 (아래 행, 가운데 열)
        int startRow = rows - 1;
        int startCol = cols / 2;

        // placeMatrix에 위치 표시
        placeMatrix[startRow][startCol] = 1;

        // 말 UI 생성
        player = new JLabel();
        player.setOpaque(true);
        player.setBackground(Color.GREEN); // 말의 색상
        player.setSize(60, 60); // 말의 크기 (블록 크기와 동일)
        player.setLocation(startCol * 70, startRow * 70); // 위치 설정 (블록과 장애물 크기 반영)

        gameArea.add(player);
        gameArea.setComponentZOrder(player, 0);

        gameArea.repaint();
    }

    // 말을 이동시키는 메서드
    public boolean movePiece(int fromRow, int toRow, int fromCol, int toCol) {
        // 이동 가능한지 검증
        if (!isValidMove(fromRow,toRow,fromCol, toCol)) {
            System.out.println("cant move there");
            return false;
        }

        // 이전 위치 초기화
        placeMatrix[fromRow][fromCol] = 0;

        // 새로운 위치 설정
        placeMatrix[toRow][toCol] = 1;

        // 말 UI 업데이트
        player.setLocation(toCol * 70, toRow * 70); // 블록 + 장애물 크기 반영
        gamePanel.repaint();

        return true;
    }

    // 이동 가능한지 검증하는 메서드
    private boolean isValidMove(int fromRow, int toRow, int fromCol, int toCol) {
        // 범위 밖이면 이동 불가
        if (toRow < 0 || toRow >= rows || toCol < 0 || toCol >= cols) {
            System.out.println("화면 밖!");
            return false;
        }

        // 다른사람 있으면 이동 불가
        if (placeMatrix[toRow][toCol] == 1) {
            System.out.println("상대 있음!");
            return false;
        }


        if (Math.abs(fromRow - toRow) > 1 || Math.abs(fromCol - toCol) > 1) {
            return false;
        }

        return true;
    }

    // KeyListener 메서드 구현
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e);
        System.out.println("trun : "+isMyTurn);
        if(!isMyTurn){
            System.out.println("not my turn!!");
            return;
        }
        // 현재 위치 찾기
        int currentRow = -1;
        int currentCol = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (placeMatrix[i][j] == 1) {
                    currentRow = i;
                    currentCol = j;
                    break;
                }
            }
        }

        // 방향키 입력 처리
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
//                movePiece(currentRow - 1, currentCol); // 위로 이동
                serverConnect.sendMove(currentRow,currentCol, currentRow-1,currentCol);
                break;
            case KeyEvent.VK_DOWN:
//                movePiece(currentRow + 1, currentCol); // 아래로 이동
                serverConnect.sendMove(currentRow,currentCol, currentRow+1,currentCol);
                break;
            case KeyEvent.VK_LEFT:
//                movePiece(currentRow, currentCol - 1); // 왼쪽으로 이동
                serverConnect.sendMove(currentRow,currentCol,currentRow,currentCol-1);
                break;
            case KeyEvent.VK_RIGHT:
//                movePiece(currentRow, currentCol + 1); // 오른쪽으로 이동
                serverConnect.sendMove(currentRow,currentCol,currentRow,currentCol+1);
                break;
        }
    }
    public void setMyTurn(boolean isMyTurn){
        this.isMyTurn=isMyTurn;
    }
}
