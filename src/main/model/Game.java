package model;

import ui.Inventory;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// Game represents the state of the game, including the update method which is used to update the game.
// It also includes what items are added and how to handle them.

public class Game {

    private final Character character = new Character();
    public static final int TPS = 10;
    private boolean ended = false;
    private final int maxX;
    private final int maxY;
    private final Set<Position> coin = new HashSet<>();
    private final Set<Position> treasures = new HashSet<>();
    private int coinAmount;
    private Treasure treasure = new Treasure("None");
    private Inventory inventory = new Inventory();

    private Position coinPos;
    private Position coinPosStart;
    private Position handleCoinPos;
    private Position spawnTreasurePos;
    private Position handleTreasurePos;



    // EFFECTS: Constructs a game with a max X and max Y.
    // Generates 2 different coin, each at a different location in the board.
    public Game(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;

        Position pos = generateRandomPosition();
        coin.add(pos);
        coinPosStart = pos;
    }

    // MODIFIES: this, Character
    // EFFECTS: Progresses the game state, moving the character, adding an infinite amount of coins and treasures
    // and handling them. Handles how the game ends as well when hp = 0.
    public void update() {
        character.move();

        handleCoin();

        if (coin.isEmpty()) {
            spawnCoins();
        }

        handleTreasure();

        if (treasures.isEmpty()) {
            spawnTreasure();
        }

        if (getCharacter().getHp() == 0) {
            ended = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: Spawns a new coin into a valid position in the game.
    // Note: Code inspired from snake console.
    public void spawnCoins() {
        Position pos = generateRandomPosition();

        makeValidRandomPos(pos);

        coin.add(pos);
        coinPos = pos;
    }

    // MODIFIES: this
    // EFFECTS: Spawns a new treasure into a valid position in the game.
    // Note: Code inspired from snake console.
    public void spawnTreasure() {
        Position pos = generateRandomPosition();

        makeValidRandomPos(pos);

        treasures.add(pos);
        spawnTreasurePos = pos;
    }

    // REQUIRES: 39 >= x >= 0 and 21 >= y >= 0 for the position.
    // EFFECTS: Makes a valid random position.
    public Position makeValidRandomPos(Position pos) {
        while (!isValidPosition(pos)) {
            pos = generateRandomPosition();
        }
        return pos;
    }


    // MODIFIES: this
    // EFFECTS: Checks for coins that the character has collided. If there is one, then remove it from the
    // set and board. Add to coin score.
    // Note: Code referenced from snake console.
    public void handleCoin() {
        Position coinsEarned = coin.stream().filter(character::hasCollided).findFirst().orElse(null);
        if (coinsEarned == null) {
            return;
        }

        coin.remove(coinsEarned);
        coinAmount++;
        handleCoinPos = coinsEarned;


    }

    // MODIFIES: this, Treasure
    // EFFECTS: Checks for treasures that the character has collided. If there is one, then remove it
    // from the set and thus the board. Adds the treasure to the inventory.
    public void handleTreasure() {
        Position checkTreasures = treasures.stream().filter(character::hasCollided).findFirst().orElse(null);
        if (checkTreasures == null) {
            return;
        }

        treasures.remove(checkTreasures);
        inventory.addTreasure(new Treasure(treasure.whatTreasure()));
        handleTreasurePos = checkTreasures;
    }


    // EFFECTS: Generates a random position, which is guaranteed to be in bounds but not necessary valid.
    public Position generateRandomPosition() {
        return new Position(ThreadLocalRandom.current().nextInt(40),
                ThreadLocalRandom.current().nextInt(22));
    }

    // REQUIRES: 39 >= x >= 0 and 21 >= y >= 0 for the position.
    // EFFECTS: Returns whether a given position is in the boundary and not occupied.
    public boolean isValidPosition(Position pos) {
        return !outOfBoundary(pos)
                && !coin.contains(pos)
                && !treasures.contains(pos)
                && !character.hasCollided(pos);
    }

    // EFFECTS: Returns true when a position is out of boundary.
    public boolean outOfBoundary(Position pos) {
        return pos.getPosX() < 0 || pos.getPosY() < 0 || pos.getPosX() > maxX || pos.getPosY() > maxY;
    }

    // EFFECTS: returns the character.
    public Character getCharacter() {
        return character;
    }

    // EFFECTS: returns the inventory with the treasures.
    public Inventory getInventory() {
        return inventory;
    }

    // EFFECTS: returns whether game has ended or not.
    public boolean isEnded() {
        return ended;
    }

    // EFFECTS: returns the position of the coins in the board.
    public Set<Position> getCoin() {
        return coin;
    }

    // EFFECTS: returns the position of the treasures in the board.
    public Set<Position> getTreasures() {
        return treasures;
    }

    // EFFECTS: returns the amount of coins the player has earned throughout the game.
    public int getCoinAmount() {
        return coinAmount;
    }

    // EFFECTS: returns maxX
    public int getMaxX() {
        return maxX;
    }

    // EFFECTS: returns maxY.
    public int getMaxY() {
        return maxY;
    }

    // EFFECTS: returns coin position, mainly for testing purposes.
    public Position getCoinPos() {
        return coinPos;
    }

    // EFFECTS: returns the starting position of the coin, mainly for testing purposes.
    public Position getCoinPosStart() {
        return coinPosStart;
    }

    // EFFECTS: returns the coinsEarned position from the handleCoin method.
    public Position getHandleCoinPos() {
        return handleCoinPos;
    }

    // EFFECTS: returns the treasure position, mainly for testing purposes.
    public Position getSpawnTreasurePos() {
        return spawnTreasurePos;
    }

    // EFFECTS: returns the position of checkTreasures in handleTreasure. Mainly for testing purposes.
    public Position getHandleTreasurePos() {
        return handleTreasurePos;
    }
}