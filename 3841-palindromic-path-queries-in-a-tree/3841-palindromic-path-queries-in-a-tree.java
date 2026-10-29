import java.util.*;

class Solution {
    int n, size, timer;
    int[] parent, depth, subtree, heavy, head, pos, seg;
    List<Integer>[] adj;

    void build(int[][] edges) {
        adj = new ArrayList[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();

        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        parent = new int[n];
        depth = new int[n];
        subtree = new int[n];
        heavy = new int[n];
        head = new int[n];
        pos = new int[n];

        Arrays.fill(parent, -2);
        Arrays.fill(heavy, -1);

        int[] order = new int[n];
        int[] stack = new int[n];
        int top = 0, count = 0;

        stack[top++] = 0;
        parent[0] = -1;

        while (top > 0) {
            int u = stack[--top];
            order[count++] = u;

            for (int v : adj[u]) {
                if (v == parent[u]) continue;
                parent[v] = u;
                depth[v] = depth[u] + 1;
                stack[top++] = v;
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            int u = order[i];
            subtree[u] = 1;
            int maxSize = 0;

            for (int v : adj[u]) {
                if (parent[v] == u) {
                    subtree[u] += subtree[v];
                    if (subtree[v] > maxSize) {
                        maxSize = subtree[v];
                        heavy[u] = v;
                    }
                }
            }
        }

        top = 0;
        stack[top++] = 0;
        timer = 0;

        while (top > 0) {
            int start = stack[--top];

            for (int u = start; u != -1; u = heavy[u]) {
                head[u] = start;
                pos[u] = timer++;

                for (int v : adj[u]) {
                    if (parent[v] == u && v != heavy[u]) {
                        stack[top++] = v;
                    }
                }
            }
        }
    }

    void update(int index, int value) {
        index += size;
        seg[index] = value;

        while (index > 1) {
            index >>= 1;
            seg[index] = seg[index << 1] ^ seg[index << 1 | 1];
        }
    }

    int rangeQuery(int left, int right) {
        int result = 0;
        left += size;
        right += size + 1;

        while (left < right) {
            if ((left & 1) == 1) result ^= seg[left++];
            if ((right & 1) == 1) result ^= seg[--right];
            left >>= 1;
            right >>= 1;
        }

        return result;
    }

    int pathQuery(int u, int v) {
        int result = 0;

        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }

            result ^= rangeQuery(pos[head[u]], pos[u]);
            u = parent[head[u]];
        }

        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        result ^= rangeQuery(pos[u], pos[v]);
        return result;
    }

    public List<Boolean> palindromePath(int n, int[][] edges,
                                         String s, String[] queries) {
        this.n = n;
        build(edges);

        size = 1;
        while (size < n) size <<= 1;

        seg = new int[size << 1];

        for (int i = 0; i < n; i++) {
            seg[size + pos[i]] = 1 << (s.charAt(i) - 'a');
        }

        for (int i = size - 1; i > 0; i--) {
            seg[i] = seg[i << 1] ^ seg[i << 1 | 1];
        }

        char[] chars = s.toCharArray();
        List<Boolean> answer = new ArrayList<>();

        for (String query : queries) {
            String[] parts = query.split(" ");

            if (parts[0].equals("update")) {
                int u = Integer.parseInt(parts[1]);
                char c = parts[2].charAt(0);

                chars[u] = c;
                update(pos[u], 1 << (c - 'a'));
            } else {
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);

                int mask = pathQuery(u, v);
                answer.add((mask & (mask - 1)) == 0);
            }
        }

        return answer;
    }
}