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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
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

    /**
     * Tests the handling of a pre-flipped geometry, checking that the geometry is wrapped correctly
     *
     * @throws Exception in case of error
     */
    @Test
    public void testPreFlipped() throws Exception {
        CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -40, 40, WGS84);
        CoordinateReferenceSystem MERCATOR = CRS.decode("EPSG:3395", true);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);
        mercatorEnvelope.translate(mercatorEnvelope.getWidth() / 2, 0);
        Geometry polygon =
                wktReader.read(
                        "MultiPolygon (((179.62477969 52.46819975, 179.51039918 52.20137203, 179.94028987 52.14290407, -179.9428079 52.40938205, 179.62477969 52.46819975)))");
        Map<String, Object> params = new HashMap<>();
        params.put(WrappingProjectionHandler.DATELINE_WRAPPING_CHECK_ENABLED, true);

        MathTransform mt = CRS.findMathTransform(WGS84, MERCATOR, true);
        Geometry reprojected = JTS.transform(polygon, mt);

        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true, params);
        Geometry preProcessed = handler.preProcess(polygon);
        assertEquals(WrappingProjectionHandler.PREFLIPPED_OBJECT, preProcessed.getUserData());
        reprojected.setUserData(WrappingProjectionHandler.PREFLIPPED_OBJECT);
        Geometry postGeom = handler.postProcess(mt, reprojected);
        Envelope env = postGeom.getEnvelopeInternal();
        double EPS = 1e-5;
        // check the geometry is in the same area as the rendering envelope
        assertEquals(0.0, env.getMinX(), EPS);
        assertEquals(-1.0, env.getMaxX(), EPS);
    }
}
