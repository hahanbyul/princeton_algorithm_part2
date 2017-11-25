import java.util.LinkedList;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
// import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    private static final int R = 256;

    private static LinkedList<Character> initializeOrderedList() {
        LinkedList<Character> linkedList = new LinkedList<Character>();
        for (char i = 0; i < R; i++)
            linkedList.add(i);

        return linkedList;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> linkedList = initializeOrderedList();

        while (!BinaryStdIn.isEmpty()) {
            char index = (char) linkedList.indexOf(BinaryStdIn.readChar());
            BinaryStdOut.write(index);
            linkedList.addFirst(linkedList.remove(index));
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> linkedList = initializeOrderedList();

        while (!BinaryStdIn.isEmpty()) {
            char ch = linkedList.remove((int) BinaryStdIn.readChar());
            BinaryStdOut.write(ch);
            linkedList.addFirst(ch);
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
