/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.junit.Test;

/**
 * Tests data modification when the expose primary key flag is raised
 *
 * @author Andrea Aime - OpenGeo
 */
public abstract class JDBCFeatureStoreExposePkOnlineTest extends JDBCFeatureStoreOnlineTest {

    @Override
    protected void connect() throws Exception {
        super.connect();
        featureStore.setExposePrimaryKeyColumns(true);
    }

    @Test
    public void testModifyExposedPk() throws IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("stringProperty")), ff.literal("zero"), false);
        featureStore.modifyFeatures(
                new Name[] {new NameImpl(aname("stringProperty")), new NameImpl(aname("id"))},
                new Object[] {"foo", 123},
                filter);

        PropertyIsEqualTo idFilter = ff.equal(ff.property(aname("id")), ff.literal(0), false);
        SimpleFeatureCollection features = featureStore.getFeatures(idFilter);
        try (SimpleFeatureIterator i = features.features()) {
            assertTrue(i.hasNext());

            while (i.hasNext()) {
                SimpleFeature feature = i.next();
                // this has been updated
                assertEquals("foo", feature.getAttribute(aname("stringProperty")));
                // the pk did not
                assertEquals(0, ((Number) feature.getAttribute(aname("id"))).intValue());
            }
        }
    }
}
