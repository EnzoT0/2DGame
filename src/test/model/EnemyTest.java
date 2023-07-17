package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnemyTest {

    private Enemy enemy;

    @BeforeEach
    void runBefore() {
        enemy = new Enemy(new Position(5, 6));
    }

    @Test
    void testConstructor() {
        assertEquals(new Position(5, 6), enemy.getEnemyPos());
    }
}
