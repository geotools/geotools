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
package org.geotools.metadata.iso.identification;

import java.util.Collection;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.identification.Keywords;
import org.opengis.metadata.identification.KeywordType;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Keywords, their type and reference source.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class KeywordsImpl extends MetadataEntity implements Keywords {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 48691634443678266L;

    /**
     * Commonly used word(s) or formalised word(s) or phrase(s) used to describe the subject.
     */
    private Collection<InternationalString> keywords;

    /**
     * Subject matter used to group similar keywords.
     */
    private KeywordType type;

    /**
     * Name of the formally registered thesaurus or a similar authoritative source of keywords.
     */
    private Citation thesaurusName;

    /**
     * Constructs an initially empty keywords.
     */
    public KeywordsImpl() {
        super();
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public KeywordsImpl(final Keywords source) {
        super(source);
    }

    /**
     * Creates keywords initialized to the given list.
     */
    public KeywordsImpl(final Collection<? extends InternationalString> keywords) {
        setKeywords(keywords);
    }

    /**
     * Commonly used word(s) or formalised word(s) or phrase(s) used to describe the subject.
     */
    public synchronized Collection<InternationalString> getKeywords() {
        return keywords = nonNullCollection(keywords, InternationalString.class);
    }

    /**
     * Set commonly used word(s) or formalised word(s) or phrase(s) used to describe the subject.
     */
    public synchronized void setKeywords(final Collection<? extends InternationalString> newValues) {
        keywords = copyCollection(newValues, keywords, InternationalString.class);
    }

    /**
     * Subject matter used to group similar keywords.
     */
    public KeywordType getType() {
        return type;
    }

    /**
     * Set the subject matter used to group similar keywords.
     */
    public synchronized void setType(final KeywordType newValue) {
        checkWritePermission();
        type = newValue;
    }

    /**
     * Name of the formally registered thesaurus or a similar authoritative source of keywords.
     */
    public Citation getThesaurusName() {
        return thesaurusName;
    }

    /**
     * Set the name of the formally registered thesaurus or a similar authoritative source
     * of keywords.
     */
    public synchronized void setThesaurusName(final Citation newValue) {
        checkWritePermission();
        thesaurusName = newValue;
    }
}
