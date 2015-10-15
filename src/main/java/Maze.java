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

/**
 * Maze class to represent the maze.
 */
public class Maze {

    private int rows;
    private int columns;
    private double[][] matrix;
    private Collection<Vertex> vertexList;
    private Map<Coordinate, Double> pheromoneMap;

    // Save the coordinates of accessible points
    // and inaccessible points for drawing only.
    private List<Point> route;
    private List<Point> walls;


    /**
     * Construct matrix with file
     * @param file
     */
    public Maze(String file) {
        vertexList = new ArrayList<Vertex>();
        route = new ArrayList<Point>();
        walls = new ArrayList<Point>();
        pheromoneMap = new HashMap<>();
        try {
            loadFile(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Maze(int rows, int columns) {
        matrix = new double[rows][columns];
        vertexList = new ArrayList<Vertex>();
        route = new ArrayList<Point>();
        walls = new ArrayList<Point>();
        pheromoneMap = new HashMap<>();
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
        double totalPheromone = 0.D;
        for(Double aDouble : pheromoneMap.values()) {
            totalPheromone = aDouble>totalPheromone?aDouble:totalPheromone;
        }

        for(Coordinate coordinate : pheromoneMap.keySet()) {
            Point p = new Point(coordinate.getColumn(), coordinate.getRow());
            double pval = (pheromoneMap.get(coordinate))/totalPheromone;
//            System.out.println(pval);
            pheromonedRoute.put(p, pheromoneMap.get(coordinate)/totalPheromone);
        }
        return pheromonedRoute;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /**
     * Returns all the known vertices.
     * @return list of vertices.
     */
    public Collection<Vertex> getAllVertex() {
        return vertexList;
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
        //calculateEdges();

        // Used for drawing only.
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if (matrix[i][j] == 1) {
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
        return pheromoneMap.get(position);
//        return pheromoneMap.getOrDefault(position, ACO.startPheromoneValue);
    }

    /**
     * Increase pheromone on this edge.
     * @param edge
     * @param pheromoneRate
     */
    public void increasePheromone(Edge edge, double pheromoneRate) {
        Collection<Coordinate> coordinates = edge.getCoordinates();
        for(Coordinate coordinate : coordinates) {
            double newPheromoneValue = pheromoneRate + pheromoneMap.get(coordinate);
            pheromoneMap.put(coordinate, newPheromoneValue);
        }
    }

    /**
     * Decrease pheromove values for all coordinates
     * in the pheromoveMap.
     */
    public void evaporatePheromone() {
        for(Map.Entry<Coordinate, Double> coordinateDoubleEntry : pheromoneMap.entrySet()) {
            double newPheromone = coordinateDoubleEntry.getValue() * (1.0D - ACO.evaporationConst);
            coordinateDoubleEntry.setValue(newPheromone);
        }
    }

    /**
     * Return all the possible directions at a given coordinate.
     * @param position current position.
     * @return list of directions.
     */
    public List<Direction> getPossibleDirections(Coordinate position) {
        int row = position.getRow();
        int column = position.getColumn();
        List<Direction> directions = new ArrayList<Direction>();

        if(row != rows-1 && matrix[row+1][column] == 1)
            directions.add(Direction.SOUTH);

        if(column != columns-1 && matrix[row][column+1] == 1)
            directions.add(Direction.EAST);

        if(row != 0 && matrix[row-1][column] == 1)
            directions.add(Direction.NORTH);

        if(column != 0 && matrix[row][column-1] == 1)
            directions.add(Direction.WEST);

        return directions;
    }

    /**
     * returns the sum of the neighbours.
     * @param row x
     * @param column y
     * @return
     */
    public double sumNeighbours(int row, int column) {
        double sum = 0;
        double north, east, south, west;
        north = (row != rows)? matrix[row+1][column]: 0;
        east = (column != columns)? matrix[row][column+1]: 0;
        south = (row != 0)? matrix[row-1][column]: 0;
        west = (column != 0)? matrix[row][column-1]: 0;
        sum = north + east + south + west;
        return sum;
    }

    /**
     * Slaat alle vertexList op in vertexList arraylist
     * WORDT NIET GEBRUIKT.
     */
    public void calculateEdges() {
        for(int i = 0; i < rows -1; i++) {
            for(int j = 0; j < columns-1; j++) {
                if(matrix[i][j] == 1) {
                    if(sumNeighbours(i, j) >= 3) {
                        vertexList.add(new Vertex(new Coordinate(i, j)));
                    }
                }
            }
        }
        System.out.println(vertexList);
    }

    /**
     * Voeg vertex toe aan de lijst.
     * @param v
     */
    public void addVertex(Vertex v) {
        for(Vertex vertex : getAllVertex()) {
            if(vertex.equals(v))
                return;
        }
        vertexList.add(v);
    }

    /**
     * Returns the vertex at the given coordinate if it exist.
     * Else return null.
     * @param coordinate coordinate
     * @return the vertex at coordinate
     */
    public Vertex getVertex(Coordinate coordinate) {
        if(getAllVertex().contains(new Vertex(coordinate))) {
            for(Vertex vertex : getAllVertex()) {
                if(vertex.getVertexCoordinate().equals(coordinate))
                    return vertex;
            }
        }
        return null;
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
