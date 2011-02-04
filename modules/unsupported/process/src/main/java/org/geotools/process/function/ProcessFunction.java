/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.function;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.data.Parameter;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.util.Converters;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * A function wrapping a {@link Process} with a single output. All inputs to the function are
 * supposed to evaluate to Map<String, Object> where the key is the name of an argument and the
 * value is the argument value
 * 
 * @author Andrea Aime - GeoSolutions
 */
class ProcessFunction implements Function {

    String name;

    Literal fallbackValue;

    List<Expression> inputExpressions;

    Map<String, Parameter<?>> parameters;

    Process process;
    
    ProcessFunction(String name, List<Expression> inputExpressions,
            Map<String, Parameter<?>> parameters, Process process, Literal fallbackValue) {
        super();
        this.name = name;
        this.inputExpressions = inputExpressions;
        this.parameters = parameters;
        this.process = process;
        this.fallbackValue = fallbackValue;
    }

    public Literal getFallbackValue() {
        return fallbackValue;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getParameters() {
        return inputExpressions;
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public <T> T evaluate(Object object, Class<T> context) {
        Object o = evaluate(object);
        return Converters.convert(o, context);
    }

    public Object evaluate(Object object) {
        // collect the entries
        Map<String, Object> processInputs = new HashMap<String, Object>();
        for (Expression input : inputExpressions) {
            Object result = input.evaluate(object, Map.class);
            if (result != null) {
                Map map = (Map) result;
                if (map.size() > 1) {
                    throw new InvalidParameterException("The parameters to a ProcessFunction "
                            + "must all be maps with a single entry, "
                            + "the key is the process argument name, "
                            + "the value is the argument value");
                } else {
                    // handle the key/value
                    Iterator it = map.entrySet().iterator();
                    Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
                    final String paramName = entry.getKey();
                    final Object paramValue = entry.getValue();

                    // see if we have a parameter with that name
                    Parameter param = parameters.get(paramName);
                    if (param == null) {
                        throw new InvalidParameterException("Parameter " + paramName
                                + " is not among the process parameters: " + parameters.keySet());
                    } else {
                        // if the value is not null, convert to the param target type and add
                        // to the process invocation params
                        if (paramValue != null) {
                            Object converted;
                            if(param.maxOccurs > 1) {
                                // converter will work if the have to convert the array type, but not if
                                // they have to deal with two conversions, from single to multi, from type to type
                                if(!(paramValue instanceof Collection) && !(paramValue.getClass().isArray())) {
                                    List<Object> collection = Collections.singletonList(paramValue);
                                    converted = Converters.convert(collection, Array.newInstance(param.type, 0).getClass());
                                } else {
                                    converted = Converters.convert(paramValue, Array.newInstance(param.type, 0).getClass()); 
                                }
                            } else {
                                converted = Converters.convert(paramValue, param.type);
                            }
                            if (converted == null) {
                                if(param.maxOccurs > 1 && Collection.class.isAssignableFrom(paramValue.getClass())) {
                                    final Collection collection = (Collection) paramValue;
                                    Collection convertedCollection = new ArrayList(collection.size());
                                    for (Object original : collection) {
                                        Object convertedItem = Converters.convert(original, param.type);
                                        if(original != null && convertedItem == null) {
                                            throw new InvalidParameterException("Could not convert the value "
                                                    + original + " into the expected type " + param.type
                                                    + " for parameter " + paramName);
                                        }
                                        convertedCollection.add(convertedItem);
                                    }
                                    converted = convertedCollection.toArray((Object[]) Array.newInstance(param.type, convertedCollection.size()));
                                } else {
                                    throw new InvalidParameterException("Could not convert the value "
                                            + paramValue + " into the expected type " + param.type
                                            + " for parameter " + paramName);
                                }
                            }
                            processInputs.put(paramName, converted);
                        }
                    }
                }
            }
        }

        // execute the process
        try {
            ExceptionProgressListener listener = new ExceptionProgressListener();
            Map<String, Object> results = process.execute(processInputs, listener);
            
            // some processes have the bad habit of not throwing exceptions, but to
            // report them to the listener
            if(listener.getExceptions().size() > 0) {
                // uh oh, an exception occurred during processing
                Throwable t = listener.getExceptions().get(0);
                throw new RuntimeException("Failed to evaluate process function, error is: " 
                        + t.getMessage(), t);
            }
            
            if (results.size() > 1) {
                throw new RuntimeException("The process returned more than one value, "
                        + "processes wrapped by functions should return just one");
            }

            // return the sole value returned
            return results.values().iterator().next();
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to evaluate the process function, error is: "
                    + e.getMessage(), e);
        }
    }

}
