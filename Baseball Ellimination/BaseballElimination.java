import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class BaseballElimination {
    private int teamNum;
    private String[] team;
    private int[] w, l, r;
    private int[][] g;

    private void readFile(String filename) {
        In in = new In(filename);
        teamNum = in.readInt();

        team = new String[teamNum];
        w = new int[teamNum];
        l = new int[teamNum];
        r = new int[teamNum];
        g = new int[teamNum][teamNum];

        in.readLine(); // to remove spaces (no meaning)

        for (int i = 0; i < teamNum; i++)
            readLine(in, i);

        System.out.println(Arrays.toString(w));

    }

    private void readLine(In in, int i) {
        team[i] = in.readString();
        w[i] = in.readInt();
        l[i] = in.readInt();
        r[i] = in.readInt();
        for (int j = 0; j < teamNum; j++)
            g[i][j] = in.readInt();
    }

    public BaseballElimination(String filename) {       // create a baseball division from given filename in format specified below
        readFile(filename);
    }

    public int numberOfTeams() {                        // number of teams
        return teamNum;
    }

    /*
    public Iterable<String> teams() {                   // all teams
    }
    public int wins(String team) {                      // number of wins for given team
    } 
    public int losses(String team) {                    // number of losses for given team
    }
    public int remaining(String team) {                 // number of remaining games for given team 
    }
    public int against(String team1, String team2) {    // number of remaining games between team1 and team2
    }
    public boolean isEliminated(String team) {          // is given team eliminated?
    }
    public Iterable<String> certificateOfElimination(String team) {  
        // subset R of teams that eliminates given team; null if not eliminated
    }
    */

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        /*
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
        */
    }
}
