import java.util.*;

public class Q3SingleSourceAll {
    static final int INF = Integer.MAX_VALUE / 2;

    public static void main(String[] args) {
        int n = 6, s = 1;
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

        // backward DP: COST(v) for EVERY node v (t is not special)
        int[] cost = new int[n + 1];
        int[] P = new int[n + 1];           // P[] = shortest-path tree rooted at s
        Arrays.fill(cost, INF);
        Arrays.fill(P, -1);
        cost[s] = 0;

        for (int j : order) {
            if (j == s) continue;
            for (int[] e : pred.get(j)) {
                int l = e[0], c = e[1];
                if (cost[l] < INF && cost[l] + c < cost[j]) {
                    cost[j] = cost[l] + c;
                    P[j] = l;
                }
            }
        }

        System.out.println("Shortest paths from source " + s + ":");
        for (int v = 1; v <= n; v++) {
            if (cost[v] >= INF) {
                System.out.println("  to " + v + " : unreachable");
            } else {
                LinkedList<Integer> path = new LinkedList<>();
                for (int x = v; x != -1; x = P[x]) {
                    path.addFirst(x);
                    if (x == s) break;
                }
                System.out.println("  to " + v + " : cost = " + cost[v] + ", path = " + path);
            }
        }
        // Time: O(V + E)   Space: O(V)
    }
}