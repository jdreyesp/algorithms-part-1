/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

package week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;
import java.util.stream.Collectors;

public class PointSET {

    private final TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        this.points = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if(p == null) throw new IllegalArgumentException();
        points.add(new Point2D(p.x(), p.y()));
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if(p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        points.forEach(point -> point.draw());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if(rect == null) throw new IllegalArgumentException();
        return points.stream().filter(point -> rect.contains(point)).collect(Collectors.toSet());
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if(p == null) throw new IllegalArgumentException();
        if(isEmpty()) return null;

        return points.stream().sorted(p.distanceToOrder()).findFirst().get();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
