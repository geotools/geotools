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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.expression.ExpressionAbstract;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.parameter.Parameter;

/**
 * Default implementation of a Function; you may extend this class to implement specific
 * functionality.
 *
 * <p>
 *
 * @author Cory Horner, Refractions Research
 */
public class FunctionImpl extends ExpressionAbstract implements Function {

    /** function name * */
    String name;

    /** function params * */
    List<Expression> params = Collections.emptyList();

    /** Fall back value to use when implementation is not available. */
    Literal fallbackValue;

    /**
     * FunctionName description for FilterCapabilities, may be provided by subclass in constructor,
     * or will be lazily created based on name and number of arguments.
     */
    protected FunctionName functionName;

    /**
     * Gets the name of this function.
     *
     * @return the name of the function.
     */
    public String getName() {
        if (name == null && functionName != null) {
            return functionName.getName();
        }
        return name;
    }

    public synchronized FunctionName getFunctionName() {
        if (functionName == null) {
            functionName = new FunctionNameImpl(name, getParameters().size());
        }
        return functionName;
    }

    /** Sets the name of the function. */
    public void setName(String name) {
        this.name = name;
    }

    /** Returns the function parameters. */
    public List<Expression> getParameters() {
        return new ArrayList<Expression>(params);
    }

    /**
     * Default implementation simply returns the fallbackValue.
     *
     * <p>Please override this method to produce a value based on the provided arguments.
     *
     * @param object Object being evaluated; often a Feature
     * @return value for the provided object
     */
    public Object evaluate(Object object) {
        if (fallbackValue != null) {
            return fallbackValue.evaluate(object);
        }
        throw new UnsupportedOperationException(
                "Function " + name + "(" + this.getClass() + ") not implemented");
    }

    /** Sets the function parameters. */
    @SuppressWarnings("unchecked")
    public void setParameters(List<Expression> params) {
        this.params = params == null ? Collections.EMPTY_LIST : new ArrayList<Expression>(params);
    }

    public void setFallbackValue(Literal fallbackValue) {
        this.fallbackValue = fallbackValue;
    }

    public Literal getFallbackValue() {
        return fallbackValue;
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    /**
     * Creates a String representation of this Function with the function name and the arguments.
     * The String created should be good for most subclasses
     */
    // Copied from FunctionExpressionImpl KS
    public String toString() {
        StringBuilder sb = new StringBuilder();
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

    /**
     * Evaluates a specific argument against the context, checking for required values and proper
     * conversion.
     */
    protected Object getParameterValue(Object object, int argumentIndex) {
        Parameter<?> parameter = getFunctionName().getArguments().get(argumentIndex);
        if (params.size() <= argumentIndex) {
            if (parameter.getMinOccurs() == 0) {
                return null;
            } else {
                throw new IllegalArgumentException(
                        String.format(
                                "No arguments specified for arg " + "%s, minOccurs = %d",
                                parameter.getName().toString(), parameter.getMinOccurs()));
            }
        }

        final Expression expression = params.get(argumentIndex);
        Object value = expression.evaluate(object, parameter.getType());
        if (value == null && expression.evaluate(object) != null) {
            throw new IllegalArgumentException(
                    String.format(
                            "Failure converting value for "
                                    + "argument %s. %s could not be converted to %s",
                            parameter.getName(),
                            expression.toString(),
                            parameter.getType().getName()));
        }
        return value;
    }

    /**
     * Evaluates a specific argument against the context, checking for required values and proper
     * conversion. This version accepts a default value
     */
    protected Object getParameterValue(Object object, int argumentIndex, Object defaultValue) {
        Object value = getParameterValue(object, argumentIndex);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    /**
     * Gathers up and groups the parameters to the function based on the declared parameters.
     *
     * <p>This method calls {@link #validateArguments()} which enforces java style argument
     * conventions for multi valued parameters. Basically enforcing that only teh last argument in a
     * function can be variable.
     */
    protected LinkedHashMap<String, Object> dispatchArguments(Object obj) {
        LinkedHashMap<String, Object> prepped = new LinkedHashMap<String, Object>();

        List<Parameter<?>> args = getFunctionName().getArguments();
        List<Expression> expr = getParameters();

        if (expr.size() < args.size()) {
            Parameter<?> last = args.get(args.size() - 1);

            // check the last argument
            if (args.get(0).getMinOccurs() != 0) {
                throw new IllegalArgumentException(
                        String.format(
                                "No arguments specified for arg " + "%s, minOccurs = %d",
                                last.getName().toString(), last.getMinOccurs()));
            }
        }
        for (int i = 0; i < expr.size(); i++) {
            Parameter<?> arg = args.get(Math.min(i, args.size() - 1));
            String argName = arg.getName().toString();

            Object o = expr.get(i).evaluate(obj, arg.getType());
            if (o == null) {
                if (expr.get(i).evaluate(obj) != null) {
                    // conversion error
                    throw new IllegalArgumentException(
                            String.format(
                                    "Failure converting value for "
                                            + "argument %s. %s could not be converted to %s",
                                    arg.getName(), obj.toString(), arg.getType().getName()));
                }
            }
            if (prepped.containsKey(argName)) {
                if (arg.getMaxOccurs() == 1) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Multiple values specified for "
                                            + "argument %s  but maxOccurs = 1",
                                    argName));
                }

                // if there is already a value for this argument it is a multi argument which
                // means a list
                List l = (List) prepped.get(argName);
                l.add(o);
            } else {
                // check for variable argument, use a list if maxOccurs > 1
                if (arg.getMaxOccurs() < 0 || arg.getMaxOccurs() > 1) {
                    List l = new ArrayList();
                    l.add(o);
                    prepped.put(argName, l);
                } else {
                    prepped.put(argName, o);
                }
            }
        }

        return prepped;
    }

