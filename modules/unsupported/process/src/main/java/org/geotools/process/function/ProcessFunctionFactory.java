/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.api.data.Parameter;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.process.RenderingProcess;

/**
 * A bridge between the process world and the filter function world: any process returning a single value can be seen as
 * a filter function
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ProcessFunctionFactory implements FunctionFactory {

    /** Parameter used to indicate a certain output is the primary one for the process */
    public static final String PRIMARY_OUTPUT = "PRIMARY";

    /** Compares process factories by their title */
    static final Comparator<ProcessFactory> FACTORY_COMPARATOR = (pf1, pf2) -> {
        if (pf1.getTitle() == null) {
            if (pf2.getTitle() == null) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (pf2.getTitle() == null) {
                return 1;
            } else {
                return pf1.getTitle().compareTo(pf2.getTitle());
            }
        }
    };

    /** Maps from function to process name */
    HashMap<Name, FunctionName> processToFunction;

    /** The cache list of functions wrapping processes */
    private ArrayList<FunctionName> functionNames;

    @Override
    public Function function(String name, List<Expression> args, Literal fallback) {
        return function(new NameImpl(name), args, fallback);
    }

    @Override
    public Function function(Name processName, List<Expression> args, Literal fallback) {
        // if the param function just return it
        if (processName.equals(new NameImpl(ParameterFunction.NAME.getName()))) {
            return new ParameterFunction(fallback, args);
        }

        // lookup the process
        if (functionNames == null) {
            init();
        }

        if (!processToFunction.containsKey(processName)) {
            // no such function
            return null;
        } else {
            // wrap the process
            org.geotools.process.Process process = Processors.createProcess(processName);
            Map<String, Parameter<?>> parameters = Processors.getParameterInfo(processName);
            if (process instanceof RenderingProcess) {
                return new RenderingProcessFunction(
                        processName, args, parameters, (RenderingProcess) process, fallback);
            } else {
                return new ProcessFunction(processName, args, parameters, process, fallback);
            }
        }
    }

    @Override
    public List<FunctionName> getFunctionNames() {
        if (functionNames == null) {
            init();
        }

        return functionNames;
    }

    private synchronized void init() {
        if (functionNames == null) {
            // collect and sort the factories to have a reproducable list of function names
            List<ProcessFactory> factories = new ArrayList<>(Processors.getProcessFactories());
            Collections.sort(factories, FACTORY_COMPARATOR);

            // collect name and params of all processes resulting in a single output
            processToFunction = new HashMap<>();
            functionNames = new ArrayList<>();
            for (ProcessFactory factory : factories) {
                if (!factory.isAvailable()) {
                    continue;
                }
                for (Name processName : factory.getNames()) {
                    try {
                        Map<String, Parameter<?>> resultInfo = factory.getResultInfo(processName, null);
                        Parameter<?> result = getPrimary(resultInfo);
                        // check there is a single output
                        if (result != null) {
                            Map<String, Parameter<?>> parameterInfo = factory.getParameterInfo(processName);
                            List<String> argumentNames = new ArrayList<>(parameterInfo.keySet());
                            List<org.geotools.api.parameter.Parameter<?>> args = new ArrayList<>(argumentNames.size());
                            for (String argumentName : argumentNames) {
                                args.add(parameterInfo.get(argumentName));
                            }
                            FunctionName functionName = new FunctionNameImpl(processName, result, args);
                            functionNames.add(functionName);
                            processToFunction.put(processName, functionName);
                        }
                    } catch (Throwable unavailable) {
                        Logger log = Logger.getLogger(factory.getClass().getName());
                        log.finer("Process " + processName + " unavailable:" + unavailable);
                        continue;
                    }
                }
            }
            // add the parameter function
            functionNames.add(ParameterFunction.NAME);
        }
    }

    private Parameter<?> getPrimary(Map<String, Parameter<?>> resultInfo) {
        if (resultInfo == null) {
            return null;
        }
        if (resultInfo.size() == 1) {
            return resultInfo.values().iterator().next();
        } else {
            for (Parameter<?> param : resultInfo.values()) {
                if (param.isRequired()) {
                    return param;
                }
            }
        }
        return null;
    }

    /** Clears the caches forcing the system to do another lookup */
    public void clear() {
        functionNames = null;
        processToFunction = null;
    }
}
