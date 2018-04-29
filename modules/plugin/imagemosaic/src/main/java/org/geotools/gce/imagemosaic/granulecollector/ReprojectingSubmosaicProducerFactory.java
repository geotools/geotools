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

package org.geotools.gce.imagemosaic.granulecollector;

import java.util.Collections;
import java.util.List;
import org.geotools.gce.imagemosaic.RasterLayerRequest;
import org.geotools.gce.imagemosaic.RasterLayerResponse;
import org.geotools.gce.imagemosaic.RasterManager;

/**
 * Creates a SubmosaicProducer that can handle reprojecting granules into the mosaic's target CRS
 */
public class ReprojectingSubmosaicProducerFactory implements SubmosaicProducerFactory {
    @Override
    public List<SubmosaicProducer> createProducers(
            RasterLayerRequest request,
            RasterManager rasterManager,
            RasterLayerResponse response,
            boolean dryRun) {
        return Collections.singletonList(
                new ReprojectingSubmosaicProducer(request, response, rasterManager, dryRun));
    }
}
