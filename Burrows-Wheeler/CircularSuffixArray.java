import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private Integer[] index;
    private String s;
    private Comparator<Integer> cmp = new Comparator<Integer>() {
        @Override
        public int compare(Integer a, Integer b) {
            int i;

            // increase i until chars to compare are different
            for (i = 0; i < length(); i++)
                if (charAt(a+i) != charAt(b+i)) break;

            return charAt(a+i) - charAt(b+i);
        }

        private char charAt(int i) {
            return s.charAt(i % length());
        }
    };

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;

        index = new Integer[s.length()];
        for (int i = 0; i < index.length; i++)
            index[i] = i;

        Arrays.sort(index, cmp);

        /*
        for (int i = 0; i < length(); i++)
            StdOut.println(String.format("index[%d]: %d", i, index(i)));
        */
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length()-1) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
    }
}
