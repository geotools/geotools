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

/**
 * <p>
 * Marker interface indicating that that the function return value can change
 * during a single data access operation even if the argument values provided to
 * it remain constant
 * </p>
 * <p>
 * Very few functions are truly volatile, one example being random(), whose
 * value is going to change over each invocation, even during the same feature
 * collection filtering
 * </p>
 * <p>
 * Functions whose value changes over time but not within the same feature
 * collection filtering are considered to be <em>stable</em> and as such their
 * result can be considered a constant during the single data access operation
 * </p>
 * <p>
 * GeoTools will try to optimize out the stable functions and replace them with
 * a constant that can be easily encoded in whatever native filtering mechanism
 * the datastores have
 * </p>
 * <p>
 * Given the vast majority of function are <em>stable</em> by the above
 * definition only the fews that aren't suitable for constant replacement during
 * a single run against a feature collection should be marked as
 * <em>volatile</em>
 * 
 * @author Andrea Aime - GeoSolutions
 */
public interface VolatileFunction extends Function {

}
