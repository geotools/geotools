/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.Binding;
import org.junit.Test;

public class CoveragetypeBindingTest extends GPKGTestSupport {
    @Test
    public void testType() {
        assertEquals(
                GeoPackageProcessRequest.TilesLayer.TilesCoverage.class,
                binding(GPKG.coveragetype).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GPKG.coveragetype).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        buildDocument(
                "<coverage><minZoom>1</minZoom><maxZoom>10</maxZoom><minColumn>100</minColumn><maxColumn>1000</maxColumn><minRow>50</minRow><maxRow>500</maxRow></coverage>");
        Object result = parse(GPKG.coveragetype);
        assertTrue(result instanceof GeoPackageProcessRequest.TilesLayer.TilesCoverage);
        GeoPackageProcessRequest.TilesLayer.TilesCoverage coverage =
                (GeoPackageProcessRequest.TilesLayer.TilesCoverage) result;
        assertEquals(1, coverage.getMinZoom().intValue());
        assertEquals(10, coverage.getMaxZoom().intValue());
        assertEquals(100, coverage.getMinColumn().intValue());
        assertEquals(1000, coverage.getMaxColumn().intValue());
        assertEquals(50, coverage.getMinRow().intValue());
        assertEquals(500, coverage.getMaxRow().intValue());
    }
}
