package recognition;

import java.util.Arrays;

public class BruteCollinearPoints {

    private int segmentNumber = 0;

    private LineSegment[] Segment = null;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] point) {
        if (null == point) {
            throw new NullPointerException();
        }
        pointCheck(point);
        LineSegment[] lineSegment = null;
        Point[] points = new Point[point.length];
        for (int i = 0; i < point.length; i++) {
            points[i] = point[i];
        }
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int n = j + 1; n < points.length; n++) {
                    for (int m = n + 1; m < points.length; m++) {
                        if (isSlopeEqual(points[i], points[j], points[n])
                                && isSlopeEqual(points[i], points[j], points[m])) {
                            if (null == lineSegment) {
                                lineSegment = new LineSegment[20];
                            } else if (segmentNumber == lineSegment.length) {
                                LineSegment[] copySegment = new LineSegment[2 * lineSegment.length];
                                for (int k = 0; k < lineSegment.length; k++) {
                                    copySegment[k] = lineSegment[k];
                                }
                                lineSegment = copySegment;
                                copySegment = null;
                            }
                            lineSegment[segmentNumber] = new LineSegment(points[i], points[m]);
                            segmentNumber++;

                        }
                    }
                }
            }
        }
        Segment = new LineSegment[segmentNumber];
        for (int i = 0; i < segmentNumber; i++) {
            Segment[i] = lineSegment[i];
        }
        lineSegment = null;
    }

    private void pointCheck(Point[] points) {
        for (Point point : points) {
            if (null == point) {
                throw new NullPointerException();
            }
        }
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

    }

    private boolean isSlopeEqual(Point p0, Point p1, Point p2) {
        return p0.slopeTo(p1) == p0.slopeTo(p2);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentNumber;
    }

    // the line segments
    public LineSegment[] segments() {
        return Segment.clone();
    }

}
