import javax.swing.*;
import java.awt.*;

public class OpponentController {
    private JComponent[][] gameMatrix; // 게임 보드 매트릭스
    private JLabel opponentPiece; // 상대방 말 UI
    private JPanel gameArea; // 게임 보드 패널

    public OpponentController(JPanel gameArea, JComponent[][] gameMatrix) {
        this.gameArea = gameArea;
        this.gameMatrix = gameMatrix;
        initializeOpponent();
    }

    // 상대방 말 초기화
    public void initializeOpponent() {
        int startRow = mirrorRow(gameMatrix.length - 1); // 초기 위치 (내 화면 기준 맨 아래)
        int startCol = mirrorCol((gameMatrix[0].length - 1) / 2);

        // 상대방 말 UI 생성
        opponentPiece = new JLabel();
        opponentPiece.setOpaque(true);
        opponentPiece.setBackground(Color.RED);
        opponentPiece.setSize(60, 60);
        opponentPiece.setLocation(startCol * 35, startRow * 35); // 35은 블록+간격 반영 크기

        // gameMatrix에 상대방 말 등록
        gameMatrix[startRow][startCol] = opponentPiece;

        gameArea.add(opponentPiece);
        gameArea.setComponentZOrder(opponentPiece, 0);
        gameArea.repaint();
    }

    // 행(row) 좌표 미러링
    public int mirrorRow(int row) {
        return gameMatrix.length - 1 - row;
    }

    // 열(col) 좌표 미러링
    public int mirrorCol(int col) {
        int centerCol = (gameMatrix[0].length - 1) / 2;
        return 2 * centerCol - col;
    }

    // 상대방 말 이동
    public void moveOpponentPiece(int fromRow, int toRow, int fromCol, int toCol) {
        // 이전 위치 초기화
        gameMatrix[mirrorRow(fromRow)][mirrorCol(fromCol)] = null;

        // 새로운 위치 설정
        gameMatrix[mirrorRow(toRow)][mirrorCol(toCol)] = opponentPiece;

        // 상대방 말 UI 업데이트
        opponentPiece.setLocation(mirrorCol(toCol) * 35, mirrorRow(toRow) * 35); // 35은 블록+간격 반영 크기
        gameArea.repaint();
    }
}
