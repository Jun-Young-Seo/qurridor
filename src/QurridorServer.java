import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class QurridorServer {
    private JFrame mainFrame;
    private JPanel displayPanel;
    private JPanel controlPanel;
    private JTextArea t_display;
    private int port = 12345;
    private ServerSocket serverSocket;
    private Vector<PlayerHandler> playerConnects = new Vector<>(2);
    private boolean assignFirst = false;
    public QurridorServer() {
        buildGUI();
    }

    public void buildGUI() {
        mainFrame = new JFrame("QurridorGame - Server");
        mainFrame.setSize(800, 600);
        mainFrame.setLocation(900, 0);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayPanel = createDisplayPanel();
        controlPanel = createControlPanel();
        mainFrame.setContentPane(displayPanel);
        displayPanel.add(controlPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
        startServer(port);
    }

    public JPanel createDisplayPanel() {
        //결과 창 패널
        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        //결과 창 - 클라이언트가 보낸 메시지가 적힐 곳
        t_display = new JTextArea();
        //결과 창 폰트 키우기
        Font resultFont = new Font("Serif", Font.BOLD, 20);
        t_display.setFont(resultFont);
        //텍스트가 추가될 때 마다 커서를 맨 뒤로
        //자동 스크롤 내리기 용도
        t_display.setCaretPosition(t_display.getDocument().getLength());
        //수정불가
        t_display.setEditable(false);

        //스크롤 붙이기
        JScrollPane resultAreaScroll = new JScrollPane(t_display);
        resultAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        displayPanel.add(resultAreaScroll);
        return displayPanel;
    }


    public JPanel createControlPanel() {
        //버튼이 2개인 패널
        controlPanel = new JPanel();
        //버튼 늘어나면 ㄴ중에 수정
        controlPanel.setLayout(new GridLayout(1, 1));

        JButton exitButton = new JButton("종료하기");
        controlPanel.add(exitButton);
        //종료하기 버튼 리스너
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return controlPanel;
    }

    public void startServer(int port) {
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            printDisplay("서버가 시작되었습니다 : " + serverSocket.getInetAddress());
            while (true) {
                //accept해서 클라이언트 소켓 생성
                clientSocket = serverSocket.accept();
                //소켓을 인자로 클라이언트별 핸들러 스레드를 생성
                PlayerHandler playerHandler = new PlayerHandler(clientSocket);
                playerConnects.add(playerHandler);
                playerHandler.start();

            }
        } catch (IOException e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
    //그냥 화면에 표시하는 메서드
    public void printDisplay(String msg) {
        t_display.append(msg + "\n");
    }

    //클라이언트들에게 브로드캐스트하는 함수
    public void broadCast(QurridorMsg qurridorMsg) {
        //벡터에 포함된 모든 클라이언트에게 전송
        for (PlayerHandler p : playerConnects) {
            try {
                p.objectOutputStream.writeObject(qurridorMsg);
                p.objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //파일 브로드캐스트 함수
    //ImageIcon과 따로 분리하지 않고, 모드로 분리해서 사용

    // 서버에서 다중 접속 처리(멀티스레딩 서버)를 위한 핸들러 클래스
    class PlayerHandler extends Thread {
        private Socket clientSocket;
        private InputStream in;

        private OutputStream out;

        private String userId;
        ObjectInputStream objectInputStream;
        ObjectOutputStream objectOutputStream;
        private QurridorMsg qurridorMsg;
        private GameObject [][] gameBoard;

        public PlayerHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                out = clientSocket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(out));
                objectOutputStream.flush();
                in = clientSocket.getInputStream();
                objectInputStream = new ObjectInputStream(new BufferedInputStream(in));
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        @Override
        public void run() {
            receiveMessages();
        }

        public void receiveMessages() {
            try {
                while (true) {
                    qurridorMsg = (QurridorMsg) objectInputStream.readObject();
                    System.out.println(qurridorMsg.toString());
                    switch (qurridorMsg.getNowMode()) {//모드에 따라 처리
//                        로그인 모드
                        case LOGIN_MODE:
                            userId = qurridorMsg.getUserId();
                            printDisplay("클라이언트가 연결되었습니다. : " + clientSocket.getInetAddress());
                            printDisplay("새 참가자 : " + userId);
                            printDisplay("현재 참가자 수 : " + playerConnects.size());

                            // 새로운 참가자 알림
                            QurridorMsg loginMsg = new QurridorMsg();
                            loginMsg.setMessage("새 참가자 : " + userId);
                            loginMsg.setUserId(userId);
                            loginMsg.setNowMode(QurridorMsg.mode.LOGIN_MODE);
                            broadCast(loginMsg);

                            // 두 명의 클라이언트가 로그인했을 때 선턴 메시지 전송
                            if (playerConnects.size() == 2 && !assignFirst) {
                                // 모든 플레이어의 ID가 초기화되었는지 확인
                                String firstId = playerConnects.get(0).getUserId();
                                String secondId = playerConnects.get(1).getUserId();

                                if (firstId != null && secondId != null) {
                                    assignFirst = true;

                                    QurridorMsg assignFirstMsg = new QurridorMsg();
                                    assignFirstMsg.setNowMode(QurridorMsg.mode.FIRST_MODE);
                                    assignFirstMsg.setMessage(firstId + "," + secondId);
                                    broadCast(assignFirstMsg);
                                }
                            }
                            break;
//                        case START_MODE:
//                            gameBoard = new GameObject[9][9];
                        //채팅 모드
                        case CHATTING_MODE:
                            QurridorMsg chattingMsg = new QurridorMsg();
                            printDisplay(userId + " : " + qurridorMsg.getMessage());
                            chattingMsg.setNowMode(QurridorMsg.mode.CHATTING_MODE);
                            chattingMsg.setUserId(userId);
                            chattingMsg.setMessage(qurridorMsg.getMessage());
                            broadCast(chattingMsg);
                            break;
                        case PLAY_MODE:
                            gameBoard = qurridorMsg.getGameBoard();
                            QurridorMsg gameMsg = new QurridorMsg();
                            gameMsg.setNowMode(QurridorMsg.mode.PLAY_MODE);
                            gameMsg.setUserId(userId);
                            gameMsg.setGameBoard(gameBoard);
                            broadCast(gameMsg);
                            break;
                        case OBSTACLE_MODE:
                            gameBoard = qurridorMsg.getGameBoard();
                            qurridorMsg.gameBoardObstacleToString(gameBoard);
                            QurridorMsg obstacleMsg = new QurridorMsg();
                            obstacleMsg.setNowMode(QurridorMsg.mode.OBSTACLE_MODE);
                            obstacleMsg.setUserId(userId);
                            obstacleMsg.setGameBoard(gameBoard);
                            broadCast(obstacleMsg);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        public String getUserId() {
            return userId;
        }
    }
}

