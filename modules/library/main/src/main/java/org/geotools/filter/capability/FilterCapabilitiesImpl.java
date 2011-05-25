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

import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.IdCapabilities;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;

/**
 * Implementation of the FilterCapabilities interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class FilterCapabilitiesImpl implements FilterCapabilities {

    String version;
    IdCapabilitiesImpl id;
    ScalarCapabilitiesImpl scalar;
    SpatialCapabiltiesImpl spatial;
    
    public FilterCapabilitiesImpl(){
        this( FilterCapabilities.VERSION_100 );
    }
    public FilterCapabilitiesImpl( String version ){
        this.version = version;
    }    
    public FilterCapabilitiesImpl( String version, ScalarCapabilities scalar, 
        SpatialCapabilities spatial, IdCapabilities id ) {
        this.version = version;
        this.id = toIdCapabilitiesImpl(id);
        this.scalar = toScalarCapabilitiesImpl( scalar );
        this.spatial = toSpatialCapabiltiesImpl( spatial );
    }
    public FilterCapabilitiesImpl( FilterCapabilities copy ) {
        this.version = copy.getVersion();
        this.id = copy.getIdCapabilities() == null ? null : new IdCapabilitiesImpl( copy.getIdCapabilities() );
        this.scalar = toScalarCapabilitiesImpl( copy.getScalarCapabilities() );
        this.spatial = toSpatialCapabiltiesImpl( copy.getSpatialCapabilities() );
    }    
    
    private ScalarCapabilitiesImpl toScalarCapabilitiesImpl( ScalarCapabilities scalarCapabilities ) {
        if( scalarCapabilities == null ){
            return new ScalarCapabilitiesImpl();
        }
        if( scalarCapabilities instanceof ScalarCapabilitiesImpl){
            return (ScalarCapabilitiesImpl) scalarCapabilities;
        }
        return new ScalarCapabilitiesImpl( scalarCapabilities );
    }
    
    private IdCapabilitiesImpl toIdCapabilitiesImpl( IdCapabilities idCapabilities ) {
        if( idCapabilities == null ){
            return new IdCapabilitiesImpl();
        }
        if( idCapabilities instanceof IdCapabilitiesImpl){
            return (IdCapabilitiesImpl) idCapabilities;
        }
        return new IdCapabilitiesImpl( idCapabilities );
    }
    private static SpatialCapabiltiesImpl toSpatialCapabiltiesImpl( SpatialCapabilities spatialCapabilities ) {
        if( spatialCapabilities == null ){
            return new SpatialCapabiltiesImpl();
        }
        if( spatialCapabilities instanceof SpatialCapabiltiesImpl){
            return (SpatialCapabiltiesImpl) spatialCapabilities;
        }
        return new SpatialCapabiltiesImpl( spatialCapabilities );
    
    }
    
    /**
     * Version of the Filter Specification supported.
     * <p>
     * This should be one of the following constants:
     * <ul>
     * <li>FilterCapabilities.VERSION_100
     * <li>FilterCapabilities.VERSION_110
     * </ul>
     * @param version
     */
    public void setVersion( String version ) {
        this.version = version;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setId( IdCapabilities id ) {
        this.id = toIdCapabilitiesImpl(id);
    }

    public IdCapabilitiesImpl getIdCapabilities() {
        if( id == null ){
            id = new IdCapabilitiesImpl();
        }
        return id;
    }

    public void setScalar( ScalarCapabilities scalar ) {
        this.scalar = toScalarCapabilitiesImpl( scalar );
    }
    public ScalarCapabilitiesImpl getScalarCapabilities() {
        if( scalar == null ){
            scalar = new ScalarCapabilitiesImpl();
        }
        return scalar;
    }

    public void setSpatial( SpatialCapabilities spatial ) {
        this.spatial = toSpatialCapabiltiesImpl( spatial );
    }
    
    public SpatialCapabiltiesImpl getSpatialCapabilities() {
        if( spatial == null ){
            spatial = new SpatialCapabiltiesImpl();
        }
        return spatial;
    }
    
    public void addAll( FilterCapabilities copy ){
        getIdCapabilities().addAll( copy.getIdCapabilities() );
        getScalarCapabilities().addAll( copy.getScalarCapabilities() );
        getSpatialCapabilities().addAll( copy.getSpatialCapabilities() );
        if( getVersion().compareTo( copy.getVersion() ) < 0 ){
            setVersion( copy.getVersion() );
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("FilterCapabilities [");
        buf.append(version);
        buf.append("]");
        if( id != null ){
            buf.append( "\n idCapabilities=");
            buf.append( id );
        }
        if( scalar != null ){
            buf.append("\n scalarCapabilities=");
            buf.append( scalar );
        }
        if( spatial != null ){
            buf.append("\n spatialCapabilities=");
            buf.append( spatial );
        }        
        return buf.toString();
    }
}
