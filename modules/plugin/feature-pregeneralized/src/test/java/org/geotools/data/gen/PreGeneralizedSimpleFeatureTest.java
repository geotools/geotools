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

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.gen.info.GeneralizationInfosProvider;
import org.geotools.data.gen.info.GeneralizationInfosProviderImpl;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.NameImpl;
import org.junit.Assert;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

public class PreGeneralizedSimpleFeatureTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestSetup.initialize();
    }

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
            ex.printStackTrace();
            Assert.fail();
        }

        Iterator<SimpleFeature> it = fCollection.iterator();
        SimpleFeature feature1 = it.next();
        SimpleFeature feature2 = it.next();
        // SimpleFeature feature3 = it.next();

        assertTrue(feature1 instanceof PreGeneralizedSimpleFeature);

        for (int i = 0; i < feature1.getType().getAttributeDescriptors().size(); i++) {
            Object value1 = feature1.getAttribute(i);
            Object value2 = feature1.getAttribute(feature1.getType().getAttributeDescriptors().get(
                    i).getLocalName());
            assertTrue(value1 == value2);
        }

        assertNotNull(feature1.getAttribute("CAT_ID"));
        assertNotNull(feature1.getAttribute(new NameImpl("CAT_ID")));
        assertTrue(feature1.getAttributeCount() == 4);

        assertFalse(feature1.equals(feature2));
        assertTrue(feature1.equals(feature1));
        assertFalse(feature1.hashCode() == feature2.hashCode());
        assertTrue(feature1.hashCode() == feature1.hashCode());

        assertFalse(feature1.getID().equals(feature2.getID()));

        assertTrue(feature1.getBounds().equals(feature1.getBounds()));
        assertFalse(feature1.getBounds().equals(feature2.getBounds()));

        assertTrue(feature1.getDefaultGeometry().equals(feature1.getDefaultGeometry()));
        assertFalse(feature1.getDefaultGeometry().equals(feature2.getDefaultGeometry()));

        assertTrue(feature1.getDefaultGeometryProperty().getName().getLocalPart()
                .equals("the_geom"));
        assertTrue(feature1.getDefaultGeometryProperty().getName().equals(
                feature2.getDefaultGeometryProperty().getName()));
        assertFalse(feature1.getDefaultGeometryProperty().equals(
                feature2.getDefaultGeometryProperty()));

        assertTrue(feature1.getFeatureType().equals(feature2.getFeatureType()));
        assertTrue(feature2.getFeatureType().getName().getLocalPart().equals("GenStreams"));

        assertTrue(feature1.getIdentifier().equals(feature1.getIdentifier()));
        assertFalse(feature1.getIdentifier().equals(feature2.getIdentifier()));

        assertTrue(feature1.getProperties().size() == 4);
        assertTrue(feature1.getProperty("CAT_ID").getValue()
                .equals(feature1.getAttribute("CAT_ID")));
        assertTrue(feature1.getProperty("the_geom").getValue().equals(
                feature1.getAttribute("the_geom")));
        // assertFalse(feature1.getProperty("CAT_ID").getValue().equals(feature2.getAttribute("CAT_ID"
        // )));
        assertFalse(feature1.getProperty("the_geom").getValue().equals(
                feature2.getAttribute("the_geom")));

        assertTrue(feature1.getProperties("CAT_ID").contains(feature1.getProperty("CAT_ID")));
        assertFalse(feature2.getProperties("the_geom").contains(feature1.getProperty("the_geom")));

        assertTrue(feature1.getProperties(new NameImpl("CAT_ID")).contains(
                feature1.getProperty("CAT_ID")));
        assertFalse(feature2.getProperties(new NameImpl("the_geom")).contains(
                feature1.getProperty("the_geom")));

        assertTrue(feature1.getType().equals(fCollection.getSchema()));
        assertTrue(feature1.getValue().equals(feature1.getProperties()));

        assertNotNull(feature1.getUserData());
        ds.dispose();
    }

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
            ex.printStackTrace();
            Assert.fail();
        }

        Iterator<SimpleFeature> it = fCollection.iterator();
        SimpleFeature feature1 = it.next();

        assertTrue(feature1 instanceof PreGeneralizedSimpleFeature);

        boolean error;

        error = true;
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
            feature1.setValue((Collection<Property>) null);
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

        ds.dispose();
    }

}
