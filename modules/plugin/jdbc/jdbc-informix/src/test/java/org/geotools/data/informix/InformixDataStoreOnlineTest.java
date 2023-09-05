/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Types;
import java.util.function.Consumer;
import org.apache.commons.lang3.RandomStringUtils;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;

public class InformixDataStoreOnlineTest extends JDBCDataStoreOnlineTest {
    @Override
    protected JDBCTestSetup createTestSetup() {
        return new InformixTestSetup();
    }

    @Override
    public void testCreateSchemaWithConstraints() throws Exception {
        // Informix does not complain if the string is too long, so we cannot run this test
    }

    @Override
    public void testCreateSchemaWithNativeTypename() throws Exception {
        assertLargeText(b -> b.userData(JDBCDataStore.JDBC_NATIVE_TYPENAME, getCLOBTypeName()));
    }

    @Override
    public void testCreateSchemaWithNativeType() throws Exception {
        assertLargeText(b -> b.userData(JDBCDataStore.JDBC_NATIVE_TYPE, Types.CLOB));
    }

    // Largely copied from JDBCDataStoreOnlineTest, could be refactored quite easily
    private void assertLargeText(Consumer<SimpleFeatureTypeBuilder> stringCustomizer)
            throws FactoryException, IOException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        String typeName = tname("ft2");
        builder.setName(typeName);
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.nillable(false).add(aname("intProperty"), Integer.class);
        // database can process this name (also tried going from Type.CLOB to a name,
        // but for example postgresql does have a mapping for it
        String stringProperty = aname("stringProperty");
        stringCustomizer.accept(builder);
        builder.add(stringProperty, String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        // 32768 bytes is the maximum length of a quoted string in Informix, including the NULL byte
        // - so, only 32767 characters
        String largeString = RandomStringUtils.random(32767, true, false);
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> w =
                dataStore.getFeatureWriter(typeName, Transaction.AUTO_COMMIT)) {
            w.hasNext();

            SimpleFeature f = w.next();
            f.setAttribute(1, 123);
            f.setAttribute(2, largeString);
            w.write();
        }

        // table was just created, it only has one feature inside, no need for a filter
        SimpleFeatureCollection fc = dataStore.getFeatureSource(typeName).getFeatures();
        SimpleFeature test = DataUtilities.first(fc);
        assertEquals(largeString, test.getAttribute(stringProperty));
    }
}
