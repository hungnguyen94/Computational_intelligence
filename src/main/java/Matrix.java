package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by hung on 6-10-15.
 */
public class Matrix {

    private int rows;
    private int columns;
    private double[][] matrix;
    private List<Coordinates> vertex;
    private List<Coordinates> deadEnds;


    /**
     * Construct matrix with file
     * @param file
     */
    public Matrix(String file) {
        vertex = new ArrayList<Coordinates>();
        deadEnds = new ArrayList<Coordinates>();
        try {
            loadFile(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Matrix(int rows, int columns) {
        matrix = new double[rows][columns];
        vertex = new ArrayList<Coordinates>();
        deadEnds = new ArrayList<Coordinates>();
    }

    /**
     * Returns the value at position xy in the matrix.
     * @param x
     * @param y
     * @return
     */
    public double getXY(int x, int y) {
        return matrix[x][y];
    }

    public List<Coordinates> getAllVertex() {
        return vertex;
    }

    /**
     * Laad matrix van een file.
     * @param file
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
        calculateEdges();
    }

    public List<Direction> getPossibleDirections(Coordinates position) {
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
     * returns the sum of the neighbours
     * @param i x
     * @param j y
     * @return
     */
    public double sumNeighbours(int i, int j) {
        double sum = 0;
        double north, east, south, west;
        north = (i != rows)? matrix[i+1][j]: 0;
        east = (j != columns)? matrix[i][j+1]: 0;
        south = (i != 0)? matrix[i-1][j]: 0;
        west = (j != 0)? matrix[i][j-1]: 0;
        sum = north + east + south + west;
        return sum;
    }

    /**
     * Slaat alle vertex op in vertex arraylist
     */
    public void calculateEdges() {
        for(int i = 0; i < rows -1; i++) {
            for(int j = 0; j < columns-1; j++) {
                if(matrix[i][j] == 1) {
                    if(sumNeighbours(i, j) >= 3) {
                        vertex.add(new Coordinates(i, j));
                    } else if(sumNeighbours(i, j) == 1) {
                        deadEnds.add(new Coordinates(i,j));
                    }
                }
            }
        }
        System.out.println(vertex);
    }

    /**
     * Print matrix
     */
    public String toString() {
        String mString = "";
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {

                if(vertex.contains(new Coordinates(i, j)))
                    mString += "V ";
                else if(deadEnds.contains(new Coordinates(i, j))) {
                    mString += "D ";
                } else
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
                if(ant.getCurrentPos().equals(new Coordinates(i, j)))
                    mString += "\033[32;1mA ";
                else if(vertex.contains(new Coordinates(i, j)))
                    mString += "\033[35;0mV ";
                else if(deadEnds.contains(new Coordinates(i, j))) {
                    mString += "\033[35;0mD ";
                } else {
                    mString += (((int) matrix[i][j]) == 1)? "\033[31;0m\u2022": "\033[30;1m\u2588";
                    mString += " ";
                }
            }
            mString += "\n";
        }
        return mString;
    }
}
