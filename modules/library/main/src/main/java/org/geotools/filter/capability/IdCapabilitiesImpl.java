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

import org.opengis.filter.capability.IdCapabilities;

/**
 * Implementation of the IdCapabilities interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class IdCapabilitiesImpl implements IdCapabilities {

    boolean eid;
    boolean fid;
    
    public IdCapabilitiesImpl() {
        this( false, false );
    }
    public IdCapabilitiesImpl( boolean eid, boolean fid ) {
        this.eid = eid;
        this.fid = fid;
    }
    public IdCapabilitiesImpl( IdCapabilities copy ) {
        this( copy.hasEID(), copy.hasFID() ); 
    }
    
    public boolean hasEID() {
        return eid;
    }
    public void setEid( boolean eid ) {
        this.eid = eid;
    }
    public boolean hasFID() {
        return fid;
    }
    public void setFID( boolean fid ){
        this.fid = fid;
    }
    public void addAll( IdCapabilities copy ){
        if( copy == null ) return;
        if( copy.hasEID() ){
            this.eid = true;
        }
        if( copy.hasFID() ){
            this.fid = true;
        }
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("IdCapabilitiesImpl[");
        if( fid){
            buf.append(" FeatureId");
        }
        if( eid){
            buf.append(" GMLObjectId");
        }
        buf.append(" ]");
        return buf.toString();
    }
}
