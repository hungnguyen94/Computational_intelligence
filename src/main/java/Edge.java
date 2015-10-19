package main.java;

import java.util.Collection;
import java.util.HashSet;

/**
 * Class to represent edges.
 */
public class Edge {
    private Collection<Coordinate> coordinateList;

    public Edge() {
        this.coordinateList = new HashSet<>();
    }

    /**
     * Add a coordinate belonging to this edge.
     * @param coord coordinate.
     */
    public void addCoordinates(Coordinate coord) {
        coordinateList.add(new Coordinate(coord));
    }

    public int getSize() {
        return coordinateList.size();
    }

    public void removeCoordinates(Coordinate coord) {
        coordinateList.remove(coord);
    }

    public void clear() {
        coordinateList.clear();
    }

    /**
     * Returns all coordinates in this edge.
     * @return all coordinates in this edge.
     */
    public Collection<Coordinate> getCoordinates() {
        return coordinateList;
    }

    /**
     * Check if coordinate is in this edge.
     * @param coord coordinates
     * @return True if coordinate is in edge.
     */
    public boolean containsCoordinate(Coordinate coord) {
        boolean contained = coordinateList.contains(coord);
        return contained;
    }

    @Override
    public String toString() {
        String output = "Edges: ";
        for(Coordinate coordinate : coordinateList) {
            output += coordinate + " ";
        }
        return output;
    }
}
