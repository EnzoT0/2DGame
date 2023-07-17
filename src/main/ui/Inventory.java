package ui;

import model.Treasure;

import java.util.ArrayList;
import java.util.List;

// Inventory class represents the inventory system of the game. It showcases the amount of treasures earned through
// the game.

public class Inventory {
    private List<Treasure> treasures;

    // EFFECTS: Constructs an inventory system with a list of treasures.
    public Inventory() {
        treasures = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a given treasure to the list of treasures inside the inventory system.
    // If there is more than one existing treasure inside the list, add quantity.
    public void addTreasure(Treasure treasure) {
        for (Treasure oneTreasure : treasures) {
            if (oneTreasure.getName().equals(treasure.getName())) {
                oneTreasure.increaseQuantity();
                System.out.println("Added " + treasure.getName() + " to inventory.");
                return;
            }
        }
        treasures.add(treasure);
        System.out.println("Added " + treasure.getName() + " to inventory.");
    }

    // EFFECTS: Displays the inventory system with the given amount of treasures inside it.
    public void displayInventory() {
        if (treasures.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Treasure treasure : treasures) {
                System.out.println("- " + treasure.getQuantity() + " " + treasure.getName());
            }
        }
    }

    // EFFECTS: Returns the list of treasures in inventory.
    public List<Treasure> getTreasures() {
        return treasures;
    }
}