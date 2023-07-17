package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnemyListTest {

    private EnemyList enemyList;

    @BeforeEach
    void runBefore() {
        enemyList = new EnemyList();
    }

    @Test
    void testAddEnemies() {
        // Add 1 Enemy
        enemyList.addEnemies(1);
        assertEquals(1, enemyList.getEnemies().size());

        // Add 5 Enemies
        enemyList.addEnemies(5);
        assertEquals(5, enemyList.getEnemies().size());

        // Add 10 Enemies
        enemyList.addEnemies(10);
        assertEquals(10, enemyList.getEnemies().size());

    }
}