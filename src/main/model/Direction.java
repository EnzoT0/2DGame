package model;


// Enum for Direction

// Code follows the one from the Direction enum in the snake console project:
// https://github.students.cs.ubc.ca/CPSC210/SnakeConsole-Lanterna/blob/main/src/main/java/com/mazenk/snake/model/Direction.java

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    RIGHT(1, 0),
    LEFT(-1, 0),
    NONE(0, 0);

    private int dx;
    private int dy;


    // EFFECTS: constructs a direction with x and y coord.
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }


    // MODIFIES: Position
    // EFFECTS: Returns a new position by adding the x and y coord.
    public Position move(Position pos) {
        return new Position(
                pos.getPosX() + dx,
                pos.getPosY() + dy
        );
    }

    // EFFECTS: returns dx of direction.
    public int getDx() {
        return dx;
    }

    // EFFECTS: returns dy of direction.
    public int getDy() {
        return dy;
    }
}