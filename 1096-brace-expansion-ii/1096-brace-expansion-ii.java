import java.util.*;

class Solution {
    int i;

    public List<String> braceExpansionII(String expression) {
        i = 0;
        Set<String> result = parse(expression);
        return new ArrayList<>(result);
    }

    private Set<String> parse(String s) {
        Set<String> result = new TreeSet<>();
        Set<String> current = new TreeSet<>();
        current.add("");

        while (i < s.length() && s.charAt(i) != '}') {
            char c = s.charAt(i);

            if (c == '{') {
                i++;
                Set<String> next = parse(s);
                i++;

                current = combine(current, next);
            } 
            else if (c == ',') {
                result.addAll(current);
                current = new TreeSet<>();
                current.add("");
                i++;
            } 
            else {
                Set<String> next = new TreeSet<>();
                next.add(String.valueOf(c));
                current = combine(current, next);
                i++;
            }
        }

        result.addAll(current);
        return result;
    }

    private Set<String> combine(Set<String> a, Set<String> b) {
        Set<String> result = new TreeSet<>();

        for (String x : a) {
            for (String y : b) {
                result.add(x + y);
            }
        }

        return result;
    }
}