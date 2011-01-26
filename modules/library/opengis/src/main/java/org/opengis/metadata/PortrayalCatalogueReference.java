/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata;

import java.util.Collection;
import org.opengis.metadata.citation.Citation;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information identifying the portrayal catalogue used.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_PortrayalCatalogueReference", specification=ISO_19115)
public interface PortrayalCatalogueReference {
    /**
     * Bibliographic reference to the portrayal catalogue cited.
     *
     * @return References to the portrayal catalogue cited.
     */
    @UML(identifier="portrayalCatalogueCitation", obligation=MANDATORY, specification=ISO_19115)
    Collection<? extends Citation> getPortrayalCatalogueCitations();
}
