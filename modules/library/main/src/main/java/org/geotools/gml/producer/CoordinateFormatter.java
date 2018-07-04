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
package org.geotools.gml.producer;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formats coordinates with a given number of decimals, using code more efficient than NumberFormat
 * when possible. The class is not thread safe, create a new instance for each thread using it.
 */
public final class CoordinateFormatter {

    /**
     * The min value at which the decimal notation is used (below it, the computerized scientific
     * one is used instead)
     */
    private static final double DECIMAL_MIN = Math.pow(10, -3);

    /**
     * The max value at which the decimal notation is used (above it, the computerized scientific
     * one is used instead)
     */
    private static final double DECIMAL_MAX = Math.pow(10, 7);

    /** To be used for formatting numbers, uses US locale. */
    private final NumberFormat coordFormatter = NumberFormat.getInstance(Locale.US);

    private final FieldPosition ZERO = new FieldPosition(0);

    /**
     * The power of ten used for fast rounding, computed using the provided number of decimal values
     */
    private final double scale;

    /** Whether we have to format in plain decimal numbers, or we can use scientific notation */
    private boolean forcedDecimal;

    public CoordinateFormatter(int numDecimals) {
        coordFormatter.setMaximumFractionDigits(numDecimals);
        coordFormatter.setGroupingUsed(false);
        scale = Math.pow(10, numDecimals);
    }

    /**
     * Formats a number with the configured number of decimals. For better performance best use
     * {@link #format(double, StringBuffer)} against a re-used StringBuffer
     *
     * @param x
     * @param sb
     */
    public String format(double x) {
        StringBuffer sb = new StringBuffer();
        format(x, sb);
        return sb.toString();
    }

    /**
     * Formats a number with the configured number of decimals
     *
     * @param x
     * @param sb
     */
    public StringBuffer format(double x, StringBuffer sb) {
        if ((Math.abs(x) >= DECIMAL_MIN && x < DECIMAL_MAX) || x == 0) {
            x = truncate(x);
            long lx = (long) x;
            if (lx == x) {
                sb.append(lx);
            } else {
                sb.append(x);
            }
        } else {
            if (forcedDecimal) {
                coordFormatter.format(x, sb, ZERO);
            } else {
                sb.append(truncate(x));
            }
        }

        return sb;
    }

    final double truncate(double x) {
        // scale the number multiplying it by the power of 10 of the desired decimals
        //  e.g. if we want 8 decimals: 3.123456786 * 10E8 = 312345678.6
        double scaled = x * scale;
        // add 0.5 to round the decimal part, e.g 312345678.6 + 0.5 = 312345679.1
        scaled += 0.5;
        // take only the decimal part, e.g.  312345679
        scaled = Math.floor(scaled);
        // remove the scale factor, the number will now have the desired number of decimals
        return scaled / scale;
    }

    /**
     * Returns the maximum number of digits allowed in the fraction portion of a number.
     *
     * @return the maximum number of digits.
     */
    public int getMaximumFractionDigits() {
        return coordFormatter.getMaximumFractionDigits();
    }

    /**
     * Returns the force decimal flag, see {@link #setForcedDecimal(boolean)}
     *
     * @return
     */
    public boolean isForcedDecimal() {
        return forcedDecimal;
    }

    /**
     * When set to true, forces decimal representation of numbers, otherwise allows scientific
     * notation too (for very large of very small numbers). False by default.
     *
     * @param forcedDecimal
     */
    public void setForcedDecimal(boolean forcedDecimal) {
        this.forcedDecimal = forcedDecimal;
    }
}
