/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.joining;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.data.complex.DataAccessMappingFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.helpers.NamespaceSupport;

public class JoiningNestedAttributeMappingTest {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testPrimeNestedSourceIteratorOnlyReadsAheadOnce() throws Exception {
        JoiningNestedAttributeMapping mapping = newMapping();
        Method method = JoiningNestedAttributeMapping.class.getDeclaredMethod(
                "primeNestedSourceIterator", DataAccessMappingFeatureIterator.class);
        method.setAccessible(true);

        try (DataAccessMappingFeatureIterator iterator = Mockito.mock(DataAccessMappingFeatureIterator.class)) {
            Mockito.when(iterator.hasNext()).thenReturn(true);

            boolean hasRows = (Boolean) method.invoke(mapping, iterator);

            assertTrue(hasRows);
            Mockito.verify(iterator, Mockito.times(1)).hasNext();
            Mockito.verify(iterator, Mockito.never()).next();
        }
    }

    @Test
    public void testResolveForeignIdsFallsBackToJoinMetadata() throws Exception {
        JoiningNestedAttributeMapping mapping = newMapping();
        JoiningQuery query = new JoiningQuery();
        JoiningQuery.QueryJoin join = new JoiningQuery.QueryJoin();
        join.getIds().add("id1");
        join.getIds().add("id2");
        query.setQueryJoins(Collections.singletonList(join));

        Method method = JoiningNestedAttributeMapping.class.getDeclaredMethod(
                "resolveForeignIds", DataAccessMappingFeatureIterator.class, JoiningQuery.class);
        method.setAccessible(true);

        try (DataAccessMappingFeatureIterator iterator = Mockito.mock(DataAccessMappingFeatureIterator.class)) {
            Mockito.when(iterator.getForeignIdsFromSourceSchema()).thenReturn(Collections.emptyList());

            @SuppressWarnings("unchecked")
            List<Expression> foreignIds = (List<Expression>) method.invoke(mapping, iterator, query);

            assertEquals(2, foreignIds.size());
            assertEquals("FOREIGN_ID_0_0", ((PropertyName) foreignIds.get(0)).getPropertyName());
            assertEquals("FOREIGN_ID_0_1", ((PropertyName) foreignIds.get(1)).getPropertyName());
        }
    }

    private static JoiningNestedAttributeMapping newMapping() throws Exception {
        return new JoiningNestedAttributeMapping(
                Expression.NIL,
                FF.property("parent"),
                null,
                false,
                Collections.emptyMap(),
                FF.literal("nested"),
                null,
                new NamespaceSupport());
    }
}
