package ui;

import javax.swing.*;
import java.awt.*;

public class Panels extends JPanel {

    final int screenWidth = 720;
    final int screenHeight = 576;

    public Panels() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));

        this.setBackground(Color.black);
        this.setLayout(new BorderLayout());
        TerminalGame terminalGame = new TerminalGame();

        MenuPanel menuPanel = new MenuPanel(terminalGame);

        add(menuPanel, BorderLayout.EAST);
        add(terminalGame, BorderLayout.CENTER);

        this.setDoubleBuffered(true);
        terminalGame.startGame();

    }
}
