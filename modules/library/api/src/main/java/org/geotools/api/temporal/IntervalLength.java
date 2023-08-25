/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

import javax.measure.Unit;

/**
 * A data type for intervals of time which supports the expression of duration in terms of a
 * specified multiple of a single unit of time.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 * @author Martin Desruisseaux (IRD)
 */
public interface IntervalLength extends Duration {
    /** The unit of measure used to express the length of the interval. */
    Unit getUnit();

    /** A positive integer that is the base of the mulitplier of the unit. */
    int getRadix();

    /** The exponent of the base. */
    int getFactor();

    /**
     * The length of the time interval as an integer multiple of one {@linkplain #getRadix
     * radix}<sup>(-{@linkplain #getFactor factor})</sup> of the {@linkplain #getUnit specified
     * unit}.
     */
    int getValue();
}
