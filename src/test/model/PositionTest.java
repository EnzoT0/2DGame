package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    private Position position;
    private Position position2;

    @Test
    void testConstructor() {
        // Test #1: first position
        position = new Position(1, 1);
        assertEquals(1, position.getPosX());
        assertEquals(1, position.getPosY());

        // Test #2: second position
        position2 = new Position(8, 14);
        assertEquals(8, position2.getPosX());
        assertEquals(14, position2.getPosY());

    }

    @Test
    void testEquals() {
        // Test for same class
        Position pos = new Position(5, 10);
        assertTrue(pos.equals(new Position(5, 10)));

        Position newPos1 = new Position(1, 5);
        assertTrue(newPos1.equals(newPos1));

        // Test same class but different variables with same parameters
        Position samePos1 = new Position(2, 3);
        Position samePos2 = new Position(2, 3);
        assertTrue(samePos1.equals(samePos2));
        assertTrue(samePos2.equals(samePos1));

        // Tests same class but different variables with different parameters.
        Position diffPos1 = new Position(5, 2);
        Position diffPos2 = new Position(8, 3);
        assertFalse(diffPos1.equals(diffPos2));
        assertFalse(diffPos2.equals(diffPos1));

        // Test for different class
        Position newPos = new Position(6, 8);
        assertFalse(newPos.equals(8));

        // Test for null.
        Position newPos2 = new Position(9, 11);
        assertFalse(newPos2.equals(null));

        // More Test Cases:
        Position equalNullPos = new Position(5, 10);
        Position equalNullPos1 = new Position(0, 0);
        assertFalse(equalNullPos.equals(equalNullPos1));

        Position diffYPos = new Position(5, 15);
        Position diffYPos1 = new Position(5, 10);
        assertFalse(diffYPos.equals(diffYPos1));

        Position diffXPos = new Position(3, 10);
        Position diffXPos1 = new Position(5, 10);
        assertFalse(diffXPos.equals(diffXPos1));

        Position diffObj = new Position(1, 4);
        String diffObj1 = "lol";
        assertTrue(diffObj.equals(new Position(1, 4)));
        assertTrue(diffObj1.equals("lol"));
        assertFalse(diffObj.equals(diffObj1));
    }

}