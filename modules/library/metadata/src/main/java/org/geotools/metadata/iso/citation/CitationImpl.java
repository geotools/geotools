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
import java.util.Date;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.metadata.citation.PresentationForm;
import org.geotools.api.metadata.citation.ResponsibleParty;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.IdentifierImpl;
import org.geotools.metadata.iso.MetadataEntity;
import org.geotools.util.SimpleInternationalString;

/**
 * Standardized resource reference.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Jody Garnett
 */
public class CitationImpl extends MetadataEntity implements Citation {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -4415559967618358778L;

    /** Name by which the cited resource is known. */
    private InternationalString title;

    /**
     * Short name or other language name by which the cited information is known. Example: "DCW" as an alternative title
     * for "Digital Chart of the World.
     */
    private Collection<InternationalString> alternateTitles;

    /** Version of the cited resource. */
    private InternationalString edition;

    /** Date of the edition in millisecondes ellapsed sine January 1st, 1970, or {@link Long#MIN_VALUE} if none. */
    private long editionDate = Long.MIN_VALUE;

    /** Unique identifier for the resource. Example: Universal Product Code (UPC), National Stock Number (NSN). */
    private Collection<Identifier> identifiers;

    /**
     * Name and position information for an individual or organization that is responsible for the resource. Returns an
     * empty string if there is none.
     */
    private Collection<ResponsibleParty> citedResponsibleParties;

    /** Mode in which the resource is represented, or an empty string if none. */
    private Collection<PresentationForm> presentationForm;

    /**
     * Other information required to complete the citation that is not recorded elsewhere. May be {@code null} if none.
     */
    private InternationalString otherCitationDetails;

    /**
     * Common title with holdings note. Note: title identifies elements of a series collectively, combined with
     * information about what volumes are available at the source cited. May be {@code null} if there is no title.
     */
    private InternationalString collectiveTitle;

    /** International Standard Book Number, or {@code null} if none. */
    private String ISBN;

    /** International Standard Serial Number, or {@code null} if none. */
    private String ISSN;

    /** Constructs an initially empty citation. */
    public CitationImpl() {}

    /**
     * Constructs a new citation initialized to the values specified by the given object. This constructor performs a
     * shallow copy (i.e. each source attributes are reused without copying them).
     */
    public CitationImpl(final Citation source) {
        super(source);
    }

    /**
     * Constructs a citation with the specified title.
     *
     * @param title The title, as a {@link String} or an {@link InternationalString} object.
     */
    public CitationImpl(final CharSequence title) {
        final InternationalString t;
        if (title instanceof InternationalString) {
            t = (InternationalString) title;
        } else {
            t = new SimpleInternationalString(title.toString());
        }
        setTitle(t);
    }

    /**
     * Constructs a citation with the specified responsible party. This convenience constructor initialize the citation
     * title to the first non-null of the following properties: {@linkplain ResponsibleParty#getOrganisationName
     * organisation name}, {@linkplain ResponsibleParty#getPositionName position name} or
     * {@linkplain ResponsibleParty#getIndividualName individual name}.
     *
     * @since 2.2
     */
    public CitationImpl(final ResponsibleParty party) {
        InternationalString title = party.getOrganisationName();
        if (title == null) {
            title = party.getPositionName();
            if (title == null) {
                String name = party.getIndividualName();
                if (name != null) {
                    title = new SimpleInternationalString(name);
                }
            }
        }
        setTitle(title);
        getCitedResponsibleParties().add(party);
    }

    /**
     * Adds the specified identifier as a CRS authority factory. This is used as a convenience method for the creation
     * of constants, and for making sure that all of them use the same identifier type.
     */
    final void addAuthority(final String identifier, final boolean asTitle) {
        if (asTitle) {
            getAlternateTitles().add(new SimpleInternationalString(identifier));
        }
        getIdentifiers().add(new IdentifierImpl(identifier));
    }

    /** Returns the name by which the cited resource is known. */
    @Override
    public InternationalString getTitle() {
        return title;
    }

    /** Set the name by which the cited resource is known. */
    public void setTitle(final InternationalString newValue) {
        checkWritePermission();
        title = newValue;
    }

    /**
     * Returns the short name or other language name by which the cited information is known. Example: "DCW" as an
     * alternative title for "Digital Chart of the World".
     */
    @Override
    public Collection<InternationalString> getAlternateTitles() {
        return alternateTitles = nonNullCollection(alternateTitles, InternationalString.class);
    }

