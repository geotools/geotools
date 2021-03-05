/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.Test;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Test FilteredFeatureReader for conformance.
 *
 * @author Jody Garnett, Refractions Research
 */
public class FilteringFeatureReaderTest extends DataTestCase {
    FeatureReader<SimpleFeatureType, SimpleFeature> roadReader;
    FeatureReader<SimpleFeatureType, SimpleFeature> riverReader;

    /*
     * @see TestCase#setUp()
     */
    @Override
    public void init() throws Exception {
        super.init();
        roadReader = DataUtilities.reader(roadFeatures);
        riverReader = DataUtilities.reader(riverFeatures);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        roadReader.close();
        roadReader = null;
        riverReader.close();
        riverReader = null;
    }

    @Test
    public void testFilteringFeatureReaderALL() throws IOException {

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                new FilteringFeatureReader<>(DataUtilities.reader(roadFeatures), Filter.EXCLUDE)) {
            assertFalse(reader.hasNext());
        }
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                new FilteringFeatureReader<>(DataUtilities.reader(roadFeatures), Filter.EXCLUDE)) {
            assertEquals(0, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                new FilteringFeatureReader<>(DataUtilities.reader(roadFeatures), Filter.EXCLUDE)) {
            assertContents(new SimpleFeature[0], reader);
        }
    }

    @Test
    public void testFilteringFeatureReaderNONE() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                new FilteringFeatureReader<>(DataUtilities.reader(roadFeatures), Filter.INCLUDE)) {
            assertTrue(reader.hasNext());
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                DataUtilities.reader(roadFeatures)) {
            assertEquals(roadFeatures.length, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                new FilteringFeatureReader<>(DataUtilities.reader(roadFeatures), Filter.INCLUDE)) {
            assertEquals(roadFeatures.length, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                new FilteringFeatureReader<>(DataUtilities.reader(roadFeatures), Filter.INCLUDE)) {
            assertContents(roadFeatures, reader);
        }
    }

    void assertContents(
            SimpleFeature expected[], FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        assertNotNull(reader);
        assertNotNull(expected);
        SimpleFeature feature;
        int count = 0;
        try {
            for (SimpleFeature simpleFeature : expected) {
                assertTrue(reader.hasNext());
                feature = reader.next();
                assertNotNull(feature);
                assertEquals(simpleFeature, feature);
                count++;
            }
            assertFalse(reader.hasNext());
        } catch (NoSuchElementException e) {
            // bad dog!
            throw new DataSourceException("hasNext() lied to me at:" + count, e);
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("next() could not understand feature at:" + count, e);
        } finally {
            reader.close();
        }
    }
}
