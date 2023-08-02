package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.GameKeyHandler;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    private Character testCharacter;
    private GameKeyHandler keyHandler = new GameKeyHandler();

    @BeforeEach
    void runBefore() {
        testCharacter = new Character();
    }

    @Test
    void testConstructor() {
        Position pos = new Position(30, 80);
        Position charPos = testCharacter.getCharacterPos();
        assertEquals(pos, charPos);
/*        assertEquals(Direction.RIGHT, testCharacter.getDirection());*/
        assertEquals(100, testCharacter.getHp());
    }

    // FIX TESTS
    @Test
    void testMove() {
        // Test Right #1
/*      testCharacter.setDirection(Direction.RIGHT);*/
        keyHandler.setRightPressed(true);
        testCharacter.move(keyHandler);
        assertEquals(new Position(34, 80), testCharacter.getCharacterPos());

        // Test Right #2
        testCharacter.setCharacterPos(new Position(571, 10));
        assertEquals(new Position(571, 10), testCharacter.getCharacterPos());
        testCharacter.move(keyHandler);
        assertEquals(new Position(571, 10), testCharacter.getCharacterPos());

        // Test Up #1
        /*testCharacter.setDirection(Direction.UP);*/
        keyHandler.setRightPressed(false);
        keyHandler.setUpPressed(true);
        testCharacter.setCharacterPos(new Position(50, 55));
        assertEquals(new Position(50, 55), testCharacter.getCharacterPos());
        testCharacter.move(keyHandler);
        assertEquals(new Position(50, 55), testCharacter.getCharacterPos());

        // Test Up #2
        testCharacter.setCharacterPos(new Position(30, 57));
        assertEquals(new Position(30, 57), testCharacter.getCharacterPos());
        testCharacter.move(keyHandler);
        assertEquals(new Position(30, 53), testCharacter.getCharacterPos());

        // Test Left #1
/*        testCharacter.setDirection(Direction.LEFT);*/
        keyHandler.setUpPressed(false);
        keyHandler.setLeftPressed(true);
        testCharacter.setCharacterPos(new Position(40, 30));
        assertEquals(new Position(40, 30), testCharacter.getCharacterPos());
        testCharacter.move(keyHandler);
        assertEquals(new Position(36, 30), testCharacter.getCharacterPos());

        // Test Left #2
        testCharacter.setCharacterPos(new Position(0, 20));
        assertEquals(new Position(0, 20), testCharacter.getCharacterPos());
        testCharacter.move(keyHandler);
        assertEquals(new Position(0, 20), testCharacter.getCharacterPos());

        // Test Down #1
/*        testCharacter.setDirection(Direction.DOWN);*/
        keyHandler.setLeftPressed(false);
        keyHandler.setDownPressed(true);
        testCharacter.setCharacterPos(new Position(5,536));
        assertEquals(new Position(5, 536), testCharacter.getCharacterPos());
        testCharacter.move(keyHandler);
        assertEquals(new Position(5, 536), testCharacter.getCharacterPos());

        // Test Down #2
        testCharacter.setCharacterPos(new Position(7, 10));
        assertEquals(new Position(7, 10), testCharacter.getCharacterPos());
        testCharacter.move(keyHandler);
        assertEquals(new Position(7, 14), testCharacter.getCharacterPos());
    }
/*
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
    }*/

    @Test
    void testHasCollided() {
        assertTrue(testCharacter.hasCollided(new Position(30, 80)));
        assertFalse(testCharacter.hasCollided(new Position(2, 1)));
        assertTrue(testCharacter.hasCollided(new Position(29, 75)));
    }

    @Test
    void testCheckCollision() {
        // Check nothing happens first
        assertEquals(100, testCharacter.getHp());
        testCharacter.checkCollision(new Position(9, 10));
        assertEquals(100, testCharacter.getHp());

        // Check collide once
        testCharacter.checkCollision(new Position(30,80));
        assertEquals(95, testCharacter.getHp());

        // Check collide twice:
        testCharacter.setCharacterPos(new Position(2, 4));
        testCharacter.checkCollision(new Position(2, 4));
        testCharacter.checkCollision(new Position(2, 4));
        assertEquals(85, testCharacter.getHp());
    }

    @Test
    public void atkHpCheck() {
        assertEquals(5, testCharacter.getAtk());
        testCharacter.setAtk(6);
        assertEquals(6, testCharacter.getAtk());
        assertEquals(100, testCharacter.getHp());
        testCharacter.setHp(50);
        assertEquals(50, testCharacter.getHp());
    }
}