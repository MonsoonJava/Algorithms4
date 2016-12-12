package percolation;

import percolation.Percolation;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

    private int size = 0;
    private int trials = 0;
    
    private Percolation perc = null;
    
    private double[] percThreshold = null;
    
    private double mean = 0;
    private double stddev = 0;
    private double confidenceLo = 0;
    private double confidenceHi = 0;
    
    public PercolationStats(int n,int trials){
        if(n <= 0 || trials <= 0 )
        {
            throw new IllegalArgumentException();
        }
        this.size = n;
        this.trials = trials;
    }
    
    public double mean(){
        percThreshold = new double[trials];
        for(int i = 0 ;i < trials ; i++){
            perc = new Percolation(size);
            double openTimes = 0;
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
            percThreshold[i] = openTimes / (size * size );
            perc = null;
        }
        double sum = 0;
        for (double d : percThreshold) {
            sum += d;
        }
        mean =  sum/percThreshold.length;
        return mean;
    }

    public double stddev(){
        if(mean == 0 || null == percThreshold){
            this.mean();
        }
        double sqrSum = 0;
        for(int i = 0;i < percThreshold.length;i++){
            sqrSum += (percThreshold[i] - mean) * (percThreshold[i] - mean);
        }
        stddev = Math.sqrt( sqrSum / (percThreshold.length - 1));
        return stddev;
        
    }
    
    public double confidenceLo(){
        if(confidenceLo == 0){
            confidence();
        }
        return confidenceLo;
    }
    
    private void confidence() {
        if(stddev == 0 ){
            this.stddev();
        }
        confidenceLo = mean - 1.96 * stddev /Math.sqrt(percThreshold.length);
        confidenceHi = mean + 1.96 * stddev /Math.sqrt(percThreshold.length);
    }

    public double confidenceHi(){
        if(confidenceHi == 0){
            confidence();
        }
        return confidenceHi;
    }
    
    
    public static void main(String[] args){
        PercolationStats percS = new PercolationStats(200, 100);
        double mean = percS.mean();
        System.out.println("mean:"+mean);
        double stddev = percS.stddev();
        System.out.println("stddev:"+stddev);
        double confidenceLo = percS.confidenceLo();
        double confidenceHi = percS.confidenceHi();
        System.out.println("confidenceLo:"+confidenceLo);
        System.out.println("confidenceHi:"+confidenceHi);
    }
}
