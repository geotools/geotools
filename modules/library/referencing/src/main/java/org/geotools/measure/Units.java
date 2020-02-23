/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import javax.measure.IncommensurableException;
import javax.measure.Quantity;
import javax.measure.UnconvertibleException;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.format.ParserException;
import javax.measure.format.UnitFormat;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.quantity.Time;
import org.geotools.referencing.wkt.DefaultUnitParser;
import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;
import tec.uom.se.AbstractUnit;
import tec.uom.se.format.SimpleUnitFormat;
import tec.uom.se.unit.TransformedUnit;

/**
 * A set of units to use in addition of {@link SI} and {@link NonSI}.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class Units {
    /** Do not allows instantiation of this class. */
    private Units() {}
    /** Length of <code>1/72</code> of a {@link USCustomary#INCH} */
    public static final Unit<Length> PIXEL = USCustomary.INCH.divide(72);

    /** Time duration of <code>1/12</code> of a {@link SI#YEAR}. */
    public static final Unit<Time> MONTH = SI.YEAR.divide(12);

    /**
     * Pseudo-unit for sexagesimal degree. Numbers in this pseudo-unit has the following format:
     *
     * <p><cite>sign - degrees - decimal point - minutes (two digits) - integer seconds (two digits)
     * - fraction of seconds (any precision)</cite>.
     *
     * <p>This unit is non-linear and not pratical for computation. Consequently, it should be avoid
     * as much as possible. Unfortunatly, this pseudo-unit is extensively used in the EPSG database
     * (code 9110).
     */
    public static final Unit<Angle> SEXAGESIMAL_DMS =
            NonSI.DEGREE_ANGLE
                    .transform(SexagesimalConverter.FRACTIONAL.inverse())
                    .asType(Angle.class);

    /**
     * Pseudo-unit for degree - minute - second. Numbers in this pseudo-unit has the following
     * format:
     *
     * <p><cite>signed degrees (integer) - arc-minutes (integer) - arc-seconds (real, any
     * precision)</cite>.
     *
     * <p>This unit is non-linear and not pratical for computation. Consequently, it should be avoid
     * as much as possible. Unfortunatly, this pseudo-unit is extensively used in the EPSG database
     * (code 9107).
     */
    public static final Unit<Angle> DEGREE_MINUTE_SECOND =
            NonSI.DEGREE_ANGLE
                    .transform(SexagesimalConverter.INTEGER.inverse())
                    .asType(Angle.class);

    /** Parts per million. */
    public static final Unit<Dimensionless> PPM = AbstractUnit.ONE.multiply(1E-6);

    static final UnitFormat format = SimpleUnitFormat.getInstance();

    static {
        /** Associates the labels to units created in this class. */
        registerCustomUnits((SimpleUnitFormat) format);
    }
    /**
     * Gets an instance of the default system-wide Unit format. Use this method instead of
     * SimpleUnitFormat.getInstance(), since custom Geotools units might not get registered if
     * SimpleUnitFormat.getInstance() is directly accessed.
     *
     * @see GeoToolsUnitFormat#getInstance()
     */
    public static UnitFormat getDefaultFormat() {
        return format;
    }

    /**
     * Registers the labels and aliases for the custom units defined by Geotools.
     *
     * @param format The UnitFormat in which the labels and aliases must be registered.
     */
    static void registerCustomUnits(SimpleUnitFormat format) {
        format.label(Units.DEGREE_MINUTE_SECOND, "DMS");
        format.alias(Units.DEGREE_MINUTE_SECOND, "degree minute second");

        format.label(Units.SEXAGESIMAL_DMS, "D.MS");
        format.alias(Units.SEXAGESIMAL_DMS, "sexagesimal DMS");
        format.alias(Units.SEXAGESIMAL_DMS, "DDD.MMSSsss");
        format.alias(Units.SEXAGESIMAL_DMS, "sexagesimal degree DDD.MMSSsss");

        format.label(Units.PPM, "ppm");

        format.label(NonSI.DEGREE_ANGLE, "°");
        format.label(Units.PIXEL, "pixel");

        format.label(USCustomary.GRADE, "grad");
        format.alias(USCustomary.GRADE, "grade");
    }

    /**
     * Unit name, willing to use {@link SimpleUnitFormat} to look up appropriate label if a name has
     * not been not defined.
     *
     * <p>This allows us to format units like {@link Units#PIXEL}.
     */
    public static String toName(Unit<?> unit) {
        if (unit.getName() != null) {
            return unit.getName();
        }
        SimpleUnitFormat format = SimpleUnitFormat.getInstance();
        return format.format(unit);
    }
    /**
     * Unit symbol, willing to use {@link SimpleUnitFormat} to look up appropriate label if
     * required.
     *
     * <p>This allows us to format units like {@link Units#PIXEL}.
     */
    public static String toSymbol(Unit<?> unit) {
        SimpleUnitFormat format = SimpleUnitFormat.getInstance();
        return format.format(unit);
    }

    /**
     * Returns an equivalent unit instance based on the provided unit. First, it tries to get one of
     * the reference units defined in the JSR363 implementation in use. Units are considered
     * equivalent if the {@link Units#equals(Unit, Unit)} method returns true. If no equivalent
     * reference unit is defined, it returns the provided unit.
     */
    @SuppressWarnings("unchecked")
    public static <Q extends Quantity<Q>> Unit<Q> autoCorrect(Unit<Q> unit) {
        return DefaultUnitParser.getInstance(SimpleUnitFormat.Flavor.Default)
                .getEquivalentUnit(unit);
    }

    /**
     * Checks whether two units can be considered equivalent. TransformedUnits are considered
     * equivalent if they have the same system unit and their conversion factors to the system unit
     * produce the identity converter when the inverse of the second factor is concatenated with the
     * first factor, considering the precision of a float number. For other types of units, the
     * comparison is delegated to their normal equals method.
     */
    public static final boolean equals(Unit<?> unit1, Unit<?> unit2) {
        if (unit1 == unit2) {
            return true;
        }
        if (unit1 != null) {
            if (unit1 instanceof TransformedUnit<?>
                    && unit2 != null
                    && unit2 instanceof TransformedUnit<?>) {
                TransformedUnit<?> tunit1 = (TransformedUnit<?>) unit1;
                TransformedUnit<?> tunit2 = (TransformedUnit<?>) unit2;
                if (unit1.getSystemUnit().equals(unit2.getSystemUnit())) {
                    try {
                        float factor =
                                (float)
                                        tunit1.getSystemConverter()
                                                .concatenate(tunit2.getSystemConverter().inverse())
                                                .convert(1.0f);
                        // NOTE: Matching old JSR-275 library practice, converting to float to
                        // compare factors to provide some tolerance
                        if (factor == 1.0f) {
                            return true;
                        }
                        return false;
                    } catch (Exception e) {
                    }
                }
            }
            return unit1.equals(unit2);
        }
        return false;
    }

    /**
     * Gets a UnitConverter between two units, wrapping any raised exception in a
     * IllegalArgumentException.
     *
     * @throws IllegalArgumentException if unit1 can't be converter to unit2
     */
    public static UnitConverter getConverterToAny(Unit<?> fromUnit, Unit<?> toUnit) {
        try {
            return fromUnit.getConverterToAny(toUnit);
        } catch (UnconvertibleException | IncommensurableException e) {
            throw new IllegalArgumentException("Can't convert to the candidate unit", e);
        }
    }

    /**
     * Parses the text into an instance of unit
     *
     * @see UnitFormat#parse(CharSequence)
     * @throws ParserException if any problem occurs while parsing the specified character sequence
     *     (e.g. illegal syntax).
     * @throws UnsupportedOperationException if the {@link UnitFormat} is unable to parse.
     * @return A unit instance
     */
    public static Unit<?> parseUnit(String name) {
        return Units.autoCorrect(DefaultUnitParser.getInstance().parse(name));
    }
}
