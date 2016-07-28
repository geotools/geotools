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
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Granule handler that reprojects envelopes of non-structured grid coverages
 */
public class ReprojectingGranuleHandler implements GranuleHandler {

    private PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING);

    private GeometryFactory GEOM_FACTORY = new GeometryFactory(PRECISION_MODEL);

    @Override
    public void handleGranule(Object source, GridCoverage2DReader inputReader,
            SimpleFeature targetFeature, SimpleFeatureType targetFeatureType,
            SimpleFeature inputFeature, SimpleFeatureType inputFeatureType,
            MosaicConfigurationBean mosaicConfiguration) throws GranuleHandlingException {

        CoordinateReferenceSystem targetCRS = mosaicConfiguration.getCrs();
        if (inputFeature instanceof StructuredGridCoverage2DReader) {
            handleStructuredGranule(source, inputReader, targetFeature, targetFeatureType,
                    inputFeature, inputFeatureType, mosaicConfiguration);
        } else {
            GeneralEnvelope coverageEnvelope = inputReader.getOriginalEnvelope();
            CoordinateReferenceSystem coverageCRS = inputReader.getCoordinateReferenceSystem();
            ReferencedEnvelope finalEnvelope = new ReferencedEnvelope(coverageEnvelope);
            if (!CRS.equalsIgnoreMetadata(targetCRS, coverageCRS)) {
                try {
                    finalEnvelope = new ReferencedEnvelope(CRS.transform(finalEnvelope, targetCRS));
                } catch (TransformException e) {
                    throw new GranuleHandlingException(
                            "Unable to reproject incoming granule, but target and granule CRS "
                                    + "differ so we can't continue",
                            e);
                }
            }

            targetFeature.setAttribute(targetFeatureType.getGeometryDescriptor().getName(),
                    GEOM_FACTORY.toGeometry(finalEnvelope));

        }

    }
}
