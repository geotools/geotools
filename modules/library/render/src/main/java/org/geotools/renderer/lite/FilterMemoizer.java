/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.InternalFunction;
import org.geotools.api.filter.expression.VolatileFunction;
import org.geotools.filter.visitor.AbstractFilterVisitor;
import org.geotools.filter.visitor.DefaultExpressionVisitor;
import org.geotools.util.logging.Logging;

/**
 * A filter wrapper that stores the evaluation of results against the last input, if the next call is performed against
 * the same input, then it returns the cached result, if not the same, a new evaluation is performed.
 *
 * <p>Implementation note: has been implemented using proxies after checking they do not hold a significant overhead,
 * the cost of filter/expression evaluation in GeoTools is much more expensive in comparison
 *
 * @param <T>
 */
class FilterMemoizer {
    static final Logger LOGGER = Logging.getLogger(FilterMemoizer.class);

    static final Object NULL_PLACEHOLDER = new Object();

    /**
     * Checks that a expression is not using unstable/volatile functions that might give back different results in the
     * same invocation thread
     */
    static class MemoizableExpressionChecker extends DefaultExpressionVisitor {

        boolean memoizable = true;

        @Override
        public Object visit(Function expr, Object extraData) {
            if (expr instanceof VolatileFunction || expr instanceof InternalFunction) {
                memoizable = false;
            }
            return expr;
        }
    }

    /**
     * Wraps the provided delegate into a memoizing filter, if the filter in question is not using volatile functions
     * and there is a suitable wrapper implementation, returns the original filter otherwise.
     *
     * @param <T>
     */
    public static <T extends Filter> T memoize(T delegate) {
        // can it be memoized?
        MemoizableExpressionChecker checker = new MemoizableExpressionChecker();
        delegate.accept(new AbstractFilterVisitor(checker), null);
        if (!checker.memoizable) {
            return delegate;
        }

        // cache results using a memoizing proxy
        @SuppressWarnings("unchecked")
        T result = (T) Proxy.newProxyInstance(
                FilterMemoizer.class.getClassLoader(),
                delegate.getClass().getInterfaces(),
                new MemoizingHandler<>(delegate));
        return result;
    }

    private static class MemoizingHandler<T extends Filter> implements InvocationHandler {
        private final T delegate;
        Object lastFeature;
        boolean lastResult;
        Method lastMethod;

        public MemoizingHandler(T delegate) {
            this.delegate = delegate;
            lastFeature = NULL_PLACEHOLDER;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (lastMethod == method || "evaluate".equals(method.getName()) && method.getParameterTypes().length == 1) {
                lastMethod = method;
                if (args[0] != lastFeature) {
                    lastFeature = args[0];
                    lastResult = delegate.evaluate(args[0]);
                }
                return lastResult;
            }

            return method.invoke(delegate, args);
        }
    }
}
