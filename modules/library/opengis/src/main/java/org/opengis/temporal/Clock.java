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
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Provides a basis for defining temporal position within a day.
 *
 * @author Alexander Petkov
 *
 * @todo Retrofit in the referencing framework.
 */
@UML(identifier="TM_Clock", specification=ISO_19108)
public interface Clock extends TemporalReferenceSystem {
    /**
     * Event used as the datum for this clock.
     */
    @UML(identifier="referenceEvent", obligation=MANDATORY, specification=ISO_19108)
    InternationalString getReferenceEvent();

    /**
     * Time of the reference Event for this clock, usually the origin of the clock scale.
     */
    @UML(identifier="ReferenceTime", obligation=MANDATORY, specification=ISO_19108)
    ClockTime getReferenceTime();

    /**
     * Provides the 24-hour local or UTC time that corresponds to the reference time.
     */
    @UML(identifier="utcReference", obligation=MANDATORY, specification=ISO_19108)
    ClockTime getUTCReference();

    /**
     * Converts UTC time to a time on this clock.
     */
    @UML(identifier="clkTrans", obligation=MANDATORY, specification=ISO_19108)
    ClockTime clkTrans(ClockTime clkTime);

    /**
     * Converts UTC time to a time on this clock.
     */
    @UML(identifier="utcTrans", obligation=MANDATORY, specification=ISO_19108)
    ClockTime utcTrans(ClockTime uTime);
}
