import java.util.*;

public class Q4ArbitrarySourceTarget {
    static final int INF = Integer.MAX_VALUE / 2;
    static int n;
    static List<List<int[]>> pred = new ArrayList<>();
    static List<List<int[]>> succ = new ArrayList<>();
    static List<Integer> order;

    static List<Integer> topoOrder() {
        int[] indeg = new int[n + 1];
        for (int v = 1; v <= n; v++) indeg[v] = pred.get(v).size();
        Deque<Integer> q = new ArrayDeque<>();
        for (int v = 1; v <= n; v++) if (indeg[v] == 0) q.add(v);
        List<Integer> res = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            res.add(u);
            for (int[] e : succ.get(u))
                if (--indeg[e[0]] == 0) q.add(e[0]);
        }
        return res.size() == n ? res : null;
    }

    // works for ANY choice of s and t in a DAG
    static void solve(int s, int t) {
        int[] cost = new int[n + 1];
        int[] P = new int[n + 1];
        Arrays.fill(cost, INF);
        Arrays.fill(P, -1);
        cost[s] = 0;                       // everything else stays INF until reached

        for (int j : order) {              // nodes before s in the order stay INF
            if (j == s) continue;
            for (int[] e : pred.get(j)) {
                int l = e[0], c = e[1];
                if (cost[l] < INF && cost[l] + c < cost[j]) {
                    cost[j] = cost[l] + c;
                    P[j] = l;
                }
            }
        }

        if (cost[t] >= INF) {
            System.out.println(s + " -> " + t + " : t is NOT reachable from s");
            return;
        }
        LinkedList<Integer> path = new LinkedList<>();
        for (int x = t; x != -1; x = P[x]) {
            path.addFirst(x);
            if (x == s) break;
        }
        System.out.println(s + " -> " + t + " : cost = " + cost[t] + ", path = " + path);
    }

    public static void main(String[] args) {
        n = 6;
        int[][] edges = {
            {5,3,2},{5,2,4},{3,6,3},{2,6,1},{1,5,1},{6,4,2},{3,4,7}
        };
        for (int i = 0; i <= n; i++) {
            pred.add(new ArrayList<>());
            succ.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            succ.get(e[0]).add(new int[]{e[1], e[2]});
            pred.get(e[1]).add(new int[]{e[0], e[2]});
        }

        order = topoOrder();
        if (order == null) {
            System.out.println("Graph has a cycle. This DP does not apply.");
            return;
        }

        solve(1, 4);   // original s and t
        solve(5, 4);   // redefined s
        solve(3, 4);   // redefined s
        solve(6, 5);   // t not reachable from s
    }
}