package ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.*;
import model.Character;
import model.EnemyList;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// UI portion of the game, including the screen and the render and update portion of the game.

// Some parts of the code is taken from the TerminalGame class
// in the snake console project: https://github.students.cs.ubc.ca/CPSC210/SnakeConsole-Lanterna

public class TerminalGame {

    private Game game;
    private Screen screen;
    private WindowBasedTextGUI endGui;
    private final Random rand = new Random();
    private List<Enemy> enemies = new ArrayList<>();
    private int dx;
    private int dy;
    private int userInput;

    // MODIFIES: this, EnemyList
    // EFFECTS: Essentially starts the game, asking for user input on the amount of enemies as well as
    // creating the screen of the game. Begins the update function.
    public void start() throws IOException, InterruptedException {
        EnemyList enemyList = new EnemyList();

        Scanner scanner = new Scanner(System.in);
        System.out.println("How many enemies would you like from 1-10?"); // 1-10 for space purposes. Infinite works.

        while (true) {
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                if (userInput >= 1 && userInput <= 10) {
                    break;
                } else {
                    System.out.println("Invalid input. Please provide a value from 1-10.");
                }
            } else {
                System.out.println("Invalid input. Please provide an integer.");
                scanner.next();
            }
        }

        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();

        game = new Game(39, 21);

        enemyList.addEnemies(userInput);
        List<Enemy> numEnemy = enemyList.getEnemies();
        enemies = numEnemy;
        beginUpdate();
    }

    // EFFECTS: Begins the update when game has not ended.
    public void beginUpdate() throws IOException, InterruptedException {
        while (!game.isEnded() || endGui.getActiveWindow() != null) {
            update();
            Thread.sleep(1000 / Game.TPS);

        }
        System.exit(0);
    }

    // MODIFIES: this, Game
    // EFFECTS: Updates both the game and enemies, doing the user inputs too. Renders the game afterward.
    public void update() throws IOException {
        handleUserInput();

        game.update();
        enemyUpdate();


        screen.setCursorPosition(new TerminalPosition(0, 0));
        screen.clear();
        render();
        screen.refresh();

        screen.setCursorPosition(new TerminalPosition(screen.getTerminalSize().getColumns() - 1, 0));
    }

    // MODIFIES: Character
    // EFFECTS: Handles user input, asking only to move when an arrow key function has been inputted.
    // When inputted, move character to desired location. Handles user input in opening the inventory
    // system as well, displaying the treasures that the player has earned through the game.
    public void handleUserInput() throws IOException {
        KeyStroke stroke = screen.pollInput();
        if (stroke == null) {
            game.getCharacter().setDirection(Direction.NONE);
            return;
        }

        if (stroke.getCharacter() != null) {
            if (stroke.getCharacter() == 'i') {
                System.out.println("Opened Inventory");
                game.getInventory().displayInventory();
                game.getCharacter().setDirection(Direction.NONE);
            }
            game.getCharacter().setDirection(Direction.NONE);
        }

        Direction dir = directionFrom(stroke.getKeyType());
        if (dir == null) {
            return;
        }

        game.getCharacter().setDirection(dir);
    }

    // EFFECTS: Gets the direction of the input from the previous method.
    private Direction directionFrom(KeyType type) {
        switch (type) {
            case ArrowUp:
                return Direction.UP;
            case ArrowDown:
                return Direction.DOWN;
            case ArrowRight:
                return Direction.RIGHT;
            case ArrowLeft:
                return Direction.LEFT;
            default:
                return null;
        }
    }

    // MODIFIES: EnemyList
    // EFFECTS: Updates the enemy movement and sets its new position on the board. It has a
    // higher probability of not moving at all but there still is a chance of moving.
    // It also checks for character collision.
    public void enemyUpdate() {
        for (Enemy enemy : enemies) {
            if (rand.nextDouble() < 0.7) {
                dx = 0;
                dy = 0;
            } else {
                dx = rand.nextInt(3) - 1;
                dy = rand.nextInt(3) - 1;
            }
            int newPosX = enemy.getEnemyPos().getPosX() + dx;
            int newPosY = enemy.getEnemyPos().getPosY() + dy;
            if (newPosX >= 39) {
                newPosX = 39 - 1;
            } else if (newPosY >= 21) {
                newPosY = 21 - 1;
            } else if (newPosX < 0) {
                newPosX += 1;
            } else if (newPosY < 0) {
                newPosY += 1;
            }
            Position pos = new Position(newPosX, newPosY);
            enemy.setEnemyPos(pos);
            game.getCharacter().checkCollision(pos);
        }
    }


    // MODIFIES: this
    // EFFECTS: Renders the screen and draw all the graphics in the game. Draw the end screen if game ends.
    public void render() {
        if (game.isEnded()) {
            if (endGui == null) {
                drawEndScreen();
            }
            return;
        }

        drawCoinAmount();
        drawHp();
        drawCharacter();
        drawEnemies();
        drawCoin();
        drawTreasure();

    }

    // MODIFIES: this
    // EFFECTS: Draws the coins into the screen in the game using the drawPosition method.
    private void drawCoin() {
        for (Position coin : game.getCoin()) {
            drawPosition(coin, TextColor.ANSI.YELLOW, "c", true);
        }
    }

    // MODIFIES: this
    // EFFECTS: Draws the treasures into the screen in the game using the drawPosition method.
    private void drawTreasure() {
        for (Position treasure : game.getTreasures()) {
            drawPosition(treasure, TextColor.ANSI.BLUE, "t", true);
        }
    }

    // MODIFIES: this
    // EFFECTS: Draws the enemies into the screen in the game using the drawPosition method.
    private void drawEnemies() {
        for (Enemy enemy : enemies) {
            drawPosition(enemy.getEnemyPos(), TextColor.ANSI.RED, "x", true);
        }
    }

    // MODIFIES: this
    // EFFECTS: Draws the end screen once the game ends.
    private void drawEndScreen() {
        endGui = new MultiWindowTextGUI(screen);

        new MessageDialogBuilder().setTitle("Good Game!").setText("You Lost!")
                .addButton(MessageDialogButton.Close).build().showDialog(endGui);
    }

    // MODIFIES: this
    // EFFECTS: Draws the amount of coins earned through the game inside the screen.
    private void drawCoinAmount() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.YELLOW);
        text.putString(1, 0, "Coins: ");
        text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);
        text.putString(8, 0, String.valueOf(game.getCoinAmount()));
    }

    // MODIFIES: this
    // EFFECTS: Draws the amount of hp left throughout the game, showcasing when it decreases as well.
    private void drawHp() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.RED);
        text.putString(12, 0, "HP: ");
        text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);
        text.putString(16, 0, String.valueOf(game.getCharacter().getHp()));
    }

    // MODIFIES: this
    // EFFECTS: Draws the character into the screen in the game using the drawPosition method.
    private void drawCharacter() {
        Character character = game.getCharacter();
        drawPosition(character.getCharacterPos(), TextColor.ANSI.WHITE, "x", true);
    }

    // MODIFIES: this
    // EFFECTS: Main method used to draw the different objects into the screen in the game.
    // If it is wide, then it will draw the object twice to make it appear wide.
    // Note: Code referenced from snake console.
    private void drawPosition(Position pos, TextColor color, String c, boolean wide) {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(color);
        text.putString(pos.getPosX() * 2, pos.getPosY() + 1, c);

        if (wide) {
            text.putString(pos.getPosX() * 2 + 1, pos.getPosY() + 1, String.valueOf(c));
        }

    }
}