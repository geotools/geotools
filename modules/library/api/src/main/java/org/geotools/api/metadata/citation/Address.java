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

import java.util.Collection;
import org.geotools.api.util.InternationalString;

/**
 * Location of the responsible individual or organization.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface Address {
    /**
     * Address line for the location (as described in ISO 11180, Annex A). Returns an empty collection if none.
     *
     * @return Address line for the location.
     */
    Collection<String> getDeliveryPoints();

    /**
     * The city of the location. Returns {@code null} if unspecified.
     *
     * @return The city of the location, or {@code null}.
     */
    InternationalString getCity();

    /**
     * State, province of the location. Returns {@code null} if unspecified.
     *
     * @return State, province of the location, or {@code null}.
     */
    InternationalString getAdministrativeArea();

    /**
     * ZIP or other postal code. Returns {@code null} if unspecified.
     *
     * @return ZIP or other postal code, or {@code null}.
     */
    String getPostalCode();

    /**
     * Country of the physical address. Returns {@code null} if unspecified.
     *
     * @return Country of the physical address, or {@code null}.
     */
    InternationalString getCountry();

    /**
     * Address of the electronic mailbox of the responsible organization or individual. Returns an empty collection if
     * none.
     *
     * @return Address of the electronic mailbox of the responsible organization or individual.
     */
    Collection<String> getElectronicMailAddresses();
}
