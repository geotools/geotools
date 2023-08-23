/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.lineage;

import java.util.Collection;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.metadata.identification.RepresentativeFraction;
import org.geotools.api.referencing.ReferenceSystem;
import org.geotools.api.util.InternationalString;

/**
 * Information about the source data used in creating the data specified by the scope.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface Source {
    /**
     * Detailed description of the level of the source data.
     *
     * @return Description of the level of the source data, or {@code null}.
     */
    InternationalString getDescription();

    /**
     * Denominator of the representative fraction on a source map.
     *
     * @return Representative fraction on a source map, or {@code null}.
     */
    RepresentativeFraction getScaleDenominator();

    /**
     * Spatial reference system used by the source data.
     *
     * @return Spatial reference system used by the source data, or {@code null}.
     */
    ReferenceSystem getSourceReferenceSystem();

    /**
     * Recommended reference to be used for the source data.
     *
     * @return Recommended reference to be used for the source data, or {@code null}.
     */
    Citation getSourceCitation();

    /**
     * Information about the spatial, vertical and temporal extent of the source data.
     *
     * @return Information about the extent of the source data.
     */
    Collection<? extends Extent> getSourceExtents();

    /**
     * Information about an event in the creation process for the source data.
     *
     * @return Information about an event in the creation process.
     */
    Collection<? extends ProcessStep> getSourceSteps();
}
