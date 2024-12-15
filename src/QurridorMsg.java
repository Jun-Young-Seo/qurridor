import javax.swing.*;
import java.io.Serializable;

public class QurridorMsg implements Serializable {
    private String userId;
    private mode nowMode;
    private GameObject[][] gameBoard;

    public enum mode {LOGIN_MODE, LOGOUT_MODE, CHATTING_MODE, IMAGE_MODE,START_MODE,XML_MODE, PLAY_MODE, OBSTACLE_MODE, WIN_MODE, LOSE_MODE, END_MODE, FIRST_MODE};
    private String message;
    private String fileName;
    private byte[] fileData;//파일 데이터를 담을 배열 --> 1k 버퍼처럼 사용
    public boolean isMsg;
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
                    System.out.print("");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public void gameBoardObstacleToString(GameObject[][] gameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int n = 0; n < gameBoard[i].length; n++) {
                GameObject obj = gameBoard[i][n];
                if (obj instanceof Obstacle) {
                    Obstacle obstacle = (Obstacle) obj;
                    if (obstacle.getIsObstacle()) {
                        // 수직(V) 또는 수평(H) 출력
                        System.out.print(obstacle.getIsVertical() ? "V" : "H");
                    } else {
                        System.out.print("X"); // 장애물 없음 표시
                    }
                } else {
                    System.out.print(" "); // Obstacle이 아닌 경우
                }
                System.out.print(" "); // 칸 간격
            }
            System.out.println(); // 줄 바꿈
        }
        System.out.println(); // 행 출력 후 공백
    }


}