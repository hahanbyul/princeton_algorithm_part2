import java.util.Arrays;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
// import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        int first = 0;
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        StringBuilder sb = new StringBuilder();
        int strLength = s.length();
        for (int i = 0; i < strLength; i++) {
            int index = (csa.index(i) - 1);
            if (index < 0) {
                index += strLength;
                first = i;
            }
            sb.append(s.charAt(index));
        }
        // StdOut.println(first);
        // StdOut.println(sb.toString());

        BinaryStdOut.write(first);
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.flush();
    }

    private static int[] computeNext(String lastChar, char[] firstChar) {
        int length = lastChar.length();
        int[] next = new int[length];

        int i = 0;
        while (i < length) {
            int j = 0;                  // initialize j
            char ch = firstChar[i];     // first char to check
            while (j < length) {
                // if i arrives at end or first char to check is changed, escape while
                if (i == length || ch != firstChar[i]) { break; }

                // if last char is same, save it and increase i
                if (i != j && lastChar.charAt(j) == ch) {
                    next[i] = j;
                    i++;
                }
                j++;
            }
        }

        return next;
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String lastChar  = BinaryStdIn.readString();
        char[] firstChar = lastChar.toCharArray();
        Arrays.sort(firstChar);
        
        int[] next = computeNext(lastChar, firstChar);

        StringBuilder sb = new StringBuilder();
        int idx = first;
        for (int i = 0; i < lastChar.length(); i++) {
            sb.append(firstChar[idx]);
            idx = next[idx];
        }
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if      (args[0].charAt(0) == '-') encode();
        else if (args[0].charAt(0) == '+') decode();
    }
}
