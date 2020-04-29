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
package org.geotools.coverage.io.netcdf;

import static javax.measure.MetricPrefix.CENTI;
import static javax.measure.MetricPrefix.MICRO;
import static javax.measure.MetricPrefix.MILLI;
import static javax.measure.MetricPrefix.NANO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tech.units.indriya.unit.Units.GRAM;
import static tech.units.indriya.unit.Units.JOULE;
import static tech.units.indriya.unit.Units.KELVIN;
import static tech.units.indriya.unit.Units.KILOGRAM;
import static tech.units.indriya.unit.Units.METRE;
import static tech.units.indriya.unit.Units.MOLE;
import static tech.units.indriya.unit.Units.PASCAL;
import static tech.units.indriya.unit.Units.SECOND;
import static tech.units.indriya.unit.Units.STERADIAN;
import static tech.units.indriya.unit.Units.WATT;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;
import org.geotools.imageio.netcdf.NetCDFUnitFormat;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import si.uom.NonSI;
import si.uom.SI;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.format.SimpleUnitFormat;
import tech.units.indriya.function.LogConverter;

@RunWith(Enclosed.class)
public class NetCDFUnitParserTest {

    @RunWith(Parameterized.class)
    public static class UnitConversionTest {

        @Parameterized.Parameters(name = "{index}: {0} -> {1}")
        public static Collection<Object[]> data() {
            return Arrays.asList(
                    new Object[][] {
                        {"microgram", MICRO(GRAM)},
                        {"microgram/m3", MICRO(GRAM).divide(METRE.pow(3))},
                        {"nanograms/m3", NANO(GRAM).divide(METRE.pow(3))},
                        {"microgrammes per cubic meter", MICRO(GRAM).divide(METRE.pow(3))},
                        {"m2", METRE.pow(2)},
                        {"m3", METRE.pow(3)},
                        {"µmol", MICRO(MOLE)},
                        {"g m-3", GRAM.multiply(METRE.pow(-3))},
                        {"mg", MILLI(GRAM)},
                        {"mol m-2", MOLE.multiply(METRE.pow(-2))},
                        {"Pa", SI.PASCAL},
                        {"unitless", AbstractUnit.ONE},
                        {"m/s", METRE.divide(SI.SECOND)},
                        {"W m-2", WATT.multiply(METRE.pow(-2))},
                        {
                            "kg m-2 s-1",
                            SI.KILOGRAM.multiply(METRE.pow(-2).multiply(SI.SECOND.pow(-1)))
                        },
                        {
                            "mW m^-2 sr^-1 nm^-1",
                            MILLI(WATT)
                                    .multiply(METRE.pow(-2))
                                    .multiply(STERADIAN.pow(-1))
                                    .multiply(NANO(METRE).pow(-1))
                        },
                        {
                            "mW m^-2 nm^-1",
                            MILLI(WATT).multiply(METRE.pow(-2)).multiply(NANO(METRE).pow(-1))
                        },
                        {"mol cm-3", MOLE.divide(CENTI(METRE).pow(3))},
                        {"Pa", SI.PASCAL},
                        {"percentage", AbstractUnit.ONE.divide(100)},
                        {"Meter", METRE},
                        {"meter", METRE},
                        {"dB", AbstractUnit.ONE.transform(new LogConverter(10)).divide(10)},
                        {"degree", NonSI.DEGREE_ANGLE},
                        {"m2/m2", METRE.pow(2).divide(METRE.pow(2))},
                        {"m-1 s", METRE.pow(-1).multiply(SECOND)},
                        {"kg m-3", KILOGRAM.multiply(METRE.pow(-3))},
                        {"K m-1", KELVIN.multiply(METRE.pow(-1))},
                        {"mol kg-1", MOLE.multiply(KILOGRAM.pow(-1))},
                        {"J kg-1", JOULE.multiply(KILOGRAM.pow(-1))},
                        {"kg m-2 s-1", KILOGRAM.multiply(METRE.pow(-2).multiply(SECOND.pow(-1)))},
                        {"J m-2", JOULE.multiply(METRE.pow(-2))},
                        {"m2 s-1", METRE.pow(2).multiply(SECOND.pow(-1))},
                        {"kg m-2", KILOGRAM.multiply(METRE.pow(-2))},
                        {"mol m-2", MOLE.multiply(METRE.pow(-2))},
                        {"s-1 m-3", SECOND.pow(-1).multiply(METRE.pow(-3))},
                        {"g kg-1", GRAM.multiply(KILOGRAM.pow(-1))},
                        {"Pa m", PASCAL.multiply(METRE)},
                        {"W m-2", WATT.multiply(METRE.pow(-2))},
                        {
                            "mol m-2 s-1 m-1 sr-1",
                            MOLE.divide(
                                    METRE.pow(2)
                                            .multiply(SECOND)
                                            .multiply(METRE)
                                            .multiply(STERADIAN))
                        },
                        {
                            "K m2 kg-1 s-1",
                            KELVIN.multiply(METRE.pow(2))
                                    .multiply(KILOGRAM.pow(-1))
                                    .multiply(SECOND.pow(-1))
                        }
                    });
        }

        private String input;

        private Unit expected;

        public UnitConversionTest(String input, Unit expected) {
            this.input = input;
            this.expected = expected;
        }

        @Test
        public void test() {
            // compare the symbols, as using direct comparison checks also the parent
            // units which are not always the same
            Unit<?> actual = NetCDFUnitFormat.parse(input);
            String message = actual + " !-> " + expected;
            assertTrue("Not compatible, " + message, expected.isCompatible(actual));
            assertEquals(expected.toString(), actual.toString());
        }
    }

    public static class SimpleTests {

        @After
        public void reset() {
            // clean up any configuration
            NetCDFUnitFormat.reset();
        }

        @Test
        public void testDU() throws Exception {
            // could not find a way to make this work with the above test, so doing it another way
            Unit<?> unit = NetCDFUnitFormat.parse("DU");
            assertEquals("(1/m²)*446.2·μmol", unit.toString());
        }

        @Test(expected = MeasurementParseException.class)
        public void testIsolation() {
            // the normal instance should be isolated, the configuration of the the NetCDF
            // unit parse should not affect the normal parser
            SimpleUnitFormat instance = SimpleUnitFormat.getInstance();
            instance.parse("degree");
        }

        @Test
        public void testReconfigureAliases() {
            NetCDFUnitFormat.setAliases(Collections.singletonMap("foobar", "m^3"));
            Unit<?> parsed = NetCDFUnitFormat.parse("foobar");
            assertEquals(parsed, METRE.pow(3));
        }

        @Test
        public void testReconfigureReplacements() {
            NetCDFUnitFormat.setReplacements(
                    Collections.singletonMap("one two three four!", "g*m^2*s^-2"));
            Unit<?> parsed = NetCDFUnitFormat.parse("one two three four!");
            Unit<?> expected = GRAM.multiply(METRE.pow(2)).multiply(SECOND.pow(-2));
            assertEquals(expected, parsed);
        }
    }
}
