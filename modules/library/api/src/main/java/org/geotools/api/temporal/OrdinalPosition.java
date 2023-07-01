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

import static org.geotools.api.annotation.Specification.ISO_19108;

import org.geotools.api.annotation.UML;

/**
 * A data type that shall be used for identifying temporal position within an ordinal temporal
 * reference system.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
@UML(identifier = "TM_OrdinalPosition", specification = ISO_19108)
public interface OrdinalPosition extends TemporalPosition {
    /**
     * Provides a reference to the ordinal era in which the instant occurs.
     *
     * @todo The method name doesn't match the return type.
     */
    @UML(identifier = "ordinalPosition", specification = ISO_19108)
    OrdinalEra getOrdinalPosition();
}
