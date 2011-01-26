/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    (C) 2001-2004 EXSE, Department of Geography, University of Bonn
 *                  lat/lon Fitzke/Fretter/Poth GbR
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *   
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.opengis.filter.capability;

import java.util.Collection;

// OpenGIS direct dependencies


/**
 * Indicates support for the named spatial operator.
 * <p>
 * The filter that is supported is indicated by the getName() field, these
 * names matc
 * <ul>
 * <li>A subclass of Filter. Examples include "BBOX" and "Beyond"
 * </ul>
 * Each filter subclass has an associated name, you can use this name to
 * determine if a matching SpatialOperator is defined as part of
 * FilterCapabilities.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 */
public interface SpatialOperator extends Operator {
    /**
     * Returns the geometryOperands.
     */
    Collection<GeometryOperand> getGeometryOperands();
}
