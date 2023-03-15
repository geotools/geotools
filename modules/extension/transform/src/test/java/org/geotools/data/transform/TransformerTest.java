/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.data.Query;
import org.junit.Test;
import org.opengis.filter.sort.SortOrder;

public class TransformerTest extends AbstractTransformTest {

    @Test
    public void testTransformedSortBy() throws Exception {
        TransformFeatureSource transformedSource = (TransformFeatureSource) transformWithRename();

        Query query = new Query(Query.ALL);
        query.setSortBy(FF.sort("name", SortOrder.DESCENDING));

        Query transformedQuery = transformedSource.transformer.transformQuery(query);
        assertNotNull(transformedQuery);
        assertNotNull(transformedQuery.getSortBy());
        assertEquals(1, transformedQuery.getSortBy().length);
        assertEquals("state_name", transformedQuery.getSortBy()[0].getPropertyName().toString());
    }

    @Test
    public void testTransformedPaging() throws Exception {
        TransformFeatureSource transformedSource = (TransformFeatureSource) transformWithRename();

        Query query = new Query(Query.ALL);
        query.setStartIndex(10);
        query.setMaxFeatures(5);

        Query transformedQuery = transformedSource.transformer.transformQuery(query);
        assertNotNull(transformedQuery);
        assertNotNull(transformedQuery.getStartIndex());
        assertEquals(10, transformedQuery.getStartIndex().intValue());
        assertEquals(5, transformedQuery.getMaxFeatures());
    }

    @Test
    public void testTransformFids() throws Exception {
        TransformFeatureSource transformedSource = (TransformFeatureSource) transformWithRename();

        Query query = new Query(Query.ALL);
        query.setFilter(FF.id(FF.featureId("usa.1")));

        Query transformedQuery = transformedSource.transformer.transformQuery(query);
        assertNotNull(transformedQuery);
        assertNotNull(transformedQuery.getFilter());
        assertEquals(FF.id(FF.featureId("states.1")), transformedQuery.getFilter());
    }
}
