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
package org.geotools.tile.impl;

import org.junit.Assert;
import org.junit.Test;

public class ZoomLevelTest {

    @Test
    public void testConstructor() {
        Assert.assertNotNull(createZoomLevel(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructor() {
        createZoomLevel(-10);
    }

    private ZoomLevel createZoomLevel(int z) {
        return new ZoomLevel(z) {

            public int calculateMaxTilePerRowNumber(int zoomLevel) {
                return 12;
            }

            public int calculateMaxTilePerColNumber(int zoomLevel) {
                return 14;
            }
        };
    }
}
