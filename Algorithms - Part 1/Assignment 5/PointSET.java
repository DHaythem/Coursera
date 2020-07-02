import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> rbTree;

    //Construct an empty set of points
    public PointSET() {
        rbTree = new TreeSet<Point2D>();
    }
  
    //Is the set empty? 
    public boolean isEmpty() {
        return rbTree.isEmpty();
    }

    //Number of points in the set 
    public int size() {
        return rbTree.size();
    }

    //Add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        if (!contains(p)) rbTree.add(p);
    }

    //Does the set contain point p? 
     public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        return rbTree.contains(p);
    }

    //Draw all points to standard draw 
    public void draw() {
        for (Point2D p : rbTree) {
        StdDraw.point(p.x(), p.y());
        }
    }

    //All points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException();
        Queue<Point2D> pq = new Queue<Point2D>();
        for (Point2D p : rbTree) {
            if (rect.contains(p)) pq.enqueue(p);
        }
        return pq;
    }

    //A nearest neighbor in the set to point p, null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        if (rbTree.isEmpty()) return null;
        double min = Double.MAX_VALUE;
        Point2D nearestP = null;
        for (Point2D point: rbTree) {
            double d = point.distanceSquaredTo(p);
            if (d < min) {
                min = d;
                nearestP = point;
            }
        }
        return nearestP;
    }

}
