package ui;

import model.*;
import model.EnemyList;
import persistence.JsonReader;
import persistence.JsonWriter;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// UI portion of the game, including the screen and the render and update portion of the game.

// Some parts of the code is referenced from the SpaceInvaders class in the SpaceInvadersBase project (addTimer):
// https://github.students.cs.ubc.ca/CPSC210/SnakeConsole-Lanterna/blob/main/src/main/java/com/mazenk/snake/ui/TerminalGame.java

public class TerminalGame extends JPanel {

    private JsonWriter jsonWriter = new JsonWriter(SAVE_FILE_PATH);
    private JsonReader jsonReader = new JsonReader(SAVE_FILE_PATH);
    private static final String SAVE_FILE_PATH = "./data/game_save.json";

    final int tileSize = 36;

    final int screenWidth = 720;
    final int screenHeight = 576;

    GameKeyHandler keyHandler = new GameKeyHandler();

    private Game game = new Game(keyHandler);
    private int currentHP;
    private int maxHP;

    private boolean isPaused = false;

    public TerminalGame() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));

        this.setBackground(Color.black);

        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);

        addMultiPurposeCheck();
        
        this.setFocusable(true);
    }

    public void startGame() {

        //game = new Game(keyHandler);
        EnemyList enemyList = new EnemyList();
        enemyList.addEnemies(5);
        List<model.Enemy> numEnemy = enemyList.getEnemies();
        game.setEnemies(numEnemy);
        // add to menu panel how much enemies you would like.
        currentHP = 100;
        maxHP = 100;

        updateTimer();
    }

    // Note: Code referenced from M02-SpaceInvadersBase
    public void updateTimer() {
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    update();
                }
                repaint();
            }
        });

        timer.start();
    }

    private void update() {
        game.update();
        game.enemyUpdate();

        currentHP = game.getCharacter().getHp();
    }


    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;

        drawHpBar(g);
        drawCharacter(g);
        drawEnemy(g);
        drawCoin(g);
        drawTreasures(g);
        drawProjectile(g);

/*        if (menuOpen) {
            menuPanel.paintComponent(g);
        }*/

        if (game.isEnded()) {
            drawEndScreen(g);
        }

        g.dispose();

    }

    public void addMultiPurposeCheck() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    checkState();
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    if (game.isEnded()) {
                        System.exit(0);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
    }

    public void checkState() {
        if (isPaused) {
            isPaused = false;
        } else if (!isPaused) {
            isPaused = true;
        }
    }

    public void drawEndScreen(Graphics2D g) {
        List<Enemy> noEnemy = new ArrayList<>();
        game.setEnemies(noEnemy);

        Position coinPos = new Position(1000000000, 10000000);
        game.getCoin().clear();
        game.getCoin().add(coinPos);

        Position treasurePos = new Position(200000000, 200000000);
        game.getTreasures().clear();
        game.getTreasures().add(treasurePos);

        // can set character position to who knows where as well LOL.

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String gameOverText = "Game Over";
        String exitGame = "Press 'e' to exit the game";
        int width = g.getFontMetrics().stringWidth(gameOverText);
        int width2 = g.getFontMetrics().stringWidth(exitGame);
        g.drawString(gameOverText, (getWidth() - width) / 2, getHeight() / 2);
        g.drawString(exitGame, (getWidth() - width2) / 2, getHeight() / 2 + 50);
    }

    public void drawProjectile(Graphics2D g) {
        for (Projectile projectile : game.getProjectiles()) {
            g.setColor(Color.WHITE);
            g.fillOval(projectile.getPos().getPosX() + 5, projectile.getPos().getPosY(),
                    tileSize / 2, tileSize / 2);
        }
    }


    public void drawCharacter(Graphics2D g) {
        g.setColor(Color.white);
        g.fillRect(game.getCharacter().getCharacterPos().getPosX(),
                game.getCharacter().getCharacterPos().getPosY(), tileSize, tileSize);
    }

    public void drawEnemy(Graphics2D g) {
        for (model.Enemy enemy : game.getEnemies()) {
            g.setColor(Color.red);
            g.fillRect(enemy.getEnemyPos().getPosX(),
                    enemy.getEnemyPos().getPosY(), tileSize, tileSize);
        }
    }

    public void drawCoin(Graphics2D g) {
        for (Position coin : game.getCoin()) {
            g.setColor(Color.yellow);
            g.fillOval(coin.getPosX(), coin.getPosY(), tileSize / 2, tileSize / 2);
        }
    }

    public void drawTreasures(Graphics2D g) {
        for (Position treasure : game.getTreasures()) {
            g.setColor(Color.BLUE);
            g.fillRect(treasure.getPosX(), treasure.getPosY(), tileSize / 2, tileSize / 2);
        }
    }

    public void drawHpBar(Graphics2D g) {
        // Draw the HP bar here, similar to the previous HPBarExample's paint method
        int barWidth = 300;
        int barHeight = 30;
        int barX = 15;
        int barY = 10;

        // background of hp
        g.setColor(Color.GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        // Percentage of hp
        double percentage = (double) currentHP / maxHP;

        // width of part of the bar based on the percentage
        int coloredWidth = (int) (barWidth * percentage);

        // green part of HP
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, coloredWidth, barHeight);

    }


    // EFFECTS: saves the game into a file.
    public void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGame() {
        try {
            game = jsonReader.loadGame(keyHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Game getGame() {
        return game;
    }

}