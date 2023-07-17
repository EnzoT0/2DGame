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
        if (treasureName.equals("Sword")) {
            assertEquals("Sword", treasureName);
        } else if (treasureName.equals("Health Pot")) {
            assertEquals("Health Pot", treasureName);
        } else if (treasureName.equals("Twig")) {
            assertEquals("Twig", treasureName);
        } else if (treasureName.equals("Meat")) {
            assertEquals("Meat", treasureName);
        } else if (treasureName.equals("Bomb")) {
            assertEquals("Bomb", treasureName);
        }
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
