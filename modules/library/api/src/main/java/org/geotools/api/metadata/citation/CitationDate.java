/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.citation;

import java.util.Date;

/**
 * Reference date and event used to describe it.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface CitationDate {
    /**
     * Reference date for the cited resource.
     *
     * @return Reference date for the cited resource.
     */
    Date getDate();

    /**
     * Event used for reference date.
     *
     * @return Event used for reference date.
     */
    DateType getDateType();
}
