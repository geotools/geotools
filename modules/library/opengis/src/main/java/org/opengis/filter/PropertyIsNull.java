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

// OpenGIS direct dependencies
import org.opengis.annotation.XmlElement;
import org.opengis.filter.expression.Expression;


/**
 * Filter operator that checks if an expression's value is {@code null}.  A {@code null}
 * is equivalent to no value present. The value 0 is a valid value and is not considered
 * {@code null}.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/PropertyIsNull.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("PropertyIsNull")
public interface PropertyIsNull extends Filter {
	/** Operator name used to check FilterCapabilities */
	public static String NAME = "NullCheck";
    /**
     * Returns the expression whose value will be checked for {@code null}.
     */
    @XmlElement("PropertyName")
    Expression getExpression();

}
