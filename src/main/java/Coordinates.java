package main.java;

/**
 * Created by hung on 6-10-15.
 */
public class Coordinates {

    private int row;
    private int column;

    public Coordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Coordinates: " +
                row + ", " + column;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if(row != that.row) return false;
        return column == that.column;

    }

//    @Override
//    public int hashCode() {
//        int result = row;
//        result = 31 * result + column;
//        return result;
//    }
}
