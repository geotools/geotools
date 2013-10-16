/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;

import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;


public interface FootprintGeometryProvider {

    /**
     * Retrieves the footprint from the current granule represenative feature (as it comes from
     * the mosaic index)
     * 
     * @param feature
     * @return
     * @throws IOException
     */
    Geometry getFootprint(SimpleFeature feature) throws IOException;
    
    /**
     * Close up the provider (in case it holds onto persistent resources such as files or
     * database connections)
     */
    void dispose();
}
