package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Hung
 */
public class Ant {

    private List<Coordinate> tspGoals;

    protected Coordinate currentPos;
    protected Maze maze;
    protected Stack<Direction> tourDirections;
    protected Edge tourEdge;
    protected boolean goalReached;
    protected Direction currentDirection;
    protected Vertex lastVertex;
    protected Edge visited;
    protected boolean followOnlyHighest;


    /**
     * Contructor for ant with starting position x,y
     * @param currentPos xy currentPos
     */
    public Ant(Coordinate currentPos, Maze maze) {
        this.currentPos = currentPos;
        this.maze = maze;
        this.currentDirection = Direction.NONE;
        this.tourDirections = new Stack<Direction>();
        this.goalReached = false;
        this.lastVertex = new Vertex(getCurrentPos());
        this.visited = new Edge();
        this.tourEdge = new Edge();
        this.tspGoals = new ArrayList<>(ACO.tspCoordinates);
        checkVertex();
        checkPosition();
        followOnlyHighest = false;
    }

    /**
     * Contructor for ant with starting position x,y
     * @param currentPos xy currentPos
     */
    public Ant(Coordinate currentPos, Maze maze, boolean followOnlyHighest) {
        this.currentPos = currentPos;
        this.maze = maze;
        this.currentDirection = Direction.NONE;
        this.tourDirections = new Stack<Direction>();
        this.goalReached = false;
        this.lastVertex = new Vertex(getCurrentPos());
        this.visited = new Edge();
        this.tourEdge = new Edge();
        this.tspGoals = new ArrayList<>(ACO.tspCoordinates);
        checkVertex();
        checkPosition();
        this.followOnlyHighest = true;
    }

    /**
     * Returns current position.
     * @return current position.
     */
    public Coordinate getCurrentPos() {
        return new Coordinate(currentPos);
    }

