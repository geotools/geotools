/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.citation;

import org.geotools.api.util.InternationalString;

/**
 * Identification of, and means of communication with, person(s) and organizations associated with the dataset.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface ResponsibleParty {
    /**
     * Name of the responsible person- surname, given name, title separated by a delimiter. Only one of
     * {@code individualName}, {@link #getOrganisationName organisationName} and {@link #getPositionName positionName}
     * should be provided.
     *
     * @return Name, surname, given name and title of the responsible person, or {@code null}.
     */
    String getIndividualName();

    /**
     * Name of the responsible organization. Only one of {@link #getIndividualName individualName},
     * {@code organisationName} and {@link #getPositionName positionName} should be provided.
     *
     * @return Name of the responsible organization, or {@code null}.
     */
    InternationalString getOrganisationName();

    /**
     * Role or position of the responsible person. Only one of {@link #getIndividualName individualName},
     * {@link #getOrganisationName organisationName} and {@code positionName} should be provided.
     *
     * @return Role or position of the responsible person, or {@code null}
     */
    InternationalString getPositionName();

    /**
     * Address of the responsible party.
     *
     * @return Address of the responsible party., or {@code null}.
     */
    Contact getContactInfo();

    /**
     * Function performed by the responsible party.
     *
     * @return Function performed by the responsible party.
     */
    Role getRole();
}
