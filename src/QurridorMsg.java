import javax.swing.*;
import java.io.Serializable;

public class QurridorMsg implements Serializable {
    private String userId;
    private mode nowMode;
    private GameObject[][] gameBoard;

    public enum mode {LOGIN_MODE, LOGOUT_MODE, CHATTING_MODE, START_MODE,XML_MODE, PLAY_MODE, OBSTACLE_MODE, WIN_MODE, LOSE_MODE, END_MODE, FIRST_MODE};
    private String message;
    private String fileName;
    private byte[] fileData;//파일 데이터를 담을 배열 --> 1k 버퍼처럼 사용

    public mode getNowMode() {
        return nowMode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public void setNowMode(mode mode) {
        this.nowMode = mode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGameBoard(GameObject[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public GameObject[][] getGameBoard() {
        return gameBoard;
    }

    @Override
    public String toString() {
        return "mode : " + nowMode.name() + " msg : " + message;
    }
    public void gameBoardToString(GameObject[][] gameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int n = 0; n < gameBoard[0].length; n++) {
                GameObject obj = gameBoard[i][n];
                if (obj instanceof Block) {
                    Block block = (Block) obj;
                    String userId = block.getUserId();
                    System.out.print((userId != null && !userId.isEmpty()) ? userId : "X");
                } else {
                    System.out.print(""); // Block이 아닌 경우 빈 칸으로 처리
                }
                System.out.print(" "); // 칸 간격
            }
            System.out.println(); // 행 구분
        }
        System.out.println(); // 출력 후 한 줄 공백
    }

}