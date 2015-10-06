package main.java;

/**
 * Created by hung on 6-10-15.
 */
public class main {

    public static void main(String[] args) {
        Matrix matrix = new Matrix("/mnt/data/Development/computational_int/src/main/resources/easy_maze.txt");
        System.out.println(matrix.toString());
    }

}
