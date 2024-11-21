import java.io.IOException;
import java.io.ObjectInputStream;

public class ReceiveThread extends Thread{
    private ObjectInputStream in;
    private QurridorMsg serverMsg;
    private QurridorUI qurridorUI;
    public ReceiveThread(ObjectInputStream in, QurridorUI qurridorUI){
        this.in=in;
        this.qurridorUI=qurridorUI;
    }
    @Override
    public void run(){
        receiveMessage();
    }
    public void receiveMessage(){
        try{
            while (true){
                serverMsg = (QurridorMsg)in.readObject();
                switch (serverMsg.getNowMode()){
                    case LOGIN_MODE:
                        break;
                    case LOGOUT_MODE:
                        break;
                    case CHATTING_MODE:
                        String msg = serverMsg.getMessage();
                        qurridorUI.addChat(msg);
                        break;
                }
            }
        }catch (IOException | ClassNotFoundException e){
            System.out.println("receive err");
            e.printStackTrace();
        }
    }
}
