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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Isolate function lookup code from Factory implementation(s).
 * <p>
 * This is done to look for two things:
 * <ul>
 * <li>org.geotools.filter.Function
 * <li>org.opengis.filter.expression.Function
 * </ul>
 * This is done as a proper utility class that accepts Hints.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class FunctionFinder {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");
	
    private volatile Map<String,FunctionFactory> functionFactoryCache;
    
    public FunctionFinder(Hints hints) {
        // currently hints are not used, need help :-P
    }

    /**
     * Return a List of FunctionName's describing the functions currently available.
     * @return List describing available functions
     */
    public List<FunctionName> getAllFunctionDescriptions(){
        Set<FunctionFactory> functionFactories = CommonFactoryFinder.getFunctionFactories(null);
        List<FunctionName> allFunctionDescriptions = new ArrayList<FunctionName>();
        
        for (FunctionFactory factory : functionFactories) {
            List<FunctionName> functionNames = factory.getFunctionNames();
            allFunctionDescriptions.addAll( functionNames );
        }
        Collections.sort( allFunctionDescriptions, new Comparator<FunctionName>() {
            public int compare(FunctionName o1, FunctionName o2) {
                if( o1 == null && o2 == null ) return 0;
                if( o1 == null && o2 != null ) return 1;
                if( o1 != null && o2 == null ) return -1;
                
                return o1.getName().compareTo( o2.getName() );
            }
        } );
        return Collections.unmodifiableList(allFunctionDescriptions);
    }
    /**
     * Lookup the FunctionName description.
     * @param name Function name; this will need to be an exact match
     * @return FunctioName description, or null if function is not available
     */
    FunctionName findFunctionDescription(String name ){
        Set<FunctionFactory> functionFactories = CommonFactoryFinder.getFunctionFactories(null);
        for (FunctionFactory factory : functionFactories) {
            List<FunctionName> functionNames = factory.getFunctionNames();
            for( FunctionName description : functionNames ){
                if( description.getName().equals( name )){
                    return description;
                }
            }
        }
        return null; // not found
    }
    public Function findFunction(String name) {
        return findFunction(name, null);
    }
    
    /**
     * Look up a function for the provided name.
     * 
     * @param name Function name; this will need to be an exact match
     * @param parameters Set of parameters required
     * @return Generated function
     * @throws a RuntimeException if an implementation for name could not be found
     */
    public Function findFunction(String name, List<org.opengis.filter.expression.Expression> parameters){
    	return findFunction(name, parameters, null );
    }
    
    /**
     * Look up a function for the provided name, may return a FallbackFunction if
     * an implementation could not be found.
     * <p>
     * You can create a function to represent an SQL function or a function hosted on
     * an external service; the fallback value will be used if you evulate 
     * by a Java implementation on the classpath.
     * @param name Function name; this will need to be an exact match
     * @param parameters Set of Expressions to use as function parameters
     * @param fallbackValue Literal to use if an implementation could not be found
     * @return Function for the provided name, may be a FallbackFunction if an implementation could not be found
     */
    public Function findFunction(String name, List<org.opengis.filter.expression.Expression>  parameters, Literal fallback) {
        //try name as is
        Function f = findFunctionInternal(name, parameters, fallback);
        if (f == null) {
            //try by trimming "Function" off of name
            if (name.endsWith("Function")) {
                name = name.substring(0, name.length()-"Function".length());
                f = findFunctionInternal(name, parameters, fallback);
            }
        }
        if( f == null && fallback != null ){
            return new FallbackFunction( name, parameters, fallback );
        }
        
        
        if (f != null) {
            return f;
        }
        
        throw new RuntimeException("Unable to find function " + name);

    }

    Function findFunctionInternal(String name, List parameters, Literal fallback) {
        if (functionFactoryCache == null) {
            synchronized (this) {
                if (functionFactoryCache == null) {
                    functionFactoryCache = lookupFunctions();
                }
            }
        }
        
        if (functionFactoryCache.containsKey(name)) {
            FunctionFactory functionFactory = functionFactoryCache.get(name);
            return functionFactory.function(name, parameters, fallback);
        }
        
        //do a lookup from all factories, this is because of the name tricks the default
        // factory does
        Function f = null;
        for (FunctionFactory ff : CommonFactoryFinder.getFunctionFactories(null)) {
            f = ff.function(name, parameters, fallback);
            if (f != null) return f;
        }

        return null;
    }
    
    private HashMap lookupFunctions() {
        // get all filter functions via function factory
        HashMap result = new HashMap();
        
        Set<FunctionFactory> functionFactories = 
            CommonFactoryFinder.getFunctionFactories(null);
        for (FunctionFactory ff : functionFactories) {
            for (FunctionName functionName : ff.getFunctionNames()) {
                result.put(functionName.getName(), ff);
            }
        }
        
        return result;
    }
    
}