    /** filter factory */
    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    /** regex for parameter specification */
    static Pattern PARAM = Pattern.compile("(\\w+)(?::([\\.\\w]*)(?::(\\d*),(\\d*))?+)?+");

    /**
     * Convenience method for creating a function name from a set of strings describing the return
     * and argument parameters of the function.
     *
     * <p>The value of <tt>ret</tt> and each value of <tt>args</tt> is a string of the following
     * structure:
     *
     * <pre>
     *  name[:type[:min,max]]
     * </pre>
     *
     * Where:
     *
     * <ul>
     *   <li><tt>name</tt> is the name of the parameter
     *   <li><tt>type</tt> is type (class name) of the parameter
     *   <li><tt>min</tt> is the minimum number of occurrences
     *   <li><tt>max</tt> is the maximum number of occurrences
     * </ul>
     *
     * Examples:
     *
     * <ul>
     *   <li><tt>foo</tt>
     *   <li><tt>foo:String</tt>
     *   <li><tt>foo:java.lang.Integer</tt>
     *   <li><tt>foo:Polygon:1,1</tt>
     *   <li><tt>foo:Polygon:1,</tt>
     *   <li><tt>foo:Polygon:,</tt>
     * </ul>
     *
     * The <tt>type</tt> parameter may be specified relative to the following well known packages:
     *
     * <ul>
     *   <li><tt>java.lang</tt>
     *   <li><tt>org.locationtech.jts.geom</tt>
     * </ul>
     *
     * Otherwise it must be specified as a full qualified class name.
     *
     * @param name The name of the function
     * @param ret The parameter specification of the return of the function
     * @param args The argument specifications of the function arguments
     */
    protected static FunctionName functionName(String name, String ret, String... args) {
        List<Parameter<?>> list = new ArrayList<Parameter<?>>();
        for (String arg : args) {
            list.add(toParameter(arg));
        }

        return ff.functionName(name, list, toParameter(ret));
    }

    static Parameter toParameter(String param) throws IllegalArgumentException {
        Matcher m = PARAM.matcher(param);
        if (!m.matches()) {
            throw new IllegalArgumentException("Illegal parameter syntax: " + param);
        }

        String name = m.group(1);
        Class type = null;
        int min = 1;
        int max = 1;

        String grp = m.group(2);
        if ("".equals(grp)) {
            grp = null;
        }
        if (grp != null) {
            try {
                type = Class.forName(grp);
            } catch (ClassNotFoundException e) {
                // try prefixing with java.lang
                try {
                    type = Class.forName("java.lang." + grp);
                } catch (ClassNotFoundException e1) {
                    // try prefixing with jts.geom
                    try {
                        type = Class.forName("org.locationtech.jts.geom." + grp);
                    } catch (ClassNotFoundException e2) {
                        // throw back the original
                        throw (IllegalArgumentException)
                                new IllegalArgumentException("Unknown type: " + grp).initCause(e);
                    }
                }
            }
        }

        // recognize some well known names
        if (type == null) {
            if ("geom".equals(name)) {
                type = Geometry.class;
            }
        }

        if (type == null) {
            type = Object.class;
        }

        grp = m.group(3);
        if (grp != null) {
            min = !"".equals(grp) ? Integer.parseInt(grp) : -1;
        }

        grp = m.group(4);
        if (grp != null) {
            max = !"".equals(grp) ? Integer.parseInt(grp) : -1;
        }

        return new org.geotools.data.Parameter(name, type, min, max);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fallbackValue == null) ? 0 : fallbackValue.hashCode());
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
        FunctionImpl other = (FunctionImpl) obj;
        if (fallbackValue == null) {
            if (other.fallbackValue != null) return false;
        } else if (!fallbackValue.equals(other.fallbackValue)) return false;
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
