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

import java.net.URI;
import java.net.URISyntaxException;
import org.opengis.metadata.citation.Contact;
import org.opengis.metadata.citation.OnLineFunction;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.citation.Role;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;
import org.geotools.util.logging.Logging;
import org.geotools.util.SimpleInternationalString;


/**
 * Identification of, and means of communication with, person(s) and
 * organizations associated with the dataset.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 */
public class ResponsiblePartyImpl extends MetadataEntity implements ResponsibleParty {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2477962229031486552L;

    /**
     * The name of Open Geospatial Consortium as an international string.
     *
     * @todo Localize.
     */
    static final InternationalString OGC_NAME =
            new SimpleInternationalString("Open Geospatial Consortium");

    /**
     * Creates a responsible party metadata entry for OGC involvement.
     * The organisation name is automatically set to "Open Geospatial Consortium".
     *
     * @param role     The OGC role (point of contact, owner, etc.) for a resource.
     * @param resource The URI to the resource.
     * @return Responsible party describing OGC involvement.
     *
     * @since 2.2
     */
    public static ResponsibleParty OGC(final Role role, final OnLineResource resource) {
        final ContactImpl contact = new ContactImpl(resource);
        contact.freeze();

        final ResponsiblePartyImpl ogc = new ResponsiblePartyImpl(role);
        ogc.setOrganisationName(OGC_NAME);
        ogc.setContactInfo(contact);
        ogc.freeze();

        return ogc;
    }

    /**
     * Creates a responsible party metadata entry for OGC involvement.
     * The organisation name is automatically set to "Open Geospatial Consortium".
     *
     * @param role           The OGC role (point of contact, owner, etc.) for a resource.
     * @param function       The OGC function (information, download, etc.) for a resource.
     * @param onlineResource The URI to the resource.
     * @return Responsible party describing OGC involvement.
     */
    public static ResponsibleParty OGC(final Role role,
                                       final OnLineFunction function,
                                       final URI onlineResource)
    {
        final OnLineResourceImpl resource = new OnLineResourceImpl(onlineResource);
        resource.setFunction(function);
        resource.freeze();
        return OGC(role, resource);
    }

    /**
     * Creates a responsible party metadata entry for OGC involvement.
     * The organisation name is automatically set to "Open Geospatial Consortium".
     *
     * @param role           The OGC role (point of contact, owner, etc.) for a resource.
     * @param function       The OGC function (information, download, etc.) for a resource.
     * @param onlineResource The URI on the resource.
     * @return Responsible party describing OGC involvement.
     */
    static ResponsibleParty OGC(final Role role,
                                final OnLineFunction function,
                                final String onlineResource)
    {
        try {
            return OGC(role, function, new URI(onlineResource));
        }
        catch (URISyntaxException badContact) {
            Logging.unexpectedException("org.geotools.metadata.iso", ResponsibleParty.class, "OGC",
                                        badContact);
            return OGC;
        }
    }

