package kdtrees;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree2 {

	private PointNode root;
	
	private Integer count;
	
	private class PointNode{
		
		private Point2D point;
		private PointNode left;
		private PointNode right;
		private PointNode parent;
		private boolean comAxis;
		
		public PointNode(Point2D point, PointNode left, PointNode right,PointNode parent,boolean comAxis) {
			this.point = point;
			this.left = left;
			this.right = right;
			this.comAxis = comAxis;
			this.parent = parent;
		}
		
		int nodeSize(){
			int nodesize = 0;
			if(point != null) {
				if(null != left) {
					nodesize += left.nodeSize(); 
				}
				if(null != right) {
					nodesize += left.nodeSize(); 
				}
			}
			return nodesize + 1;
		}
		
		
	}
	
	// construct an empty set of points
	public KdTree2() {
	}

	// is the set empty?
	public boolean isEmpty() {
		return null == root;

	}

	// number of points in the set
	public int size() {
		if(count == null && root != null){
			count = root.nodeSize();
		}
		return count;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		root =  insert(null,root,p);
	}
	
	private PointNode insert(PointNode parent,PointNode node,Point2D p){
		if(node == null){
			if(parent == null ){
				return   new PointNode(p,null,null,null,true);
			}else{
				return new PointNode(p,null,null,parent,!parent.comAxis);
			}
		}
		if(node.comAxis){
			int cmpX = p.x() < node.point.x() ? -1 : p.x() > node.point.x() ? 1 : 0;
			if(cmpX == -1){
				node.left = insert(node,node.left,p);
			}else{
				node.right = insert(node,node.right,p);
			}
		}else{
			int cmpY = p.y() < node.point.y() ? -1 : p.y() > node.point.y() ? 1 : 0;
			if(cmpY == -1){
				node.left = insert(node,node.left,p);
			}else{
				node.right = insert(node,node.right,p);
			}
		}
		return node;
	}
	

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if(null == root) return false;
		return contains(root,p);
	}
	
	private boolean contains(PointNode node,Point2D p){
		if(p == null) throw new NullPointerException();
		if(node.point.compareTo(p) == 0) return true;
		if( node.comAxis) {
			int cmpX = p.x() < node.point.x() ? -1 : p.x() > node.point.x() ? 1 : 0;
			if(cmpX == -1){
				if(null == node.left) return false;
				return contains(node.left,p);
			}else{
				if(null == node.right) return false;
				return contains(node.right,p);
			}
		}else{
			int cmpY = p.y() < node.point.y() ? -1 : p.y() > node.point.y() ? 1 : 0;
			if(cmpY == -1){
				if(null == node.left) return false;
				return contains(node.left,p);
			}else{
				if(null == node.right) return false;
				return contains(node.right,p);
			}
		}
	}

	// draw all points to standard draw
	public void draw() {
		//设置颜色 StdDraw.setPenColor(StdDraw.RED); 
		//画线   drawTo(Point2D that)
		draw(null,root);
	}
	
	private void draw(PointNode parent,PointNode p){
		if(null == parent){
			if(null == p) { return; }
			StdDraw.setPenColor(StdDraw.BLACK);
			p.point.draw();
			StdDraw.setPenColor(StdDraw.RED);
			new Point2D(p.point.x(),0).drawTo(new Point2D(p.point.x(),1.0));
			if(p.left != null){
				draw(p,p.left);
			}
			if(p.right != null){
				draw(p,p.right);
			}
		} else{
			if(parent.comAxis){
				StdDraw.setPenColor(StdDraw.BLUE);
				if(parent.point.x() > p.point.x()){
					new Point2D(0,p.point.y()).drawTo(new Point2D(parent.point.x(),p.point.y()));
				}else{
					new Point2D(parent.point.x(),p.point.y()).drawTo(new Point2D(1.0,p.point.y()));
				}
			}else{
				StdDraw.setPenColor(StdDraw.RED);
				if(parent.point.y() > p.point.y()){
					new Point2D(p.point.x(),0).drawTo(new Point2D(p.point.x(),parent.point.y()));
				}else{
					new Point2D(p.point.x(),parent.point.y()).drawTo(new Point2D(p.point.x(),1.0));
				}
			}
			if(p.left != null){
				draw(p,p.left);
			}
			if(p.right != null){
				draw(p,p.right);
			}
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		ArrayList<Point2D> innerPoint = new ArrayList<Point2D>();
		
		
		return null;
	}

	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		return null;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		KdTree2 kt = new KdTree2();
		kt.insert(new Point2D(0.7,0.2));
		kt.insert(new Point2D(0.5,0.4));
		kt.insert(new Point2D(0.2,0.3));
		kt.insert(new Point2D(0.4,0.7));
		kt.insert(new Point2D(0.9,0.6));
		kt.insert(new Point2D(0.5,0.9));
		kt.draw();
		System.out.println(kt.contains(new Point2D(0.3,0.9)));
		System.out.println("hello");
	}

}
