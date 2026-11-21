import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    private List<List<Integer>> origAdj;
    private List<List<Integer>> newAdj;
    private int[] parent;
    private char[] chars;
    private int[] lastSeen;
    private int[] ans;

    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;
        this.parent = parent;
        this.chars = s.toCharArray();

        origAdj = new ArrayList<>(n);
        newAdj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            origAdj.add(new ArrayList<>());
            newAdj.add(new ArrayList<>());
        }

        for (int i = 1; i < n; i++) {
            origAdj.get(parent[i]).add(i);
        }

        lastSeen = new int[26];
        Arrays.fill(lastSeen, -1);

        assignParents(0);

        ans = new int[n];
        calcSubtreeSizes(0);
        return ans;
    }

    private void assignParents(int u) {
        int ch = chars[u] - 'a';
        int newParent = (lastSeen[ch] != -1) ? lastSeen[ch] : parent[u];
        if (newParent != -1) {
            newAdj.get(newParent).add(u);
        }

        int prevSeen = lastSeen[ch];
        lastSeen[ch] = u;

        for (int v : origAdj.get(u)) {
            assignParents(v);
        }

        // Restore previous ancestor on backtrack so sibling subtrees don't leak
        lastSeen[ch] = prevSeen;
    }

    private int calcSubtreeSizes(int u) {
        int size = 1;
        for (int v : newAdj.get(u)) {
            size += calcSubtreeSizes(v);
        }
        return ans[u] = size;
    }
}