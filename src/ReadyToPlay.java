import javax.swing.*;
import java.awt.*;

public class ReadyToPlay extends JLabel {
    public ReadyToPlay(){
        setText("다음 접속을 기다리는 중... 2명이 되면 시작");
        setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬
        setVerticalAlignment(SwingConstants.CENTER);

        // 폰트 설정
        setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        setForeground(Color.BLACK); // 텍스트 색상

        // 크기 및 위치 설정
        this.setSize(1200, 50);
        this.setLocation(0, 400);


        this.setVisible(true);

    }
    public void setReady(){
        this.setText("플레이 준비가 끝났습니다. 우측에서 맵을 고르거나 하단에서 맵을 만들어주세요.");
    }
    public void showReady(boolean visible) {
        this.setVisible(visible);
    }
}
