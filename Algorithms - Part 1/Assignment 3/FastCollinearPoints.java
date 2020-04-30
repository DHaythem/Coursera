import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {
    private final Point[] points;
    private final ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] pointArray) {
        if (pointArray == null) throw new IllegalArgumentException();
        for (Point p : pointArray) if (p == null) throw new IllegalArgumentException();

        this.points = pointArray.clone();
        this.segments = new ArrayList<>();

        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++)
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();

        for (Point p : points) {
            Point[] points = this.points.clone();
            Arrays.sort(points, p.slopeOrder());

            double previousSlope = Double.NaN;
            LinkedList<Point> segment = new LinkedList<>();
            for (int i = 1; i <= points.length; i++) {
                int index = i % points.length;
                if (p.slopeTo(points[index]) != previousSlope) {
                    if (segment.size() > 2 && p.compareTo(segment.peekFirst()) < 0)
                        segments.add(new LineSegment(p, segment.peekLast()));
                    segment.clear();
                }
                segment.add(points[index]);
                previousSlope = p.slopeTo(points[index]);
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }
}
