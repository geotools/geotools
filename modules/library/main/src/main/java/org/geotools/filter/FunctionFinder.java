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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.filter.function.ClassificationFunction;
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
	
    private Map<String,Class<FunctionExpression>> functionExpressionCache;
    private Map<String,Class<FunctionImpl>> functionImplCache;
    private Map<String,Class<Function>> functionCache;
    
    public FunctionFinder(Hints hints) {
        // currently hints are not used, need help :-P
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
    public Function findFunction(String name, List/* <Expression> */parameters){
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
    public Function findFunction(String name, List parameters, Literal fallback ) {
        name = functionName(name);

        try {
            // load the caches at first access
            synchronized (this) {

                if (functionExpressionCache == null) {
                    // scan for all the functions on the classpath

                    functionExpressionCache = new HashMap();
                    functionImplCache = new HashMap();
                    functionCache = new HashMap();

                    // Get all the GeoTools FunctionExpression implementations
                    // and store in functionExpressionCache
                    // (these are implementations of the legacy GeoTools FunctionExpression
                    // interface)
                    Set functions = CommonFactoryFinder.getFunctionExpressions(null);
                    for (Iterator it = functions.iterator(); it.hasNext();) {
                        FunctionExpression function = (FunctionExpression) it.next();
                        functionExpressionCache.put(function.getName().toLowerCase(),
                                (Class<FunctionExpression>) function.getClass());
                    }
                    // Get all the GeoAPI Function implementations
                    functions = CommonFactoryFinder.getFunctions(null);
                    for (Iterator i = functions.iterator(); i.hasNext();) {
                        Function function = (Function) i.next();
                        String functionName = function.getName().toLowerCase();
                        Class functionImplementation = function.getClass();
                        if (function instanceof FunctionImpl) {
                            functionImplCache.put(functionName,
                                    (Class<FunctionImpl>) functionImplementation);
                        } else if (function instanceof FunctionExpression) {
                            if (!functionExpressionCache.containsKey(functionName)) {
                                functionExpressionCache.put(functionName, functionImplementation);
                            }
                        } else {
                            functionCache.put(functionName,
                                    (Class<Function>) functionImplementation);
                        }
                    }
                }
            }
            
            // cache lookup
            Class clazz = (Class) functionExpressionCache.get(name.toLowerCase());
            if(clazz != null) {
                FunctionExpression function = (FunctionExpression) clazz.newInstance();
                if(parameters != null)
                    function.setParameters(parameters);
                
                if( fallback != null && function instanceof ClassificationFunction){
                    ClassificationFunction classification = (ClassificationFunction) function;
                    classification.setFallbackValue( fallback );
                }
                return function;
            }
            clazz = (Class) functionImplCache.get(name.toLowerCase());
            if(clazz != null) {
                FunctionImpl function = (FunctionImpl) clazz.newInstance();
                if(parameters != null){
                    function.setParameters( (List) parameters );
                }
                if(fallback != null)
                	function.setFallbackValue( fallback );
                return function;
            }    
            
            if(functionCache.containsKey(name.toLowerCase()) ){
            	Class<Function> functionClass = functionCache.get( name.toLowerCase() );
            	//Function function = (Function) functionClass.newInstance();
            	Constructor<Function> constructor = functionClass.getConstructor( new Class[]{ List.class, Literal.class} );
                return constructor.newInstance( parameters, fallback );
            }
        } catch (Exception e) {
        	LOGGER.log( Level.FINER, "Unable to create class " + name + "Function", e);
            if( fallback != null ){
                return new FallbackFunction( name, parameters, fallback );                
            }
            else {
                throw new RuntimeException("Unable to create class " + name + "Function", e);
            }
        }
        if(  fallback != null ){
            return new FallbackFunction( name, parameters, fallback );                
        }
        else {
            throw new RuntimeException("Unable to find function " + name);
        }
    }

    private String functionName(String name) {
        int index = -1;

        if ((index = name.indexOf("Function")) != -1) {
            name = name.substring(0, index);
        }

        name = name.toLowerCase().trim();
        char c = name.charAt(0);
        name = name.replaceFirst("" + c, "" + Character.toUpperCase(c));

        return name;
    }
}
