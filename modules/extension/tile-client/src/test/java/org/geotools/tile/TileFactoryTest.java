/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TileFactoryTest {

    protected TileFactory factory;

    @Before
    public void setUp() {
        this.factory = createFactory();
    }

    @Test
    public void testNormalizeDegreeValue() {

        double value1 = TileFactory.normalizeDegreeValue(-210, 180);
        Assert.assertEquals(150, value1, 0.00001);
        double value2 = TileFactory.normalizeDegreeValue(-91, 90);
        Assert.assertEquals(89, value2, 0.00001);

        double value3 = TileFactory.normalizeDegreeValue(210, 180);
        Assert.assertEquals(-150, value3, 0.00001);
        double value4 = TileFactory.normalizeDegreeValue(91, 90);
        Assert.assertEquals(-89, value4, 0.00001);
    }

    protected TileFactory createFactory() {
        // not used in this test
        return null;
    }
}
