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

package org.geotools.data.gen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Repository;
import org.geotools.data.Transaction;
import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.gen.info.GeneralizationInfosProvider;
import org.geotools.data.gen.info.GeneralizationInfosProviderImpl;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortOrder;
import org.xml.sax.helpers.NamespaceSupport;

public abstract class AbstractPreGeneralizedFeatureSourceTest {

    static Map<String, PreGeneralizedDataStore> DSMap = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        TestSetup.initialize();
    }

    protected PreGeneralizedDataStore getDataStore(String configName) throws IOException {

        PreGeneralizedDataStore ds = DSMap.get(configName);
        if (ds != null) return ds;

        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos ginfos = provider.getGeneralizationInfos(configName);
        ds = new PreGeneralizedDataStore(ginfos, getRepository());
        DSMap.put(configName, ds);
        return ds;
    }

    protected Repository getRepository() {
        return TestSetup.REPOSITORY;
    }

    protected void testPropertySelection(String configName) {

        try {

            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            double[] distances = {1, 5, 10, 20, 25};

            for (double distance : distances) {
                // System.out.println(distance);
                // subset of the properties, and out of order
                Query query =
                        new Query(
                                "GenStreams", Filter.INCLUDE, new String[] {"CAT_ID", "the_geom"});
                query.getHints().put(Hints.GEOMETRY_DISTANCE, distance);

                // check the collection schema
                SimpleFeatureCollection fc = fs.getFeatures(query);
                SimpleFeatureType schema = fc.getSchema();
                Assert.assertEquals(2, schema.getAttributeCount());
                Assert.assertEquals("CAT_ID", schema.getDescriptor(0).getLocalName());
                Assert.assertEquals("the_geom", schema.getDescriptor(1).getLocalName());

                // grab a feature and check the schema and direct attribute access
                try (SimpleFeatureIterator features = fc.features()) {
                    while (features.hasNext()) {
                        SimpleFeature sf = features.next();
                        SimpleFeatureType sfSchema = sf.getType();
                        Assert.assertEquals(2, sfSchema.getAttributeCount());
                        Assert.assertEquals("CAT_ID", sfSchema.getDescriptor(0).getLocalName());
                        Assert.assertEquals("the_geom", sfSchema.getDescriptor(1).getLocalName());

                        // attributes are correctly mapped to indexes
                        Assert.assertTrue(sf.getAttribute(0) instanceof Number);
                        Assert.assertTrue(sf.getAttribute(1) instanceof Geometry);
                    }
                }
            }
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetCount(String configName) {

        try {

            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query[] queries = new Query[2];
            queries[0] = new Query("GenStreams");
            queries[1] = new Query("GenStreams", filter);

            for (Query q : queries) {

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                int count = fs.getCount(q);

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
                Assert.assertEquals(count, fs.getCount(q));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 10.0);
                Assert.assertEquals(count, fs.getCount(q));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 20.0);
                Assert.assertEquals(count, fs.getCount(q));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 25.0);
                Assert.assertEquals(count, fs.getCount(q));
            }
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetBounds(String configName) {

        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query[] queries = new Query[2];
            queries[0] = new Query("GenStreams");
            queries[1] = new Query("GenStreams", filter);

            ReferencedEnvelope env = null;
            ReferencedEnvelope envOrig = fs.getBounds();
            Assert.assertFalse(envOrig.isEmpty());

            for (Query q : queries) {

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                env = fs.getBounds(q);
                if (env != null) Assert.assertTrue(envOrig.intersects((Envelope) env));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
                env = fs.getBounds(q);
                if (env != null) Assert.assertTrue(envOrig.intersects((Envelope) env));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 10.0);
                env = fs.getBounds(q);
                if (env != null) Assert.assertTrue(envOrig.intersects((Envelope) env));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 20.0);
                env = fs.getBounds(q);
                if (env != null) Assert.assertTrue(envOrig.intersects((Envelope) env));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 25.0);
                env = fs.getBounds(q);
                if (env != null) Assert.assertTrue(envOrig.intersects((Envelope) env));
            }
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testFeatureReader(String configName) {

        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query[] queries = new Query[2];
            queries[0] = new Query("GenStreams");
            queries[1] = new Query("GenStreams", filter, new String[] {"the_geom", "CAT_ID"});

            for (Query q : queries) {

                String typeName;

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        ds.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
                    typeName = reader.getFeatureType().getTypeName();
                    Assert.assertEquals("GenStreams", typeName);
                    Assert.assertEquals(
                            "the_geom",
                            reader.getFeatureType().getGeometryDescriptor().getLocalName());
                    while (reader.hasNext()) {
                        SimpleFeature f = reader.next();
                        checkPoints(f, 0.0);
                    }
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        ds.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
                    typeName = reader.getFeatureType().getTypeName();
                    Assert.assertEquals("GenStreams", typeName);
                    Assert.assertEquals(
                            "the_geom",
                            reader.getFeatureType().getGeometryDescriptor().getLocalName());
                    while (reader.hasNext()) {
                        SimpleFeature f = reader.next();
                        checkPoints(f, 5.0);
                    }
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 10.0);
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        ds.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
                    typeName = reader.getFeatureType().getTypeName();
                    Assert.assertEquals("GenStreams", typeName);
                    Assert.assertEquals(
                            "the_geom",
                            reader.getFeatureType().getGeometryDescriptor().getLocalName());
                    while (reader.hasNext()) {
                        SimpleFeature f = reader.next();
                        checkPoints(f, 10.0);
                    }
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 20.0);
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        ds.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
                    typeName = reader.getFeatureType().getTypeName();
                    Assert.assertEquals("GenStreams", typeName);
                    Assert.assertEquals(
                            "the_geom",
                            reader.getFeatureType().getGeometryDescriptor().getLocalName());
                    while (reader.hasNext()) {
                        SimpleFeature f = reader.next();
                        checkPoints(f, 20.0);
                    }
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 50.0);
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        ds.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
                    typeName = reader.getFeatureType().getTypeName();
                    Assert.assertEquals("GenStreams", typeName);
                    Assert.assertEquals(
                            "the_geom",
                            reader.getFeatureType().getGeometryDescriptor().getLocalName());
                    while (reader.hasNext()) {
                        SimpleFeature f = reader.next();
                        checkPoints(f, 50.0);
                    }
                }
            }
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testFeatureReaderWithoutGeom(String configName) {

        try {

            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query q = new Query("GenStreams", filter, new String[] {"CAT_ID"});

            for (Double distance : new Double[] {1.0, 5.0, 10.0, 20.0, 50.0}) {
                q.getHints().put(Hints.GEOMETRY_DISTANCE, distance);
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        ds.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
                    String typeName = reader.getFeatureType().getTypeName();
                    Assert.assertEquals("GenStreams", typeName);
                }
            }

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetFeatures(String configName) {

        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query[] queries = new Query[2];
            queries[0] = new Query("GenStreams");
            queries[1] = new Query("GenStreams", filter, new String[] {"the_geom", "CAT_ID"});

            for (Query q : queries) {

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                SimpleFeatureCollection fCollection = fs.getFeatures(q);
                String typeName = fCollection.getSchema().getTypeName();
                Assert.assertEquals("GenStreams", typeName);
                Assert.assertEquals(
                        "the_geom", fCollection.getSchema().getGeometryDescriptor().getLocalName());
                try (SimpleFeatureIterator iterator = fCollection.features()) {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 0.0);
                    }
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                Assert.assertEquals("GenStreams", typeName);
                Assert.assertEquals(
                        "the_geom", fCollection.getSchema().getGeometryDescriptor().getLocalName());
                try (SimpleFeatureIterator iterator = fCollection.features()) {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 5.0);
                    }
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 10.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                Assert.assertEquals("GenStreams", typeName);
                Assert.assertEquals(
                        "the_geom", fCollection.getSchema().getGeometryDescriptor().getLocalName());
                try (SimpleFeatureIterator iterator = fCollection.features()) {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 10.0);
                    }
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 20.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                Assert.assertEquals("GenStreams", typeName);
                Assert.assertEquals(
                        "the_geom", fCollection.getSchema().getGeometryDescriptor().getLocalName());
                try (SimpleFeatureIterator iterator = fCollection.features()) {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 20.0);
                    }
                }
                q.getHints().put(Hints.GEOMETRY_DISTANCE, 50.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                Assert.assertEquals("GenStreams", typeName);
                Assert.assertEquals(
                        "the_geom", fCollection.getSchema().getGeometryDescriptor().getLocalName());
                try (SimpleFeatureIterator iterator = fCollection.features()) {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 0.0);
                    }
                }
            }
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetDataStore(String configName) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);
            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertSame(ds, fs.getDataStore());
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetFeatures2(String configName) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            SimpleFeatureCollection fCollection = fs.getFeatures();
            String typeName = fCollection.getSchema().getTypeName();
            Assert.assertEquals("GenStreams", typeName);
            Assert.assertTrue(fCollection.size() > 0);
            Assert.assertFalse(fCollection.isEmpty());
            try (SimpleFeatureIterator iterator = fCollection.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature f = iterator.next();
                    checkPoints(f, 0.0);
                }
            }
            fCollection = fs.getFeatures(Filter.INCLUDE);
            typeName = fCollection.getSchema().getTypeName();
            Assert.assertEquals("GenStreams", typeName);
            Assert.assertTrue(fCollection.size() > 0);
            Assert.assertFalse(fCollection.isEmpty());
            try (SimpleFeatureIterator iterator = fCollection.features()) {
                while (iterator.hasNext()) {

                    SimpleFeature f = iterator.next();
                    checkPoints(f, 0.0);
                }
                // iterator.remove() no longer provided
            }
            ds.dispose();

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetFeatures3(String configName) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            SimpleFeatureCollection fCollection = fs.getFeatures();
            String typeName = fCollection.getSchema().getTypeName();
            Assert.assertEquals("GenStreams", typeName);
            Assert.assertTrue(fCollection.size() > 0);
            Assert.assertFalse(fCollection.isEmpty());

            Object[] array = fCollection.toArray();
            Assert.assertEquals(array.length, fCollection.size());
            Assert.assertNotNull(array[0]);
            Assert.assertTrue(array[0] instanceof PreGeneralizedSimpleFeature);

            array = fCollection.toArray(new Object[fCollection.size()]);
            Assert.assertEquals(array.length, fCollection.size());
            Assert.assertNotNull(array[0]);
            Assert.assertTrue(array[0] instanceof PreGeneralizedSimpleFeature);

            Assert.assertEquals(fCollection.getBounds(), fs.getBounds());
            Assert.assertTrue(fCollection.contains(array[0]));

            List<Object> list = new ArrayList<>();
            list.add(array[0]);
            list.add(array[1]);
            Assert.assertTrue(fCollection.containsAll(list));

            SimpleFeatureCollection subCollection = fCollection.subCollection(Filter.INCLUDE);
            typeName = subCollection.getSchema().getTypeName();
            Assert.assertEquals("GenStreams", typeName);
            Assert.assertEquals(fCollection.size(), subCollection.size());
            Assert.assertTrue(subCollection.contains(array[0]));

            final List<Long> catIds = new ArrayList<>();
            FeatureVisitor checkSortVisitor =
                    feature -> {
                        SimpleFeature sf = (SimpleFeature) feature;
                        Assert.assertTrue(feature instanceof PreGeneralizedSimpleFeature);
                        long catid = (Long) sf.getAttribute("CAT_ID");
                        catIds.add(catid);
                    };

            try {
                fCollection.accepts(checkSortVisitor, null);
            } catch (Throwable e) {
                Assert.fail();
            }
            Assert.assertEquals(catIds.size(), fCollection.size());
            ds.dispose();

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testNameAndInfo(String configName) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertEquals("GenStreams", fs.getInfo().getName());
            Assert.assertEquals("GenStreams", fs.getName().getLocalPart());
            Assert.assertNull(fs.getName().getNamespaceURI());
            ds.dispose();

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testQueryCapabilities(String configName, boolean pureShapefile) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertTrue(fs.getQueryCapabilities().isOffsetSupported());
            Assert.assertTrue(fs.getQueryCapabilities().isReliableFIDSupported());

            PropertyName propertyName =
                    new PropertyName() {
                        @Override
                        public String getPropertyName() {
                            return "CAT_ID";
                        }

                        @Override
                        public Object accept(ExpressionVisitor arg0, Object arg1) {
                            return true;
                        }

                        @Override
                        public Object evaluate(Object arg0) {
                            return arg0;
                        }

                        @Override
                        public <T> T evaluate(Object arg0, Class<T> arg1) {
                            return null;
                        }

                        @Override
                        public NamespaceSupport getNamespaceContext() {
                            return null;
                        }
                    };

            SortOrder so = SortOrder.valueOf("CAT_ID");
            Assert.assertTrue(
                    fs.getQueryCapabilities().supportsSorting(new SortByImpl(propertyName, so)));

            ds.dispose();

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetSchema(String configName) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            Assert.assertEquals("GenStreams", fs.getSchema().getTypeName());
            Assert.assertEquals(4, fs.getSchema().getAttributeCount());
            Assert.assertEquals("the_geom", fs.getSchema().getGeometryDescriptor().getLocalName());
            Assert.assertNotNull(fs.getSchema().getDescriptor("CAT_ID") != null);
            Assert.assertNotNull(fs.getSchema().getDescriptor("the_geom") != null);
            Assert.assertNotNull(fs.getSchema().getDescriptor("CAT_DESC") != null);
            Assert.assertNotNull(fs.getSchema().getDescriptor("ID") != null);
            ds.dispose();

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected boolean checkPoints(SimpleFeature f, double distance) {
        int geomIndex = -1;
        for (int i = 0; i < f.getFeatureType().getAttributeDescriptors().size(); i++) {
            if ("the_geom"
                    .equals(f.getFeatureType().getAttributeDescriptors().get(i).getLocalName())) {
                geomIndex = i;
                break;
            }
        }
        Assert.assertTrue(geomIndex >= 0);
        // TODO, Test not possible because of MemoryDS impl.
        // assertTrue(f.getAttribute(geomIndex)==f.getDefaultGeometry());
        Assert.assertSame(f.getAttribute("the_geom"), f.getDefaultGeometry());
        Integer numPoints = TestSetup.POINTMAP.get(distance).get(f.getID());
        return numPoints == ((Geometry) f.getDefaultGeometry()).getNumPoints();
    }
}
