/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Collections;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.data.postgis.PostGISDialect;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.PrimaryKey;
import org.junit.Test;
import org.mockito.Mockito;
import org.opengis.feature.simple.SimpleFeatureType;

public class JoiningJDBCFeatureSourceTest {

    @Test
    public void testMultipleIds() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        SimpleFeatureType origType = builder.buildFeatureType();

        JoiningQuery query = new JoiningQuery();
        JoiningQuery.QueryJoin join = new JoiningQuery.QueryJoin();
        join.getIds().add("one");
        join.getIds().add("two");
        query.setQueryJoins(Collections.singletonList(join));

        JDBCDataStore mockStore = Mockito.mock(JDBCDataStore.class);
        ContentEntry mockEntry = Mockito.mock(ContentEntry.class);
        Mockito.when(mockStore.getPrimaryKey(origType))
                .thenReturn(new PrimaryKey(null, Collections.emptyList()));

        Mockito.when(mockStore.getSQLDialect()).thenReturn(new PostGISDialect(mockStore));
        Mockito.when(mockEntry.getDataStore()).thenReturn(mockStore);
        JoiningJDBCFeatureSource source =
                new JoiningJDBCFeatureSource(new JDBCFeatureSource(mockEntry, null));
        SimpleFeatureType type = source.getFeatureType(origType, query);
        assertNotNull(type);
        assertEquals("FOREIGN_ID_0_0", type.getDescriptor(0).getName().getLocalPart());
        assertEquals("FOREIGN_ID_0_1", type.getDescriptor(1).getName().getLocalPart());
    }

    @Test
    public void testSimpleFeatureTypeNoPk() throws IOException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");

        JoiningQuery query = new JoiningQuery();
        JoiningQuery.QueryJoin join = new JoiningQuery.QueryJoin();
        join.getIds().add("one");
        join.getIds().add("two");
        query.setQueryJoins(Collections.singletonList(join));

        JDBCDataStore mockStore = Mockito.mock(JDBCDataStore.class);
        ContentEntry mockEntry = Mockito.mock(ContentEntry.class);

        Mockito.when(mockStore.getSQLDialect()).thenReturn(new PostGISDialect(mockStore));
        Mockito.when(mockEntry.getDataStore()).thenReturn(mockStore);
        JoiningJDBCFeatureSource source =
                new JoiningJDBCFeatureSource(new JDBCFeatureSource(mockEntry, null));

        assertNull(source.getPrimaryKey());
    }
}
