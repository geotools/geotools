/*
 *    GeoTools - +The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gce.imagemosaic.jdbc.custom;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PGRaster32BitOnlineTest extends PGRasterOnlineTest {
    public PGRaster32BitOnlineTest(String test) {
        super(test);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();

        PGRaster32BitOnlineTest test = new PGRaster32BitOnlineTest("");

        if (test.checkPreConditions() == false) {
            return suite;
        }

        suite.addTest(new PGRaster32BitOnlineTest("testGetConnection"));

        // Test with in db pgraster
        suite.addTest(new PGRaster32BitOnlineTest("testDrop"));
        suite.addTest(new PGRaster32BitOnlineTest("testCreateFloat32"));
        suite.addTest(new PGRaster32BitOnlineTest("testImage1"));
        suite.addTest(new PGRaster32BitOnlineTest("testFullExtent"));
        suite.addTest(new PGRaster32BitOnlineTest("testNoData"));
        suite.addTest(new PGRaster32BitOnlineTest("testPartial"));
        suite.addTest(new PGRaster32BitOnlineTest("testVienna"));
        suite.addTest(new PGRaster32BitOnlineTest("testViennaEnv"));

        // The following two tests fail but it's not clear why yet.
        // suite.addTest(new PGRaster32BitOnlineTest("testOutputTransparentColor"));
        // suite.addTest(new PGRaster32BitOnlineTest("testOutputTransparentColor2"));

        suite.addTest(new PGRaster32BitOnlineTest("testCloseConnection"));

        return suite;
    }

    public void testCreateFloat32() {
        executeCreate(
                Connection,
                new String[] {
                    "pgraster32bit.sql", "1/pgraster32bit.sql", "2/pgraster32bit.sql",
                },
                false);
    }
}
