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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.process.RenderingProcess;
import org.opengis.feature.type.Name;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * A bridge between the process world and the filter function world: any process returning a single
 * value can be seen as a filter function
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/process/src/main/java/org/geotools/process/function/ProcessFunctionFactory.java $
 */
public class ProcessFunctionFactory implements FunctionFactory {

    /**
     * Compares process factories by their title
     */
    static final Comparator<ProcessFactory> FACTORY_COMPARATOR = new Comparator<ProcessFactory>() {

        public int compare(ProcessFactory pf1, ProcessFactory pf2) {
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
        }
    };
    
    /**
     * Maps from function to process name
     */
    HashMap<String, Name> functionToProcess;
    
    /**
     * The cache list of functions wrapping processes
     */
    private ArrayList<FunctionName> functionNames;

    public Function function(String name, List<Expression> args, Literal fallback) {
        // if the param function just return it
        if(name.equals(ParameterFunction.NAME)) {
            return new ParameterFunction(fallback, args);
        }
        
        // lookup the process
        if(functionToProcess == null) {
            init();
        }
        Name processName = functionToProcess.get(name);
        if(processName == null) {
            // no such function
            return null; 
        } else {
            // wrap the process            
            org.geotools.process.Process process = Processors.createProcess(processName);
            Map<String, Parameter<?>> parameters = Processors.getParameterInfo(processName);
            if (process instanceof RenderingProcess){
                return new RenderingProcessFunction(name, args, parameters, (RenderingProcess) process, fallback);
            } else {
                return new ProcessFunction(name, args, parameters, process, fallback);
            }
        }
    }

    public List<FunctionName> getFunctionNames() {
        if(functionNames == null) {
            init();
        }
        
        return functionNames;
    }
    
    private synchronized void init() {
        if(functionNames == null || functionToProcess == null) {
            // collect and sort the factories to have a reproducable list of function names
            List<ProcessFactory> factories = new ArrayList<ProcessFactory>(Processors
                    .getProcessFactories());
            Collections.sort(factories, FACTORY_COMPARATOR);
            
            // collect name and params of all processes resulting in a single output
            functionToProcess = new HashMap<String, Name>();
            functionNames = new ArrayList<FunctionName>();
            for (ProcessFactory factory : factories) {
                for (Name processName : factory.getNames()) {
                    String functionName = processName.getURI();
                    Map<String, Parameter<?>> resultInfo = factory.getResultInfo(processName, null);
                    
                    // check there is a single output
                    if(resultInfo.size() == 1) {
                        Map<String, Parameter<?>> parameterInfo = factory.getParameterInfo(processName);
                        List<String> argumentNames = new ArrayList<String>(parameterInfo.keySet());
                        functionNames.add(new FunctionNameImpl(functionName, argumentNames));
                        functionToProcess.put(functionName, processName);
                    }
                }
            }
            
            // add the parameter function
            functionNames.add(new FunctionNameImpl(ParameterFunction.NAME, -1));
        }
    }
    
    /**
     * Clears the caches forcing the system to do another lookup
     */
    public void clear() {
        functionNames = null;
        functionToProcess = null;
    }

}
