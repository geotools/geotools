/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.measure.UnitFormat;
import org.geotools.measure.UnitFormatter;
import org.geotools.measure.Units;
import org.junit.Test;

/** @author ian */
public class UnitFormatTest {

    /**
     * Test method for {@link org.geotools.measure.UnitFormatter#format(javax.measure.Unit,
     * java.lang.Appendable)} for units that have labels or aliases defined by GT in the default
     * format
     */
    @Test
    public void testFormatForGTDefinedUnits() throws IOException {
        UnitFormatter unitFormatter = UnitFormat.getInstance();
        doTestFormatForGTDefinedUnits(Units.SEXAGESIMAL_DMS, unitFormatter, "D.MS");
    }

    public static void doTestFormatForGTDefinedUnits(
            Unit<?> u, UnitFormatter unitFormatter, String expected) throws IOException {
        Appendable appendable = new StringBuilder();
        unitFormatter.format(u, appendable);
        assertEquals("Missing symbol formats", expected, appendable.toString());
    }

    public static void doTestNotModifiedUnits(Unit<?> u, UnitFormatter unitFormatter)
            throws IOException {
        Appendable appendable = new StringBuilder();
        unitFormatter.format(u, appendable);
        assertEquals("Missing symbol formats", u.toString(), appendable.toString());
    }
}
