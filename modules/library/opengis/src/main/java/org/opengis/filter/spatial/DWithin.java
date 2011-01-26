/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.spatial;

// Annotations
import org.opengis.annotation.XmlElement;


/**
 * Concrete {@linkplain DistanceBufferOperator distance buffer operator} that evaluates as
 * true when any part of the first geometry lies within the given distance
 * of the second geometry.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("DWithin")
public interface DWithin extends DistanceBufferOperator {
	/** Operator name used to check FilterCapabilities */
	public static String NAME = "DWithin";
}
