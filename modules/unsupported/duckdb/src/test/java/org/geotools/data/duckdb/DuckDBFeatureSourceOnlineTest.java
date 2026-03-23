/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCFeatureSourceOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;

public class DuckDBFeatureSourceOnlineTest extends JDBCFeatureSourceOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new DuckDBTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(AbstractDuckDBDataStoreFactory.READ_ONLY.key, Boolean.FALSE);
        return params;
    }

    @Override
    @Test
    public void testSchema() throws Exception {
        SimpleFeatureType schema = featureSource.getSchema();
        assertEquals(tname("ft1"), schema.getTypeName());
        assertEquals(dataStore.getNamespaceURI(), schema.getName().getNamespaceURI());

        assertEquals(4, schema.getAttributeCount());
        assertNotNull(schema.getDescriptor(aname("geometry")));
        assertNotNull(schema.getDescriptor(aname("intProperty")));
        assertNotNull(schema.getDescriptor(aname("stringProperty")));
        assertNotNull(schema.getDescriptor(aname("doubleProperty")));
    }

    @Override
    @Test
    public void testBounds() throws Exception {
        ReferencedEnvelope bounds = featureSource.getBounds();
        assertEquals(0L, Math.round(bounds.getMinX()));
        assertEquals(0L, Math.round(bounds.getMinY()));
        assertEquals(2L, Math.round(bounds.getMaxX()));
        assertEquals(2L, Math.round(bounds.getMaxY()));
    }

    @Override
    @Test
    public void testBoundsWithQuery() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo filter = ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setFilter(filter);

        ReferencedEnvelope bounds = featureSource.getBounds(query);
        assertEquals(1L, Math.round(bounds.getMinX()));
        assertEquals(1L, Math.round(bounds.getMinY()));
        assertEquals(1L, Math.round(bounds.getMaxX()));
        assertEquals(1L, Math.round(bounds.getMaxY()));
    }

    @Override
    @Test
    public void testBoundsWithLimit() throws Exception {
        Query query = new Query(featureSource.getSchema().getTypeName());
        query.setMaxFeatures(2);
        FilterFactory ff = dataStore.getFilterFactory();
        query.setSortBy(ff.sort(aname("intProperty"), org.geotools.api.filter.sort.SortOrder.ASCENDING));
        ReferencedEnvelope bounds = featureSource.getBounds(query);

        assertEquals(0L, Math.round(bounds.getMinX()));
        assertEquals(0L, Math.round(bounds.getMinY()));
        assertEquals(1L, Math.round(bounds.getMaxX()));
        assertEquals(1L, Math.round(bounds.getMaxY()));
    }

    @Override
    @Test
    public void testBoundsWithOffset() throws Exception {
        Query query = new Query(featureSource.getSchema().getTypeName());
        query.setStartIndex(2);
        FilterFactory ff = dataStore.getFilterFactory();
        query.setSortBy(ff.sort(aname("intProperty"), org.geotools.api.filter.sort.SortOrder.ASCENDING));
        ReferencedEnvelope bounds = featureSource.getBounds(query);

        assertEquals(2L, Math.round(bounds.getMinX()));
        assertEquals(2L, Math.round(bounds.getMinY()));
        assertEquals(2L, Math.round(bounds.getMaxX()));
        assertEquals(2L, Math.round(bounds.getMaxY()));
    }
}
