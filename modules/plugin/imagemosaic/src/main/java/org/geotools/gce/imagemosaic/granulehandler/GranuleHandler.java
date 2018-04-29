/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic.granulehandler;

import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/** Handle setting the geometry of the index feature for incoming granules */
public interface GranuleHandler {

    /**
     * Handle the case of a grid coverage being added to the mosaic.
     *
     * @param source input file
     * @param inputReader input reader of the incoming granule
     * @param targetFeature the target index feature
     * @param targetFeatureType the index schema
     * @param inputFeature the incoming granule feature
     * @param inputFeatureType the incoming coverage schema
     * @param mosaicConfiguration the mosaic configuration
     */
    void handleGranule(
            Object source,
            GridCoverage2DReader inputReader,
            SimpleFeature targetFeature,
            SimpleFeatureType targetFeatureType,
            SimpleFeature inputFeature,
            SimpleFeatureType inputFeatureType,
            MosaicConfigurationBean mosaicConfiguration)
            throws GranuleHandlingException;

    default void handleStructuredGranule(
            Object source,
            GridCoverage2DReader inputReader,
            SimpleFeature targetFeature,
            SimpleFeatureType targetFeatureType,
            SimpleFeature inputFeature,
            SimpleFeatureType inputFeatureType,
            MosaicConfigurationBean mosaicConfiguration) {
        Object geometryAttribute =
                inputFeature.getAttribute(inputFeatureType.getGeometryDescriptor().getName());
        targetFeature.setAttribute(
                targetFeatureType.getGeometryDescriptor().getName(), geometryAttribute);
    }
}
