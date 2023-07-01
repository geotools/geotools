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
     * Returns the human readable title of this style. This can be any string, but should be fairly
     * short as it is intended to be used in list boxes or drop down menus or other selection
     * interfaces.
     *
     * @return the human readable title of this style.
     */
    InternationalString getTitle();

    /**
     * Returns a human readable, prose description of this style. This can be any string and can
     * consist of any amount of text.
     *
     * @return a human readable, prose description of this style.
     */
    InternationalString getAbstract();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(StyleVisitor visitor, Object extraData);
}
