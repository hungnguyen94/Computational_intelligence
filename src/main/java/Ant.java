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

    /**
     * Contructor for ant with starting position x,y
     * @param currentPos xy currentPos
     */
    public Ant(Coordinates currentPos, Matrix maze) {
        this.currentPos = currentPos;
        this.maze = maze;
        tour = new Stack<Direction>();
        vertexReached = false;
    }

    /**
     * Returns current position.
     * @return current position.
     */
    public Coordinates getCurrentPos() {
        return currentPos;
    }

    public Direction calcProbabilityMove() {
        List<Direction> possibleDirections = maze.getPossibleDirections(getCurrentPos());
        int random = (int) (Math.random() * possibleDirections.size());

        return (possibleDirections.size() == 0)? Direction.NONE: possibleDirections.get(random);
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
                tour.push(Direction.NORTH);
                break;
            case EAST:
                currentPos.setColumn(currentPos.getColumn() + 1);
                tour.push(Direction.EAST);
                break;
            case SOUTH:
                currentPos.setRow(currentPos.getRow() + 1);
                tour.push(Direction.SOUTH);
                break;
            case WEST:
                currentPos.setColumn(currentPos.getColumn() - 1);
                tour.push(Direction.WEST);
                break;
            default:
                break;
        }
        System.out.println(toString());
        //System.out.println("Current pos: " + currentPos);
        if(getCurrentPos().equals(new Coordinates(24, 14))) {
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
