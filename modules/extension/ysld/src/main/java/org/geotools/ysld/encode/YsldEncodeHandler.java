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
package org.geotools.ysld.encode;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.ysld.Tuple;
import org.geotools.ysld.Ysld;
import org.geotools.ysld.parse.Util;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Encodes a single style object as YSLD
 *
 * @param <T> Class of the style object
 */
public abstract class YsldEncodeHandler<T> implements Iterator<Object> {
    Deque<Map<String, Object>> stack = new ArrayDeque<Map<String, Object>>();

    public YsldEncodeHandler() {
        reset();
    }

    Iterator<T> it;

    YsldEncodeHandler(Iterator<T> it) {
        this.it = it;
    }

    @SuppressWarnings("unchecked")
    YsldEncodeHandler(T obj) {
        this.it =
                obj != null
                        ? Collections.singleton(obj).iterator()
                        : (Iterator<T>) Collections.emptyIterator();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public Object next() {
        reset();
        encode(it.next());
        return root();
    }

    protected abstract void encode(T next);

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    YsldEncodeHandler<T> reset() {
        stack.clear();
        stack.push(newMap());
        return this;
    }

    YsldEncodeHandler<T> push(String key) {
        Map<String, Object> map = newMap();
        stack.peek().put(key, map);
        stack.push(map);
        return this;
    }

    YsldEncodeHandler<T> pop() {
        stack.pop();
        return this;
    }

    YsldEncodeHandler<T> put(String key, Object val) {
        if (val != null) {
            Map<String, Object> peek = stack.peek();
            peek.put(key, val);
        }
        return this;
    }

    /** Should be used to encode values parsed with Util.name */
    YsldEncodeHandler<T> putName(String key, Expression expr) {
        if (expr != null && expr != Expression.NIL) {
            put(key, toObjOrNull(expr, true));
        }
        return this;
    }

    YsldEncodeHandler<T> put(String key, Expression expr) {
        if (expr != null && expr != Expression.NIL) {
            put(key, toObjOrNull(expr, false));
        }
        return this;
    }

    YsldEncodeHandler<T> put(String key, Expression e1, Expression e2) {
        Tuple t = Tuple.of(toObjOrNull(e1, false), toObjOrNull(e2, false));
        if (!t.isNull()) {
            put(key, t);
        }
        return this;
    }

    YsldEncodeHandler<T> putColor(String key, Expression expr) {
        boolean special = false;
        if (expr instanceof Literal) {
            // special case for color literals, drop the # so that we don't need to quote it
            String str = ECQL.toCQL(expr);
            str = Util.stripQuotes(str);

            if (str != null && str.startsWith("#")) {
                str = str.substring(1);
                put(key, makeColorIfPossible(str));
                special = true;
            }
        }
        if (!special) {
            put(key, expr);
        }
        return this;
    }

    YsldEncodeHandler<T> inline(YsldEncodeHandler<?> e) {
        if (e.hasNext()) {
            e.next();
            inline(e.root());
        }
        return this;
    }

    YsldEncodeHandler<T> inline(Map<String, Object> values) {
        stack.peek().putAll(values);
        return this;
    }

    Object toColorOrNull(Expression expr) {
        Object obj;
        if (expr instanceof Literal) {
            obj = ((Literal) expr).getValue();
            if (obj instanceof Color) {
                return obj;
            }
        }
        obj = toObjOrNull(expr, false);
        if (obj instanceof String && expr instanceof Literal) {
            String str = Util.stripQuotes(obj.toString());
            obj = makeColorIfPossible(str);
        }
        return obj;
    }

    /** See {@link #toObjOrNull(Expression, boolean)} */
    Object toObjOrNull(Expression expr) {
        return toObjOrNull(expr, false);
    }

    static final Pattern COLOR_PATTERN = Pattern.compile("^#?([a-fA-F0-9]{6})$");

    Object makeColorIfPossible(String str) {
        Matcher m = COLOR_PATTERN.matcher(str);
        if (m.matches()) {
            // If it matches the regexp, then we know it should parse
            int i = Integer.parseInt(m.group(1), 16);
            return new Color(i);
        } else {
            return str;
        }
    }

    static final Pattern EMBEDED_EXPRESSION_TO_ESCAPE = Pattern.compile("[$}\\\\]");

    /** Escapes the characters '$', '}', and '\' by prepending '\'. */
    String escapeForEmbededCQL(String s) {
        return EMBEDED_EXPRESSION_TO_ESCAPE.matcher(s).replaceAll("\\\\$0");
    }

    /**
     * Takes an {@link Expression} and encodes it as YSLD. Literals are encoded as Strings.
     * Concatenation expressions are removed, as they are implicit in the YSLD syntax. Other
     * non-literal expressions are wrapped in ${}.
     *
     * <p>If the resulting string can be converted to the number, returns an appropriate {@link
     * Number} object. Otherwise returns a {@link String}. Returns null if the passed expressison
     * was null
     *
     * @param expr Expression to encode
     * @return {@link String} or {@link Number} representation of expr, or null if expr is null.
     */
    Object toObjOrNull(Expression expr, boolean isname) {
        if (isNull(expr)) return null;

        List<Expression> subExpressions = Util.splitConcatenates(expr);

        StringBuilder builder = new StringBuilder();
        for (Expression subExpr : subExpressions) {
            if (!isNull(subExpr)) {
                if (subExpr instanceof Literal) {
                    builder.append(escapeForEmbededCQL(((Literal) subExpr).getValue().toString()));
                } else {
                    builder.append("${")
                            .append(escapeForEmbededCQL(ECQL.toCQL(subExpr)))
                            .append("}");
                }
            }
        }

        Object result = Util.makeNumberIfPossible(builder.toString());

        return result;
    }

    Object toObjOrNull(String text) {
        String str = text == null ? null : Util.stripQuotes(text);
        if (str != null) {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException e1) {
                try {
                    return Double.parseDouble(str);
                } catch (NumberFormatException e2) {
                    if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                        return Boolean.parseBoolean(str);
                    }
                }
            }
        }
        return text;
    }

    Expression nullIf(Expression expr, double value) {
        return nullIf(expr, value, Double.class);
    }

    Expression nullIf(Expression expr, String value) {
        return nullIf(expr, value, String.class);
    }

    <T> Expression nullIf(Expression expr, T value, Class<T> clazz) {
        if (expr instanceof Literal) {
            T t = expr.evaluate(null, clazz);
            if (t != null && t.equals(value)) {
                return null;
            }
        }
        return expr;
    }

    Map<String, Object> get() {
        return stack.peek();
    }

    Map<String, Object> root() {
        return stack.getLast();
    }

    Map<String, Object> newMap() {
        return new LinkedHashMap<String, Object>();
    }

    boolean isNull(Expression expr) {
        return expr == null || expr == Expression.NIL;
    }

    protected void vendorOptions(Map<String, String> options) {
        if (!options.isEmpty()) {
            for (Map.Entry<String, String> kv : options.entrySet()) {
                String option = Ysld.OPTION_PREFIX + kv.getKey();
                String text = kv.getValue();

                put(option, toObjOrNull(text));
            }
        }
    }
}
