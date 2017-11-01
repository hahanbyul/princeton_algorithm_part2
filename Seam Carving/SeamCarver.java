import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    
    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        this.picture = picture;
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

    /*
    public   int[] findHorizontalSeam() {               // sequence of indices for horizontal seam
    }
    public   int[] findVerticalSeam() {                 // sequence of indices for vertical seam
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
        picture.show();
    }
}
