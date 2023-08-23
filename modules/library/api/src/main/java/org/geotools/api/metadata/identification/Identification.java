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

import java.util.Collection;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.metadata.citation.ResponsibleParty;
import org.geotools.api.metadata.constraint.Constraints;
import org.geotools.api.metadata.distribution.Format;
import org.geotools.api.metadata.maintenance.MaintenanceInformation;
import org.geotools.api.util.InternationalString;

/**
 * Basic information required to uniquely identify a resource or resources.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface Identification {
    /**
     * Citation data for the resource(s).
     *
     * @return Citation data for the resource(s).
     */
    Citation getCitation();

    /**
     * Brief narrative summary of the content of the resource(s).
     *
     * @return Brief narrative summary of the content.
     */
    InternationalString getAbstract();

    /**
     * Summary of the intentions with which the resource(s) was developed.
     *
     * @return The intentions with which the resource(s) was developed, or {@code null}.
     */
    InternationalString getPurpose();

    /**
     * Recognition of those who contributed to the resource(s).
     *
     * @return Recognition of those who contributed to the resource(s).
     */
    Collection<String> getCredits();

    /**
     * Status of the resource(s).
     *
     * @return Status of the resource(s), or {@code null}.
     */
    Collection<Progress> getStatus();

    /**
     * Identification of, and means of communication with, person(s) and organizations(s) associated
     * with the resource(s).
     *
     * @return Means of communication with person(s) and organizations(s) associated with the
     *     resource(s).
     */
    Collection<? extends ResponsibleParty> getPointOfContacts();

    /**
     * Provides information about the frequency of resource updates, and the scope of those updates.
     *
     * @return Frequency and scope of resource updates.
     */
    Collection<? extends MaintenanceInformation> getResourceMaintenance();

    /**
     * Provides a graphic that illustrates the resource(s) (should include a legend for the
     * graphic).
     *
     * @return A graphic that illustrates the resource(s).
     */
    Collection<? extends BrowseGraphic> getGraphicOverviews();

    /**
     * Provides a description of the format of the resource(s).
     *
     * @return Description of the format.
     */
    Collection<? extends Format> getResourceFormat();

    /**
     * Provides category keywords, their type, and reference source.
     *
     * @return Category keywords, their type, and reference source.
     */
    Collection<? extends Keywords> getDescriptiveKeywords();

    /**
     * Provides basic information about specific application(s) for which the resource(s) has/have
     * been or is being used by different users.
     *
     * @return Information about specific application(s) for which the resource(s) has/have been or
     *     is being used.
     */
    Collection<? extends Usage> getResourceSpecificUsages();

    /**
     * Provides information about constraints which apply to the resource(s).
     *
     * @return Constraints which apply to the resource(s).
     */
    Collection<? extends Constraints> getResourceConstraints();

    /**
     * Provides aggregate dataset information.
     *
     * @return Aggregate dataset information.
     * @since GeoAPI 2.1
     */
    Collection<? extends AggregateInformation> getAggregationInfo();
}
