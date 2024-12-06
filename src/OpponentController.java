public class OpponentController {
    private GameObject[][] gameBoard;
    private QurridorUI qurridorUI;
    private String opponentId;

    public OpponentController(GameObject[][] gameBoard, QurridorUI qurridorUI) {
        this.gameBoard = gameBoard;
        this.qurridorUI = qurridorUI;
    }

    public void initOpponent(String opponentId, boolean isFirst) {
        this.opponentId = opponentId;

        int opponentRow = isFirst ? 0 : gameBoard.length - 1; // 상대는 반대 위치
        int opponentCol = gameBoard[0].length / 2;

        // 상대방 초기 말 위치 설정
        if (gameBoard[opponentRow][opponentCol] instanceof Block) {
            ((Block) gameBoard[opponentRow][opponentCol]).setUserId(opponentId);
        }

        qurridorUI.renderGameArea(gameBoard);
    }

    public void updateGameBoardFromServer(GameObject[][] updatedBoard) {
        this.gameBoard = updatedBoard;
        qurridorUI.renderGameArea(gameBoard);
    }
}
