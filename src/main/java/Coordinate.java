package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to represent coordinates.
 */
public class Coordinate implements Comparable {

    private int row;
    private int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Coordinate(Coordinate coordinate) {
        this.row = coordinate.getRow();
        this.column = coordinate.getColumn();
    }

    /**
     * Move this coordinate in a direction.
     * @param direction enum direction
     */
    public void move(Direction direction) {
        switch(direction) {
            case NORTH:
                row -= 1;
                break;
            case EAST:
                column += 1;
                break;
            case SOUTH:
                row += 1;
                break;
            case WEST:
                column -= 1;
                break;
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * Extracts the coordinates from the maze coordinate files.
     * @param file String containing the path to the file.
     * @return List of coordinates.
     */
    public static List<Coordinate> readGoalCoordinates(String file) {
        List<Coordinate> goalCoordinates = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(file));
            Pattern pattern = Pattern.compile("(\\d+)..(\\d+);");
            Matcher matcher;
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                matcher = pattern.matcher(line);
                if(matcher.find()) {
                    Coordinate coord = new Coordinate(Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(1)));
                    goalCoordinates.add(coord);
                }
            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return goalCoordinates;
    }

    /**
     * Extracts the coordinates from the Tsp coordinate files.
     * @param file String containing the path to the file.
     * @return List of coordinates.
     */
    public static List<Coordinate> readTspCoordinates(String file) {
        List<Coordinate> tspCoordinates = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(file));
            Pattern pattern = Pattern.compile("^\\d+:\\s*(\\d+).\\s*(\\d+);");
            Matcher matcher;
            sc.nextLine();
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                matcher = pattern.matcher(line);
                if(matcher.find()) {
                    Coordinate coord = new Coordinate(Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(1)));
                    tspCoordinates.add(coord);
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return tspCoordinates;
    }

    @Override
    public String toString() {
        return "[" + row + ", " + column + "]";
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if(row != that.row) return false;
        return column == that.column;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 100 * result + column;
        return result;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object o) {
        if(this == o) return 0;
        if(o == null) throw new NullPointerException();
        if(getClass() != o.getClass()) throw new ClassCastException();

        Coordinate that = (Coordinate) o;
        int rowCompare = Integer.compare(this.getRow(), that.getRow());
        int columnCompare = Integer.compare(this.getColumn(), that.getColumn());

        if(rowCompare < 0) return rowCompare;
        if(rowCompare == 0) return columnCompare;
        return 1;
    }
}
