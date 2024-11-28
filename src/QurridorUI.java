import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class QurridorUI extends JFrame {
    private JPanel gamePanel;
    private JPanel userInfoPanel;
    private JPanel chatPanel;
    private JTextArea chatArea;
    private Container contentPane;
    private JPanel gameArea;
    private int howManyRows;
    private int howManyCols;
    private int [][] placeMatrix;
    private int [][] opponentMatrix;
    private boolean[][] verticalObstacleMatrix;
    private boolean[][] horizontalObstacleMatrix;
    private MessageQueue qurridorMessageQueue;
    private OpponentController opponentController;
    private QurridorGameController qurridorGameController;
    private final String userId = String.valueOf(Math.random());
    private boolean isFirst = false;
    ServerConnect serverConnect;
    public QurridorUI() {
        qurridorMessageQueue = new MessageQueue();

        setTitle("Quoridor Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1630, 940); // 전체 프레임 크기 설정
        contentPane = getContentPane();
        splitPanel();
        setVisible(true);
    }

    private void splitPanel() {
        xmlParsing();
        setLayout(new BorderLayout());

        // JSplitPane을 생성하여 좌우 분할
        JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        hSplitPane.setDividerLocation(1200); // Divider 위치 설정 (좌측 1200, 우측 400)

        // 게임 화면 패널(왼쪽)
        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setBackground(Color.LIGHT_GRAY);
        gamePanel.setSize(1200, 900);

        // 위쪽 상태바
        JPanel topStatusBar = new JPanel();
        topStatusBar.setBackground(Color.GRAY);
        topStatusBar.setSize(1200, 100);
        topStatusBar.setLocation(0, 0);
        JLabel topStatusLabel = new JLabel("상단 상태 표시줄", SwingConstants.CENTER);
        topStatusBar.add(topStatusLabel);

        // 아래쪽 상태바
        JPanel bottomStatusBar = new JPanel();
        bottomStatusBar.setBackground(Color.GRAY);
        bottomStatusBar.setSize(1200, 100);
        bottomStatusBar.setLocation(0, 800);
        JLabel bottomStatusLabel = new JLabel("하단 상태 표시줄", SwingConstants.CENTER);
        bottomStatusBar.add(bottomStatusLabel);

        serverConnect = new ServerConnect(userId, this, qurridorMessageQueue);
        // 게임 영역 설정 및 정중앙 배치
        setGameArea(howManyRows, howManyCols);
        int centerX = (gamePanel.getWidth() - gameArea.getWidth()) / 2;
        int centerY = (gamePanel.getHeight() - gameArea.getHeight()) / 2;
        gameArea.setLocation(centerX, centerY);

        // 게임 패널에 추가
        gamePanel.add(topStatusBar);
        gamePanel.add(gameArea);
        gamePanel.add(bottomStatusBar);

        hSplitPane.setLeftComponent(gamePanel);

        // 오른쪽 접속자 정보 및 채팅창
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);

        // 접속자 정보 패널
        userInfoPanel = new JPanel();
        userInfoPanel.setBackground(Color.CYAN);
        userInfoPanel.setSize(400, 450);
        userInfoPanel.setLocation(0, 0);
        JLabel userInfoLabel = new JLabel("접속자 정보", SwingConstants.CENTER);
        userInfoPanel.add(userInfoLabel);
        // 채팅창 패널
        chatPanel = new JPanel();
        chatPanel.setBackground(Color.PINK);
        chatPanel.setSize(400, 400);
        chatPanel.setLocation(0, 450);
        chatPanel.setLayout(new BorderLayout());

        // 채팅창 제목
        JLabel chatLabel = new JLabel("채팅창", SwingConstants.CENTER);
        chatLabel.setFont(new Font("Serif", Font.BOLD, 18)); // 글씨 크기 조정
        chatPanel.add(chatLabel, BorderLayout.NORTH);

        // 채팅창 메시지 표시 영역
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 16)); // 글씨 크기 조정
        chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // 입력창과 버튼 영역
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        // 입력창
        JTextField chatInput = new JTextField();
        chatInput.setFont(new Font("SansSerif", Font.PLAIN, 16)); // 글씨 크기 조정
        inputPanel.add(chatInput, BorderLayout.CENTER);

        // 보내기 버튼
        JButton sendButton = new JButton("보내기");
        sendButton.setFont(new Font("SansSerif", Font.PLAIN, 16)); // 글씨 크기 조정
        inputPanel.add(sendButton, BorderLayout.EAST);

        // 입력창과 버튼을 채팅창 아래쪽에 추가
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // 오른쪽 맨 아래 버튼
        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(400, 50);
        buttonPanel.setLocation(0, 850);
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.setBackground(Color.GREEN);
        JButton mapSettingButton = new JButton("맵 설정");
        JButton exitButton = new JButton("종료하기");
        buttonPanel.add(mapSettingButton);
        buttonPanel.add(exitButton);

        rightPanel.add(userInfoPanel);
        rightPanel.add(chatPanel);
        rightPanel.add(buttonPanel);

        hSplitPane.setRightComponent(rightPanel);

        contentPane.add(hSplitPane, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();


        ProcessMessage processMessage = new ProcessMessage();
        qurridorGameController = new QurridorGameController(gamePanel,gameArea,
                placeMatrix,verticalObstacleMatrix,horizontalObstacleMatrix,serverConnect,qurridorMessageQueue, isFirst);

        opponentController = new OpponentController(gameArea, opponentMatrix);
        processMessage.start();

        // 버튼 리스너 추가
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chat = chatInput.getText();
                if(chat.isEmpty()) return;
                chatInput.setText("");
                serverConnect.sendChatting(chat);
            }
        });
        chatInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
            }
        });

        gameArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameArea.requestFocus();
            }
        });

    }

    private void xmlParsing() {
        String xmlFile = "./d.xml";
        XMLReader xmlReader = new XMLReader(xmlFile);
        Node settingNode = xmlReader.getSettingElement();
        howManyRows = Integer.parseInt(xmlReader.getAttr(settingNode, xmlReader.E_HOWMANYROWS));
        howManyCols = Integer.parseInt(xmlReader.getAttr(settingNode, xmlReader.E_HOWMANYCOLS));
    }

    public void setGameArea(int rows, int cols) {
        placeMatrix = new int[rows][cols];
        opponentMatrix = new int[rows][cols];
        verticalObstacleMatrix = new boolean[rows][cols+1];
        horizontalObstacleMatrix =new boolean[rows+1][cols];
        gameArea = new JPanel();
        gameArea.setLayout(null);
        gameArea.setBackground(Color.WHITE);

        // 블록 및 장애물 크기 고정
        int blockWidth = 60;  // 블록 너비
        int blockHeight = 60; // 블록 높이
        int roadWidth = 10;   // 장애물 너비
        int roadHeight = 10;  // 장애물 높이

        int gameWidth = cols * blockWidth + (cols - 1) * roadWidth;
        int gameHeight = rows * blockHeight + (rows - 1) * roadHeight;

        gameArea.setSize(gameWidth, gameHeight);
        for (int row = 0; row < rows; row++) {
            verticalObstacleMatrix[row][0] = true;        // 왼쪽 벽
            verticalObstacleMatrix[row][cols] = true;     // 오른쪽 벽
        }
        // 위쪽과 아래쪽 가장자리 벽 설정
        for (int col = 0; col < cols; col++) {
            horizontalObstacleMatrix[0][col] = true;       // 위쪽 벽
            horizontalObstacleMatrix[rows][col] = true;    // 아래쪽 벽
        }

        ObstacleActionListener obstacleActionListener= new ObstacleActionListener(verticalObstacleMatrix,
                                                        horizontalObstacleMatrix,serverConnect);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 블록 추가
                JLabel block = new JLabel();
                block.setOpaque(true);
//                block.setBackground(Color.BLACK);
                block.setSize(blockWidth, blockHeight);
                block.setLocation(j * (blockWidth + roadWidth), i * (blockHeight + roadHeight));
                gameArea.add(block);

                // 수직 길 추가 (각 블록 오른쪽)
                if (j < cols - 1) {
                    Obstacle verticalObstacle = new Obstacle(i, j, true);
                    verticalObstacle.setSize(roadWidth, blockHeight);
                    verticalObstacle.setLocation(block.getX() + blockWidth, block.getY());
                    verticalObstacle.addActionListener(obstacleActionListener);
                    gameArea.add(verticalObstacle);
                }

                // 수평 길 추가 (각 블록 아래쪽)
                if (i < rows - 1) {
                    Obstacle horizontalObstacle = new Obstacle(i, j, false);
                    horizontalObstacle.setSize(blockWidth, roadHeight);
                    horizontalObstacle.setLocation(block.getX(), block.getY() + blockHeight);
                    horizontalObstacle.addActionListener(obstacleActionListener);
                    gameArea.add(horizontalObstacle);
                }
            }
        }

    }

    private class ProcessMessage extends Thread{
        @Override
        public void run(){
            while (true) {
                while (!qurridorMessageQueue.isEmpty()) {
                    QurridorMsg serverMsg = qurridorMessageQueue.dequeueMessage();
                    String id = serverMsg.getUserId();
                    switch (serverMsg.getNowMode()) {
                        case LOGIN_MODE:
                            break;
                        case LOGOUT_MODE:
                            break;
                        case CHATTING_MODE:
                            String msg = serverMsg.getMessage();
                            if(id.equals(userId)) {
                                chatArea.append(userId + " : "+msg + "\n");
                            }
                            else{
                                chatArea.append(id+" : "+msg+"\n");
                            }
                            break;
                        case FIRST_MODE:
                            String whoFirst = serverMsg.getMessage();
                            if(userId.equals(whoFirst)){
                                isFirst = true;
                                chatArea.append("선턴입니다.");
                                qurridorGameController.setMyTurn(true);
                            }
                            else{
                                chatArea.append("상대가 선턴입니다.");
                                qurridorGameController.setMyTurn(false);
                            }
                            break;
                        case PLAY_MODE:
                            if(id.equals(userId)) {
                                qurridorGameController.movePiece(serverMsg.getFromRow(), serverMsg.getToRow()
                                        , serverMsg.getFromCol(), serverMsg.getToCol());
                                qurridorGameController.setMyTurn(false);
                            }
                            else{
                                opponentController.moveOpponentPiece(serverMsg.getFromRow(), serverMsg.getToRow()
                                        , serverMsg.getFromCol(), serverMsg.getToCol());
                                qurridorGameController.setMyTurn(true);
                            }
                            break;
                        case OBSTACLE_MODE:
                            if(id.equals(userId)){
                                System.out.println("ME");
                                serverMsg.printObstacle();
                                qurridorGameController.setObstacle(serverMsg.getVerticalObstacleMatrix(),serverMsg.getHorizontalObstacleMatrix());
                                qurridorGameController.setMyTurn(false);
                            }
                            else{
                                System.out.println("ENEMY");
                                serverMsg.printObstacle();
                                opponentController.setOpponentObstacle(serverMsg.getVerticalObstacleMatrix(),serverMsg.getHorizontalObstacleMatrix());
                                qurridorGameController.setMyTurn(true);
                            }
                    }
                }
            }
        }
    }
}
