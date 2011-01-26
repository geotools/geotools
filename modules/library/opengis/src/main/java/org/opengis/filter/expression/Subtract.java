/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.expression;

// Annotations
import org.opengis.annotation.XmlElement;


/**
 * Encodes the operation of subtraction where the second argument is subtracted from the first.
 * <p>
 * Instances of this interface implement their {@link #evaluate evaluate} method by
 * computing the numeric difference between the {@linkplain #getExpression1 first} and
 * {@linkplain #getExpression2 second} operand.
 * </p>
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("Sub")
public interface Subtract extends BinaryExpression {
	/** Operator name used to check FilterCapabilities */
	public static String NAME = "Sub";
}
