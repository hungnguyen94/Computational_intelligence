package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author hung
 */
public class Ant {

    private List<Coordinate> tspGoals;

    private Coordinate currentPos;
    private Maze maze;
    private Stack<Direction> tourDirections;
    private Edge tourEdge;
    private boolean goalReached;
    private Direction currentDirection;
    private Vertex lastVertex;
    private Edge visited;


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

        Direction opposite = getOpposite(currentDirection);
        // Delete opposite direction if
        // there are more than 2 possibilities
        // (so when you arent stuck)
//        if(directionList.size() >= 2 && directionList.contains(opposite)) {
//            directionList.remove(opposite);
//        }

        for(Direction direction : directionList) {
            double currentPheromone = maze.getPheromone(position, direction);
            Vertex vertex = maze.getVertex(getCurrentPos());
//            Edge edge = vertex != null?vertex.getEdge(direction): null;
            // If edge length is unknown, use 5?
            Edge edge = null;
            int lengthEdge = (edge != null)? edge.getSize(): 3;
            double probability = Math.pow(currentPheromone, ACO.alpha) * Math.pow(1.0D/lengthEdge, ACO.beta);

//            System.out.println("prob in map: " + probability
//                    + "\ncurrentpheromone: " + currentPheromone
//                    + "\nedgelength: " + lengthEdge);
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
        List<Direction> possibleDirections = maze.getPossibleDirections(getCurrentPos());
        Direction opposite = getOpposite(currentDirection);

        // Delete opposite direction if
        // there are more than 2 possibilities
        // (so when you arent stuck)
//        if(possibleDirections.size() >= 2 && possibleDirections.contains(opposite)) {
//            possibleDirections.remove(opposite);
//        }

        // If current position is on a vertex.
//        if(maze.getVertex(getCurrentPos()) != null) {
        if(possibleDirections.size() >= 3) {
            Map<Direction, Double> probabilityDirectionMap = getAllProbability(getCurrentPos());
            double random = Math.random();
            double sumProbability = 0;

            for(Map.Entry<Direction, Double> directionDoubleEntry : probabilityDirectionMap.entrySet()) {
                double prob = directionDoubleEntry.getValue() * 0.9d;
                sumProbability += prob;
//                System.out.println("Direction: \t" + directionDoubleEntry.getKey() + "\tProbability: " + prob);
                if(random < sumProbability) {
//                    System.out.println("CHOSEN: \t" + directionDoubleEntry.getKey());
                    return directionDoubleEntry.getKey();
                }
            }
        }

        int random = (int) (Math.random() * possibleDirections.size());
        Direction nextMove = possibleDirections.get(random);
        return nextMove;
    }

    public boolean isGoalReached() {
        return goalReached;
    }

    /**
     * If current position has more than 2 possible directions, then it must be a vertex.
     * Add a new vertex if it doesn't exist yet and connect them.
     */
    private void checkVertex() {
        // If true, then current position is a vertex.
        int possibleDirections = maze.getPossibleDirections(getCurrentPos()).size();
        boolean possibleDirectionsBool = possibleDirections >= 3;
        boolean sumNeighboursSmallerThan = maze.sumNeighbours(getCurrentPos()) <= possibleDirections + 1;
        boolean isTspCoordinate = ACO.tspCoordinates.contains(getCurrentPos());
        boolean isGoalCoordinate = ACO.goalCoordinates.contains(getCurrentPos());

        if(isTspCoordinate || isGoalCoordinate) {
            Vertex vertexHere =  maze.getVertex(getCurrentPos());
            // True if the vertex already exists.
            if(vertexHere == null) {
                // Add the new vertex.
                vertexHere = new Vertex(getCurrentPos());

            }
            vertexHere.addVertex(lastVertex, visited);
            lastVertex.addVertex(vertexHere, visited);
            visited = new Edge();
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
            // Pheromone value calculation and apply.
            double pheromoneValue = Math.pow(ACO.pheromoneDropRate, ACO.alpha) * (1.0D / Math.pow(tourDirections.size(), ACO.beta));
            maze.increasePheromone(tourEdge, pheromoneValue);

            if(ACO.shortestDirections.size() > tourDirections.size() || ACO.shortestDirections.size() == 0) {
                ACO.shortestDirections = tourDirections;
                System.out.println("\nShortest route: " + ACO.shortestDirections.size());
//                System.out.println(shortestDirections + "\n" + tourEdge);
//                System.out.println(ACO.routeToString());
                ACO.writeRoute();
                tourDirections = new Stack<>();
            } else {
                tourDirections.clear();
            }
            tourEdge.clear();
            goalReached = true;
        }

        if((tourDirections.size() > (5 * ACO.shortestDirections.size())) && ACO.shortestDirections.size() != 0) {
            tourDirections.clear();
            tourEdge.clear();
            goalReached = true;
        }
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
