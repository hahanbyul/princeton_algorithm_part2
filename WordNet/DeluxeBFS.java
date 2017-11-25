import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.SET;

public class DeluxeBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    public boolean[] marked;  // marked[v] = is there an s->v path?
    public int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    public int[] distTo;      // distTo[v] = length of shortest s->v path
    public SET<Integer> prevMarked;
    
    /**
     * Computes the shortest path from <tt>s</tt> and every other vertex in graph <tt>G</tt>.
     * @param G the digraph
     * @param s the source vertex
     */
    public DeluxeBFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        prevMarked = new SET<Integer>();
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        bfs(G, s);
    }

    /**
     * Computes the shortest path from any one of the source vertices in <tt>sources</tt>
     * to every other vertex in graph <tt>G</tt>.
     * @param G the digraph
     * @param sources the source vertices
     */
    public DeluxeBFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        prevMarked = new SET<Integer>();
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        bfs(G, sources);
    }

    // BFS from single source
    public void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        prevMarked = new SET<Integer>();

        marked[s] = true;
        distTo[s] = 0;
        q.enqueue(s);
        prevMarked.add(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                    prevMarked.add(w);
                }
            }
        }
    }

    // BFS from multiple sources
    public void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        prevMarked = new SET<Integer>();

        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
            prevMarked.add(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                    prevMarked.add(w);
                }
            }
        }
    }

    /**
     * Is there a directed path from the source <tt>s</tt> (or sources) to vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a directed path, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Returns the number of edges in a shortest path from the source <tt>s</tt>
     * (or sources) to vertex <tt>v</tt>?
     * @param v the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(int v) {
        return distTo[v];
    }

    /**
     * Returns a shortest path from <tt>s</tt> (or sources) to <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }
    
    public void updateDistTo(Digraph G, int parent, int depth) {
        Queue<Integer> q = new Queue<Integer>();
        distTo[parent]++;
        q.enqueue(parent);
        int d = 0;
        while (d < depth && !q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                distTo[w]++;
                q.enqueue(w);
            }
            d++;
        }
    }
    
    public void reinitialize() {
        if (prevMarked == null || prevMarked.isEmpty()) return;

        for (int i : prevMarked) {
            marked[i] = false;
            distTo[i] = INFINITY;
            edgeTo[i] = 0;
        }
    }
}
