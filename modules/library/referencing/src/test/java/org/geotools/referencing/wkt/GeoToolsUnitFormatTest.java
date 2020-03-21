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
import org.geotools.measure.Units;
import org.geotools.metadata.iso.citation.Citations;
import org.junit.Test;
import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;
import tec.uom.se.format.SimpleUnitFormat;

/** @author ian */
public class GeoToolsUnitFormatTest {

    private UnitFormat epsgUnitFormat = GeoToolsCRSUnitFormat.getInstance(Citations.EPSG);

    private UnitFormat esriUnitFormat = GeoToolsCRSUnitFormat.getInstance(Citations.ESRI);

    /**
     * Test method for {@link javax.measure.unit.UnitFormat#format(javax.measure.unit.Unit,
     * java.lang.Appendable)} for units that have labels or aliases defined in the default format.
     * The goal is ensuring that the label and alias definitions have been correctly cloned from the
     * default format instance to the GT format instances
     */
    @Test
    public void testFormatUnitOfQAppendable() throws IOException {
        doTestNotModifiedUnits(SI.CELSIUS, epsgUnitFormat);
        doTestNotModifiedUnits(SI.CELSIUS, esriUnitFormat);
    }
    /**
     * Test ESRI representation of {@link USCustomary#FOOT_SURVEY) for formatting and parsing.
     */
    @Test
    public void testFootSurvey() {
        assertEquals("Foot_US", esriUnitFormat.format(USCustomary.FOOT_SURVEY));
        Unit<?> unit = esriUnitFormat.parse("Foot_US");
        assertEquals(USCustomary.FOOT_SURVEY, unit);
    }

    /**
     * Test method for {@link javax.measure.unit.UnitFormat#format(javax.measure.unit.Unit,
     * java.lang.Appendable)} for units that have labels or aliases defined only in the custom GT
     * formats
     */
    @Test
    public void testGTDefinedFormats() throws IOException {
        doTestFormatForGTDefinedUnits(NonSI.DEGREE_ANGLE, epsgUnitFormat, "degree");
        doTestFormatForGTDefinedUnits(NonSI.DEGREE_ANGLE, esriUnitFormat, "Degree");
        doTestFormatForGTDefinedUnits(SI.METRE, esriUnitFormat, "Meter");

        doTestFormatForGTDefinedUnits(Units.SEXAGESIMAL_DMS, epsgUnitFormat, "D.MS");
        doTestFormatForGTDefinedUnits(Units.SEXAGESIMAL_DMS, esriUnitFormat, "D.MS");
    }

    /**
     * Test method for {@link javax.measure.unit.UnitFormat#format(javax.measure.unit.Unit,
     * java.lang.Appendable)} for units that have labels or aliases defined by GT in the default
     * format
     */
    @Test
    public void testFormatForGTDefinedUnits() throws IOException {
        UnitFormat unitFormat = SimpleUnitFormat.getInstance();
        doTestFormatForGTDefinedUnits(Units.SEXAGESIMAL_DMS, unitFormat, "D.MS");
    }

    protected void doTestFormatForGTDefinedUnits(Unit<?> u, UnitFormat unitFormat, String expected)
            throws IOException {
        Appendable appendable = new StringBuilder();
        unitFormat.format(u, appendable);
        assertEquals("Missing symbol formats", expected, appendable.toString());
    }

    protected void doTestNotModifiedUnits(Unit<?> u, UnitFormat unitFormat) throws IOException {
        Appendable appendable = new StringBuilder();
        unitFormat.format(u, appendable);
        assertEquals("Missing symbol formats", u.toString(), appendable.toString());
    }
}
