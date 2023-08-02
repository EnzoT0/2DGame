package model;

// Enemy class represents the enemies inside the game that move and such.

public class Enemy {

    private Position enemyPos;
    private int hp;


    // EFFECTS: Constructs an enemy with a given position.
    // ADDED CONSTRUCTOR HP NEED TESTS
    public Enemy(Position pos) {
        enemyPos = pos;
        hp = 20;
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

    public int getHp() {
        return hp;
    }

    public void minusHp(Integer integer) {
        hp = hp - integer;
    }

    public boolean hasCollided(Position pos) {
        double approx = 15;
        double distance = Math.sqrt(Math.pow(enemyPos.getPosX() - pos.getPosX(), 2)
                + Math.pow(enemyPos.getPosY() - pos.getPosY(), 2));
        return distance < approx;
    }

    public void setHp(Integer integer) {
        hp = integer;
    }
}