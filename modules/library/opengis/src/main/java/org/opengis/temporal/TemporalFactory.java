/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.temporal;

import java.util.Collection;
import java.util.Date;
import javax.measure.Unit;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.util.InternationalString;

/**
 * @author Open Geospatial Consortium
 * @author Guilhem Legal (Geomatys)
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.3
 */
public interface TemporalFactory {

    /**
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @return Calendar
     */
    Calendar createCalendar(ReferenceIdentifier name, Extent domainOfValidit);

    /**
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition, if
     *     not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for TM_TemporalPosition
     *     unless a subtype of TM_TemporalPosition is used as the data type.
     * @param calendarEraName : This is the name of the calendar era to which the date is
     *     referenced.
     * @param calendarDate : This is a sequence of positive integers in which the first
     *     integeridentifies a specific instance of the unit used at the highest level of the
     *     calendar hierarchy, the second integer identifies a specific instance of the unit used at
     *     the next lower level in the hierarchy, and so on. The format defined in ISO 8601 for
     *     dates in the Gregorian calendar may be used for any date that is composed of values for
     *     year, month and day.
     * @return CalendarDate
     */
    CalendarDate createCalendarDate(
            TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition,
            InternationalString calendarEraName,
            int[] calendarDate);

    /**
     * @param name : identify the calendar era within this calendar.
     * @param referenceEvent : provide the name or description of a mythical or historic event which
     *     fixes the position of the base scale of the calendar era.
     * @param referenceDate : provide the date of the reference referenceEvent expressed as a date
     *     in the given calendar. In most calendars, this date is the origin (i.e the first day) of
     *     the scale, but this is not always true.
     * @param julianReference : provide the Julian date that corresponds to the reference date.
     * @param epochOfUse : identify the TM_Period for which the calendar era was used as a basis for
     *     dating, the datatype for TM_Period.begin and Tm_Period.end shall be JulianDate.
     * @return CalendarEra
     */
    CalendarEra createCalendarEra(
            InternationalString name,
            InternationalString referenceEvent,
            CalendarDate referenceDate,
            JulianDate julianReference,
            Period epochOfUse);

    /**
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @param referenceEvent : Provide the name or description of an event, such as solar noon or
     *     sunrise.
     * @param referenceTime : Provide the time of day associated with the reference event expressed
     *     as a time of day in the given clock, the reference time is usually the origin of the
     *     clock scale.
     * @param utcReference : This is the 24-hour local or UTC time that corresponds to the reference
     *     time.
     * @return Clock
     */
    Clock createClock(
            ReferenceIdentifier name,
            Extent domainOfValidity,
            InternationalString referenceEvent,
            ClockTime referenceTime,
            ClockTime utcReference);

    /**
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition, if
     *     not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for TM_TemporalPosition
     *     unless a subtype of TM_TemporalPosition is used as the data type.
     * @param clockTime : This is a sequence of positive numbers with a structure similar to a
     *     CalendarDate.
     * @return ClockTime
     */
    ClockTime createClockTime(
            TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition,
            Number[] clockTime);

    /**
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition, if
     *     not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for TM_TemporalPosition
     *     unless a subtype of TM_TemporalPosition is used as the data type.
     * @param calendarEraName : This is the name of the calendar era to which the date is
     *     referenced.
     * @param calendarDate : This is a sequence of positive integers in which the first
     *     integeridentifies a specific instance of the unit used at the highest level of the
     *     calendar hierarchy, the second integer identifies a specific instance of the unit used at
     *     the next lower level in the hierarchy, and so on. The format defined in ISO 8601 for
     *     dates in the Gregorian calendar may be used for any date that is composed of values for
     *     year, month and day.
     * @param clockTime : This is a sequence of positive numbers with a structure similar to a
     *     CalendarDate.
     * @return DateAndTime
     */
    DateAndTime createDateAndTime(
            TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition,
            InternationalString calendarEraName,
            int[] calendarDate,
            Number[] clockTime);

    /**
     * @param instant : This is the position of this TM_Instant, it shall be associated with a
     *     single temporal reference system.
     * @return Instant
     */
    Instant createInstant(Position instant);

