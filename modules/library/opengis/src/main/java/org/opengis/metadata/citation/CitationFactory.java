/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.citation;

import java.net.URI;
import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.annotation.Obligation;
import org.opengis.referencing.Factory;


/**
 * A factory for metadata from the citation package.
 * All factory methods accept null value for {@linkplain Obligation#OPTIONAL optional} arguments.
 * The value must be non-null for {@linkplain Obligation#MANDATORY mandatory} arguments.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/citation/CitationFactory.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Jesse Crossley (SYS Technologies)
 * @since   GeoAPI 2.0
 */
public interface CitationFactory extends Factory {
    /**
     * Location of the responsible individual or organization.
     *
     * @param deliveryPoints          Address line for the location (as described in ISO 11180, Annex A).
     * @param city                    The city of the location.
     * @param administrativeArea      State, province of the location.
     * @param postalCode              ZIP or other postal code.
     * @param country                 Country of the physical address.
     * @param electronicMailAddresses Address of the electronic mailbox of the responsible organization or individual.
     * @return The address.
     */
    Address createAddress(
            Collection<String>  deliveryPoints,
            InternationalString city,
            InternationalString administrativeArea,
            String              postalCode,
            InternationalString country,
            Collection<String>  electronicMailAddresses);

    /**
     * Information required to enable contact with the responsible person and/or organization.
     *
     * @param phone               Telephone numbers at which the organization or individual may be contacted.
     * @param address             Physical and email address at which the organization or individual may be contacted.
     * @param onLineResource      On-line information that can be used to contact the individual or organization.
     * @param hoursOfService      Time period (including time zone) when individuals can contact the organization or individual.
     * @param contactInstructions Supplemental instructions on how or when to contact the individual or organization.
     * @return The contact.
     */
    Contact createContact(
            Telephone           phone,
            Address             address,
            OnLineResource      onLineResource,
            InternationalString hoursOfService,
            InternationalString contactInstructions);

    /**
     * Information about on-line sources from which the dataset, specification, or
     * community profile name and extended metadata elements can be obtained.
     *
     * @param linkage            Location (address) for on-line access.
     * @param protocol           Connection protocol to be used.
     * @param applicationProfile Name of an application profile that can be used with the online resource.
     * @param description        Detailed text description of what the online resource is/does.
     * @param function           Code for function performed by the online resource.
     * @return The online resource.
     */
    OnLineResource createOnLineResource(
            URI                 linkage,
            String              protocol,
            String              applicationProfile,
            InternationalString description,
            OnLineFunction      function);

    /**
     * Identification of, and means of communication with, person(s) and organizations associated with the dataset.
     * Only one of {@code individualName}, {@code organisationName} and {@code positionName} should be provided.
     *
     * @param individualName   Name of the responsible person- surname, given name, title separated by a delimiter.
     * @param organisationName Name of the responsible organization.
     * @param positionName     Role or position of the responsible person.
     * @param contactInfo      Address of the responsible party.
     * @param role             Function performed by the responsible party.
     * @return The responsible party.
     */
    ResponsibleParty createResponsibleParty(
            String              individualName,
            InternationalString organisationName,
            InternationalString positionName,
            Contact             contactInfo,
            Role                role);

    /**
     * Telephone numbers for contacting the responsible individual or organization.
     *
     * @param voice     Telephone number by which individuals can speak to the responsible organization or individual.
     * @param facsimile Telephone number of a facsimile machine for the responsible organization or individual.
     * @return The telephone.
     */
    Telephone createTelephone(
            String voice,
            String facsimile);
}
