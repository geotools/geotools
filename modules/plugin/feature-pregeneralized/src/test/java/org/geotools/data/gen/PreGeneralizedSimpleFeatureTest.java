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

package org.geotools.data.gen;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.gen.info.GeneralizationInfosProvider;
import org.geotools.data.gen.info.GeneralizationInfosProviderImpl;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

public class PreGeneralizedSimpleFeatureTest {

    @Before
    public void setUp() throws Exception {
        TestSetup.initialize();
    }

    @Test
    public void testNoHint() {
        Assert.assertFalse(testHint(null));
    }

    @Test
    public void testDinstanceHint() {
        Assert.assertTrue(testHint(Hints.GEOMETRY_DISTANCE));
    }

    @Test
    public void testSimplificationHint() {
        Assert.assertTrue(testHint(Hints.GEOMETRY_SIMPLIFICATION));
    }

    private boolean testHint(Key hintKey) {
        String baseName = "streams";
        String typeName = "GenStreams";
        try {
            // Get base features
            DataStore baseDs = TestSetup.REPOSITORY.dataStore("dsStreams");
            SimpleFeatureSource fs = baseDs.getFeatureSource(baseName);
            SimpleFeatureCollection fColl = fs.getFeatures();
            Geometry original = null;
            try (SimpleFeatureIterator iterator = fColl.features()) {
                if (iterator.hasNext()) {
                    original = (Geometry) iterator.next().getDefaultGeometry();
                }
            }
            double width = original.getEnvelope().getEnvelopeInternal().getWidth();

            // Get generalized features
            GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
            GeneralizationInfos ginfos =
                    provider.getGeneralizationInfos("src/test/resources/geninfo_horizontal.xml");
            PreGeneralizedDataStore ds = new PreGeneralizedDataStore(ginfos, TestSetup.REPOSITORY);
            fs = ds.getFeatureSource(typeName);
            Query query = new Query();
            if (hintKey != null) {
                Hints hints = new Hints(Hints.GEOMETRY_SIMPLIFICATION, width / 2);
                query.setHints(hints);
            }
            Geometry simplified = null;
            fColl = fs.getFeatures(query);
            try (SimpleFeatureIterator iterator = fColl.features()) {
                if (iterator.hasNext())
                    simplified = (Geometry) iterator.next().getDefaultGeometry();
            }
            return original.getNumPoints() > simplified.getNumPoints();
        } catch (IOException ex) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ex);
            Assert.fail();
            return false;
        }
    }

    @Test
    public void testSimpleFeatureBasics() {

        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos ginfos = null;
        PreGeneralizedDataStore ds = null;
        SimpleFeatureCollection fCollection = null;
        String typeName = "GenStreams";
        try {
            ginfos = provider.getGeneralizationInfos("src/test/resources/geninfo_vertical.xml");
            ds = new PreGeneralizedDataStore(ginfos, TestSetup.REPOSITORY);
            fCollection = ds.getFeatureSource(typeName).getFeatures();
        } catch (IOException ex) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ex);
            Assert.fail();
        }

        try (SimpleFeatureIterator it = fCollection.features()) {
            SimpleFeature feature1 = it.next();

            SimpleFeature feature2 = it.next();
            // SimpleFeature feature3 = it.next();

            Assert.assertTrue(feature1 instanceof PreGeneralizedSimpleFeature);

            for (int i = 0; i < feature1.getType().getAttributeDescriptors().size(); i++) {
                Object value1 = feature1.getAttribute(i);
                Object value2 =
                        feature1.getAttribute(
                                feature1.getType().getAttributeDescriptors().get(i).getLocalName());
                Assert.assertSame(value1, value2);
            }

            Assert.assertNotNull(feature1.getAttribute("CAT_ID"));
            Assert.assertNotNull(feature1.getAttribute(new NameImpl("CAT_ID")));
            Assert.assertEquals(4, feature1.getAttributeCount());

            assertNotEquals(feature1, feature2);
            Assert.assertEquals(feature1, feature1);
            assertNotEquals(feature1.hashCode(), feature2.hashCode());
            Assert.assertEquals(feature1.hashCode(), feature1.hashCode());

            assertNotEquals(feature1.getID(), feature2.getID());

            Assert.assertEquals(feature1.getBounds(), feature1.getBounds());
            assertNotEquals(feature1.getBounds(), feature2.getBounds());

            Assert.assertEquals(feature1.getDefaultGeometry(), feature1.getDefaultGeometry());
            assertNotEquals(feature1.getDefaultGeometry(), feature2.getDefaultGeometry());

            Assert.assertEquals(
                    "the_geom", feature1.getDefaultGeometryProperty().getName().getLocalPart());
            Assert.assertEquals(
                    feature1.getDefaultGeometryProperty().getName(),
                    feature2.getDefaultGeometryProperty().getName());
            assertNotEquals(
                    feature1.getDefaultGeometryProperty(), feature2.getDefaultGeometryProperty());

            Assert.assertEquals(feature1.getFeatureType(), feature2.getFeatureType());
            Assert.assertEquals("GenStreams", feature2.getFeatureType().getName().getLocalPart());

            Assert.assertEquals(feature1.getIdentifier(), feature1.getIdentifier());
            assertNotEquals(feature1.getIdentifier(), feature2.getIdentifier());

            Assert.assertEquals(4, feature1.getProperties().size());
            Assert.assertEquals(
                    feature1.getProperty("CAT_ID").getValue(), feature1.getAttribute("CAT_ID"));
            Assert.assertEquals(
                    feature1.getProperty("the_geom").getValue(), feature1.getAttribute("the_geom"));
            // assertFalse(feature1.getProperty("CAT_ID").getValue().equals(feature2.getAttribute
            // ("CAT_ID"
            // )));
            assertNotEquals(
                    feature1.getProperty("the_geom").getValue(), feature2.getAttribute("the_geom"));

            Assert.assertTrue(
                    feature1.getProperties("CAT_ID").contains(feature1.getProperty("CAT_ID")));
            Assert.assertFalse(
                    feature2.getProperties("the_geom").contains(feature1.getProperty("the_geom")));

            Assert.assertTrue(
                    feature1.getProperties(new NameImpl("CAT_ID"))
                            .contains(feature1.getProperty("CAT_ID")));
            Assert.assertFalse(
                    feature2.getProperties(new NameImpl("the_geom"))
                            .contains(feature1.getProperty("the_geom")));

            Assert.assertEquals(feature1.getType(), fCollection.getSchema());
            Assert.assertEquals(feature1.getValue(), feature1.getProperties());

            Assert.assertNotNull(feature1.getUserData());
        } finally {
            ds.dispose();
        }
    }

    @Test
    public void testUnsupported() {

        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos ginfos = null;
        PreGeneralizedDataStore ds = null;
        SimpleFeatureCollection fCollection = null;
        String typeName = "GenStreams";
        try {
            ginfos = provider.getGeneralizationInfos("src/test/resources/geninfo_vertical.xml");
            ds = new PreGeneralizedDataStore(ginfos, TestSetup.REPOSITORY);
            fCollection = ds.getFeatureSource(typeName).getFeatures();
        } catch (IOException ex) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ex);
            Assert.fail();
        }

        try (SimpleFeatureIterator it = fCollection.features()) {
            SimpleFeature feature1 = it.next();

            Assert.assertTrue(feature1 instanceof PreGeneralizedSimpleFeature);

            boolean error = true;
            try {
                feature1.setAttribute(0, null);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }

            try {
                feature1.setAttribute("CAT_ID", null);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }

            try {
                feature1.setAttribute(new NameImpl("CAT_ID"), null);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }

            try {
                feature1.setAttributes((List<Object>) null);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }

            try {
                feature1.setAttributes(new Object[0]);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }

            try {
                feature1.setDefaultGeometry(null);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }

            try {
                feature1.setDefaultGeometryProperty(null);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }

            try {
                feature1.setValue(null);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }

            try {
                feature1.setValue((Object) null);
            } catch (UnsupportedOperationException ex) {
                error = false;
            }
            if (error) {
                Assert.fail();
            }
        } finally {
            ds.dispose();
        }
    }
}
