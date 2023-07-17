package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}