    /**
     * The <A HREF="http://www.opengeospatial.org">Open Geospatial consortium</A> responsible party.
     * "Open Geospatial consortium" is the new name for "OpenGIS consortium".
     *
     * @see ContactImpl#OGC
     */
    public static ResponsibleParty OGC;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.RESOURCE_PROVIDER);
        r.setOrganisationName(OGC_NAME);
        r.setContactInfo(ContactImpl.OGC);
        r.freeze();
        OGC = r;
    }

    /**
     * The <A HREF="http://www.opengis.org">OpenGIS consortium</A> responsible party.
     * "OpenGIS consortium" is the old name for "Open Geospatial consortium".
     *
     * @see ContactImpl#OPEN_GIS
     */
    public static ResponsibleParty OPEN_GIS;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.RESOURCE_PROVIDER);
        r.setOrganisationName(new SimpleInternationalString("OpenGIS consortium"));
        r.setContactInfo(ContactImpl.OPEN_GIS);
        r.freeze();
        OPEN_GIS = r;
    }

    /**
     * The <A HREF="http://www.epsg.org">European Petroleum Survey Group</A> responsible party.
     *
     * @see ContactImpl#EPSG
     */
    public static ResponsibleParty EPSG;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.PRINCIPAL_INVESTIGATOR);
        r.setOrganisationName(new SimpleInternationalString("European Petroleum Survey Group"));
        r.setContactInfo(ContactImpl.EPSG);
        r.freeze();
        EPSG = r;
    }

    /**
     * The <A HREF="http://www.remotesensing.org/geotiff/geotiff.html">GeoTIFF</A> responsible
     * party.
     *
     * @see ContactImpl#GEOTIFF
     */
    public static ResponsibleParty GEOTIFF;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.PRINCIPAL_INVESTIGATOR);
        r.setOrganisationName(new SimpleInternationalString("GeoTIFF"));
        r.setContactInfo(ContactImpl.GEOTIFF);
        r.freeze();
        GEOTIFF = r;
    }

    /**
     * The <A HREF="http://www.esri.com">ESRI</A> responsible party.
     *
     * @see ContactImpl#ESRI
     */
    public static ResponsibleParty ESRI;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.OWNER);
        r.setOrganisationName(new SimpleInternationalString("ESRI"));
        r.setContactInfo(ContactImpl.ESRI);
        r.freeze();
        ESRI = r;
    }

    /**
     * The <A HREF="http://www.oracle.com">Oracle</A> responsible party.
     *
     * @see ContactImpl#ORACLE
     */
    public static ResponsibleParty ORACLE;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.OWNER);
        r.setOrganisationName(new SimpleInternationalString("Oracle"));
        r.setContactInfo(ContactImpl.ORACLE);
        r.freeze();
        ORACLE = r;
    }

    /**
     * The <A HREF="http://postgis.refractions.net">PostGIS</A> responsible party.
     *
     * @see ContactImpl#POSTGIS
     *
     * @since 2.4
     */
    public static ResponsibleParty POSTGIS;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.PRINCIPAL_INVESTIGATOR);
        r.setOrganisationName(new SimpleInternationalString("PostGIS"));
        r.setContactInfo(ContactImpl.POSTGIS);
        r.freeze();
        POSTGIS = r;
    }

    /**
     * The <A HREF="http://www.sun.com/">Sun Microsystems</A> party.
     *
     * @see ContactImpl#SUN_MICROSYSTEMS
     *
     * @since 2.2
     */
    public static ResponsibleParty SUN_MICROSYSTEMS;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.PRINCIPAL_INVESTIGATOR);
        r.setOrganisationName(new SimpleInternationalString("Sun Microsystems"));
        r.setContactInfo(ContactImpl.SUN_MICROSYSTEMS);
        r.freeze();
        SUN_MICROSYSTEMS = r;
    }

    /**
     * The <A HREF="http://www.geotools.org">Geotools</A> project.
     *
     * @see ContactImpl#GEOTOOLS
     */
    public static ResponsibleParty GEOTOOLS;
    static {
        final ResponsiblePartyImpl r = new ResponsiblePartyImpl(Role.PRINCIPAL_INVESTIGATOR);
        r.setOrganisationName(new SimpleInternationalString("Geotools"));
        r.setContactInfo(ContactImpl.GEOTOOLS);
        r.freeze();
        GEOTOOLS = r;
    }

    /**
     * Name of the responsible person- surname, given name, title separated by a delimiter.
     */
    private String individualName;

    /**
     * Name of the responsible organization.
     */
    private InternationalString organisationName;

    /**
     * Role or position of the responsible person
     */
    private InternationalString positionName;

    /**
     * Address of the responsible party.
     */
    private Contact contactInfo;

    /**
     * Function performed by the responsible party.
     */
    private Role role;

    /**
     * Constructs an initially empty responsible party.
     */
    public ResponsiblePartyImpl() {
    }

    /**
     * Constructs a new responsible party initialized to the values specified by the given object.
     * This constructor performs a shallow copy (i.e. each source attributes are reused without
     * copying them).
     *
     * @since 2.2
     */
    public ResponsiblePartyImpl(final ResponsibleParty source) {
        super(source);
    }

    /**
     * Constructs a responsability party with the given role.
     */
    public ResponsiblePartyImpl(final Role role) {
        setRole(role);
    }

    /**
     * Returns the name of the responsible person- surname, given name, title separated by a delimiter.
     * Only one of {@code individualName}, {@link #getOrganisationName organisationName}
     * and {@link #getPositionName positionName} should be provided.
     */
    public String getIndividualName() {
        return individualName;
    }

    /**
     * Set the name of the responsible person- surname, given name, title separated by a delimiter.
     * Only one of {@code individualName}, {@link #getOrganisationName organisationName}
     * and {@link #getPositionName positionName} should be provided.
     */
    public synchronized void setIndividualName(final String newValue) {
        checkWritePermission();
        individualName = newValue;
    }

    /**
     * Returns the name of the responsible organization.
     * Only one of {@link #getIndividualName individualName}, </code>organisationName</code>
     * and {@link #getPositionName positionName} should be provided.
     */
    public InternationalString getOrganisationName() {
        return organisationName;
    }

    /**
     * Set the name of the responsible organization.
     * Only one of {@link #getIndividualName individualName}, </code>organisationName</code>
     * and {@link #getPositionName positionName} should be provided.
     */
    public synchronized void setOrganisationName(final InternationalString newValue) {
        checkWritePermission();
        organisationName = newValue;
    }

    /**
     * Returns the role or position of the responsible person
     * Only one of {@link #getIndividualName individualName},
     * {@link #getOrganisationName organisationName} and {@code positionName}
     * should be provided.
     */
    public InternationalString getPositionName() {
        return positionName;
    }

    /**
     * set the role or position of the responsible person
     * Only one of {@link #getIndividualName individualName},
     * {@link #getOrganisationName organisationName} and {@code positionName}
     * should be provided.
     */
    public synchronized void setPositionName(final InternationalString newValue) {
        checkWritePermission();
        positionName = newValue;
    }

    /**
     * Returns the address of the responsible party.
     */
    public Contact getContactInfo() {
        return contactInfo;
    }

    /**
     * Set the address of the responsible party.
     */
    public synchronized void setContactInfo(final Contact newValue) {
        checkWritePermission();
        contactInfo = newValue;
    }

    /**
     * Returns the function performed by the responsible party.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set the function performed by the responsible party.
     */
    public synchronized void setRole(final Role newValue) {
        checkWritePermission();
        role = newValue;
    }
}
