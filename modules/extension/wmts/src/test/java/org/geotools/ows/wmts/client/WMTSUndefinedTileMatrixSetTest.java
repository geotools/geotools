/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.geotools.ows.wmts.WMTSTestUtils;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.junit.Test;

/** @author kshaw */
public class WMTSUndefinedTileMatrixSetTest {

    @Test
    public void testTileMatrixSet() {

        WMTSCapabilities capabilities = null;
        try {
            capabilities = WMTSTestUtils.createCapabilities("undefinedTileMatrixSet.xml");
        } catch (Exception ex) {
            fail(ex.getMessage());
            return;
        }

        TileMatrixSet undefinedMatrixSet = capabilities.getMatrixSet("undefined");
        TileMatrixSet definedMatrixSet = capabilities.getMatrixSet("defined");

        assertNotNull(definedMatrixSet);
        // Should be null without throwing a runtime exception like it was before GEOT-7541 fix.
        assertNull(undefinedMatrixSet);
    }
}
