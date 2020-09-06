/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

package week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class KdTree {

    private Node node;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private KdTree lb;        // the left/bottom subtree
        private KdTree rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect, KdTree lb, KdTree rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return node == null;
    }

    // number of points in the set
    public int size() {
        return 1 + node.lb.size() + node.rt.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if(p == null) throw new IllegalArgumentException();
        if(isEmpty()) {
            node = new Node(p, null, null, null);
            return;
        }

        NodeInsertion nodeInsertion = findInsertionPoint(p);
        if(nodeInsertion != null && nodeInsertion.insertionPoint == NodeInsertion.InsertionPoint.lb) {
            nodeInsertion.node.lb = new KdTree();
            nodeInsertion.node.lb.node = new Node(p, null, null, null);
        } else if(nodeInsertion != null && nodeInsertion.insertionPoint == NodeInsertion.InsertionPoint.rt) {
            nodeInsertion.node.rt = new KdTree();
            nodeInsertion.node.rt.node = new Node(p, null, null, null);
        }
    }

    private static class NodeInsertion {
        Node node;
        InsertionPoint insertionPoint;

        public enum InsertionPoint {
            lb, rt
        }

        public NodeInsertion(Node node, InsertionPoint insertionPoint) {
            this.node = node;
            this.insertionPoint = insertionPoint;
        }

    }
    private NodeInsertion findInsertionPoint(Point2D p) {
        Node x = node; //root
        int depth = 0;
        while(true) {
            final Comparator<Point2D> comparator = depth % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(p, x.p);
            if(cmp < 0) {
                if(x.lb == null) {
                    return new NodeInsertion(x, NodeInsertion.InsertionPoint.lb);
                } else {
                    x = x.lb.node;
                    depth++;
                    continue;
                }
            }
            else if (cmp > 0) {
                if(x.rt == null) {
                    return new NodeInsertion(x, NodeInsertion.InsertionPoint.rt);
                } else {
                    x = x.rt.node;
                    depth++;
                    continue;
                }
            }
            else return new NodeInsertion(x, null);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if(p == null) throw new IllegalArgumentException();
        return findInsertionPoint(p).insertionPoint == null;
    }

    // draw all points to standard draw
    public void draw() {
        drawWithLines(0, new ArrayList<>(), new ArrayList<>());
    }

    private void drawWithLines(final int level, List<Point2D> prevPointsX, List<Point2D> prevPointsY) {

            //Draw the line
            final AtomicReference<Double> line_min_coordinate = new AtomicReference<>(0D);
            final AtomicReference<Double> line_max_coordinate = new AtomicReference<>(1D);

            if(level % 2 == 0) {
                prevPointsY.forEach(prevPoint -> {
                    double distance = node.p.y() - prevPoint.y();
                    if(distance < 0) {
                        line_max_coordinate.set(prevPoint.y());
                    } else {
                        line_min_coordinate.set(prevPoint.y());
                    }
                });
                prevPointsX.add(node.p);
            } else {
                prevPointsX.forEach(prevPoint -> {
                    double distance = node.p.x() - prevPoint.x();
                    if(distance < 0) {
                        line_max_coordinate.set(prevPoint.x());
                    } else {
                        line_min_coordinate.set(prevPoint.x());
                    }
                });
                prevPointsY.add(node.p);
            }

            drawLine(level, node.p, line_min_coordinate.get(), line_max_coordinate.get());

            if(node.lb != null) node.lb.drawWithLines(level + 1, prevPointsX, prevPointsY);
            if(node.rt != null) node.rt.drawWithLines(level + 1, prevPointsX, prevPointsY);

            //Draw the point
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();
            StdDraw.setPenRadius();
    }

    private void drawLine(int level, Point2D point, double min_coordinate, double max_coordinate) {

        if(level % 2 == 0) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(point.x(), min_coordinate, point.x(), max_coordinate);
        } else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(min_coordinate, point.y(), max_coordinate, point.y());
        }

    }
//     // all points that are inside the rectangle (or on the boundary)
     public Iterable<Point2D> range(RectHV rect) {
         if(rect == null) throw new IllegalArgumentException();
         if(isEmpty()) return new ArrayList<>();

         return range_level(0, this, new ArrayList<>(), rect);
     }

     private Iterable<Point2D> range_level(int level, KdTree t, List<Point2D> acc, RectHV rect) {
        if(inside_rect(t.node.p.x(), t.node.p.y())) {
            acc.add(t.node.p);
            range_level(level + 1, t.node.lb, acc, rect);
            range_level(level + 1, t.node.rt, acc, rect);
            return acc;
        } else {
            if(level % 2 == 0) {
                rect.intersects()
            }
        }
     }

     // a nearest neighbor in the set to point p; null if the set is empty
     public Point2D nearest(Point2D p) {
         if(p == null) throw new IllegalArgumentException();
         if(isEmpty()) return null;

         return nearest_level(0, this, p);
     }

     private Point2D nearest_level(int level, KdTree t, Point2D p) {

        if(level % 2 == 0) {
            if(p.x() < t.node.p.x()) { //left tree
                if(t.node.lb == null) {
                    return t.node.p;
                } else {
                    Point2D lt_p = nearest_level(level + 1, t.node.lb, p);
                    if(lt_p.distanceTo(p) < t.node.p.distanceTo(p)) {
                        return lt_p;
                    } else {
                        return t.node.p;
                    }
                }
            } else { //right tree
                if(t.node.rt == null) {
                    return t.node.p;
                } else {
                    Point2D rt_p = nearest_level(level + 1, t.node.rt, p);
                    if(rt_p.distanceTo(p) < t.node.p.distanceTo(p)) {
                        return rt_p;
                    } else {
                        return t.node.p;
                    }
                }
            }
        } else {
            if(p.y() < t.node.p.y()) { //left tree
                if(t.node.lb == null) {
                    return t.node.p;
                } else {
                    Point2D lt_p = nearest_level(level + 1, t.node.lb, p);
                    if(lt_p.distanceTo(p) < t.node.p.distanceTo(p)) {
                        return lt_p;
                    } else {
                        return t.node.p;
                    }
                }
            } else { //right tree
                if(t.node.rt == null) {
                    return t.node.p;
                } else {
                    Point2D rt_p = nearest_level(level + 1, t.node.rt, p);
                    if(rt_p.distanceTo(p) < t.node.p.distanceTo(p)) {
                        return rt_p;
                    } else {
                        return t.node.p;
                    }
                }
            }
        }
     }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        Point2D p0 = new Point2D(.1, .2); //root
        Point2D p1 = new Point2D(.2, .3); //goes to right
        Point2D p2 = new Point2D(.3, .2); //goes to right and then left
        kdTree.insert(p0);
        kdTree.insert(p1);
        kdTree.insert(p2);

        System.out.println(kdTree.node.rt.node.lb.node.p);
        System.out.println(kdTree.contains(new Point2D(.3,.2)));

        StdDraw.enableDoubleBuffering();
        while(true) {
            StdDraw.clear();
            kdTree.draw();
            StdDraw.show();
            StdDraw.pause(20);
        }


    }
}
