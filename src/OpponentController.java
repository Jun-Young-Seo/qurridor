import javax.swing.*;
import java.awt.*;

public class OpponentController {
    private int[][] opponentMatrix; // 상대방의 위치를 관리하는 배열
    private JLabel opponentPiece; // 상대방 말 UI
    private JPanel gameArea; // 게임 보드 패널
    private int rows;
    private int cols;
    private boolean [][] preVMatrix;
    private boolean [][] preHMatrix;
    public OpponentController(JPanel gameArea, int[][] opponentMatrix) {
        this.gameArea = gameArea;
        this.opponentMatrix = opponentMatrix;
        this.rows = opponentMatrix.length;
        this.cols = opponentMatrix[0].length;
        this.preVMatrix= new boolean[rows][cols+1];
        this.preHMatrix = new boolean[rows+1][cols];
        // 수직 장애물 초기화 (첫 번째 및 마지막 열을 true로 설정)
        for (int i = 0; i < rows; i++) {
            preVMatrix[i][0] = true; // 첫 번째 열
            preVMatrix[i][cols] = true; // 마지막 열
        }

        // 수평 장애물 초기화 (첫 번째 및 마지막 행을 true로 설정)
        for (int j = 0; j < cols; j++) {
            preHMatrix[0][j] = true; // 첫 번째 행
            preHMatrix[rows][j] = true; // 마지막 행
        }

        initializeOpponent();
    }

    // 상대방 말 초기화
    public void initializeOpponent() {
        int startRow = 0; // 초기 위치 (내 화면 기준 맨 아래)
        int startCol = cols / 2;

        opponentMatrix[startRow][startCol] = 1;

        opponentPiece = new JLabel();
        opponentPiece.setOpaque(true);
        opponentPiece.setBackground(Color.RED);
        opponentPiece.setSize(60, 60);
        opponentPiece.setLocation(startCol * 70, startRow * 70);

        gameArea.add(opponentPiece);
        gameArea.setComponentZOrder(opponentPiece, 0);
        gameArea.repaint();
    }
    public void setOpponentObstacle(boolean[][] verticalObstacleMatrix, boolean[][] horizontalObstacleMatrix){
        int[] difV = findDiffrenceV(preVMatrix, verticalObstacleMatrix);
        int[] difH = findDiffrenceH(preHMatrix, horizontalObstacleMatrix);

        // 변경된 장애물 UI 업데이트
        updateObstacleUI(difV[0], difV[1], true); // 수직 장애물
        updateObstacleUI(difH[0], difH[1], false); // 수평 장애물
        copyMatrix(verticalObstacleMatrix, preVMatrix);
        copyMatrix(horizontalObstacleMatrix, preHMatrix);

        gameArea.repaint();
    }
    private void updateObstacleUI(int row, int col, boolean isVertical) {
        // 좌표 미러링
        int mirroredRow = mirrorRow(row);
        int mirroredCol = mirrorCol(col);
        Component[] components = gameArea.getComponents();
        for (Component comp : components) {
            if (comp instanceof Obstacle) {
                Obstacle obstacle = (Obstacle) comp;
                // 특정 장애물만 찾아서 업데이트
                if (obstacle.getRow() == mirroredRow &&
                        obstacle.getCol() == mirroredCol &&
                        obstacle.isVertical() == isVertical) {

                    obstacle.setObstacle(true); // 장애물 활성화
                    obstacle.setBackground(Color.RED); // 상대방 장애물 색상
                    break; // 장애물을 찾았으므로 루프 종료
                }
            }
        }
    }


    // 행(row) 좌표 미러링
    private int mirrorRow(int row) {
        return rows - 1 - row;
    }

    // 열(col) 좌표 미러링
    private int mirrorCol(int col) {
        return cols - 1 - col;
    }

    // 상대방 말 이동
    public void moveOpponentPiece(int fromRow, int toRow, int fromCol, int toCol) {
        // 이전 위치 초기화
        opponentMatrix[mirrorRow(fromRow)][mirrorCol(fromCol)] = 0;

        // 새로운 위치 설정
        opponentMatrix[mirrorRow(toRow)][mirrorCol(toCol)] = 1;

        // 상대방 말 UI 업데이트
        opponentPiece.setLocation(mirrorCol(toCol) * 70, mirrorRow(toRow) * 70);
        gameArea.repaint();
    }
    private int[] findDiffrenceV(boolean[][] preVMatrix, boolean[][] verticalObstacleMatrix){
        int [] dif = new int[2];
        for(int i=0; i< preVMatrix.length; i++){
            for(int n=0; n<preVMatrix[0].length; n++){
                if(preVMatrix[i][n] != verticalObstacleMatrix[i][n]){
                    dif[0] = i;
                    dif[1] = n;
                }
            }
        }
        return dif;
    }

    private int[] findDiffrenceH(boolean[][] preHMatrix, boolean[][] horizontalObstacleMatrix){
        int [] dif = new int[2];
        for(int i=0; i< preHMatrix.length; i++){
            for(int n=0; n<preHMatrix[0].length; n++){
                if(preHMatrix[i][n] != horizontalObstacleMatrix[i][n]){
                    dif[0] = i;
                    dif[1] = n;
                }
            }
        }

        return dif;
    }
    private void copyMatrix(boolean[][] source, boolean[][] destination) {
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, source[i].length);
        }
    }

}
