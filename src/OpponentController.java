import javax.swing.*;

public class OpponentController {
    private JPanel gameArea;
    private GameObject[][] gameBoard;
    private QurridorUI qurridorUI;
    private int opponentRow;
    private int opponentCol;

    public OpponentController(JPanel gameArea, GameObject[][] gameBoard, QurridorUI qurridorUI, boolean isFirst) {
        this.gameArea = gameArea;
        this.gameBoard = gameBoard;
        this.qurridorUI = qurridorUI;

        // 상대방 말 위치 초기화
        if (isFirst) {
            opponentRow = 0; // 상대방은 맨 위
        } else {
            opponentRow = gameBoard.length - 1; // 상대방은 맨 아래
        }
        opponentCol = gameBoard[0].length / 2; // 중앙

        // 상대방 말 초기 상태 설정
        if (gameBoard[opponentRow][opponentCol] instanceof Block) {
            ((Block) gameBoard[opponentRow][opponentCol]).setBlockStatus(-1); // 상대 말은 -1
        }
    }

    // 서버에서 받은 상태 반영
    public void updateGameBoardFromServer(GameObject[][] updatedBoard) {
        this.gameBoard = updatedBoard;
        qurridorUI.renderGameArea(gameBoard);
    }
}
