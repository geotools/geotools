/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.function;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.filter.FunctionExpressionImpl;
import org.opengis.filter.expression.Literal;

/**
 * Provides a lookup table of named variables allowing externally defined
 * values to be access within a SLD document.
 * <p>
 * Example: in the application, prior to rendering...
 * <pre><code>
 * EnvFunction.setValue("foo", 42);
 * </code></pre>
 * Then, in the SLD document we can refer to this variable using
 * the "env" function
 * <pre><code>
 * ...
 * &lt;FeatureTypeStyle>
 *   &lt;Rule>
 *     &lt;Filter>
 *       &lt;PropertyIsEqualTo>
 *         &lt;PropertyName>FooValue&lt;/PropertyName>
 *         &lt;Function name="env">
 *           &lt;literal>foo&lt;/literal>
 *         &lt;/Function>
 *       &lt;/PropertyIsEqualTo>
 *     &lt;/Filter>
 *     ...
 * </code></pre>
 * The function provides a lookup table that is local to the active thread
 * so that a given variable can hold different values in different threads.
 * There is also a global lookup table, accessible from all threads.
 * When the function is given a variable to look up it first searches the
 * thread's local table and then, if the variable was not found, the global
 * table. All lookups are case-insensitive.
 * <p>
 * Setting a fallback value is not supported in accordance with SLD 1.1 specification.
 * However, you can provide a default value when calling the function as in
 * these examples:
 * <pre><code>
 *   &lt;!-- Here, if variable foo is not set the function returns null -->
 *   &lt;Function name="env">
 *     &lt;Literal>foo&lt;/Literal>
 *   &lt;/Function>
 *
 *   &lt;!-- Here, a second argument is provided. If foo is not set the -->
 *   &lt;!-- function will return 0.                                    -->
 *   &lt;Function name="env">
 *     &lt;Literal>foo&lt;/Literal>
 *     &lt;Literal>0&lt;/Literal>
 *   &lt;/Function>
 * </code></pre>
 * The same approach can be used programmatically...
 * <pre><code>
 * // set argument to set a default return value of 0
 * FilterFactory ff = ...
 * ff.function("env", ff.literal("foo"), ff.literal(0));
 * </code></pre>
 *
 * @author Andrea Aime
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id $
 */
public class EnvFunction extends FunctionExpressionImpl {

    /**
     * Provides a lookup table that is local to each thread.
     */
    private static class LocalLookup extends ThreadLocal<Map<String, Object>> {

        @Override
        protected Map<String, Object> initialValue() {
            return new LinkedHashMap<String, Object>();
        }

        /**
         * Get the table of lookup values.
         * Defined to make code using this class more obvious.
         *
         * @return the table of lookup values
         */
        public Map<String, Object> getTable() {
            return super.get();
        }
    };

    private static final LocalLookup localLookup = new LocalLookup();

    /**
     * A global lookup table
     */
    private static ConcurrentMap<String, Object> globalLookup = new ConcurrentHashMap<String, Object>();


    /**
     * Create a new instance of this function.
     */
    public EnvFunction() {
        super("env");
    }

    /**
     * Set the local (to this thread) table of lookup values, deleting any previously
     * set table. The input {@code Map} is copied.
     *
     * @param values the lookup table; if {@code null} the existing lookup
     *        table will be cleared.
     */
    public static void setLocalValues(Map<String, Object> values) {
        Map<String, Object> table = localLookup.getTable();
        table.clear();

        if (values != null) {
            for (Entry<String, Object> e : values.entrySet()) {
                table.put(e.getKey().toUpperCase(), e.getValue());
            }
        }
    }

    /**
     * Clear all values from the local (to this thread) lookup table.
     */
    public static void clearLocalValues() {
        localLookup.getTable().clear();
        localLookup.remove();
    }

    /**
     * Set the table of global lookup values that is accessible from any
     * thread, replacing the previously set table. The input {@code Map} is copied.
     *
     * @param values the lookup table; if {@code null} the existing lookup
     *        table will be cleared.
     */
    public static void setGlobalValues(Map<String, Object> values) {
        globalLookup.clear();

        if (values != null) {
            for (Entry<String, Object> e : values.entrySet()) {
                globalLookup.put(e.getKey().toUpperCase(), e.getValue());
            }
        }
    }

    /**
     * Clear all values from the global (accessible from any thread) lookup table.
     */
    public static void clearGlobalValues() {
        globalLookup.clear();
    }

    /**
     * Add a named value to the local (to this thread) lookup table. If the name is
     * already present in the table it will be assigned the new value.
     *
     * @param name the name
     * @param value the value
     */
    public static void setLocalValue(String name, Object value) {
        localLookup.getTable().put(name.toUpperCase(), value);
    }

    /**
     * Add a named value to the global (accessible from any thread) lookup table.
     * If the name is already present in the table it will be assigned the new value.
     *
     * @param name the name
     * @param value the value
     */
    public static void setGlobalValue(String name, Object value) {
        globalLookup.put(name.toUpperCase(), value);
    }

    /**
     * {@inheritDoc}
     * @return Always returns 1
     */
    @Override
    public int getArgCount() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * The variable name to search for is provided as the single argument to
     * this function. The active thread's local lookup table is searched first.
     * If the name is not found there the global table is searched.
     *
     * @return the variable value or {@code null} if the variable was not found
     */
    @Override
    public Object evaluate(Object feature) {
        String varName = getExpression(0).evaluate(feature, String.class);
        Object value = localLookup.getTable().get(varName.toUpperCase());

        // No result - check the global lookup table
        if (value == null) {
            value = globalLookup.get(varName.toUpperCase());
        }

        // Still no result - check if there is a default
        if (value == null) {
            final int paramSize = getParameters().size();
            if (paramSize > getArgCount()) {
                value = getExpression(paramSize - 1).evaluate(feature);
            }
        }

        return value;
    }

    /**
     * {@inheritDoc}
     * This method is overriden to allow for either a single parameter
     * (variable name) or two parameters (variable name plus default value).
     */
    @Override
    public void setParameters(List params) {
        if(params == null){
            throw new NullPointerException("params can't be null");
        }

        final int argCount = getArgCount();
        final int paramsSize = params.size();
        if(paramsSize < argCount || paramsSize > argCount + 1){
            throw new IllegalArgumentException(
                    String.format("Function %s expected %d or %d arguments but got %d",
                                  name, argCount, argCount+1, paramsSize));
        }
    	this.params = new ArrayList(params);
    }

    /**
     * {@inheritDoc}
     * This method is overriden to ignore the fallback value and log
     * a warning message. If you want to set a default value it can be provided
     * as a second argument when calling the function. See the class description
     * for details.
     */
    @Override
    public void setFallbackValue(Literal fallback) {
        Logger logger = Logger.getLogger(EnvFunction.class.getName());
        logger.log(Level.WARNING,
                "The setFallbackValue is not supported by this function." +
                "Use a second argument when calling the function to provide " +
                "a default value.");
    }

}
