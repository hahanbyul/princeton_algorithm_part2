import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {
    private final int s = 0;
    private final int t = 1;

    private int teamNum, gameNum, vertexNum;
    private String[] team;
    private int[] wins, losses, remaining;
    private int[][] g;

    private int[][] gameAddr;
    private int[]   teamAddr;

    private boolean[] isTrivial;

    private FlowNetwork G;
    private FordFulkerson ff;

    private void readLine(In in, int i) {
        team[i] = in.readString();
        wins[i] = in.readInt();
        losses[i] = in.readInt();
        remaining[i] = in.readInt();
        for (int j = 0; j < teamNum; j++)
            g[i][j] = in.readInt();
    }

    public BaseballElimination(String filename) {       // create a baseball division from given filename in format specified below
        In in = new In(filename);
        teamNum = in.readInt();
        gameNum = (teamNum-1)*(teamNum-1)/2;

        team = new String[teamNum];
        wins = new int[teamNum];
        losses = new int[teamNum];
        remaining = new int[teamNum];
        g = new int[teamNum][teamNum];

        for (int i = 0; i < teamNum; i++)
            readLine(in, i);
    }

    private void updateAddr(int team) {
        int V = teamNum;

        gameAddr = new int[V][V];
        int count = 2;          // offset: s, t
        for (int i = 0; i < V; i++) {
            if (i == team) continue;
            for (int j = i+1; j < V; j++) {
                if (j == team) continue;
                gameAddr[i][j] = count;
                count++;
            }
        }

        teamAddr = new int[V];
        for (int i = 0; i < V; i++) {
            if (i == team) continue;
            teamAddr[i] = count; // offset: 2, gameNum
            count++;
        }

        vertexNum = count;
    }

    public int numberOfTeams() {                        // number of teams
        return teamNum;
    }

    public Iterable<String> teams() {                   // all teams
        return Arrays.asList(team);
    }

    private int getTeamIdx(String team) {
        int i = 0;
        for (i = 0; i < teamNum; i++) {
            if (team.equals(this.team[i]))
                break;
        }
        return i;
    }

    private boolean isInTeamList(String team) {
        for (String t : teams()) {
            if (t.equals(team))
                return true;
        }

        return false;
    }
    public int wins(String team) {                      // number of wins for given team
        if (!isInTeamList(team)) throw new IllegalArgumentException();
        int idx = getTeamIdx(team);
        if (idx == teamNum) throw new IllegalArgumentException();
        return wins[idx];

    } 

    public int losses(String team) {                    // number of losses for given team
        if (!isInTeamList(team)) throw new IllegalArgumentException();
        int idx = getTeamIdx(team);
        if (idx == teamNum) throw new IllegalArgumentException();
        return losses[idx];
    }

    public int remaining(String team) {                 // number of remaining games for given team 
        if (!isInTeamList(team)) throw new IllegalArgumentException();
        return remaining[getTeamIdx(team)];
    }

    public int against(String team1, String team2) {    // number of remaining games between team1 and team2
        if (!isInTeamList(team1)) throw new IllegalArgumentException();
        if (!isInTeamList(team2)) throw new IllegalArgumentException();
        return g[getTeamIdx(team1)][getTeamIdx(team2)];
    }

    private boolean isTriviallyEliminated(String team) {
        if (!isInTeamList(team)) throw new IllegalArgumentException();
        isTrivial = new boolean[teamNum];

        boolean ret = false;
        String theOtherTeam;
        for (int i = 0; i < teamNum; i++) {
            theOtherTeam = this.team[i];
            if (wins(theOtherTeam) > wins(team) + remaining(team)) {
                ret = true;
                isTrivial[i] = true;
            }

        }

        return ret;
    }

    public boolean isEliminated(String team) {          // is given team eliminated?
        if (!isInTeamList(team)) throw new IllegalArgumentException();
        if (isTriviallyEliminated(team))
            return true;

        int teamIdx = getTeamIdx(team);
        
        updateAddr(teamIdx);

        makeGraph(teamIdx);

        ff = new FordFulkerson(G, s, t);

        boolean ret = false;
        for (FlowEdge e : G.adj(s)) {
            if (e.flow() != e.capacity()) {
                ret = true;
            }
        }

        return ret;
    }

    private void makeGraph(int team) {
        G = new FlowNetwork(vertexNum);
        double capacity;
        // add edge ending 't'
        for (int i = 0; i < teamNum; i++) {
            int v = teamAddr[i];
            if (v == 0) continue;
            capacity = wins[team] - wins[i] + remaining[team];
            FlowEdge e = new FlowEdge(v, t, capacity);
            G.addEdge(e);
        }


        // add edge starting 's'
        for (int i = 0; i < teamNum; i++) {
            for (int j = 0; j < teamNum; j++) {
                int w = gameAddr[i][j];
                if (w == 0) continue;

                FlowEdge e = new FlowEdge(s, w, g[i][j]);
                G.addEdge(e);

                int v = w;
                w = teamAddr[i];
                e = new FlowEdge(v, w, Double.POSITIVE_INFINITY);
                G.addEdge(e);

                w = teamAddr[j];
                e = new FlowEdge(v, w, Double.POSITIVE_INFINITY);
                G.addEdge(e);
            }
        }
    }

    public Iterable<String> certificateOfElimination(String team) {  
        // subset R of teams that eliminates given team; null if not eliminated
        if (!isInTeamList(team)) throw new IllegalArgumentException();

        ArrayList<String> ret = new ArrayList<String>();

        if (isTriviallyEliminated(team)) {
            for (int i = 0; i < teamNum; i++) {
                if (isTrivial[i]) ret.add(this.team[i]);
            }
            return ret;
        } else if (isEliminated(team)) {
            for (int i = 0; i < teamNum; i++) {
                int v = teamAddr[i];
                if (v == 0) continue;

                if (ff.inCut(v)) { ret.add(this.team[i]); }
            }

            return ret;

        } else { return null; }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        // BaseballElimination division = new BaseballElimination("baseball/teams5.txt");

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
