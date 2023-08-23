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

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19115;

import java.util.Collection;
import org.geotools.api.annotation.UML;
import org.geotools.api.metadata.citation.Citation;

/**
 * Information identifying the portrayal catalogue used.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface PortrayalCatalogueReference {
    /**
     * Bibliographic reference to the portrayal catalogue cited.
     *
     * @return References to the portrayal catalogue cited.
     */
    @UML(
            identifier = "portrayalCatalogueCitation",
            obligation = MANDATORY,
            specification = ISO_19115)
    Collection<? extends Citation> getPortrayalCatalogueCitations();
}
