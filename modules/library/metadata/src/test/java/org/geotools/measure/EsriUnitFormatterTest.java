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
package org.geotools.measure;

import static org.geotools.measure.GeoToolsUnitFormatterTest.doTestFormatForGTDefinedUnits;
import static org.geotools.measure.GeoToolsUnitFormatterTest.doTestNotModifiedUnits;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import javax.measure.Unit;
import org.junit.Test;
import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;

public class EsriUnitFormatterTest {

    private final UnitFormatter esriUnitFormatter =
            EsriUnitFormatterFactory.getUnitFormatterSingleton();

    /**
     * Test method for {@link UnitFormatter#format(javax.measure.Unit, java.lang.Appendable)} for
     * units that have labels or aliases defined in the default format. The goal is ensuring that
     * the label and alias definitions have been correctly cloned from the default format instance
     * to the GT format instances
     */
    @Test
    public void testFormatUnitOfQAppendable() throws IOException {
        doTestNotModifiedUnits(SI.CELSIUS, esriUnitFormatter);
    }
    /**
     * Test ESRI representation of {@link systems.uom.common.USCustomary#FOOT_SURVEY) for formatting and parsing.
     */
    @Test
    public void testFootSurvey() {
        assertEquals("Foot_US", esriUnitFormatter.format(USCustomary.FOOT_SURVEY));
        Unit<?> unit = esriUnitFormatter.parse("Foot_US");
        assertEquals(USCustomary.FOOT_SURVEY, unit);
    }

    /**
     * Test method for {@link UnitFormatter#format(javax.measure.Unit, java.lang.Appendable)} for
     * units that have labels or aliases defined only in the custom GT formats
     */
    @Test
    public void testGTDefinedFormats() throws IOException {
        doTestFormatForGTDefinedUnits(NonSI.DEGREE_ANGLE, esriUnitFormatter, "Degree");
        doTestFormatForGTDefinedUnits(SI.METRE, esriUnitFormatter, "Meter");
        doTestFormatForGTDefinedUnits(Units.SEXAGESIMAL_DMS, esriUnitFormatter, "D.MS");
    }
}
