/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import static junit.framework.TestCase.assertEquals;
import static tec.uom.se.unit.Units.GRAM;
import static tec.uom.se.unit.Units.JOULE;
import static tec.uom.se.unit.Units.KELVIN;
import static tec.uom.se.unit.Units.KILOGRAM;
import static tec.uom.se.unit.Units.METRE;
import static tec.uom.se.unit.Units.MOLE;
import static tec.uom.se.unit.Units.PASCAL;
import static tec.uom.se.unit.Units.SECOND;
import static tec.uom.se.unit.Units.WATT;

import java.util.Arrays;
import java.util.Collection;
import javax.measure.Unit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import si.uom.NonSI;
import si.uom.SI;
import tec.uom.se.AbstractUnit;
import tec.uom.se.format.SimpleUnitFormat;

@RunWith(Parameterized.class)
public class GeoToolsUnitFormatRoundTripTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][] {
                    {GRAM.divide(1_000_000)},
                    {GRAM.divide(1_000_000).divide(METRE.pow(3))},
                    {GRAM.divide(1_000_000_000).divide(METRE.pow(3))},
                    {GRAM.divide(1_000_000).divide(METRE.pow(3))},
                    {METRE.pow(2)},
                    {METRE.pow(3)},
                    {SI.MOLE.divide(1_000_000)},
                    {GRAM.multiply(METRE.pow(-3))},
                    {GRAM.divide(1_000)},
                    {SI.MOLE.multiply(METRE.pow(-2))},
                    {SI.PASCAL},
                    {AbstractUnit.ONE},
                    {METRE.divide(SI.SECOND)},
                    {WATT.multiply(METRE.pow(-2))},
                    {SI.KILOGRAM.multiply(METRE.pow(-2).multiply(SI.SECOND.pow(-1)))},
                    {
                        WATT.divide(1000)
                                .multiply(METRE.pow(-2))
                                .multiply(SI.STERADIAN.pow(-1))
                                .multiply(METRE.divide(1_000_000_000).pow(-1))
                    },
                    {
                        WATT.divide(1000)
                                .multiply(METRE.pow(-2))
                                .multiply(METRE.divide(1_000_000_000).pow(-1))
                    },
                    {MOLE.multiply(METRE.divide(100).pow(-3))},
                    {SI.PASCAL},
                    {AbstractUnit.ONE.divide(100)},
                    {METRE},
                    {NonSI.DEGREE_ANGLE},
                    {METRE.pow(2).divide(METRE.pow(2))},
                    {METRE.pow(-1).multiply(SECOND)},
                    {KILOGRAM.multiply(METRE.pow(-3))},
                    {SI.KELVIN.multiply(METRE.pow(-1))},
                    {MOLE.multiply(KILOGRAM.pow(-1))},
                    {JOULE.multiply(KILOGRAM.pow(-1))},
                    {KILOGRAM.multiply(METRE.pow(-2).multiply(SECOND.pow(-1)))},
                    {JOULE.multiply(METRE.pow(-2))},
                    {METRE.pow(2).multiply(SECOND.pow(-1))},
                    {KILOGRAM.multiply(METRE.pow(-2))},
                    {MOLE.multiply(METRE.pow(-2))},
                    {SECOND.pow(-1).multiply(METRE.pow(-3))},
                    {GRAM.multiply(KILOGRAM.pow(-1))},
                    {PASCAL.multiply(METRE)},
                    {WATT.multiply(METRE.pow(-2))},
                    {
                        MOLE.multiply(METRE.pow(-2))
                                .multiply(SECOND.pow(-1))
                                .multiply(METRE.pow(-1))
                                .multiply(SI.STERADIAN.pow(-1))
                    },
                    {
                        KELVIN.multiply(METRE.pow(2))
                                .multiply(KILOGRAM.pow(-1))
                                .multiply(SECOND.pow(-1))
                    },
                    {SI.MOLE.divide(1_000_000).multiply(SI.METRE.pow(-2)).multiply(466.2)}
                });
    }

    private Unit unit;

    public GeoToolsUnitFormatRoundTripTest(Unit unit) {
        this.unit = unit;
    }

    @Test
    public void testRoundTrip() {
        SimpleUnitFormat format = GeoToolsUnitFormat.getInstance();
        String formatted = format.format(unit);
        Unit<?> parsed = format.parse(formatted);
        // the library re-builds units that are not exactly equal in all details, but work
        // the same... the easiest approach is to compare their toString (which end up formatting
        // with SimpleUnitFormat) and verify they are equal
        assertEquals(unit.toString(), parsed.toString());
    }
}
