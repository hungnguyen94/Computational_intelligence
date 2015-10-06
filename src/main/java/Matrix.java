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
    private List<Coordinates> edges;


    /**
     * Construct matrix with file
     * @param file
     */
    public Matrix(String file) {
        edges = new ArrayList<Coordinates>();
        try {
            loadFile(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Matrix(int rows, int columns) {
        matrix = new double[rows][columns];
        edges = new ArrayList<Coordinates>();
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
     * Slaat alle edges op in edges arraylist
     */
    public void calculateEdges() {
        for(int i = 1; i < rows -1; i++) {
            for(int j = 1; j < columns-1; j++) {
                if(matrix[i][j] == 1) {
                    if(sumNeighbours(i, j) >= 3)
                        edges.add(new Coordinates(i, j));
                }
            }
        }
        System.out.println(edges);
    }

    /**
     * Print matrix
     */
    public String toString() {
        String mString = "";
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(edges.contains(new Coordinates(i, j)))
                    mString += "E ";
                else
                    mString += (int)matrix[i][j] + " ";
            }
            mString += "\n";
        }
        return mString;
    }
}
