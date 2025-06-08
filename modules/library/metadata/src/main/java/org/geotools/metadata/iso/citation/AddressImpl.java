/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.citation;

import java.util.Collection;
import java.util.Collections;
import net.opengis.ows11.AddressType;
import org.geotools.api.metadata.citation.Address;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;
import org.geotools.util.SimpleInternationalString;

/**
 * Location of the responsible individual or organization.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 * @since 2.1
 */
public class AddressImpl extends MetadataEntity implements Address {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 2278687294173262546L;

    /** State, province of the location. */
    private InternationalString administrativeArea;

    /** The city of the location */
    private InternationalString city;

    /** Country of the physical address. */
    private InternationalString country;

    /** ZIP or other postal code. */
    private String postalCode;

    /** Address line for the location (as described in ISO 11180, Annex A). */
    private Collection<String> deliveryPoints;

    /** Address of the electronic mailbox of the responsible organization or individual. */
    private Collection<String> electronicMailAddresses;

    /** Constructs an initially empty address. */
    public AddressImpl() {}

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public AddressImpl(final Address source) {
        super(source);
    }

    public AddressImpl(AddressType address) {
        if (address.getAdministrativeArea() != null)
            setAdministrativeArea(new SimpleInternationalString(address.getAdministrativeArea()));
        if (address.getCity() != null) setCity(new SimpleInternationalString(address.getCity()));
        if (address.getCountry() != null) setCountry(new SimpleInternationalString(address.getCountry()));
        if (address.getDeliveryPoint() != null) setDeliveryPoints(Collections.singleton(address.getDeliveryPoint()));
        if (address.getElectronicMailAddress() != null)
            setElectronicMailAddresses(Collections.singleton(address.getElectronicMailAddress()));
        if (address.getPostalCode() != null) setPostalCode(address.getPostalCode());
    }

    /** Return the state, province of the location. Returns {@code null} if unspecified. */
    @Override
    public InternationalString getAdministrativeArea() {
        return administrativeArea;
    }

    /** Set the state, province of the location. */
    public void setAdministrativeArea(final InternationalString newValue) {
        checkWritePermission();
        administrativeArea = newValue;
    }

    /** Returns the city of the location Returns {@code null} if unspecified. */
    @Override
    public InternationalString getCity() {
        return city;
    }

    /** Set the city of the location */
    public void setCity(final InternationalString newValue) {
        checkWritePermission();
        city = newValue;
    }

    /** Returns the country of the physical address. Returns {@code null} if unspecified. */
    @Override
    public InternationalString getCountry() {
        return country;
    }

    /** set the country of the physical address. */
    public void setCountry(final InternationalString newValue) {
        checkWritePermission();
        country = newValue;
    }

    /** Returns the address line for the location (as described in ISO 11180, Annex A). */
    @Override
    public Collection<String> getDeliveryPoints() {
        return deliveryPoints = nonNullCollection(deliveryPoints, String.class);
    }

    /** Set the address line for the location (as described in ISO 11180, Annex A). */
    public void setDeliveryPoints(final Collection<? extends String> newValues) {
        deliveryPoints = copyCollection(newValues, deliveryPoints, String.class);
    }

    /** Returns the address of the electronic mailbox of the responsible organization or individual. */
    @Override
    public Collection<String> getElectronicMailAddresses() {
        return electronicMailAddresses = nonNullCollection(electronicMailAddresses, String.class);
    }

    /** Set the address of the electronic mailbox of the responsible organization or individual. */
    public void setElectronicMailAddresses(final Collection<? extends String> newValues) {
        electronicMailAddresses = copyCollection(newValues, electronicMailAddresses, String.class);
    }

    /** Returns ZIP or other postal code. Returns {@code null} if unspecified. */
    @Override
    public String getPostalCode() {
        return postalCode;
    }

    /** Set ZIP or other postal code. */
    public void setPostalCode(final String newValue) {
        checkWritePermission();
        postalCode = newValue;
    }
}
