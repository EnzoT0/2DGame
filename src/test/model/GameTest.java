package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Inventory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        assertFalse(game.getCoin().contains(game.getCheckCoinPos())); // Update changes coinPos to newSpawn CoinPos.
        assertEquals(1, game.getCoin().size());

        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertFalse(game.getCoinPos() == game.getCheckCoinPos()); // Shows that it's now a different position.
        assertEquals(2, game.getCoinAmount());
        assertFalse(game.isEnded());

        // Check 4th update, this time character collided with treasure.
        game.getCharacter().setCharacterPos(game.getSpawnTreasurePos());
        game.update();
        assertFalse(game.getTreasures().contains(game.getCheckTreasurePos())); // Collided with treasure.

        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos())); // new treasure was spawned.
        assertEquals(1, game.getTreasures().size());
        assertFalse(game.getCheckTreasurePos() == game.getSpawnTreasurePos());
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
    void testEnemyUpdate() {
        EnemyList enemyList = new EnemyList();
        List<Enemy> enemies = enemyList.addEnemies(1);
        game.setEnemies(enemies);

        assertEquals(1, enemyList.getEnemies().size());

        // Test 1
        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(1, 1));
        }
        game.enemyUpdate();

        assertEquals(new Position(1 + game.getDx(), 1 + game.getDy()), enemies.get(0).getEnemyPos());

        // Test 2
        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(-2, 4));
        }
        while (game.getEnemies().get(0).getEnemyPos().getPosX() != 1) {
            game.enemyUpdate();
        }
        assertEquals(1, game.getEnemies().get(0).getEnemyPos().getPosX());

        // Test 3
        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(4, -2));
        }

        while (game.getEnemies().get(0).getEnemyPos().getPosY() != 1) {
            game.enemyUpdate();
        }
        assertEquals(1, game.getEnemies().get(0).getEnemyPos().getPosY());

        // Test 4
        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(40, 5));
        }

        while (game.getEnemies().get(0).getEnemyPos().getPosX() != 38) {
            game.enemyUpdate();
        }
        assertEquals(38, game.getEnemies().get(0).getEnemyPos().getPosX());

        // Test 5
        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(2, 22));
        }
        while (game.getEnemies().get(0).getEnemyPos().getPosY() != 20) {
            game.enemyUpdate();
        }
        assertEquals(20, game.getEnemies().get(0).getEnemyPos().getPosY());


    }

    @Test
    void testCheckRandPosUpdate() {
        // Test 1
        while (game.isRand() != true) {
            game.checkRandPosUpdate();
        }
        assertEquals(0, game.getDx());
        assertEquals(0, game.getDy());
        assertTrue(game.isRand());

        // Test 2
        while (game.isRand() != false) {
            game.checkRandPosUpdate();
        }
        assertEquals(game.getRandDx(), game.getDx());
        assertEquals(game.getRandDy(), game.getDy());
        assertFalse(game.isRand());
    }

    @Test
    void testSpawnCoins() {
        // Checks whether first coin already in the game.
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertFalse(game.isValidPosition(game.getCoinPosStart())); // No longer a valid position due to coin

        // Tests 2nd spawnCoins
        game.spawnCoins();
        assertEquals(2, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertFalse(game.isValidPosition(game.getCoinPos()));

        // Tests spawning another coin.
        game.spawnCoins();
        assertEquals(3, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertFalse(game.isValidPosition(game.getCoinPos()));
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
    void testCheckCoin() {
        // Nothing happens at first, no collision. 1 coin in the screen right now due to initialization.
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(0, game.getCoinAmount());
        game.checkCoin();
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(0, game.getCoinAmount());

        // Collision happens
        game.getCharacter().setCharacterPos(game.getCoinPosStart());
        assertEquals(1, game.getCoin().size());
        assertEquals(0, game.getCoinAmount());

        game.checkCoin();

        assertEquals(0, game.getCoin().size());
        assertFalse(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(1, game.getCoinAmount());

        // Test collision again, this time with spawnCoins
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        game.spawnCoins();
        assertEquals(1, game.getCoin().size());
        assertEquals(1, game.getCoinAmount());

        game.getCharacter().setCharacterPos(game.getCoinPos());
        game.checkCoin();

        assertEquals(0, game.getCoin().size());
        assertFalse(game.getCoin().contains(game.getCoinPos()));
        assertEquals(2, game.getCoinAmount());

    }

    @Test
    void testCheckTreasure() {
        // Nothing happens at first, no collision.
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        assertEquals(0, game.getTreasures().size());
        game.checkTreasure();
        assertEquals(0, game.getTreasures().size());

        // Collides with treasure.
        game.spawnTreasure();
        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        game.getCharacter().setCharacterPos(game.getSpawnTreasurePos());
        assertEquals(0, game.getInventory().getTreasures().size());

        game.checkTreasure();

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
        game.checkTreasure();

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
        while (!game.isValidPosition(randomPos)) {
            randomPos = game.generateRandomPosition();
        }
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

        // Test 5: Boundary checking
        Position boundaryPos1 = new Position(0, 0);
        assertTrue(game.isValidPosition(boundaryPos1));

        Position boundaryPos2 = new Position(-1, 0);
        assertFalse(game.isValidPosition(boundaryPos2));

        Position boundaryPos3 = new Position(0, -1);
        assertFalse(game.isValidPosition(boundaryPos3));

        Position boundaryPos4 = new Position(4, 5);
        assertTrue(game.isValidPosition(boundaryPos4));

        Position boundaryPos5 = new Position(39, 5);
        assertTrue(game.isValidPosition(boundaryPos5));

        Position boundaryPos6 = new Position(40, 9);
        assertFalse(game.isValidPosition(boundaryPos6));

        Position boundaryPos7 = new Position(9, 21);
        assertTrue(game.isValidPosition(boundaryPos7));

        Position boundaryPos8 = new Position(10, 22);
        assertFalse(game.isValidPosition(boundaryPos8));
    }

    @Test
    void testMakeValidRandomPosition() {
        Position pos = new Position(1, 1);
        assertFalse(game.isValidPosition(pos));
        Position newPos = game.makeValidRandomPos(pos);
        assertTrue(game.isValidPosition(newPos));

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

        // Test #10
        assertFalse(game.outOfBoundary(new Position(9, 10)));

        // More Tests
        assertTrue(game.outOfBoundary(new Position(-1, -1)));
        assertTrue(game.outOfBoundary(new Position(10, -1)));
        assertTrue(game.outOfBoundary(new Position(-1, 5)));

        assertTrue(game.outOfBoundary(new Position(40, 22)));
        assertTrue(game.outOfBoundary(new Position(36, 25)));
        assertTrue(game.outOfBoundary(new Position(42, 20)));
        assertFalse(game.outOfBoundary(new Position(5, 8)));

    }

    @Test
    void testSetInventory() {
        assertEquals(0, game.getInventory().getTreasures().size());
        Inventory inventory = new Inventory();
        inventory.addTreasure(new Treasure("lol"));
        assertEquals(1, inventory.getTreasures().size());

        game.setInventory(inventory);
        assertEquals(1, game.getInventory().getTreasures().size());
    }

    @Test
    void testSetTreasure() {
        Set<Position> treasures = new HashSet<>();
        treasures.add(new Position(2, 3));
        game.setTreasures(treasures);

        assertEquals(new Position(2, 3), game.getTreasures().stream().findFirst().get());
    }

    @Test
    void testSetTreasurePos() {
        game.getTreasures().remove(game.getSpawnTreasurePos());

        // Set Spawn Treasure:
        game.setSpawnTreasurePos(new Position(4, 5));
        assertEquals(new Position(4, 5), game.getSpawnTreasurePos());

        // Set Check Treasure
        game.setCheckTreasurePos(new Position(2, 3));
        assertEquals(new Position(2, 3), game.getCheckTreasurePos());
    }

    @Test
    void testSetCoin() {
        game.getCoin().remove(game.getCoinPosStart());

        // Set Spawn Coin
        game.setCoinPosStart(new Position(5, 6));
        assertEquals(new Position(5, 6), game.getCoinPosStart());

        // Set Coin Pos
        game.setCoinPos(new Position(8, 7));
        assertEquals(new Position(8, 7), game.getCoinPos());

        // Set Check Coin Pos
        game.setCheckCoinPos(new Position(12, 8));
        assertEquals(new Position(12, 8), game.getCheckCoinPos());

        // Set Coin Amount
        assertEquals(0, game.getCoinAmount());
        game.setCoinAmount(2);
        assertEquals(2, game.getCoinAmount());
    }
}