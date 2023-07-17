package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    @BeforeEach
    void runBefore() {
        game = new Game(39, 21);
    }

    @Test
    void testConstructor() {
        assertEquals(39, game.getMaxX());
        assertEquals(21, game.getMaxY());

        assertEquals(1, game.getCoin().size());
    }

    @Test
    void testUpdate() {
        // character.move() has already been tested at the characterTest class.

        // Check if coin is in game.
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));

        // Check 1st update, coin not in starting position of character.
        game.update();
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(1, game.getCoin().size());

        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertEquals(0, game.getCoinAmount());
        assertFalse(game.isEnded());

        // Check 2nd update, this time character collided with coin.
        game.getCharacter().setDirection(Direction.NONE);
        game.getCharacter().setCharacterPos(game.getCoinPosStart());
        game.update();
        assertFalse(game.getCoin().contains(game.getCoinPosStart())); // Shows coin is not in set anymore

        assertTrue(game.getCoin().contains(game.getCoinPos())); // Spawn coin added a new coin into the set.
        assertEquals(1, game.getCoin().size());
        assertFalse(game.getCoinPosStart() == game.getCoinPos());
        assertEquals(1, game.getCoinAmount());

        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertFalse(game.isEnded());

        // Check 3rd update, this time character collided with the spawned coin.
        game.getCharacter().setCharacterPos(game.getCoinPos());
        game.update();
        assertFalse(game.getCoin().contains(game.getHandleCoinPos())); // Update changes coinPos to newSpawn CoinPos.
        assertEquals(1, game.getCoin().size());

        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertFalse(game.getCoinPos() == game.getHandleCoinPos()); // Shows that it's now a different position.
        assertEquals(2, game.getCoinAmount());
        assertFalse(game.isEnded());

        // Check 4th update, this time character collided with treasure.
        game.getCharacter().setCharacterPos(game.getSpawnTreasurePos());
        game.update();
        assertFalse(game.getTreasures().contains(game.getHandleTreasurePos())); // Collided with treasure.

        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos())); // new treasure was spawned.
        assertEquals(1, game.getTreasures().size());
        assertFalse(game.getHandleTreasurePos() == game.getSpawnTreasurePos());
        assertFalse(game.isEnded());

        // Check 4th update, nothing happens.
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        game.update();
        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertFalse(game.isEnded());

        // Check 5th update, game has ended.
        assertFalse(game.isEnded());
        game.getCharacter().setHp(0);
        game.update();
        assertTrue(game.isEnded());

    }

    @Test
    void testSpawnCoins() {
        // Checks whether first coin already in the game.
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));

        // Tests 2nd spawnCoins
        game.spawnCoins();
        assertEquals(2, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPos()));

        // Tests spawning another coin.
        game.spawnCoins();
        assertEquals(3, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPos()));
    }

    @Test
    void testSpawnTreasure() {
        // Check that there are no treasures at the very start
        assertEquals(0, game.getTreasures().size());

        // Spawns the first treasure.
        game.spawnTreasure();
        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));

        // Spawns another treasure.
        game.spawnTreasure();
        assertEquals(2, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
    }

    @Test
    void testHandleCoin() {
        // Nothing happens at first, no collision. 1 coin in the screen right now due to initialization.
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(0, game.getCoinAmount());
        game.handleCoin();
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(0, game.getCoinAmount());

        // Collision happens
        game.getCharacter().setCharacterPos(game.getCoinPosStart());
        assertEquals(1, game.getCoin().size());
        assertEquals(0, game.getCoinAmount());

        game.handleCoin();

        assertEquals(0, game.getCoin().size());
        assertFalse(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(1, game.getCoinAmount());

        // Test collision again, this time with spawnCoins
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        game.spawnCoins();
        assertEquals(1, game.getCoin().size());
        assertEquals(1, game.getCoinAmount());

        game.getCharacter().setCharacterPos(game.getCoinPos());
        game.handleCoin();

        assertEquals(0, game.getCoin().size());
        assertFalse(game.getCoin().contains(game.getCoinPos()));
        assertEquals(2, game.getCoinAmount());

    }

    @Test
    void testHandleTreasure() {
        // Nothing happens at first, no collision.
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        assertEquals(0, game.getTreasures().size());
        game.handleTreasure();
        assertEquals(0, game.getTreasures().size());

        // Collides with treasure.
        game.spawnTreasure();
        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        game.getCharacter().setCharacterPos(game.getSpawnTreasurePos());
        assertEquals(0, game.getInventory().getTreasures().size());

        game.handleTreasure();

        assertEquals(0, game.getTreasures().size());
        assertFalse(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertEquals(1, game.getInventory().getTreasures().size());

        // Spawn another treasure
        Position charPos = game.generateRandomPosition();
        game.getCharacter().setCharacterPos(charPos);
        game.spawnTreasure();
        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertFalse(charPos == game.getSpawnTreasurePos());

        game.getCharacter().setCharacterPos(game.getSpawnTreasurePos());
        game.handleTreasure();

        assertEquals(0, game.getTreasures().size());
        assertFalse(game.getTreasures().contains(game.getSpawnTreasurePos()));
        //   assertEquals(1, game.getInventory().getTreasures().size()); BOTH OPTIONS WORK DEPENDING ON WHAT
        //   assertEquals(2, game.getInventory().getTreasures().size()); TREASURE YOU GET.

    }

    @Test
    void testGenerateRandomPosition() {
        // Test whether random position gives a position between the boundaries.
        Position randomPos = game.generateRandomPosition();
        assertTrue(randomPos.getPosX() >= 0 && randomPos.getPosX() <= 39
                && randomPos.getPosY() >= 0 && randomPos.getPosY() <= 21);
    }

    @Test
    void testIsValidPosition() {
        // Test #1: Check is valid position normally
        Position randomPos = game.generateRandomPosition();
        assertTrue(game.isValidPosition(randomPos));

        // Test #2: Coin is colliding
        Position inCoinPos = game.getCoinPosStart(); // Uses the initial coin at the start to check if it's valid.
        assertFalse(game.isValidPosition(inCoinPos));

        // Add another coin to board, check if valid.
        game.spawnCoins();
        Position anotherCoinPos = game.getCoinPos();
        assertFalse(game.isValidPosition(anotherCoinPos));

        // Test #3: Treasure is colliding.
        game.spawnTreasure();
        Position treasurePos = game.getSpawnTreasurePos();
        assertFalse(game.isValidPosition(treasurePos));

        // Test #4: Character is colliding.
        assertFalse(game.isValidPosition(game.getCharacter().getCharacterPos()));
    }

    @Test
    void testOutOfBoundary() {
        // Test #1
        assertFalse(game.outOfBoundary(new Position(0, 0)));

        // Test #2
        assertFalse(game.outOfBoundary(new Position(1, 1)));

        // Test #3
        assertFalse(game.outOfBoundary(new Position(0, 21)));

        // Test #4
        assertFalse(game.outOfBoundary(new Position(39, 0)));

        // Test #5
        assertTrue(game.outOfBoundary(new Position(40, 0)));

        // Test #6
        assertTrue(game.outOfBoundary(new Position(0, 22)));

        // Test #7
        assertTrue(game.outOfBoundary(new Position(50, 5)));

        // Test #8
        assertTrue(game.outOfBoundary(new Position(7, 23)));

        // Test #9
        assertFalse(game.outOfBoundary(new Position(game.getMaxX(), game.getMaxY())));
    }
}