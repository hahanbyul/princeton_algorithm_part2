import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DigraphGenerator;

public class SAP {
    private static final boolean CACHE_ON   = true;
    private static final boolean CACHE_OFF  = false;
    private static final boolean IS_CACHE   = CACHE_OFF;
    private static final boolean VERBOSE    = false;

    private Digraph G, R;
    //private Cache[][] cache;
    private Path pathRecent;
    private DeluxeBFS bfs;

    private class Path {
        private final SET<Integer>          vStart;
        private       int                   v;
        private final int                   w;

        private boolean[]                   marked;
        private ST<Integer, Integer>        prevPath;
        private int                         prev;
        private SET<Integer>                adj;

        private int                         dist;
        private int                         ancstr;
        private int                         shortest;

        private Path(SET<Integer> v, int w) {
            this.vStart   = v;
            this.w        = w;
            this.marked   = new boolean[G.V()];
            this.prevPath = new ST<Integer, Integer>();
            this.prev     = 0;
            this.adj      = null;
            
            this.dist     = Integer.MAX_VALUE;
            this.ancstr   = -1;
            this.shortest = Integer.MAX_VALUE; 
            
            for (int a : v) { 
                prevPath.put(a, a);
                marked[a] = true;
            }
            this.next();
        }
        
        private Path(Path p) {
            this.vStart   = p.vStart;
            this.w        = p.w;
            this.marked   = new boolean[G.V()];
            for (int i = 0; i < p.marked.length; i++)
                this.marked[i] = p.marked[i];
            this.prevPath = new ST<Integer, Integer>();
            this.prev     = p.prev;

            this.dist     = p.dist;
            this.ancstr   = p.ancstr;
            this.shortest = p.shortest;
            
            for (int a : p.adj) {
                prevPath.put(a, p.prevPath.get(a));
                marked[a] = true;
            }
            
            this.next();
        }

        private void next() {
            SET<Integer>              adjTemp = new SET<Integer>();
            ST<Integer, Integer> prevPathTemp = new ST<Integer, Integer>();
            
            if (prev + 1 < shortest) {
                for (int key : prevPath.keys()) {
                    int p = prevPath.get(key);
                    
                    for (int ancestor : G.adj(key)) {
                        if (marked[ancestor]) continue;
                        
                        adjTemp.add(ancestor);
                        prevPathTemp.put(ancestor, p);
                    }
                }
            }
            
            this.adj      = adjTemp;
            this.prevPath = prevPathTemp;
            this.prev    += 1;
            
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);        // deep copy of argument
        this.R = G.reverse();           // reverse digraph of argument
        //this.cache = new Cache[G.V()][G.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) { 
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (G.V()-1));
        if (v == w) return 0;

        //if (IS_CACHE && cache[v][w] != null) return cache[v][w].length;
        
        SET<Integer> V = new SET<Integer>();
        V.add(v);
        bfs = new DeluxeBFS(R, V);
        Path v2w = length(new Path(V, w));

        SET<Integer> W = new SET<Integer>();
        W.add(w);
        bfs = new DeluxeBFS(R, W);
        Path w2v = length(new Path(W, v));
        
        if (v2w.dist == -1) {
            pathRecent = w2v;
        } else if (w2v.dist == -1) {
            pathRecent = v2w;
        } else if (v2w.dist <= w2v.dist) {
            pathRecent = v2w;
        } else {
            pathRecent = w2v;
        }
        
