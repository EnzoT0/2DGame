package model;

// Projectile class of the game. User can shoot projectiles using this class.

public class Projectile {

    private Position pos;

    // EFFECTS: Constructs the projectile, giving it a position.
    public Projectile(Position pos) {
        this.pos = pos;
    }

    // EFFECTS: Returns the position of the projectile.
    public Position getPos() {
        return pos;
    }

    // MODIFIES: this
    // EFFECTS: Moves the projectile to a new position.
    public void movePos() {
        setPos(new Position(pos.getPosX() + 5, pos.getPosY()));
    }

    // MODIFIES: this
    // EFFECTS: Moves the boss projectile to a new position.
    public void moveBossPos() {
        setPos(new Position(pos.getPosX() - 2, pos.getPosY()));
    }

    // MODIFIES: this
    // EFFECTS: Sets the projectile position to a specified position.
    public void setPos(Position pos) {
        this.pos = pos;
    }
}
