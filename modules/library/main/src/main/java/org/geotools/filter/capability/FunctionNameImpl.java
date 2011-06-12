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
package org.geotools.filter.capability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Function;
import org.opengis.geometry.coordinate.ParametricCurveSurface;
import org.opengis.parameter.Parameter;

/**
 * Implementation of the FunctionName interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class FunctionNameImpl extends OperatorImpl implements FunctionName {
    
    /** function arguments */
    List<Parameter<?>> args;
    
    /** funtion return */
    Parameter<?> ret;

    
    public FunctionNameImpl( String name, int argumentCount ) {
        this(name, generateReturn(), generateArguments(argumentCount));
    }
    
    public FunctionNameImpl( String name, String ... argumentsNames ) {
        this(name, argumentsNames.length, Arrays.asList(argumentsNames));
    }
    
    public FunctionNameImpl( String name, List<String> argumentsNames ) {
        this( name, argumentsNames.size(), argumentsNames );
    }
    
    public FunctionNameImpl( String name, int argumentCount, List<String> argumentsNames ) {
        this(name, generateReturn(), generateArguments(argumentsNames));
    }
    
    public FunctionNameImpl( String name, int argumentCount, String ... argumentsNames ) {
        this(name, argumentCount, Arrays.asList(argumentsNames));
    }
    
    public FunctionNameImpl( FunctionName copy ) {
        super( copy );
        this.ret = copy.getReturn();
        this.args = copy.getArguments();
    }
    
    public FunctionNameImpl( String name, Parameter<?> retern, Parameter<?>... arguments) {
        this(name, retern, Arrays.asList(arguments));
    }
    
    public FunctionNameImpl( String name, Class returnType, Parameter<?>... arguments) {
        this(name, parameter(name, returnType), Arrays.asList(arguments));
    }
    
    public FunctionNameImpl( String name, Parameter<?> retern, List<Parameter<?>> arguments) {
        super(name);
        this.ret = retern;
        this.args = arguments;
    }
    
    public int getArgumentCount() {
        return args.size();
    }
    
    public List<Parameter<?>> getArguments() {
        return args;
    }
    
    public Parameter<?> getReturn() {
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        if (args != null) {
            result = prime * result + args.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final FunctionNameImpl other = (FunctionNameImpl) obj;
        if (args == null) {
            return other.args == null;
        }
        return args.equals(other.args);
    }
    
    /**
     * Optional ArgumentNames.
     * <p>
     * This is a fixed length list the same size as getArgumentCount(). 
     */
    public List<String> getArgumentNames() {
        List<String> names = new ArrayList();
        for (Parameter<?> arg : args) {
            names.add(arg.getName());
        }
        return names;
        
    }

    private static Parameter<?> generateReturn() {
        return parameter("return", Object.class);
    }
    
    /**
     * Number of required arguments.
     * <p>
     * <ul>
     * <li>Use a postivie number to indicate the number of arguments.
     *     Example: add( number1, number2 ) = 2</li>
     * <li>Use a negative number to indicate a minimum number:
     *    Example: concat( str1, str2,... ) has -2</li>
     * </ul> 
     */
    private static List<Parameter<?>> generateArguments(int count) {
        List<Parameter<?>> args = new ArrayList();
        if (count < 0) {
            //negative count used to represent variable arguments, create a single argument 
            // with minOccurs == abs(count)
            args.add(parameter("arg", Object.class, Math.abs(count), Integer.MAX_VALUE));
        }
        else {
            for (int i = 0; i < count; i++) {
                args.add(parameter("arg" + i, Object.class, 1, 1));
            }
        }
        
        return args;
    }
    
    private static List<Parameter<?>> generateArguments(List<String> names) {
        List<Parameter<?>> args = new ArrayList();

        for(String name : names) {
            args.add(parameter(name, Object.class, 1, 1));
        }
        
        return args;
    }
    
    public static Parameter<?> parameter(String name, Class type) {
        return parameter(name, type, 1, 1);
    }
    
    public static Parameter<?> parameter(String name, Class type, int min, int max) {
        return new org.geotools.data.Parameter(name, type, min, max);
    }
}
