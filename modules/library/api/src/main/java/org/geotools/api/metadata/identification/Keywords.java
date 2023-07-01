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

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19115;

import java.util.Collection;
import org.geotools.api.annotation.UML;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.util.InternationalString;

/**
 * Keywords, their type and reference source.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "MD_Keywords", specification = ISO_19115)
public interface Keywords {
    /**
     * Commonly used word(s) or formalised word(s) or phrase(s) used to describe the subject.
     *
     * @return Word(s) or phrase(s) used to describe the subject.
     */
    @UML(identifier = "keyword", obligation = MANDATORY, specification = ISO_19115)
    Collection<? extends InternationalString> getKeywords();

    /**
     * Subject matter used to group similar keywords.
     *
     * @return Subject matter used to group similar keywords, or {@code null}.
     */
    @UML(identifier = "type", obligation = OPTIONAL, specification = ISO_19115)
    KeywordType getType();

    /**
     * Name of the formally registered thesaurus or a similar authoritative source of keywords.
     *
     * @return Name of registered thesaurus or similar authoritative source of keywords, or {@code
     *     null}.
     */
    @UML(identifier = "thesaurusName", obligation = OPTIONAL, specification = ISO_19115)
    Citation getThesaurusName();
}
