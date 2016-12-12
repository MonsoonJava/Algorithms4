package kdtrees;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
	
	private SET<Point2D> pointSet;
	
	// construct an empty set of points
	public PointSET() {
		pointSet = new SET<>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return pointSet.isEmpty();

	}

	// number of points in the set
	public int size() {
		return pointSet.size(); 
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		ArgCkeck(p);
		pointSet.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		ArgCkeck(p);
		return pointSet.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D point : pointSet) {
			point.draw();
		}

	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		ArgCkeck(rect);
		ArrayList<Point2D> inPoints = new ArrayList<>();
		for (Point2D point : inPoints) {
			if(rect.contains(point)){
				inPoints.add(point);
			}
		}
		return inPoints;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		ArgCkeck(p);
		if(null == pointSet) return null;
		Point2D nearest = null;
		double lengthMin = 0;
		for (Point2D point : pointSet) {
			if(null == nearest){
				nearest  = point;
				lengthMin = p.distanceSquaredTo(point);
				continue;
			}
			double distance = p.distanceSquaredTo(point);
			if(distance < lengthMin){ lengthMin = distance; nearest = point; }
		}
		return nearest;
	}

	private void ArgCkeck(Object obj){
		if(null == obj){
			throw new NullPointerException();
		}
	}
	
	
	// unit testing of the methods (optional)
	public static void main(String[] args) {
		PointSET set = new PointSET();
		set.insert(new Point2D(0.1,0.2));
		set.insert(new Point2D(0.2,0.2));
		set.insert(new Point2D(0.3,0.2));
		set.insert(new Point2D(0.9,0.81));
		RectHV rhv = new RectHV(0.4,0.4,0.8,0.8);
		rhv.draw();
		System.out.println(set.contains(new Point2D(0.3,0.2)));
		System.out.println(set.size());
		set.draw();
	}
}