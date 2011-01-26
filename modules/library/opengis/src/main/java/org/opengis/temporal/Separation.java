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

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Provides operations for calculating temporal length and distance.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 *
 * @todo The Javadoc suggest that this interface should extends some kind of
 *       {@linkplain TemporalGeometricPrimitive temporal geometric primitive}.
 */
@UML(identifier="TM_Separation", specification=ISO_19108)
public interface Separation {
    /**
     * Returns the distance from this {@linkplain TemporalGeometricPrimitive temporal geometric
     * primitive} to another {@linkplain TemporalGeometricPrimitive temporal geometric primitive}.
     * This is the absolute value of the difference b/n their temporal positions.
     */
    @UML(identifier="distance", obligation=MANDATORY, specification=ISO_19108)
    Duration distance(TemporalGeometricPrimitive other);

    /**
     * Return the duration of this {@linkplain TemporalGeometricPrimitive temporal geometric
     * primitive}.
     */
    @UML(identifier="length", obligation=MANDATORY, specification=ISO_19108)
    Duration length();
}
