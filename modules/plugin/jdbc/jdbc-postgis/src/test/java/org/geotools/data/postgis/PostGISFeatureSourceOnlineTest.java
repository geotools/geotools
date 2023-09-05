/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCFeatureSourceOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class PostGISFeatureSourceOnlineTest extends JDBCFeatureSourceOnlineTest {

    public PostGISFeatureSourceOnlineTest() {
        this.forceLongitudeFirst = true;
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISTestSetup();
    }

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
    }

    @Test
    public void testBBOXOverlapsEncoding() throws Exception {
        // enable bbox envelope encoding
        ((PostGISDialect) dataStore.getSQLDialect()).setEncodeBBOXFilterAsEnvelope(true);

        GeometryFactory gf = dataStore.getGeometryFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Intersects filter =
                ff.intersects(
                        ff.property("geometry"),
                        ff.literal(
                                gf.createPolygon(
                                        gf.createLinearRing(
                                                new Coordinate[] {
                                                    new Coordinate(0, 0),
                                                    new Coordinate(0, 2),
                                                    new Coordinate(2, 2),
                                                    new Coordinate(2, 0),
                                                    new Coordinate(0, 0)
                                                }))));

        Query query = new Query();
        query.setFilter(filter);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds(query);
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(2l, Math.round(bounds.getMaxX()));
        assertEquals(2l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(decodeEPSG(4326), bounds.getCoordinateReferenceSystem()));
    }

    @Test
    public void testEstimatedBounds() throws Exception {
        // enable fast bbox
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds();
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(2l, Math.round(bounds.getMaxX()));
        assertEquals(2l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(decodeEPSG(4326), bounds.getCoordinateReferenceSystem()));
    }

    @Test
    public void testEstimatedBoundsWithQuery() throws Exception {
        // enable fast bbox
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setFilter(filter);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds(query);
        assertEquals(1l, Math.round(bounds.getMinX()));
        assertEquals(1l, Math.round(bounds.getMinY()));
        assertEquals(1l, Math.round(bounds.getMaxX()));
        assertEquals(1l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(decodeEPSG(4326), bounds.getCoordinateReferenceSystem()));
    }

    @Test
    public void testEstimatedBoundsWithLimit() throws Exception {
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);
        super.testBoundsWithLimit();
    }

    @Test
    public void testEstimatedBoundsWithOffset() throws Exception {
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);
        super.testBoundsWithOffset();
    }

    @Test
    public void testSridFirstGeometry() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema(tname("ft3"));
        GeometryDescriptor gd = schema.getGeometryDescriptor();
        assertTrue(areCRSEqual(decodeEPSG(4326), gd.getCoordinateReferenceSystem()));
    }

    @Test
    public void testCrsIsLongitudeFirst() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema(tname("ft3"));
        GeometryDescriptor gd = schema.getGeometryDescriptor();
        assertTrue(areCRSEqual(decodeEPSG(4326), gd.getCoordinateReferenceSystem()));
        // an explicit check to ensure we have a longitude first (XY) axis order.
        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(gd.getCoordinateReferenceSystem()));
    }
}
