package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by hung on 6-10-15.
 */
public class Matrix {

    private int rows;
    private int columns;
    private double[][] matrix;


    public Matrix(String file) {
        try {
            loadFile(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
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
}
