/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.util;

import static org.junit.Assert.assertEquals;

import org.geotools.referencing.operation.transform.IdentityTransform;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class FeatureUtilitiesTest {

    @Test
    public void testConvertPolygonToPointArrayRounding() throws Exception {
        Polygon polygon =
                (Polygon)
                        new WKTReader()
                                .read(
                                        "POLYGON((-9.9 -9.9, -9.9 9.9, 9.9 9.9, 9.9 -9.9, -9.9 -9.9))");
        java.awt.Polygon poly =
                FeatureUtilities.convertPolygonToPointArray(
                        polygon, IdentityTransform.create(2), null);

        assertEquals(-10, poly.getBounds().x);
        assertEquals(-10, poly.getBounds().y);
        assertEquals(20, poly.getBounds().width);
        assertEquals(20, poly.getBounds().height);
    }
}
