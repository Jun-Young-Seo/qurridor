import java.io.*;
import java.net.Socket;

public class ServerConnect {
    private int serverPort = 12345;
    private String  serverAddress = "localhost";
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private QurridorUI qurridorUI;
    ///나중에 수정
    private String userId;
    private ReceiveThread receiveThread;
    public ServerConnect(String userId, QurridorUI qurridorUI){
        this.userId=userId;
        this.qurridorUI=qurridorUI;
        try {
            socket= new Socket(serverAddress,serverPort);
            outputStream = socket.getOutputStream();
            out = new ObjectOutputStream(new BufferedOutputStream(outputStream));
            out.flush();
            inputStream = socket.getInputStream();
            in = new ObjectInputStream(inputStream);
            sendUserId();

            receiveThread = new ReceiveThread(in,qurridorUI);
            receiveThread.start();
        } catch (IOException e) {
            System.out.println("Server connect error");
            e.printStackTrace();
        }
    }
    private void sendUserId(){
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
    public void sendChatting(String chat){
        QurridorMsg chatMsg = new QurridorMsg();
        chatMsg.setNowMode(QurridorMsg.mode.CHATTING_MODE);
        chatMsg.setUserId(userId);
        chatMsg.setMessage(chat);
        try {
            out.writeObject(chatMsg);
            out.flush();
        }catch (IOException e){
            System.out.println("Chatting err");
            e.printStackTrace();
        }
    }
    public void sendMove(int fromRow, int toRow, int fromCol, int toCol){
        QurridorMsg gameMsg = new QurridorMsg();
        gameMsg.setUserId(userId);
        gameMsg.setNowMode(QurridorMsg.mode.PLAY_MODE);
        gameMsg.setMoveData(fromRow,toRow,fromCol,toCol);
        try {
            out.writeObject(gameMsg);
            out.flush();
        } catch (IOException e) {
            System.out.println("Send Move err");
            e.printStackTrace();
        }
    }
}
