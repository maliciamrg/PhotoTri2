package com.malicia.mrg.view;

import com.malicia.mrg.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class app {
    public app() {
        hidden1.setVisible(false);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.changeimage();
            }
        });
        lb1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Main.popUpName();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }
        });
    }

    public JPanel getJpane() {
        return jpane;
    }

    public JLabel getLb1() {
        return lb1;
    }

    private JPanel jpane;

    public void setLb1(JLabel lb1) {
        this.lb1 = lb1;
    }

    private JLabel lb1;

    public JButton getButton1() {
        return button1;
    }

    private JButton button1;

    public JLabel getHidden1() {
        return hidden1;
    }

    private JLabel hidden1;
}
