package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreasureTest {
    private Treasure treasure;

    @BeforeEach
    void runBefore() {
        treasure = new Treasure("testTreasure");
    }

    @Test
    void testWhatTreasure() {
        String treasureName = treasure.whatTreasure();

        // Test Sword
        while (!treasureName.equals("Sword")) {
            treasureName = treasure.whatTreasure();
        }
        assertEquals("Sword", treasureName);


        //Test Health Pot
        while (!treasureName.equals("Health Pot")) {
            treasureName = treasure.whatTreasure();
        }
        assertEquals("Health Pot", treasureName);


        //Test Twig
        while (!treasureName.equals("Twig")) {
            treasureName = treasure.whatTreasure();
        }
        assertEquals("Twig", treasureName);


        // Test Meat
        while (!treasureName.equals("Meat")) {
            treasureName = treasure.whatTreasure();
        }
        assertEquals("Meat", treasureName);

        // Test Bomb
        while (!treasureName.equals("Bomb")) {
            treasureName = treasure.whatTreasure();
        }
        assertEquals("Bomb", treasureName);

    }

    @Test
    void testIncreaseQuantity() {
        assertEquals(1, treasure.getQuantity());
        treasure.increaseQuantity();
        assertEquals(2, treasure.getQuantity());
        treasure.increaseQuantity();
        treasure.increaseQuantity();
        assertEquals(4, treasure.getQuantity());
    }
}