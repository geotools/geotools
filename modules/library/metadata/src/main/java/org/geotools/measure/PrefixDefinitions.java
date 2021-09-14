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
import static java.util.Collections.unmodifiableList;
import static javax.measure.MetricPrefix.ATTO;
import static javax.measure.MetricPrefix.CENTI;
import static javax.measure.MetricPrefix.DECI;
import static javax.measure.MetricPrefix.DEKA;
import static javax.measure.MetricPrefix.EXA;
import static javax.measure.MetricPrefix.FEMTO;
import static javax.measure.MetricPrefix.GIGA;
import static javax.measure.MetricPrefix.HECTO;
import static javax.measure.MetricPrefix.KILO;
import static javax.measure.MetricPrefix.MEGA;
import static javax.measure.MetricPrefix.MICRO;
import static javax.measure.MetricPrefix.MILLI;
import static javax.measure.MetricPrefix.NANO;
import static javax.measure.MetricPrefix.PETA;
import static javax.measure.MetricPrefix.PICO;
import static javax.measure.MetricPrefix.TERA;
import static javax.measure.MetricPrefix.YOCTO;
import static javax.measure.MetricPrefix.YOTTA;
import static javax.measure.MetricPrefix.ZEPTO;
import static javax.measure.MetricPrefix.ZETTA;

import java.util.List;

/** This class holds definitions of common metric unit prefixes for use in unit definitions. */
public final class PrefixDefinitions {

    /**
     * A list of metric prefix definitions from YOTTA (10<sup>24</sup>) to YOCTO (10<sup>-24</sup>).
     */
    public static List<PrefixDefinition> STANDARD =
            unmodifiableList(
                    asList(
                            PrefixDefinition.of(YOTTA),
                            PrefixDefinition.of(ZETTA),
                            PrefixDefinition.of(EXA),
                            PrefixDefinition.of(PETA),
                            PrefixDefinition.of(TERA),
                            PrefixDefinition.of(GIGA),
                            PrefixDefinition.of(MEGA),
                            PrefixDefinition.of(KILO),
                            PrefixDefinition.of(HECTO),
                            PrefixDefinition.of(DEKA),
                            PrefixDefinition.of(DECI),
                            PrefixDefinition.of(CENTI),
                            PrefixDefinition.of(MILLI),
                            PrefixDefinition.of(MICRO, "\u03BC"),
                            PrefixDefinition.of(NANO),
                            PrefixDefinition.of(PICO),
                            PrefixDefinition.of(FEMTO),
                            PrefixDefinition.of(ATTO),
                            PrefixDefinition.of(ZEPTO),
                            PrefixDefinition.of(YOCTO)));

    /**
     * A prefix definition that leaves out KILO, which is used for the definition of units of mass:
     * As the base unit of mass is kilogram, adding prefixes to that unit would result in µkg, kkg,
     * nkg etc. which is not desired. Instead we define kilogram as a unit without any prefixes, and
     * then define gram, with all prefixes but KIL0.
     */
    static List<PrefixDefinition> GRAM =
            unmodifiableList(
                    asList(
                            PrefixDefinition.of(YOTTA),
                            PrefixDefinition.of(ZETTA),
                            PrefixDefinition.of(EXA),
                            PrefixDefinition.of(PETA),
                            PrefixDefinition.of(TERA),
                            PrefixDefinition.of(GIGA),
                            PrefixDefinition.of(MEGA),
                            // no kilo
                            PrefixDefinition.of(HECTO),
                            PrefixDefinition.of(DEKA),
                            PrefixDefinition.of(DECI),
                            PrefixDefinition.of(CENTI),
                            PrefixDefinition.of(MILLI),
                            PrefixDefinition.of(MICRO, "\u03BC"),
                            PrefixDefinition.of(NANO),
                            PrefixDefinition.of(PICO),
                            PrefixDefinition.of(FEMTO),
                            PrefixDefinition.of(ATTO),
                            PrefixDefinition.of(ZEPTO),
                            PrefixDefinition.of(YOCTO)));
}
