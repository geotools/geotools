/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata;

import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19115;

import java.util.Collection;
import org.geotools.api.annotation.UML;
import org.geotools.api.metadata.citation.OnLineResource;

/**
 * Information describing metadata extensions.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface MetadataExtensionInformation {
    /**
     * Information about on-line sources containing the community profile name and the extended
     * metadata elements. Information for all new metadata elements.
     *
     * @return On-line sources to community profile name and extended metadata elements.
     */
    OnLineResource getExtensionOnLineResource();

    /**
     * Provides information about a new metadata element, not found in ISO 19115, which is required
     * to describe geographic data.
     *
     * @return New metadata element not found in ISO 19115.
     */
    @UML(
            identifier = "extendedElementInformation",
            obligation = OPTIONAL,
            specification = ISO_19115)
    Collection<? extends ExtendedElementInformation> getExtendedElementInformation();
}
