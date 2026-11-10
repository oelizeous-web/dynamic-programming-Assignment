import java.util.*;

public class Q1BackwardOrdered {
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

        // predecessor lists: pred[j] holds {l, c(l,j)}
        List<List<int[]>> pred = new ArrayList<>();
        for (int i = 0; i <= n; i++) pred.add(new ArrayList<>());
        for (int[] e : edges) pred.get(e[1]).add(new int[]{e[0], e[2]});

        int[] cost = new int[n + 1];
        int[] P = new int[n + 1];
        Arrays.fill(cost, INF);
        Arrays.fill(P, -1);
        cost[s] = 0;

        // COST(j) = min over predecessors l of { COST(l) + c(l,j) }
        for (int j = s + 1; j <= n; j++) {
            for (int[] e : pred.get(j)) {
                int l = e[0], c = e[1];
                if (cost[l] < INF && cost[l] + c < cost[j]) {
                    cost[j] = cost[l] + c;
                    P[j] = l;
                }
            }
        }

        // recover path from t back to s
        LinkedList<Integer> path = new LinkedList<>();
        for (int x = t; x != -1; x = P[x]) {
            path.addFirst(x);
            if (x == s) break;
        }

        System.out.println("Shortest cost from " + s + " to " + t + " = " + cost[t]);
        System.out.println("Path: " + path);
        // Time: O(V + E)   Space: O(V)
    }
}