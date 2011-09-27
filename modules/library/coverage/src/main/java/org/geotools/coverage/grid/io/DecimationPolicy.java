/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.coverage.grid.io;

/**
 * Decimation policies.
 *
 * @since 2.7
 *
 *
 * @source $URL$
 * @author Daniele Romagnoli, GeoSolutions SAS.
 * @author Simone Giannecchini, GeoSolutions SAS.
 */
public enum DecimationPolicy {

    /**
     * Allows decimation on reading
     */
    ALLOW,

    /**
     * Disallows decimation on reading
     */
    DISALLOW;
    
    public static DecimationPolicy getDefaultPolicy(){
    	return ALLOW;
    }
}
