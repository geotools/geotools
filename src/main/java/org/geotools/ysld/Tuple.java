package org.geotools.ysld;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tuple {
    static final Map<Integer,Pattern> PATTERNS = new HashMap<Integer, Pattern>();

    public static Tuple of(String...values) {
        Tuple t = of(values.length);
        t.values = values;
        return t;
    }

    public static Tuple of(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be greater than zero");
        }

        Pattern p = PATTERNS.get(n);
        if (p == null) {
            StringBuilder sb = new StringBuilder("\\s*\\(");
            for (int i = 0; i < n; i++) {
                sb.append("\\s*(.*)\\s*,");
            }
            sb.setLength(sb.length()-1);
            p = Pattern.compile(sb.append("\\)\\s*").toString());
            PATTERNS.put(n, p);
        }
        return new Tuple(n, p);
    }

    String[] values;
    Pattern pattern;

    Tuple(int n, Pattern pattern) {
        this.values = new String[n];
        this.pattern = pattern;
    }

    public Tuple parse(String str) throws IllegalArgumentException {
        Matcher m = pattern.matcher(str);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < values.length; i++) {
            String val = m.group(i+1);
            if (val != null && !"".equals(val)) {
                values[i] = val;
            }
        }
        return this;
    }

    public String at(int i) {
        return values[i];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < values.length; i++) {
            String v = values[i];
            if (v != null) {
                sb.append(v);
            }
            sb.append(",");
        }
        sb.setLength(sb.length()-1);
        return sb.append(")").toString();
    }

    public boolean isNull() {
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                return false;
            }
        }
        return true;
    }

}
