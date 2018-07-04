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
package org.geotools.gce.imagemosaic.catalog;

import it.geosolutions.imageio.maskband.DatasetLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIRaster;
import org.geotools.coverage.grid.io.footprint.SidecarFootprintProvider;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * {@link MultiLevelROIProvider} implementation returning a {@link MultiLevelROIRaster} instance
 *
 * @author Nicola Lagomarsini
 */
public class MultiLevelROIRasterProvider implements MultiLevelROIProvider {

    /** Logger used for logging exceptions */
    static final Logger LOGGER = Logging.getLogger(SidecarFootprintProvider.class);

    /** Mosaic Folder used as root folder */
    private File mosaicFolder;

    public MultiLevelROIRasterProvider(File mosaicFolder) {
        this.mosaicFolder = mosaicFolder;
    }

    @Override
    public MultiLevelROI getMultiScaleROI(SimpleFeature sf) throws IOException {
        if (sf == null) {
            // Feature is not defined
            return null;
        }
        // Extracting File from feature
        Object value = sf.getAttribute("location");
        if (value != null && value instanceof String) {
            String strValue = (String) value;
            File file = Utils.getFile(strValue, mosaicFolder);
            MultiLevelROI result = null;
            if (file.exists() && file.canRead()) {
                try {
                    // When looking for formats which may parse this file, make sure to exclude the
                    // ImageMosaicFormat as return
                    AbstractGridFormat format =
                            (AbstractGridFormat)
                                    GridFormatFinder.findFormat(file, Utils.EXCLUDE_MOSAIC_HINTS);
                    AbstractGridCoverage2DReader reader = format.getReader(file);
                    // Getting Dataset Layout
                    DatasetLayout layout = reader.getDatasetLayout();
                    // If present use it
                    if (layout != null) {
                        // Getting Total Number of masks
                        int numExternalMasks =
                                layout.getNumExternalMasks() > 0 ? layout.getNumExternalMasks() : 0;
                        int numInternalMasks =
                                layout.getNumInternalMasks() > 0 ? layout.getNumInternalMasks() : 0;
                        int numExternalMaskOverviews =
                                layout.getNumExternalMaskOverviews() > 0
                                        ? layout.getNumExternalMaskOverviews()
                                        : 0;
                        int totalMasks =
                                numExternalMasks + numInternalMasks + numExternalMaskOverviews;

                        // Check if masks are present
                        // NOTE No Mask: Outside ROI
                        if (totalMasks > 0) {
                            CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();
                            final SimpleFeatureType indexSchema = sf.getFeatureType();
                            if (crs != null
                                    && indexSchema != null
                                    && indexSchema.getCoordinateReferenceSystem() != null
                                    && !CRS.equalsIgnoreMetadata(
                                            crs, indexSchema.getCoordinateReferenceSystem())) {
                                // do not trust the feature footprint, it's reprojected
                                ReferencedEnvelope envelope =
                                        ReferencedEnvelope.reference(reader.getOriginalEnvelope());
                                Polygon nativeFootprint = JTS.toGeometry(envelope);
                                SimpleFeatureType ftNative =
                                        FeatureTypes.transform(
                                                indexSchema, reader.getCoordinateReferenceSystem());
                                SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ftNative);
                                fb.init(sf);
                                fb.set(
                                        indexSchema.getGeometryDescriptor().getLocalName(),
                                        nativeFootprint);
                                SimpleFeature nativeFeature = fb.buildFeature(sf.getID());
                                return new MultiLevelROIRaster(layout, file, nativeFeature);
                            } else {
                                return new MultiLevelROIRaster(layout, file, sf);
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new IOException(
                            "Failed to load the footprint for granule " + strValue, e);
                }
            }
            return result;
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Could not use the location attribute value to search for "
                                + "the file, the value was: "
                                + value);
            }
            return null;
        }
    }

    @Override
    public void dispose() {}
}
