import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
    private final int R = 26;
    private TST[] firstTwo;
    private TreeSet validWords;
    private boolean[][] visited;

    private int alphaToInt(char ch) {
        return ch - 'A';
    }

    private int getAddress(String st) {
        if (st.length() == 1)    return alphaToInt(st.charAt(0)) * R;
        if (st.charAt(0) == 'Q') {
            if (st.length() > 2) return alphaToInt('Q') * R + alphaToInt(st.charAt(2));
            else return alphaToInt('Q') * R + alphaToInt('U');
        }
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

            if (firstTwo[addr] == null) firstTwo[addr] = new TST();

            firstTwo[addr].put(word, val);
            val++;
        }

        for (int i = 0; i < R; i++) {
            TST dict = firstTwo[alphaToInt('Q')*R + i];
            if (dict == null) continue;
            StdOut.print(String.format("%d: ", i));
            printKeys(dict.keys());
        }
    }

    private boolean isValidIndex(BoggleBoard board, int m, int n) {
        if (m < 0 || m >= board.rows()) return false;
        if (n < 0 || n >= board.cols()) return false;
        return true;
    }

    private void solve(BoggleBoard board, int m, int n) {
        char L = board.getLetter(m, n);
        char[] ch;
        if (L != 'Q') {
            ch = new char[2];
            ch[0] = L;
        } else {
            ch = new char[3];
            ch[0] = 'Q';
            ch[1] = 'U';
        }

        StdOut.println(String.format("(%d, %d): %c", m, n, ch[0]));

        visited[m][n] = true;
        for (int dm = -1; dm <= 1; dm++) {
            for (int dn = -1; dn <= 1; dn++) {
                if (dm == 0 && dn == 0) continue;

                int mm = m + dm;
                int nn = n + dn;

                if (!isValidIndex(board, mm, nn)) continue;

                if (L != 'Q') ch[1] = board.getLetter(mm, nn);
                else          ch[2] = board.getLetter(mm, nn);
                String s = new String(ch);
                int addr = getAddress(s);

                if (firstTwo[addr] == null) continue;

                StdOut.println(String.format("-> (%d, %d): %s", mm, nn, s));

                visited[mm][nn] = true;
                solve(board, firstTwo[addr], mm, nn, s);
                visited[mm][nn] = false;
            }
        }
        visited[m][n] = false;
    }

    private boolean solve(BoggleBoard board, TST dict, int m, int n, String s) {
        StdOut.println(String.format("---> (%d, %d): %s", m, n, s));
        if (s.length() > 2 && isInDictionary(dict, s)) { validWords.add(s); }

        Iterable<String> words = dict.keysWithPrefix(s);
        printKeys(words);
        if (!words.iterator().hasNext()) return false;

        for (int dm = -1; dm <= 1; dm++) {
            for (int dn = -1; dn <= 1; dn++) {
                if (dm == 0 && dn == 0) continue;

                int mm = m + dm;
                int nn = n + dn;
                if (!isValidIndex(board, mm, nn) || visited[mm][nn]) continue;

                visited[mm][nn] = true;
                boolean ret = solve(board, dict, mm, nn, s + board.getLetter(mm, nn));
                visited[mm][nn] = false;
            }
        }

        return true;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords = new TreeSet<String>();
        visited = new boolean[board.rows()][board.cols()];

        for (int m = 0; m < board.rows(); m++) {
            for (int n = 0; n < board.cols(); n++) {
                solve(board, m, n);
            }
        }

        return validWords;
    }

    private boolean isInDictionary(TST dict, String word) {
        return dict.get(word) != null;
    }

    private boolean isInDictionary(String word) {
        return isInDictionary(firstTwo[getAddress(word)], word);
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
