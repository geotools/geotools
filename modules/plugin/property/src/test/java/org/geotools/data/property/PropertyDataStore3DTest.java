/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

/** Makes sure PropertyDatastore can read and write 3d data */
public class PropertyDataStore3DTest {
    PropertyDataStore store;

    static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Before
    public void setUp() throws Exception {
        File dir = new File("target", "threeDimensionsTestData");
        dir.mkdir();

        File file = new File(dir, "full3d.properties");
        if (file.exists()) {
            file.delete();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("_=name:String,geom:Geometry:srid=7415");
            writer.newLine();
            writer.write(
                    "full3d.poly=poly|POLYGON((94000 471000 12, 94001 471000 12, 94001 471001 12, 94000 471001 12, 94000 471000 12))");
            writer.newLine();
            writer.write("full3d.point=point|POINT(94330 471816 16)");
            writer.newLine();
            writer.write("full3d.ls=line|LINESTRING(94330 471816 16, 194319 471814 17)");
            writer.newLine();
        }

        store = new PropertyDataStore(dir);
    }

    @After
    public void tearDown() throws Exception {
        File dir = new File("target", "threeDimensionsTestData");
        for (File file : dir.listFiles()) {
            file.delete();
        }
        dir.delete();
    }

    @Test
    public void testRead3D() throws Exception {
        String[] names = store.getTypeNames();
        assertEquals(1, names.length);
        assertEquals("full3d", names[0]);
        SimpleFeature poly = getOneFeature("full3d.poly");
        assertEquals(3, CoordinateSequences.coordinateDimension((Geometry) poly.getDefaultGeometry()));
        SimpleFeature point = getOneFeature("full3d.point");
        assertEquals(3, CoordinateSequences.coordinateDimension((Geometry) point.getDefaultGeometry()));
        SimpleFeature line = getOneFeature("full3d.ls");
        assertEquals(3, CoordinateSequences.coordinateDimension((Geometry) line.getDefaultGeometry()));
    }

    @Test
    public void testInsert3DPoint() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        final String featureId = "full3d.newPoint";
        SimpleFeature feature = SimpleFeatureBuilder.build(
                fs.getSchema(), new Object[] {"New Point", new WKTReader().read("POINT(1 2 3)")}, featureId);
        List<FeatureId> ids = fs.addFeatures(DataUtilities.collection(feature));
        assertEquals(1, ids.size());

