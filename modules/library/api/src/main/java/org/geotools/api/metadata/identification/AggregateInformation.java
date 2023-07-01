/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.identification;

import static org.geotools.api.annotation.Obligation.CONDITIONAL;
import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19115;

import org.geotools.api.annotation.UML;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.metadata.citation.Citation;

/**
 * Aggregate dataset information.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Ely Conn (Leica Geosystems Geospatial Imaging, LLC)
 * @since GeoAPI 2.1
 */
@UML(identifier = "MD_AggregateInformation", specification = ISO_19115)
public interface AggregateInformation {
    /**
     * Citation information about the aggregate dataset.
     *
     * @return Citation information about the aggregate dataset, or {@code null}.
     */
    @UML(identifier = "aggregateDataSetName", obligation = CONDITIONAL, specification = ISO_19115)
    Citation getAggregateDataSetName();

    /**
     * Identification information about aggregate dataset.
     *
     * @return Identification information about aggregate dataset, or {@code null}.
     */
    @UML(
            identifier = "aggregateDataSetIdentifier",
            obligation = CONDITIONAL,
            specification = ISO_19115)
    Identifier getAggregateDataSetIdentifier();

    /**
     * Association type of the aggregate dataset.
     *
     * @return Association type of the aggregate dataset.
     */
    @UML(identifier = "associationType", obligation = MANDATORY, specification = ISO_19115)
    AssociationType getAssociationType();

    /**
     * Type of initiative under which the aggregate dataset was produced.
     *
     * @return Type of initiative under which the aggregate dataset was produced, or {@code null}.
     */
    @UML(identifier = "initiativeType", obligation = OPTIONAL, specification = ISO_19115)
    InitiativeType getInitiativeType();
}
