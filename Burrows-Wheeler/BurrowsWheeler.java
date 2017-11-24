import java.lang.StringBuilder;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {
    private static int first;
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
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

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if      (args[0].charAt(0) == '-') encode();
        else if (args[0].charAt(0) == '+') decode();
    }
}
