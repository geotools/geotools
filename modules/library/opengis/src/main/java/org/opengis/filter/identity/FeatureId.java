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
 * Feature identifier.
 * Features are identified as strings.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @author Justin Deoliveira (The Open Planning Project)
 * @since GeoAPI 2.0
 */
@XmlElement("FeatureId")
public interface FeatureId extends Identifier {
    /**
     * The identifier value, which is a string.
     */
    @XmlElement("fid")
    String getID();

    /**
     * Evaluates the identifer value against the given feature.
     *
     * @param feature The feature to be tested.
     * @return {@code true} if a match, otherwise {@code false}.
     */
    boolean matches(Object feature);
}
