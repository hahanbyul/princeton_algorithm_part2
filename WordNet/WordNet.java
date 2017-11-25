import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private ST<Integer, String>      synsetId_st;
    private ST<String, SET<Integer>> synonym_st;

    private SAP sap;

    private static final boolean VERBOSE = false;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new NullPointerException("Null argument is not allowed");

        synonym_st  = new ST<String, SET<Integer>>();   // word, set of synset ids
        synsetId_st = new ST<Integer, String>();        // synset id, merged words

        // ===> 1. read synset file
        In synsetFile = new In(synsets);
        String line = synsetFile.readLine();
        while (line != null) {
            if (VERBOSE) StdOut.println(line);
            String[] fields = line.split(",");

            int synset_id = Integer.parseInt(fields[0]);
            synsetId_st.put(synset_id, fields[1]);

            String[] synonym_set = fields[1].split(" ");
            for (int i = 0; i < synonym_set.length; i++) {
                String s = synonym_set[i];

                SET<Integer> set;
                if (synonym_st.contains(s)) set = synonym_st.get(s);
                else                        set = new SET<Integer>();
                set.add(synset_id);
                synonym_st.put(s, set);
            }

            line = synsetFile.readLine();
        }

        // ===> 2. read hypernym file
        // TODO: illegal argument
        Digraph G  = new Digraph(synonym_st.size());

        In hypernymsFile = new In(hypernyms);
        line = hypernymsFile.readLine();
        while (line != null) {
            if (VERBOSE) StdOut.println(line);
            String[] fields = line.split(",");
            int   synset_id = Integer.parseInt(fields[0]);

            for (int i = 1; i < fields.length; i++)
                G.addEdge(synset_id, Integer.parseInt(fields[i]));

            line = hypernymsFile.readLine();

        }
        if (VERBOSE) StdOut.println(G.toString());
        this.sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() { 
        return synonym_st.keys(); 
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) { 
        if (word == null) throw new NullPointerException("Null argument is not allowed");

        return synonym_st.contains(word); 
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) { 
        if (nounA == null || nounB == null) throw new NullPointerException("Null argument is not allowed");
        if (!isNoun(nounA)) throw new IllegalArgumentException(nounA + " is not defined in the wordnet");
        if (!isNoun(nounB)) throw new IllegalArgumentException(nounA + " is not defined in the wordnet");

        SET<Integer> v = synonym_st.get(nounA);
        SET<Integer> w = synonym_st.get(nounB);

        return sap.length(v, w); 
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) { 
        if (nounA == null || nounB == null) throw new NullPointerException("Null argument is not allowed");
        if (!isNoun(nounA)) throw new IllegalArgumentException(nounA + " is not defined in the wordnet");
        if (!isNoun(nounB)) throw new IllegalArgumentException(nounA + " is not defined in the wordnet");

        SET<Integer> v = synonym_st.get(nounA);
        SET<Integer> w = synonym_st.get(nounB);

        int ancestor = sap.ancestor(v, w);

        if (ancestor == -1) return null;
        else                return synsetId_st.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);

        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();

            int length   = wordnet.distance(nounA, nounB);
            String ancestor = wordnet.sap(nounA, nounB);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }
    }
}
