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
package org.geotools.data.shapefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.GeoTools;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * @version $Id$
 * @author Ian Schneider
 */
public class ShapefileQuadTreeReadWriteTest extends TestCaseSupport {
    final String[] files = {
        "shapes/statepop.shp",
        "shapes/polygontest.shp",
        "shapes/pointtest.shp",
        "shapes/holeTouchEdge.shp",
        "shapes/streams.shp"
    };

    boolean readStarted = false;

    Exception exception = null;

    @Test
    public void testAll() throws Throwable {
        for (String file : files) {
            test(file);
        }
    }

    @Test
    public void testReadOutside() throws Exception {
        File f = copyShapefiles("shapes/statepop.shp");
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();
        ShapefileDataStore ds =
                (ShapefileDataStore) createDataStore(fac, f.toURI().toURL(), true);
        Query q = new Query(ds.getTypeNames()[0]);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        q.setFilter(ff.bbox("the_geom", -62, -61, 23, 22, null));
        assertEquals(0, ds.getFeatureSource().getFeatures(q).size());
        ds.dispose();
    }

    @Test
    public void testWriteTwice() throws Exception {
        copyShapefiles("shapes/stream.shp");
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();
        DataStore s1 = createDataStore(fac, TestData.url(TestCaseSupport.class, "shapes/stream.shp"), true);
        String typeName = s1.getTypeNames()[0];
        SimpleFeatureSource source = s1.getFeatureSource(typeName);
        SimpleFeatureType type = source.getSchema();
        SimpleFeatureCollection one = source.getFeatures();

        ShapefileDataStoreFactory maker = new ShapefileDataStoreFactory();

        doubleWrite(type, one, getTempFile(), maker, false);
        doubleWrite(type, one, getTempFile(), maker, true);
        s1.dispose();
    }

    private DataStore createDataStore(ShapefileDataStoreFactory fac, URL url, boolean memoryMapped) throws IOException {
        Map<String, Serializable> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, url);
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
        DataStore createDataStore = fac.createDataStore(params);
        return createDataStore;
    }

    private void doubleWrite(
            SimpleFeatureType type,
            SimpleFeatureCollection one,
            File tmp,
            ShapefileDataStoreFactory maker,
            boolean memorymapped)
            throws IOException, MalformedURLException {
        DataStore s = createDataStore(maker, tmp.toURI().toURL(), memorymapped);

        s.createSchema(type);
        SimpleFeatureStore store = (SimpleFeatureStore) s.getFeatureSource(s.getTypeNames()[0]);

        assertEquals(0, store.getCount(Query.ALL));
        store.addFeatures(one);
        assertEquals(one.size(), store.getCount(Query.ALL));
        store.addFeatures(one);

        assertEquals(one.size() * 2, store.getCount(Query.ALL));
        s.dispose();
    }

    void test(String f) throws Exception {
        File file = copyShapefiles(f); // Work on File rather than URL from
        // JAR.
        DataStore s =
                createDataStore(new ShapefileDataStoreFactory(), file.toURI().toURL(), true);
        String typeName = s.getTypeNames()[0];
        SimpleFeatureSource source = s.getFeatureSource(typeName);
        SimpleFeatureType type = source.getSchema();
        SimpleFeatureCollection one = source.getFeatures();

        ShapefileDataStoreFactory maker = new ShapefileDataStoreFactory();
        test(type, one, getTempFile(), maker, false);
        test(type, one, getTempFile(), maker, true);
        s.dispose();
    }

    private void test(
            SimpleFeatureType type,
            SimpleFeatureCollection one,
            File tmp,
            ShapefileDataStoreFactory maker,
            boolean memorymapped)
            throws IOException, MalformedURLException, Exception {
        DataStore s = createDataStore(maker, tmp.toURI().toURL(), memorymapped);

        s.createSchema(type);

        SimpleFeatureStore store = (SimpleFeatureStore) s.getFeatureSource(s.getTypeNames()[0]);

        store.addFeatures(one);
        s.dispose();

        s = createDataStore(new ShapefileDataStoreFactory(), tmp.toURI().toURL(), true);
        String typeName = s.getTypeNames()[0];

        SimpleFeatureCollection two = s.getFeatureSource(typeName).getFeatures();

        compare(one.features(), two.features());
        s.dispose();
    }

    static void compare(SimpleFeatureIterator fs1, SimpleFeatureIterator fs2) throws Exception {
        try (fs1;
                fs2) {
            while (fs1.hasNext()) {
                SimpleFeature f1 = fs1.next();
                SimpleFeature f2 = fs2.next();

                compare(f1, f2);
            }
        }
    }

    static void compare(SimpleFeature f1, SimpleFeature f2) throws Exception {
        if (f1.getAttributeCount() != f2.getAttributeCount()) {
            throw new Exception("Unequal number of attributes");
        }

        for (int i = 0; i < f1.getAttributeCount(); i++) {
            Object att1 = f1.getAttribute(i);
            Object att2 = f2.getAttribute(i);

            if (att1 instanceof Geometry && att2 instanceof Geometry) {
                Geometry g1 = (Geometry) att1;
                Geometry g2 = (Geometry) att2;
                g1.normalize();
                g2.normalize();

                if (!g1.equalsExact(g2)) {
                    throw new Exception("Different geometries (" + i + "):\n" + g1 + "\n" + g2);
                }
            } else {
                if (!att1.equals(att2)) {
                    throw new Exception("Different attribute (" + i + "): [" + att1 + "] - [" + att2 + "]");
                }
            }
        }
    }

    /** Test optimized getBounds(). Testing when filter is a bbox filter and a fidfilter */
    @Test
    public void testGetBoundsQuery() throws Exception {
        File file = copyShapefiles("shapes/streams.shp");

        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
        ShapefileDataStore ds = (ShapefileDataStore) fac.createDataStore(params);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());

        FeatureId featureId = ff.featureId("streams.84");
        Id filter = ff.id(Collections.singleton(featureId));
        SimpleFeature first = DataUtilities.first(ds.getFeatureSource().getFeatures(filter));
        ReferencedEnvelope bounds = ReferencedEnvelope.reference(first.getBounds());

        FeatureId id = featureId;
        filter = ff.id(Collections.singleton(id));

        Query query = new Query(ds.getTypeNames()[0], filter);

        Envelope result = ds.getFeatureSource().getBounds(query);

        assertTrue(result == null || result.equals(bounds));
        ds.dispose();
    }
}
