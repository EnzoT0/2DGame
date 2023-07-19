package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {

    private Direction direction;

    @Test
    void testConstructor() {

        // Test Direction Up
        direction = Direction.UP;
        assertEquals(0, direction.getDx());
        assertEquals(-1, direction.getDy());

        // Test Direction Down
        direction = Direction.DOWN;
        assertEquals(0, direction.getDx());
        assertEquals(1, direction.getDy());

        // Test Direction Right
        direction = Direction.RIGHT;
        assertEquals(1, direction.getDx());
        assertEquals(0, direction.getDy());

        // Test Direction Left
        direction = Direction.LEFT;
        assertEquals(-1, direction.getDx());
        assertEquals(0, direction.getDy());

        // Test Direction None
        direction = Direction.NONE;
        assertEquals(0, direction.getDx());
        assertEquals(0, direction.getDy());
    }

    @Test
    void testMove() {
        // Test Direction Up
        direction = Direction.UP;
        assertEquals(new Position(0, -2), direction.move(new Position(0, -1)));

        // Test Direction Down
        direction = Direction.DOWN;
        assertEquals(new Position(0, 2), direction.move(new Position(0, 1)));

        // Test Direction Right
        direction = Direction.RIGHT;
        assertEquals(new Position(1, 1), direction.move(new Position(0, 1)));

        // Test Direction Left
        direction = Direction.LEFT;
        assertEquals(new Position(-1, 0), direction.move(new Position(0, 0)));

        // Test Direction None
        direction = Direction.NONE;
        assertEquals(new Position(0, 0), direction.move(new Position(0, 0)));
    }
}
