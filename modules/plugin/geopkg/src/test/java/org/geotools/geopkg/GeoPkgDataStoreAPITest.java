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
package org.geotools.geopkg;

import static org.junit.Assert.*;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeoPkgDataStoreAPITest {

    DataStore dataStore = null;

    @Rule public TemporaryFolder tmp = new TemporaryFolder(new File("target"));

    @Before
    public void setUp() throws Exception {
        Map<String, Serializable> map = new HashMap<>();
        map.put(GeoPkgDataStoreFactory.DBTYPE.key, "geopkg");
        map.put(GeoPkgDataStoreFactory.DATABASE.key, "foo.gpkg");

        GeoPkgDataStoreFactory factory = new GeoPkgDataStoreFactory();
        factory.setBaseDirectory(tmp.getRoot());
        dataStore = factory.createDataStore(map);
    }

    @Test
    public void testCreateDataTypes() throws Exception {
        String featureTypeName = "datatypes";

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(featureTypeName);

        // All Java Classes from
        // org.geotools.jdbc.SQLDialect.registerClassToSqlMappings()
        ftb.add("string", String.class);
        ftb.add("boolean_class", Boolean.class);
        ftb.add("boolean", boolean.class);
        ftb.add("short_class", Short.class);
        ftb.add("short", short.class);
        ftb.add("integer_class", Integer.class);
        ftb.add("int", int.class);
        ftb.add("long_class", Long.class);
        ftb.add("long", long.class);
        ftb.add("float_class", Float.class);
        ftb.add("float", float.class);
        ftb.add("double_class", Double.class);
        ftb.add("double", double.class);
        ftb.add("bigdecimal", BigDecimal.class);
        ftb.add("sql_date", Date.class);
        ftb.add("time", Time.class);
        ftb.add("java_util_date", java.util.Date.class);
        ftb.add("timestamp", Timestamp.class);
        ftb.add("byte_array", byte[].class);

        ftb.add("geometry", Point.class, CRS.decode("EPSG:4326", true));

        SimpleFeatureType newFT = ftb.buildFeatureType();
        dataStore.createSchema(newFT);

        SimpleFeatureType newSchema = dataStore.getSchema(featureTypeName);
        assertNotNull(newSchema);
        assertEquals(20, newSchema.getAttributeCount());
    }

    @After
    public void tearDown() {
        dataStore.dispose();
    }
}
