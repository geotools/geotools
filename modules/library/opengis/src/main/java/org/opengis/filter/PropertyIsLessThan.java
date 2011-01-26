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
 * Filter operator that checks that its first sub-expression is less than its
 * second subexpression.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("PropertyIsLessThan")
public interface PropertyIsLessThan extends BinaryComparisonOperator {
	/** Operator name used to check FilterCapabilities */
	public static String NAME = "LessThan";
}
