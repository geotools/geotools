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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.Operator;

/**
 * Implementation of the ComparisonOperators interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class ComparisonOperatorsImpl implements ComparisonOperators {

    Set<Operator> operators;
    
    public ComparisonOperatorsImpl(){
        this( new ArrayList<Operator>());
    }
    
    /**
     * Copy the provided ComparisonOperator
     * @param copy
     */
    public ComparisonOperatorsImpl( ComparisonOperators copy ){
        this.operators = new HashSet<Operator>( copy.getOperators() );
    }
    
    public ComparisonOperatorsImpl( Collection<Operator> operators ) {
        this.operators = new HashSet<Operator>( operators );        
    }
    
    public ComparisonOperatorsImpl( Operator[] operators ) {
        if ( operators == null ){
            operators = new Operator[]{};
        }
        this.operators = new HashSet( Arrays.asList( operators ) );
    }
    
    public Collection<Operator> getOperators() {
        if( operators == null ){
            operators = new HashSet<Operator>();
        }
        return operators;
    }

    public void setOperators( Collection<Operator> operators ) {
        this.operators = new HashSet<Operator>( operators );
    }
    /**
     * @return Operator with the provided name, or null if not supported
     */
    public Operator getOperator(String name) {
        if ( name == null || operators == null) {
            return null;
        }        
        for ( Operator operator : operators ) {            
            if ( name.equals( operator.getName() ) ) {
                return operator;
            }
        }        
        return null;
    }
    
    public void addAll( ComparisonOperators copy ){
        if( copy.getOperators() != null ){
            getOperators().addAll( copy.getOperators() );
        }
    }
    @Override
    public String toString() {
        if( operators == null ){
            return "ComparisonOperators: none";
        }
        return "ComparisonOperators:"+operators;
    }
}
