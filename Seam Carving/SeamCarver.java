import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private double[][] distTo;
    private int[][]    edgeTo;
    
    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        this.picture = picture;

        this.energy = new double[width()][height()];
        this.distTo = new double[width()][height()];
        this.edgeTo = new int[width()][height()];

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                energy[col][row] = energy(col, row);
                distTo[col][row] = Double.POSITIVE_INFINITY;
            }
        }
    }

    public void printDistTo() {
        printArrary(distTo);
    }

    private void printArrary(double[][] array) {
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++)
                System.out.print(String.format("%10.1f ", array[col][row]));
            System.out.println();
        }
    }

    private void relax(int x, int y) {
        if (y == height() - 1) return;

        for (int dx = -1; dx <= 1; dx++) {
            if (x + dx < 0 || x + dx >= width()) continue;

            if (distTo[x+dx][y+1] > distTo[x][y] + energy[x+dx][y+1]) {
                distTo[x+dx][y+1] = distTo[x][y] + energy[x+dx][y+1];
                edgeTo[x+dx][y+1] = dx;
            }
        }
    }

    private void findShortest() {
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (row == 0) distTo[col][row] = 1000;
                relax(col, row);
            }
        }
    }

    public Picture picture() {                         // current picture
        return this.picture;
    }

    public     int width() {                           // width of current picture
        return this.picture.width();
    }
    
    public     int height() {                           // height of current picture
        return this.picture.height();
    }
    
    public  double energy(int x, int y) {               // energy of pixel at column x and row y
        if (x == 0 || x == width()-1 || y == 0 || y == height()-1) return 1000;
        return Math.sqrt(deltaXSq(x, y) + deltaYSq(x, y));
    }

    private double deltaXSq(int x, int y) {
        Color left  = this.picture.get(x-1, y);
        Color right = this.picture.get(x+1, y);

        return Math.pow(left.getRed() - right.getRed(), 2) + Math.pow(left.getGreen() - right.getGreen(), 2) + Math.pow(left.getBlue() - right.getBlue(), 2);
    }

    private double deltaYSq(int x, int y) {
        Color left  = this.picture.get(x, y-1);
        Color right = this.picture.get(x, y+1);

        return Math.pow(left.getRed() - right.getRed(), 2) + Math.pow(left.getGreen() - right.getGreen(), 2) + Math.pow(left.getBlue() - right.getBlue(), 2);
    }

    public   int[] findVerticalSeam() {                 // sequence of indices for vertical seam
        int[] path = new int[height()];

        findShortest();
        printDistTo();
        double min_val = Double.POSITIVE_INFINITY;
        int min_col = -1;
        for (int col = 0; col < width(); col++) {
            if (min_val > distTo[col][height()-1]) {
                min_val = distTo[col][height()-1];
                min_col = col;
            }
        }
        System.out.println(min_col);
        path[height()-1] = min_col;
        for (int row = height()-2; row >= 0; row--) {
            path[row] = path[row+1] - edgeTo[path[row+1]][row+1];
        }

        return path;
    }

    /*
    public   int[] findHorizontalSeam() {               // sequence of indices for horizontal seam
        return 0;
    }
    public    void removeHorizontalSeam(int[] seam) {   // remove horizontal seam from current picture
    }
    public    void removeVerticalSeam(int[] seam) {     // remove vertical seam from current picture

    }
    */
    
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        
        System.out.printf("%d-by-%d\n", sc.width(), sc.height());
        // picture.show();
        int[] path = sc.findVerticalSeam();
        for (int i = 0; i < sc.height(); i++)
            System.out.print(String.format("%d ", path[i]));

    }
}