    /**
     * @param unit : This is the name of the unit of measure used to express the length of the
     *     interval.
     * @param radix : This is the base of the multiplier of the unit.
     * @param factor : This is the exponent of the base.
     * @param value : This is the length of the time interval as an integer multiple of one
     *     radix(exp -factor) of the specified unit.
     * @return IntervalLength
     */
    IntervalLength createIntervalLenght(Unit unit, int radix, int factor, int value);

    /**
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition, if
     *     not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for TM_TemporalPosition
     *     unless a subtype of TM_TemporalPosition is used as the data type.
     * @param coordinateValue : This is the distance from the scale origin expressed as a multiple
     *     of the standard interval associated with the temporal coordinate system.
     * @return JulianDate
     */
    JulianDate createJulianDate(
            TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition,
            Number coordinateValue);

    /**
     * @param name : This is a string that identifies the ordinal era within the
     *     TM_OrdinalReferenceSystem.
     * @param beginning : This is the temporal position at which the ordinal era began, if it is
     *     known.
     * @param end : This is the temporal position at which the ordinal era ended.
     * @return OrdinalEra
     */
    OrdinalEra createOrdinalEra(
            InternationalString name, Date beginning, Date end, Collection<OrdinalEra> composition);

    /**
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition, if
     *     not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for TM_TemporalPosition
     *     unless a subtype of TM_TemporalPosition is used as the data type.
     * @param ordinalPosition : This is a reference to the ordinal era in which the instant occurs.
     * @return OrdinalPosition
     */
    OrdinalPosition createOrdinalPosition(
            TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition,
            OrdinalEra ordinalPosition);

    /**
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @param ordinalEraSequence : An ordinal temporal reference system consists of a set of ordinal
     *     eras.
     * @return OrdinalReferenceSystem
     */
    OrdinalReferenceSystem createOrdinalReferenceSystem(
            ReferenceIdentifier name,
            Extent domainOfValidity,
            Collection<OrdinalEra> ordinalEraSequence);

    /**
     * @param begin : This is the TM_Instant at which this Period starts.
     * @param end : This is the TM_Instant at which this Period ends.
     * @return Period
     */
    Period createPeriod(Instant begin, Instant end);

    /** @return PeriodDuration */
    PeriodDuration createPeriodDuration(
            InternationalString years,
            InternationalString months,
            InternationalString week,
            InternationalString days,
            InternationalString hours,
            InternationalString minutes,
            InternationalString seconds);

    /**
     * @param position : this object represents one of the data types listed as : Date, Time,
     *     DateTime, and TemporalPosition with its subtypes
     * @return Position
     */
    Position createPosition(Date position);

    /**
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition, if
     *     not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for TM_TemporalPosition
     *     unless a subtype of TM_TemporalPosition is used as the data type.
     * @param coordinateValue : This is the distance from the scale origin expressed as a multiple
     *     of the standard interval associated with the temporal coordinate system.
     * @return TemporalCoordinate
     */
    TemporalCoordinate createTemporalCoordinate(
            TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition,
            Number coordinateValue);

    /**
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @param origin : The origin of the scale, it must be specified in the Gregorian calendar with
     *     time of day in UTC.
     * @param interval : The name of a single unit of measure used as the base interval for the
     *     scale. it shall be one of those units of measure for time specified by ISO 31-1, or a
     *     multiple of one of those units, as specified by ISO 1000.
     * @return TemporalCoordinateSystem
     */
    TemporalCoordinateSystem createTemporalCoordinateSystem(
            ReferenceIdentifier name,
            Extent domainOfValidity,
            Date origin,
            InternationalString interval);

    /**
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition, if
     *     not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for TM_TemporalPosition
     *     unless a subtype of TM_TemporalPosition is used as the data type.
     * @return TemporalPosition
     */
    TemporalPosition createTemporalPosition(
            TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition);

    /**
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @return TemporalReferenceSystem
     */
    TemporalReferenceSystem createTemporalReferenceSystem(
            ReferenceIdentifier name, Extent domainOfValidity);
}
