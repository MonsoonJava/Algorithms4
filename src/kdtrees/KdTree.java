package kdtrees;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private PointNode root;

    private RectHV rect;
    
    private int count;

    private class PointNode {

        private Point2D point;

        private PointNode left;

        private PointNode right;

        private RectHV rect;

        private boolean comAxis;

        public PointNode(Point2D point, PointNode left, PointNode right, RectHV rect, boolean comAxis) {
            this.point = point;
            this.left = left;
            this.right = right;
            this.comAxis = comAxis;
            this.rect = rect;
        }

    }
    private int nodeSize(PointNode node) {
        int nodesize = 0;
        if (node != null) {
            if (null != node.left) {
                nodesize += nodeSize(node.left);
            }
            if (null != node.right) {
                nodesize += nodeSize(node.right);
            }
        }
        return nodesize + 1;
    }

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
    }

    // is the set empty?
    public boolean isEmpty() {
        return null == root;

    }

    // number of points in the set
    public int size() {
        //return root == null ? 0 : nodeSize(root);
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        root = put(root, p, this.rect, true);
    }

    private PointNode put(PointNode node, Point2D p, RectHV rect, boolean comAxis) {
        if (null == p)
            throw new NullPointerException();
        if (node == null) {
            count++;
            return new PointNode(p, null, null, rect, comAxis);
        }
        if (node.point.compareTo(p) == 0)
            return node;
        // comAxis true 比较x轴的值
        // comAxis false 比较y轴的值
        if (node.comAxis) {
            int cmp = node.point.x() > p.x() ? 1 : -1;
            if (cmp > 0) {
                node.left = put(node.left, p, subRectHv(node.rect, node.comAxis, true, node.point), !node.comAxis);
            } else {
                node.right = put(node.right, p, subRectHv(node.rect, node.comAxis, false, node.point), !node.comAxis);
            }
        } else {
            int cmp = node.point.y() > p.y() ? 1 : -1;
            if (cmp > 0) {
                node.left = put(node.left, p, subRectHv(node.rect, node.comAxis, true, node.point), !node.comAxis);
            } else {
                node.right = put(node.right, p, subRectHv(node.rect, node.comAxis, false, node.point), !node.comAxis);
            }
        }
        return node;
    }

    private RectHV subRectHv(RectHV rect, boolean comAxis, boolean leftORup, Point2D p) {
        RectHV subRect;
        if (comAxis) {
            if (leftORup) {
                // 竖 左
                subRect = new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
            } else {
                // 竖 右
                subRect = new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
        } else {
            if (leftORup) {
                // 横 下
                subRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
            } else {
                // 横 上
                subRect = new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
            }
        }
        return subRect;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (null == root)
            return false;
        return contains(root, p);
    }

    private boolean contains(PointNode node, Point2D p) {
        if (p == null)
            throw new NullPointerException();
        if (node.point.compareTo(p) == 0)
            return true;
        if (node.comAxis) {
            int cmpX = p.x() < node.point.x() ? -1 : p.x() > node.point.x() ? 1 : 0;
            if (cmpX == -1) {
                if (null == node.left)
                    return false;
                return contains(node.left, p);
            } else {
                if (null == node.right)
                    return false;
                return contains(node.right, p);
            }
        } else {
            int cmpY = p.y() < node.point.y() ? -1 : p.y() > node.point.y() ? 1 : 0;
            if (cmpY == -1) {
                if (null == node.left)
                    return false;
                return contains(node.left, p);
            } else {
                if (null == node.right)
                    return false;
                return contains(node.right, p);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        // 设置颜色 StdDraw.setPenColor(StdDraw.RED);
        // 画线 drawTo(Point2D that)
        draw(root);
    }

    private void draw(PointNode p) {
        if (null == p)
            return;
        if (p.comAxis) {
            StdDraw.setPenColor(StdDraw.RED);
            new Point2D(p.point.x(), p.rect.ymin()).drawTo(new Point2D(p.point.x(), p.rect.ymax()));
            System.out.println(p.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            new Point2D(p.rect.xmin(), p.point.y()).drawTo(new Point2D(p.rect.xmax(), p.point.y()));
            System.out.println(p.rect.xmax());
        }
        draw(p.left);
        draw(p.right);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if(null == root) return null;
        if(null == rect) throw new NullPointerException();
        ArrayList<Point2D> innerPoint = new ArrayList<Point2D>();
        searchRange(root,rect,innerPoint);
        return innerPoint;
    }
    
    private void searchRange(PointNode node,RectHV rect,ArrayList<Point2D> list){
        if(node == null) return ;
        if(rect.contains(node.point)) list.add(node.point);
        if(rect.intersects(node.rect)){
            searchRange(node.left,rect,list);
            searchRange(node.right,rect,list);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if(root == null) return null;
        if(p == null) throw new  NullPointerException();
        Point2D theNearest = root.point;
        return nearest(root,p,theNearest);
    }
    
    private Point2D nearest(PointNode searchNode,Point2D searchPoint,Point2D theNearest){
        double pointDistance = searchNode.point.distanceTo(searchPoint);
        double nearestDistance = theNearest.distanceTo(searchPoint);
        if(pointDistance < nearestDistance ){
            theNearest = searchNode.point;
        }
        Point2D theLeftNearest = theNearest;
        Point2D theRightNearest = theNearest;
        if(searchNode.left !=null && searchNode.left.rect.contains(searchPoint)){
             theLeftNearest = nearest(searchNode.left,searchPoint,theNearest);
        }
        if(searchNode.right != null && searchNode.right.rect.contains(searchPoint)){
             theRightNearest = nearest(searchNode.right,searchPoint,theNearest);
        }
        double leftDistance = searchNode.point.distanceTo(theLeftNearest);
        double rightDistance = searchNode.point.distanceTo(theRightNearest);
        theNearest = leftDistance >= rightDistance ? theRightNearest : theLeftNearest;
        return theNearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kt = new KdTree();
        kt.insert(new Point2D(0.7, 0.2));
        kt.insert(new Point2D(0.5, 0.4));
        System.out.println(kt.size());
        kt.insert(new Point2D(0.2, 0.3));
        kt.insert(new Point2D(0.4, 0.7));
        kt.insert(new Point2D(0.9, 0.6));
        System.out.println(kt.size());
        kt.draw();
        System.out.println(kt.contains(new Point2D(0.9, 0.6)));
        System.out.println("hello");
    }

}
