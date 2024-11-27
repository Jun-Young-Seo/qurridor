import java.io.Serializable;

public class QurridorMsg implements Serializable {
    private String userId;
    private mode nowMode;
    private MoveData moveData;
    private boolean [][] verticalObstacleMatrix;
    private boolean [][] horizontalObstacleMatrix;

    public enum mode{LOGIN_MODE,LOGOUT_MODE,CHATTING_MODE,XML_MODE,PLAY_MODE,OBSTACLE_MODE,WIN_MODE,LOSE_MODE,END_MODE,FIRST_MODE};
    private String message;
    private String fileName;
    private byte [] fileData;//파일 데이터를 담을 배열 --> 1k 버퍼처럼 사용

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

    public void setNowMode(mode mode){
        this.nowMode=mode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean[][] getHorizontalObstacleMatrix() {
        return horizontalObstacleMatrix;
    }

    public boolean[][] getVerticalObstacleMatrix() {
        return verticalObstacleMatrix;
    }
    public void printObstacle(){
        System.out.println("V");
        for(int i=0; i<verticalObstacleMatrix.length; i++){
            for(int n=0; n<verticalObstacleMatrix[0].length; n++){
                System.out.print(verticalObstacleMatrix[i][n]+" ");
            }
            System.out.println();
        }

        System.out.println("H");
        for(int i=0;i<horizontalObstacleMatrix.length; i++){
            for(int n=0; n<horizontalObstacleMatrix[0].length; n++){
                System.out.print(horizontalObstacleMatrix[i][n]+" ");
            }
            System.out.println();
        }
    }

    public void setHorizontalObstacleMatrix(boolean[][] horizontalObstacleMatrix) {
        this.horizontalObstacleMatrix = horizontalObstacleMatrix;
    }

    public void setVerticalObstacleMatrix(boolean[][] verticalObstacleMatrix) {
        this.verticalObstacleMatrix = verticalObstacleMatrix;
    }

    @Override
    public String toString(){
        return "mode : " + nowMode.name() + "msg : "+message + "fname : "+fileName;
    }
    private class MoveData implements Serializable {
        private int fromRow;
        private int fromCol;
        private int toRow;
        private int toCol;

        public MoveData(int fromRow, int fromCol, int toRow, int toCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }

        // Getters and Setters
        public int getFromRow() {
            return fromRow;
        }

        public void setFromRow(int fromRow) {
            this.fromRow = fromRow;
        }

        public int getFromCol() {
            return fromCol;
        }

        public void setFromCol(int fromCol) {
            this.fromCol = fromCol;
        }

        public int getToRow() {
            return toRow;
        }

        public void setToRow(int toRow) {
            this.toRow = toRow;
        }

        public int getToCol() {
            return toCol;
        }

        public void setToCol(int toCol) {
            this.toCol = toCol;
        }

        @Override
        public String toString() {
            return "MoveData [from=(" + fromRow + ", " + fromCol + "), to=(" + toRow + ", " + toCol + ")]";
        }
    }
    public void setMoveData(int fromRow, int fromCol, int toRow, int toCol) {
        this.moveData = new MoveData(fromRow, fromCol, toRow, toCol);
    }
    //브로드캐스트용
    public void setMoveData(MoveData moveData){
        this.moveData = new MoveData(moveData.fromRow, moveData.fromCol, moveData.toRow, moveData.toCol);
    }

    public MoveData getMoveData() {
        return moveData;
    }
    public int getFromRow(){
        return moveData.fromRow;
    }
    public int getToRow(){
        return moveData.toRow;
    }
    public int getFromCol(){
        return moveData.getFromCol();
    }
    public int getToCol(){
        return moveData.toCol;
    }
}
