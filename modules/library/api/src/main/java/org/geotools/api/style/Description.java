/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

package org.geotools.api.style;

import org.geotools.api.util.InternationalString;

/**
 * A Description is used to store various informations who describe an element. Description values
 * are mostly used in User Interfaces (Lists, trees, ...).
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface Description {

    /**
     * Human readable title.
     *
     * <p>
     *
     * @return the human readable title.
     */
    InternationalString getTitle();

    void setTitle(InternationalString title);

    /** Define title using the current locale. */
    void setTitle(String title);

    /** Human readable description. */
    InternationalString getAbstract();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    void setAbstract(InternationalString description);

    /**
     * Define description in the current locale.
     *
     * @param description Abstract providing summary of contents
     */
    void setAbstract(String description);

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    void accept(StyleVisitor visitor);
}
