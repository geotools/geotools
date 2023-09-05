/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

/**
 * Test non functionality of PropertyDataStore.
 *
 * @author Jody Garnett, Refractions Research Inc.
 * @author Torben Barsballe (Boundless)
 */
public class PropertyDataStore2Test {
    PropertyDataStore store;
    PropertyDataStore store3d;
    PropertyDataStore sridStore;

    private static final String TARGET_DIR = "./target";

    @Before
    public void setUp() throws Exception {
        File dir = new File(TARGET_DIR, "propertyTestData");
        dir.mkdir();

        File file = new File(dir, "road.properties");
        if (file.exists()) {
            file.delete();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("_=id:Integer,*geom:Geometry,name:String");
            writer.newLine();
            writer.write("fid1=1|LINESTRING(0 0,10 10)|jody");
            writer.newLine();
            writer.write("fid2=2|LINESTRING(20 20,30 30)|brent");
            writer.newLine();
            writer.write("fid3=3|LINESTRING(5 0, 5 10)|dave");
            writer.newLine();
            writer.write("fid4=4|LINESTRING(0 5, 5 0, 10 5, 5 10, 0 5)|justin");
        }
        store = new PropertyDataStore(dir, "propertyTestData");

        // Create a similar data store but with srid in the geometry column
        File dir2 = new File(TARGET_DIR, "propertyTestData2");
        dir2.mkdir();
        File file2 = new File(dir2, "road2.properties");
        if (file2.exists()) {
            file2.delete();
        }
        try (BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2))) {
            writer2.write("_=id:Integer,geom:Geometry:srid=4283");
            writer2.newLine();
            writer2.write("fid1=1|LINESTRING(0 0,10 10)");
            writer2.newLine();
            writer2.write("fid2=2|LINESTRING(20 20,30 30)");
            writer2.newLine();
            writer2.write("fid3=3|LINESTRING(5 0, 5 10)");
            writer2.newLine();
            writer2.write("fid4=4|LINESTRING(0 5, 5 0, 10 5, 5 10, 0 5)");
        }
        sridStore = new PropertyDataStore(dir2, "propertyTestData2");

        // Create a data store with a 3D geometry
        File dir3 = new File(TARGET_DIR, "propertyTestData3");
        dir3.mkdir();
        File file3 = new File(dir3, "road3.properties");
        if (file3.exists()) {
            file3.delete();
        }

        try (BufferedWriter writer3 = new BufferedWriter(new FileWriter(file3))) {
            writer3.write("_=id:Integer,geom:Geometry:srid=4327");
            writer3.newLine();
            writer3.write("fid1=1|LINESTRING(0 0 0,10 10 0)");
            writer3.newLine();
            writer3.write("fid2=2|LINESTRING(20 20 10,30 30 20)");
        }
        store3d = new PropertyDataStore(dir3, "propertyTestData3");
    }

    @After
    public void tearDown() throws Exception {
        File dir = new File(TARGET_DIR, "propertyTestData");
        File[] list = dir.listFiles();
        for (File item : list) {
            item.delete();
        }
        dir.delete();

        dir = new File(TARGET_DIR, "propertyTestData2");
        list = dir.listFiles();
        for (File value : list) {
            value.delete();
        }
        dir.delete();

        dir = new File(TARGET_DIR, "propertyTestData3");
        list = dir.listFiles();
        for (File file : list) {
            file.delete();
        }
        dir.delete();
    }

    /** Test CRS being passed into Geometry user data. */
    @Test
    public void testCRS() throws Exception {
        SimpleFeatureSource road = sridStore.getFeatureSource("road2");
        SimpleFeatureCollection features = road.getFeatures();
        Assert.assertEquals(4, features.size());

        SimpleFeature feature;
        Geometry geom;
        Property prop;
        GeometryType geomType;
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                feature = iterator.next();
                prop = feature.getProperty("geom");
                Assert.assertTrue(prop.getType() instanceof GeometryType);
                geomType = (GeometryType) prop.getType();

                Object val = prop.getValue();
                Assert.assertTrue(val != null && val instanceof Geometry);
                geom = (Geometry) val;

                Object userData = geom.getUserData();
                Assert.assertTrue(
                        userData != null && userData instanceof CoordinateReferenceSystem);
                // ensure the same CRS is passed on to userData for encoding
                Assert.assertEquals(userData, geomType.getCoordinateReferenceSystem());
            }
        }
    }

    @Test
    public void test3D() throws Exception {
        SimpleFeatureSource fs = store3d.getFeatureSource("road3");
        Query q = new Query("road3", Filter.INCLUDE);
        Assert.assertEquals(2, fs.getCount(q));
        ReferencedEnvelope bounds =
                new ReferencedEnvelope3D(
                        0, 30, 0, 30, 0, 20, fs.getSchema().getCoordinateReferenceSystem());
        Assert.assertEquals(bounds, fs.getBounds());
        Assert.assertEquals(bounds, fs.getBounds(q));

        SimpleFeatureCollection features = fs.getFeatures();
        Assert.assertEquals(2, features.size());

        try (SimpleFeatureIterator i = features.features()) {
            for (SimpleFeature feature = i.next(); i.hasNext(); feature = i.next()) {
                Property p = feature.getProperty("geom");
                Assert.assertTrue(p.getType() instanceof GeometryType);
                Assert.assertTrue(p.getValue() instanceof Geometry);
            }
        }
    }

    @Test
    public void testSimple() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource("road");
        SimpleFeatureCollection features = road.getFeatures();

        // assertEquals( 1, features.getFeatureType().getAttributeCount() );
        Assert.assertEquals(4, features.size());
    }

    @Test
    public void testQuery() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource("road");

        Query query = new Query("road", Filter.INCLUDE, new String[] {"name"});

        SimpleFeatureCollection features = road.getFeatures(query);
        Assert.assertEquals(4, features.size());
        // assertEquals( 1, features.getFeatureType().getAttributeCount() );
    }

    @Test
    public void testQueryReproject() throws Exception {
        CoordinateReferenceSystem world = CRS.decode("EPSG:4326"); // world lon/lat
        CoordinateReferenceSystem local = CRS.decode("EPSG:3005"); // british columbia

        SimpleFeatureSource road = store.getFeatureSource("road");
        SimpleFeatureType origionalType = road.getSchema();

        Query query = new Query("road", Filter.INCLUDE, new String[] {"geom", "name"});

        query.setCoordinateSystem(local); // FROM
        query.setCoordinateSystemReproject(world); // TO

        SimpleFeatureCollection features = road.getFeatures(query);
        SimpleFeatureType resultType = features.getSchema();

        Assert.assertNotNull(resultType);
        Assert.assertNotSame(resultType, origionalType);

        Assert.assertEquals(world, resultType.getCoordinateReferenceSystem());

        Assert.assertNotNull(resultType.getGeometryDescriptor());
    }

    @Test
    public void testGetFeaturesFilterSize() throws Exception {
        Filter f = CQL.toFilter("name = 'brent'");
        SimpleFeatureSource features = store.getFeatureSource("road");
        Assert.assertEquals(1, features.getFeatures(f).size());
    }

    @Test
    public void testGetFeaturesFilterBounds() throws Exception {
        Filter f = CQL.toFilter("name = 'brent'");
        SimpleFeatureSource features = store.getFeatureSource("road");
        ReferencedEnvelope envelope = new ReferencedEnvelope(20, 30, 20, 30, null);
        Assert.assertEquals(envelope, features.getFeatures(f).getBounds());
    }

    /** Test query with a start index */
    @Test
    public void testOffset() throws FileNotFoundException, IOException {
        Query query = new Query(Query.ALL);
        query.setStartIndex(3);
        SimpleFeatureSource features = store.getFeatureSource("road");

        SimpleFeatureCollection matches = features.getFeatures(query);

        Assert.assertEquals(1, matches.size());
        Assert.assertEquals(1, features.getCount(query));
    }

    /** Test query with maxFeatures */
    @Test
    public void testLimit() throws FileNotFoundException, IOException {
        Query query = new Query(Query.ALL);
        query.setMaxFeatures(3);
        SimpleFeatureSource features = store.getFeatureSource("road");

        SimpleFeatureCollection matches = features.getFeatures(query);

        Assert.assertEquals(3, matches.size());
        Assert.assertEquals(3, features.getCount(query));
    }

    /** Test query with maxFeatures and startIndex */
    @Test
    public void testLimitOffset() throws FileNotFoundException, IOException {
        Query query = new Query(Query.ALL);
        query.setMaxFeatures(3);
        query.setStartIndex(3);
        SimpleFeatureSource features = store.getFeatureSource("road");

        SimpleFeatureCollection matches = features.getFeatures(query);

        Assert.assertEquals(1, matches.size());
        Assert.assertEquals(1, features.getCount(query));
    }
}
