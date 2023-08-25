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

import java.util.Collection;

/**
 * Telephone numbers for contacting the responsible individual or organization.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 1.0
 */
public interface Telephone {
    /**
     * Telephone numbers by which individuals can speak to the responsible organization or
     * individual.
     *
     * @return Telephone numbers by which individuals can speak to the responsible organization or
     *     individual.
     * @since GeoAPI 2.1
     */
    Collection<String> getVoices();

    /**
     * Telephone numbers of a facsimile machine for the responsible organization or individual.
     *
     * @return Telephone numbers of a facsimile machine for the responsible organization or
     *     individual.
     * @since GeoAPI 2.1
     */
    Collection<String> getFacsimiles();
}
