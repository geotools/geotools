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
package org.geotools.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the ArrayFeatureReader class
 *
 * @author jones
 */
public class ArrayFeatureReaderTest {
    private CollectionFeatureReader arrayReader;
    private CollectionFeatureReader collectionReader;
    private CollectionFeatureReader featureCollectionReader;
    private SimpleFeatureType type;
    private SimpleFeature[] features;

    @Before
    public void setUp() throws Exception {
        type = DataUtilities.createType("TestType", "geom:Geometry");
        features =
                new SimpleFeature[] {
                    SimpleFeatureBuilder.build(type, new Object[] {null}, "f1"),
                    SimpleFeatureBuilder.build(type, new Object[] {null}, "f2"),
                    SimpleFeatureBuilder.build(type, new Object[] {null}, "f3"),
                    SimpleFeatureBuilder.build(type, new Object[] {null}, "f4"),
                    SimpleFeatureBuilder.build(type, new Object[] {null}, "f5"),
                    SimpleFeatureBuilder.build(type, new Object[] {null}, "f6")
                };

        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        List<SimpleFeature> list = Arrays.asList(features);
        collection.addAll(list);
        arrayReader = new CollectionFeatureReader(features);
        collectionReader = new CollectionFeatureReader(list, type);
        featureCollectionReader =
                new CollectionFeatureReader((SimpleFeatureCollection) collection, type);
    }

    /** Test method for 'org.geotools.data.ArrayFeatureReader.getFeatureType()' */
    @Test
    public void testGetFeatureType() {
        Assert.assertEquals(type, arrayReader.getFeatureType());
        Assert.assertEquals(type, collectionReader.getFeatureType());
        Assert.assertEquals(type, featureCollectionReader.getFeatureType());
    }

    /** Test method for 'org.geotools.data.ArrayFeatureReader.next()' */
    @Test
    public void testNext() throws Exception {
        Assert.assertEquals(features[0], arrayReader.next());
        Assert.assertEquals(features[1], arrayReader.next());
        Assert.assertEquals(features[2], arrayReader.next());
        Assert.assertEquals(features[3], arrayReader.next());
        Assert.assertEquals(features[4], arrayReader.next());
        Assert.assertEquals(features[5], arrayReader.next());

        Assert.assertEquals(features[0], collectionReader.next());
        Assert.assertEquals(features[1], collectionReader.next());
        Assert.assertEquals(features[2], collectionReader.next());
        Assert.assertEquals(features[3], collectionReader.next());
        Assert.assertEquals(features[4], collectionReader.next());
        Assert.assertEquals(features[5], collectionReader.next());

        Assert.assertEquals(features[0], featureCollectionReader.next());
        Assert.assertEquals(features[1], featureCollectionReader.next());
        Assert.assertEquals(features[2], featureCollectionReader.next());
        Assert.assertEquals(features[3], featureCollectionReader.next());
        Assert.assertEquals(features[4], featureCollectionReader.next());
        Assert.assertEquals(features[5], featureCollectionReader.next());
    }

    /** Test method for 'org.geotools.data.ArrayFeatureReader.hasNext()' */
    @Test
    public void testHasNext() throws Exception {
        testHasNext(arrayReader);
        testHasNext(collectionReader);
        testHasNext(featureCollectionReader);
    }

    private void testHasNext(FeatureReader<SimpleFeatureType, SimpleFeature> arrayReader)
            throws IOException, IllegalAttributeException {
        Assert.assertTrue(arrayReader.hasNext());
        arrayReader.next();
        Assert.assertTrue(arrayReader.hasNext());
        arrayReader.next();
        Assert.assertTrue(arrayReader.hasNext());
        arrayReader.next();
        Assert.assertTrue(arrayReader.hasNext());
        arrayReader.next();
        Assert.assertTrue(arrayReader.hasNext());
        arrayReader.next();
        Assert.assertTrue(arrayReader.hasNext());
        arrayReader.next();
        Assert.assertFalse(arrayReader.hasNext());
    }

    /** Test method for 'org.geotools.data.ArrayFeatureReader.close()' */
    @Test
    public void testClose() throws Exception {
        arrayReader.close();
        Assert.assertFalse(arrayReader.hasNext());

        try {
            arrayReader.next();
            Assert.fail();
        } catch (Exception e) {
            // good
        }

        collectionReader.close();
        Assert.assertFalse(collectionReader.hasNext());

        try {
            collectionReader.next();
            Assert.fail();
        } catch (Exception e) {
            // good
        }

        featureCollectionReader.close();
        Assert.assertFalse(featureCollectionReader.hasNext());

        try {
            featureCollectionReader.next();
            Assert.fail();
        } catch (Exception e) {
            // good
        }
    }
}
