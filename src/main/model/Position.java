package model;

// Position class represents the position of any object viable to this class (such as characters, enemies, coins
// and such). The position is used to determine where the objects are in the screen.

public class Position {
    private int posX;
    private int posY;

    // EFFECTS: Constructs a position with coordinate x and coordinate y.
    public Position(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    // EFFECTS: Returns the x position.
    public int getPosX() {
        return posX;
    }

    // EFFECTS: Returns the y position
    public int getPosY() {
        return posY;
    }

    @Override
    public boolean equals(Object c) {
        if (this == c) {
            return true;
        }

        if (c == null || getClass() != c.getClass()) {
            return false;
        }

        Position characterPos = (Position) c;
        return posX == characterPos.posX && posY == characterPos.posY;
    }


}