package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("MiniDF");

        GameWindow gameWindow = new GameWindow();
        frame.setContentPane(gameWindow);

        frame.pack();

        gameWindow.start();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
