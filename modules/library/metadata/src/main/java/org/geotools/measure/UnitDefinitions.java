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

import java.util.List;
import si.uom.NonSI;
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
            unmodifiableList(
                    asList(
                            UnitDefinition.of(AbstractUnit.ONE, emptyList(), "one", emptyList()),
                            UnitDefinition.of(Units.PERCENT, emptyList(), "%", emptyList())));

    public static List<UnitDefinition> BASE =
            unmodifiableList(
                    asList(
                            UnitDefinition.withStandardPrefixes(Units.AMPERE),
                            UnitDefinition.withStandardPrefixes(Units.CANDELA),
                            UnitDefinition.withStandardPrefixes(Units.KELVIN),
                            UnitDefinition.of(Units.KILOGRAM, emptyList(), null, emptyList()),
                            UnitDefinition.withStandardPrefixes(Units.METRE),
                            UnitDefinition.withStandardPrefixes(Units.MOLE),
                            UnitDefinition.withStandardPrefixes(Units.SECOND)));

    public static List<UnitDefinition> DERIVED =
            unmodifiableList(
                    asList(
                            UnitDefinition.withStandardPrefixes(Units.BECQUEREL),
                            UnitDefinition.of(
                                    Units.CELSIUS, PrefixDefinitions.STANDARD, "℃", asList("°C")),
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
                            UnitDefinition.of(
                                    Units.KILOMETRE_PER_HOUR, emptyList(), "km/h", emptyList()),
                            UnitDefinition.withStandardPrefixes(Units.LITRE),
                            UnitDefinition.withStandardPrefixes(Units.LUMEN),
                            UnitDefinition.withStandardPrefixes(Units.LUX),
                            UnitDefinition.of(Units.MINUTE, emptyList(), "min", emptyList()),
                            UnitDefinition.withStandardPrefixes(Units.NEWTON),
                            UnitDefinition.of(
                                    Units.OHM, PrefixDefinitions.STANDARD, null, asList("Ohm")),
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
                            UnitDefinition.of(Units.YEAR, emptyList(), "year", asList("days365"))));

    /** Additional unit definitions for custom units defined by Geotools. */
    public static List<UnitDefinition> GEOTOOLS =
            unmodifiableList(
                    asList(
                            UnitDefinition.of(
                                    org.geotools.measure.Units.DEGREE_MINUTE_SECOND,
                                    emptyList(),
                                    "DMS",
                                    asList("degree minute second")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.SEXAGESIMAL_DMS,
                                    emptyList(),
                                    "D.MS",
                                    asList(
                                            "sexagesimal DMS",
                                            "DDD.MMSSsss",
                                            "sexagesimal degree DDD.MMSSsss")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.PPM,
                                    emptyList(),
                                    "ppm",
                                    emptyList()),
                            UnitDefinition.of(NonSI.DEGREE_ANGLE, emptyList(), "°", asList("deg")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.PIXEL,
                                    emptyList(),
                                    "pixel",
                                    emptyList()),
                            UnitDefinition.of(
                                    USCustomary.GRADE, emptyList(), "grad", asList("grade")),
                            UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", emptyList())));

    /** Additional unit definitions for custom units defined by EPSG formats. */
    public static List<UnitDefinition> EPSG =
            unmodifiableList(
                    asList(
                            UnitDefinition.of(
                                    org.geotools.measure.Units.DEGREE_MINUTE_SECOND,
                                    emptyList(),
                                    "DMS",
                                    asList("degree minute second")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.SEXAGESIMAL_DMS,
                                    emptyList(),
                                    "D.MS",
                                    asList(
                                            "sexagesimal DMS",
                                            "DDD.MMSSsss",
                                            "sexagesimal degree DDD.MMSSsss")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.PPM,
                                    emptyList(),
                                    "ppm",
                                    emptyList()),
                            UnitDefinition.of(
                                    NonSI.DEGREE_ANGLE, emptyList(), "degree", emptyList()),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.PIXEL,
                                    emptyList(),
                                    "pixel",
                                    emptyList()),
                            UnitDefinition.of(
                                    USCustomary.GRADE, emptyList(), "grad", asList("grade")),
                            UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", emptyList())));

    /** Additional unit definitions for custom units defined by ESRI formats. */
    public static List<UnitDefinition> ESRI =
            unmodifiableList(
                    asList(
                            UnitDefinition.of(
                                    org.geotools.measure.Units.DEGREE_MINUTE_SECOND,
                                    emptyList(),
                                    "DMS",
                                    asList("degree minute second")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.SEXAGESIMAL_DMS,
                                    emptyList(),
                                    "D.MS",
                                    asList(
                                            "sexagesimal DMS",
                                            "DDD.MMSSsss",
                                            "sexagesimal degree DDD.MMSSsss")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.PPM,
                                    emptyList(),
                                    "ppm",
                                    emptyList()),
                            UnitDefinition.of(
                                    NonSI.DEGREE_ANGLE, emptyList(), "Degree", emptyList()),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.PIXEL,
                                    emptyList(),
                                    "pixel",
                                    emptyList()),
                            UnitDefinition.of(
                                    USCustomary.GRADE, emptyList(), "grad", asList("grade")),
                            UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", asList("Foot")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.FOOT_GOLD_COAST,
                                    emptyList(),
                                    null,
                                    emptyList()),
                            UnitDefinition.of(
                                    USCustomary.FOOT_SURVEY, emptyList(), "Foot_US", emptyList()),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.METRE,
                                    emptyList(),
                                    "Meter",
                                    emptyList())));

    public static List<UnitDefinition> WKT =
            unmodifiableList(
                    asList(
                            UnitDefinition.of(
                                    org.geotools.measure.Units.DEGREE_MINUTE_SECOND,
                                    emptyList(),
                                    "DMS",
                                    asList("degree minute second")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.SEXAGESIMAL_DMS,
                                    emptyList(),
                                    "D.MS",
                                    asList(
                                            "sexagesimal DMS",
                                            "DDD.MMSSsss",
                                            "sexagesimal degree DDD.MMSSsss")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.PPM,
                                    emptyList(),
                                    "ppm",
                                    emptyList()),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.PIXEL,
                                    emptyList(),
                                    "pixel",
                                    emptyList()),
                            UnitDefinition.of(
                                    USCustomary.GRADE, emptyList(), "grad", asList("grade")),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.FOOT_GOLD_COAST,
                                    emptyList(),
                                    null,
                                    emptyList()),
                            UnitDefinition.of(
                                    USCustomary.FOOT_SURVEY, emptyList(), "Foot_US", emptyList()),
                            UnitDefinition.of(
                                    NonSI.DEGREE_ANGLE, emptyList(), "degree", emptyList()),
                            UnitDefinition.of(USCustomary.FOOT, emptyList(), "ft", emptyList()),
                            UnitDefinition.of(
                                    org.geotools.measure.Units.METRE,
                                    emptyList(),
                                    "Meter",
                                    emptyList())));
}
