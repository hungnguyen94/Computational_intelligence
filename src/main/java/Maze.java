package main.java;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maze class to represent the maze.
 */
public class Maze {
    private int rows;
    private int columns;
    private double[][] matrix;
    private Map<Coordinate, Vertex> vertexMap;
    private Map<Coordinate, Double> pheromoneMap;
    private Map<Edge, Double> pheromoneQueue;
    private Map<Coordinate, Tile> pheromoneTileMap;

    // Save the coordinates of accessible points
    // and inaccessible points for drawing only.
    private List<Point> route;
    private List<Point> walls;


    /**
     * Construct matrix with file
     * @param file
     */
    public Maze(String file) {
        vertexMap = new ConcurrentHashMap<Coordinate, Vertex>();
        route = new ArrayList<Point>();
        walls = new ArrayList<Point>();
        pheromoneMap = new HashMap<>();
        pheromoneQueue = new HashMap<>();
        pheromoneTileMap = new HashMap<>();
        try {
            loadFile(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Used for gui only.
    public List<Point> getRoute() {
        return route;
    }

    // Used for gui only.
    public List<Point> getWalls() {
        return walls;
    }

    // Used for gui only.
    public Map<Point, Double> getPheromonedRoute() {
        Map<Point, Double> pheromonedRoute = new HashMap<>();
        double maxPheromone = 0.D;
        for(Double aDouble : pheromoneMap.values()) {
            maxPheromone = (aDouble>maxPheromone)?aDouble:maxPheromone;
        }
//        System.out.println(maxPheromone);

        for(Coordinate coordinate : pheromoneMap.keySet()) {
            Point p = new Point(coordinate.getColumn(), coordinate.getRow());
            pheromonedRoute.put(p, pheromoneMap.get(coordinate)/maxPheromone);
        }
        return pheromonedRoute;
    }

    public synchronized List<Point> getVertexPoints() {
        List<Point> pointsVertices = new ArrayList<>();
        for(Map.Entry<Coordinate, Vertex> coordinateVertexEntry : vertexMap.entrySet()) {
            Point point = new Point(coordinateVertexEntry.getKey().getColumn(), coordinateVertexEntry.getKey().getRow());
            pointsVertices.add(point);
        }
        return pointsVertices;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /**
     * Returns the value at the given
     * location in the maze.
     * @param row Row
     * @param column Column
     * @return Value at location.
     */
    public synchronized double getXY(int row, int column) {
        return matrix[row][column];
    }

    /**
     * Returns all the known vertices.
     * @return list of vertices.
     */
    public Map<Coordinate, Vertex> getAllVertex() {
        return vertexMap;
    }

    /**
     * Load matrix from a file.
     * @param file string location of matrix.
     * @throws FileNotFoundException
     */
    public void loadFile(String file) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(file));
        columns = sc.nextInt();
        rows = sc.nextInt();
        matrix = new double[rows][columns];
        while(sc.hasNext()) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    matrix[i][j] = sc.nextDouble();
                }
            }
        }
        sc.close();

