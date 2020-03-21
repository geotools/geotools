/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.feature.NameImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.feature.type.Name;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Literal;

/**
 * Abstract class for a function expression implementation
 *
 * <p>By default this implementation returns the provided {@link #fallback} value. To implement a
 * function please override the {@link #evaluate(Object)} method.
 *
 * @author James Macgill, PSU
 */
public abstract class FunctionExpressionImpl extends org.geotools.filter.DefaultExpression
        implements FunctionExpression {

    /** function name * */
    protected String name;

    /** function params * */
    protected List<org.opengis.filter.expression.Expression> params;

    protected Literal fallback;

    /** FunctionName provided by subclass; or lazely created */
    protected FunctionName functionName;

    /**
     * Preferred constructor to ensure name and functionName match.
     *
     * <p>Recommended use:
     *
     * <pre>
     * import static org.geotools.filter.capability.FunctionNameImpl.*;
     * public class AreaFunction extends FunctionExpressionImpl {
     *
     *   public static FunctionName NAME = new FunctionNameImpl("Area",
     *        parameter("area",Double.class),
     *        parameter("geometry",Geometry.class));
     *
     * public AreaFunction() {
     *     super(NAME);
     * }
     * </pre>
     *
     * @param functionName FunctionName describing subclass
     */
    protected FunctionExpressionImpl(FunctionName functionName) {
        this(functionName.getName(), null);
        this.functionName = functionName;
    }

    protected FunctionExpressionImpl(String name) {
        this(new NameImpl(name));
    }

    protected FunctionExpressionImpl(Name name) {
        this(name, null);
    }
    /** Creates a new instance of FunctionExpression */
    protected FunctionExpressionImpl(String name, Literal fallback) {
        this(new NameImpl(name), fallback);
    }
    /** Creates a new instance of FunctionExpression */
    protected FunctionExpressionImpl(Name name, Literal fallback) {
        this.functionName = new FunctionNameImpl(name, (Class<?>) null);
        this.name = name.getLocalPart();
        this.fallback = fallback;
        params = new ArrayList<org.opengis.filter.expression.Expression>();
    }

    /**
     * Gets the name of this function.
     *
     * @return the name of the function.
     */
    public String getName() {
        return name;
    }

    public synchronized FunctionName getFunctionName() {
        if (functionName == null) {
            functionName = new FunctionNameImpl(getName(), getParameters().size());
        }
        return functionName;
    }

    public Literal getFallbackValue() {
        return fallback;
    }

    public void setFallbackValue(Literal fallback) {
        this.fallback = fallback;
    }
    /** Returns the function parameters. */
    public List<org.opengis.filter.expression.Expression> getParameters() {
        return params;
    }

    /** Sets the function parameters. */
    public void setParameters(List<Expression> params) {
        if (params == null) {
            throw new NullPointerException("Function parameters required");
        }
        final int argCount = functionName.getArgumentCount();
        final int paramsSize = params.size();
        if (argCount > 0 && argCount != paramsSize) {
            throw new IllegalArgumentException(
                    "Function " + name + " expected " + argCount + " arguments, got " + paramsSize);
        }
        this.params = new ArrayList<Expression>(params);
    }

    /** @see org.opengis.filter.expression.Expression#accept(ExpressionVisitor, Object) */
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    /** Returns the implementation hints. The default implementation returns an empty map. */
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    /**
     * Convenience method for creating a function name.
     *
     * @see FunctionImpl#functionName(String, String, String...)
     */
    protected static FunctionName functionName(String name, String ret, String... args) {
        return FunctionImpl.functionName(name, ret, args);
    }

    /**
     * Creates a String representation of this Function with the function name and the arguments.
     * The String created should be good for most subclasses
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append("(");
        List<org.opengis.filter.expression.Expression> params = getParameters();
        if (params != null) {
            org.opengis.filter.expression.Expression exp;
            for (Iterator<org.opengis.filter.expression.Expression> it = params.iterator();
                    it.hasNext(); ) {
                exp = it.next();
                sb.append("[");
                sb.append(exp);
                sb.append("]");
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /** Utility method for subclasses to ask for an argument at a given index */
    protected org.opengis.filter.expression.Expression getExpression(int index) {
        org.opengis.filter.expression.Expression exp;
        exp = getParameters().get(index);
        return exp;
    }

    @Override
    public Object evaluate(Object object) {
        if (fallback != null) {
            return fallback.evaluate(object);
        }
        throw new UnsupportedOperationException("Function " + name + " not implemented");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fallback == null) ? 0 : fallback.hashCode());
        result = prime * result + ((functionName == null) ? 0 : functionName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((params == null) ? 0 : params.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FunctionExpressionImpl other = (FunctionExpressionImpl) obj;
        if (fallback == null) {
            if (other.fallback != null) return false;
        } else if (!fallback.equals(other.fallback)) return false;
        if (functionName == null) {
            if (other.functionName != null) return false;
        } else if (!functionName.equals(other.functionName)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (params == null) {
            if (other.params != null) return false;
        } else if (!params.equals(other.params)) return false;
        return true;
    }
}
