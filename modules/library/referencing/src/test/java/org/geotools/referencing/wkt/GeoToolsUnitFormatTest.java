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

import javax.measure.Unit;
import javax.measure.format.UnitFormat;

import org.geotools.metadata.iso.citation.Citations;
import org.junit.Test;

import si.uom.NonSI;
import si.uom.SI;

/**
 * @author ian
 *
 */
public class GeoToolsUnitFormatTest {

    private UnitFormat epsgUnitFormat = GeoToolsUnitFormat.getInstance(Citations.EPSG);

    private UnitFormat esriUnitFormat = GeoToolsUnitFormat.getInstance(Citations.ESRI);

    /**
     * Test method for {@link javax.measure.unit.UnitFormat#format(javax.measure.unit.Unit, java.lang.Appendable)}.
     * 
     * @throws IOException
     */
    @Test
    public void testFormatUnitOfQAppendable() throws IOException {
        dotestModifiedUnits(SI.CELSIUS, epsgUnitFormat);
        doTestModifiedUnits(NonSI.DEGREE_ANGLE, epsgUnitFormat, "degree");

        dotestModifiedUnits(SI.CELSIUS, esriUnitFormat);
        doTestModifiedUnits(NonSI.DEGREE_ANGLE, esriUnitFormat, "Degree");
        doTestModifiedUnits(SI.METRE, esriUnitFormat, "Meter");

    }

    protected void doTestModifiedUnits(Unit<?> u, UnitFormat unitFormat, String expected)
            throws IOException {
        Appendable appendable = new StringBuilder();
        unitFormat.format(u, appendable);
        assertEquals("Missing symbol formats", expected, appendable.toString());
    }

    protected void dotestModifiedUnits(Unit<?> u, UnitFormat unitFormat) throws IOException {
        Appendable appendable = new StringBuilder();
        unitFormat.format(u, appendable);
        assertEquals("Missing symbol formats", u.toString(), appendable.toString());
    }

}
