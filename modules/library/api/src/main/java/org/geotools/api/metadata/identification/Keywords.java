/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.identification;

import java.util.Collection;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.util.InternationalString;

/**
 * Keywords, their type and reference source.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface Keywords {
    /**
     * Commonly used word(s) or formalised word(s) or phrase(s) used to describe the subject.
     *
     * @return Word(s) or phrase(s) used to describe the subject.
     */
    Collection<? extends InternationalString> getKeywords();

    /**
     * Subject matter used to group similar keywords.
     *
     * @return Subject matter used to group similar keywords, or {@code null}.
     */
    KeywordType getType();

    /**
     * Name of the formally registered thesaurus or a similar authoritative source of keywords.
     *
     * @return Name of registered thesaurus or similar authoritative source of keywords, or {@code
     *     null}.
     */
    Citation getThesaurusName();
}
