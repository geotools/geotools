/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.wkt;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.geotools.metadata.iso.citation.Citations;
import org.junit.Test;

/**
 * @author ian
 *
 */
public class GeoToolsUnitFormatTest {

    private UnitFormat unitFormat = GeoToolsUnitFormat.getInstance(Citations.EPSG);

    /**
     * Test method for {@link javax.measure.unit.UnitFormat#format(javax.measure.unit.Unit, java.lang.Appendable)}.
     * 
     * @throws IOException
     */
    @Test
    public void testFormatUnitOfQAppendable() throws IOException {

        Unit<?> u = SI.CELSIUS;
        Appendable appendable = new StringBuilder();
        unitFormat.format(u, appendable);
        assertEquals("Missing symbol formats", u.toString(), appendable.toString());
    }

}
