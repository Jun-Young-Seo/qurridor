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
}