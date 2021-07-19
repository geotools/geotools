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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.geotools.measure.Units.DEGREE_MINUTE_SECOND;
import static org.geotools.measure.Units.FOOT_GOLD_COAST;
import static org.geotools.measure.Units.METRE;
import static org.geotools.measure.Units.PIXEL;
import static org.geotools.measure.Units.PPM;
import static org.geotools.measure.Units.SEXAGESIMAL_DMS;

import java.util.List;
import javax.measure.MetricPrefix;
import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.unit.Units;

/**
 * This class holds unit definitions for use in unit formatter implementations.
 *
 * <p>The individual definitions allow mixing and matching of units definitions as required for each
 * individual unit formatter implementation.
 */
public final class UnitDefinitions {

    private UnitDefinitions() {}

    public static List<UnitDefinition> DIMENSIONLESS =
            listOf(
                    UnitDefinition.of(AbstractUnit.ONE, emptyList(), "one", emptyList()),
                    UnitDefinition.of(Units.PERCENT, emptyList(), "%", emptyList()));

    public static List<UnitDefinition> SI_BASE =
            listOf(
                    UnitDefinition.withStandardPrefixes(Units.AMPERE),
                    UnitDefinition.withStandardPrefixes(Units.CANDELA),
                    UnitDefinition.withStandardPrefixes(Units.KELVIN),
                    UnitDefinition.of(Units.KILOGRAM, emptyList(), null, emptyList()),
                    UnitDefinition.withStandardPrefixes(Units.METRE),
                    UnitDefinition.withStandardPrefixes(Units.MOLE),
                    UnitDefinition.withStandardPrefixes(Units.SECOND));

    public static List<UnitDefinition> CONSTANTS =
            listOf(
                    UnitDefinition.of(SI.AVOGADRO_CONSTANT, emptyList(), "NA", emptyList()),
                    UnitDefinition.of(SI.BOLTZMANN_CONSTANT, emptyList(), "kB", emptyList()),
                    UnitDefinition.of(SI.ELEMENTARY_CHARGE, emptyList(), "e", emptyList()),
                    UnitDefinition.of(SI.PLANCK_CONSTANT, emptyList(), "\u210E", emptyList()));

    public static List<UnitDefinition> SI_DERIVED =
            listOf(
                    UnitDefinition.withStandardPrefixes(Units.BECQUEREL),
                    UnitDefinition.of(Units.CELSIUS, PrefixDefinitions.STANDARD, "℃", asList("°C")),
                    UnitDefinition.withStandardPrefixes(Units.COULOMB),
                    // "m3" was added later, as well as (Units.SQUARE_METRE, "m2"), but not
                    // m²
                    UnitDefinition.of(Units.CUBIC_METRE, emptyList(), "㎥", emptyList()),
                    UnitDefinition.of(Units.DAY, emptyList(), "day", asList("d")),
                    UnitDefinition.withStandardPrefixes(Units.FARAD),
                    UnitDefinition.of(Units.GRAM, PrefixDefinitions.GRAM, "g", emptyList()),
                    UnitDefinition.withStandardPrefixes(Units.GRAY),
                    UnitDefinition.withStandardPrefixes(Units.HENRY),
                    UnitDefinition.withStandardPrefixes(Units.HERTZ),
                    UnitDefinition.of(Units.HOUR, emptyList(), "h", emptyList()),
                    UnitDefinition.withStandardPrefixes(Units.JOULE),
                    UnitDefinition.withStandardPrefixes(Units.KATAL),
                    UnitDefinition.of(Units.KILOMETRE_PER_HOUR, emptyList(), "km/h", emptyList()),
                    UnitDefinition.withStandardPrefixes(Units.LITRE),
                    UnitDefinition.withStandardPrefixes(Units.LUMEN),
                    UnitDefinition.withStandardPrefixes(Units.LUX),
                    UnitDefinition.of(Units.MINUTE, emptyList(), "min", emptyList()),
                    UnitDefinition.withStandardPrefixes(Units.NEWTON),
                    UnitDefinition.of(Units.OHM, PrefixDefinitions.STANDARD, null, asList("Ohm")),
                    UnitDefinition.withStandardPrefixes(Units.PASCAL),
                    UnitDefinition.withStandardPrefixes(Units.RADIAN),
                    UnitDefinition.withStandardPrefixes(Units.SIEMENS),
                    UnitDefinition.withStandardPrefixes(Units.SIEVERT),
                    UnitDefinition.withStandardPrefixes(Units.STERADIAN),
                    UnitDefinition.withStandardPrefixes(Units.TESLA),
                    UnitDefinition.withStandardPrefixes(Units.VOLT),
                    UnitDefinition.withStandardPrefixes(Units.WATT),
                    UnitDefinition.withStandardPrefixes(Units.WEBER),
                    UnitDefinition.of(Units.WEEK, emptyList(), "week", emptyList()),
                    UnitDefinition.of(Units.YEAR, emptyList(), "year", asList("days365")));

