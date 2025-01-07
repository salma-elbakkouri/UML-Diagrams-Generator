package org.mql.java.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Frame extends JFrame {

    private static final long serialVersionUID = 1L;
    private String imagePath = "resources/img2.png";

    public Frame() {
        setTitle("Frame");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
