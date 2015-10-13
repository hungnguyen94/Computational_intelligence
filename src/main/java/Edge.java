package main.java;

import java.util.Set;
import java.util.TreeSet;

/**
 * Edge class.
 */
public class Edge {
    private Set<Coordinates> coordinatesList;

    public Edge() {
        this.coordinatesList = new TreeSet<Coordinates>();
    }

    public void addCoordinates(Coordinates coord) {
        coordinatesList.add(coord);
    }

    public int getSize() {
        return coordinatesList.size();
    }

    public void removeCoordinates(Coordinates coord) {
        coordinatesList.remove(coord);
    }
}
