import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class FastCollinearPoints {

    private Point[] points;


    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        checkNullPoints(points);
        this.points = points;
        Arrays.sort(this.points, this.points[0].slopeOrder());
        checkIfCollinear();
    }

    private void checkIfCollinear() {
        int numOfEquals = 0;
        final Point origin = points[0];
        double currentSlope = Double.NEGATIVE_INFINITY;
        boolean collinears = false;

        for(int i = 1; i < points.length; i++) {
                Point p = points[i];
                double calculatedSlope = origin.slopeTo(p);

                if(calculatedSlope > currentSlope) {
                    numOfEquals=0;
                    currentSlope = calculatedSlope;
                }
                if(origin.slopeTo(p) == currentSlope) numOfEquals++;
                if(numOfEquals >= 3) {
                    System.out.println("Points are collinear at least with 3 points!");
                    collinears = true;
                    break;
                }
        }

        if(!collinears) System.out.println("Points are not collinear!");
    }

    // the number of line segments
    public int numberOfSegments() {
        return 0;
    }



    // the line segments
    public LineSegment[] segments() {
        class PointSlopes {
            private Point target;
            private double slope;

            private PointSlopes(Point target, double slope) {
                this.target = target;
                this.slope = slope;
            }
        }

        Queue<PointSlopes> pointSlopes = new ArrayDeque<>();
        Point initialLineSegmentPoint = points[0];

        for (int i = 1; i < points.length; i++) {
            Point currentPoint = points[i];

            if(currentPoint == null) continue;

            double slope = initialLineSegmentPoint.slopeTo(currentPoint);
            boolean slopeFound = false;
            for(PointSlopes pointSlope : pointSlopes) {
                if(pointSlope.slope == slope) {
                    pointSlope.target = currentPoint;
                    slopeFound = true;
                    break;
                }
            }

            if(!slopeFound) {
                pointSlopes.add(new PointSlopes(currentPoint, slope));
            }
        }

        int pointSize = pointSlopes.size();
        final LineSegment[] segments = new LineSegment[pointSize];

        for (int i = 0; i < pointSize; i++) {
            segments[i] = new LineSegment(points[0], pointSlopes.poll().target);
        }

        return segments;
    }

    private void checkNullPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for(Point point : points) {
            if(point == null) throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {

//        final Point[] points = new Point[] {
//                new Point(0,0),
//                new Point(1, 1),
//                new Point(2, 3),
//                new Point(3, 3)
//        };
//
//        final Point[] points2 = new Point[] {
//                new Point(0,0),
//                new Point(1, 1),
//                new Point(2, 2),
//                new Point(3, 3),
//                new Point(4,4),
//                new Point(1, 1),
//                new Point(2, 2),
//                new Point(3, 4)
//        };
//
//        new FastCollinearPoints(points);
//        new FastCollinearPoints(points2);

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        LineSegment[] segments = collinear.segments();
        for (LineSegment segment : segments) {
            StdOut.println(segment);
            if(segment != null) {
                segment.draw();
            }

        }
        StdDraw.show();

    }
}
