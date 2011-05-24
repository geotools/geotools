/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.extent;

import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information about spatial, vertical, and temporal extent.
 * This interface has four optional attributes
 * ({@linkplain #getGeographicElements geographic elements},
 *  {@linkplain #getTemporalElements temporal elements}, and
 *  {@linkplain #getVerticalElements vertical elements}) and an element called
 *  {@linkplain #getDescription description}.
 *  At least one of the four shall be used.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/extent/Extent.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="EX_Extent", specification=ISO_19115)
public interface Extent {
    /**
     * Returns the spatial and temporal extent for the referring object.
     *
     * @return The spatial and temporal extent, or {@code null} in none.
     */
    @UML(identifier="description", obligation=CONDITIONAL, specification=ISO_19115)
    InternationalString getDescription();

    /**
     * Provides geographic component of the extent of the referring object
     *
     * @return The geographic extent, or an empty set if none.
     */
    @UML(identifier="geographicElement", obligation=CONDITIONAL, specification=ISO_19115)
    Collection<? extends GeographicExtent> getGeographicElements();

    /**
     * Provides temporal component of the extent of the referring object
     *
     * @return The temporal extent, or an empty set if none.
     */
    @UML(identifier="temporalElement", obligation=CONDITIONAL, specification=ISO_19115)
    Collection<? extends TemporalExtent> getTemporalElements();

    /**
     * Provides vertical component of the extent of the referring object
     *
     * @return The vertical extent, or an empty set if none.
     */
    @UML(identifier="verticalElement", obligation=CONDITIONAL, specification=ISO_19115)
    Collection<? extends VerticalExtent> getVerticalElements();
}
