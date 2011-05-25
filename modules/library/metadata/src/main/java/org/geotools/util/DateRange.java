/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Date;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.quantity.Duration;
import javax.measure.converter.UnitConverter;
import javax.measure.converter.ConversionException;

import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A range of dates.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class DateRange extends Range<Date> {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -6400011350250757942L;

    /**
     * The unit used for time representation in a date.
     */
    private static final Unit<Duration> MILLISECOND = SI.MILLI(SI.SECOND);

    /**
     * Creates a new date range for the given dates. Start time and end time are inclusive.
     *
     * @param startTime The start time (inclusive), or {@code null} if none.
     * @param endTime   The end time (inclusive), or {@code null} if none.
     */
    public DateRange(final Date startTime, final Date endTime) {
        super(Date.class, clone(startTime), clone(endTime));
    }

    /**
     * Creates a new date range for the given dates.
     *
     * @param startTime     The start time, or {@code null} if none.
     * @param isMinIncluded {@code true} if the start time is inclusive.
     * @param endTime       The end time, or {@code null} if none.
     * @param isMaxIncluded {@code true} if the end time is inclusive.
     */
    public DateRange(final Date startTime, boolean isMinIncluded,
                     final Date   endTime, boolean isMaxIncluded)
    {
        super(Date.class, clone(startTime), isMinIncluded,
                          clone(  endTime), isMaxIncluded);
    }

    /**
     * Creates a date range from the specified measurement range. Units are converted as needed.
     *
     * @param range The range to convert.
     * @param origin The date to use as the origin.
     * @throws ConversionException if the given range doesn't have a
     *         {@linkplain MeasurementRange#getUnits unit} compatible with milliseconds.
     */
    public DateRange(final MeasurementRange<?> range, final Date origin) throws ConversionException {
        this(range, getConverter(range.getUnits()), origin.getTime());
    }

    /**
     * Workaround for RFE #4093999 ("Relax constraint on placement of this()/super()
     * call in constructors").
     */
    private DateRange(final MeasurementRange<?> range, final UnitConverter converter, final long origin)
            throws ConversionException
    {
        super(Date.class,
              new Date(origin + Math.round(converter.convert(range.getMinimum()))), range.isMinIncluded(),
              new Date(origin + Math.round(converter.convert(range.getMaximum()))), range.isMaxIncluded());
    }

    /**
     * Returns a clone of the specified date.
     */
    private static Date clone(final Date date) {
        return (date != null) ? (Date) date.clone() : null;
    }

    /**
     * Workaround for RFE #4093999 ("Relax constraint on placement of this()/super()
     * call in constructors").
     */
    private static UnitConverter getConverter(final Unit<?> source) throws ConversionException {
        if (source == null) {
            throw new ConversionException(Errors.format(ErrorKeys.NO_UNIT));
        }
        return source.getConverterTo(MILLISECOND);
    }

    /**
     * Returns the start time.
     */
    @Override
    public Date getMinValue() {
        return clone(super.getMinValue());
    }

    /**
     * Returns the end time.
     */
    @Override
    public Date getMaxValue() {
        return clone(super.getMaxValue());
    }
}
