package model;

// Character class represents the controllable character.

public class Character {
    private Position characterPos;
    private Direction direction;
    private int hp;


    // EFFECTS: Constructs a character with a set position, direction and hp
    public Character() {
        this.characterPos = new Position(1, 1);
        this.direction = Direction.RIGHT;
        this.hp = 100;
    }


    // MODIFIES: this
    // EFFECTS: Checks if a character has collided with the borders, and if so, then return without modifying this.
    // If no collusion occurs, move character.
    public void move() {
        if (hasCollidedR(direction.move(characterPos))) {
            return;
        }
        characterPos = direction.move(characterPos);

    }


    // EFFECTS: Returns a boolean, either a true or false, depending on whether the position is over the borders.
    public boolean hasCollidedR(Position pos) {
        return pos.getPosX() < 0 || pos.getPosY() < 0 || pos.getPosX() > 39
                || pos.getPosY() > 21;
    }

    // EFFECTS: Returns a boolean, either a true or false, depending on whether the character collided with something.
    public boolean hasCollided(Position pos) {
        return characterPos.equals(pos);
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

    // MODIFIES: this
    // EFFECTS: set the direction to given input.
    public void setDirection(Direction dir) {
        direction = dir;
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

    // EFFECTS: Return direction of the character.
    public Direction getDirection() {
        return direction;
    }

    // REQUIRES: hp >= 0.
    // MODIFIES: this
    // EFFECTS: Sets the character hp to inputted value.
    public void setHp(int hp) {
        this.hp = hp;
    }

}