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

import java.util.Collection;

/**
 * A zero-dimensional geometric primitive that represents position in time, equivalent to a point in
 * space.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov TODO There is a bit of a conflict in the spec document as to what should
 *     be returned for "position." The diagram shows that Position should be returned, while the
 *     text in the document demands that TemporalPosition should represent position in time.
 */
public interface Instant extends TemporalGeometricPrimitive {
    /** Get the position of this instant. */
    Position getPosition();

    /**
     * Get the Collection of temporal {@link Period}s, for which this Instant is the beginning. The
     * collection may be empty.
     *
     * @see Period#begin
     */
    Collection<Period> getBegunBy();

    /**
     * Get the Collection of temporal {@link Period}s, for which this Instant is the end. The
     * collection may be empty.
     *
     * @see Period#end
     */
    Collection<Period> getEndedBy();
}
