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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.geotools.jdbc.JDBCFeatureSourceExposePkOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

public class OracleFeatureSourceExposePkOnlineTest extends JDBCFeatureSourceExposePkOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

    /**
     * Because Oracle uses "Bigdecimal" for any number we need to change the type of the objects that are in the list of
     * expected objects.
     *
     * @return expected list for {@link #testMixedEncodeIn()}
     */
    @Override
    protected List<Object> getTestMixedEncodeInExpected() {
        return Arrays.asList("zero", "two", BigDecimal.valueOf(1), BigDecimal.valueOf(2), 0d);
    }
}
