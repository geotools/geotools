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
package org.geotools.data.oracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCGeometryOnlineTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

public class OracleGeometryOnlineTest extends JDBCGeometryOnlineTest {

    OracleGeometryTestSetup testSetup;

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        testSetup = new OracleGeometryTestSetup();
        return testSetup;
    }

    @Override
    public void testLinearRing() throws Exception {
        assertEquals(LineString.class, checkGeometryType(LinearRing.class));
    }

    @Test
    public void testInsertEmptyGeometry() throws Exception {
        ContentFeatureSource source = dataStore.getFeatureSource("COLA_MARKETS_CS");
        if (!(source instanceof SimpleFeatureStore)) {
            fail("store not writable");
        }
        GeometryFactory gf = new GeometryFactory();
        ArrayList<SimpleFeature> list = new ArrayList<>();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(source.getSchema());

        builder.add("empty point");
        builder.add(gf.createPoint((Coordinate) null));
        SimpleFeature f = builder.buildFeature(null);
        list.add(f);
        builder.add("empty line");
        builder.add(gf.createLineString((CoordinateSequence) null));
        f = builder.buildFeature(null);
        list.add(f);
        builder.add("empty polygon");
        builder.add(gf.createPolygon(null, null));
        f = builder.buildFeature(null);
        list.add(f);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = DataUtilities.collection(list);
        SimpleFeatureStore store = (SimpleFeatureStore) source;

        try (Transaction transaction = new DefaultTransaction("create")) {
            store.setTransaction(transaction);
            // GEOT-724 https://osgeo-org.atlassian.net/browse/GEOT-724
            // throws exception here
            store.addFeatures(collection);

            transaction.commit();
        }
    }

    @Test
    public void testComplexGeometryFallback() throws Exception {
        try (SimpleFeatureIterator fi =
                dataStore.getFeatureSource("COLA_MARKETS_CS").getFeatures().features()) {
            assertTrue(fi.hasNext());
            SimpleFeature sf = fi.next();
            assertNotNull(sf.getDefaultGeometry());
            Geometry expected = new WKTReader().read("POLYGON((6 4, 12 4, 12 12, 6 12, 6 4))");
            assertTrue(expected.equalsTopo((Geometry) sf.getDefaultGeometry()));
        }
    }

    @Test
    public void testGeometryMetadataTable() throws Exception {
        testSetup.setupGeometryColumns(dataStore);

        GeometryDescriptor gd = dataStore.getFeatureSource("GTMETA").getSchema().getGeometryDescriptor();
        assertEquals(Point.class, gd.getType().getBinding());
        assertEquals(4269, (int) CRS.lookupEpsgCode(gd.getCoordinateReferenceSystem(), false));
    }
}
