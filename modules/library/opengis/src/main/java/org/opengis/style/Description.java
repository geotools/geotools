/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

package org.opengis.style;

import org.opengis.annotation.Extension;
import org.opengis.annotation.XmlElement;
import org.opengis.util.InternationalString;

/**
 * A Description is used to store various informations
 * who describe an element. Description values are mostly used
 * in User Interfaces (Lists, trees, ...).
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/Description.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@XmlElement("Description")
public interface Description {

    /**
     * Returns the human readable title of this style.
     * This can be any string, but should be fairly short as it is intended to
     * be used in list boxes or drop down menus or other selection interfaces.
     * @return the human readable title of this style.
     */
    @XmlElement("Title")
    InternationalString getTitle();

    /**
     * Returns a human readable, prose description of this style.
     * This can be any string and can consist of any amount of text.
     * @return a human readable, prose description of this style.
     */
    @XmlElement("Abstract")
    InternationalString getAbstract();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}

