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

    public void setHp(Integer integer) {
        hp = integer;
    }


    public boolean hasCollided(Position pos, Integer integer) {
        if (pos.getPosX() >= enemyPos.getPosX() && pos.getPosX() <= enemyPos.getPosX() + integer
                && pos.getPosY() >= enemyPos.getPosY() && pos.getPosY() <= enemyPos.getPosY() + integer) {
            return true;
        } else {
            return false;
        }
    }

    public Rectangle getHitBox(Integer width, Integer height) {
        return new Rectangle(enemyPos.getPosX(), enemyPos.getPosY(), width, height);
    }
}