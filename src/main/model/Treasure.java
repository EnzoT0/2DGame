package model;

import java.util.Random;

// Treasure class represents the treasure that can be obtained throughout the game. Treasures can be
// earned an infinite amount of times and are put into an inventory system which can display them.

public class Treasure {
    private final Random rand = new Random();
    private final int upperbound = 5;
    private String name;
    private int quantity;

    // EFFECTS: Constructs a treasure with a name and a quantity of 1.
    public Treasure(String name) {
        this.name = name;
        quantity = 1;
    }

    // EFFECTS: Gives a random treasure depending on the number (0-4).
    public String whatTreasure() {
        int random = rand.nextInt(upperbound);
        switch (random) {
            case 0:
                String t1 = "Sword";
                return t1;
            case 1:
                String t2 = "Health Pot";
                return t2;
            case 2:
                String t3 = "Twig";
                return t3;
            case 3:
                String t4 = "Meat";
                return t4;
            default:
                String t5 = "Bomb";
                return t5;

        }
    }

    // MODIFIES: this
    // EFFECTS: Increases quantity by 1.
    public void increaseQuantity() {
        int newQuantity = quantity + 1;
        quantity = newQuantity;
    }

    // EFFECTS: Returns the name of the treasure.
    public String getName() {
        return name;
    }

    // EFFECTS: Returns the quantity of the treasure.
    public int getQuantity() {
        return quantity;
    }

}