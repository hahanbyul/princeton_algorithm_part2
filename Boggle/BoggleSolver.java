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

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        firstTwo = new TST[R*R];

        int val = 0;
        for (String word : dictionary) {
            int addr = getAddress(word);
            // StdOut.println(String.format("word: %s, addr: %d", word, addr));

            if (firstTwo[addr] == null) firstTwo[addr] = new TST();

            firstTwo[addr].put(word, val);
            val++;
        }

        for (int i = 0; i < R*R; i++) {
            if (firstTwo[i] == null) continue;
            printKeys(firstTwo[i].keys());
        }

        StdOut.println(scoreOf("YOGI"));
        StdOut.println(scoreOf("YOGE"));
        StdOut.println(isInDictionary("YOGE"));
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return null;
    }

    private boolean isInDictionary(String word) {
        return firstTwo[getAddress(word)].get(word) != null;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word.length() <= 2)    return 0;
        if (!isInDictionary(word)) return 0;
        else {
            if (word.length() <= 4)      return 1;
            else if (word.length() <= 5) return 2;
            else if (word.length() <= 6) return 3;
            else if (word.length() <= 7) return 5;
            else                         return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
