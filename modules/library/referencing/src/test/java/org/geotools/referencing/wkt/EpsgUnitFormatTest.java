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

import java.io.IOException;
import org.geotools.measure.UnitFormatter;
import org.geotools.measure.Units;
import org.junit.Test;
import si.uom.NonSI;
import si.uom.SI;

public class EpsgUnitFormatTest {

    private final UnitFormatter epsgUnitFormatter = EpsgUnitFormat.getInstance();

    /**
     * Test method for {@link UnitFormatter#format(javax.measure.Unit, java.lang.Appendable)} for
     * units that have labels or aliases defined in the default format. The goal is ensuring that
     * the label and alias definitions have been correctly cloned from the default format instance
     * to the GT format instances
     */
    @Test
    public void testFormatUnitOfQAppendable() throws IOException {
        UnitFormatTest.doTestNotModifiedUnits(SI.CELSIUS, epsgUnitFormatter);
    }

    /**
     * Test method for {@link UnitFormatter#format(javax.measure.Unit, java.lang.Appendable)} for
     * units that have labels or aliases defined only in the custom GT formats
     */
    @Test
    public void testGTDefinedFormats() throws IOException {
        UnitFormatTest.doTestFormatForGTDefinedUnits(
                NonSI.DEGREE_ANGLE, epsgUnitFormatter, "degree");
        UnitFormatTest.doTestFormatForGTDefinedUnits(
                Units.SEXAGESIMAL_DMS, epsgUnitFormatter, "D.MS");
    }
}
