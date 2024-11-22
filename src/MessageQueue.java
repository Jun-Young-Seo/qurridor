import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//중앙 처리 큐 클래스
//Thread-Safety 를 위해 Linked-Blocking queue 사용
public class MessageQueue {
    private BlockingQueue<QurridorMsg> messageQueue;
    public MessageQueue(){
        messageQueue=new LinkedBlockingQueue<>();
    }
    //메세지 넣기
    public void enqueueMessage(QurridorMsg msg) {
//        System.out.println("enqeue!! "+ msg.toString());
        messageQueue.offer(msg);
//        System.out.println(messageQueue.size());
    }
    //메세지 빼기 --> 맨 앞 삭제됨 poll 아니고 디큐
    public QurridorMsg dequeueMessage() {
//        System.out.println("Dequeue!!");
        try {
            if(!isEmpty()) return messageQueue.take();
            return null;
        } catch (InterruptedException e) {
            System.out.println("deque err");
            e.printStackTrace();
            return null;
        }
    }

    public boolean isEmpty() {
        return messageQueue.isEmpty();
    }

}
