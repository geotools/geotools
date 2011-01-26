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
package org.geotools.geometry.jts.spatialschema.geometry.complex;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geotools.factory.Factory;
import org.geotools.factory.Hints;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.complex.ComplexFactory;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.complex.CompositePoint;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class JTSComplexFactory implements Factory, ComplexFactory {

    private CoordinateReferenceSystem crs;
    private final Map usedHints = new LinkedHashMap();
    /**
     * No argument constructor for FactorySPI
     */
    public JTSComplexFactory(){
        this( DefaultGeographicCRS.WGS84);
    }
    /**
     * Hints constructor for FactoryRegistry
     */
    public JTSComplexFactory( Hints hints ){
        this( (CoordinateReferenceSystem) hints.get( Hints.CRS ) );
    }
    /**
     * Direct constructor for test cases
     */
    public JTSComplexFactory( CoordinateReferenceSystem crs ) {
        this.crs = crs;
        usedHints.put( Hints.CRS, crs );
    }
    public Map getImplementationHints() {
        return usedHints;
    }
    /**
     * @param curves List of OrientableCurve
     */
    public CompositeCurve createCompositeCurve( List curves ) {
        CompositeCurveImpl composite = new CompositeCurveImpl( null, crs );
        composite.getElements().addAll( curves );
        return composite;
    }

    public CompositePoint createCompositePoint( Point arg0 ) {
        return null;
    }

    public CompositeSurface createCompositeSurface( List list ) {
        CompositeSurfaceImpl composite = new CompositeSurfaceImpl();
        composite.getElementList().addAll( list );        
        return composite;
    }

}
