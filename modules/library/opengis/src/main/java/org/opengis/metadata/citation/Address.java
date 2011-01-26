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

import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Location of the responsible individual or organization.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="CI_Address", specification=ISO_19115)
public interface Address {
    /**
     * Address line for the location (as described in ISO 11180, Annex A).
     * Returns an empty collection if none.
     *
     * @return Address line for the location.
     */
    @UML(identifier="deliveryPoint", obligation=OPTIONAL, specification=ISO_19115)
    Collection<String> getDeliveryPoints();

    /**
     * The city of the location.
     * Returns {@code null} if unspecified.
     *
     * @return The city of the location, or {@code null}.
     */
    @UML(identifier="city", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getCity();

    /**
     * State, province of the location.
     * Returns {@code null} if unspecified.
     *
     * @return State, province of the location, or {@code null}.
     */
    @UML(identifier="administrativeArea", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getAdministrativeArea();

    /**
     * ZIP or other postal code.
     * Returns {@code null} if unspecified.
     *
     * @return ZIP or other postal code, or {@code null}.
     */
    @UML(identifier="postalCode", obligation=OPTIONAL, specification=ISO_19115)
    String getPostalCode();

    /**
     * Country of the physical address.
     * Returns {@code null} if unspecified.
     *
     * @return Country of the physical address, or {@code null}.
     */
    @UML(identifier="country", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getCountry();

    /**
     * Address of the electronic mailbox of the responsible organization or individual.
     * Returns an empty collection if none.
     *
     * @return Address of the electronic mailbox of the responsible organization or individual.
     */
    @UML(identifier="electronicMailAddress", obligation=OPTIONAL, specification=ISO_19115)
    Collection<String> getElectronicMailAddresses();
}
