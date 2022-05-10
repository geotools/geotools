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
package org.geotools.data.oracle;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.jdbc.JDBCFeatureCollectionOnlineTest;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

public class OracleFeatureCollectionOnlineTest extends JDBCFeatureCollectionOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

    @Test
    public void testReservedWords() throws IOException, CQLException {
        JDBCFeatureStore lsource = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft3"));

        String[] props = {"NUMBER", "DATE"};

        Filter f = CQL.toFilter("DATE > 1");
        Query q = new Query(lsource.getName().getLocalPart(), f, props);
        ContentFeatureCollection lcollection = lsource.getFeatures(q);
        assertEquals(1, lcollection.size());
        List<AttributeDescriptor> desc = lcollection.getSchema().getAttributeDescriptors();
        assertEquals(2, desc.size());
    }
}
