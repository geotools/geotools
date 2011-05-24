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

import javax.measure.unit.Unit;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A data type for intervals of time which supports the expression of duration in
 * terms of a specified multiple of a single unit of time.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 * @author Martin Desruisseaux (IRD)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/temporal/IntervalLength.java $
 */
@UML(identifier="TM_IntervalLength", specification=ISO_19108)
public interface IntervalLength extends Duration {
    /**
     * The unit of measure used to express the length of the interval.
     */
    @UML(identifier="unit", specification=ISO_19108)
    Unit getUnit();

    /**
     * A positive integer that is the base of the mulitplier of the unit.
     */
    @UML(identifier="radix", specification=ISO_19108)
    int getRadix();

    /**
     * The exponent of the base.
     */
    @UML(identifier="factor", specification=ISO_19108)
    int getFactor();

    /**
     * The length of the time interval as an integer multiple of one
     * {@linkplain #getRadix radix}<sup>(-{@linkplain #getFactor factor})</sup>
     * of the {@linkplain #getUnit specified unit}.
     */
    @UML(identifier="value", specification=ISO_19108)
    int getValue();
}
