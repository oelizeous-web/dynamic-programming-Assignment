import java.util.*;

public class F1ForwardOrdered {
    static final int INF = Integer.MAX_VALUE / 2;

    public static void main(String[] args) {
        int n = 12, s = 1, t = 12;
        int[][] edges = {               // {from, to, cost}
            {1,2,9},{1,3,7},{1,4,3},{1,5,2},
            {2,6,4},{2,7,2},{2,8,1},
            {3,6,2},{3,7,7},
            {4,8,11},
            {5,7,11},{5,8,8},
            {6,9,6},{6,10,5},
            {7,9,4},{7,10,3},
            {8,10,5},{8,11,6},
            {9,12,4},{10,12,2},{11,12,5}
        };

        // successor lists: succ[i] holds {j, c(i,j)}
        List<List<int[]>> succ = new ArrayList<>();
        for (int i = 0; i <= n; i++) succ.add(new ArrayList<>());
        for (int[] e : edges) succ.get(e[0]).add(new int[]{e[1], e[2]});

        int[] cost = new int[n + 1];    // cost[i] = shortest distance from i to t
        int[] D = new int[n + 1];       // D[i] = next node on the best path from i
        Arrays.fill(cost, INF);
        Arrays.fill(D, -1);
        cost[t] = 0;

        // COST(i) = min over successors j of { c(i,j) + COST(j) }
        // Nodes are in stage order, so every successor j > i is already computed.
        for (int i = t - 1; i >= s; i--) {
            for (int[] e : succ.get(i)) {
                int j = e[0], c = e[1];
                if (cost[j] < INF && c + cost[j] < cost[i]) {
                    cost[i] = c + cost[j];
                    D[i] = j;
                }
            }
        }

        // recover path from s forward to t
        List<Integer> path = new ArrayList<>();
        for (int x = s; x != -1; x = D[x]) {
            path.add(x);
            if (x == t) break;
        }
        System.out.println("Shortest cost from " + s + " to " + t + " = " + cost[s]);
        System.out.println("Path: " + path);
        // Time: O(V + E)   Space: O(V)
    }
}
