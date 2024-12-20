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
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIRaster;
import org.geotools.coverage.grid.io.footprint.SidecarFootprintProvider;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.gce.imagemosaic.DefaultGranuleAccessProvider;
import org.geotools.gce.imagemosaic.GranuleAccessProvider;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Polygon;

/**
 * {@link MultiLevelROIProvider} implementation returning a {@link MultiLevelROIRaster} instance
 *
 * @author Nicola Lagomarsini
 */
public class MultiLevelROIRasterProvider implements MultiLevelROIProvider {

    /** Logger used for logging exceptions */
    static final Logger LOGGER = Logging.getLogger(SidecarFootprintProvider.class);

    private final Hints hints;

    /** Mosaic Folder used as root folder */
    private File mosaicFolder;

    public MultiLevelROIRasterProvider(File mosaicFolder, Hints hints) {
        this.mosaicFolder = mosaicFolder;
        this.hints = hints;
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
            GranuleAccessProvider granuleProvider = getGranuleAccessProvider(strValue);
            MultiLevelROI result = null;
            if (granuleProvider != null) {
                try {
                    // Getting Dataset Layout
                    AbstractGridCoverage2DReader reader = granuleProvider.getGridCoverageReader();
                    DatasetLayout layout = reader.getDatasetLayout();
                    // If present use it
                    if (layout != null) {
                        // Getting Total Number of masks
                        int numExternalMasks = layout.getNumExternalMasks() > 0 ? layout.getNumExternalMasks() : 0;
                        int numInternalMasks = layout.getNumInternalMasks() > 0 ? layout.getNumInternalMasks() : 0;
                        int numExternalMaskOverviews =
                                layout.getNumExternalMaskOverviews() > 0 ? layout.getNumExternalMaskOverviews() : 0;
                        int totalMasks = numExternalMasks + numInternalMasks + numExternalMaskOverviews;

                        // Check if masks are present
                        // NOTE No Mask: Outside ROI
                        if (totalMasks > 0) {
                            CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();
                            final SimpleFeatureType indexSchema = sf.getFeatureType();
                            SimpleFeature nativeFeature = sf;
                            if (crs != null
                                    && indexSchema != null
                                    && indexSchema.getCoordinateReferenceSystem() != null
                                    && !CRS.equalsIgnoreMetadata(crs, indexSchema.getCoordinateReferenceSystem())) {
                                // do not trust the feature footprint, it's reprojected
                                ReferencedEnvelope envelope =
                                        ReferencedEnvelope.reference(reader.getOriginalEnvelope());
                                Polygon nativeFootprint = JTS.toGeometry(envelope);
                                SimpleFeatureType ftNative =
                                        FeatureTypes.transform(indexSchema, reader.getCoordinateReferenceSystem());
                                SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ftNative);
                                fb.init(sf);
                                fb.set(indexSchema.getGeometryDescriptor().getLocalName(), nativeFootprint);
                                nativeFeature = fb.buildFeature(sf.getID());
                            }

                            return new MultiLevelROIRaster(
                                    layout, granuleProvider.getMaskOverviewsProvider(), nativeFeature);
                        }
                    }
                } catch (Exception e) {
                    throw new IOException("Failed to load the footprint for granule " + strValue, e);
                }
            }
            return result;
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Could not use the location attribute value to search for "
                        + "the file, the value was: "
                        + value);
            }
            return null;
        }
    }

    private GranuleAccessProvider getGranuleAccessProvider(String location) throws IOException {
        Object providerHint = Utils.getHintIfAvailable(hints, GranuleAccessProvider.GRANULE_ACCESS_PROVIDER);
        GranuleAccessProvider provider;
        if (providerHint != null) {
            provider = ((GranuleAccessProvider) providerHint).copyProviders();
            provider.setGranuleInput(location);
        } else {
            File file = Utils.getFile(location, mosaicFolder);
            if (file == null) return null;

            provider = new DefaultGranuleAccessProvider(hints);
            provider.setGranuleInput(URLs.fileToUrl(file));
        }

        return provider;
    }

    @Override
    public void dispose() {}
}
