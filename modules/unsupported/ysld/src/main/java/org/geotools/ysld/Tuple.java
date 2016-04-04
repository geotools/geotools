/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.ysld;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.ysld.parse.Util;

public class Tuple {
    static final Map<Integer,Pattern> PATTERNS = new HashMap<Integer, Pattern>();

    public static Tuple of(Object...values) {
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

    Object[] values;
    Pattern pattern;

    Tuple(int n, Pattern pattern) {
        this.values = new String[n];
        this.pattern = pattern;
    }

    @Deprecated
    public Tuple parse(String str) throws IllegalArgumentException {
        // TODO Log a warning that this is deprecated
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
    
    public Tuple parse(List<?> seq) throws IllegalArgumentException {
        
        if (seq.size()!=values.length) {
            throw new IllegalArgumentException();
        }
        
        for (int i = 0; i < values.length; i++) {
            Object val = seq.get(i);
            if (val != null && !"".equals(val)) {
                values[i] = val.toString();
            }
        }
        return this;
    }
    
    public Tuple parse(Object obj) throws IllegalArgumentException {
        if (obj instanceof List) {
            return parse((List<?>) obj);
        } else if (obj instanceof String) {
            return parse((String) obj);
        } else if (obj instanceof YamlObject) {
            return parse(((YamlObject<?>) obj).raw());
        } 
        throw new IllegalArgumentException();
    }

    public Object at(int i) {
        return values[i];
    }

    public String strAt(int i) {
        Object obj = at(i);
        return obj != null ? obj.toString() : null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < values.length; i++) {
            Object v = values[i];
            if (v != null) {
                if(v instanceof Color) {
                    sb.append('\'');
                    sb.append(Util.serializeColor((Color) v));
                    sb.append('\'');
                } else {
                    sb.append(v);
                }
            }
            sb.append(",");
        }
        sb.setLength(sb.length()-1);
        return sb.append(")").toString();
    }
    
    public List<?> toList() {
        return Arrays.asList(values);
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
