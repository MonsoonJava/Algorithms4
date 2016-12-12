package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size = 0;
    
    private boolean [][] gridSite = null;
    
    private WeightedQuickUnionUF WQUF = null;
    
    private WeightedQuickUnionUF WQUF_HW = null;
    
    public Percolation(int n){
        if(n <= 0){
            throw new IllegalArgumentException();
        }
        this.size = n;
        gridSite = new boolean[n][n];
        for(int i = 0; i < n;i++){
            for(int j = 0; j < n ;j++){
                gridSite[i][j]= false;
            }
        }
        WQUF = new WeightedQuickUnionUF(n*n + 2); //max --401
        WQUF_HW = new WeightedQuickUnionUF(n*n + 1); //max -- 400
    }
    /**
     * validate the index weather in correct
     * @param index
     */
    private void validateIndex(int index){
        if(index < 0 || index > this.size){
            System.out.println(index);
            throw new IndexOutOfBoundsException();
        }
    }
    
    /**
     * change 2D position to 1D
     * @param x position
     * @param y position
     * @return
     * 
     *    (1,1) --> 0
     *    (n,n) --> n(n-1) + n-1 = n^2 - 1
     */
    private int XYTO1D(int x,int y){
        this.validateIndex(x);
        this.validateIndex(y);
        return this.size*(x-1) + (y -1);
    }
    
    
    /**
     * do three things
     * First,  validate the indices of the site that it receives.
     * Second, it should somehow mark the site as open. 
     * Third,  it should perform some sequence of WeightedQuickUnionUF operations 
     *            that links the site in question to its open neighbors.
     * @param i
     * @param j
     * 
     * start node (1,1)  end node(n,n)
     */
    public void open(int i,int j){
        this.validateIndex(i);
        this.validateIndex(j);
        this.gridSite[i-1][j-1] = true;
        if(i == 1){
            //top virtual connect
            WQUF.union(XYTO1D(i, j),size*size);
            WQUF_HW.union(XYTO1D(i, j),size*size);
        }
        
        if(i == size){
            //bottom virtual connect
            WQUF.union(XYTO1D(i, j),size*size + 1);
        }
        
        if(((i - 1) >= 1) && isOpen(i - 1,j)){
            WQUF.union(XYTO1D(i, j), XYTO1D(i - 1, j));
            WQUF_HW.union(XYTO1D(i, j), XYTO1D(i - 1, j));
        }
        if(((i + 1) <= size) && isOpen(i + 1,j) ){
            WQUF.union(XYTO1D(i, j), XYTO1D(i + 1, j));
            WQUF_HW.union(XYTO1D(i, j), XYTO1D(i + 1, j));
        }
        if(((j - 1) >= 1) && isOpen(i ,j - 1)  ){
            WQUF.union(XYTO1D(i, j), XYTO1D(i, j - 1));
            WQUF_HW.union(XYTO1D(i, j), XYTO1D(i, j - 1));
        }
        if( ((j + 1) <= size ) && isOpen(i ,j + 1) ){
            WQUF.union(XYTO1D(i, j), XYTO1D(i, j + 1));
            WQUF_HW.union(XYTO1D(i, j), XYTO1D(i, j + 1));
        }
    }
    
    public boolean isOpen(int i, int j){
        this.validateIndex(i);
        this.validateIndex(j);
        return gridSite[i-1][j-1];
    }
    
    public boolean isFull(int i,int j){
        return WQUF_HW.connected(XYTO1D(i, j), size * size);
    }
    
    public boolean percolates(){
        return WQUF.connected(size * size  , size * size + 1);
        /*for(int i = 0;i < size;i++){
            if(WQUF_HW.connected(XYTO1D(size, i), size*size)){
                return true;
            }
        }
        return false;*/
    }
    
    public static void main(String agrs[]){
        double openTimes = 0;
        int size = 200   ;
        Percolation perc = new Percolation(size);
        while(!perc.percolates()){
            int x = StdRandom.uniform(1,size + 1);
            int y = StdRandom.uniform(1,size + 1);
            if(perc.isOpen(x, y)){
                continue;
            }else{
                perc.open(x, y);
                openTimes++;
            }
        }
        double percolationThreshold = openTimes / (size * size);
        System.out.println(percolationThreshold);
    }
}
