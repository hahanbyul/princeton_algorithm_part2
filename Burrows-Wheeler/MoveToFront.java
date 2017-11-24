import java.util.LinkedList;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    private static final int R = 256;
    private static LinkedList<Character> orderedList;

    private static void initializeOrderedList() {
        orderedList = new LinkedList<Character>();
        for (char i = 0; i < R; i++)
            orderedList.add(i);
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        initializeOrderedList();

        while (!BinaryStdIn.isEmpty()) {
            char index = (char) orderedList.indexOf(BinaryStdIn.readChar());
            BinaryStdOut.write(index);
            orderedList.addFirst(orderedList.remove(index));
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        initializeOrderedList();

        while (!BinaryStdIn.isEmpty()) {
            char ch = orderedList.remove((int) BinaryStdIn.readChar());
            BinaryStdOut.write(ch);
            orderedList.addFirst(ch);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].charAt(0) == '-') encode();
        else if (args[0].charAt(0) == '+') decode();
    }
}
