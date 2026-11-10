import java.util.*;

public class Q2GeneralGraph {
    static final int INF = Integer.MAX_VALUE / 2;
    static int n;
    static List<List<int[]>> pred = new ArrayList<>();
    static List<List<int[]>> succ = new ArrayList<>();

    // ---------- Method 1: topological sort (Kahn) ----------
    static List<Integer> topoOrder() {
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
        return order.size() == n ? order : null;   // null => cycle
    }

    static void solveTopological(int s, int t) {
        List<Integer> order = topoOrder();
        if (order == null) {
            System.out.println("Graph has a cycle. This DP does not apply.");
            return;
        }
        int[] cost = new int[n + 1];
        int[] P = new int[n + 1];
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
        System.out.println("[Topological] cost(" + s + "->" + t + ") = " + cost[t]
                + ", path = " + path(P, s, t));
    }

    // ---------- Method 2: memoised recursion ----------
    static int[] mCost, mP;
    static boolean[] done;

    static int cost(int s, int j) {
        if (done[j]) return mCost[j];
        if (j == s) {
            mCost[j] = 0;
        } else {
            for (int[] e : pred.get(j)) {
                int l = e[0], c = e[1];
                int cl = cost(s, l);               // compute on demand
                if (cl < INF && cl + c < mCost[j]) {
                    mCost[j] = cl + c;
                    mP[j] = l;
                }
            }
        }
        done[j] = true;
        return mCost[j];
    }

    static void solveMemo(int s, int t) {
        mCost = new int[n + 1];
        mP = new int[n + 1];
        done = new boolean[n + 1];
        Arrays.fill(mCost, INF);
        Arrays.fill(mP, -1);
        cost(s, t);
        System.out.println("[Memoised]    cost(" + s + "->" + t + ") = " + mCost[t]
                + ", path = " + path(mP, s, t));
    }

    static String path(int[] P, int s, int v) {
        LinkedList<Integer> p = new LinkedList<>();
        for (int x = v; x != -1; x = P[x]) {
            p.addFirst(x);
            if (x == s) break;
        }
        return p.toString();
    }

    public static void main(String[] args) {
        n = 6;
        int[][] edges = {               // scrambled numbering, still a DAG
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

        solveTopological(1, 4);
        solveMemo(1, 4);
        // Time: O(V + E)   Space: O(V)
    }
}