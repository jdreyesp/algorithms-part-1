/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

package week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KdTree {

    private List<Node> nodes;

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
        nodes = new ArrayList<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    // number of points in the set
    public int size() {
        return nodes.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if(p == null) throw new IllegalArgumentException();
        if(isEmpty()) {
            Node node = new Node(p, null, null, null);
            nodes.add(node);
        }

        NodeInsertion nodeInsertion = findInsertionPoint(p);
        if(nodeInsertion != null && nodeInsertion.insertionPoint == NodeInsertion.InsertionPoint.lb) {
            nodeInsertion.node.lb = new KdTree();
            nodeInsertion.node.lb.nodes.add(new Node(p, null, null, null));
        } else if(nodeInsertion != null && nodeInsertion.insertionPoint == NodeInsertion.InsertionPoint.rt) {
            nodeInsertion.node.rt = new KdTree();
            nodeInsertion.node.rt.nodes.add(new Node(p, null, null, null));
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
        Node x = nodes.get(0); //root
        int depth = 0;
        while(true) {
            final Comparator<Point2D> comparator = depth % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(p, x.p);
            if(cmp < 0) {
                if(x.lb == null) {
                    return new NodeInsertion(x, NodeInsertion.InsertionPoint.lb);
                } else {
                    x = x.lb.nodes.get(0);
                    depth++;
                    continue;
                }
            }
            else if (cmp > 0) {
                if(x.rt == null) {
                    return new NodeInsertion(x, NodeInsertion.InsertionPoint.rt);
                } else {
                    x = x.rt.nodes.get(0);
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
        nodes.forEach(node -> node.p.draw());
        //TODO: Draw lines
    }

    // // all points that are inside the rectangle (or on the boundary)
    // public Iterable<Point2D> range(RectHV rect) {
    //     if(rect == null) throw new IllegalArgumentException();
    //
    // }
    //
    // // a nearest neighbor in the set to point p; null if the set is empty
    // public Point2D nearest(Point2D p) {
    //     if(p == null) throw new IllegalArgumentException();
    //     if(isEmpty()) return null;
    //
    //
    // }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        Point2D p0 = new Point2D(.1, .2); //root
        Point2D p1 = new Point2D(.2, .3); //goes to right
        Point2D p2 = new Point2D(.3, .2); //goes to right and then left
        kdTree.insert(p0);
        kdTree.insert(p1);
        kdTree.insert(p2);

        System.out.println(kdTree.nodes.get(0).rt.nodes.get(0).lb.nodes.get(0).p);
        System.out.println(kdTree.contains(new Point2D(.3,.2)));
    }
}
