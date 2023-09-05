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
 * A one-dimensional geometric primitive that represent extent in time.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
public interface Period extends TemporalGeometricPrimitive {
    /** Links this period to the instant at which it starts. */
    Instant getBeginning();

    /** Links this period to the instant at which it ends. */
    Instant getEnding();
}
