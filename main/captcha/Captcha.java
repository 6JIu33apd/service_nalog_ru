package captcha;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Captcha {

    static ImageIcon img = new ImageIcon();
    static JLabel labelImg;
    static JFrame frame = new JFrame("CAPTCHA");
    static JPanel panel = new JPanel();
    static JLabel labelDate = new JLabel("Введите дату:");
    static JTextField textDate = new JTextField(10);
    static JButton buttonDate = new JButton("Отправить");
    static JLabel labelCaptcha = new JLabel("Введите цифры на картинке:");
    static JTextField textCaptcha = new JTextField(10);
    static JButton buttonCaptcha = new JButton("Отправить");
    static JButton buttonSuccess = new JButton("Успех");
    private static String captchaValue;
    private static String date;

    public static void setCaptcha(String captchaValue) {

        Captcha.captchaValue = captchaValue;
    }

    public static String getCaptcha() {

        return captchaValue;
    }

    public static void setDate(String date) {

        Captcha.date = date;
    }

    public static String getDate() {

        return date;
    }

    public static void guiDate() {

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        frame.setSize(300, 120);
        panel.add(labelDate);
        panel.add(textDate);
        panel.add(buttonDate);
        buttonDate.addActionListener((ActionEvent actionEvent) -> setDate(textDate.getText()));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void guiCaptcha(String state) {

        img = new ImageIcon("./main/img/captcha.jpg");
        img.getImage().flush();
        labelImg = new JLabel(img);
        if (state.equals("init")) {
            panel.add(labelImg);
            panel.add(labelCaptcha);
            panel.add(textCaptcha);
            panel.add(buttonCaptcha);
            buttonCaptcha.addActionListener((ActionEvent actionEvent) ->
                    setCaptcha(textCaptcha.getText()));
        }
        else if (state.equals("error")) {
            labelCaptcha.setText("Неверно! Введите еще раз:");
            textCaptcha.setText(null);
        }
        else {
            panel.removeAll();
            panel.add(buttonSuccess);
            buttonSuccess.addActionListener(((ActionEvent actionEvent) ->
                    frame.dispose()));
        }
        frame.setSize(300, 300);
        frame.repaint();
        frame.setVisible(true);
    }
}