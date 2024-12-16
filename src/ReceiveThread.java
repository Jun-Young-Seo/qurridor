import java.io.IOException;
import java.io.ObjectInputStream;

public class ReceiveThread extends Thread{
    private ObjectInputStream in;
    private QurridorMsg serverMsg;
    private QurridorUI qurridorUI;
    private MessageQueue qurridorMessageQueue;
    public ReceiveThread(ObjectInputStream in, QurridorUI qurridorUI, MessageQueue qurridorMessageQueue){
        this.in=in;
        this.qurridorUI=qurridorUI;
        this.qurridorMessageQueue=qurridorMessageQueue;
    }
    @Override
    public void run(){
        receiveMessage();
    }
    public void receiveMessage(){
        try{
            while (true){
                serverMsg = (QurridorMsg)in.readObject();
                qurridorMessageQueue.enqueueMessage(serverMsg);
                System.out.println("Enqueue : "+ serverMsg);
            }
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("receive err");
            e.printStackTrace();
        }
    }
}
