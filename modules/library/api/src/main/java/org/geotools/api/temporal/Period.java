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

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19108;

import org.geotools.api.annotation.UML;

/**
 * A one-dimensional geometric primitive that represent extent in time.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
@UML(identifier = "TM_Period", specification = ISO_19108)
public interface Period extends TemporalGeometricPrimitive {
    /** Links this period to the instant at which it starts. */
    @UML(identifier = "Beginning", obligation = MANDATORY, specification = ISO_19108)
    Instant getBeginning();

    /** Links this period to the instant at which it ends. */
    @UML(identifier = "Ending", obligation = MANDATORY, specification = ISO_19108)
    Instant getEnding();
}
