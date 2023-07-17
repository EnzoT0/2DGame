package model;

// Enemy class represents the enemies inside the game that move and such.

public class Enemy {

    private Position enemyPos;


    // EFFECTS: Constructs an enemy with a given position.
    public Enemy(Position pos) {
        enemyPos = pos;
    }

    // EFFECTS: returns enemy position.
    public Position getEnemyPos() {
        return enemyPos;
    }

    // REQUIRES: 39 >= x >= 0 and 21 >= y >= 0 for the position.
    // MODIFIES: this
    // EFFECTS: sets the enemy position to the position given.
    public void setEnemyPos(Position enemyPos) {
        this.enemyPos = enemyPos;
    }
}