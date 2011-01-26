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
import java.util.Set;

import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperators;

/**
 * Implementation of the SpatialCapabilities interface.
 * <p>
 * This class is "null safe" in that component classes
 * will be created as needed if if they were not provided
 * during construction.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class SpatialCapabiltiesImpl implements SpatialCapabilities {

    Set<GeometryOperand> geometryOperands;
    SpatialOperatorsImpl spatialOperators;
    
    public SpatialCapabiltiesImpl(){
        this.geometryOperands = new HashSet<GeometryOperand>();
        this.spatialOperators = new SpatialOperatorsImpl();
    }
    public SpatialCapabiltiesImpl(Collection<GeometryOperand> geometryOperands, 
            SpatialOperators spatialOperators) {
        this.geometryOperands = new HashSet<GeometryOperand>();
        if( geometryOperands != null ){
            this.geometryOperands.addAll( geometryOperands );    
        }
        this.spatialOperators = toSpatialOperatorsImpl( spatialOperators );
    }
    public SpatialCapabiltiesImpl( GeometryOperand[] geometryOperands, 
            SpatialOperators spatialOperators) {
        this.geometryOperands = new HashSet<GeometryOperand>();
        if( geometryOperands != null ){
            this.geometryOperands.addAll( Arrays.asList( geometryOperands ) );
        }
        this.spatialOperators = toSpatialOperatorsImpl( spatialOperators );
    }
    public SpatialCapabiltiesImpl( SpatialCapabilities copy ){
        this.spatialOperators = new SpatialOperatorsImpl(  );
        this.geometryOperands = new HashSet<GeometryOperand>();
        if( copy.getGeometryOperands() != null ){
            geometryOperands.addAll( copy.getGeometryOperands() );
        }
    }
    
    private static SpatialOperatorsImpl toSpatialOperatorsImpl( SpatialOperators spatialOperators ) {
        if( spatialOperators == null ){
            return new SpatialOperatorsImpl();
        }
        else if( spatialOperators instanceof SpatialOperatorsImpl){
            return (SpatialOperatorsImpl) spatialOperators;
        }
        return new SpatialOperatorsImpl( spatialOperators );
    }
    
    public void setGeometryOperands( Collection<GeometryOperand> geometryOperands ) {
        this.geometryOperands = new HashSet<GeometryOperand>( geometryOperands );
    }
    public Collection<GeometryOperand> getGeometryOperands() {
        return geometryOperands;
    }    
    public void setSpatialOperators( SpatialOperatorsImpl spatialOperators ) {
        this.spatialOperators = spatialOperators;
    }
    public SpatialOperatorsImpl getSpatialOperators() {
        if( spatialOperators == null ){
            spatialOperators = new SpatialOperatorsImpl();
        }
        return spatialOperators;
    }
    
    /**
     * Add additional SpatialOperators.
     * 
     * @param copy Copy these additional SpatialCapabilities 
     */
    public void addAll( SpatialCapabilities copy ) {
        if( copy.getGeometryOperands() != null ){
            this.geometryOperands.addAll( copy.getGeometryOperands() );
        }
        this.spatialOperators.addAll( copy.getSpatialOperators() );
    }
}
