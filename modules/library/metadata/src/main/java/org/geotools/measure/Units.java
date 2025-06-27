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
import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.UnconvertibleException;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.quantity.Time;
import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.unit.TransformedUnit;

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

    // Angle Units

    public static final Unit<Angle> DEGREE_ANGLE = NonSI.DEGREE_ANGLE;

    /**
     * Pseudo-unit for degree - minute - second. Numbers in this pseudo-unit has the following format:
     *
     * <p><cite>signed degrees (integer) - arc-minutes (integer) - arc-seconds (real, any precision)</cite>.
     *
     * <p>This unit is non-linear and not practical for computation. Consequently, it should be avoid as much as
     * possible. Unfortunately, this pseudo-unit is extensively used in the EPSG database (code 9107).
     */
    public static final Unit<Angle> DEGREE_MINUTE_SECOND =
            NonSI.DEGREE_ANGLE.transform(SexagesimalConverter.INTEGER.inverse()).asType(Angle.class);

    public static final Unit<Angle> GRADE = USCustomary.GRADE;
    public static final Unit<Angle> MICRORADIAN = MetricPrefix.MICRO(tech.units.indriya.unit.Units.RADIAN);
    public static final Unit<Angle> MINUTE_ANGLE = NonSI.MINUTE_ANGLE;
    public static final Unit<Angle> RADIAN = tech.units.indriya.unit.Units.RADIAN;

    /**
     * Pseudo-unit for sexagesimal degree. Numbers in this pseudo-unit has the following format:
     *
     * <p><cite>sign - degrees - decimal point - minutes (two digits) - integer seconds (two digits) - fraction of
     * seconds (any precision)</cite>.
     *
     * <p>This unit is non-linear and not pratical for computation. Consequently, it should be avoid as much as
     * possible. Unfortunatly, this pseudo-unit is extensively used in the EPSG database (code 9110).
     */
    public static final Unit<Angle> SEXAGESIMAL_DMS = NonSI.DEGREE_ANGLE
            .transform(SexagesimalConverter.FRACTIONAL.inverse())
            .asType(Angle.class);

    public static final Unit<Angle> SECOND_ANGLE = NonSI.SECOND_ANGLE;

    // Dimensionless Units

    /** Parts per million. */
    public static final Unit<Dimensionless> PPM = AbstractUnit.ONE.multiply(1E-6);

    public static final Unit<Dimensionless> ONE = AbstractUnit.ONE;

    // Length Units

    public static final Unit<Length> FOOT = USCustomary.FOOT;
    public static final Unit<Length> METRE = tech.units.indriya.unit.Units.METRE;
    public static final Unit<Length> KILOMETER = MetricPrefix.KILO(METRE);
    public static final Unit<Length> NAUTICAL_MILE = USCustomary.NAUTICAL_MILE;
    static final Unit<Length> FOOT_GOLD_COAST =
            new TransformedUnit<>("Foot_Gold_Coast", SI.METRE, MultiplyConverter.of(0.3047997101815088));

    /** Length of <code>1/72</code> of a {@link USCustomary#INCH} */
    public static final Unit<Length> PIXEL = USCustomary.INCH.divide(72);

    /** Helper definition so someone can align to GeoTools Day Unit type * */
    public static final Unit<Time> DAY = SI.DAY;

    /** Time duration of <code>1/12</code> of a {@link SI#YEAR}. */
    public static final Unit<Time> MONTH = SI.YEAR.divide(12);

    public static final Unit<Time> YEAR = SI.YEAR;

    /**
     * Gets an instance of the default system-wide Unit format. Use this method instead of
     * {@code SimpleUnitFormat.getInstance()}, since custom Geotools units are not known to
     * {@code SimpleUnitFormat.getInstance()}.
     *
     * @see UnitFormat#getInstance()
     */
    public static UnitFormatter getDefaultFormat() {
        return UnitFormat.getInstance();
    }

    /**
     * Unit name, willing to use {@link UnitFormat} to look up appropriate label if a name has not been not defined.
     *
     * <p>This allows us to format units like {@link Units#PIXEL}.
     */
    public static String toName(Unit<?> unit) {
        if (unit.getName() != null) {
            return unit.getName();
        }
        UnitFormatter format = UnitFormat.getInstance();
        return format.format(unit);
    }
    /**
     * Unit symbol, willing to use {@link UnitFormat} to look up appropriate label if required.
     *
     * <p>This allows us to format units like {@link Units#PIXEL}.
     */
    public static String toSymbol(Unit<?> unit) {
        UnitFormatter format = UnitFormat.getInstance();
        return format.format(unit);
    }

    /**
     * Returns an equivalent unit instance based on the provided unit. First, it tries to get one of the reference units
     * defined in the JSR 385 implementation in use. Units are considered equivalent if the {@link Units#equals(Unit,
     * Unit)} method returns true. If no equivalent reference unit is defined, it returns the provided unit.
     */
    public static <Q extends Quantity<Q>> Unit<Q> autoCorrect(Unit<Q> unit) {
        return ((WktUnitFormat) WktUnitFormat.getInstance()).getEquivalentUnit(unit);
    }

    /**
     * Checks whether two units can be considered equivalent. TransformedUnits are considered equivalent if they have
     * the same system unit and their conversion factors to the system unit produce the identity converter when the
     * inverse of the second factor is concatenated with the first factor, considering the precision of a float number.
     * For other types of units, the comparison is delegated to their normal equals method.
     */
    public static final boolean equals(Unit<?> unit1, Unit<?> unit2) {
        if (unit1 == unit2) {
            return true;
        }
        if (unit1 != null) {
            if (unit1 instanceof TransformedUnit<?> && unit2 != null && unit2 instanceof TransformedUnit<?>) {
                TransformedUnit<?> tunit1 = (TransformedUnit<?>) unit1;
                TransformedUnit<?> tunit2 = (TransformedUnit<?>) unit2;
                if (unit1.getSystemUnit().equals(unit2.getSystemUnit())) {
                    try {
                        float factor = (float) tunit1.getSystemConverter()
                                .concatenate(tunit2.getSystemConverter().inverse())
                                .convert(1.0f);
                        // NOTE: Matching old JSR-275 library practice, converting to float to
                        // compare factors to provide some tolerance
                        if (factor == 1.0f) {
                            return true;
                        }
                        return false;
                    } catch (Exception e) {
                        // Fall through to standard equals comparison if conversion fails
                    }
                }
            }
            return unit1.equals(unit2);
        }
        return false;
    }

    /**
     * Gets a UnitConverter between two units, wrapping any raised exception in a IllegalArgumentException.
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
     * @see UnitFormatter#parse(CharSequence)
     * @throws javax.measure.format.MeasurementParseException if any problem occurs while parsing the specified
     *     character sequence (e.g. illegal syntax).
     * @throws UnsupportedOperationException if the {@link UnitFormatter} is unable to parse.
     * @return A unit instance
     */
    public static Unit<?> parseUnit(String name) {
        return Units.autoCorrect(WktUnitFormat.getInstance().parse(name));
    }
}
