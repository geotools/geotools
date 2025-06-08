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
import net.opengis.ows11.TelephoneType;
import org.geotools.api.metadata.citation.Telephone;
import org.geotools.metadata.iso.MetadataEntity;

/**
 * Telephone numbers for contacting the responsible individual or organization.
 *
 * @author Jody Garnett
 * @author Martin Desruisseaux
 * @since 2.1
 */
public class TelephoneImpl extends MetadataEntity implements Telephone {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 4920157673337669241L;

    /** Telephone numbers by which individuals can speak to the responsible organization or individual. */
    private Collection<String> voices;

    /** Telephone numbers of a facsimile machine for the responsible organization or individual. */
    private Collection<String> facsimiles;

    /** Constructs a default telephone. */
    public TelephoneImpl() {}

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public TelephoneImpl(final Telephone source) {
        super(source);
    }

    public TelephoneImpl(TelephoneType phone) {

        setFacsimiles(Collections.singleton(phone.getFacsimile()));
        setVoices(Collections.singleton(phone.getVoice()));
    }

    /**
     * Returns the telephone numbers by which individuals can speak to the responsible organization or individual.
     *
     * @since 2.4
     */
    @Override
    public Collection<String> getVoices() {
        return voices = nonNullCollection(voices, String.class);
    }

    /**
     * Set the telephone numbers by which individuals can speak to the responsible organization or individual.
     *
     * @since 2.4
     */
    public void setVoices(final Collection<? extends String> newValues) {
        voices = copyCollection(newValues, voices, String.class);
    }

    /**
     * Returns the telephone numbers of a facsimile machine for the responsible organization or individual.
     *
     * @since 2.4
     */
    @Override
    public Collection<String> getFacsimiles() {
        return facsimiles = nonNullCollection(facsimiles, String.class);
    }

    /**
     * Set the telephone number of a facsimile machine for the responsible organization or individual.
     *
     * @since 2.4
     */
    public void setFacsimiles(final Collection<? extends String> newValues) {
        facsimiles = copyCollection(newValues, facsimiles, String.class);
    }
}
