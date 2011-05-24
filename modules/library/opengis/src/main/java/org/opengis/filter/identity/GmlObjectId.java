/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.identity;

import org.opengis.annotation.XmlElement;


/**
 * Feature and Geometry identifier for GML3 specification.
 * <p>
 * GML3 constructs are are identified with a String, commonly referred to as a "id".
 * </p>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/identity/GmlObjectId.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @author Justin Deoliveira (The Open Planning Project)
 * @since GeoAPI 2.0
 */
@XmlElement("GMLObjectId")
public interface GmlObjectId extends Identifier {
    /**
     * The identifier value, which is a string.
     */
    @XmlElement("id")
    String getID();

    /**
     * Evaluates the identifer value against the given GML3 construct.
     *
     * @param feature The GML3 construct to be tested.
     * @return {@code true} if a match, otherwise {@code false}.
     */
    boolean matches(Object object);
}
