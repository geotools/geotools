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

import javax.measure.unit.SI;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;


/**
 * A set of units to use in addition of {@link SI} and {@link NonSI}.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class Units {
    /**
     * Do not allows instantiation of this class.
     */
    private Units() {
    }

    /**
     * Pseudo-unit for sexagesimal degree. Numbers in this pseudo-unit has the following format:
     *
     * <cite>sign - degrees - decimal point - minutes (two digits) - integer seconds (two digits) -
     * fraction of seconds (any precision)</cite>.
     *
     * This unit is non-linear and not pratical for computation. Consequently, it should be
     * avoid as much as possible. Unfortunatly, this pseudo-unit is extensively used in the
     * EPSG database (code 9110).
     */
    public static final Unit<Angle> SEXAGESIMAL_DMS = NonSI.DEGREE_ANGLE.transform(
            SexagesimalConverter.FRACTIONAL.inverse()).asType(Angle.class);

    /**
     * Pseudo-unit for degree - minute - second. Numbers in this pseudo-unit has the following
     * format:
     *
     * <cite>signed degrees (integer) - arc-minutes (integer) - arc-seconds
     * (real, any precision)</cite>.
     *
     * This unit is non-linear and not pratical for computation. Consequently, it should be
     * avoid as much as possible. Unfortunatly, this pseudo-unit is extensively used in the
     * EPSG database (code 9107).
     */
    public static final Unit<Angle> DEGREE_MINUTE_SECOND = NonSI.DEGREE_ANGLE.transform(
            SexagesimalConverter.INTEGER.inverse()).asType(Angle.class);

    /**
     * Parts per million.
     */
    public static final Unit<Dimensionless> PPM = Unit.ONE.times(1E-6);

    /**
     * Associates the labels to units created in this class.
     */
    static {
        final UnitFormat format = UnitFormat.getInstance();
        format.label(SEXAGESIMAL_DMS,      "D.MS");
        format.label(DEGREE_MINUTE_SECOND, "DMS" );
        format.label(PPM,                  "ppm" );
    }
}
