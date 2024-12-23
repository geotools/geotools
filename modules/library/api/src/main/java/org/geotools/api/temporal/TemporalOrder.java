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

/**
 * Provides an operation for determining the position of this {@linkplain TemporalPrimitive temporal primitive} relative
 * to another {@linkplain TemporalPrimitive temporal primitive}.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 * @todo The javadoc suggests that this interface should extends some kind of {@link TemporalPrimitive}.
 */
public interface TemporalOrder {
    /** Determines the position of this primitive relative to another primitive. */
    RelativePosition relativePosition(TemporalPrimitive other);
}
