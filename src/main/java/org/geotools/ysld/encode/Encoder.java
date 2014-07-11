package org.geotools.ysld.encode;

import org.geotools.ysld.Tuple;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

import java.util.*;

public abstract class Encoder<T> implements Iterator<Object> {

    Deque<Map<String,Object>> stack = new ArrayDeque<Map<String, Object>>();

    public Encoder() {
        reset();
    }

    Iterator<T> it;

    Encoder(Iterator<T> it) {
        this.it = it;
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

    Encoder reset() {
        stack.clear();;
        stack.push(newMap());
        return this;
    }

    Encoder push(String key) {
        Map<String,Object> map = newMap();
        stack.peek().put(key, map);
        stack.push(map);
        return this;
    }

    Encoder pop() {
        stack.pop();
        return this;
    }

    Encoder put(String key, Object val) {
        if (val != null) {
            stack.peek().put(key, val);
        }
        return this;
    }

    Encoder put(String key, Expression expr) {
        if (expr != null) {
            put(key, toStringOrNull(expr));
        }
        return this;
    }

    Encoder put(String key, Expression e1, Expression e2) {
        Tuple t = Tuple.of(toStringOrNull(e1), toStringOrNull(e2));
        if (!t.isNull()) {
            put(key, t.toString());
        }
        return this;
    }

    String toStringOrNull(Expression expr) {
        String str = expr != null ? ECQL.toCQL(expr) : null;
        if (str != null) {
            // strip quotes
            if (str.charAt(0) == '\'') {
                str = str.substring(1);
            }
            if (str.charAt(str.length()-1) == '\'') {
                str = str.substring(0, str.length()-1);
            }
        }
        return str;
    }

    Expression nullIf(Expression expr, double value) {
        if (expr instanceof Literal) {
            Double d = expr.evaluate(null, Double.class);
            if (d != null && d.doubleValue() == value) {
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
}