    public static List<UnitDefinition> NON_SI =
            listOf(
                    UnitDefinition.of(NonSI.DEGREE_ANGLE, emptyList(), "deg", emptyList()),
                    UnitDefinition.of(NonSI.MINUTE_ANGLE, emptyList(), "'", emptyList()),
                    UnitDefinition.of(NonSI.SECOND_ANGLE, emptyList(), "''", emptyList()),
                    UnitDefinition.of(NonSI.ELECTRON_VOLT, emptyList(), "eV", emptyList()),
                    UnitDefinition.of(NonSI.UNIFIED_ATOMIC_MASS, emptyList(), "u", emptyList()),
                    UnitDefinition.of(NonSI.DALTON, emptyList(), "Da", emptyList()),
                    UnitDefinition.of(NonSI.ASTRONOMICAL_UNIT, emptyList(), "UA", emptyList()),
                    UnitDefinition.of(NonSI.HECTARE, emptyList(), "ha", emptyList()),
                    UnitDefinition.of(NonSI.ANGSTROM, emptyList(), "\u00C5", emptyList()),
                    UnitDefinition.of(NonSI.BOHR_RADIUS, emptyList(), "a0", emptyList()),
                    UnitDefinition.of(NonSI.KNOT, emptyList(), "kn", emptyList()),
                    UnitDefinition.of(
                            NonSI.STANDARD_GRAVITY, emptyList(), "g\\u2099", asList("gn")),
                    UnitDefinition.of(NonSI.PHOT, emptyList(), "ph", emptyList()),
                    UnitDefinition.of(NonSI.OERSTED, emptyList(), "Oe", emptyList()),
                    UnitDefinition.of(NonSI.DYNE, emptyList(), "dyn", emptyList()),
                    UnitDefinition.of(NonSI.KILOGRAM_FORCE, emptyList(), "kgf", emptyList()),
                    UnitDefinition.of(NonSI.BAR, emptyList(), "b", emptyList()),
                    UnitDefinition.of(NonSI.RAD, emptyList(), "rd", emptyList()),
                    UnitDefinition.of(NonSI.ROENTGEN, emptyList(), "R", emptyList()),
                    UnitDefinition.of(NonSI.BEL, emptyList(), "B", emptyList()),
                    UnitDefinition.of(NonSI.NEPER, emptyList(), "Np", emptyList()),
                    UnitDefinition.of(NonSI.TONNE, emptyList(), "t", emptyList()),
                    UnitDefinition.of(
                            MetricPrefix.MEGA(NonSI.TONNE), emptyList(), "Mt", emptyList()));

