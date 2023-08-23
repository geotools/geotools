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

import org.geotools.api.metadata.Identifier;

/**
 * Description of the geographic area using identifiers.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface GeographicDescription extends GeographicExtent {
    /**
     * Returns the identifier used to represent a geographic area.
     *
     * @return The identifier used to represent a geographic area.
     */
    Identifier getGeographicIdentifier();
}
