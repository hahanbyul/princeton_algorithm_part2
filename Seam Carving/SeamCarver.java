import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private final Picture working;
    private int height, width;
    private double[][] energy;
    private double[][] distTo;
    private int[][]    edgeTo;
    
    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        if (picture == null) throw new IllegalArgumentException();

        working = new Picture(picture);           // picture copy
        height = picture.height();
        width = picture.width();

        energy = new double[width][height];
        distTo = new double[width][height];
        edgeTo = new int[width][height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energy[col][row] = energy(col, row);
            }
        }
    }
    private void initializeDistTo() {
        // initialize distTo array
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                distTo[col][row] = Double.POSITIVE_INFINITY;
            }
        }
    }

    private void updateDistToVertical() {
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (row == 0) distTo[col][row] = 1000;
                relaxVertical(col, row);
            }
        }
    }

    private void updateDistToHorizontal() {
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if (col == 0) distTo[col][row] = 1000;
                relaxHorizontal(col, row);
            }
        }
    }

    private void relaxVertical(int x, int y) {
        if (y == height() - 1) return;

        for (int dx = -1; dx <= 1; dx++) {
            if (x + dx < 0 || x + dx >= width()) continue;

            if (distTo[x+dx][y+1] > distTo[x][y] + energy[x+dx][y+1]) {
                distTo[x+dx][y+1] = distTo[x][y] + energy[x+dx][y+1];
                edgeTo[x+dx][y+1] = dx;
            }
        }
    }

    private void relaxHorizontal(int x, int y) {
        if (x == width() - 1) return;

        for (int dy = -1; dy <= 1; dy++) {
            if (y + dy < 0 || y + dy >= height()) continue;

            if (distTo[x+1][y+dy] > distTo[x][y] + energy[x+1][y+dy]) {
                distTo[x+1][y+dy] = distTo[x][y] + energy[x+1][y+dy];
                edgeTo[x+1][y+dy] = dy;
            }
        }
    }

    public Picture picture() {                         // current picture
        Picture ret = new Picture(width(), height());
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                ret.set(col, row, working.get(col, row));
        return ret;
    }

    public     int width() {                           // width of current picture
        return width;
    }
    
    public     int height() {                           // height of current picture
        return height;
    }
    
    public  double energy(int x, int y) {               // energy of pixel at column x and row y
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException();

        if (x == 0 || x == width()-1 || y == 0 || y == height()-1)  // border condition
            return 1000;
        return Math.sqrt(deltaXSq(x, y) + deltaYSq(x, y));
    }

    private double deltaXSq(int x, int y) {
        Color left  = working.get(x-1, y);
        Color right = working.get(x+1, y);

        return Math.pow(left.getRed() - right.getRed(), 2) + Math.pow(left.getGreen() - right.getGreen(), 2) + Math.pow(left.getBlue() - right.getBlue(), 2);
    }

    private double deltaYSq(int x, int y) {
        Color left  = working.get(x, y-1);
        Color right = working.get(x, y+1);

        return Math.pow(left.getRed() - right.getRed(), 2) + Math.pow(left.getGreen() - right.getGreen(), 2) + Math.pow(left.getBlue() - right.getBlue(), 2);
    }

    public   int[] findVerticalSeam() {                 // sequence of indices for vertical seam
        initializeDistTo();
        updateDistToVertical();

        // find min val at the bottom line
        double minVal = Double.POSITIVE_INFINITY;
        int minCol = -1;
        for (int col = 0; col < width(); col++) {
            if (minVal > distTo[col][height()-1]) {
                minVal = distTo[col][height()-1];
                minCol = col;
            }
        }

        // reconstruct path upward
        int[] path = new int[height()];
        path[height()-1] = minCol;
        for (int row = height()-2; row >= 0; row--) {
            path[row] = path[row+1] - edgeTo[path[row+1]][row+1];
        }

        return path;
    }

    public    void removeVerticalSeam(int[] seam) {     // remove vertical seam from current picture
        if (seam == null || seam.length != height) throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] > width()-1) throw new IllegalArgumentException();
        for (int i = 1; i < height; i++) {
            if (seam[i] < 0 || seam[i] > width()-1) throw new IllegalArgumentException();
            if (Math.abs(seam[i] - seam[i-1]) > 1) throw new IllegalArgumentException();
        }

        width--;

        // shift working image
        for (int row = 0; row < height(); row++)
            for (int col = seam[row]; col < width(); col++)
                working.set(col, row, working.get(col+1, row));

        // update and shift energy array
        for (int row = 0; row < height(); row++) {
            if (seam[row] > 0) 
                energy[seam[row]-1][row] = energy(seam[row]-1, row);
            if (seam[row] <= width()-1)
                energy[seam[row]][row] = energy(seam[row], row);
            for (int col = seam[row]+1; col < width()-1; col++)
                energy[col][row] = energy[col+1][row];
            energy[width()-1][row] = 1000;
        }
    }

    public   int[] findHorizontalSeam() {               // sequence of indices for horizontal seam
        initializeDistTo();
        updateDistToHorizontal();
        
        // find min val at the rightmost line
        double minVal = Double.POSITIVE_INFINITY;
        int minRow = -1;
        for (int row = 0; row < height(); row++) {
            if (minVal > distTo[width()-1][row]) {
                minVal = distTo[width()-1][row];
                minRow = row;
            }
        }

        // reconstruct path backward
        int[] path = new int[width()];
        path[width()-1] = minRow;
        for (int col = width()-2; col >= 0; col--) {
            path[col] = path[col+1] - edgeTo[col+1][path[col+1]];
        }

        return path;
    }

    public    void removeHorizontalSeam(int[] seam) {   // remove horizontal seam from current picture
        if (seam == null || seam.length != width) throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] > height()-1) throw new IllegalArgumentException();
        for (int i = 1; i < width; i++) {
            if (seam[i] < 0 || seam[i] > height()-1) throw new IllegalArgumentException();
            if (Math.abs(seam[i] - seam[i-1]) > 1) throw new IllegalArgumentException();
        }

        height--;

        // shift working image
        for (int col = 0; col < width(); col++)
            for (int row = seam[col]; row < height(); row++)
                working.set(col, row, working.get(col, row+1));

        // update and shift energy array
        for (int col = 0; col < width(); col++) {
            if (seam[col] > 0) 
                energy[col][seam[col]-1] = energy(col, seam[col]-1);
            if (seam[col] <= height()-1)
                energy[col][seam[col]] = energy(col, seam[col]);
            for (int row = seam[col]+1; row < height()-1; row++)
                energy[col][row] = energy[col][row+1];
            energy[col][height()-1] = 1000;
        }
    }

    /*
    private void printWorking() {
        System.out.println("working: ");
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++)
                System.out.print(String.format("%5d ", working.get(col, row).getRGB()));
            System.out.println();
        }
    }

    private void printPicture() {
        System.out.println("picture: ");
        for (int row = 0; row < picture.height(); row++) {
            for (int col = 0; col < picture.width(); col++)
                System.out.print(String.format("%5d ", picture.get(col, row).getRGB()));
            System.out.println();
        }
    }

    private void printDistTo() {
        System.out.println("distTo: ");
        printArrary(distTo);
    }

    private void printEnergy() {
        System.out.println("energy: ");
        printArrary(energy);
    }

    private void printArrary(double[][] array) {
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++)
                System.out.print(String.format("%10.1f ", array[col][row]));
            System.out.println();
        }
    }
    */
    
    public static void main(String[] args) {
        // Picture picture = new Picture(args[0]);
        Picture picture = new Picture("seam/chameleon.png");
        SeamCarver carver = new SeamCarver(picture);
        
        System.out.printf("%d-by-%d\n", carver.width(), carver.height());

        for (int i = 0; i < 100; i++) {
            int[] verticalSeam = carver.findVerticalSeam();
            carver.removeVerticalSeam(verticalSeam);

            int[] horizontalSeam = carver.findHorizontalSeam();
            carver.removeHorizontalSeam(horizontalSeam);

            carver.picture().show();
        }

    }
}