        // Used for drawing only.
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if (getXY(i, j) == 1) {
                    route.add(new Point(j, i));
                    pheromoneMap.put(new Coordinate(i, j), ACO.startPheromoneValue);
                } else {
                    walls.add(new Point(j, i));
                }
            }
        }
    }

    /**
     * Returns the pheromone value at the given coordinate.
     * @param coordinate Coordinate.
     * @param direction Direction of the edge from the vertex.
     * @return
     */
    public double getPheromone(Coordinate coordinate, Direction direction) {
        Coordinate position = new Coordinate(coordinate);
        position.move(direction);
        return pheromoneMap.getOrDefault(position, ACO.startPheromoneValue);
    }

    /**
     * Increase pheromone on this edge.
     * @param edge Coordinates where pheromone has to be applied.
     * @param pheromone Amount it has to be increased by.
     */
    public void increasePheromone(Edge edge, double pheromone) {
        Collection<Coordinate> coordinates = edge.getCoordinates();
        for(Coordinate coordinate : coordinates) {
            double newPheromoneValue = pheromone + pheromoneMap.getOrDefault(coordinate, 0.d);
            pheromoneMap.put(coordinate, newPheromoneValue);
        }
    }

    /**
     * Enqueue the increasing of pheromone.
     * @param edge Coordinates where pheromone has to be applied.
     * @param pheromone Amount it has to be increased by.
     */
    public synchronized void enqueueIncreasePheromone(Edge edge, double pheromone) {
        Edge enqueuedEdge = new Edge(edge);
        pheromoneQueue.put(enqueuedEdge, pheromone);
    }

    /**
     * Decrease pheromove values for all coordinates
     * in the pheromoveMap.
     * Then apply the enqueued pheromone.
     */
    public synchronized void evaporatePheromone() {
        for(Map.Entry<Coordinate, Double> coordinateDoubleEntry : pheromoneMap.entrySet()) {
            double newPheromone = coordinateDoubleEntry.getValue() * (1.0D - ACO.evaporationConst);
            coordinateDoubleEntry.setValue(newPheromone);
        }
        applyPheromone();
    }

    /**
     * Apply the pheromone for all edges in the queue.
     */
    public synchronized void applyPheromone() {
        for(Map.Entry<Edge, Double> edgeDoubleEntry : pheromoneQueue.entrySet()) {
            increasePheromone(edgeDoubleEntry.getKey(), edgeDoubleEntry.getValue());
        }
        pheromoneQueue.clear();
    }

    /**
     * Return all the possible directions at a given coordinate.
     * @param position current position.
     * @return list of directions.
     */
    public List<Direction> getPossibleDirections(Coordinate position) {
        int currentRow = position.getRow();
        int currentColumn = position.getColumn();
        int maxRows = rows - 1;
        int maxColumns = columns - 1;
        List<Direction> directions = new ArrayList<Direction>();

        if(currentRow != maxRows && getXY(currentRow+1, currentColumn) == 1)
            directions.add(Direction.SOUTH);

        if(currentColumn != maxColumns && getXY(currentRow, currentColumn+1) == 1)
            directions.add(Direction.EAST);

        if(currentRow != 0 && getXY(currentRow-1, currentColumn) == 1)
            directions.add(Direction.NORTH);

        if(currentColumn != 0 && getXY(currentRow, currentColumn-1) == 1)
            directions.add(Direction.WEST);

        return directions;
    }

    /**
     * returns the sum of the neighbours.
     * @param coordinate coordinate
     * @return
     */
    public double sumNeighbours(Coordinate coordinate) {
        int row = coordinate.getRow();
        int column = coordinate.getColumn();
        double sum = 0;
        double north, east, south, west, northwest, southwest, northeast, southeast;
        south = (row != rows-1)? getXY(row+1, column): 0;
        east = (column != columns-1)? getXY(row, column+1): 0;
        north = (row != 0)? getXY(row-1, column): 0;
        west = (column != 0)? getXY(row, column-1): 0;

        northwest = (row != rows-1)&&(column != 0)? getXY(row+1,column-1): 0;
        northeast = (row != rows-1)&&(column != columns-1)? getXY(row+1,column+1): 0;
        southwest = (row != 0)&&(column != 0)? getXY(row-1,column-1): 0;
        southeast = (row != 0)&&(column != columns-1)? getXY(row-1,column+1): 0;

        sum = north + east + south + west;
        sum += northwest + northeast + southeast + southwest;
        return sum;
    }

    /**
     * Voeg vertex toe aan de lijst.
     * @param v
     */
    public void addVertex(Vertex v) {
        if(!vertexMap.containsKey(v.getVertexCoordinate())) {
            vertexMap.put(v.getVertexCoordinate(), v);
        }
    }

    /**
     * Returns the vertex at the given coordinate if it exist.
     * Else return null.
     * @param coordinate coordinate
     * @return the vertex at coordinate
     */
    public Vertex getVertex(Coordinate coordinate) {
        return getAllVertex().getOrDefault(coordinate, null);
    }

    /**
     * Print matrix
     */
    public String toString() {
        String mString = "";
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                    mString += (int)matrix[i][j] + " ";
            }
            mString += "\n";
        }
        return mString;
    }

    /**
     * Print matrix with ant.
     */
    public String toStringWithAnt(Ant ant) {
        String mString = "";
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(ant.getCurrentPos().equals(new Coordinate(i, j)))
                    mString += "\033[32;1mA ";
                else {
                    mString += (((int) matrix[i][j]) == 1)? "\033[31;0m\u2022": "\033[30;1m\u2588";
                    mString += " ";
                }
            }
            mString += "\n";
        }
        return mString;
    }
}
