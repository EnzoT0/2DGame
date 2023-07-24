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
import persistence.JsonReader;
import persistence.JsonWriter;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// UI portion of the game, including the screen and the render and update portion of the game.

// Some parts of the code is referenced from the TerminalGame class in the snake console project (drawPosition):
// https://github.students.cs.ubc.ca/CPSC210/SnakeConsole-Lanterna/blob/main/src/main/java/com/mazenk/snake/ui/TerminalGame.java

public class TerminalGame {

    private Game game;
    private Screen screen;
    private WindowBasedTextGUI endGui;

    private int userInput;
    private boolean isLoaded;

    private JsonWriter jsonWriter = new JsonWriter(SAVE_FILE_PATH);
    private JsonReader jsonReader = new JsonReader(SAVE_FILE_PATH);
    private static final String SAVE_FILE_PATH = "./data/game_save.json";

    // MODIFIES: this, EnemyList
    // EFFECTS: Essentially starts the game, asking for user input on the amount of enemies as well as
    // creating the screen of the game. Begins the update function.
    public void start() throws IOException, InterruptedException {
        EnemyList enemyList = new EnemyList();

        yesOrNoGame();
        if (isLoaded == true) {
            loadGame();
            instructions();
            screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            System.out.println("Loaded a new game!");
            beginUpdate();
        } else {
            askAmountEnemies();

            screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();

            game = new Game(39, 21);

            enemyList.addEnemies(userInput);
            List<Enemy> numEnemy = enemyList.getEnemies();
            game.setEnemies(numEnemy);
            //enemies = enemyList.getEnemies();
            instructions();
            beginUpdate();
        }

    }

    // EFFECTS: asks whether user would like to load a saved game. If yes, then load a saved version
    // of the game. If no, then continue normally.
    public void yesOrNoGame() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to load a saved game? yes or no");

        while (true) {
            String userInput = scan.next();
            if (userInput.equals("yes")) {
                isLoaded = true;
                break;
            } else if (userInput.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'");
            }
        }
    }

    // EFFECTS: asks the user about how many enemies they would like to have.
    public void askAmountEnemies() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many enemies would you like?");

        while (true) {
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                if (userInput >= 0) {
                    break;
                } else {
                    System.out.println("Invalid input. Please provide a value that is above 0.");
                }
            } else {
                System.out.println("Invalid input. Please provide an integer.");
                scanner.next();
            }
        }
    }

    // EFFECTS: Gives the instructions of the game.
    public void instructions() {
        System.out.println("Press the arrow keys to move!");
        System.out.println("Press 'i' to access your inventory");
        System.out.println("Press 's' to save the game");
    }

    // EFFECTS: Begins the update when game has not ended.
    public void beginUpdate() throws IOException, InterruptedException {
        while (!game.isEnded() || endGui.getActiveWindow() != null) {
            update();
            Thread.sleep(1000 / Game.TPS);

        }
        System.exit(0);
    }

    // MODIFIES: this, game
    // EFFECTS: Updates both the game and enemies, doing the user inputs too. Renders the game afterward.
    public void update() throws IOException {
        handleUserInput();

        game.update();
        game.enemyUpdate();


        screen.setCursorPosition(new TerminalPosition(0, 0));
        screen.clear();
        render();
        screen.refresh();

        screen.setCursorPosition(new TerminalPosition(screen.getTerminalSize().getColumns() - 1, 0));
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
            game = jsonReader.loadGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // MODIFIES: character
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
            if (stroke.getCharacter() == 's') {
                System.out.println("Saved the game!");
                saveGame();
            }
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
        for (Enemy enemy : game.getEnemies()) {
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
            text.putString(pos.getPosX() * 2 + 1, pos.getPosY() + 1, c);
        }

    }
}