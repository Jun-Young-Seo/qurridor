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
    private boolean[][] verticalObstacleMatrix;   // 세로 장애물 배열
    private boolean[][] horizontalObstacleMatrix; // 가로 장애물 배열

    public QurridorGameController(JPanel gamePanel, JPanel gameArea, int[][] placeMatrix, boolean[][] v, boolean[][]h,
                                  ServerConnect serverConnect, MessageQueue qurridorMessageQueue, boolean isFirst) {
        this.gamePanel = gamePanel;
        this.placeMatrix = placeMatrix;
        this.gameArea = gameArea;
        this.serverConnect=serverConnect;
        this.verticalObstacleMatrix=v;
        this.horizontalObstacleMatrix=h;
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
        //맵 밖 체크
        if (toRow < 0 || toRow >= rows || toCol < 0 || toCol >= cols) {
            System.out.println("화면 밖!");
            return false;
        }


        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        // 장애물 체크
        if (rowDiff == -1) { // 위로 이동
            System.out.println("UP");
            if (horizontalObstacleMatrix[toRow + 1][fromCol]) {
                System.out.println(toRow+1+ ", "+fromCol);
                System.out.println("위쪽 장애물!");
                return false;
            }
        } else if (rowDiff == 1 ) { // 아래로 이동
            System.out.println("DOWN");
            if (horizontalObstacleMatrix[fromRow + 1][fromCol]) {
                System.out.print(fromRow+1+", "+fromCol);
                System.out.println("아래쪽 장애물!");
                return false;
            }
        } else if (colDiff == -1) { // 왼쪽으로 이동
            System.out.println("LEFT");
            if (verticalObstacleMatrix[fromRow][toCol + 1]) {
                System.out.println("왼쪽 장애물!");
                return false;
            }
        } else if (colDiff == 1) { // 오른쪽으로 이동
            System.out.println("RIGHT");
            if (verticalObstacleMatrix[fromRow][fromCol + 1]) {
                System.out.println("오른쪽 장애물!");
                return false;
            }
        }

        return true;
    }

    public void setObstacle(boolean[][] verticalObstacleMatrix, boolean[][] horizontalObstacleMatrix){
        this.verticalObstacleMatrix = verticalObstacleMatrix;
        this.horizontalObstacleMatrix = horizontalObstacleMatrix;

        // 게임 보드의 컴포넌트들을 순회하여 Obstacle 버튼들을 찾습니다.
        Component[] components = gameArea.getComponents();
        for (Component comp : components) {
            if (comp instanceof Obstacle) {
                Obstacle obstacle = (Obstacle) comp;
                int row = obstacle.getRow();
                int col = obstacle.getCol();
                boolean isVertical = obstacle.isVertical();
                boolean isObstacleSet;

                if (isVertical) {
                    // 수직 장애물 배열에서 해당 위치의 값 가져오기
                    isObstacleSet = verticalObstacleMatrix[row][col + 1];
                } else {
                    // 수평 장애물 배열에서 해당 위치의 값 가져오기
                    isObstacleSet = horizontalObstacleMatrix[row + 1][col];
                }

                // Obstacle의 상태와 UI 업데이트
                obstacle.setObstacle(isObstacleSet);
                if (isObstacleSet) {
                    obstacle.setBackground(Color.CYAN); // 장애물이 설치된 경우
                } else {
                    obstacle.setBackground(Color.LIGHT_GRAY); // 장애물이 없는 경우
                }
            }
        }

        gameArea.repaint();
    }


    // KeyListener 메서드 구현
    @Override
    public void keyPressed(KeyEvent e) {
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
//                movePiece(currentRow,currentRow-1, currentCol,currentCol); // 위로 이동
                serverConnect.sendMove(currentRow,currentCol, currentRow-1,currentCol);
                break;
            case KeyEvent.VK_DOWN:
//                movePiece(currentRow,currentRow+1, currentCol,currentCol); // 아래로 이동
                serverConnect.sendMove(currentRow,currentCol, currentRow+1,currentCol);
                break;
            case KeyEvent.VK_LEFT:
//                movePiece(currentRow,currentRow,currentCol,currentCol-1); // 왼쪽으로 이동
                serverConnect.sendMove(currentRow,currentCol,currentRow,currentCol-1);
                break;
            case KeyEvent.VK_RIGHT:
//                movePiece(currentRow,currentRow,currentCol,currentCol+1); // 오른쪽으로 이동
                serverConnect.sendMove(currentRow,currentCol,currentRow,currentCol+1);
                break;
        }
    }
    public void setMyTurn(boolean isMyTurn){
        this.isMyTurn=isMyTurn;
    }
}
