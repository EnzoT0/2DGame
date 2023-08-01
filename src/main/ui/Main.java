package ui;

// Main method.

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Game");

        //TerminalGame terminalGame = new TerminalGame();
        Panels panels = new Panels();
        window.add(panels);
        //window.add(terminalGame);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        //terminalGame.startGame();

    }
}