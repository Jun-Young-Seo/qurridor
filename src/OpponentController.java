public class OpponentController {
    private GameObject[][] gameBoard;
    private QurridorUI qurridorUI;
    private String opponentId;

    public OpponentController(GameObject[][] gameBoard, QurridorUI qurridorUI) {
        this.gameBoard = gameBoard;
        this.qurridorUI = qurridorUI;
    }

    public void updateGameBoardFromServer(GameObject[][] updatedBoard) {
        this.gameBoard = updatedBoard;
        qurridorUI.renderGameArea(gameBoard);
    }
    public void setGameBoard(GameObject[][] gameBoard){
        this.gameBoard=gameBoard;
    }
}
