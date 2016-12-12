package recognition; 

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private int segmentNumber = 0;

    private LineSegment[] segment = null;

    private int capacity = 20;

    private static final Comparator<Slope4Point> BY_SLOPE = new BySlope();

    private static final Comparator<Slope4Point> BY_STARTY = new ByStartY();
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] point) {
        if (null == point) {
            throw new NullPointerException();
        }
        pointCheck(point);
        Point[] points = new Point[point.length];
        for (int i = 0; i < point.length; i++) {
            points[i] = point[i];
        }
        /*
         * save slope and other point how? two array? slope array and other point index array? need
         * to resizing the array?
         * 
         * Think of p as the origin. For each other point q, determine the slope it makes with p.
         * Sort the points according to the slopes they makeswith p. Check if any 3 (or more)
         * adjacent points in the sorted orderhave equal slopes with respect to p. If so, these
         * points, togetherwith p, are collinear.
         */
        Arrays.sort(points);
        Slope4Point[] sharpSlope = new Slope4Point[capacity];
        LineSegment[] segmentArray = null;
        int sharpS = 0;
        for (int i = 0; i < points.length - 1; i++) {
            Slope4Point[] slopeArray = new Slope4Point[capacity];
            int index = 0;
            for (int j = i + 1; j < points.length; j++) {
                if (index == slopeArray.length) {
                    slopeArray =  resizingSlopeArray(slopeArray);
                }
                double slope = points[i].slopeTo(points[j]);
                slopeArray[index] = new Slope4Point(slope, points[i], points[j]);
                index++;
            }
            // 将Slope4Point按照slope大小排序
            // copy index个存在的内容进行排序
            Slope4Point[] cSlopeArray = new Slope4Point[index];
            for (int t = 0; t < index; t++) {
                cSlopeArray[t] = slopeArray[t];
            }
            slopeArray = cSlopeArray;
            cSlopeArray = null;
            //安装y轴排序,要先除去resizing时的null
       
            Arrays.sort(slopeArray,FastCollinearPoints.BY_STARTY);
            Arrays.sort(slopeArray, FastCollinearPoints.BY_SLOPE);
            // 以上为排序过程

            // 以下为选择斜率相等大于4个的过程
            for (int loopS = 0, len = 1; loopS < slopeArray.length - 1;) {
                if (loopS + len < slopeArray.length
                        && slopeArray[loopS].slope.doubleValue() == slopeArray[loopS + len].slope
                                .doubleValue()) {
                    len++;
                    continue;
                } else {
                    if (len >= 3) {
                        // 判断是否有大于4个的slope相等
                        if (sharpS == sharpSlope.length) {
                            sharpSlope= resizingSlopeArray(sharpSlope);
                        }
                        sharpSlope[sharpS] = slopeArray[loopS + len - 1];
                        sharpS++;
                    }
                    loopS += 1;
                    len = 1;
                }
            }
        }
        // 去除subLineSegment
        
        Slope4Point[] copySharp = new Slope4Point[sharpS];
        for(int m = 0;m < sharpS;m++){
            copySharp[m] = sharpSlope[m];
        }
        sharpSlope = copySharp;
        copySharp = null;
        
        //排除方法：slope相等且end相等时，选第一个，slope不相等时，则直接选用
        LineSegment[] tempSegment =new LineSegment[capacity];
        for(int lop = 0,selectIndex = 0;lop<sharpSlope.length - 1;lop++){
                  if (sharpSlope[lop].slope.doubleValue() == sharpSlope[lop+1].slope.doubleValue()
                          && sharpSlope[lop].end == sharpSlope[lop+1].end) {
                      if(lop + 1 == sharpSlope.length - 1){
                          if(segmentNumber == tempSegment.length){
                              tempSegment =  lineSegResizing(tempSegment);
                          }
                          tempSegment[segmentNumber] = new LineSegment(sharpSlope[selectIndex].start,sharpSlope[selectIndex].end);
                          segmentNumber++;
                      }
                      continue;
                  }else{
                      if(segmentNumber == tempSegment.length){
                          tempSegment =  lineSegResizing(tempSegment);
                      }
                      tempSegment[segmentNumber] = new LineSegment(sharpSlope[selectIndex].start,sharpSlope[selectIndex].end);
                      segmentNumber++;
                      selectIndex = lop + 1;
                      if(lop + 1 == sharpSlope.length - 1){
                          if(segmentNumber == tempSegment.length){
                              tempSegment =  lineSegResizing(tempSegment);
                          }
                          tempSegment[segmentNumber] = new LineSegment(sharpSlope[selectIndex].start,sharpSlope[selectIndex].end);
                          segmentNumber++;
                      }
                  }
        }
        
        //缩减reszing时的null值
        segment = new LineSegment[segmentNumber];
        for(int i = 0;i< segment.length;i++){
            segment[i] = tempSegment[i];
        }
        tempSegment = null;
    }
    
    private LineSegment[] lineSegResizing(LineSegment[] source){
              LineSegment[] copySe = new LineSegment[source.length * 2];
              for (int k = 0; k < source.length; k++) {
                  copySe[k] = source[k];
              }
              return copySe;
    }

    private Slope4Point[] resizingSlopeArray(Slope4Point[] source) {
        Slope4Point[] copySlope = new Slope4Point[source.length * 2];
        for (int copyIndex = 0; copyIndex < source.length; copyIndex++) {
            copySlope[copyIndex] = source[copyIndex];
        }
        return copySlope;
    }

    private class Slope4Point {

        private Double slope;

        private Point end;

        private Point start;

        public Slope4Point(double slope, Point start, Point end) {
            super();
            this.slope = slope;
            this.end = end;
            this.start = start;
        }
    }
    
    private static class ByStartY implements Comparator<Slope4Point>{

        @Override
        public int compare(Slope4Point o1, Slope4Point o2) {
            return o1.end.compareTo(o2.end);
        }
    }

    
    private static class BySlope implements Comparator<Slope4Point> {
        @Override
        public int compare(Slope4Point o1, Slope4Point o2) {
            /*if (o1.slope.doubleValue() - o2.slope.doubleValue() == 0) {
                return 0;
            } else if (o1.slope.doubleValue() - o2.slope.doubleValue() < 0) {
                return -1;
            } else {
                return 1;
            }*/
            return o1.slope.compareTo(o2.slope);
        }
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

    // the number of line segments
    public int numberOfSegments() {
        return segmentNumber;
    }

    // the line segments
    public LineSegment[] segments() {
        return segment.clone();
    }

}
