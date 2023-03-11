package com.example.chatting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Logo extends JFrame implements ActionListener{
    JPanel p1, p2;
    JLabel intro;
    JLabel lbImg;

    private Image background=new ImageIcon("images/logo_back.png").getImage();

    public Logo() {
        super();
        setTitle("Good Parking");
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLUE);
        setBounds(50, 450, 300, 400);
        setLayout(null);
        setBackground(new Color(0, 122, 255));

        setVisible(true);

    }

    public void paint(Graphics g) {//그리는 함수
        g.drawImage(background, 0, 75, null);//background를 그려줌
    }
    public static void main(String[] args){
        new Logo();
    }




    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
