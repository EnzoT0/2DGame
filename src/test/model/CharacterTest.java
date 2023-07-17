package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    private Character testCharacter;

    @BeforeEach
    void runBefore() {
        testCharacter = new Character();
    }

    @Test
    void testConstructor() {
        Position pos = new Position(1, 1);
        Position charPos = testCharacter.getCharacterPos();
        assertEquals(pos, charPos);
        assertEquals(Direction.RIGHT, testCharacter.getDirection());
        assertEquals(100, testCharacter.getHp());
    }

    @Test
    void testMove() {
        // Test Right #1
        testCharacter.setDirection(Direction.RIGHT);
        testCharacter.move();
        assertEquals(new Position(2, 1), testCharacter.getCharacterPos());

        // Test Right #2
        testCharacter.setCharacterPos(new Position(39, 10));
        assertEquals(new Position(39, 10), testCharacter.getCharacterPos());
        testCharacter.move();
        assertEquals(new Position(39, 10), testCharacter.getCharacterPos());

        // Test Up #1
        testCharacter.setDirection(Direction.UP);
        testCharacter.setCharacterPos(new Position(4, 0));
        assertEquals(new Position(4, 0), testCharacter.getCharacterPos());
        testCharacter.move();
        assertEquals(new Position(4, 0), testCharacter.getCharacterPos());

        // Test Up #2
        testCharacter.setCharacterPos(new Position(2, 2));
        assertEquals(new Position(2, 2), testCharacter.getCharacterPos());
        testCharacter.move();
        assertEquals(new Position(2, 1), testCharacter.getCharacterPos());

        // Test Left #1
        testCharacter.setDirection(Direction.LEFT);
        testCharacter.setCharacterPos(new Position(4, 3));
        assertEquals(new Position(4, 3), testCharacter.getCharacterPos());
        testCharacter.move();
        assertEquals(new Position(3, 3), testCharacter.getCharacterPos());

        // Test Left #2
        testCharacter.setCharacterPos(new Position(0, 2));
        assertEquals(new Position(0, 2), testCharacter.getCharacterPos());
        testCharacter.move();
        assertEquals(new Position(0, 2), testCharacter.getCharacterPos());

        // Test Down #1
        testCharacter.setDirection(Direction.DOWN);
        testCharacter.setCharacterPos(new Position(5,21));
        assertEquals(new Position(5, 21), testCharacter.getCharacterPos());
        testCharacter.move();
        assertEquals(new Position(5, 21), testCharacter.getCharacterPos());

        // Test Down #2
        testCharacter.setCharacterPos(new Position(7, 10));
        assertEquals(new Position(7, 10), testCharacter.getCharacterPos());
        testCharacter.move();
        assertEquals(new Position(7, 11), testCharacter.getCharacterPos());
    }

    @Test
    void testHasCollidedR() {
        testCharacter.setCharacterPos(new Position(0, 0));
        assertFalse(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(1, 1));
        assertFalse(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(-1, 1));
        assertTrue(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(1, -1));
        assertTrue(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(39, 10));
        assertFalse(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(40, 11));
        assertTrue(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(50, 17));
        assertTrue(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(15, 21));
        assertFalse(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(17, 22));
        assertTrue(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));

        testCharacter.setCharacterPos(new Position(24, 30));
        assertTrue(testCharacter.hasCollidedR(testCharacter.getCharacterPos()));
    }

    @Test
    void testHasCollided() {
        assertTrue(testCharacter.hasCollided(new Position(1, 1)));
        assertFalse(testCharacter.hasCollided(new Position(2, 1)));
    }

    @Test
    void testCheckCollision() {
        // Check nothing happens first
        assertEquals(100, testCharacter.getHp());
        testCharacter.checkCollision(new Position(9, 10));
        assertEquals(100, testCharacter.getHp());

        // Check collide once
        testCharacter.checkCollision(new Position(1,1));
        assertEquals(95, testCharacter.getHp());

        // Check collide twice:
        testCharacter.setCharacterPos(new Position(2, 4));
        testCharacter.checkCollision(new Position(2, 4));
        testCharacter.checkCollision(new Position(2, 4));
        assertEquals(85, testCharacter.getHp());
    }
}