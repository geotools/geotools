/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTReader;

public class WrappingProjectionHandlerTest {

    final GeometryFactory gf = new GeometryFactory();
    final WKTReader wktReader = new WKTReader();

    /**
     * Tests the accumulate function using a Geometry collection with different geometry types,
     * checking that it should returns a {@link Geometry} class.
     */
    @Test
    public void testAccumulateWithDifferentGeometryTypes() throws Exception {
        List<Geometry> resultGeoms = new ArrayList<>();
        Geometry point = wktReader.read("POINT(1 1)");
        Geometry line = wktReader.read("LINESTRING(1 1, 1 2, 2 2)");
        GeometryCollection collection = new GeometryCollection(new Geometry[] {point, line}, gf);
        ReferencedEnvelope envelope = new ReferencedEnvelope(0, 5, 0, 5, CRS.decode("EPSG:4326"));
        Class resultClass =
                WrappingProjectionHandler.accumulate(resultGeoms, collection, null, envelope);
        assertEquals(Geometry.class, resultClass);
    }
}
