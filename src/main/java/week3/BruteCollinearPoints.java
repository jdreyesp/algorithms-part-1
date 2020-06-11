import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class BruteCollinearPoints {

    private Point[] points4by4;
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkNullPoints(points);
        Arrays.sort(points, points[0].slopeOrder());
        for (int i = 0; i < points.length; i+=4) {
            this.points4by4 = subArray(points, i, i+4);
            this.lineSegments = segments();
            checkIfCollinear();
        }
    }

    private Point[] subArray(Point[] originalPoints, int from, int to) {
        Point[] points = new Point[4];
        for(int i = from, j = 0; i < to; i++, j++) {
            if(i == originalPoints.length) break;
            points[j] = originalPoints[i];
        }
        return points;
    }

    private void checkIfCollinear() {
        int lineSegmentsCount = numberOfSegments();
        System.out.println("Number of segments calculated: " + lineSegmentsCount);
        if(lineSegmentsCount == 1) System.out.println("Points are collinear!");
        else System.out.println("Points are not collinears");
    }

    // the number of line segments
    public int numberOfSegments() {
        int lineSegmentCount = 0;
        for(int i = 0; i < lineSegments.length; i++) {
            if(lineSegments[i] != null) {
                lineSegmentCount++;
            }
        }
        return lineSegmentCount;
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
        Point initialLineSegmentPoint = points4by4[0];

        for (int i = 1; i < points4by4.length; i++) {
            Point currentPoint = points4by4[i];

            if(currentPoint == null) continue;

            double slope = initialLineSegmentPoint.slopeTo(currentPoint);
            boolean slopeFound = false;
            for(PointSlopes pointSlope : pointSlopes) {
                if(pointSlope.slope == slope) {
                    pointSlope.target = currentPoint;
                    slopeFound = true;
                }
            }

            if(!slopeFound) {
                pointSlopes.add(new PointSlopes(currentPoint, slope));
            }
        }

        LineSegment[] segments = new LineSegment[pointSlopes.size()];

        int pointSize = pointSlopes.size();
        for (int i = 0; i < pointSize; i++) {
            segments[i] = new LineSegment(points4by4[0], pointSlopes.poll().target);
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
//                new Point(2, 2),
//                new Point(3, 3)
//                /*
//
//                19000 10000
//                18000 10000
//                32000 10000
//                21000 10000
//                1234 5678
//                14000 10000
//                 */
//        };
//
//        final Point[] points2 = new Point[] {
//                new Point(19000,10000),
//                new Point(18000, 10000),
//                new Point(32000, 10000),
//                new Point(21000, 10000),
//                new Point(1234, 5678),
//                new Point(14000, 10000)
//        };
//
//        new BruteCollinearPoints(points);
//        new BruteCollinearPoints(points2);

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
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
