import java.util.*;

public class F3SingleDestinationAll {
    static final int INF = Integer.MAX_VALUE / 2;

    public static void main(String[] args) {
        int n = 6, t = 4;               // single destination t
        int[][] edges = {
            {5,3,2},{5,2,4},{3,6,3},{2,6,1},{1,5,1},{6,4,2},{3,4,7}
        };
        List<List<int[]>> pred = new ArrayList<>();
        List<List<int[]>> succ = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            pred.add(new ArrayList<>());
            succ.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            succ.get(e[0]).add(new int[]{e[1], e[2]});
            pred.get(e[1]).add(new int[]{e[0], e[2]});
        }

        // topological order (Kahn)
        int[] indeg = new int[n + 1];
        for (int v = 1; v <= n; v++) indeg[v] = pred.get(v).size();
        Deque<Integer> q = new ArrayDeque<>();
        for (int v = 1; v <= n; v++) if (indeg[v] == 0) q.add(v);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int[] e : succ.get(u))
                if (--indeg[e[0]] == 0) q.add(e[0]);
        }
        if (order.size() != n) {
            System.out.println("Graph has a cycle. This DP does not apply.");
            return;
        }

        // forward DP: COST(v) = shortest distance from v TO t, for EVERY node v (s is not special)
        int[] cost = new int[n + 1];
        int[] D = new int[n + 1];       // D[] = shortest-path tree pointing toward t
        Arrays.fill(cost, INF);
        Arrays.fill(D, -1);
        cost[t] = 0;
        for (int k = n - 1; k >= 0; k--) {          // reverse topological order
            int i = order.get(k);
            if (i == t) continue;
            for (int[] e : succ.get(i)) {
                int j = e[0], c = e[1];
                if (cost[j] < INF && c + cost[j] < cost[i]) {
                    cost[i] = c + cost[j];
                    D[i] = j;
                }
            }
        }

        System.out.println("Shortest paths to destination " + t + ":");
        for (int v = 1; v <= n; v++) {
            if (cost[v] >= INF) {
                System.out.println("  from " + v + " : cannot reach " + t);
            } else {
                List<Integer> path = new ArrayList<>();
                for (int x = v; x != -1; x = D[x]) {
                    path.add(x);
                    if (x == t) break;
                }
                System.out.println("  from " + v + " : cost = " + cost[v] + ", path = " + path);
            }
        }
        // Time: O(V + E)   Space: O(V)
    }
}
