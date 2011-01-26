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

// OpenGIS direct dependencies


/**
 * Marker interface for spatial operators that are a subset of the BBOX relationship.
 * <p>
 * This interface can be used to quickly check when an BBox optimization is applicable.
 * </p>
 * @author Jody Garnett, Refractions Research
 * @since GeoAPI 2.1
 */
public interface BoundedSpatialOperator extends SpatialOperator {

}
