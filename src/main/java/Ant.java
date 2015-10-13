package main.java;

import java.util.List;
import java.util.Stack;

/**
 * @author hung
 */
public class Ant {
    private Coordinates currentPos;
    private Matrix maze;
    private final static double pheromoneDrop = 0.5f;
    private Stack<Direction> tour;
    private boolean vertexReached;
    private Direction currentDirection;

    /**
     * Contructor for ant with starting position x,y
     * @param currentPos xy currentPos
     */
    public Ant(Coordinates currentPos, Matrix maze) {
        this.currentPos = currentPos;
        this.maze = maze;
        tour = new Stack<Direction>();
        vertexReached = false;
        currentDirection = Direction.NORTH;
    }

    /**
     * Returns current position.
     * @return current position.
     */
    public Coordinates getCurrentPos() {
        return currentPos;
    }

    private Direction getOpposite(Direction d) {
        switch(d) {
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
            default:
                return Direction.NONE;
        }
    }

    public Direction calcProbabilityMove() {
        List<Direction> possibleDirections = maze.getPossibleDirections(getCurrentPos());
        Direction nextMove = Direction.NONE;
        // Als er mogelijke richtingen zijn.
        if(possibleDirections.size() != 0) {
            Direction opposite = getOpposite(currentDirection);
            // Verwijder de opposite direction.
            if(possibleDirections.size() > 1 && possibleDirections.contains(opposite)) {
                possibleDirections.remove(opposite);
            }
            int random = (int) (Math.random() * possibleDirections.size());
            nextMove = possibleDirections.get(random);
        }
        return nextMove;
    }

    public boolean isVertexReached() {
        return vertexReached;
    }

    /**
     * Moves the ant.
     */
    public void move() {
        switch(calcProbabilityMove()) {
            case NORTH:
                currentPos.setRow(currentPos.getRow() - 1);
                currentDirection = Direction.NORTH;
                tour.push(Direction.NORTH);
                break;
            case EAST:
                currentPos.setColumn(currentPos.getColumn() + 1);
                currentDirection = Direction.EAST;
                tour.push(Direction.EAST);
                break;
            case SOUTH:
                currentPos.setRow(currentPos.getRow() + 1);
                currentDirection = Direction.SOUTH;
                tour.push(Direction.SOUTH);
                break;
            case WEST:
                currentPos.setColumn(currentPos.getColumn() - 1);
                currentDirection = Direction.WEST;
                tour.push(Direction.WEST);
                break;
            default:
                break;
        }
        System.out.println(toString());
        //System.out.println("Current pos: " + currentPos);
        if(getCurrentPos().equals(new Coordinates(3, 10))) {
            System.out.println("VERTEX reached: " + getCurrentPos());
            vertexReached = true;
            System.out.println(tour);
        }
    }

    /**
     *
     */
    public String toString() {
        return maze.toStringWithAnt(this);
    }
}
