package main.java;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Class to represent edges.
 */
public class Edge implements Comparable {
    private Collection<Coordinate> coordinateList;

    public Edge() {
        this.coordinateList = new LinkedHashSet<>();
    }

    public Edge(Edge edge) {
        this.coordinateList = new LinkedHashSet<>(edge.getCoordinates());
        for(Coordinate coordinate : edge.getCoordinates()) {
            edge.addCoordinates(coordinate);
        }
    }

    /**
     * Add a coordinate belonging to this edge.
     * @param coord coordinate.
     */
    public void addCoordinates(Coordinate coord) {
        coordinateList.add(new Coordinate(coord));
    }

    public int getSize() {
        return coordinateList.size();
    }

    public void removeCoordinates(Coordinate coord) {
        coordinateList.remove(coord);
    }

    public void clear() {
        coordinateList.clear();
    }

    /**
     * Returns all coordinates in this edge.
     * @return all coordinates in this edge.
     */
    public Collection<Coordinate> getCoordinates() {
        return coordinateList;
    }

    /**
     * Check if coordinate is in this edge.
     * @param coord coordinates
     * @return True if coordinate is in edge.
     */
    public boolean containsCoordinate(Coordinate coord) {
        boolean contained = coordinateList.contains(coord);
        return contained;
    }

    @Override
    public String toString() {
        String output = "Edges: ";
        for(Coordinate coordinate : coordinateList) {
            output += coordinate + " ";
        }
        return output;
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

        Edge that = (Edge) o;
        return (this.getSize() > that.getSize())? 1: (this.getSize() < that.getSize())? -1: 0;
    }
}
