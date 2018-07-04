/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.spatial;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class DefaultCRSFilterVisitorTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void force3DCRS2DEnvelope() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4939", true);
        CoordinateReferenceSystem hcrs = CRS.getHorizontalCRS(crs);
        BBOX bbox = ff.bbox("the_geom", -180, -90, 180, 90, null);
        DefaultCRSFilterVisitor visitor = new DefaultCRSFilterVisitor(ff, crs);
        BBOX filtered = (BBOX) bbox.accept(visitor, null);
        Literal box = (Literal) filtered.getExpression2();
        Geometry g = (Geometry) box.evaluate(null);
        assertEquals(hcrs, g.getUserData());
    }

    @Test
    public void force3DCRS3DEnvelope() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4939", true);
        CoordinateReferenceSystem hcrs = CRS.getHorizontalCRS(crs);
        BBOX bbox =
                ff.bbox(
                        ff.property("the_geom"),
                        new ReferencedEnvelope3D(-180, 180, -90, 90, 0, 100, null));
        DefaultCRSFilterVisitor visitor = new DefaultCRSFilterVisitor(ff, crs);
        BBOX filtered = (BBOX) bbox.accept(visitor, null);
        Literal box = (Literal) filtered.getExpression2();
        Geometry g = (Geometry) box.evaluate(null);
        assertEquals(crs, g.getUserData());
    }
}