    /**
     * Get the opposite direction.
     * @param d current direction.
     * @return Opposite direction.
     */
    static Direction getOpposite(Direction d) {
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

    /**
     * Calculates the probability and
     * returns a map with all the directions mapped
     * to a probability value.
     * @param position Vertex.
     * @return Map of directions mapped to probability.
     */
    public Map<Direction, Double> getAllProbability(Coordinate position) {
        List<Direction> directionList = maze.getPossibleDirections(position);
        Map<Direction, Double> pheromoneDirectionMap = new HashMap<>();
        double totalProbability = 0;

//        // Delete opposite direction if it isn't an one way edge.
        if(maze.sumNeighbours(position) > 4 && directionList.size() > 2)
            directionList.remove(getOpposite(currentDirection));

        if(followOnlyHighest) {
            directionList.remove(getOpposite(currentDirection));
            double highestPheromone = 0.d;
            Direction highestDirection = Direction.NONE;
            for(Direction direction : directionList) {
                if(maze.getPheromone(position, direction) > highestPheromone) {
                    highestDirection = direction;
                    highestPheromone = maze.getPheromone(position, direction);
                }
            }
            pheromoneDirectionMap.put(highestDirection, 10.d);
//            System.out.println(pheromoneDirectionMap);
            return pheromoneDirectionMap;
        }

        for(Direction direction : directionList) {
            double currentPheromone = maze.getPheromone(position, direction);
            Vertex vertex = maze.getVertex(getCurrentPos());
            Edge edge = vertex != null?vertex.getEdge(direction): null;
            int lengthEdge = (edge != null)? edge.getSize(): 20;
            double probability = Math.pow(currentPheromone, ACO.alpha);// * Math.pow(1.0D/lengthEdge, ACO.beta);

            // Give lower probability to positions that already have been visited.
            Coordinate nextPosition = getCurrentPos();
            nextPosition.move(direction);
            if(tourEdge.containsCoordinate(nextPosition)) {
                probability *= 0.4d;
            }
            // Give lower probability for the opposite direction.
            probability = (direction == getOpposite(currentDirection))? probability/2: probability;

            pheromoneDirectionMap.put(direction, probability);
            totalProbability += probability;
        }

        for(Map.Entry<Direction, Double> directionDoubleEntry : pheromoneDirectionMap.entrySet()) {
            directionDoubleEntry.setValue(directionDoubleEntry.getValue() / totalProbability);
        }
        return pheromoneDirectionMap;
    }

    /**
     * Calculate which direction to take.
     * Output is partly random and partly depends on pheromone value of the edges.
     * @return Direction to take.
     */
    public Direction calcProbabilityMove() {
        List<Direction> possibleDirections = new ArrayList<>(maze.getPossibleDirections(getCurrentPos()));

        // If current position is on a vertex.
//        if(maze.getVertex(getCurrentPos()) != null) {
        if(possibleDirections.size() >= 1) {
            Map<Direction, Double> probabilityDirectionMap = getAllProbability(getCurrentPos());
            double random = Math.random();
            double sumProbability = 0;

            for(Map.Entry<Direction, Double> directionDoubleEntry : probabilityDirectionMap.entrySet()) {
                double prob = directionDoubleEntry.getValue();
                sumProbability += prob;
                if(random < sumProbability) {
                    return directionDoubleEntry.getKey();
                }
            }
        }
        System.out.println("RANDOM");
        int random = (int) (Math.random() * possibleDirections.size());
        Direction nextMove = possibleDirections.get(random);
        return nextMove;
    }

    public boolean isGoalReached() {
        return goalReached;
    }

    /**
     * Add a new vertex if it doesn't exist yet and connect them.
     */
    private void checkVertex() {
        // If true, then current position is a vertex.
        int possibleDirections = maze.getPossibleDirections(getCurrentPos()).size();
        boolean moreThanThreeDirections = possibleDirections >= 3;
//        boolean sumNeighboursSmallerThan = maze.sumNeighbours(getCurrentPos()) <= possibleDirections + 2;
        boolean isTspCoordinate = ACO.tspCoordinates.contains(getCurrentPos());
        boolean isGoalCoordinate = ACO.goalCoordinates.contains(getCurrentPos());

        if(isGoalCoordinate || isTspCoordinate) {
            Vertex vertexHere =  maze.getVertex(getCurrentPos());
            // True if the vertex already exists.
            if(vertexHere == null)
                vertexHere = new Vertex(getCurrentPos());

            vertexHere.addVertex(lastVertex, visited);
            lastVertex.addVertex(vertexHere, visited);
            visited.clear();
            maze.addVertex(vertexHere);
            maze.addVertex(lastVertex);
            lastVertex = vertexHere;
        }
    }

    /**
     * Check if current position is a goal that needed to be reached.
     */
    private void checkPosition() {
        if(ACO.tspCoordinates.contains(getCurrentPos())) {
            tspGoals.remove(getCurrentPos());
            // Set direction to none to allow walk back.
            currentDirection = Direction.NONE;
        }

        if(tspGoals.size() == 0 && getCurrentPos().equals(ACO.goalCoordinate)) {
            if(followOnlyHighest) {
                System.out.println("follow only highest: " + tourDirections.size());
            }


            // Pheromone value calculation and apply.
            double pheromoneValue = ACO.pheromoneDropRate / Math.pow(tourDirections.size(), 2);
            maze.enqueueIncreasePheromone(tourEdge, pheromoneValue);

            if(ACO.shortestDirections.size() > tourDirections.size() || ACO.shortestDirections.size() == 0) {
                ACO.shortestDirections = tourDirections;
                System.out.println("Shortest route: " + ACO.shortestDirections.size());
                ACO.writeRoute();
                tourDirections = new Stack<>();
            } else {
                tourDirections.clear();
            }
            tourEdge.clear();
            goalReached = true;
        }

//        if((tourDirections.size() > (ACO.stopCriterionRouteLength * ACO.shortestDirections.size())) && ACO.shortestDirections.size() != 0) {
//            tourDirections.clear();
//            tourEdge.clear();
//            goalReached = true;
//        }
    }

    /**
     * Moves the ant.
     */
    public void move() {
        if(goalReached)
            return;

        currentDirection = calcProbabilityMove();
        currentPos.move(currentDirection);
        tourDirections.push(currentDirection);

        // Add coordinates to visited list.
        visited.addCoordinates(getCurrentPos());
        tourEdge.addCoordinates(getCurrentPos());

        checkVertex();
        checkPosition();
    }

    /**
     *
     */
    public String toString() {
        return maze.toStringWithAnt(this);
    }
}
