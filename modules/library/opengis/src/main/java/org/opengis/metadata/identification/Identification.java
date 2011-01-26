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

import java.util.Collection;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.maintenance.MaintenanceInformation;
import org.opengis.metadata.constraint.Constraints;
import org.opengis.metadata.distribution.Format;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;


/**
 * Basic information required to uniquely identify a resource or resources.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.0
 */
@Profile (level=CORE)
@UML(identifier="MD_Identification", specification=ISO_19115)
public interface Identification {
    /**
     * Citation data for the resource(s).
     *
     * @return Citation data for the resource(s).
     */
    @Profile (level=CORE)
    @UML(identifier="citation", obligation=MANDATORY, specification=ISO_19115)
    Citation getCitation();

    /**
     * Brief narrative summary of the content of the resource(s).
     *
     * @return Brief narrative summary of the content.
     */
    @Profile (level=CORE)
    @UML(identifier="abstract", obligation=MANDATORY, specification=ISO_19115)
    InternationalString getAbstract();

    /**
     * Summary of the intentions with which the resource(s) was developed.
     *
     * @return The intentions with which the resource(s) was developed, or {@code null}.
     */
    @UML(identifier="purpose", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getPurpose();

    /**
     * Recognition of those who contributed to the resource(s).
     *
     * @return Recognition of those who contributed to the resource(s).
     */
    @UML(identifier="credit", obligation=OPTIONAL, specification=ISO_19115)
    Collection<String> getCredits();

    /**
     * Status of the resource(s).
     *
     * @return Status of the resource(s), or {@code null}.
     */
    @UML(identifier="status", obligation=OPTIONAL, specification=ISO_19115)
    Collection<Progress> getStatus();

    /**
     * Identification of, and means of communication with, person(s) and organizations(s)
     * associated with the resource(s).
     *
     * @return Means of communication with person(s) and organizations(s) associated with the
     *         resource(s).
     */
    @Profile (level=CORE)
    @UML(identifier="pointOfContact", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends ResponsibleParty> getPointOfContacts();

    /**
     * Provides information about the frequency of resource updates, and the scope of those updates.
     *
     * @return Frequency and scope of resource updates.
     */
    @UML(identifier="resourceMaintenance", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends MaintenanceInformation> getResourceMaintenance();

    /**
     * Provides a graphic that illustrates the resource(s) (should include a legend for the graphic).
     *
     * @return A graphic that illustrates the resource(s).
     */
    @UML(identifier="graphicOverview", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends BrowseGraphic> getGraphicOverviews();

    /**
     * Provides a description of the format of the resource(s).
     *
     * @return Description of the format.
     */
    @UML(identifier="resourceFormat", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends Format> getResourceFormat();

    /**
     * Provides category keywords, their type, and reference source.
     *
     * @return Category keywords, their type, and reference source.
     */
    @UML(identifier="descriptiveKeywords", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends Keywords> getDescriptiveKeywords();

    /**
     * Provides basic information about specific application(s) for which the resource(s)
     * has/have been or is being used by different users.
     *
     * @return Information about specific application(s) for which the resource(s)
     *         has/have been or is being used.
     */
    @UML(identifier="resourceSpecificUsage", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends Usage> getResourceSpecificUsages();

    /**
     * Provides information about constraints which apply to the resource(s).
     *
     * @return Constraints which apply to the resource(s).
     */
    @UML(identifier="resourceConstraints", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends Constraints> getResourceConstraints();

    /**
     * Provides aggregate dataset information.
     *
     * @return Aggregate dataset information.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="aggregationInfo", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends AggregateInformation> getAggregationInfo();
}
