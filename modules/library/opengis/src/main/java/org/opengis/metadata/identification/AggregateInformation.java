/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.identification;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.ISO_19115;


/**
 * Aggregate dataset information.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/identification/AggregateInformation.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Ely Conn (Leica Geosystems Geospatial Imaging, LLC)
 * @since   GeoAPI 2.1
 */
@UML(identifier="MD_AggregateInformation", specification=ISO_19115)
public interface AggregateInformation {
    /**
     * Citation information about the aggregate dataset.
     *
     * @return Citation information about the aggregate dataset, or {@code null}.
     */
    @UML(identifier="aggregateDataSetName", obligation=CONDITIONAL, specification=ISO_19115)
    Citation getAggregateDataSetName();

    /**
     * Identification information about aggregate dataset.
     *
     * @return Identification information about aggregate dataset, or {@code null}.
     */
    @UML(identifier="aggregateDataSetIdentifier", obligation=CONDITIONAL, specification=ISO_19115)
    Identifier getAggregateDataSetIdentifier();

    /**
     * Association type of the aggregate dataset.
     *
     * @return Association type of the aggregate dataset.
     */
    @UML(identifier="associationType", obligation=MANDATORY, specification=ISO_19115)
    AssociationType getAssociationType();

    /**
     * Type of initiative under which the aggregate dataset was produced.
     *
     * @return Type of initiative under which the aggregate dataset was produced, or {@code null}.
     */
    @UML(identifier="initiativeType", obligation=OPTIONAL, specification=ISO_19115)
    InitiativeType getInitiativeType();
}
