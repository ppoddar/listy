package com.listy.orm;

import java.util.Iterator;

/**
 * Created by ppoddar on 7/14/16.
 */
public class StringUtils {

    public static StringBuilder enclose(char start, char end, StringBuilder v) {
        return new StringBuilder().append(start).append(v).append(end);
    }

    public static StringBuilder enclose(char start, char end, String v) {
        return new StringBuilder().append(start).append(v).append(end);
    }

    public static StringBuilder join(char sep, Iterator<?> it) {
        StringBuilder buf = new StringBuilder();
        while (it.hasNext()) {
            buf.append(it.next());
            if (it.hasNext()) buf.append(sep);
        }
        return buf;
    }

    public static StringBuilder joinRepeat(char sep, String v, int n) {
        StringBuilder buf = new StringBuilder();
        if (n == 1) return buf.append(v);
        for (int i = 0; i < n; i++) {
            buf.append(v);
            if (i != n - 1) buf.append(sep);
        }
        return buf;
    }
}
