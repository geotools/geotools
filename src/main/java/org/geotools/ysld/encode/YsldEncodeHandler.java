package org.geotools.ysld.encode;

import org.geotools.filter.text.ecql.ECQL;
import org.geotools.ysld.Tuple;
import org.geotools.ysld.parse.Util;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class YsldEncodeHandler<T> implements Iterator<Object> {
    Deque<Map<String,Object>> stack = new ArrayDeque<Map<String, Object>>();

    public YsldEncodeHandler() {
        reset();
    }

    Iterator<T> it;

    YsldEncodeHandler(Iterator<T> it) {
        this.it = it;
    }

    @SuppressWarnings("unchecked")
    YsldEncodeHandler(T obj) {
        this.it = obj != null ? Collections.singleton(obj).iterator() : (Iterator<T>) Collections.emptyIterator();
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
        stack.clear();;
        stack.push(newMap());
        return this;
    }

    YsldEncodeHandler<T> push(String key) {
        Map<String,Object> map = newMap();
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
            stack.peek().put(key, val);
        }
        return this;
    }
    
    /**
     * Should be used to encode values parsed with Util.name
     * @param key
     * @param expr
     * @return
     */
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
            put(key, t.toString());
        }
        return this;
    }

    YsldEncodeHandler<T> putColor(String key, Expression expr) {
        boolean special = false;
        if (expr instanceof Literal) {
            // special case for color literals, drop the # so that we don't need to quote it
            String str = ECQL.toCQL(expr);
            str = stripQuotes(str);

            if (str != null && str.startsWith("#")) {
                str = str.substring(1);
                put(key, str);
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

    YsldEncodeHandler<T> inline(Map<String,Object> values) {
        stack.peek().putAll(values);
        return this;
    }
    
    Object toColorOrNull(Expression expr) {
        Object obj = toObjOrNull(expr, false);
        if (obj instanceof String && expr instanceof Literal) {
            String str = this.stripQuotes(obj.toString());
            if (str.startsWith("#")) {
                obj = str.substring(1);
            }
        }
        return obj;
    }
    
    Object toObjOrNull(Expression expr) {
        return toObjOrNull(expr, false);
    }
    
    
    Object makeNumberIfPossible(String str) {
        if(str==null) return null;
        
        try {
            return Long.parseLong(str);
        }
        catch(NumberFormatException e1) {
            try {
                return Double.parseDouble(str);
            }
            catch(NumberFormatException e2) {
                if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                    return Boolean.parseBoolean(str);
                }
            }
        }
        return str;
    }
    
    String escapeForEmbededCQL(String s) {
        return s; // TODO
    }
    
    Object toObjOrNull(Expression expr, boolean isname) {
        if(isNull(expr)) return null;
        
        List<Expression> subExpressions = Util.splitConcatenates(expr);
        StringBuilder builder = new StringBuilder();
        for(Expression subExpr: subExpressions) {
            if(isNull(subExpr)) {
                // Do nothing
            } else if(subExpr instanceof Literal) {
                builder.append(escapeForEmbededCQL(((Literal) subExpr).getValue().toString()));
            } else {
                builder.append("${").append(escapeForEmbededCQL(ECQL.toCQL(subExpr))).append("}");
            }
        }
        
        Object result = makeNumberIfPossible(builder.toString());
        
        return result;
    }
    
    Object toObjOrNull(String text) {
        String str = text == null ? null : stripQuotes(text);
        if (str != null) {
            try {
                return Long.parseLong(str);
            }
            catch(NumberFormatException e1) {
                try {
                    return Double.parseDouble(str);
                }
                catch(NumberFormatException e2) {
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

    Map<String,Object> get() {
        return stack.peek();
    }

    Map<String,Object> root() {
        return stack.getLast();
    }

    Map<String,Object> newMap() {
        return new LinkedHashMap<String, Object>();
    }

    String stripQuotes(String str) {
        if (str == null) {
            return str;
        }
        // strip quotes
        if (str.charAt(0) == '\'') {
            str = str.substring(1);
        }
        if (str.charAt(str.length()-1) == '\'') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    boolean isNull(Expression expr) {
        return expr == null || expr == Expression.NIL;
    }
}
