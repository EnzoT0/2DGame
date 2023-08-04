package ui;

import model.Treasure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ItemActionListener implements ActionListener {

    private Treasure treasure;
    private JLabel imgLabel;
    private JButton useTreasure;
    private JPanel itemPanel;
    private JPanel listOfItemsPanel;
    private TerminalGame terminalGame;
    private InventoryPanel inventoryPanel;
    private JLabel notification;
    private int duration = 1500;

    Timer notificationTime = new Timer(duration, e -> {
        inventoryPanel.remove(notification);
        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    });

    Timer buttonCooldown = new Timer(1500, e -> useTreasure.setEnabled(true));

    public ItemActionListener(Treasure treasure, JLabel imgLabel, JButton useTreasure, JPanel itemPanel,
                              JPanel listOfItemsPanel, TerminalGame terminalGame, InventoryPanel inventoryPanel) {
        this.treasure = treasure;
        this.imgLabel = imgLabel;
        this.useTreasure = useTreasure;
        this.itemPanel = itemPanel;
        this.listOfItemsPanel = listOfItemsPanel;
        this.terminalGame = terminalGame;
        this.inventoryPanel = inventoryPanel;
        notification = new JLabel();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        useTreasure.setEnabled(false);

        if (treasure.getQuantity() > 0) {
            treasure.setQuantity(treasure.getQuantity() - 1);

            checkWhatTreasureDo(treasure);
            inventoryPanel.add(notification, BorderLayout.SOUTH);

            notificationTime.start();

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

        buttonCooldown.start();
    }


    public void checkWhatTreasureDo(Treasure treasure) {
        if (treasure.getName().equals("Bomb")) {
            doBomb();
        }

        if (treasure.getName().equals("Sword")) {
            doSword();
        }

        if (treasure.getName().equals("Twig")) {
            doTwig();
        }

        if (treasure.getName().equals("Health Pot")) {
            doHealthPot();
        }

        if (treasure.getName().equals("Meat")) {
            doMeat();
        }
    }

    public void doBomb() {
        if (terminalGame.getGame().getEnemies().size() == 0) {
            JLabel notification = new JLabel();
            notification.setText("Used a bomb but there are no enemies! Quantity remaining: " + treasure.getQuantity());
            notification.setFont(new Font("Arial", Font.BOLD, 15));
            this.notification = notification;
        } else {
            terminalGame.getGame().getEnemies().remove(terminalGame.getGame().getEnemies().size() - 1);
            JLabel notification = new JLabel();
            notification.setText("One enemy has been obliterated. Quantity remaining: " + treasure.getQuantity());
            notification.setFont(new Font("Arial", Font.BOLD, 15));
            this.notification = notification;
        }
    }

    public void doSword() {
        terminalGame.getGame().getCharacter().setAtk(terminalGame.getGame().getCharacter().getAtk() + 2);
        JLabel notification = new JLabel();
        notification.setText("Character Attack has risen by 2! Current Attack: " + terminalGame.getGame()
                .getCharacter().getAtk() + ". Quantity remaining: " + treasure.getQuantity());
        notification.setFont(new Font("Arial", Font.BOLD, 12));
        this.notification = notification;

    }

    public void doTwig() {
        terminalGame.getGame().getCharacter().setAtk(terminalGame.getGame().getCharacter().getAtk() + 1);
        JLabel notification = new JLabel();
        notification.setText("Character Attack has risen by 1! Current Attack: " + terminalGame.getGame()
                .getCharacter().getAtk() + ". Quantity Remaining: " + treasure.getQuantity());
        notification.setFont(new Font("Arial", Font.BOLD, 12));
        this.notification = notification;
    }

    public void doHealthPot() {
        if (terminalGame.getGame().getCharacter().getHp() >= 80) {
            terminalGame.getGame().getCharacter().setHp(100);
        } else {
            terminalGame.getGame().getCharacter().setHp(terminalGame.getGame().getCharacter().getHp() + 20);
        }
        JLabel notification = new JLabel();
        notification.setText("You drank a health pot! You have regenerated 20 HP! Quantity Remaining: "
                + treasure.getQuantity());
        notification.setFont(new Font("Arial", Font.BOLD, 12));

        this.notification = notification;
    }

    public void doMeat() {
        Random rand = new Random();
        int randNum = rand.nextInt(2);
        if (randNum == 0) {
            doUnluckyMeat();
        } else if (randNum == 1) {
            doLuckyMeat();
        }
    }

    public void doUnluckyMeat() {
        terminalGame.getGame().getCharacter().setHp(terminalGame.getGame().getCharacter().getHp() - 10);
        JLabel notification = new JLabel();
        notification.setText("Uh oh, you ate some poisoned meat! You have lost 10 HP! Quantity Remaining: "
                + treasure.getQuantity());
        notification.setFont(new Font("Arial", Font.BOLD, 10));

        this.notification = notification;
    }

    public void doLuckyMeat() {
        if (terminalGame.getGame().getCharacter().getHp() >= 90) {
            terminalGame.getGame().getCharacter().setHp(100);
        } else {
            terminalGame.getGame().getCharacter().setHp(terminalGame.getGame().getCharacter().getHp() + 10);
        }
        JLabel notification = new JLabel();
        notification.setText("You ate some meat! You have regenerated 10 HP! Quantity Remaining "
                + treasure.getQuantity());
        notification.setFont(new Font("Arial", Font.BOLD, 10));

        this.notification = notification;
    }
}
