class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        int n = nums1.length;
        long k = (long) k1 + k2;
        int[] diff = new int[n];
        int max = 0;
        long sum = 0;

        for (int i = 0; i < n; i++) {
            diff[i] = Math.abs(nums1[i] - nums2[i]);
            max = Math.max(max, diff[i]);
            sum += diff[i];
        }

        if (k >= sum) return 0;

        int l = 0, r = max;
        while (l < r) {
            int mid = l + (r - l) / 2;
            long need = 0;

            for (int d : diff) {
                if (d > mid) need += d - mid;
            }

            if (need <= k) r = mid;
            else l = mid + 1;
        }

        long ans = 0;
        long used = 0;

        for (int d : diff) {
            int x = Math.min(d, l);
            ans += (long) x * x;
            if (d > l) used += d - l;
        }

        long extra = k - used;

        for (int d : diff) {
            if (extra == 0) break;
            if (d >= l && d > 0) {
                ans -= (long) l * l;
                ans += (long) (l - 1) * (l - 1);
                extra--;
            }
        }

        return ans;
    }
}