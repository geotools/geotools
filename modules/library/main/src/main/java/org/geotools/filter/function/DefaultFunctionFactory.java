/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    reated on October 27, 2004, 11:27 AM
 */
package org.geotools.filter.function;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.FunctionImpl;
import org.geotools.util.logging.Logging;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Filter function facotry that uses the spi lookup mechanism to create functions.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class DefaultFunctionFactory implements FunctionFactory {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.filter");
    private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    
    private Map<String,FunctionDescriptor> functionCache;

    public List<FunctionName> getFunctionNames() {
        ArrayList list = new ArrayList(functionCache().size());
        for (FunctionDescriptor fd : functionCache().values()) {
            list.add(fd.name);
        }
        return list;
    }
    
    public Function function(String name, List<Expression> parameters, Literal fallback) {
        
        // cache lookup
        FunctionDescriptor fd = functionCache().get(functionName(name));
        if (fd == null) {
            //no such function
            return null;
        }
        
        try {
            return fd.newFunction(parameters, fallback);
        }
        catch(Exception e){
            LOGGER.log( Level.FINER, "Unable to create function " + name + "Function", e);
            //just continue on to return null
        }
        
        return null;
    }
    
    private Map<String,FunctionDescriptor> functionCache() {
        if (functionCache == null) {
            synchronized(this) {
                if (functionCache == null) {
                    loadFunctions();
                }
            }
        }
        
        return functionCache;
    }
    
    private void loadFunctions() {
        functionCache = new HashMap();
        
        // Get all the GeoTools FunctionExpression implementations
        // and store in functionExpressionCache
        // (these are implementations of the legacy GeoTools FunctionExpression
        // interface)
        Set functions = CommonFactoryFinder.getFunctionExpressions(null);
        for (Iterator it = functions.iterator(); it.hasNext();) {
            FunctionExpression function = (FunctionExpression) it.next();
            FunctionDescriptor fd = new FunctionDescriptor(
                filterFactory.functionName(function.getName(), function.getArgCount()), 
                function.getClass()); 
            
            functionCache.put(functionName(function.getName()), fd);
        }
        
        // Get all the GeoAPI Function implementations
        functions = CommonFactoryFinder.getFunctions(null);
        for (Iterator i = functions.iterator(); i.hasNext();) {
            Function function = (Function) i.next();
            int argc = function instanceof FunctionExpression ? 
                ((FunctionExpression)function).getArgCount() : function.getParameters().size();
            
            FunctionDescriptor fd = new FunctionDescriptor(
                filterFactory.functionName(function.getName(), argc), function.getClass());
            functionCache.put(functionName(function.getName()), fd);
        }
    }
    
    private String functionName(String name) {
        
        //strip off "Function" prefix
        int index = -1;

        if ((index = name.indexOf("Function")) != -1) {
            name = name.substring(0, index);
        }

        //convert to lower case
        name = name.toLowerCase().trim();
        
        //JD: not sure why the first character is converted back to upper case, disabling this and
        // just keeping everything lower case, as this is only used internally to store functions
        // it should affect any existing clients
        //char c = name.charAt(0);
        //name = name.replaceFirst("" + c, "" + Character.toUpperCase(c));

        return name;
    }

    static class FunctionDescriptor {
        FunctionName name;
        Class clazz;
        
        FunctionDescriptor(FunctionName name, Class clazz) {
            this.name = name;
            this.clazz = clazz;
        }
        
        Function newFunction(List<Expression> parameters, Literal fallback) throws Exception {
            // cache lookup
            if (FunctionExpression.class.isAssignableFrom(clazz)) {
                FunctionExpression function = (FunctionExpression) clazz.newInstance();
                if(parameters != null) {
                    function.setParameters(parameters);
                }
                
                if( fallback != null && function instanceof ClassificationFunction){
                    ClassificationFunction classification = (ClassificationFunction) function;
                    classification.setFallbackValue( fallback );
                }
                return function;
            }

            if(FunctionImpl.class.isAssignableFrom(clazz)) {
                FunctionImpl function = (FunctionImpl) clazz.newInstance();
                if(parameters != null){
                    function.setParameters( (List) parameters );
                }
                if(fallback != null)
                        function.setFallbackValue( fallback );
                return function;
            }
            
            //Function function = (Function) functionClass.newInstance();
            Constructor<Function> constructor = clazz.getConstructor( new Class[]{ List.class, Literal.class} );
            return constructor.newInstance( parameters, fallback );
        }
    }
}
