package model;

import ui.Inventory;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// Game represents the state of the game, including the update method which is used to update the game.
// It also includes what items are added and how to check them.

// Some parts of the code is referenced from the Game class in the snake console project (Update):
// https://github.students.cs.ubc.ca/CPSC210/SnakeConsole-Lanterna/blob/main/src/main/java/com/mazenk/snake/model/Game.java

// Map is arrayList

public class Game {

    private final Character character = new Character();
    public static final int TPS = 10;
    private boolean ended = false;
    private final int maxX;
    private final int maxY;
    private Set<Position> coin = new HashSet<>();
    private Set<Position> treasures = new HashSet<>();
    private int coinAmount;
    private Treasure treasure = new Treasure("None");
    private Inventory inventory = new Inventory();

    private Position coinPos;
    private Position coinPosStart;
    private Position checkCoinPos;
    private Position spawnTreasurePos;
    private Position checkTreasurePos;

    private int dx;
    private int dy;
    private final Random rand = new Random();
    private List<Enemy> enemies = new ArrayList<>();

    private boolean isRand;
    private int randDx;
    private int randDy;


    // EFFECTS: Constructs a game with a max X and max Y.
    // Generates a coin at a random location in the board.
    public Game(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;

        Position pos = generateRandomPosition();
        coin.add(pos);
        coinPosStart = pos;
    }

    // MODIFIES: this, character
    // EFFECTS: Progresses the game state, moving the character, adding an infinite amount of coins and treasures
    // and handling them. Checks how the game ends as well when hp = 0.
    public void update() {
        character.move();
        enemyUpdate();

        checkCoin();

        if (coin.isEmpty()) {
            spawnCoins();
        }

        checkTreasure();

        if (treasures.isEmpty()) {
            spawnTreasure();
        }

        if (getCharacter().getHp() <= 0) {
            ended = true;
        }
    }

    // MODIFIES: enemies
    // EFFECTS: Updates the enemy movement and sets its new position on the board. It has a
    // higher probability of not moving at all but there still is a chance of moving.
    // It also checks for character collision.
    public void enemyUpdate() {
        for (Enemy enemy : enemies) {
            checkRandPosUpdate();
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
            getCharacter().checkCollision(pos);
        }
    }

    // MODIFIES: this
    // EFFECTS: If rand.nextDouble() is less than 70%, then position will not move, else it will move from
    // -1 to 1, depending on the random number it gets.
    public void checkRandPosUpdate() {
        if (rand.nextDouble() < 0.7) {
            dx = 0;
            dy = 0;
            isRand = true;
        } else {
            randDx = rand.nextInt(3) - 1;
            randDy = rand.nextInt(3) - 1;
            dx = randDx;
            dy = randDy;
            isRand = false;
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
    public void checkCoin() {
        Position coinsEarned = coin.stream().filter(character::hasCollided).findFirst().orElse(null);
        if (coinsEarned == null) {
            return;
        }

        coin.remove(coinsEarned);
        coinAmount++;
        checkCoinPos = coinsEarned;


    }

    // MODIFIES: this, treasure
    // EFFECTS: Checks for treasures that the character has collided. If there is one, then remove it
    // from the set and thus the board. Adds the treasure to the inventory.
    public void checkTreasure() {
        Position checkTreasures = treasures.stream().filter(character::hasCollided).findFirst().orElse(null);
        if (checkTreasures == null) {
            return;
        }

        treasures.remove(checkTreasures);
        inventory.addTreasure(new Treasure(treasure.whatTreasure()));
        checkTreasurePos = checkTreasures;
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

    // MODIFIES: this
    // EFFECTS: sets the enemy list to the given input.
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    // MODIFIES: this
    // EFFECTS: sets the inventory to the given input.
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    // EFFECTS: returns the enemy list.
    public List<Enemy> getEnemies() {
        return enemies;
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

    // EFFECTS: returns dx.
    public int getDx() {
        return dx;
    }

    // EFFECTS: returns dy.
    public int getDy() {
        return dy;
    }

    // EFFECTS: returns whether isRand is true or false.
    public boolean isRand() {
        return isRand;
    }

    // EFFECTS: Returns the random Dx.
    public int getRandDx() {
        return randDx;
    }

    // EFFECTS: Returns the random Dy.
    public int getRandDy() {
        return randDy;
    }

    // MODIFIES: this
    // EFFECTS: sets the set of treasures to the given input.
    public void setTreasures(Set<Position> treasures) {
        this.treasures = treasures;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the treasure spawn to the given input.
    public void setSpawnTreasurePos(Position pos) {
        spawnTreasurePos = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the checkTreasure method to the given input.
    public void setCheckTreasurePos(Position pos) {
        checkTreasurePos = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the starting coin position to the given input.
    public void setCoinPosStart(Position pos) {
        coinPosStart = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the spawned coin to the given input.
    public void setCoinPos(Position pos) {
        coinPos = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the checkCoin method to the given input.
    public void setCheckCoinPos(Position pos) {
        checkCoinPos = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the coin amount into the given input.
    public void setCoinAmount(Integer amount) {
        coinAmount = amount;
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

    // EFFECTS: returns the coinsEarned position from the checkCoinPos method.
    public Position getCheckCoinPos() {
        return checkCoinPos;
    }

    // EFFECTS: returns the treasure position, mainly for testing purposes.
    public Position getSpawnTreasurePos() {
        return spawnTreasurePos;
    }

    // EFFECTS: returns the position of checkTreasures in checkTreasure. Mainly for testing purposes.
    public Position getCheckTreasurePos() {
        return checkTreasurePos;
    }
}