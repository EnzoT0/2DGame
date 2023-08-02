package ui;

import model.Treasure;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemActionListener implements ActionListener {

    private Treasure treasure;
    private JLabel imgLabel;
    private JButton useTreasure;
    private JPanel itemPanel;
    private JPanel listOfItemsPanel;
    private TerminalGame terminalGame;

    public ItemActionListener(Treasure treasure, JLabel imgLabel, JButton useTreasure, JPanel itemPanel,
                              JPanel listOfItemsPanel, TerminalGame terminalGame) {
        this.treasure = treasure;
        this.imgLabel = imgLabel;
        this.useTreasure = useTreasure;
        this.itemPanel = itemPanel;
        this.listOfItemsPanel = listOfItemsPanel;
        this.terminalGame = terminalGame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (treasure.getQuantity() > 0) {
            treasure.setQuantity(treasure.getQuantity() - 1);

            // methods here

            if (treasure.getQuantity() == 0) {
                itemPanel.remove(imgLabel);
                itemPanel.remove(useTreasure);
                listOfItemsPanel.remove(itemPanel);
            }
        }

        itemPanel.revalidate();
        itemPanel.repaint();
        listOfItemsPanel.revalidate();
        listOfItemsPanel.repaint();
    }
}
