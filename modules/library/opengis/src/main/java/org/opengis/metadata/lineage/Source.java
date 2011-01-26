/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.lineage;

import java.util.Collection;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.identification.RepresentativeFraction;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.ReferenceSystem;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information about the source data used in creating the data specified by the scope.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.0
 */
@UML(identifier="LI_Source", specification=ISO_19115)
public interface Source {
    /**
     * Detailed description of the level of the source data.
     *
     * @return Description of the level of the source data, or {@code null}.
     */
    @UML(identifier="description", obligation=CONDITIONAL, specification=ISO_19115)
    InternationalString getDescription();

    /**
     * Denominator of the representative fraction on a source map.
     *
     * @return Representative fraction on a source map, or {@code null}.
     */
    @UML(identifier="scaleDenominator", obligation=OPTIONAL, specification=ISO_19115)
    RepresentativeFraction getScaleDenominator();

    /**
     * Spatial reference system used by the source data.
     *
     * @return Spatial reference system used by the source data, or {@code null}.
     */
    @UML(identifier="sourceReferenceSystem", obligation=OPTIONAL, specification=ISO_19115)
    ReferenceSystem getSourceReferenceSystem();

    /**
     * Recommended reference to be used for the source data.
     *
     * @return Recommended reference to be used for the source data, or {@code null}.
     */
    @UML(identifier="sourceCitation", obligation=OPTIONAL, specification=ISO_19115)
    Citation getSourceCitation();

    /**
     * Information about the spatial, vertical and temporal extent of the source data.
     *
     * @return Information about the extent of the source data.
     */
    @UML(identifier="sourceExtent", obligation=CONDITIONAL, specification=ISO_19115)
    Collection<? extends Extent> getSourceExtents();

    /**
     * Information about an event in the creation process for the source data.
     *
     * @return Information about an event in the creation process.
     */
    @UML(identifier="sourceStep", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends ProcessStep> getSourceSteps();
}
