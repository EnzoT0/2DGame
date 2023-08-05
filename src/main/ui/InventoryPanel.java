package ui;

import model.Treasure;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Random;

// Some parts of the code is inspired from the PlayDrawingTool class in the DrawingPlayer project (setImageItemPanel):
// https://github.students.cs.ubc.ca/CPSC210/SimpleDrawingPlayer-Complete/blob/main/src/ui/tools/PlayDrawingTool.java

// Inventory panel of the game.

public class InventoryPanel extends JFrame {
    private TerminalGame terminalGame;
    private Inventory inventory;
    private JPanel inventoryPanel;
    private JPanel listOfItemsPanel;
    private JLabel coinImageLabel;
    private JPanel coinPanel;
    private JLabel imgLabel;
    private JButton useTreasure;
    private JPanel itemPanel;
    private JLabel notification;

    // EFFECTS: Constructs the inventory panel with the given parameters. Adds whatever treasure the
    // user currently has as a button and image.
    public InventoryPanel(TerminalGame terminalGame, Inventory inventory) {
        this.terminalGame = terminalGame;
        this.inventory = inventory;

        setTitle("Inventory");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                terminalGame.setPaused(false);
            }
        });

        setPreferredSize(new Dimension(500, 300));
        setLayout(new BorderLayout());

        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BorderLayout());

        listOfItemsPanel = new JPanel(new GridLayout(2, 0, 10, 10));

        add(inventoryPanel, BorderLayout.CENTER);

        setImageItemPanel();

        inventoryPanel.add(listOfItemsPanel, BorderLayout.CENTER);

        pack();

        setLocationRelativeTo(terminalGame);

    }

    // MODIFIES: this
    // EFFECTS: Sets the image and button of the treasure and puts them inside a panel.
    public void setImageItemPanel() {
        createCoin();
        List<Treasure> treasures = inventory.getTreasures();
        for (Treasure treasure : treasures) {
            if (treasure.getQuantity() > 0) {
                String image = treasure.getName().toLowerCase() + ".png";
                ImageIcon imageIcon = new ImageIcon("./data/" + image);

                Image scaled = imageIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(scaled);
                imgLabel = new JLabel(imageIcon);

                itemPanel = new JPanel(new BorderLayout());
                itemPanel.add(imgLabel, BorderLayout.CENTER);

                useTreasure = new JButton("Use " + treasure.getName());

                useTreasure.addActionListener(new ItemActionListener(treasure,
                        imgLabel, useTreasure, itemPanel, listOfItemsPanel, terminalGame, this));

                itemPanel.add(useTreasure, BorderLayout.SOUTH);

                listOfItemsPanel.add(itemPanel);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates the coin image and sets the image to the panel.
    public void createCoin() {
        coinPanel = new JPanel(new BorderLayout());
        ImageIcon coinImageIcon = new ImageIcon("./data/coin.png");
        Image coinScaled = coinImageIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        coinImageIcon = new ImageIcon(coinScaled);
        coinImageLabel = new JLabel(String.valueOf(terminalGame.getGame().getCoinAmount()),
                coinImageIcon, JLabel.CENTER);
        coinPanel.add(coinImageLabel, BorderLayout.CENTER);
        listOfItemsPanel.add(coinPanel);
    }

}
