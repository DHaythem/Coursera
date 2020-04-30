import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;
    private final ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] pointArray) {
        if (pointArray == null) throw new IllegalArgumentException();
        for (Point p : pointArray) if (p == null) throw new IllegalArgumentException();
        this.points = pointArray.clone();
        this.segments = new ArrayList<>();
        int N = points.length;

        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++)
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();

        for (int i = 0; i < N - 3; i++) {
            for (int j = i + 1; j < N - 2; j++) {
                for (int k = j + 1; k < N - 1; k++) {
                    for (int l = k + 1; l < N; l++) {
                        Point point1 = points[i];
                        Point point2 = points[j];
                        Point point3 = points[k];
                        Point point4 = points[l];

                        if (point1.slopeTo(point2) == point2.slopeTo(point3)
                            && point2.slopeTo(point3) == point3.slopeTo(point4)) {
                            segments.add(new LineSegment(point1, point4));
                        }
                    }
                }
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
