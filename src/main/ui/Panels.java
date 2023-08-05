package ui;

import javax.swing.*;
import java.awt.*;

// Panels class that includes all the panels.

public class Panels extends JPanel {

    final int screenWidth = 720;
    final int screenHeight = 576;

    // EFFECTS: Constructs a new panel with the size, background color and everything else.
    // adds the menu panel and game panel as well.
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
