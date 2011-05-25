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

import java.util.Arrays;
import java.util.List;

import org.opengis.filter.capability.FunctionName;

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
    /** Number of required arguments */
    int argumentCount;
    
    List<String> argumentNames;
    
    public FunctionNameImpl( String name, int argumentCount ) {
        super( name );
        this.argumentCount = argumentCount;
        this.argumentNames = null;
    }
    
    public FunctionNameImpl( String name, String argumentsNames[] ) {
        super( name );
        this.argumentCount = argumentsNames.length;
        this.argumentNames = generateArgumentNames( argumentCount, argumentsNames );        
    }
    public FunctionNameImpl( String name, List<String> argumentsNames ) {
        super( name );
        this.argumentCount = argumentsNames.size();
        this.argumentNames = generateArgumentNames( argumentCount, argumentsNames );        
    }
    public FunctionNameImpl( FunctionName copy ) {
        super( copy );
        this.argumentCount = copy.getArgumentCount();
        this.argumentNames = generateArgumentNames( argumentCount, copy.getArgumentNames() );
    }
    
    public void setArgumentCount( int argumentCount ) {
        this.argumentCount = argumentCount;
        this.argumentNames = generateArgumentNames( argumentCount, argumentNames );
    }
    
    public int getArgumentCount() {
        return argumentCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + argumentCount;
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
        if (argumentCount != other.argumentCount)
            return false;
        return true;
    }
    
    public void setArgumentNames( List<String> argumentNames ) {
        this.argumentNames = argumentNames;
    }
    /**
     * Optional ArgumentNames.
     * <p>
     * This is a fixed length list the same size as getArgumentCount(). 
     */
    public List<String> getArgumentNames() {
        if( argumentNames == null ){
            argumentNames = generateArgumentNames( argumentCount );
        }
        return argumentNames;
    }

    private static List<String> generateArgumentNames( int count ){
        List<String> names = Arrays.asList( new String[count]);
        for( int i=0; i < count; i++){
            names.set(i, "arg"+i );
        }
        return names;
    }
    private static List<String> generateArgumentNames( int count, List<String> copy ){
        List<String> names = Arrays.asList( new String[count]);    
        for( int i=0; i < count; i++){
            String name = "arg"+i;
            if( copy != null && i < copy.size() && copy.get(i) != null ){
                name = copy.get(i);
            }
            names.set(i, name );
        }
        return names;
    }
    private static List<String> generateArgumentNames( int count, String[] copy ){        
        List<String> names = Arrays.asList( new String[count]);
        for( int i=0; i < count; i++){
            String name = "arg"+i;
            if( copy != null && i < copy.length && copy[i] != null ){
                name = copy[i];
            }
            names.set(i, name );
        }
        return names;        
    }
}
