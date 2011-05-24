/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter;

// Annotations
import org.opengis.annotation.XmlElement;


/**
 * Reverses the logical value of an expression.
 * <p>
 * You can check if the Not operation is supported using:<pre><code>
 * scalarCapabilities.hasLogicalOperators() == true
 * </code></pre>
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/Not.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("Not")
public interface Not extends Filter {
    /**
     * The filter to reverse.
     */
    Filter getFilter();
}
