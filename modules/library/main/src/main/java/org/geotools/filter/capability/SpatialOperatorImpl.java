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
import java.util.Collection;
import java.util.HashSet;

import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.SpatialOperator;

/**
 * Implementation of the SpatialOperator interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public class SpatialOperatorImpl extends OperatorImpl 
    implements SpatialOperator {

    HashSet<GeometryOperand> geometryOperands;
    
    public SpatialOperatorImpl( String name ){
        super( name );
        geometryOperands = new HashSet<GeometryOperand>();
    }
    public SpatialOperatorImpl( String name, Collection<GeometryOperand> geometryOperands ) {
        super( name );
        this.geometryOperands = new HashSet<GeometryOperand>(geometryOperands);
    }
    public SpatialOperatorImpl( String name, GeometryOperand[] geometryOperands ) {
        super( name );
        this.geometryOperands = new HashSet<GeometryOperand>();
        if( geometryOperands != null ){
            this.geometryOperands.addAll( Arrays.asList(geometryOperands) );
        }
    }
    public SpatialOperatorImpl( SpatialOperator copy ) {
        this( copy.getName() );
        this.geometryOperands = new HashSet<GeometryOperand>();
        if( copy.getGeometryOperands() != null ){
            this.geometryOperands.addAll( copy.getGeometryOperands() );
        }
    }
    public void setGeometryOperands( Collection<GeometryOperand> geometryOperands ) {
        this.geometryOperands = new HashSet<GeometryOperand>( geometryOperands );
    }
    public Collection<GeometryOperand> getGeometryOperands() {
        return geometryOperands;
    }
    
    public void addAll( SpatialOperator copy ){
        if( copy == null ) return;
        if( copy.getGeometryOperands() != null ){
            for( GeometryOperand operand : copy.getGeometryOperands() ){
                this.geometryOperands.add( operand );
            }
        }
    }
}
