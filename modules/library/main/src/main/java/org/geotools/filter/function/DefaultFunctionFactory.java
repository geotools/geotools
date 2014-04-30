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
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.FunctionImpl;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Filter function factory that uses the service provider interface (SPI) lookup mechanism to create functions.
 * <p>
 * DefaultFunctionFactory is responsible for collection {@link FunctionExpressio} implementations into a
 * FunctionFactory.
 * 
 * @author Justin Deoliveira, OpenGeo
 * 
 * @source $URL$
 */
public class DefaultFunctionFactory implements FunctionFactory {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.filter");
    private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    
    private Map<Name,FunctionDescriptor> functionCache;

    public List<FunctionName> getFunctionNames() {
        ArrayList<FunctionName> list = new ArrayList<FunctionName>(functionCache().size());
        for (FunctionDescriptor fd : functionCache().values()) {
            list.add(fd.name);
//            if( "rint".equals(fd.name.getName())){
//                System.out.println("Listed rint");
//            }
        }
        return list;
    }

    public Function function(String name, List<Expression> parameters, Literal fallback) {
        return function(new NameImpl(name), parameters, fallback);
    }

    public Function function(Name name, List<Expression> parameters, Literal fallback) {
        
        // cache lookup
        Name key = functionName(name);
        FunctionDescriptor fd = functionCache().get(key);
        if (fd == null) {
            fd = functionCache().get(name);
            if( fd == null ){
                //no such function
                return null;
            }
            // LOGGER.warning("Name conflict between '"+name+"' and '"+key+"'");
        }
        
        try {
            Function newFunction = fd.newFunction(parameters, fallback);
            return newFunction;
        }
        catch(Exception e){
            LOGGER.log( Level.FINER, "Unable to create function " + name + "Function", e);
            //just continue on to return null
        }        
        return null;
    }
    
    private Map<Name,FunctionDescriptor> functionCache() {
        if (functionCache == null) {
            synchronized(this) {
                if (functionCache == null) {
                    functionCache = loadFunctions();
                }
            }
        }
        return functionCache;
    }
    
    private FunctionName getFunctionName( Function function ){
        String name = function.getName();
        FunctionName functionName = function.getFunctionName();
        
        if( functionName == null && function instanceof FunctionExpressionImpl){
            functionName = function.getFunctionName();
        }
        if( functionName == null ){
            int argc;
            if( function instanceof FunctionExpression ){
                argc = ((FunctionExpression)function).getArgCount();
            }
            else {
                argc = function.getParameters().size();
            }
            functionName = filterFactory.functionName(name, argc );
        }
        else {
            if( !functionName.getName().equals(name )){
                LOGGER.warning( function.getClass() +" has name conflict betwee '"+name+"' and '"+functionName.getName()+"'");
            }
        }
        return functionName;
    }
    private Map<Name,FunctionDescriptor> loadFunctions() {
        Map<Name,FunctionDescriptor> functionMap = new HashMap<Name,FunctionDescriptor>();

        Set<Function> functions = CommonFactoryFinder.getFunctions(null);
        for (Iterator<Function> i = functions.iterator(); i.hasNext();) {
            Function function = (Function) i.next();
            FunctionName functionName = getFunctionName( function );
            Name name = functionName.getFunctionName();

            FunctionDescriptor fd = new FunctionDescriptor( functionName, (Class<Function>) function.getClass() );

            // needed to insert justin's name hack here to ensure consistent lookup
            Name key = functionName(name);
            if( functionMap.containsKey(key)){
                // conflicted name - probably a cut and paste error when creating functionName
                FunctionDescriptor conflict = functionMap.get(key);
                LOGGER.warning("Function "+key+" clash between "+conflict.clazz.getSimpleName()+" and "+function.getClass().getSimpleName());
            }
            functionMap.put( key, fd );
        }
        return functionMap;
    }
    
    private Name functionName(Name functionName) {
        String name = functionName.getLocalPart();
        
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

        return new NameImpl(functionName.getNamespaceURI(), functionName.getSeparator(), name);
    }

    static class FunctionDescriptor {
        FunctionName name;
        Class<Function> clazz;
        
        FunctionDescriptor(FunctionName name, Class<Function> clazz) {
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
                if( fallback != null ){
                    function.setFallbackValue( fallback );
                }
                return function;
            }
            if(FunctionImpl.class.isAssignableFrom(clazz)) {
                FunctionImpl function = (FunctionImpl) clazz.newInstance();
                if(parameters != null){
                    function.setParameters( parameters );
                }
                if(fallback != null){
                    function.setFallbackValue( fallback );
                }
                return function;
            }
            //Function function = (Function) functionClass.newInstance();
            Constructor<Function> constructor = clazz.getConstructor( new Class[]{ List.class, Literal.class} );
            return constructor.newInstance( parameters, fallback );
        }
    }
}
