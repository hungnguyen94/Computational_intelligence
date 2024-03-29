package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hung
 */
public class EliteAnt extends Ant {

    private Edge followingEdge;

    /**
     * Contructor for ant with starting position x,y
     *
     * @param currentPos xy currentPos
     * @param maze
     */
    public EliteAnt(Coordinate currentPos, Maze maze) {
        super(currentPos, maze);
        followingEdge = new Edge();
    }


    /**
     * Calculates the probability and
     * returns a map with all the directions mapped
     * to a probability value.
     * @param position Vertex.
     * @return Map of directions mapped to probability.
     */
    @Override
    public synchronized Map<Direction, Double> getAllProbability(Coordinate position) {
        List<Direction> directionList = maze.getPossibleDirections(position);
        Map<Direction, Double> pheromoneDirectionMap = new HashMap<>();
        double totalProbability = 0;

//        double highestPheromone = 0.d;
//        Direction highestDirection = Direction.NONE;
//        for(Direction direction : directionList) {
//            if(maze.getPheromone(position, direction) > highestPheromone) {
//                highestDirection = direction;
//                highestPheromone = maze.getPheromone(position, direction);
//            }
//        }
//        pheromoneDirectionMap.put(highestDirection, 10.d);
//        return pheromoneDirectionMap;

//        // Delete opposite direction if it isn't an one way edge.
//        if(maze.sumNeighbours(position) > 4 && directionList.size() > 2)
//            directionList.remove(getOpposite(currentDirection));

        for(Direction direction : directionList) {
            double currentPheromone = maze.getPheromone(position, direction);
            Vertex vertex = maze.getVertex(getCurrentPos());
            Edge edge = vertex != null?vertex.getEdge(direction): null;
            int lengthEdge = (edge != null)? edge.getSize(): 20;
            double probability = Math.pow(currentPheromone, 10) * Math.pow(1.0D/lengthEdge, ACO.beta);

            // Give lower probability to positions that already have been visited.
            Coordinate nextPosition = getCurrentPos();
            nextPosition.move(direction);
            if(tourEdge.containsCoordinate(nextPosition)) {
                probability *= 0.1d;
            }
            // Give lower probability for the opposite direction.
            probability = (direction == getOpposite(currentDirection))? probability*0.1d: probability;

            pheromoneDirectionMap.put(direction, probability);
            totalProbability += probability;
        }

        for(Map.Entry<Direction, Double> directionDoubleEntry : pheromoneDirectionMap.entrySet()) {
            directionDoubleEntry.setValue(directionDoubleEntry.getValue() / totalProbability);
        }
        return pheromoneDirectionMap;
    }

    /**
     * Moves the ant.
     */
    @Override
    public void move() {
        if(goalReached)
            return;

        currentDirection = calcProbabilityMove();
//        System.out.println(currentPos + ": " + currentDirection);
        currentPos.move(currentDirection);
        tourDirections.push(currentDirection);

        // Add coordinates to visited list.
        visited.addCoordinates(getCurrentPos());
        tourEdge.addCoordinates(getCurrentPos());

        super.checkVertex();
        super.checkPosition();
    }

    private Direction followEdge() {
        if(followingEdge == null || followingEdge.getSize() <= 0) {
            if(maze.getVertex(getCurrentPos()) == null) {
                return calcProbabilityMove();
            }
            Vertex currentVertex = maze.getVertex(getCurrentPos());
            List<Vertex> goalVertexList = new ArrayList<>();
            for(Coordinate tspGoal : tspGoals) {
                goalVertexList.add(maze.getVertex(tspGoal));
            }
            goalVertexList.add(maze.getVertex(ACO.goalCoordinate));
            followingEdge = currentVertex.getShortestEdge(goalVertexList);
        }
        if(followingEdge != null && followingEdge.containsCoordinate(getCurrentPos())) {
            followingEdge.removeCoordinates(getCurrentPos());
            for(Direction direction : maze.getPossibleDirections(getCurrentPos())) {
                Coordinate nextCoordinate = getCurrentPos();
                nextCoordinate.move(direction);
                if(followingEdge.containsCoordinate(nextCoordinate)) {
                    return direction;
                }
            }
        }
        return calcProbabilityMove();
    }
}
