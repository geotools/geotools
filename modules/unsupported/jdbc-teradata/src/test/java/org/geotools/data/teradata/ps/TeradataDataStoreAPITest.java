/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata.ps;

import java.io.IOException;
import java.util.Collections;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.teradata.TeradataDataStoreAPITestSetup;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCDataStoreAPITest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.TestData;
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.Set;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

public class TeradataDataStoreAPITest extends JDBCDataStoreAPITest {

    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new TeradataDataStoreAPITestSetup(new TeradataPSTestSetup());
    }

    public void testBigLine() throws Exception {
        try {
            TestData td = new TestData(10);
            td.build();

            /*
             * SimpleFeatureBuilder.build(roadType, new Object[] { new
             * Integer(1), line(new int[] { 1, 1, 2, 2, 4, 2, 5, 1 }), "r1", },
             * ROAD + "." + (initialFidValue));
             */
            // FeatureReader<SimpleFeatureType, SimpleFeature> reader =
            // DataUtilities.reader(new SimpleFeature[] { td.newRoad, });
            SimpleFeatureStore road = (SimpleFeatureStore) dataStore
                    .getFeatureSource(tname("road"));

            int[] points = new int[200 * 2 * 4];
            for (int i = 0; i < 200; i++) {
                points[i * 2 * 4] = 1;
                points[i * 2 * 4 + 1] = 1;
                points[i * 2 * 4 + 2] = 2;
                points[i * 2 * 4 + 3] = 2;
                points[i * 2 * 4 + 4] = 4;
                points[i * 2 * 4 + 5] = 2;
                points[i * 2 * 4 + 6] = 5;
                points[i * 2 * 4 + 7] = 1;
            }
            SimpleFeature newRoad = SimpleFeatureBuilder.build(td.roadType, new Object[] {
                    new Integer(20), td.line(points), "r20" }, "20");
            FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities
                    .reader(new SimpleFeature[] { newRoad });
            road.addFeatures(DataUtilities.collection(reader));

            road = (SimpleFeatureStore) dataStore.getFeatureSource(tname("road"));
            SimpleFeatureCollection results = road.getFeatures();
            SimpleFeatureIterator features = results.features();
            assertTrue(features.hasNext());
            assertEquals(200 * 4, ((Geometry) features.next().getDefaultGeometry()).getNumPoints());
            results.close(features);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testGetFeaturesWriterAdd() throws IOException, IllegalAttributeException {
        // TODO Auto-generated method stub
        super.testGetFeaturesWriterAdd();
    }

    @Override
    public void testGetFeatureWriterConcurrency() throws Exception {
        // Teradata will lock indefinitely, won't throw an exception
    }

    public void testTransactionIsolation() throws Exception {
        // TODO implement writing
    }
}
