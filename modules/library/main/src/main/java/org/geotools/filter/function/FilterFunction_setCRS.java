package org.geotools.filter.function;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

public class FilterFunction_setCRS extends FunctionExpressionImpl {

    public FilterFunction_setCRS() {
        super("setCRS");
    }

    public int getArgCount() {
        return 2;
    }

    public Object evaluate(Object feature) {
        Geometry geom;
        CoordinateReferenceSystem crs;

        try { // attempt to get value and perform conversion
            geom = (Geometry) getExpression(0).evaluate(feature, Geometry.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Expected argument of type Geometry for argument #0");
        }
        
        try { // try to parse the SRS
            crs = getExpression(1).evaluate(feature, CoordinateReferenceSystem.class);
            if(crs == null) {
                String srs = getExpression(1).evaluate(feature, String.class);
                try {
                    crs = CRS.decode(srs);
                } catch(FactoryException e) {
                    crs = CRS.parseWKT(srs);
                }
            }
        } catch(Exception e) {
            throw new IllegalArgumentException(
                    "Expected argument of type CoordinateReferenceSystem, WKT or valid EPSG code for argument #1");
        }

        if(geom != null) {
            geom.setUserData(crs);
        }
        
        return geom;
    }
}
