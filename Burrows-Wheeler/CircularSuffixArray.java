import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();

        index = new int[s.length()];

        /*
        for (int i = 0; i < length(); i++)
            StdOut.println(String.format("index[%d]: %d", i, index(i)));
        */
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    private String ithString(String s, int i) {
        return s.substring(i) + s.substring(0,i);
    }

    private String sortedIthString(int i) {
        int cnt = 0;
        for (String s : tst.keys()) {
            if (cnt == i) return s;
            cnt++;
        }
        return null;
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length()-1) throw new IllegalArgumentException();
        return tst.get(sortedIthString(i));
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
    }
}
