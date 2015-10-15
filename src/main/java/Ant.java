package main.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author hung
 */
public class Ant {

    private Coordinate goalPosition = ACO.goalPosition;
    public static Stack<Direction> shortestDirections = new Stack<>();

    private Coordinate currentPos;
    private Maze maze;
    private Stack<Direction> tourDirections;
    private Edge tourEdge;
    private boolean vertexReached;
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
        this.currentDirection = Direction.NORTH;
        this.tourDirections = new Stack<Direction>();
        this.vertexReached = false;
        this.lastVertex = new Vertex(getCurrentPos());
        this.visited = new Edge();
        this.tourEdge = new Edge();
//        shortestDirections = tourDirections;
        checkVertex();
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
    Direction getOpposite(Direction d) {
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
     * @param vertex Vertex.
     * @return Map of directions mapped to probability.
     */
    public Map<Direction, Double> getAllProbability(Vertex vertex) {
        Coordinate position = vertex.getVertexCoordinate();
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
//            System.out.println("Pheromone for direction " + direction + ": " + currentPheromone);
//            Edge edge = vertex.getEdge(direction);
            // If edge length is unknown, use 5?
            Edge edge = null;
            int lengthEdge = (edge != null)? edge.getSize(): 3;
            double probability = Math.pow(currentPheromone, ACO.alpha) * Math.pow(1.0D/lengthEdge, ACO.beta);

/*            System.out.println("prob in map: " + probability
                    + "\ncurrentpheromone: " + currentPheromone
                    + "\nedgelength: " + lengthEdge);*/
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
        if(possibleDirections.size() >= 2 && possibleDirections.contains(opposite)) {
            possibleDirections.remove(opposite);
        }

//        System.out.println("Possible directions: " + possibleDirections.size());
        // If current position is on a vertex.
        if(maze.getVertex(getCurrentPos()) != null) {
            Map<Direction, Double> probabilityDirectionMap = getAllProbability(maze.getVertex(getCurrentPos()));
            double random = Math.random();
            double sumProbability = 0;
//            for(Map.Entry<Direction, Double> directionDoubleEntry : probabilityDirectionMap.entrySet()) {
//                System.out.println("Direction: \t" + directionDoubleEntry.getKey() + "\tProbability: " + directionDoubleEntry.getValue());
//            }
            for(Map.Entry<Direction, Double> directionDoubleEntry : probabilityDirectionMap.entrySet()) {
                double prob = Math.min(directionDoubleEntry.getValue(), 0.99D);
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
        return vertexReached;
    }

    public void setGoalReached(boolean bool) {
        vertexReached = bool;
    }


    /**
     * If current position has more than 2 possible directions, then it must be a vertex.
     * Add a new vertex if it doesn't exist yet and connect them.
     */
    private void checkVertex() {
        // If true, then current position is a vertex.
        if(maze.getPossibleDirections(getCurrentPos()).size() >=  3) {
            Vertex vertexHere =  maze.getVertex(getCurrentPos());
            // True if the vertex already exists.
            if(vertexHere == null) {
                // Add the new vertex.
                vertexHere = new Vertex(getCurrentPos());
                vertexHere.addVertex(lastVertex, visited);
                lastVertex.addVertex(vertexHere, visited);
                visited.clear();
                maze.addVertex(vertexHere);
                maze.addVertex(lastVertex);
            }
            lastVertex = vertexHere;
            visited.clear();
        }
    }

    /**
     * Moves the ant.
     */
    public void move() {
        currentDirection = calcProbabilityMove();
        currentPos.move(currentDirection);
        tourDirections.push(currentDirection);

        // Add coordinates to visited list.
        visited.addCoordinates(getCurrentPos());
        tourEdge.addCoordinates(getCurrentPos());
        checkVertex();

        if(getCurrentPos().equals(goalPosition)) {
            // Pheromone value calculation and apply.
            double pval = Math.pow(ACO.pheromoneDropRate, ACO.alpha) * (1.0D / Math.pow(tourEdge.getSize(), ACO.beta));
//            System.out.println(pval + " \tlength: " + tourEdge.getSize());
            maze.increasePheromone(tourEdge, pval);
//            maze.evaporatePheromone();

            if(shortestDirections.size() > tourDirections.size() || shortestDirections.size() == 0) {
                shortestDirections = tourDirections;
                System.out.println("shortest: " + shortestDirections.size() + "\ttour: " + tourDirections.size());
                System.out.println(shortestDirections);
                tourDirections = new Stack<>();
            } else {
                tourDirections.clear();
            }

            tourEdge.clear();
            vertexReached = true;

//            goalPosition = goalPosition.equals(ACO.startingPosition)? ACO.goalPosition: ACO.startingPosition;
        }
    }

    /**
     *
     */
    public String toString() {
        return maze.toStringWithAnt(this);
    }
}
