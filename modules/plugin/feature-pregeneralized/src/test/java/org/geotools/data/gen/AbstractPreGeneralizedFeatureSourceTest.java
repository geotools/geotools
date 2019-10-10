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
import junit.framework.TestCase;
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
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.xml.sax.helpers.NamespaceSupport;

public abstract class AbstractPreGeneralizedFeatureSourceTest extends TestCase {

    static Map<String, PreGeneralizedDataStore> DSMap =
            new HashMap<String, PreGeneralizedDataStore>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestSetup.initialize();
    }

    protected PreGeneralizedDataStore getDataStore(String configName) throws IOException {

        PreGeneralizedDataStore ds = DSMap.get(configName);
        if (ds != null) return ds;

        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos ginfos = null;
        ginfos = provider.getGeneralizationInfos(configName);
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
            assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            double[] distances = new double[] {1, 5, 10, 20, 25};

            for (int i = 0; i < distances.length; i++) {
                System.out.println(distances[i]);
                // subset of the properties, and out of order
                Query query =
                        new Query(
                                "GenStreams", Filter.INCLUDE, new String[] {"CAT_ID", "the_geom"});
                query.getHints().put(Hints.GEOMETRY_DISTANCE, distances[i]);

                // check the collection schema
                SimpleFeatureCollection fc = fs.getFeatures(query);
                SimpleFeatureType schema = fc.getSchema();
                assertEquals(2, schema.getAttributeCount());
                assertEquals("CAT_ID", schema.getDescriptor(0).getLocalName());
                assertEquals("the_geom", schema.getDescriptor(1).getLocalName());

                // grab a feature and check the schema and direct attribute access
                try (SimpleFeatureIterator features = fc.features()) {
                    while (features.hasNext()) {
                        SimpleFeature sf = features.next();
                        SimpleFeatureType sfSchema = sf.getType();
                        assertEquals(2, sfSchema.getAttributeCount());
                        assertEquals("CAT_ID", sfSchema.getDescriptor(0).getLocalName());
                        assertEquals("the_geom", sfSchema.getDescriptor(1).getLocalName());

                        // attributes are correctly mapped to indexes
                        assertTrue(sf.getAttribute(0) instanceof Number);
                        assertTrue(sf.getAttribute(1) instanceof Geometry);
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
            assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query[] queries = new Query[2];
            queries[0] = new Query("GenStreams");
            queries[1] = new Query("GenStreams", filter);

            for (Query q : queries) {

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                int count = fs.getCount(q);

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
                assertTrue(count == fs.getCount(q));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 10.0);
                assertTrue(count == fs.getCount(q));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 20.0);
                assertTrue(count == fs.getCount(q));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 25.0);
                assertTrue(count == fs.getCount(q));
            }
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetBounds(String configName) {

        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query[] queries = new Query[2];
            queries[0] = new Query("GenStreams");
            queries[1] = new Query("GenStreams", filter);

            ReferencedEnvelope env = null;
            ReferencedEnvelope envOrig = fs.getBounds();
            assertTrue(envOrig.isEmpty() == false);

            for (Query q : queries) {

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                env = fs.getBounds(q);
                if (env != null) assertTrue(envOrig.intersects((Envelope) env));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
                env = fs.getBounds(q);
                if (env != null) assertTrue(envOrig.intersects((Envelope) env));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 10.0);
                env = fs.getBounds(q);
                if (env != null) assertTrue(envOrig.intersects((Envelope) env));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 20.0);
                env = fs.getBounds(q);
                if (env != null) assertTrue(envOrig.intersects((Envelope) env));

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 25.0);
                env = fs.getBounds(q);
                if (env != null) assertTrue(envOrig.intersects((Envelope) env));
            }
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testFeatureReader(String configName) {

        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query[] queries = new Query[2];
            queries[0] = new Query("GenStreams");
            queries[1] = new Query("GenStreams", filter, new String[] {"the_geom", "CAT_ID"});

            for (Query q : queries) {

                FeatureReader<SimpleFeatureType, SimpleFeature> reader;
                String typeName;

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                reader = ds.getFeatureReader(q, Transaction.AUTO_COMMIT);
                typeName = reader.getFeatureType().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        reader.getFeatureType()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                while (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    checkPoints(f, 0.0);
                }
                reader.close();

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
                reader = ds.getFeatureReader(q, Transaction.AUTO_COMMIT);
                typeName = reader.getFeatureType().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        reader.getFeatureType()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                while (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    checkPoints(f, 5.0);
                }
                reader.close();

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 10.0);
                reader = ds.getFeatureReader(q, Transaction.AUTO_COMMIT);
                typeName = reader.getFeatureType().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        reader.getFeatureType()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                while (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    checkPoints(f, 10.0);
                }
                reader.close();

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 20.0);
                reader = ds.getFeatureReader(q, Transaction.AUTO_COMMIT);
                typeName = reader.getFeatureType().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        reader.getFeatureType()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                while (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    checkPoints(f, 20.0);
                }
                reader.close();

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 50.0);
                reader = ds.getFeatureReader(q, Transaction.AUTO_COMMIT);
                typeName = reader.getFeatureType().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        reader.getFeatureType()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                while (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    checkPoints(f, 50.0);
                }
                reader.close();
            }
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testFeatureReaderWithoutGeom(String configName) {

        try {

            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query q = new Query("GenStreams", filter, new String[] {"CAT_ID"});

            for (Double distance : new Double[] {1.0, 5.0, 10.0, 20.0, 50.0}) {
                FeatureReader<SimpleFeatureType, SimpleFeature> reader;
                String typeName;

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                reader = ds.getFeatureReader(q, Transaction.AUTO_COMMIT);
                typeName = reader.getFeatureType().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                reader.close();
            }

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetFeatures(String configName) {

        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            Filter filter = CQL.toFilter("CAT_ID = 2");

            Query[] queries = new Query[2];
            queries[0] = new Query("GenStreams");
            queries[1] = new Query("GenStreams", filter, new String[] {"the_geom", "CAT_ID"});

            for (Query q : queries) {

                SimpleFeatureCollection fCollection;
                String typeName;

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        fCollection
                                                .getSchema()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                SimpleFeatureIterator iterator = fCollection.features();
                try {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 0.0);
                    }
                } finally {
                    iterator.close();
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        fCollection
                                                .getSchema()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                iterator = fCollection.features();
                try {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 5.0);
                    }
                } finally {
                    iterator.close();
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 10.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        fCollection
                                                .getSchema()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                iterator = fCollection.features();
                try {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 10.0);
                    }
                } finally {
                    iterator.close();
                }

                q.getHints().put(Hints.GEOMETRY_DISTANCE, 20.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        fCollection
                                                .getSchema()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                iterator = fCollection.features();
                try {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 20.0);
                    }
                } finally {
                    iterator.close();
                }
                q.getHints().put(Hints.GEOMETRY_DISTANCE, 50.0);
                fCollection = fs.getFeatures(q);
                typeName = fCollection.getSchema().getTypeName();
                assertTrue("GenStreams".equals(typeName));
                assertTrue(
                        "the_geom"
                                .equals(
                                        fCollection
                                                .getSchema()
                                                .getGeometryDescriptor()
                                                .getLocalName()));
                iterator = fCollection.features();
                try {
                    while (iterator.hasNext()) {
                        SimpleFeature f = iterator.next();
                        checkPoints(f, 0.0);
                    }
                } finally {
                    iterator.close();
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
            assertTrue(ds == fs.getDataStore());
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetFeatures2(String configName) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            SimpleFeatureCollection fCollection;
            String typeName;

            fCollection = fs.getFeatures();
            typeName = fCollection.getSchema().getTypeName();
            assertTrue("GenStreams".equals(typeName));
            assertTrue(fCollection.size() > 0);
            assertFalse(fCollection.isEmpty());
            SimpleFeatureIterator iterator = fCollection.features();
            try {
                while (iterator.hasNext()) {
                    SimpleFeature f = iterator.next();
                    checkPoints(f, 0.0);
                }
            } finally {
                iterator.close();
            }
            fCollection = fs.getFeatures(Filter.INCLUDE);
            typeName = fCollection.getSchema().getTypeName();
            assertTrue("GenStreams".equals(typeName));
            assertTrue(fCollection.size() > 0);
            assertFalse(fCollection.isEmpty());
            iterator = fCollection.features();
            try {
                while (iterator.hasNext()) {

                    SimpleFeature f = iterator.next();
                    checkPoints(f, 0.0);
                }
                // iterator.remove() no longer provided
            } finally {
                iterator.close();
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
            assertTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_DISTANCE));

            SimpleFeatureCollection fCollection;
            String typeName;

            fCollection = fs.getFeatures();
            typeName = fCollection.getSchema().getTypeName();
            assertTrue("GenStreams".equals(typeName));
            assertTrue(fCollection.size() > 0);
            assertFalse(fCollection.isEmpty());

            Object[] array = fCollection.toArray();
            assertTrue(array.length == fCollection.size());
            assertNotNull(array[0]);
            assertTrue(array[0] instanceof PreGeneralizedSimpleFeature);

            array = fCollection.toArray(new Object[fCollection.size()]);
            assertTrue(array.length == fCollection.size());
            assertNotNull(array[0]);
            assertTrue(array[0] instanceof PreGeneralizedSimpleFeature);

            assertTrue(fCollection.getBounds().equals(fs.getBounds()));
            assertTrue(fCollection.contains(array[0]));

            List<Object> list = new ArrayList<Object>();
            list.add(array[0]);
            list.add(array[1]);
            assertTrue(fCollection.containsAll(list));

            SimpleFeatureCollection subCollection = fCollection.subCollection(Filter.INCLUDE);
            typeName = subCollection.getSchema().getTypeName();
            assertTrue("GenStreams".equals(typeName));
            assertTrue(fCollection.size() == subCollection.size());
            assertTrue(subCollection.contains(array[0]));

            // subCollection = fCollection.subCollection(Filter.EXCLUDE);
            // typeName=subCollection.getSchema().getTypeName();
            // assertTrue("GenStreams".equals(typeName));
            // assertTrue(subCollection.size()==0);
            // assertFalse(subCollection.contains(array[0]));

            // SortBy2 sortBy = new SortByImpl(new
            // AttributeExpressionImpl("CAT_ID"),SortOrder.ASCENDING);
            SortBy sortBy =
                    new SortBy() {
                        public PropertyName getPropertyName() {
                            return new AttributeExpressionImpl("CAT_ID");
                        }

                        public SortOrder getSortOrder() {
                            return SortOrder.ASCENDING;
                        }
                    };
            SimpleFeatureCollection sortedCollection = fCollection.sort(sortBy);
            // null here

            // /typeName=sortedCollection.getSchema().getTypeName();
            // assertTrue("GenStreams".equals(typeName))
            // assertTrue(fCollection.size()==sortedCollection.size());

            final List<Long> catIds = new ArrayList<Long>();

            FeatureVisitor checkSortVisitor =
                    new FeatureVisitor() {

                        public void visit(Feature feature) {
                            SimpleFeature sf = (SimpleFeature) feature;
                            assertTrue(feature instanceof PreGeneralizedSimpleFeature);
                            long catid = (Long) sf.getAttribute("CAT_ID");
                            catIds.add(catid);
                        }
                    };

            try {
                fCollection.accepts(checkSortVisitor, null);
            } catch (Throwable e) {
                Assert.fail();
            }
            assertTrue(catIds.size() == fCollection.size());
            ds.dispose();

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testNameAndInfo(String configName) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            assertTrue("GenStreams".equals(fs.getInfo().getName()));
            assertTrue("GenStreams".equals(fs.getName().getLocalPart()));
            assertNull(fs.getName().getNamespaceURI());
            ds.dispose();

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testQueryCapabilities(String configName, boolean pureShapefile) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            assertTrue(fs.getQueryCapabilities().isOffsetSupported());
            assertTrue(fs.getQueryCapabilities().isReliableFIDSupported());

            PropertyName propertyName =
                    new PropertyName() {
                        public String getPropertyName() {
                            return "CAT_ID";
                        }

                        public Object accept(ExpressionVisitor arg0, Object arg1) {
                            return true;
                        }

                        public Object evaluate(Object arg0) {
                            return arg0;
                        }

                        public <T> T evaluate(Object arg0, Class<T> arg1) {
                            return null;
                        }

                        public NamespaceSupport getNamespaceContext() {
                            return null;
                        }
                    };

            SortOrder so = SortOrder.valueOf("CAT_ID");
            assertTrue(
                    fs.getQueryCapabilities()
                            .supportsSorting(new SortBy[] {new SortByImpl(propertyName, so)}));

            ds.dispose();

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    protected void testGetSchema(String configName) {
        try {
            PreGeneralizedDataStore ds = getDataStore(configName);

            SimpleFeatureSource fs = ds.getFeatureSource("GenStreams");
            assertTrue("GenStreams".equals(fs.getSchema().getTypeName()));
            assertTrue(fs.getSchema().getAttributeCount() == 4);
            assertTrue("the_geom".equals(fs.getSchema().getGeometryDescriptor().getLocalName()));
            assertNotNull(fs.getSchema().getDescriptor("CAT_ID") != null);
            assertNotNull(fs.getSchema().getDescriptor("the_geom") != null);
            assertNotNull(fs.getSchema().getDescriptor("CAT_DESC") != null);
            assertNotNull(fs.getSchema().getDescriptor("ID") != null);
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
        assertTrue(geomIndex >= 0);
        // TODO, Test not possible because of MemoryDS impl.
        // assertTrue(f.getAttribute(geomIndex)==f.getDefaultGeometry());
        assertTrue(f.getAttribute("the_geom") == f.getDefaultGeometry());
        Integer numPoints = TestSetup.POINTMAP.get(distance).get(f.getID());
        return numPoints == ((Geometry) f.getDefaultGeometry()).getNumPoints();
    }
}
