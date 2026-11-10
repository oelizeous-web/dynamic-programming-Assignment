import java.util.*;

public class F2ForwardGeneralGraph {
    static final int INF = Integer.MAX_VALUE / 2;
    static int n;
    static List<List<int[]>> pred = new ArrayList<>();
    static List<List<int[]>> succ = new ArrayList<>();

    // ---------- Method 1: topological sort (Kahn), processed in REVERSE ----------
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
        int[] D = new int[n + 1];
        Arrays.fill(cost, INF);
        Arrays.fill(D, -1);
        cost[t] = 0;
        // reverse topological order: every successor is computed before its predecessor
        for (int k = n - 1; k >= 0; k--) {
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
        System.out.println("[Topological] cost(" + s + "->" + t + ") = " + cost[s]
                + ", path = " + path(D, s, t));
    }

    // ---------- Method 2: memoised recursion ----------
    static int[] mCost, mD;
    static boolean[] done;

    static int cost(int t, int i) {
        if (done[i]) return mCost[i];
        if (i == t) {
            mCost[i] = 0;
        } else {
            for (int[] e : succ.get(i)) {
                int j = e[0], c = e[1];
                int cj = cost(t, j);               // compute on demand
                if (cj < INF && c + cj < mCost[i]) {
                    mCost[i] = c + cj;
                    mD[i] = j;
                }
            }
        }
        done[i] = true;
        return mCost[i];
    }

    static void solveMemo(int s, int t) {
        mCost = new int[n + 1];
        mD = new int[n + 1];
        done = new boolean[n + 1];
        Arrays.fill(mCost, INF);
        Arrays.fill(mD, -1);
        cost(t, s);
        System.out.println("[Memoised]    cost(" + s + "->" + t + ") = " + mCost[s]
                + ", path = " + path(mD, s, t));
    }

    // follow D[] from s forward to t
    static String path(int[] D, int s, int t) {
        List<Integer> p = new ArrayList<>();
        for (int x = s; x != -1; x = D[x]) {
            p.add(x);
            if (x == t) break;
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