    public static List<UnitDefinition> US_CUSTOMARY =
            listOf(
                    // length
                    UnitDefinition.of(USCustomary.INCH, emptyList(), "in", emptyList()),
                    UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", emptyList()),
                    UnitDefinition.of(
                            USCustomary.FOOT_SURVEY, emptyList(), "ft_survey_us", emptyList()),
                    UnitDefinition.of(USCustomary.YARD, emptyList(), "yd", emptyList()),
                    UnitDefinition.of(USCustomary.MILE, emptyList(), "mi", emptyList()),
                    UnitDefinition.of(USCustomary.NAUTICAL_MILE, emptyList(), "nmi", emptyList()),
                    UnitDefinition.of(USCustomary.LIGHT_YEAR, emptyList(), "ly", emptyList()),

                    // mass
                    UnitDefinition.of(USCustomary.OUNCE, emptyList(), "oz", emptyList()),
                    UnitDefinition.of(USCustomary.POUND, emptyList(), "lb", emptyList()),
                    UnitDefinition.of(USCustomary.TON, emptyList(), "ton_us", emptyList()),

                    // temperature
                    UnitDefinition.of(USCustomary.FAHRENHEIT, emptyList(), "°F", emptyList()),

                    // angle
                    UnitDefinition.of(USCustomary.REVOLUTION, emptyList(), "rev", emptyList()),

                    // speed
                    UnitDefinition.of(USCustomary.MILE_PER_HOUR, emptyList(), "mph", emptyList()),
                    UnitDefinition.of(USCustomary.KNOT, emptyList(), "kn", emptyList()),

                    // area
                    UnitDefinition.of(USCustomary.SQUARE_FOOT, emptyList(), "sft", emptyList()),
                    UnitDefinition.of(USCustomary.ARE, emptyList(), "a", emptyList()),
                    UnitDefinition.of(USCustomary.ACRE, emptyList(), "ac", emptyList()),
                    UnitDefinition.of(USCustomary.HECTARE, emptyList(), "ha", emptyList()),

                    // energy
                    UnitDefinition.of(USCustomary.ELECTRON_VOLT, emptyList(), "eV", emptyList()),

                    // power
                    UnitDefinition.of(USCustomary.HORSEPOWER, emptyList(), "HP", emptyList()),

                    // volume
                    UnitDefinition.of(USCustomary.LITER, emptyList(), "L", emptyList()),
                    UnitDefinition.of(USCustomary.CUBIC_INCH, emptyList(), "in³", emptyList()),
                    UnitDefinition.of(USCustomary.CUBIC_FOOT, emptyList(), "ft³", emptyList()),
                    UnitDefinition.of(USCustomary.ACRE_FOOT, emptyList(), "ac ft", emptyList()),
                    UnitDefinition.of(
                            USCustomary.GALLON_DRY, emptyList(), "gal_dry_us", emptyList()),
                    UnitDefinition.of(USCustomary.GALLON_LIQUID, emptyList(), "gal", emptyList()),
                    UnitDefinition.of(USCustomary.FLUID_OUNCE, emptyList(), "fl oz", emptyList()),
                    UnitDefinition.of(USCustomary.GILL_LIQUID, emptyList(), "liq.gi", emptyList()),
                    UnitDefinition.of(USCustomary.MINIM, emptyList(), "min_us", emptyList()),
                    UnitDefinition.of(USCustomary.FLUID_DRAM, emptyList(), "fl dr", emptyList()),
                    UnitDefinition.of(USCustomary.CUP, emptyList(), "cup", emptyList()),
                    UnitDefinition.of(USCustomary.TEASPOON, emptyList(), "tsp", emptyList()),
                    UnitDefinition.of(USCustomary.TABLESPOON, emptyList(), "Tbsp", emptyList()),
                    UnitDefinition.of(USCustomary.BARREL, emptyList(), "bbl", emptyList()),
                    UnitDefinition.of(USCustomary.PINT, emptyList(), "pt", emptyList()));

