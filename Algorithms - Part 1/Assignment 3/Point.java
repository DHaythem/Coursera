import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    //Initializes a new point
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Draws this point to standard draw.
    public void draw() {
        StdDraw.point(x, y);
    }

    //Draws the line segment between this point and the specified point to standard draw
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /*
     Returns the slope between this point and the specified point.
     Formally, if the two points are (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 - x0)
     For completeness, the slope is defined to be +0.0 if the line segment connecting the two points is horizontal,
     Double.POSITIVE_INFINITY if the line segment is vertical and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     */
    public double slopeTo(Point that) {
        if (x == that.x) {
            if (y == that.y) {
                return Double.NEGATIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        else if (y == that.y) {
            return 0;
        }
        return (that.y - y) / (that.x - x);
    }

    /*
     Compares two points by y-coordinate, breaking ties by x-coordinate.
     Formally, the invoking point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     Return the value 0 if this point is equal to the argument point (x0 = x1 and y0 = y1), a negative integer if this point is less than the argument point
     and a positive integer if this point is greater than the argument point.   
     */
    public int compareTo(Point that) {
        return this.y - that.y == 0 ? this.x - that.x : this.y - that.y;
    }

    /*
     Compares two points by the slope they make with this point.
     The slope is defined as in the slopeTo() method.
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
    }
    
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point a, Point b) {
            double aSlope = slopeTo(a);
            double bSlope = slopeTo(b);
            if (aSlope < bSlope) return -1;
            if (bSlope < aSlope) return 1;
            return 0;
        }
    }


    /*
     Returns a string representation of this point.
     This method is provide for debugging;
     your program should not rely on the format of the string representation.
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p = new Point(69, 420);
        Point q = new Point(777, 999);
        Point r = new Point(19, 53);
        StdOut.println(p.slopeOrder().compare(q, r));
        StdOut.println(p.slopeTo(q));
        StdOut.println(p.slopeTo(r));
    }
}
