import javax.swing.*;
import java.awt.*;

public class TurnDisplay extends JLabel {

    public TurnDisplay() {
        // 텍스트 설정
        setText("My Turn!");
        setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬
        setVerticalAlignment(SwingConstants.CENTER);

        // 폰트 설정
        setFont(new Font("Arial", Font.BOLD, 30));
        setForeground(Color.RED); // 텍스트 색상

        // 크기 및 위치 설정
        this.setSize(200, 50);
        this.setLocation(510, 750);


        this.setVisible(false); // 초기 상태를 보이게 설정
    }

    public void showTurn(boolean visible) {
        System.out.println("Turn : " + visible);
        this.setVisible(visible);
    }
}