    /** Additional unit definitions for custom units defined by Geotools. */
    public static List<UnitDefinition> GEOTOOLS =
            listOf(
                    UnitDefinition.of(
                            DEGREE_MINUTE_SECOND,
                            emptyList(),
                            "DMS",
                            asList("degree minute second")),
                    UnitDefinition.of(
                            SEXAGESIMAL_DMS,
                            emptyList(),
                            "D.MS",
                            asList(
                                    "sexagesimal DMS",
                                    "DDD.MMSSsss",
                                    "sexagesimal degree DDD.MMSSsss")),
                    UnitDefinition.of(PPM, emptyList(), "ppm", emptyList()),
                    UnitDefinition.of(NonSI.DEGREE_ANGLE, emptyList(), "°", asList("deg")),
                    UnitDefinition.of(PIXEL, emptyList(), "pixel", emptyList()),
                    UnitDefinition.of(USCustomary.GRADE, emptyList(), "grad", asList("grade")),
                    UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", emptyList()));

    /** Additional unit definitions for custom units defined by EPSG formats. */
    public static List<UnitDefinition> EPSG =
            listOf(
                    UnitDefinition.of(
                            DEGREE_MINUTE_SECOND,
                            emptyList(),
                            "DMS",
                            asList("degree minute second")),
                    UnitDefinition.of(
                            SEXAGESIMAL_DMS,
                            emptyList(),
                            "D.MS",
                            asList(
                                    "sexagesimal DMS",
                                    "DDD.MMSSsss",
                                    "sexagesimal degree DDD.MMSSsss")),
                    UnitDefinition.of(PPM, emptyList(), "ppm", emptyList()),
                    UnitDefinition.of(NonSI.DEGREE_ANGLE, emptyList(), "degree", emptyList()),
                    UnitDefinition.of(PIXEL, emptyList(), "pixel", emptyList()),
                    UnitDefinition.of(USCustomary.GRADE, emptyList(), "grad", asList("grade")),
                    UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", emptyList()));

    /** Additional unit definitions for custom units defined by ESRI formats. */
    public static List<UnitDefinition> ESRI =
            listOf(
                    UnitDefinition.of(
                            DEGREE_MINUTE_SECOND,
                            emptyList(),
                            "DMS",
                            asList("degree minute second")),
                    UnitDefinition.of(
                            SEXAGESIMAL_DMS,
                            emptyList(),
                            "D.MS",
                            asList(
                                    "sexagesimal DMS",
                                    "DDD.MMSSsss",
                                    "sexagesimal degree DDD.MMSSsss")),
                    UnitDefinition.of(PPM, emptyList(), "ppm", emptyList()),
                    UnitDefinition.of(NonSI.DEGREE_ANGLE, emptyList(), "Degree", emptyList()),
                    UnitDefinition.of(PIXEL, emptyList(), "pixel", emptyList()),
                    UnitDefinition.of(USCustomary.GRADE, emptyList(), "grad", asList("grade")),
                    UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", asList("Foot")),
                    UnitDefinition.of(FOOT_GOLD_COAST, emptyList(), null, emptyList()),
                    UnitDefinition.of(USCustomary.FOOT_SURVEY, emptyList(), "Foot_US", emptyList()),
                    UnitDefinition.of(METRE, emptyList(), "Meter", emptyList()));

    public static List<UnitDefinition> WKT =
            listOf(
                    UnitDefinition.of(
                            DEGREE_MINUTE_SECOND,
                            emptyList(),
                            "DMS",
                            asList("degree minute second")),
                    UnitDefinition.of(
                            SEXAGESIMAL_DMS,
                            emptyList(),
                            "D.MS",
                            asList(
                                    "sexagesimal DMS",
                                    "DDD.MMSSsss",
                                    "sexagesimal degree DDD.MMSSsss")),
                    UnitDefinition.of(PPM, emptyList(), "ppm", emptyList()),
                    UnitDefinition.of(PIXEL, emptyList(), "pixel", emptyList()),
                    UnitDefinition.of(USCustomary.GRADE, emptyList(), "grad", asList("grade")),
                    UnitDefinition.of(FOOT_GOLD_COAST, emptyList(), null, emptyList()),
                    UnitDefinition.of(USCustomary.FOOT_SURVEY, emptyList(), "Foot_US", emptyList()),
                    UnitDefinition.of(NonSI.DEGREE_ANGLE, emptyList(), "degree", emptyList()),
                    UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", emptyList()),
                    UnitDefinition.of(METRE, emptyList(), "Meter", emptyList()));

    // can be replaced with `List.of` in Java 9
    @SafeVarargs
    private static <T> List<T> listOf(T... values) {
        return unmodifiableList(asList(values));
    }
}
