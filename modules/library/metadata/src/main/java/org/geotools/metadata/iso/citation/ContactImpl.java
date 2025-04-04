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

import net.opengis.ows11.ContactType;
import org.geotools.api.metadata.citation.Address;
import org.geotools.api.metadata.citation.Contact;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.metadata.citation.Telephone;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;
import org.geotools.util.SimpleInternationalString;

/**
 * Information required to enable contact with the responsible person and/or organization.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 */
public class ContactImpl extends MetadataEntity implements Contact {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 3283637180253117382L;

    /**
     * Contact informations for the <A HREF="http://www.opengeospatial.org">Open Geospatial consortium</A>. "Open
     * Geospatial consortium" is the new name for "OpenGIS consortium".
     *
     * @see OnLineResourceImpl#OGC
     */
    public static final Contact OGC;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.OGC);
        c.freeze();
        OGC = c;
    }
    /**
     * Contact informations for the <A HREF="http://www.opengis.org">OpenGIS consortium</A>. "OpenGIS consortium" is the
     * old name for "Open Geospatial consortium".
     *
     * @see OnLineResourceImpl#OPEN_GIS
     */
    public static final Contact OPEN_GIS;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.OPEN_GIS);
        c.freeze();
        OPEN_GIS = c;
    }

    /**
     * Contact informations for the <A HREF="http://www.epsg.org">European Petroleum Survey Group</A>.
     *
     * @see OnLineResourceImpl#EPSG
     */
    public static final Contact EPSG;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.EPSG);
        c.freeze();
        EPSG = c;
    }

    /**
     * Contact informations for the <A HREF="http://www.remotesensing.org/geotiff/geotiff.html">GeoTIFF</A> group.
     *
     * @see OnLineResourceImpl#GEOTIFF
     */
    public static final Contact GEOTIFF;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.GEOTIFF);
        c.freeze();
        GEOTIFF = c;
    }

    /**
     * Contact informations for <A HREF="http://www.esri.com">ESRI</A>.
     *
     * @see OnLineResourceImpl#ESRI
     */
    public static final Contact ESRI;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.ESRI);
        c.freeze();
        ESRI = c;
    }

    /**
     * Contact informations for <A HREF="https://www.iau.org">IAU</A>.
     *
     * @see OnLineResourceImpl#IAU
     */
    public static final Contact IAU;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.IAU);
        c.freeze();
        IAU = c;
    }

    /**
     * Contact informations for <A HREF="http://www.oracle.com">Oracle</A>.
     *
     * @see OnLineResourceImpl#ORACLE
     */
    public static final Contact ORACLE;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.ORACLE);
        c.freeze();
        ORACLE = c;
    }

    /**
     * Contact informations for <A HREF="http://postgis.refractions.net">PostGIS</A>.
     *
     * @see OnLineResourceImpl#POSTGIS
     * @since 2.4
     */
    public static final Contact POSTGIS;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.POSTGIS);
        c.freeze();
        POSTGIS = c;
    }

    /**
     * Contact information for <A HREF="https://proj.org">PROJ</A>.
     *
     * @see OnLineResourceImpl#PROJ
     */
    public static final Contact PROJ;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.PROJ);
        c.freeze();
        PROJ = c;
    }

    /**
     * Contact informations for <A HREF="http://www.sun.com/">Sun Microsystems</A>.
     *
     * @see OnLineResourceImpl#SUN_MICROSYSTEMS
     * @since 2.2
     */
    public static final Contact SUN_MICROSYSTEMS;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.SUN_MICROSYSTEMS);
        c.freeze();
        SUN_MICROSYSTEMS = c;
    }

    /**
     * Contact informations for the <A HREF="http://www.geotools.org">Geotools</A> project.
     *
     * @see OnLineResourceImpl#GEOTOOLS
     */
    public static final Contact GEOTOOLS;

    static {
        final ContactImpl c = new ContactImpl(OnLineResourceImpl.GEOTOOLS);
        c.freeze();
        GEOTOOLS = c;
    }

    /** Supplemental instructions on how or when to contact the individual or organization. */
    private InternationalString contactInstructions;

    /** Time period (including time zone) when individuals can contact the organization or individual. */
    private InternationalString hoursOfService;

    /** On-line information that can be used to contact the individual or organization. */
    private OnLineResource onLineResource;

    /** Physical and email address at which the organization or individual may be contacted. */
    private Address address;

    /** Telephone numbers at which the organization or individual may be contacted. */
    private Telephone phone;

    /** Constructs an initially empty contact. */
    public ContactImpl() {
        // empty constructor, please use set methods and call
        // freeze before returning this instance to client code
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ContactImpl(final Contact source) {
        super(source);
    }

    /** Constructs a contact initialized to the specified online resource. */
    public ContactImpl(final OnLineResource resource) {
        setOnLineResource(resource);
    }

    public ContactImpl(ContactType contactInfo) {

        if (contactInfo != null) {

            if (contactInfo.getAddress() != null) {
                setAddress(new AddressImpl(contactInfo.getAddress()));
            }

            if (contactInfo.getContactInstructions() != null) {
                setContactInstructions(new SimpleInternationalString(contactInfo.getContactInstructions()));
            }

            if (contactInfo.getHoursOfService() != null) {
                setHoursOfService(new SimpleInternationalString(contactInfo.getHoursOfService()));
            }

            if (contactInfo.getOnlineResource() != null) {
                setOnLineResource(new OnLineResourceImpl(contactInfo.getOnlineResource()));
            }

            if (contactInfo.getPhone() != null) {
                setPhone(new TelephoneImpl(contactInfo.getPhone()));
            }
        }
    }

    /**
     * Returns the physical and email address at which the organization or individual may be contacted. Returns
     * {@code null} if none.
     */
    @Override
    public Address getAddress() {
        return address;
    }

    /** Set the physical and email address at which the organization or individual may be contacted. */
    public void setAddress(final Address newValue) {
        checkWritePermission();
        address = newValue;
    }

    /**
     * Returns supplemental instructions on how or when to contact the individual or organization. Returns {@code null}
     * if none.
     */
    @Override
    public InternationalString getContactInstructions() {
        return contactInstructions;
    }

    /** Set supplemental instructions on how or when to contact the individual or organization. */
    public void setContactInstructions(final InternationalString newValue) {
        checkWritePermission();
        contactInstructions = newValue;
    }

    /**
     * Return on-line information that can be used to contact the individual or organization. Returns {@code null} if
     * none.
     */
    @Override
    public OnLineResource getOnLineResource() {
        return onLineResource;
    }

    /** Set on-line information that can be used to contact the individual or organization. */
    public void setOnLineResource(final OnLineResource newValue) {
        checkWritePermission();
        onLineResource = newValue;
    }

    /**
     * Returns telephone numbers at which the organization or individual may be contacted. Returns {@code null} if none.
     */
    @Override
    public Telephone getPhone() {
        return phone;
    }

    /** Set telephone numbers at which the organization or individual may be contacted. */
    public void setPhone(final Telephone newValue) {
        checkWritePermission();
        phone = newValue;
    }

    /**
     * Returns time period (including time zone) when individuals can contact the organization or individual. Returns
     * {@code null} if none.
     */
    @Override
    public InternationalString getHoursOfService() {
        return hoursOfService;
    }

    /** Set time period (including time zone) when individuals can contact the organization or individual. */
    public void setHoursOfService(final InternationalString newValue) {
        checkWritePermission();
        hoursOfService = newValue;
    }
}
