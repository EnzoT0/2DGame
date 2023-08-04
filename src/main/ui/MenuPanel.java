package ui;

import model.Enemy;
import model.Game;
import model.Position;

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

        JButton addEnemy = new JButton("Add an enemy");
        addEnemy.addActionListener(e -> enemyAdd());
        add(addEnemy);

    }
    
    public void displayInventory() {
        if (terminalGame.getGame().isEnded()) {
            terminalGame.requestFocusInWindow();
            return;
        }

        InventoryPanel inventoryPanel = new InventoryPanel(terminalGame, terminalGame.getGame().getInventory());
        inventoryPanel.setVisible(true);
        terminalGame.setPaused(true);
        terminalGame.getGame().getInventory().displayInventory();
        terminalGame.requestFocusInWindow();
    }

    public void changeState() {
        if (terminalGame.getGame().isEnded()) {
            terminalGame.requestFocusInWindow();
            return;
        }

        terminalGame.checkState();
        terminalGame.requestFocusInWindow();
    }

    public void save() {
        if (terminalGame.getGame().isEnded()) {
            terminalGame.requestFocusInWindow();
            return;
        }

        terminalGame.saveGame();
        terminalGame.requestFocusInWindow();
    }

    public void load() {
        terminalGame.loadGame();
        terminalGame.requestFocusInWindow();
    }

    public void enemyAdd() {
        Position pos = terminalGame.getGame().generateRandomPosition();
        terminalGame.getGame().getEnemies().add(new Enemy(pos));
        terminalGame.requestFocusInWindow();
    }

}