        // read back and check
        SimpleFeature point = getOneFeature(ids.get(0).getID());
        final Point geom = (Point) point.getDefaultGeometry();
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
        assertEquals(3, geom.getCoordinate().getOrdinate(2), 0d);
    }

    @Test
    public void testUpdate3DPoint() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        fs.modifyFeatures("geom", new WKTReader().read("POINT(1 2 3)"), newIdFilter("full3d.point"));

        // read back and check
        SimpleFeature point = getOneFeature("full3d.point");
        final Point geom = (Point) point.getDefaultGeometry();
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
        assertEquals(3, geom.getCoordinate().getOrdinate(2), 0d);
    }

    @Test
    public void testRemove3DPoint() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        fs.removeFeatures(newIdFilter("full3d.point"));

        // read back and check
        getOneFeature("full3d.ls");
        getOneFeature("full3d.poly");
        assertEquals(2, fs.getCount(Query.ALL));
    }

    @Test
    public void testInsert3DPolygon() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        final String featureId = "full3d.newPolygon";
        SimpleFeature feature = SimpleFeatureBuilder.build(
                fs.getSchema(),
                new Object[] {
                    "New Polygon",
                    new WKTReader()
                            .read(
                                    "POLYGON((94000 471000 16, 94001 471000 16, 94001 471001 16, 94000 471001 16, 94000 471000 16))")
                },
                featureId);
        List<FeatureId> ids = fs.addFeatures(DataUtilities.collection(feature));
        assertEquals(1, ids.size());

        // read back and check
        SimpleFeature point = getOneFeature(ids.get(0).getID());
        final Polygon geom = (Polygon) point.getDefaultGeometry();
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
        assertEquals(16, geom.getCoordinate().getOrdinate(2), 0d);
    }

    @Test
    public void testUpdate3DPolygon() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        fs.modifyFeatures(
                "geom",
                new WKTReader()
                        .read(
                                "POLYGON((94000 471000 16, 94001 471000 16, 94001 471001 16, 94000 471001 16, 94000 471000 16))"),
                newIdFilter("full3d.poly"));

        // read back and check
        SimpleFeature point = getOneFeature("full3d.poly");
        final Polygon geom = (Polygon) point.getDefaultGeometry();
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
        assertEquals(16, geom.getCoordinate().getOrdinate(2), 0d);
    }

    @Test
    public void testRemove3DPolygon() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        fs.removeFeatures(newIdFilter("full3d.poly"));

        // read back and check
        getOneFeature("full3d.ls");
        getOneFeature("full3d.point");
        assertEquals(2, fs.getCount(Query.ALL));
    }

    @Test
    public void testInsert3DLine() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        final String featureId = "full3d.newLine";
        SimpleFeature feature = SimpleFeatureBuilder.build(
                fs.getSchema(),
                new Object[] {"New Polygon", new WKTReader().read("LINESTRING(94330 471816 30, 194319 471814 30)")},
                featureId);
        List<FeatureId> ids = fs.addFeatures(DataUtilities.collection(feature));
        assertEquals(1, ids.size());

        // read back and check
        SimpleFeature point = getOneFeature(ids.get(0).getID());
        final LineString geom = (LineString) point.getDefaultGeometry();
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
        assertEquals(30, geom.getCoordinate().getOrdinate(2), 0d);
    }

    @Test
    public void testUpdate3DLine() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        fs.modifyFeatures(
                "geom",
                new WKTReader().read("LINESTRING(94330 471816 30, 194319 471814 30)"),
                newIdFilter("full3d.ls"));

        // read back and check
        SimpleFeature point = getOneFeature("full3d.ls");
        final LineString geom = (LineString) point.getDefaultGeometry();
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
        assertEquals(30, geom.getCoordinate().getOrdinate(2), 0d);
    }

    @Test
    public void testRemove3DLine() throws Exception {
        // write out new feature
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource("full3d");
        fs.removeFeatures(newIdFilter("full3d.ls"));

        // read back and check
        getOneFeature("full3d.poly");
        getOneFeature("full3d.point");
        assertEquals(2, fs.getCount(Query.ALL));
    }

    @Test
    public void testBounds() throws Exception {
        ContentFeatureSource fs = store.getFeatureSource("full3d");
        ContentFeatureCollection fc = fs.getFeatures(Filter.INCLUDE);
        // used to throw an exception here
        ReferencedEnvelope3D bounds = (ReferencedEnvelope3D) fc.getBounds();
        assertNotNull(bounds);
        assertFalse(bounds.isEmpty());
        assertEquals(CRS.decode("EPSG:7415"), bounds.getCoordinateReferenceSystem());
        assertEquals(12, bounds.getMinZ(), 0d);
        assertEquals(17, bounds.getMaxZ(), 0d);
    }

    @Test
    public void testEmptyBounds() throws Exception {
        ContentFeatureSource fs = store.getFeatureSource("full3d");
        ContentFeatureCollection fc = fs.getFeatures(Filter.EXCLUDE);
        // used to throw an exception here
        ReferencedEnvelope bounds = fc.getBounds();
        assertNotNull(bounds);
        assertTrue(bounds.isEmpty());
        assertEquals(CRS.decode("EPSG:7415"), bounds.getCoordinateReferenceSystem());
    }

    private SimpleFeature getOneFeature(String featureId) throws IOException {
        ContentFeatureCollection fc = store.getFeatureSource("full3d").getFeatures(newIdFilter(featureId));
        try (SimpleFeatureIterator fi = fc.features()) {
            assertTrue(fi.hasNext());
            SimpleFeature result = fi.next();
            assertFalse(fi.hasNext());
            return result;
        }
    }

    private Id newIdFilter(String featureId) {
        return ff.id(ff.featureId(featureId));
    }
}
