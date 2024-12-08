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
    private GameObject[][] gameBoard;
    private MessageQueue qurridorMessageQueue;
    private QurridorGameController qurridorGameController;
    private OpponentController opponentController;
    private String userId = String.valueOf((int) (Math.random() * 100));
    private String firstUserId;
    private String secondUserId;
    private String opponentId;
    private boolean isFirst = false;
    ServerConnect serverConnect;
    private ObstacleActionListener obstacleActionListener;
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
        setGameArea();
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


        ProcessMessage processMessage = new ProcessMessage(this);
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

    public void setGameArea() {
        gameArea = new JPanel();
        gameArea.setLayout(null);
        gameArea.setBackground(Color.WHITE);
        int boardRows = 2 * howManyRows - 1;
        int boardCols = 2 * howManyCols - 1;
        gameBoard = new GameObject[boardRows][boardCols];

        // 블록 및 장애물 크기 고정
        int blockWidth = 60;  // 블록 너비
        int blockHeight = 60; // 블록 높이
        int roadWidth = 10;   // 장애물 너비
        int roadHeight = 10;  // 장애물 높이
        int totalWidth = (howManyCols * blockWidth) + ((howManyCols - 1) * roadWidth);
        int totalHeight = (howManyRows * blockHeight) + ((howManyRows - 1) * roadHeight);

        obstacleActionListener= new ObstacleActionListener(gameBoard,serverConnect,userId);

        gameArea.setSize(totalWidth, totalHeight);
        gameArea.setBackground(Color.WHITE);
        for (int r = 0; r < gameBoard.length; r++) {
            for (int c = 0; c < gameBoard[0].length; c++) {
                if (r % 2 == 0 && c % 2 == 0) {
                    // 짝수행, 짝수열 -> 블록
                    Block block = new Block(r,c);

                    JLabel blockLabel = new JLabel();
                    blockLabel.setOpaque(true);
                    blockLabel.setBackground(Color.WHITE);
                    int x = (c / 2) * (blockWidth + roadWidth);
                    int y = (r / 2) * (blockHeight + roadHeight);
                    blockLabel.setBounds(x, y, blockWidth, blockHeight);
                    gameArea.add(blockLabel);
                    gameBoard[r][c] = block;
                } else if (r % 2 == 0) {
                    // 짝수행, 홀수열 -> 수직 장애물
                    Obstacle verticalObstacle = new Obstacle(r,c,true);

                    ObstacleButton verticalObstacleButton = new ObstacleButton(r,c,true);
                    verticalObstacleButton.setOpaque(true);
                    verticalObstacleButton.setBackground(Color.RED);
                    int x = (c / 2) * (blockWidth + roadWidth) + blockWidth;
                    int y = (r / 2) * (blockHeight + roadHeight);
                    verticalObstacleButton.setBounds(x, y, roadWidth, blockHeight);
                    verticalObstacleButton.addActionListener(obstacleActionListener);
                    gameArea.add(verticalObstacleButton);
                    gameBoard[r][c] = verticalObstacle;
                } else if (c % 2 == 0) {
                    // 홀수행, 짝수열 -> 수평 장애물
                    Obstacle horizontalObstacle = new Obstacle(r,c,false);

                    ObstacleButton horizontalObstacleButton = new ObstacleButton(r,c,false);
                    horizontalObstacleButton.setOpaque(true);
                    horizontalObstacleButton.setBackground(Color.CYAN);
                    int x = (c / 2) * (blockWidth + roadWidth);
                    int y = (r / 2) * (blockHeight + roadHeight) + blockHeight;
                    horizontalObstacleButton.setBounds(x, y, blockWidth, roadHeight);
                    horizontalObstacleButton.addActionListener(obstacleActionListener);
                    gameArea.add(horizontalObstacleButton);
                    gameBoard[r][c] = horizontalObstacle;
                }
            }
        }
        qurridorGameController = new QurridorGameController(gamePanel,gameArea,
                gameBoard,serverConnect, userId,this);
        opponentController = new OpponentController(gameBoard,this);

    }
    public void startGame(GameObject[][] gameBoard){
        int bottomRow = gameBoard.length - 1; // 맨 아래
        int centerCol = gameBoard[0].length / 2; // 중앙
        int topRow = 0; //맨위
        if (gameBoard[bottomRow][centerCol] instanceof Block) {
            ((Block) gameBoard[bottomRow][centerCol]).setUserId(firstUserId);
        }
        if(gameBoard[topRow][centerCol] instanceof Block){
            ((Block) gameBoard[topRow][centerCol]).setUserId(secondUserId);
        }        renderGameArea(gameBoard);
    }
    public void renderGameArea(GameObject[][] gameBoard) {
        gameArea.removeAll();
        int blockSize = 60;
        int obstacleSize = 10;

        for (int r = 0; r < gameBoard.length; r++) {
            for (int c = 0; c < gameBoard[0].length; c++) {
                GameObject obj = gameBoard[r][c];
                if (obj instanceof Block) {
                    Block block = (Block) obj;
                    JLabel blockLabel = new JLabel();
                    blockLabel.setOpaque(true);

                    if (block.getUserId().equals(firstUserId)) {
                        blockLabel.setBackground(Color.GREEN); // 내 말
                    } else if (block.getUserId().equals(secondUserId)) {
                        blockLabel.setBackground(Color.RED); // 상대방 말
                    } else if (block.getUserId().isEmpty()) {
                        blockLabel.setBackground(Color.WHITE); // 빈 블록
                    }

                    blockLabel.setBounds(c / 2 * (blockSize + obstacleSize), r / 2 * (blockSize + obstacleSize), blockSize, blockSize);
                    gameArea.add(blockLabel);
                }
                else if (obj instanceof Obstacle) {
                    Obstacle obstacle = (Obstacle) obj;
                    ObstacleButton obstacleButton = new ObstacleButton(obstacle.getRow(), obstacle.getCol(), obstacle.getIsVertical());
                    obstacleButton.setBackground(obstacle.getIsObstacle() ? Color.RED : Color.LIGHT_GRAY);
                    obstacleButton.addActionListener(obstacleActionListener);
                    if (obstacle.getIsVertical()) {
                        obstacleButton.setBounds(c / 2 * (blockSize + obstacleSize) + blockSize, r / 2 * (blockSize + obstacleSize), obstacleSize, blockSize);
                    } else {
                        obstacleButton.setBounds(c / 2 * (blockSize + obstacleSize), r / 2 * (blockSize + obstacleSize) + blockSize, blockSize, obstacleSize);
                    }
                    gameArea.add(obstacleButton);
                }
            }
        }

        gameArea.revalidate();
        gameArea.repaint();
    }


    private class ProcessMessage extends Thread{
        QurridorUI qurridorUI;
        private ProcessMessage(QurridorUI qurridorUI){
            this.qurridorUI=qurridorUI;
        }
        @Override
        public void run(){
            while (true) {
                while (!qurridorMessageQueue.isEmpty()) {
                    QurridorMsg serverMsg = qurridorMessageQueue.dequeueMessage();
                    System.out.println(serverMsg.getNowMode());
                    String id = serverMsg.getUserId();
                    switch (serverMsg.getNowMode()) {
                        case LOGIN_MODE:
                            if(!id.equals(userId)){
                                opponentId=id;
                                chatArea.append(opponentId+" Login \n");
                            }
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
                            String message = serverMsg.getMessage();
                            String[] ids = message.split(",");

                            firstUserId = ids[0];
                            secondUserId = ids[1];
                            if(userId.equals(firstUserId)){
                                isFirst = true;
                                chatArea.append("선턴입니다.");
                                qurridorGameController.startGame(true,userId,secondUserId,gameBoard); // 첫 번째 플레이어로 게임 시작

                            }
                            else if(userId.equals(secondUserId)){
                                chatArea.append("상대가 선턴입니다.");
                                qurridorGameController.startGame(false,firstUserId,userId,gameBoard); // 두 번째 플레이어로 게임 시작
                                qurridorGameController.setUserId(secondUserId);
                            }
                            startGame(gameBoard);
                            serverConnect.sendMove(gameBoard);
                            break;
                        case PLAY_MODE, OBSTACLE_MODE:
                            if(id.equals(userId)) {
                                gameBoard = serverMsg.getGameBoard();
                                qurridorGameController.updateGameBoardFromServer(gameBoard);
                                opponentController.setGameBoard(gameBoard);
                                qurridorGameController.setMyTurn(false);
                                obstacleActionListener.setGameBoard(gameBoard);
                            }
                            else{
                                gameBoard= serverMsg.getGameBoard();
                                opponentController.updateGameBoardFromServer(gameBoard);
                                qurridorGameController.setGameBoard(gameBoard);
                                qurridorGameController.setMyTurn(true);
                                obstacleActionListener.setGameBoard(gameBoard);
                            }
                            break;
                    }
                }
            }
        }
    }
}