    /** Set the short name or other language name by which the cited information is known. */
    public void setAlternateTitles(final Collection<? extends InternationalString> newValues) {
        alternateTitles = copyCollection(newValues, alternateTitles, InternationalString.class);
    }

    /** Returns the version of the cited resource. */
    @Override
    public InternationalString getEdition() {
        return edition;
    }

    /** Set the version of the cited resource. */
    public void setEdition(final InternationalString newValue) {
        checkWritePermission();
        edition = newValue;
    }

    /** Returns the date of the edition, or {@code null} if none. */
    @Override
    public Date getEditionDate() {
        return editionDate != Long.MIN_VALUE ? new Date(editionDate) : null;
    }

    /**
     * Set the date of the edition, or {@code null} if none.
     *
     * @todo Use an unmodifiable {@link Date} here.
     */
    public void setEditionDate(final Date newValue) {
        checkWritePermission();
        editionDate = newValue != null ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns the unique identifier for the resource. Example: Universal Product Code (UPC), National Stock Number
     * (NSN).
     */
    @Override
    public Collection<Identifier> getIdentifiers() {
        return identifiers = nonNullCollection(identifiers, Identifier.class);
    }

    /**
     * Set the unique identifier for the resource. Example: Universal Product Code (UPC), National Stock Number (NSN).
     */
    public void setIdentifiers(final Collection<? extends Identifier> newValues) {
        identifiers = copyCollection(newValues, identifiers, Identifier.class);
    }

    /**
     * Returns the name and position information for an individual or organization that is responsible for the resource.
     * Returns an empty string if there is none.
     */
    @Override
    public Collection<ResponsibleParty> getCitedResponsibleParties() {
        return citedResponsibleParties = nonNullCollection(citedResponsibleParties, ResponsibleParty.class);
    }

    /**
     * Set the name and position information for an individual or organization that is responsible for the resource.
     * Returns an empty string if there is none.
     */
    public void setCitedResponsibleParties(final Collection<? extends ResponsibleParty> newValues) {
        citedResponsibleParties = copyCollection(newValues, citedResponsibleParties, ResponsibleParty.class);
    }

    /** Returns the mode in which the resource is represented, or an empty string if none. */
    @Override
    public Collection<PresentationForm> getPresentationForm() {
        return presentationForm = nonNullCollection(presentationForm, PresentationForm.class);
    }

    /** Set the mode in which the resource is represented, or an empty string if none. */
    public void setPresentationForm(final Collection<? extends PresentationForm> newValues) {
        presentationForm = copyCollection(newValues, presentationForm, PresentationForm.class);
    }

    /**
     * Returns other information required to complete the citation that is not recorded elsewhere. Returns {@code null}
     * if none.
     */
    @Override
    public InternationalString getOtherCitationDetails() {
        return otherCitationDetails;
    }

    /**
     * Set other information required to complete the citation that is not recorded elsewhere. Set to {@code null} if
     * none.
     */
    public void setOtherCitationDetails(final InternationalString newValue) {
        checkWritePermission();
        otherCitationDetails = newValue;
    }

    /**
     * Returns the common title with holdings note. Note: title identifies elements of a series collectively, combined
     * with information about what volumes are available at the source cited. Returns {@code null} if there is no title.
     */
    @Override
    public InternationalString getCollectiveTitle() {
        return collectiveTitle;
    }

    /**
     * Set the common title with holdings note. Note: title identifies elements of a series collectively, combined with
     * information about what volumes are available at the source cited. Set to {@code null} if there is no title.
     */
    public void setCollectiveTitle(final InternationalString newValue) {
        checkWritePermission();
        collectiveTitle = newValue;
    }

    /** Returns the International Standard Book Number, or {@code null} if none. */
    @Override
    public String getISBN() {
        return ISBN;
    }

    /** Set the International Standard Book Number, or {@code null} if none. */
    public void setISBN(final String newValue) {
        checkWritePermission();
        ISBN = newValue;
    }

    /** Returns the International Standard Serial Number, or {@code null} if none. */
    @Override
    public String getISSN() {
        return ISSN;
    }

    /** Set the International Standard Serial Number, or {@code null} if none. */
    public void setISSN(final String newValue) {
        checkWritePermission();
        ISSN = newValue;
    }
}
