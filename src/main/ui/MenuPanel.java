package ui;

import model.Enemy;
import model.Game;
import model.Position;

import javax.swing.*;
import java.awt.*;

// MenuPanel class is the menu panel of the game

public class MenuPanel extends JPanel {

    private TerminalGame terminalGame;

    // EFFECTS: Constructs the menu panel, adding all the necessary buttons and action listeners to said buttons.
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

    // EFFECTS: displays the inventory if pressed. If game ended, then do not do anything.
    public void displayInventory() {
        if (terminalGame.getGame().isEnded()) {
            terminalGame.requestFocusInWindow();
            return;
        }

        InventoryPanel inventoryPanel = new InventoryPanel(terminalGame, terminalGame.getGame().getInventory());
        inventoryPanel.setVisible(true);
        terminalGame.setPaused(true);
//        terminalGame.getGame().getInventory().displayInventory();
        terminalGame.requestFocusInWindow();
    }

    // EFFECTS: Changes the state of the game if pressed. If game ended, then do not do anything.
    public void changeState() {
        if (terminalGame.getGame().isEnded()) {
            terminalGame.requestFocusInWindow();
            return;
        }

        terminalGame.checkState();
        terminalGame.requestFocusInWindow();
    }

    // MODIFIES: game
    // EFFECTS: Saves the game if pressed. If game ended, then do not do anything.
    public void save() {
        if (terminalGame.getGame().isEnded()) {
            terminalGame.requestFocusInWindow();
            return;
        }

        terminalGame.saveGame();
        terminalGame.requestFocusInWindow();
    }

    // EFFECTS: Loads the game if pressed. If game ended, then do not do anything.
    public void load() {
        terminalGame.loadGame();
        terminalGame.requestFocusInWindow();
    }

    // EFFECTS: Adds an enemy to the list of enemies. If game ended, then do not do anything.
    public void enemyAdd() {
        Position pos = terminalGame.getGame().generateRandomPosition();
        terminalGame.getGame().getEnemies().add(new Enemy(pos));
        terminalGame.requestFocusInWindow();
    }

}
