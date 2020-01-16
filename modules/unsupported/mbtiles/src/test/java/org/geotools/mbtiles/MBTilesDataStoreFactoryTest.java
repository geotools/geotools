/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbtiles;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.feature.NameImpl;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.util.URLs;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

public class MBTilesDataStoreFactoryTest {

    @Test
    public void testFactory() throws IOException {
        String namespaceURI = "http://geotools.org/mbtiles";
        File file =
                URLs.urlToFile(MBTilesFileVectorTileTest.class.getResource("datatypes.mbtiles"));
        Map<String, Serializable> params = new HashMap<>();
        params.put(MBTilesDataStoreFactory.DBTYPE.key, "mbtiles");
        params.put(MBTilesDataStoreFactory.DATABASE.key, file);
        params.put(JDBCDataStoreFactory.NAMESPACE.key, namespaceURI);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertNotNull(store);
        assertThat(store, Matchers.instanceOf(MBTilesDataStore.class));
        assertThat(store.getTypeNames(), arrayContaining("datatypes"));
        SimpleFeatureType schema = store.getSchema("datatypes");
        NameImpl qualifiedName = new NameImpl(namespaceURI, "datatypes");
        assertThat(schema.getName(), equalTo(qualifiedName));
        SimpleFeatureType schemaFromQualified = store.getSchema(qualifiedName);
        assertThat(schema, equalTo(schemaFromQualified));
    }

    @Test
    public void testNoDbType() throws IOException {
        String namespaceURI = "http://geotools.org/mbtiles";
        File file =
                URLs.urlToFile(MBTilesFileVectorTileTest.class.getResource("datatypes.mbtiles"));
        Map<String, Serializable> params = new HashMap<>();
        params.put(MBTilesDataStoreFactory.DATABASE.key, file);
        params.put(JDBCDataStoreFactory.NAMESPACE.key, namespaceURI);

        // static check
        assertFalse(new MBTilesDataStoreFactory().canProcess(params));

        // verify on the SPI lookup path too
        DataStore store = DataStoreFinder.getDataStore(params);
        assertNull(store);
    }
}
