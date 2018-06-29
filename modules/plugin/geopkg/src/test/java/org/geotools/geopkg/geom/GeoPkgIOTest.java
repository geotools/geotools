/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.geom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

public class GeoPkgIOTest {

    @Test
    public void testReadWrite() throws IOException {
        Geometry g1 = new GeometryBuilder().point(0, 0).buffer(10);
        byte[] bytes = new GeoPkgGeomWriter().write(g1);

        Geometry g2 = new GeoPkgGeomReader(bytes).get();
        assertTrue(g1.equals(g2));
    }

    @Test
    public void testHeader() throws IOException {
        Geometry g1 = new GeometryBuilder().point(0, 0).buffer(10);
        byte[] bytes = new GeoPkgGeomWriter().write(g1);

        assertEquals(0x47, bytes[0]);
        assertEquals(0x50, bytes[1]);
        assertEquals(0x00, bytes[2]);
    }
}
