package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private TerminalGame terminalGame;

    private int space = 5;

    public MenuPanel(TerminalGame terminalGame) {
        this.terminalGame = terminalGame;
        setBackground(Color.white);

        setLayout(new GridLayout(10, 1));
        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.addActionListener(e -> displayInventory());
        add(inventoryButton);

        add(Box.createHorizontalStrut(space));

        JButton pauseGame = new JButton("Pause Game");
        pauseGame.addActionListener(e -> changeState());
        add(pauseGame);
    }

    public void displayInventory() {
        System.out.println("displayed inventory");
        terminalGame.getGame().getInventory().displayInventory();
        terminalGame.requestFocusInWindow();
    }

    public void changeState() {
        terminalGame.checkState();
        terminalGame.requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
