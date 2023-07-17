package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testSetEnemyPos() {
        assertEquals(new Position(5, 6), enemy.getEnemyPos());

        enemy.setEnemyPos(new Position(8, 10));
        assertEquals(new Position(8, 10), enemy.getEnemyPos());

        enemy.setEnemyPos(new Position(0, 0));
        assertEquals(new Position(0,0), enemy.getEnemyPos());

    }
}