import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;
    private static final boolean VERBOSE = false;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }         

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] distances = new int[nouns.length];

        for (int i = 0; i < nouns.length-1; i++) {
            for (int j = i+1; j < nouns.length; j++) {
                int dist = wordnet.distance(nouns[i], nouns[j]);
                distances[i] += dist;
                distances[j] += dist;
            }
        }

        int max_d = Integer.MIN_VALUE;
        int max_i = -1;
        for (int i = 0; i < nouns.length; i++) {
            if (VERBOSE) StdOut.printf("%s: %d\n", nouns[i], distances[i]);
            if (distances[i] > max_d) {
                max_i = i;
                max_d = distances[i];
            }
        }

        return nouns[max_i];
    }   

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
