/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.net.URL;
import java.util.logging.Logger;
import org.geotools.TestData;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.Test;

public abstract class OGRPeformanceTest extends TestCaseSupport {

    static final Logger LOGGER = Logging.getLogger(OGRPeformanceTest.class);

    static final String STATE_POP = "shapes/statepop.shp";

    protected OGRPeformanceTest(Class<? extends OGRDataStoreFactory> dataStoreFactoryClass) {
        super(dataStoreFactoryClass);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        CRS.decode("EPSG:4326");
    }

    @Test
    public void testOGRShapePerformance() throws Exception {
        OGRDataStore ods = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        long start = System.currentTimeMillis();
        ods.getSchema(ods.getTypeNames()[0]);
        long end = System.currentTimeMillis();
        // System.out.println("OGR schema: " + (end - start) / 1000.0);
        Query query = new Query(ods.getTypeNames()[0]);
        start = System.currentTimeMillis();
        try (FeatureReader ofr = ods.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            while (ofr.hasNext()) ofr.next();
        }
        end = System.currentTimeMillis();
        LOGGER.info("OGR: " + (end - start) / 1000.0);
    }

    @Test
    public void testShapefilePerformance() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore sds = new ShapefileDataStore(url);
        long start = System.currentTimeMillis();
        sds.getSchema();
        long end = System.currentTimeMillis();
        LOGGER.info("SDS schema: " + (end - start) / 1000.0);

        Query query = new Query(sds.getTypeNames()[0]);
        start = System.currentTimeMillis();
        try (FeatureReader sfr = sds.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            while (sfr.hasNext()) sfr.next();
        }
        end = System.currentTimeMillis();
        LOGGER.info("SDS: " + (end - start) / 1000.0);

        // System.out.println("Attribute count: " + sds.getSchema().getAttributeCount());
        // System.out.println(
        //       "Feature count: "
        //              + sds.getFeatureSource(sds.getSchema().getTypeName()).getCount(Query.ALL));
    }
}
