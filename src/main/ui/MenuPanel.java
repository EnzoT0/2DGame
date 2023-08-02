package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private TerminalGame terminalGame;

    public MenuPanel(TerminalGame terminalGame) {
        this.terminalGame = terminalGame;
        setBackground(Color.white);

        setLayout(new GridLayout(5, 1, 0, 20));
        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.addActionListener(e -> displayInventory());
        add(inventoryButton);

        JButton pauseGame = new JButton("Pause Game");
        pauseGame.addActionListener(e -> changeState());
        add(pauseGame);

        JButton saveGame = new JButton("Save Game");
        saveGame.addActionListener(e -> save());
        add(saveGame);

        JButton loadGame = new JButton("Load Game");
        loadGame.addActionListener(e -> load());
        add(loadGame);

    }

    public void displayInventory() {
        InventoryPanel inventoryPanel = new InventoryPanel(terminalGame, terminalGame.getGame().getInventory());
        inventoryPanel.setVisible(true);
        terminalGame.setPaused(true);
        terminalGame.getGame().getInventory().displayInventory();
        terminalGame.requestFocusInWindow();
    }

    public void changeState() {
        terminalGame.checkState();
        terminalGame.requestFocusInWindow();
    }

    public void save() {
        terminalGame.saveGame();
        terminalGame.requestFocusInWindow();
    }

    public void load() {
        terminalGame.loadGame();
        terminalGame.requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
