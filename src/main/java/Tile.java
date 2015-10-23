package main.java;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hung
 */
public class Tile {

    private Map<Direction, Double> tilePheromoneMap;
    private Coordinate tileCoordinate;

    public Tile(Coordinate coordinate) {
        this.tilePheromoneMap = new HashMap<>();
        this.tileCoordinate = new Coordinate(coordinate);
    }

    /**
     * Adds pheromone to the direction of the tile.
     * @param direction direction.
     * @param pheromone pheromone.
     */
    public void addPheromone(Direction direction, double pheromone) {
        tilePheromoneMap.put(direction, pheromone);
    }

    /**
     * Get the pheromone value for the given direction of the tile.
     * @param direction direction
     * @return pheromone
     */
    public double getPheromone(Direction direction) {
        return tilePheromoneMap.getOrDefault(direction, 1.d);
    }

    /**
     * Get the total pheromone value for this tile.
     * @return total pheromone value.
     */
    public double getTotalPheromone() {
        double pheromone = 0.d;
        for(Double aDouble : tilePheromoneMap.values()) {
            pheromone += aDouble;
        }
        return pheromone;
    }

    /**
     * Returns the tile coordinate.
     * @return tile coordinate.
     */
    public Coordinate getTileCoordinate() {
        return new Coordinate(tileCoordinate);
    }
}
