package main.java;

/**
 * Representation for direction.
 */
public enum Direction {
    EAST    (0),
    NORTH   (1),
    WEST    (2),
    SOUTH   (3),
    NONE    (4);

    private final int directionCode;

    Direction(int directionCode) {
        this.directionCode = directionCode;
    }

    public int getDirectionCode() {
        return this.directionCode;
    }
}
