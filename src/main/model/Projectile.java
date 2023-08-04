package model;

public class Projectile {

    private Position pos;


    public Projectile(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public void movePos() {
        setPos(new Position(pos.getPosX() + 5, pos.getPosY()));
    }

    public void moveBossPos() {
        setPos(new Position(pos.getPosX() - 2, pos.getPosY()));
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
