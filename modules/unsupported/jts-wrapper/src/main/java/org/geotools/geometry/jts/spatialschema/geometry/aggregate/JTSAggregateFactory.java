/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema.geometry.aggregate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.geotools.factory.Factory;
import org.geotools.factory.Hints;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.aggregate.MultiCurve;
import org.opengis.geometry.aggregate.MultiPoint;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Implementation of AggregateFactory able to make MultiPointImpl but
 * little else.
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class JTSAggregateFactory implements Factory,  AggregateFactory {
    private CoordinateReferenceSystem crs;
    private final Map usedHints = new LinkedHashMap();
    /**
     * No argument constructor for FactorySPI
     */
    public JTSAggregateFactory(){
        this( DefaultGeographicCRS.WGS84);
    }
    /**
     * Hints constructor for FactoryRegistry
     */
    public JTSAggregateFactory( Hints hints ){
        this( (CoordinateReferenceSystem) hints.get( Hints.CRS ) );
    }
    /**
     * Direct constructor for test cases
     */
    public JTSAggregateFactory( CoordinateReferenceSystem crs ) {
        this.crs = crs;
        usedHints.put( Hints.CRS, crs );
    }
    public Map getImplementationHints() {
        return usedHints;
    }
    public MultiCurve createMultiCurve( Set arg0 ) {
        throw new UnsupportedOperationException("MultiCurve not implemented");
    }
    public MultiPoint createMultiPoint( Set arg0 ) {
        return new MultiPointImpl( crs );
    }
    public MultiPrimitive createMultiPrimitive( Set arg0 ) {
        throw new UnsupportedOperationException("MultiPrimitive not implemented");
    }
    public MultiSurface createMultiSurface( Set arg0 ) {
        throw new UnsupportedOperationException("MultiSurface not implemented");
    }
}
