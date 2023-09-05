/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.extent;

import org.geotools.api.temporal.TemporalPrimitive;

/**
 * Time period covered by the content of the dataset.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface TemporalExtent {
    /**
     * Returns the date and time for the content of the dataset.
     *
     * @return The date and time for the content.
     */
    TemporalPrimitive getExtent();
}
