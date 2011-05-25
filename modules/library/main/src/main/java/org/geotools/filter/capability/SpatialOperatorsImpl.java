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

import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;

/**
 * Implementation of the SpatialOperators interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class SpatialOperatorsImpl implements SpatialOperators {
    
    /** It is not worth it to type narrow the component SpatialOperatorImpl */
    Set<SpatialOperator> operators;
    
    public SpatialOperatorsImpl(){
        this( new ArrayList<SpatialOperator>());
    }
    public SpatialOperatorsImpl( Collection<SpatialOperator> operators ){
        this.operators = new HashSet<SpatialOperator>();
        if ( operators != null ) {
            for( SpatialOperator operator : operators ){
                this.operators.add( new SpatialOperatorImpl( operator ));
            }
        }
    }
    public SpatialOperatorsImpl( SpatialOperator[] operators ) {
        this.operators = new HashSet<SpatialOperator>();
        if ( operators != null ) {
            for( SpatialOperator operator : operators ){
                this.operators.add( new SpatialOperatorImpl( operator ));
            }
        }
    }
    
    public SpatialOperatorsImpl( SpatialOperators copy ){
        this.operators = new HashSet<SpatialOperator>();
        if( copy.getOperators() != null ){
            for( SpatialOperator operator : copy.getOperators() ){
                this.operators.add( new SpatialOperatorImpl( operator ));
            }
        }
    }
    
    public void setOperators( Collection<SpatialOperator> operators ) {
        this.operators = new HashSet<SpatialOperator>();
        if ( operators != null ) {
            for( SpatialOperator operator : operators ){
                this.operators.add( new SpatialOperatorImpl( operator ));
            }
        }
    }
    
    public Collection<SpatialOperator> getOperators() {
        return operators;
    }
    public SpatialOperator getOperator(String name) {
        if ( name == null || operators == null ) {
            return null;
        }
        
        for ( SpatialOperator spatialOperator : operators ) {
            if ( name.equals( spatialOperator.getName() ) ) {
                return spatialOperator;
            }
        }
        
        return null;
    }

    public void addAll( SpatialOperators copy ) {
        if( copy == null ) return;        
        if( copy.getOperators() != null ){
            for( SpatialOperator operator : copy.getOperators() ){
                this.operators.add( new SpatialOperatorImpl( operator ));
            }
        }
    }
    @Override
    public String toString() {
        if( operators == null ){
            return "SpatialOperators: none";
        }
        return "SpatialOperators:"+operators;
    }
}
