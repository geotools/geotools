/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 1998-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.TemporalDatum;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultTemporalCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * Formats a {@linkplain org.geotools.geometry.GeneralDirectPosition direct position}
 * in an arbitrary {@linkplain CoordinateReferenceSystem coordinate reference system}.
 * The format for each ordinate is infered from the coordinate system units using the
 * following rules:
 * <ul>
 *   <li>Ordinate values in {@linkplain NonSI#DEGREE_ANGLE degrees} are formated as angles
 *       using {@link AngleFormat}.</li>
 *   <li>Ordinate values in any unit compatible with {@linkplain SI#SECOND seconds}
 *       are formated as dates using {@link DateFormat}.</li>
 *   <li>All other values are formatted as numbers using {@link NumberFormat}.</li>
 * </ul>
 *
 * <strong>Note:</strong> parsing is not yet implemented in this version.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (PMO, IRD)
 */
public class CoordinateFormat extends Format {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8235685097881260737L;

    /**
     * The output coordinate reference system. May be {@code null}.
     */
    private CoordinateReferenceSystem crs;

    /**
     * The separator between each coordinate values to be formatted.
     */
    private String separator;

    /**
     * The formats to use for formatting. This array's length must be equals
     * to the {@linkplain #getCoordinateReferenceSystem coordinate system}'s
     * dimension. This array is never {@code null}.
     */
    private Format[] formats;

    /**
     * Formatter for units. Will be created only when first needed.
     */
    private transient UnitFormat unitFormat;

    /**
     * The type for each value in the {@code formats} array.
     * Types are: 0=number, 1=longitude, 2=latitude, 3=other angle,
     * 4=date, 5=ellapsed time. This array is never {@code null}.
     */
    private byte[] types;

    /**
     * Constants for the {@code types} array.
     */
    private static final byte LONGITUDE=1, LATITUDE=2, ANGLE=3, DATE=4, TIME=5;

    /**
     * The time epochs. Non-null only if at least one ordinate is a date.
     */
    private long[] epochs;

    /**
     * Conversions from temporal axis units to milliseconds.
     * Non-null only if at least one ordinate is a date.
     */
    private UnitConverter[] toMillis;

    /**
     * Dummy field position.
     */
    private final FieldPosition dummy = new FieldPosition(0);

    /**
     * The locale for formatting coordinates and numbers.
     */
    private final Locale locale;

    /**
     * Constructs a new coordinate format with default locale and a two-dimensional
     * {@linkplain DefaultGeographicCRS#WGS84 geographic (WGS 1984)} coordinate reference system.
     */
    public CoordinateFormat() {
        this(Locale.getDefault());
    }

    /**
     * Construct a new coordinate format for the specified locale and a two-dimensional
     * {@linkplain DefaultGeographicCRS#WGS84 geographic (WGS 1984)} coordinate reference system.
     *
     * @param locale The locale for formatting coordinates and numbers.
     */
    public CoordinateFormat(final Locale locale) {
        this(locale, DefaultGeographicCRS.WGS84);
    }

    /**
     * Constructs a new coordinate format for the specified locale and coordinate reference system.
     *
     * @param locale The locale for formatting coordinates and numbers.
     * @param crs    The output coordinate reference system.
     */
    public CoordinateFormat(final Locale locale, final CoordinateReferenceSystem crs) {
        this.locale = locale;
        this.separator = " ";
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Returns the coordinate reference system for points to be formatted.
     *
     * @return The output coordinate reference system.
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Set the coordinate reference system for points to be formatted. The number
     * of dimensions must matched the dimension of points to be formatted.
     *
     * @param crs The new coordinate system.
     */
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs) {
        if (CRS.equalsIgnoreMetadata(this.crs, (this.crs = crs))) {
            return;
        }
        Format numberFormat = null;
        Format  angleFormat = null;
        Format   dateFormat = null;
        /*
         * Reuses existing formats. It is necessary in order to avoid
         * overwritting any setting done with 'setNumberPattern(...)'
         * or 'setAnglePattern(...)'
         */
        if (formats != null) {
            for (int i=formats.length; --i>=0;) {
                final Format format = formats[i];
                if (format instanceof NumberFormat) {
                    numberFormat = format;
                } else if (format instanceof AngleFormat) {
                    angleFormat = format;
                } else if (format instanceof DateFormat) {
                    dateFormat = format;
                }
            }
        }
        /*
         * If no CRS were specified, formats everything as numbers. Working with null CRS is
         * sometime useful because null CRS are allowed in DirectPosition according ISO 19107.
         */
        if (crs == null) {
            if (numberFormat == null) {
                numberFormat = NumberFormat.getNumberInstance(locale);
            }
            types   = new byte[1];
            formats = new Format[] {numberFormat};
            return;
        }
        /*
         * Creates a new array of 'Format' objects, one for each dimension.
         * The format subclasses are infered from coordinate system axis.
         */
        final CoordinateSystem cs = crs.getCoordinateSystem();
        epochs   = null;
        toMillis = null;
        formats  = new Format[cs.getDimension()];
        types    = new byte[formats.length];
        for (int i=0; i<formats.length; i++) {
            final Unit<?> unit = cs.getAxis(i).getUnit();
            /////////////////
            ////  Angle  ////
            /////////////////
            if (NonSI.DEGREE_ANGLE.equals(unit)) {
                if (angleFormat == null) {
                    angleFormat = new AngleFormat("DD°MM.m'", locale);
                }
                formats[i] = angleFormat;
                final AxisDirection axis = cs.getAxis(i).getDirection().absolute();
                if (AxisDirection.EAST.equals(axis)) {
                    types[i] = LONGITUDE;
                } else if (AxisDirection.NORTH.equals(axis)) {
                    types[i] = LATITUDE;
                } else {
                    types[i] = ANGLE;
                }
                continue;
            }
            ////////////////
            ////  Date  ////
            ////////////////
            if (SI.SECOND.isCompatible(unit)) {
                final Datum datum = CRSUtilities.getDatum(CRSUtilities.getSubCRS(crs, i, i+1));
                if (datum instanceof TemporalDatum) {
                    if (toMillis == null) {
                        toMillis = new UnitConverter[formats.length];
                        epochs   = new long[formats.length];
                    }
                    toMillis[i] = unit.getConverterTo(DefaultTemporalCRS.MILLISECOND);
                    epochs  [i] = ((TemporalDatum) datum).getOrigin().getTime();
                    if (dateFormat == null) {
                        dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
                    }
                    formats[i] = dateFormat;
                    types  [i] = DATE;
                    continue;
                }
                types[i] = TIME;
                // Fallthrough: formatted as number for now.
                // TODO: Provide ellapsed time formatting later.
            }
            //////////////////
            ////  Number  ////
            //////////////////
            if (numberFormat == null) {
                numberFormat = NumberFormat.getNumberInstance(locale);
            }
            formats[i] = numberFormat;
            // types[i] default to 0.
        }
    }

    /**
     * Returns the separator between each coordinate (number, angle or date).
     *
     * @return The current coordinate separator.
     *
     * @since 2.2
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Set the separator between each coordinate.
     *
     * @param separator The new coordinate separator.
     *
     * @since 2.2
     */
    public void setSeparator(final String separator) {
        this.separator = separator;
    }

    /**
     * Set the pattern for numbers fields.  If some ordinates are formatted as plain number
     * (for example in {@linkplain org.geotools.referencing.cs.DefaultCartesianCS cartesian
     * coordinate system}), then those numbers will be formatted using this pattern.
     *
     * @param pattern The number pattern as specified in {@link DecimalFormat}.
     */
    public void setNumberPattern(final String pattern) {
        Format lastFormat = null;
        for (int i=0; i<formats.length; i++) {
            final Format format = formats[i];
            if (format!=lastFormat && (format instanceof DecimalFormat)) {
                ((DecimalFormat) format).applyPattern(pattern);
                lastFormat = format;
            }
        }
    }

    /**
     * Set the pattern for angles fields. If some ordinates are formatted as angle
     * (for example in {@linkplain org.geotools.referencing.cs.DefaultEllipsoidalCS
     * ellipsoidal coordinate system}), then those angles will be formatted using
     * this pattern.
     *
     * @param pattern The angle pattern as specified in {@link AngleFormat}.
     */
    public void setAnglePattern(final String pattern) {
        Format lastFormat = null;
        for (int i=0; i<formats.length; i++) {
            final Format format = formats[i];
            if (format!=lastFormat && (format instanceof AngleFormat)) {
                ((AngleFormat) format).applyPattern(pattern);
                lastFormat = format;
            }
        }
    }

    /**
     * Set the pattern for dates fields. If some ordinates are formatted as date (for example in
     * {@linkplain org.geotools.referencing.cs.DefaultTimeCS time coordinate system}), then
     * those dates will be formatted using this pattern.
     *
     * @param pattern The date pattern as specified in {@link SimpleDateFormat}.
     */
    public void setDatePattern(final String pattern) {
        Format lastFormat = null;
        for (int i=0; i<formats.length; i++) {
            final Format format = formats[i];
            if (format!=lastFormat && (format instanceof SimpleDateFormat)) {
                ((SimpleDateFormat) format).applyPattern(pattern);
                lastFormat = format;
            }
        }
    }

    /**
     * Set the time zone for dates fields. If some ordinates are formatted as date (for example in
     * {@linkplain org.geotools.referencing.cs.DefaultTimeCS time coordinate system}), then
     * those dates will be formatted using the specified time zone.
     *
     * @param timezone The time zone for dates.
     */
    public void setTimeZone(final TimeZone timezone) {
        Format lastFormat = null;
        for (int i=0; i<formats.length; i++) {
            final Format format = formats[i];
            if (format!=lastFormat && (format instanceof DateFormat)) {
                ((DateFormat) format).setTimeZone(timezone);
                lastFormat = format;
            }
        }
    }

    /**
     * Returns the format to use for formatting an ordinate at the given dimension.
     * The dimension parameter range from 0 inclusive to the
     * {@linkplain #getCoordinateReferenceSystem coordinate reference system}'s dimension,
     * exclusive. This method returns a direct reference to the internal format; any change
     * to the returned {@link Format} object will change the formatting for this
     * {@code CoordinateFormat} object.
     *
     * @param  dimension The dimension for the ordinate to format.
     * @return The format for the given dimension.
     * @throws IndexOutOfBoundsException if {@code dimension} is out of range.
     */
    public Format getFormat(final int dimension) throws IndexOutOfBoundsException {
        return formats[dimension];
    }

    /**
     * Formats a direct position. The position's dimension must matches the
     * {@linkplain #getCoordinateReferenceSystem coordinate reference system} dimension.
     *
     * @param  point The position to format.
     * @return The formatted position.
     * @throws IllegalArgumentException if this {@code CoordinateFormat}
     *         cannot format the given object.
     */
    public String format(final DirectPosition point) {
        return format(point, new StringBuffer(), null).toString();
    }

    /**
     * Formats a direct position and appends the resulting text to a given string buffer.
     * The position's dimension must matches the {@linkplain #getCoordinateReferenceSystem
     * coordinate reference system} dimension.
     *
     * @param point      The position to format.
     * @param toAppendTo Where the text is to be appended.
     * @param position   A {@code FieldPosition} identifying a field in the formatted text,
     *                   or {@code null} if none.
     * @return The string buffer passed in as {@code toAppendTo}, with formatted text appended.
     * @throws IllegalArgumentException if this {@code CoordinateFormat}
     *         cannot format the given object.
     */
    public StringBuffer format(final DirectPosition  point,
                               final StringBuffer    toAppendTo,
                               final FieldPosition   position)
            throws IllegalArgumentException
    {
        final int dimension = point.getDimension();
        final CoordinateSystem cs;
        if (crs != null) {
            if (dimension != formats.length) {
                throw new MismatchedDimensionException(Errors.format(
                            ErrorKeys.MISMATCHED_DIMENSION_$3, "point", dimension, formats.length));
            }
            cs = crs.getCoordinateSystem();
        } else {
            cs = null;
        }
        for (int i=0; i<dimension; i++) {
            final double value = point.getOrdinate(i);
            final int fi = Math.min(i, formats.length-1);
            final Object object;
            final byte type = types[fi];
            switch (type) {
                default:        object=Double.valueOf(value); break;
                case LONGITUDE: object=new Longitude (value); break;
                case LATITUDE:  object=new Latitude  (value); break;
                case ANGLE:     object=new Angle     (value); break;
                case DATE: {
                    final CoordinateSystemAxis axis = cs.getAxis(i);
                    long offset = Math.round(toMillis[fi].convert(value));
                    if (AxisDirection.PAST.equals(axis.getDirection())) {
                        offset = -offset;
                    }
                    object = new Date(epochs[fi] + offset);
                    break;
                }
            }
            if (i != 0) {
                toAppendTo.append(separator);
            }
            formats[fi].format(object, toAppendTo, dummy);
            /*
             * If the formatted value is a number, append the units.
             */
            if (type==0 && cs!=null) {
                final Unit<?> unit = cs.getAxis(i).getUnit();
                if (unit != null) {
                    if (unitFormat == null) {
                        unitFormat = UnitFormat.getInstance();
                    }
                    final String asText = unitFormat.format(unit);
                    if (asText.length() != 0) {
                        toAppendTo.append('\u00A0'); // No break space
                        toAppendTo.append(unit);
                    }
                }
            }
        }
        return toAppendTo;
    }

    /**
     * Formats a direct position and appends the resulting text to a given string buffer.
     * The position's dimension must matches the {@linkplain #getCoordinateReferenceSystem
     * coordinate reference system} dimension.
     *
     * @param object     The {@link DirectPosition} to format.
     * @param toAppendTo Where the text is to be appended.
     * @param position   A {@code FieldPosition} identifying a field in the formatted text,
     *                   or {@code null} if none.
     * @return The string buffer passed in as {@code toAppendTo}, with formatted text appended.
     * @throws NullPointerException if {@code toAppendTo} is null.
     * @throws IllegalArgumentException if this {@code CoordinateFormat}
     *         cannot format the given object.
     */
    public StringBuffer format(final Object        object,
                               final StringBuffer  toAppendTo,
                               final FieldPosition position)
            throws IllegalArgumentException
    {
        if (object instanceof DirectPosition) {
            return format((DirectPosition) object, toAppendTo, position);
        } else {
            throw new IllegalArgumentException(String.valueOf(object));
        }
    }

    /**
     * Not yet implemented.
     *
     * @param source The string to parse.
     * @param position The position of the first character to parse.
     */
    public DirectPosition parseObject(final String source, final ParsePosition position) {
        throw new UnsupportedOperationException("DirectPosition parsing not yet implemented.");
    }
}
