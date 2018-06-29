/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.spatialite;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @source $URL$ */
public class SpatiaLiteDataStoreAPIOnlineTest extends JDBCDataStoreAPIOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new SpatiaLiteDataStoreAPITestSetup();
    }

    public void testRecreateSchema() throws Exception {
        String featureTypeName = tname("recreated");
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");

        // Build feature type
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(featureTypeName);
        ftb.add(aname("id"), Integer.class);
        ftb.add(aname("name"), String.class);
        ftb.add(aname("the_geom"), Point.class, crs);
        SimpleFeatureType newFT = ftb.buildFeatureType();

        // Crate a schema
        dataStore.createSchema(newFT);
        SimpleFeatureType newSchema = dataStore.getSchema(featureTypeName);
        assertNotNull(newSchema);

        // Delete it
        dataStore.removeSchema(newFT.getTypeName());
        try {
            dataStore.getSchema(featureTypeName);
            fail("Should have thrown an IOException because featureTypeName shouldn't exist");
        } catch (IOException e) {
        }

        // Create the same schema again
        dataStore.createSchema(newFT);
        SimpleFeatureType recreatedSchema = dataStore.getSchema(featureTypeName);
        assertNotNull(recreatedSchema);
    }

    @Override
    public void testCreateSchema() throws Exception {
        super.testCreateSchema();

        String featureTypeName = tname("datatypes");

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(featureTypeName);

        // All Java Classes from
        // org.geotools.jdbc.SQLDialect.registerClassToSqlMappings()
        ftb.add(aname("string"), String.class);
        ftb.add(aname("boolean_class"), Boolean.class);
        ftb.add(aname("boolean"), boolean.class);
        ftb.add(aname("short_class"), Short.class);
        ftb.add(aname("short"), short.class);
        ftb.add(aname("integer_class"), Integer.class);
        ftb.add(aname("int"), int.class);
        ftb.add(aname("long_class"), Long.class);
        ftb.add(aname("long"), long.class);
        ftb.add(aname("float_class"), Float.class);
        ftb.add(aname("float"), float.class);
        ftb.add(aname("double_class"), Double.class);
        ftb.add(aname("double"), double.class);
        ftb.add(aname("bigdecimal"), BigDecimal.class);
        ftb.add(aname("sql_date"), Date.class);
        ftb.add(aname("time"), Time.class);
        ftb.add(aname("java_util_date"), java.util.Date.class);
        ftb.add(aname("timestamp"), Timestamp.class);
        ftb.add(aname("byte_array"), byte[].class);

        SimpleFeatureType newFT = ftb.buildFeatureType();
        dataStore.createSchema(newFT);

        SimpleFeatureType newSchema = dataStore.getSchema(featureTypeName);
        assertNotNull(newSchema);
        assertEquals(19, newSchema.getAttributeCount());
    }

    @Override
    public void testTransactionIsolation() throws Exception {
        // super.testTransactionIsolation();
        // JD: In order to allow multiple connections from the same thread (which this test
        // requires)
        // we need to put the database in read_uncommitted mode, which means transaction isolation
        // can not be achieved
    }

    @Override
    public void testGetFeatureReaderFilterTransaction()
            throws NoSuchElementException, IOException, IllegalAttributeException {
        // super.testGetFeatureReaderFilterTransaction();
    }
}
