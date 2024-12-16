import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ServerConnect {
    private int serverPort = 12345;
    private String serverAddress = "localhost";
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private QurridorUI qurridorUI;
    ///나중에 수정
    private String userId;
    private ReceiveThread receiveThread;
    private MessageQueue qurridorMessageQueue;

    public ServerConnect(String userId, QurridorUI qurridorUI, MessageQueue qurriodrMessageQueue) {
        this.userId = userId;
        this.qurridorUI = qurridorUI;
        this.qurridorMessageQueue = qurriodrMessageQueue;

        try {
            socket = new Socket(serverAddress, serverPort);
            outputStream = socket.getOutputStream();
            out = new ObjectOutputStream(new BufferedOutputStream(outputStream));
            out.flush();
            inputStream = socket.getInputStream();
            in = new ObjectInputStream(inputStream);
            sendUserId();

            receiveThread = new ReceiveThread(in, qurridorUI, qurridorMessageQueue);
            receiveThread.start();
        } catch (IOException e) {
            System.out.println("Server connect error");
            e.printStackTrace();
        }
    }

    private void sendUserId() {
        QurridorMsg loginMsg = new QurridorMsg();
        loginMsg.setNowMode(QurridorMsg.mode.LOGIN_MODE);
        loginMsg.setUserId(userId);
        try {
            out.writeObject(loginMsg);
            out.flush();
        } catch (IOException e) {
            System.out.println("Login err");
            e.printStackTrace();
        }
    }

    public void sendChatting(String chat) {
        QurridorMsg chatMsg = new QurridorMsg();
        chatMsg.setNowMode(QurridorMsg.mode.CHATTING_MODE);
        chatMsg.setUserId(userId);
        chatMsg.setMessage(chat);
        try {
            out.writeObject(chatMsg);
            out.flush();
        } catch (IOException e) {
            System.out.println("Chatting err");
            e.printStackTrace();
        }
    }

    public void sendMove(GameObject[][] gameBoard) {
        QurridorMsg gameMsg = new QurridorMsg();
        gameMsg.setUserId(userId);
        gameMsg.setNowMode(QurridorMsg.mode.PLAY_MODE);
        gameMsg.setGameBoard(gameBoard);
        try {
            out.writeObject(gameMsg);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendObstacle(GameObject[][] gameBoard) {
        QurridorMsg obstacleMsg = new QurridorMsg();
        obstacleMsg.setUserId(userId);
        obstacleMsg.setNowMode(QurridorMsg.mode.OBSTACLE_MODE);
        obstacleMsg.setGameBoard(gameBoard);
        obstacleMsg.isMsg = true;
        try {
            out.writeObject(obstacleMsg);
            out.flush();
        } catch (IOException e) {
            System.out.println("obstacle send err");
            e.printStackTrace();
        }
    }

    public void sendWin(String userId) {
        QurridorMsg winMsg = new QurridorMsg();
        winMsg.setUserId(userId);
        winMsg.setNowMode(QurridorMsg.mode.WIN_MODE);
        try {
            out.writeObject(winMsg);
            out.flush();
        } catch (IOException e) {
            System.out.println("Win msg send err");
            e.printStackTrace();
        }
    }

    public void requestMap(String fileName) {
        QurridorMsg mapSettingMsg = new QurridorMsg();
        mapSettingMsg.setNowMode(QurridorMsg.mode.XML_MODE);
        System.out.println(fileName);
        mapSettingMsg.setMessage(fileName);
        try {
            out.writeObject(mapSettingMsg);
            out.flush();
        } catch (IOException e) {
            System.out.println("request map err");
            e.printStackTrace();
        }
    }

    public void sendFile(File file, QurridorMsg.mode mode, String row, String col, String fileCase) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[1024]; // 1KB 버퍼
            int bytesRead; // 읽은 바이트 수

            while ((bytesRead = bis.read(buffer)) != -1) {
                QurridorMsg fileMsg = new QurridorMsg();
                fileMsg.setGameRow(row);
                fileMsg.setGameCol(col);
                fileMsg.setNowMode(mode);
                fileMsg.setFileName(file.getName());
                fileMsg.setFileCase(fileCase);
                fileMsg.setMessage("");
                // 실제 읽은 데이터 크기만큼 파일 데이터 설정
                if (bytesRead < 1024) {
                    byte[] exactData = new byte[bytesRead];
                    for (int i = 0; i < bytesRead; i++) {
                        exactData[i] = buffer[i]; // 데이터를 하나씩 복사
                    }
                    fileMsg.setFileData(exactData);
                } else {
                    fileMsg.setFileData(buffer);
                }
                out.writeObject(fileMsg);
                out.flush();

                //버퍼 초기화
                //버퍼에 남아있는 내용이 문제가 될 수 있다
                buffer = new byte[1024];
            }
                QurridorMsg EOFMessage = new QurridorMsg();
                EOFMessage.setNowMode(QurridorMsg.mode.IMAGE_MODE);
                EOFMessage.setFileCase("player1");
                EOFMessage.setMessage("EOF");
                out.writeObject(EOFMessage);
                out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
