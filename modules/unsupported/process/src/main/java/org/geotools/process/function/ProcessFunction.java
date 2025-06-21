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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.geotools.api.data.Parameter;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.Processors;
import org.geotools.util.Converters;

/**
 * A wrapper allowing a {@link Process} with a single output to be called as a {@link Function}. Since Function
 * parameters are positional and Process parameters are named, the following strategy is used to allow specifying named
 * Process parameters as function inputs. All inputs to the function must evaluate to Map&lt;String, Object&gt;, with a
 * single entry where the key is the name of a process parameter and the value is the argument value
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ProcessFunction implements Function {

    String name;

    Literal fallbackValue;

    List<Expression> inputExpressions;

    Map<String, Parameter<?>> parameters;

    Process process;

    Name processName;

    FunctionNameImpl functionName;

    public ProcessFunction(
            Name processName,
            List<Expression> inputExpressions,
            Map<String, Parameter<?>> parameters,
            Process process,
            Literal fallbackValue) {
        super();
        String nsuri = processName.getNamespaceURI();
        this.name = nsuri == null ? processName.getLocalPart() : nsuri + ":" + processName.getLocalPart();
        this.processName = processName;
        this.inputExpressions = inputExpressions;
        this.parameters = parameters;
        this.process = process;
        this.fallbackValue = fallbackValue;

        // build the function name
        List<org.geotools.api.parameter.Parameter<?>> inputParams = new ArrayList<>();
        Map<String, Parameter<?>> parameterInfo = Processors.getParameterInfo(processName);
        if (parameterInfo instanceof LinkedHashMap) {
            // predictable order so we can assume parameter order
            for (Parameter<?> param : parameterInfo.values()) {
                // we do not specify the parameter type to avoid validation issues with the
                // different positional/named conventions
                inputParams.add(param);
            }
        } else {
            Set<String> paramNames = parameterInfo.keySet();
            for (String pn : paramNames) {
                // we do not specify the parameter type to avoid validation issues with the
                // different positional/named conventions
                org.geotools.api.parameter.Parameter param = FunctionNameImpl.parameter(pn, Object.class, 0, 1);
                inputParams.add(param);
            }
        }
        Map<String, Parameter<?>> resultParams = Processors.getResultInfo(processName, null);
        org.geotools.api.parameter.Parameter result =
                resultParams.values().iterator().next();
        functionName = new FunctionNameImpl(name, result, inputParams);
    }

    @Override
    public Literal getFallbackValue() {
        return fallbackValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public Name getProcessName() {
        return processName;
    }

    @Override
    public FunctionName getFunctionName() {
        return functionName;
    }

    @Override
    public List<Expression> getParameters() {
        return inputExpressions;
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        Object o = evaluate(object);
        return Converters.convert(o, context);
    }

    @Override
    public Object evaluate(Object object) {
        Map<String, Object> processInputs = evaluateInputs(object);

        // execute the process
        try {
            ExceptionProgressListener listener = new ExceptionProgressListener();
            Map<String, Object> results = process.execute(processInputs, listener);

            // some processes have the bad habit of not throwing exceptions, but to
            // report them to the listener
            if (!listener.getExceptions().isEmpty()) {
                // uh oh, an exception occurred during processing
                Throwable t = listener.getExceptions().get(0);
                throw new RuntimeException("Failed to evaluate process function, error is: " + t.getMessage(), t);
            }

            return getResult(results, processInputs);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to evaluate the process function, error is: " + e.getMessage(), e);
        }
    }

    /**
     * Evaluates the process input expressions. The object provides the context for evaluating the input expressions,
     * and may be null if no context is available (for instance, when being called to evaluation the inputs for the
     * {@link RenderingProcessFunction} inversion methods).
     *
     * @param object the object to evaluate the input expressions against.
     * @return the map of inputs
     */
    protected Map<String, Object> evaluateInputs(Object object) {
        // collect the entries
        Map<String, Object> processInputs = new HashMap<>();
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
                    @SuppressWarnings("unchecked")
                    Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
                    final String paramName = entry.getKey();
                    final Object paramValue = entry.getValue();

                    // see if we have a parameter with that name
                    Parameter<?> param = parameters.get(paramName);
                    if (param == null) {
                        throw new InvalidParameterException("Parameter "
                                + paramName
                                + " is not among the process parameters: "
                                + parameters.keySet());
                    } else {
                        // if the value is not null, convert to the param target type and add
                        // to the process invocation params
                        if (paramValue != null) {
                            Object converted = convertParameter(paramName, paramValue, param);
                            processInputs.put(paramName, converted);
                        }
                    }
                }
            }
        }
        return processInputs;
    }

    private Object convertParameter(String paramName, Object paramValue, Parameter<?> param) {
        Object converted;
        if (param.maxOccurs > 1) {
            // converter will work if the have to convert the array type, but
            // not if
            // they have to deal with two conversions, from single to multi,
            // from type to type
            if (!(paramValue instanceof Collection) && !paramValue.getClass().isArray()) {
                List<Object> collection = Collections.singletonList(paramValue);
                converted = Converters.convert(
                        collection, Array.newInstance(param.type, 0).getClass());
            } else {
                converted = Converters.convert(
                        paramValue, Array.newInstance(param.type, 0).getClass());
            }
        } else {
            converted = Converters.convert(paramValue, param.type);
        }
        if (converted == null) {
            if (param.maxOccurs > 1 && Collection.class.isAssignableFrom(paramValue.getClass())) {
                final Collection collection = (Collection) paramValue;
                Collection<Object> convertedCollection = new ArrayList<>(collection.size());
                for (Object original : collection) {
                    Object convertedItem = Converters.convert(original, param.type);
                    if (original != null && convertedItem == null) {
                        throw new InvalidParameterException("Could not convert the value "
                                + original
                                + " into the expected type "
                                + param.type
                                + " for parameter "
                                + paramName);
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
                        + paramValue
                        + " into the expected type "
                        + param.type
                        + " for parameter "
                        + paramName);
            }
        }
        return converted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (fallbackValue == null ? 0 : fallbackValue.hashCode());
        result = prime * result + (functionName == null ? 0 : functionName.hashCode());
        result = prime * result + (inputExpressions == null ? 0 : inputExpressions.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (parameters == null ? 0 : parameters.hashCode());
        result = prime * result + (processName == null ? 0 : processName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ProcessFunction other = (ProcessFunction) obj;
        if (fallbackValue == null) {
            if (other.fallbackValue != null) return false;
        } else if (!fallbackValue.equals(other.fallbackValue)) return false;
        if (functionName == null) {
            if (other.functionName != null) return false;
        } else if (!functionName.equals(other.functionName)) return false;
        if (inputExpressions == null) {
            if (other.inputExpressions != null) return false;
        } else if (!inputExpressions.equals(other.inputExpressions)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (parameters == null) {
            if (other.parameters != null) return false;
        } else if (!parameters.equals(other.parameters)) return false;
        if (processName == null) {
            if (other.processName != null) return false;
        } else if (!processName.equals(other.processName)) return false;
        return true;
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
        if (resultInfo.size() == 1) {
            return resultInfo.keySet().iterator().next();
        } else {
            for (Parameter<?> param : resultInfo.values()) {
                if (param.isRequired()) {
                    return param.getName();
                }
            }
        }
        return null;
    }
}
