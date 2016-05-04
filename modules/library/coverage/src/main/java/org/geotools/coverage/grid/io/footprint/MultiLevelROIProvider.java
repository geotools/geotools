/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.footprint;

import java.io.IOException;

import org.opengis.feature.simple.SimpleFeature;

/**
 * Provider used for generating a {@link MultiLevelROI} object from a granule
 */
public interface MultiLevelROIProvider {

    /**
     * Returns a {@link MultiLevelROI} object from a granule
     * 
     * @param sf {@link SimpleFeature} related to a granule (if several are available). 
     *     Specifying a null feature should return the default provider. 
     * @return a {@link MultiLevelROI} object associated to the input SimpleFeature
     * @throws IOException
     */
    public MultiLevelROI getMultiScaleROI(SimpleFeature sf) throws IOException;

    /**
     * Optional method to call for disposing the Provider
     */
    public void dispose();

}
