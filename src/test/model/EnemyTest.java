package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

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
        assertEquals(20, enemy.getHp());
    }

    @Test
    void testSetEnemyPos() {
        assertEquals(new Position(5, 6), enemy.getEnemyPos());

        enemy.setEnemyPos(new Position(8, 10));
        assertEquals(new Position(8, 10), enemy.getEnemyPos());

        enemy.setEnemyPos(new Position(0, 0));
        assertEquals(new Position(0,0), enemy.getEnemyPos());

    }

/*    @Test
    void testHasCollided() {
        assertTrue(enemy.hasCollidedFake(new Position(5, 6)));
        assertFalse(enemy.hasCollidedFake(new Position(100, 300)));
        assertTrue(enemy.hasCollidedFake(new Position(4, 5)));
    }*/

    @Test
    void testMinusHp() {
        assertEquals(20, enemy.getHp());
        enemy.minusHp(5);
        assertEquals(15, enemy.getHp());
        enemy.minusHp(5);
        assertEquals(10, enemy.getHp());
    }

    @Test
    void testSetHp() {
        assertEquals(20, enemy.getHp());
        enemy.setHp(5);
        assertEquals(5, enemy.getHp());
        enemy.setHp(0);
        assertEquals(0, enemy.getHp());
    }

/*    @Test
    void testHasCollided() {
        assertTrue(enemy.hasCollided(new Position(5, 6), 2));
        assertTrue(enemy.hasCollided(new Position(7, 6), 2));
        assertTrue(enemy.hasCollided(new Position(5, 8), 2));
        assertFalse(enemy.hasCollided(new Position(100, 100), 30));

        assertFalse(enemy.hasCollided(new Position(5, 9), 2));
        assertTrue(enemy.hasCollided(new Position(5, 7), 2));
        assertFalse(enemy.hasCollided(new Position(4, 3), 2));

        assertTrue(enemy.hasCollided(new Position(5, 11), 5));
        assertTrue(enemy.hasCollided(new Position(5, 11), 5));

        assertTrue(enemy.hasCollided(new Position(5, 6), 5));

        assertTrue(enemy.hasCollided(new Position(5, 8), 6));
        assertFalse(enemy.hasCollided(new Position(5, 5), 3));

        assertFalse(enemy.hasCollided(new Position(3, 5), 8));
    }*/

    @Test
    void testHitBox() {
        assertEquals(new Rectangle(enemy.getEnemyPos().getPosX(),
                enemy.getEnemyPos().getPosY(), 10, 10), enemy.getHitBox(10, 10));
        assertEquals(new Rectangle(enemy.getEnemyPos().getPosX(), enemy.getEnemyPos().getPosY(), 15, 15),
                enemy.getHitBox(15, 15));
    }

}