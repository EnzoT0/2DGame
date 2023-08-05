package model;

// Enemy class represents the enemies inside the game that move and such.

import java.awt.*;

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

    // REQUIRES: 570 >= x >= 0 and 535 >= y >= 55.
    // MODIFIES: this
    // EFFECTS: sets the enemy position to the position given.
    public void setEnemyPos(Position enemyPos) {
        this.enemyPos = enemyPos;
    }

    // EFFECTS: Returns the hp of the enemy.
    public int getHp() {
        return hp;
    }

    // MODIFIES: this
    // EFFECTS: Minus the hp by the specified integer.
    public void minusHp(Integer integer) {
        hp = hp - integer;
    }

    // MODIFIES: this
    // EFFECTS: Sets the hp of the character to the specified integer.
    public void setHp(Integer integer) {
        hp = integer;
    }


/*    public boolean hasCollided(Position pos, Integer integer) {
        if (pos.getPosX() >= enemyPos.getPosX() && pos.getPosX() <= enemyPos.getPosX() + integer
                && pos.getPosY() >= enemyPos.getPosY() && pos.getPosY() <= enemyPos.getPosY() + integer) {
            return true;
        } else {
            return false;
        }
    }*/

    // EFFECTS: Gets the enemy hit box.
    public Rectangle getHitBox(Integer width, Integer height) {
        return new Rectangle(enemyPos.getPosX(), enemyPos.getPosY(), width, height);
    }
}