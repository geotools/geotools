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
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;

/** Default granule handling */
public class DefaultGranuleHandler implements GranuleHandler {

    private static final PrecisionModel PRECISION_MODEL =
            new PrecisionModel(PrecisionModel.FLOATING);

    private static final GeometryFactory GEOM_FACTORY = new GeometryFactory(PRECISION_MODEL);

    @Override
    public void handleGranule(
            Object source,
            GridCoverage2DReader inputReader,
            SimpleFeature targetFeature,
            SimpleFeatureType targetFeatureType,
            SimpleFeature feature,
            SimpleFeatureType inputFeatureType,
            MosaicConfigurationBean mosaicConfiguration) {

        if (inputReader instanceof StructuredGridCoverage2DReader) {
            handleStructuredGranule(
                    source,
                    inputReader,
                    targetFeature,
                    targetFeatureType,
                    feature,
                    inputFeatureType,
                    mosaicConfiguration);
        } else {
            Envelope coverageEnvelope = inputReader.getOriginalEnvelope();
            targetFeature.setAttribute(
                    targetFeatureType.getGeometryDescriptor().getLocalName(),
                    GEOM_FACTORY.toGeometry(new ReferencedEnvelope(coverageEnvelope)));
        }
    }
}