        return pathRecent.dist;
        
    }

    private Path length(Path path) {
        if (path.adj == null || path.adj.isEmpty()) {
            path.dist     = -1;
            path.ancstr   = -1;
            path.shortest = -1;
            
            return path;
        }

        for (int a : path.adj)
            path.marked[a] = true;

        //DeluxeBFS bfs = new DeluxeBFS(R, path.adj);
        bfs.reinitialize();
        bfs.bfs(R, path.adj);
        
        if (bfs.hasPathTo(path.w)) {
            int dist   = bfs.distTo(path.w) + path.prev;
            int ancstr = bfs.pathTo(path.w).iterator().next();

            if (dist < path.shortest) path.shortest = dist;

            Path path_another = length(new Path(path));
            
            if (path_another.dist != -1 && path_another.dist < dist) {
                path = path_another;
            } else {
                path.dist   = dist;
                path.ancstr = ancstr;
            }
            
            path.v = path.prevPath.get(path.ancstr);

            return path;
        } else {
            path.next();
            return length(path);
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException();

        SET<Integer> vSet = copy(v);
        SET<Integer> wSet = copy(w);
        if (vSet.size() == 0 || wSet.size() == 0) return -1;

        SET<Integer> inter = vSet.intersects(wSet);
        if (!inter.isEmpty()) {
            Path p = new Path(new SET<Integer>(), -1);
            p.dist = 0;
            p.ancstr = inter.iterator().next();
            pathRecent = p;
            
            return 0;
        }

        Path minPath = new Path(new SET<Integer>(), -1);
        minPath = min(vSet, wSet, minPath);
        minPath = min(wSet, vSet, minPath);
        if (minPath.dist == Integer.MAX_VALUE)
            minPath.dist = -1;
        pathRecent = minPath;
        
        return minPath.dist;
        
        /*
        SET<Integer> q = getAncestors(v);
        int l = length(q, w, 1);
        if (l == -1) return -1;

        SET<Integer> set = vCand.intersects((SET<Integer>) v);
        min_v = set.iterator().next();
        length(min_v, min_w);        // find more optimal path by using length(int v, int w)
        //StdOut.printf("l: %d, l2: %d\n", l, length);

        if (IS_CACHE) {
            cache[min_v][min_w] = new Cache();
            cache[min_v][min_w].length = l;
            cache[min_v][min_w].ancestor = ancestor;
        }
        return this.length;
        */
    }
    
    private Path min(SET<Integer> v, SET<Integer> w, Path prevMinPath) {
        Path minPath = new Path(prevMinPath);
        bfs = new DeluxeBFS(R, v);

        for (int w_ : w) {
            Path p = new Path(v, w_);
            p.shortest = minPath.shortest;

            Path path = length(p);

            if (path.dist == -1) continue;
            else if (path.dist < minPath.dist) {
                path.shortest = path.dist;
                minPath = path;
            }
        }

        return minPath;
    }

    private SET<Integer> copy(Iterable<Integer> v) {
        SET<Integer> set = new SET<Integer>();
        for (int iter : v) {
            if (iter < 0 || iter >= G.V()) 
                throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (G.V()-1));
            set.add(iter);
        }

        return set;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) { 
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (G.V()-1));

        if (v == w)              return v;
        //if (IS_CACHE)            return cache[v][w].ancestor;
        if (length(v, w) == -1)  return -1;
        
        return pathRecent.ancstr;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) { 
        if (v == null || w == null) throw new NullPointerException();
        if (length(v, w) == -1)  return -1;

        return pathRecent.ancstr;

        //if (IS_CACHE)         return cache[min_v][min_w].ancestor;
        //else                  return ancestor;
        //return 0;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        //In in = new In(args[0]);
        //Digraph G = new Digraph(in);
        //Digraph G = DigraphGenerator.binaryTree(10);
        //Digraph G = DigraphGenerator.complete(6);
        //Digraph G = DigraphGenerator.cycle(10);
        //Digraph G = DigraphGenerator.dag(10, 10);
        //Digraph G = DigraphGenerator.eulerianCycle(10, 10);
        //Digraph G = DigraphGenerator.eulerianPath(10, 10);
        //Digraph G = DigraphGenerator.path(10);
        //Digraph G = DigraphGenerator.rootedInDAG(10, 15);
        //Digraph G = DigraphGenerator.rootedInTree(10);
        Digraph G = DigraphGenerator.rootedOutDAG(10, 13);
        StdOut.println(G);
        SAP sap = new SAP(G);

        final boolean SINGLE = false;
        if (!SINGLE) {
            while (!StdIn.isEmpty()) {
                String line = StdIn.readLine();
                String[] fields = line.split("/");
                String[] V = fields[0].split(" ");
                String[] W = fields[1].split(" ");
                
                SET<Integer> v = new SET<Integer>();
                SET<Integer> w = new SET<Integer>();
                
                for (int i = 0; i < V.length; i++)
                    v.add(Integer.parseInt(V[i]));
                for (int i = 0; i < W.length; i++)
                    w.add(Integer.parseInt(W[i]));

                int length   = sap.length(v, w);
                int ancestor = sap.ancestor(v, w);
                StdOut.printf("v: %s, w: %s\n", v, w);
                StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            }
            
        } else {  
            while (!StdIn.isEmpty()) {
                int v = StdIn.readInt();
                int w = StdIn.readInt();
                int length   = sap.length(v, w);
                int ancestor = sap.ancestor(v, w);
                StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            }
        }

    }
}
