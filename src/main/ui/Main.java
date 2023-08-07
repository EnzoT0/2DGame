package ui;

// Main method.

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                EventLog el = EventLog.getInstance();
                for (Event next : el) {
                    System.out.println(next.getDate() + " " + next.getDescription());
                }
                System.exit(0);
            }
        });
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