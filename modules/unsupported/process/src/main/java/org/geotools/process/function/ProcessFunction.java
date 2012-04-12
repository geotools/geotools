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
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.Processors;
import org.geotools.util.Converters;
import org.opengis.feature.type.Name;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * A wrapper allowing a {@link Process} with a single output to be called as a {@link Function}.
 * Since Function parameters are positional and Process parameters are named,
 * the following strategy is used to allow specifying named Process parameters
 * as function inputs. 
 * All inputs to the function must evaluate to Map<String, Object>,
 * with a single entry where the key is the name of a process parameter and the
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

    Name processName;
    
    ProcessFunction(String name, Name processName, List<Expression> inputExpressions,
            Map<String, Parameter<?>> parameters, Process process, Literal fallbackValue) {
        super();
        this.name = name;
        this.processName = processName;
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
    public FunctionName getFunctionName() {
        return new FunctionNameImpl( name, inputExpressions.size() );
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
        Map<String, Object> processInputs = evaluateInputs(object);

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
            
            return getResult(results, processInputs);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to evaluate the process function, error is: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Evaluates the process input expressions.
     * The object provides the context for evaluating the input expressions,
     * and may be null if no context is available
     * (for instance, when being called to evaluation the inputs
     * for the {@link RenderingProcessFunction} inversion methods).
     * 
     * @param object the object to evaluate the input expressions against.
     * @return the map of inputs
     */
	protected Map<String, Object> evaluateInputs(Object object) {
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
                                    Object array = Array.newInstance(param.type, convertedCollection.size());
                                    int i = 0;
                                    for (Object item : convertedCollection) {
                                        Array.set(array, i, item);
                                        i++;
                                    }
                                    converted = array;
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
		return processInputs;
	}

    private Object getResult(Map<String, Object> results, Map<String, Object> processInputs) {
        if (results.size() == 1) {
            return results.values().iterator().next();
        }

        // return the sole value returned
        Map<String, Parameter<?>> resultInfo = Processors.getResultInfo(processName, processInputs);
        String primary = getPrimary(resultInfo);
        return results.get(primary);
    }
    
    private String getPrimary(Map<String, Parameter<?>> resultInfo) {
        if(resultInfo.size() == 1) {
            return resultInfo.get(0).getName();
        } else {
            for (Parameter<?> param : resultInfo.values()) {
                if(param.isRequired()) {
                    return param.getName();
                }
            }
        }
        return null;
    }
    
    

}
