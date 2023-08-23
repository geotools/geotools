/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.quality;

import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.util.InternationalString;

/**
 * Information about the outcome of evaluating the obtained value (or set of values) against a
 * specified acceptable conformance quality level.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface ConformanceResult extends Result {
    /**
     * Citation of product specification or user requirement against which data is being evaluated.
     *
     * @return Citation of product specification or user requirement.
     */
    Citation getSpecification();

    /**
     * Explanation of the meaning of conformance for this result.
     *
     * @return Explanation of the meaning of conformance.
     */
    InternationalString getExplanation();

    /**
     * Indication of the conformance result.
     *
     * @return Indication of the conformance result.
     */
    boolean pass();
}
