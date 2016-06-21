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

package org.geotools.gce.imagemosaic.geomhandler;

import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Default granule geometry handling
 */
public class DefaultGranuleGeometryHandler implements GranuleGeometryHandler {

    private final static PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING);
    private final static GeometryFactory GEOM_FACTORY = new GeometryFactory(PRECISION_MODEL);

    @Override
    public void handleGeometry(GridCoverage2DReader inputReader, SimpleFeature feature,
            SimpleFeatureType indexSchema, MosaicConfigurationBean mosaicConfigurationBean) {
        Envelope coverageEnvelope = inputReader.getOriginalEnvelope();
        feature.setAttribute(indexSchema.getGeometryDescriptor().getLocalName(),
                GEOM_FACTORY.toGeometry(new ReferencedEnvelope(coverageEnvelope)));
    }

    @Override
    public void handleGeometry(StructuredGridCoverage2DReader inputReader,
            SimpleFeature targetFeature, SimpleFeatureType targetFeatureType,
            SimpleFeature inputFeature, SimpleFeatureType inputFeatureType,
            MosaicConfigurationBean mosaicConfiguration) {

        Object geometryAttribute = inputFeature.getAttribute(inputFeatureType.getGeometryDescriptor().getName());
        targetFeature.setAttribute(targetFeatureType.getGeometryDescriptor().getName(), geometryAttribute);
    }
}
