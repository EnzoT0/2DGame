package ui;

import model.*;
import model.EnemyList;
import org.w3c.dom.css.Rect;
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

    private int currentEnemyHP;
    private int maxEnemyHP;

    private boolean isPaused = false;


    // EFFECTS: Constructs the game screen
    public TerminalGame() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));

        setBackground(Color.black);

        setDoubleBuffered(true);
        addKeyListener(keyHandler);

        addMultiPurposeCheck();
        
        setFocusable(true);
    }

    // EFFECTS: Starts the game.
    public void startGame() {

        //game = new Game(keyHandler);
        EnemyList enemyList = new EnemyList();
        enemyList.addEnemies(5);
        List<model.Enemy> numEnemy = enemyList.getEnemies();
        game.setEnemies(numEnemy);
        // add to menu panel how much enemies you would like.
        currentHP = 100;
        maxHP = 100;

        currentEnemyHP = 200;
        maxEnemyHP = 200;

        updateTimer();
    }

    // EFFECTS: initializes a timer that updates and repaints the game by 10 milliseconds.
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

    // MODIFIES: game
    // EFFECTS: updates all the game elements, including the character, enemy, projectile, etc.
    // Checks if a boss is on screen as well.
    private void update() {
        game.update();
        game.enemyUpdate();

        currentHP = game.getCharacter().getHp();
        if (game.getBoss().size() != 0) {
            currentEnemyHP = game.getBoss().get(0).getHp();
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;

        drawTopBar(g);
        drawCharacter(g);
        drawEnemy(g);
        drawCoin(g);
        drawTreasures(g);
        drawTile(g);
        if (game.getEnemies().isEmpty() && game.getBoss().isEmpty()) {
            drawYellowTile(g);
        }
        drawBoss(g);
        drawProjectile(g);

        drawBossProjectiles(g);

        if (game.getBoss().size() != 0) {
            drawBossEnemyBar(g);
        }

        if (game.isEnded()) {
            drawEndScreen(g);
        }


        g.dispose();

    }

    // MODIFIES: g
    // EFFECTS: draws the blue tile to the far end of the screen
    public void drawTile(Graphics2D g) {
        g.fillRect(585, 270, 25, 100);
    }

    // MODIFIES: g
    // EFFECTS: draws the yellow tile to the far end of the screen
    public void drawYellowTile(Graphics2D g) {
        g.setColor(Color.yellow);
        g.fillRect(585, 270, 25, 100);
    }

    // MODIFIES: g
    // EFFECTS: draws the boss onto the screen.
    public void drawBoss(Graphics2D g) {
        for (Enemy boss : game.getBoss()) {
            g.setColor(Color.RED);
            g.fillRect(boss.getEnemyPos().getPosX(), boss.getEnemyPos().getPosY(), 150, 150);
        }
    }

    // MODIFIES: g
    // EFFECTS: draws the boss's projectiles onto the screen.
    public void drawBossProjectiles(Graphics2D g) {
        for (Projectile bossProjectile : game.getBossProjectiles()) {
            g.setColor(Color.WHITE);
            g.fillOval(bossProjectile.getPos().getPosX() + 5, bossProjectile.getPos().getPosY(),
                    tileSize / 2, tileSize / 2);
        }
    }

    // EFFECTS: Adds another key listener into the game, when one presses escape, it pauses the game.
    // When game is over, press e to leave game.
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

    // MODIFIES: this
    // EFFECTS: Pauses the game if it isn't paused and vice versa.
    public void checkState() {
        if (isPaused) {
            isPaused = false;
        } else if (!isPaused) {
            isPaused = true;
        }
    }

    // MODIFIES: g
    // EFFECTS: Draws the end screen onto the game.
    public void drawEndScreen(Graphics2D g) {
        List<Enemy> noEnemy = new ArrayList<>();
        game.setEnemies(noEnemy);

        Position coinPos = new Position(1000000000, 10000000);
        game.getCoin().clear();
        game.getCoin().add(coinPos);

        Position treasurePos = new Position(200000000, 200000000);
        game.getTreasures().clear();
        game.getTreasures().add(treasurePos);

        game.getBoss().clear();
        game.getBossProjectiles().clear();

        g.setColor(Color.BLACK);
        g.fillRect(585, 270, 25, 100);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String gameOverText = "Game Over";
        String exitGame = "Press 'e' to exit the game";
        int width = g.getFontMetrics().stringWidth(gameOverText);
        int width2 = g.getFontMetrics().stringWidth(exitGame);
        g.drawString(gameOverText, (getWidth() - width) / 2, getHeight() / 2);
        g.drawString(exitGame, (getWidth() - width2) / 2, getHeight() / 2 + 50);
    }

    // MODIFIES: g
    // EFFECTS: Draws the user's projectiles onto the game.
    public void drawProjectile(Graphics2D g) {
        for (Projectile projectile : game.getProjectiles()) {
            g.setColor(Color.WHITE);
            g.fillOval(projectile.getPos().getPosX() + 5, projectile.getPos().getPosY(),
                    tileSize / 2, tileSize / 2);
        }
    }

    // MODIFIES: g
    // EFFECTS: Draws the character onto the game
    public void drawCharacter(Graphics2D g) {
        g.setColor(Color.white);
        g.fillRect(game.getCharacter().getCharacterPos().getPosX(),
                game.getCharacter().getCharacterPos().getPosY(), tileSize, tileSize);
    }

    // MODIFIES: this
    // EFFECTS: Draws the enemies onto the game.
    public void drawEnemy(Graphics2D g) {
        for (Enemy enemy : game.getEnemies()) {
            g.setColor(Color.red);
            g.fillRect(enemy.getEnemyPos().getPosX(),
                    enemy.getEnemyPos().getPosY(), tileSize, tileSize);
        }
    }

    // MODIFIES: g
    // EFFECTS: Draws the enemy boss bar if there is a boss onto the game.
    public void drawBossEnemyBar(Graphics2D g) {
        int barWidthEnemy = 250;
        int barHeightEnemy = 30;
        int barXEnemy = 350;
        int barYEnemy = 10;

        g.setColor(Color.GRAY);
        g.fillRect(barXEnemy, barYEnemy, barWidthEnemy, barHeightEnemy);

        double percentageEnemy = (double) currentEnemyHP / maxEnemyHP;

        int coloredWidthEnemy = (int) (barWidthEnemy * percentageEnemy);

        g.setColor(Color.RED);
        g.fillRect(barXEnemy, barYEnemy, coloredWidthEnemy, barHeightEnemy);
    }

    // MODIFIES: g
    // EFFECTS: Draws the coin onto the game.
    public void drawCoin(Graphics2D g) {
        for (Position coin : game.getCoin()) {
            g.setColor(Color.yellow);
            g.fillOval(coin.getPosX(), coin.getPosY(), tileSize / 2, tileSize / 2);
        }
    }

    // MODIFIES: g
    // EFFECTS: Draws the treasure onto the game.
    public void drawTreasures(Graphics2D g) {
        for (Position treasure : game.getTreasures()) {
            g.setColor(Color.BLUE);
            g.fillRect(treasure.getPosX(), treasure.getPosY(), tileSize / 2, tileSize / 2);
        }
    }

    // MODIFIES: g
    // EFFECTS: Draws the top part of the screen onto the game.
    public void drawTopBar(Graphics2D g) {
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, 720, 50);
/*        g.setColor(Color.BLUE);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.drawString("HP: " + currentHP, 275, 30);

        g.setColor(Color.RED);
        g.drawString("ATK: " + game.getCharacter().getAtk(), 375, 30);*/

        int barWidth = 250;
        int barHeight = 30;
        int barX = 15;
        int barY = 10;

        g.setColor(Color.GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        double percentage = (double) currentHP / maxHP;

        int coloredWidth = (int) (barWidth * percentage);

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

    // MODIFIES: game
    // EFFECTS: Loads a saved version of the game
    public void loadGame() {
        try {
            game = jsonReader.loadGame(keyHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: Return the game.
    public Game getGame() {
        return game;
    }

    // MODIFIES: this
    // EFFECTS: sets whether the game is paused or not to the specified boolean.
    public void setPaused(Boolean b) {
        isPaused = b;
    }

}