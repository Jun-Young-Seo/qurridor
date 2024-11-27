import javax.swing.*;
import java.awt.*;

public class OpponentController {
    private int[][] opponentMatrix; // 상대방의 위치를 관리하는 배열
    private JLabel opponentPiece; // 상대방 말 UI
    private JPanel gameArea; // 게임 보드 패널
    private int rows;
    private int cols;

    public OpponentController(JPanel gameArea, int[][] opponentMatrix) {
        this.gameArea = gameArea;
        this.opponentMatrix = opponentMatrix;
        this.rows = opponentMatrix.length;
        this.cols = opponentMatrix[0].length;
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

    // 행(row) 좌표 미러링
    public int mirrorRow(int row) {
        return rows - 1 - row;
    }

    // 열(col) 좌표 미러링
    public int mirrorCol(int col) {
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
}
