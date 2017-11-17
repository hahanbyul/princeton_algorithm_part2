import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver
{
    private final int R = 26;
    private TST[] firstTwo;

    private int alphaToInt(char ch) {
        return ch - 'A';
    }

    private int getAddress(String st) {
        if (st.length() == 1) return alphaToInt(st.charAt(0)) * R;
        return alphaToInt(st.charAt(0)) * R + alphaToInt(st.charAt(1));
    }

    private void printKeys(Iterable<String> keys) {
        for (String key : keys)
            StdOut.print(String.format("%s, ", key));
        StdOut.println();
    }

    private static String[] readFromFile(String filename) {
        ArrayList<String> ret = new ArrayList<String>();

        In in = new In(filename);
        while (in.hasNextLine()) {
            ret.add(in.readLine());
        }

        return ret.toArray(new String[ret.size()]);
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        firstTwo = new TST[R*R];

        int val = 0;
        for (String word : dictionary) {
            int addr = getAddress(word);
            StdOut.println(String.format("word: %s, addr: %d", word, addr));

            if (firstTwo[addr] == null) firstTwo[addr] = new TST();

            // firstTwo[addr].put(word.substring(2), 0);
            firstTwo[addr].put(word, val);
            val++;
        }
        printKeys(firstTwo[0].keys());
        printKeys(firstTwo[664].keys());
    }

    /*
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    }
    */

    public static void main(String[] args) {
        String[] dict = readFromFile(args[0]);
        BoggleSolver bs = new BoggleSolver(dict);
    }

}
