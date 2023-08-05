package model;

// Character class represents the controllable character.

import ui.GameKeyHandler;

public class Character {
    private Position characterPos;
    //private Direction direction;
    private int hp;
    private int atk;


    // EFFECTS: Constructs a character with a set position, direction and hp
    public Character() {
        this.characterPos = new Position(30, 80);
/*        this.direction = Direction.RIGHT;*/
        this.hp = 100;
        atk = 5;
    }


    // MODIFIES: this
    // EFFECTS: Checks if a character has collided with the borders, and if so, then return without modifying this.
    // If no collusion occurs, move character.
    public void move(GameKeyHandler keyHandler) {
        handleUserInput(keyHandler);
    }

    public void handleUserInput(GameKeyHandler keyHandler) {
        if (keyHandler.isUpPressed() == true) {
            if (characterPos.getPosY() > 55) {
                setCharacterPos(new Position(characterPos.getPosX(), characterPos.getPosY() - 4));
            }
        }
        if (keyHandler.isDownPressed() == true) {
            if (characterPos.getPosY() > 535) {
                return;
            }
            setCharacterPos(new Position(characterPos.getPosX(), characterPos.getPosY() + 4));
        }
        if (keyHandler.isRightPressed() == true) {
            if (characterPos.getPosX() > 570) {
                return;
            }
            setCharacterPos(new Position(characterPos.getPosX() + 4, characterPos.getPosY()));
        }
        if (keyHandler.isLeftPressed() == true) {
            if (characterPos.getPosX() <= 0) {
                return;
            }
            setCharacterPos(new Position(characterPos.getPosX() - 4, characterPos.getPosY()));
        }
    }

    public boolean hasCollided(Position pos) {
        double approx = 20;
        double distance = Math.sqrt(Math.pow(characterPos.getPosX() - pos.getPosX(), 2)
                + Math.pow(characterPos.getPosY() - pos.getPosY(), 2));
        return distance < approx;
    }

    // REQUIRES: 39 >= x >= 0 and 21 >= y >= 0.
    // MODIFIES: this
    // EFFECTS: Checks whether character is colliding with something, namely the enemy.
    // If so, subtract 5 from current hp.
    public void checkCollision(Position pos) {
        if (hasCollided(pos)) {
            changeHp();
        }
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(Integer integer) {
        atk = integer;
    }

    // EFFECTS: return character position.
    public Position getCharacterPos() {
        return characterPos;
    }

    // REQUIRES: 39 >= x >= 0 and 21 >= y >= 0.
    // MODIFIES: this
    // EFFECTS: Sets character position to inputted position.
    public void setCharacterPos(Position pos) {
        characterPos = pos;
    }

    // MODIFIES: this
    // EFFECTS: reduces hp by 5.
    public void changeHp() {
        this.hp -= 5;
    }

    // EFFECTS: Returns amount of hp left.
    public int getHp() {
        return hp;
    }

/*    // EFFECTS: Return direction of the character.
    public Direction getDirection() {
        return direction;
    }*/

    // REQUIRES: hp >= 0.
    // MODIFIES: this
    // EFFECTS: Sets the character hp to inputted value.
    public void setHp(int hp) {
        this.hp = hp;
    }

}