import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GameSettingDialog extends JDialog {
    private JTextField rowField;
    private JTextField columnField;
    private JLabel image1Label;
    private JLabel image2Label;
    private File image1File;
    private File image2File;
    private boolean confirmed = false;

    public GameSettingDialog(JFrame qurridorUI) {
        super(qurridorUI,"Setting",true);
        setLayout(null);
        setSize(400, 300);
        setResizable(false);

        JLabel rowLabel = new JLabel("행:");
        rowLabel.setBounds(30, 20, 50, 30);
        add(rowLabel);

        rowField = new JTextField();
        rowField.setBounds(90, 20, 100, 30);
        add(rowField);

        JLabel columnLabel = new JLabel("열:");
        columnLabel.setBounds(30, 60, 50, 30);
        add(columnLabel);

        columnField = new JTextField();
        columnField.setBounds(90, 60, 100, 30);
        add(columnField);

        JLabel image1TextLabel = new JLabel("이미지1:");
        image1TextLabel.setBounds(30, 100, 50, 30);
        add(image1TextLabel);

        image1Label = new JLabel("경로 미선택");
        image1Label.setBounds(90, 100, 200, 30);
        add(image1Label);

        JButton image1Button = new JButton("선택");
        image1Button.setBounds(300, 100, 70, 30);

        add(image1Button);

        JLabel image2TextLabel = new JLabel("이미지2:");
        image2TextLabel.setBounds(30, 140, 50, 30);
        add(image2TextLabel);

        image2Label = new JLabel("경로 미선택");
        image2Label.setBounds(90, 140, 200, 30);
        add(image2Label);

        JButton image2Button = new JButton("선택");
        image2Button.setBounds(300, 140, 70, 30);

        add(image2Button);

        JButton confirmButton = new JButton("확인");
        confirmButton.setBounds(100, 200, 80, 30);
        add(confirmButton);

        JButton cancelButton = new JButton("종료");
        cancelButton.setBounds(200, 200, 80, 30);
        add(cancelButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        image1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImageFile(1);
            }
        });
        image2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImageFile(2);
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmInput();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelInput();
            }
        });


    }

    private void selectImageFile(int imageNumber) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and JPG Images", "png", "jpg");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (imageNumber == 1) {
                image1File = selectedFile;
                image1Label.setText(selectedFile.getAbsolutePath());
            } else {
                image2File = selectedFile;
                image2Label.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    private void confirmInput() {
        try {
            int row = Integer.parseInt(rowField.getText());
            int column = Integer.parseInt(columnField.getText());

            if (column % 2 == 0) {
                JOptionPane.showMessageDialog(this, "열은 반드시 홀수여야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (image1File == null || image2File == null) {
                JOptionPane.showMessageDialog(this, "모든 이미지를 선택해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            confirmed = true;
            dispose();
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "행과 열은 숫자여야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelInput() {
        confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getRow() {
        return rowField.getText();
    }

    public String getColumn() {
        return columnField.getText();
    }

    public File getImage1File() {
        return image1File;
    }

    public File getImage2File() {
        return image2File;
    }

}
