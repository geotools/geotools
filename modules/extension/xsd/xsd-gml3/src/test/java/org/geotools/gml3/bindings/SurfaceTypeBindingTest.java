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
package org.geotools.gml3.bindings;

import org.geotools.gml3.GML3TestSupport;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class SurfaceTypeBindingTest extends GML3TestSupport {

    public void testParse() throws Exception {
        GML3MockData.surface(document, document);
        MultiPolygon surface = (MultiPolygon) parse();
        assertNotNull(surface);
        
        assertEquals( 1, surface.getNumGeometries() );
        Polygon p = (Polygon) surface.getGeometryN( 0 );
        
        assertEquals( 1, p.getNumInteriorRing() );
    }
}
