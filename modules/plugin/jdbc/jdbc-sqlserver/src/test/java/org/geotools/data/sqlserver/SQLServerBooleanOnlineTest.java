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
package org.geotools.data.sqlserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.FeatureStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.DataUtilities;
import org.geotools.jdbc.JDBCBooleanOnlineTest;
import org.geotools.jdbc.JDBCBooleanTestSetup;
import org.junit.Test;

public class SQLServerBooleanOnlineTest extends JDBCBooleanOnlineTest {

    @Override
    protected JDBCBooleanTestSetup createTestSetup() {
        return new SQLServerBooleanTestSetup();
    }

    @Test
    public void testInsertBoolean() throws Exception {
        try {
            dataStore.setExposePrimaryKeyColumns(true);
            SimpleFeatureType ft = dataStore.getSchema(tname("b"));
            SimpleFeature falseFeat = DataUtilities.createFeature(ft, "id=3|false");
            FeatureStore<SimpleFeatureType, SimpleFeature> fs =
                    (FeatureStore<SimpleFeatureType, SimpleFeature>) dataStore.getFeatureSource("b");
            List<FeatureId> featIds = fs.addFeatures(DataUtilities.collection(falseFeat));
            assertEquals(1, featIds.size());
            assertEquals("b.3", featIds.get(0).getID());
        } catch (IOException io) {
            fail("Inserting boolean value failed with: " + io.getMessage());
        }
    }
}
