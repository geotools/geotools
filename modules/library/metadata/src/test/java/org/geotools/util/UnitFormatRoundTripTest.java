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

import static org.junit.Assert.assertEquals;
import static tech.units.indriya.unit.Units.GRAM;
import static tech.units.indriya.unit.Units.JOULE;
import static tech.units.indriya.unit.Units.KELVIN;
import static tech.units.indriya.unit.Units.KILOGRAM;
import static tech.units.indriya.unit.Units.METRE;
import static tech.units.indriya.unit.Units.MOLE;
import static tech.units.indriya.unit.Units.PASCAL;
import static tech.units.indriya.unit.Units.SECOND;
import static tech.units.indriya.unit.Units.WATT;

import java.util.Arrays;
import java.util.Collection;
import javax.measure.Unit;
import org.geotools.measure.UnitFormat;
import org.geotools.measure.UnitFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import si.uom.NonSI;
import si.uom.SI;
import tech.units.indriya.AbstractUnit;

@RunWith(Parameterized.class)
public class UnitFormatRoundTripTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][] {
                    {GRAM},
                    {GRAM.divide(1_000_000)},
                    {GRAM.divide(1_000_000).divide(METRE.pow(3))},
                    {GRAM.divide(1_000_000_000).divide(METRE.pow(3))},
                    {GRAM.divide(1_000_000).divide(METRE.pow(3))},
                    {METRE.pow(2)},
                    {METRE.pow(3)},
                    {MOLE.divide(1_000_000)},
                    {GRAM.multiply(METRE.pow(-3))},
                    {GRAM.divide(1_000)},
                    {MOLE.multiply(METRE.pow(-2))},
                    {PASCAL},
                    {AbstractUnit.ONE},
                    {METRE.divide(SECOND)},
                    {WATT.multiply(METRE.pow(-2))},
                    {KILOGRAM.multiply(METRE.pow(-2).multiply(SECOND.pow(-1)))},
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
                    {PASCAL},
                    {AbstractUnit.ONE.divide(100)},
                    {METRE},
                    {NonSI.DEGREE_ANGLE},
                    {METRE.pow(2).divide(METRE.pow(2))},
                    {METRE.pow(-1).multiply(SECOND)},
                    {KILOGRAM.multiply(METRE.pow(-3))},
                    {KELVIN.multiply(METRE.pow(-1))},
                    {MOLE.multiply(KILOGRAM.pow(-1))},
                    {JOULE.multiply(KILOGRAM.pow(-1))},
                    {KILOGRAM.multiply(METRE.pow(-2).multiply(SECOND.pow(-1)))},
                    {JOULE.multiply(METRE.pow(-2))},
                    {METRE.pow(2).multiply(SECOND.pow(-1))},
                    {KILOGRAM.multiply(METRE.pow(-2))},
                    {MOLE.multiply(METRE.pow(-2))},
                    {SECOND.multiply(METRE.pow(-3))},
                    {METRE.multiply(SECOND.pow(-3))},
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
                    {MOLE.divide(1_000_000).multiply(METRE.pow(-2)).multiply(466.2)}
                });
    }

    private Unit<?> unit;

    public UnitFormatRoundTripTest(Unit<?> unit) {
        this.unit = unit;
    }

    @Test
    public void testRoundTrip() {
        UnitFormatter format = UnitFormat.getInstance();
        String formatted = format.format(unit);
        Unit<?> parsed = format.parse(formatted);
        // the library re-builds units that are not exactly equal in all details, but work
        // the same... the easiest approach is to compare their toString (which end up formatting
        // with SimpleUnitFormat) and verify they are equal
        assertEquals(unit.toString(), parsed.toString());
    }
}
